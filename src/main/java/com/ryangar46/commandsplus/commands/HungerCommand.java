package com.ryangar46.commandsplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class HungerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("hunger")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("target", EntityArgumentType.players())
                        .then(CommandManager.literal("get")
                                .executes((context) -> {
                                    return getHunger(context.getSource(), EntityArgumentType.getPlayer(context, "target")); }))
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("hunger", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                        .executes((context) -> {
                                            return setHunger(context.getSource(), EntityArgumentType.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "hunger")); })))
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("hunger", IntegerArgumentType.integer(-Integer.MAX_VALUE, Integer.MAX_VALUE))
                                        .executes((context) -> {
                                            return addHunger(context.getSource(), EntityArgumentType.getPlayers(context, "target"), IntegerArgumentType.getInteger(context, "hunger")); })))));
    }

    private static int setHunger(ServerCommandSource source, Collection<? extends ServerPlayerEntity> target, int hunger) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : target) {
            player.getHungerManager().setFoodLevel(hunger);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.hunger.set.success", i, hunger), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.hunger.set.fail")).create();
        }

        return i;
    }

    private static int getHunger(ServerCommandSource source, ServerPlayerEntity target) throws CommandSyntaxException {
        int hunger = -1;

        hunger = target.getHungerManager().getFoodLevel();

        if (hunger >= 0) {
            source.sendFeedback(new TranslatableText("command.hunger.get.success", hunger), false);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.hunger.get.fail")).create();
        }

        return hunger;
    }

    private static int addHunger(ServerCommandSource source, Collection<? extends ServerPlayerEntity> target, int hunger) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : target) {
            player.getHungerManager().setFoodLevel(player.getHungerManager().getFoodLevel() + hunger);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.hunger.add.success", hunger, i), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.hunger.add.fail")).create();
        }

        return i;
    }
}
