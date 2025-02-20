package com.thepinkhacker.decree.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.data.command.CommandConfig;
import com.thepinkhacker.decree.registry.DecreeRegistries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Function;

public class DecreeUtils {
    public static LiteralCommandNode<ServerCommandSource> register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistryKey<CommandConfig> key,
            Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> command
    ) {
        // TODO: Check for collisions
        LiteralArgumentBuilder<ServerCommandSource> builtCommand = command.apply(CommandManager.literal(key.getValue().getPath()));
        CommandConfig config = DecreeRegistries.COMMAND_CONFIG.getValueOrThrow(key);

        if (config.prefix.optional) {
            dispatcher.register(builtCommand);
        }

        return dispatcher.register(CommandManager.literal(config.prefix.prefix).then(builtCommand));
    }
}
