package com.thepinkhacker.decree.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.Decree;
import com.thepinkhacker.decree.data.command.CommandConfig;
import com.thepinkhacker.decree.registry.DecreeRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Optional;
import java.util.function.Function;

public class DecreeUtils {
    public static LiteralCommandNode<ServerCommandSource> register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistryKey<CommandConfig> key,
            Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> command
    ) {
        return register(dispatcher, key, 0, command);
    }

    public static LiteralCommandNode<ServerCommandSource> register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            RegistryKey<CommandConfig> key,
            int permissionLevel,
            Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> command
    ) {
        // TODO: Check for collisions
        LiteralArgumentBuilder<ServerCommandSource> builtCommand = command.apply(CommandManager.literal(key.getValue().getPath()));
        CommandConfig config = DecreeRegistries.COMMAND_CONFIG.getValueOrThrow(key);

        if (config.prefix.optional) {
            dispatcher.register(builtCommand);
        }

        if (permissionLevel == 0) {
            for (String alias : config.aliases) {
                createAlias(dispatcher, builtCommand.getLiteral(), alias);
            }
        } else {
            for (String alias : config.aliases) {
                createAlias(dispatcher, builtCommand.getLiteral(), alias, permissionLevel);
            }
        }

        return dispatcher.register(CommandManager.literal(config.prefix.prefix).then(builtCommand));
    }

    // TODO: Fix issue with blank aliases
    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, String original, String alias) {
        dispatcher.register(CommandManager.literal(alias).redirect(dispatcher.getRoot().getChild(original)));
    }

    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, String original, String alias, int permissionLevel) {
        dispatcher.register(CommandManager.literal(alias)
                .redirect(dispatcher.getRoot().getChild(original))
                .requires(source -> source.hasPermissionLevel(permissionLevel))
        );
    }

    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> original, String alias) {
        dispatcher.register(CommandManager.literal(alias).redirect(original));
    }

    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> original, String alias, int permissionLevel) {
        dispatcher.register(CommandManager.literal(alias)
                .redirect(original)
                .requires(source -> source.hasPermissionLevel(permissionLevel))
        );
    }
}
