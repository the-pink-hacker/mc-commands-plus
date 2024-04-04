package com.ryangar46.commandsplus.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

public class PathArgumentType implements ArgumentType<Path> {
    private static final Collection<String> EXAMPLES = Arrays.asList("saves/World", "resourcepacks/pack/pack.mcmeta");
    private static final DynamicCommandExceptionType PATH_NOT_FOUND_EXCEPTION = new DynamicCommandExceptionType(path -> Text.translatable("argument.path.not_found", path));
    private static final DynamicCommandExceptionType PATH_OUT_OF_GAME_DIRECTORY_EXCEPTION = new DynamicCommandExceptionType(path -> Text.translatable("argument.path.out_of_game_directory", path));

    public static PathArgumentType path() {
        return new PathArgumentType();
    }

    public static Path getPath(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Path.class);
    }

    @Override
    public Path parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        Path path = Path.of(string);

        Path minecraftPath = FabricLoader.getInstance().getGameDir();

        try {
            if (!path.toRealPath().startsWith(minecraftPath)) throw PATH_OUT_OF_GAME_DIRECTORY_EXCEPTION.create(path);
        } catch (IOException ignored) {}

        if (Files.isDirectory(path) || Files.exists(path)) return path;
        throw PATH_NOT_FOUND_EXCEPTION.create(path);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
