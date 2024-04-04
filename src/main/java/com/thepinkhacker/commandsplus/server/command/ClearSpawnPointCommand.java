package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;

public class ClearSpawnPointCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("clearspawnpoint")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .executes(context -> clearSpawnPoint(
                                context.getSource(),
                                EntityArgumentType.getPlayers(context, "targets"))
                        )
                )
                .executes(context -> clearSpawnPoint(context.getSource()))
        );
    }

    private static int clearSpawnPoint(ServerCommandSource source, Collection<ServerPlayerEntity> players) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.setSpawnPoint(World.OVERWORLD, null, 0, false, false);
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.clearspawnpoint.success", finalI), true);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.clearspawnpoint.fail")).create();
        }

        return i;
    }

    private static int clearSpawnPoint(ServerCommandSource source) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return clearSpawnPoint(source, players);
    }
}
