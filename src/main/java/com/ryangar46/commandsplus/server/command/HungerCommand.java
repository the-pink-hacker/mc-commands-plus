package com.ryangar46.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class HungerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // Todo: Allow this to work for saturation and exhaustion too
        dispatcher.register(CommandManager.literal("hunger")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("hunger", IntegerArgumentType.integer(0))
                                        .executes(context -> setHunger(
                                                context.getSource(),
                                                EntityArgumentType.getPlayers(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "hunger"))
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("hunger", IntegerArgumentType.integer())
                                        .executes(context -> addHunger(
                                                context.getSource(),
                                                EntityArgumentType.getPlayers(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "hunger"))
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("query")
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(context -> queryHunger(
                                        context.getSource(),
                                        EntityArgumentType.getPlayer(context, "target"))
                                )
                        )
                )
        );
    }

    private static int setHunger(ServerCommandSource source, Collection<ServerPlayerEntity> players, int hunger) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setFoodLevel(hunger);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.set.success", hunger, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.set.fail")).create();
        }

        return i;
    }

    private static int addHunger(ServerCommandSource source, Collection<ServerPlayerEntity> players, int hunger) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setFoodLevel(hunger + hungerManager.getFoodLevel());
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.add.success", hunger, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.add.fail")).create();
        }

        return i;
    }

    private static int queryHunger(ServerCommandSource source, ServerPlayerEntity player) {
        int hunger = player.getHungerManager().getFoodLevel();
        source.sendFeedback(Text.translatable("command.hunger.query.success", hunger), false);
        return hunger > 0 ? 1 : 0;
    }
}
