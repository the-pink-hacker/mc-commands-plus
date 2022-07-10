package com.ryangar46.commandsplus.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.ryangar46.commandsplus.world.GameRulePreset;
import net.minecraft.server.command.ServerCommandSource;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

public class GameRulePresetArgumentType extends PathArgumentType {
    private static final Collection<String> EXAMPLES = Arrays.asList("default.json", "redstone_world.json");

    public static GameRulePresetArgumentType preset() {
        return new GameRulePresetArgumentType();
    }

    public static Path getPreset(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Path.class);
    }

    @Override
    public Path getStartingPath() {
        return GameRulePreset.GAMERULE_PRESET_PATH;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public boolean mustExist() {
        return false;
    }
}
