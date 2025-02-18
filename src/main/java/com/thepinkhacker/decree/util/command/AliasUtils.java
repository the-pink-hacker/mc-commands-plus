package com.thepinkhacker.decree.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class AliasUtils {
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

    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> original, String alias,  int permissionLevel) {
        dispatcher.register(CommandManager.literal(alias)
                .redirect(original)
                .requires(source -> source.hasPermissionLevel(permissionLevel))
        );
    }
}
