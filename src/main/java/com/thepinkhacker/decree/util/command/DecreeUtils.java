package com.thepinkhacker.decree.util.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.Decree;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Consumer;
import java.util.function.Function;

public class DecreeUtils {
    public static LiteralCommandNode<ServerCommandSource> register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            String id,
            Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> command
    ) {
        return register(dispatcher, id, false, command);
    }

    public static LiteralCommandNode<ServerCommandSource> register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            String id,
            boolean prefix,
            Function<LiteralArgumentBuilder<ServerCommandSource>, LiteralArgumentBuilder<ServerCommandSource>> command
    ) {
        LiteralArgumentBuilder<ServerCommandSource> builtCommand = command.apply(CommandManager.literal(id));
        return dispatcher.register(prefix ? CommandManager.literal(Decree.MOD_ID).then(builtCommand) : builtCommand);
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

    public static void createAlias(CommandDispatcher<ServerCommandSource> dispatcher, LiteralCommandNode<ServerCommandSource> original, String alias,  int permissionLevel) {
        dispatcher.register(CommandManager.literal(alias)
                .redirect(original)
                .requires(source -> source.hasPermissionLevel(permissionLevel))
        );
    }
}
