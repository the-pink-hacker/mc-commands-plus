package com.thepinkhacker.decree.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.thepinkhacker.decree.world.GameRulePreset;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class GameRulePresetArgumentType implements ArgumentType<Path> {
    private static final Collection<String> EXAMPLES = Arrays.asList("default", "redstone", "creative");

    public static GameRulePresetArgumentType preset() {
        return new GameRulePresetArgumentType();
    }

    public static Path getPreset(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Path.class);
    }

    @Override
    public Path parse(StringReader reader) {
        String string = reader.readUnquotedString();
        return Path.of(GameRulePreset.GAMERULE_PRESET_PATH.toString(), string + ".json");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String[] files = GameRulePreset.GAMERULE_PRESET_PATH.toFile().list();
        Collection<String> presets = new ArrayList<>();

        if (files != null) {
            for (String file : files) {
                if (FilenameUtils.isExtension(file, "json")) {
                    String preset = FilenameUtils.getBaseName(file);
                    presets.add(preset);
                }
            }

            return CommandSource.suggestMatching(presets, builder);
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
