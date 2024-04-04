package com.ryangar46.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class HungerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("hunger")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("food", IntegerArgumentType.integer(0))
                                                .executes(context -> setFood(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        IntegerArgumentType.getInteger(context, "food"))
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("exhaustion")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("exhaustion", FloatArgumentType.floatArg(0.0f))
                                                .executes(context -> setExhaustion(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "exhaustion"))
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("saturation")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("saturation", FloatArgumentType.floatArg(0.0f))
                                                .executes(context -> setSaturation(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "saturation"))
                                                )
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("add")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("food", IntegerArgumentType.integer())
                                                .executes(context -> addFood(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        IntegerArgumentType.getInteger(context, "food"))
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("exhaustion")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("exhaustion", FloatArgumentType.floatArg())
                                                .executes(context -> setExhaustion(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "exhaustion"))
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("saturation")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.argument("saturation", FloatArgumentType.floatArg())
                                                .executes(context -> setSaturation(
                                                        context.getSource(),
                                                        EntityArgumentType.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "saturation"))
                                                )
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("query")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> queryFood(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                        )
                        .then(CommandManager.literal("exhaustion")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> queryExhaustion(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                        )
                        .then(CommandManager.literal("saturation")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> querySaturation(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                        )
                )
        );
    }

    private static int setFood(ServerCommandSource source, Collection<ServerPlayerEntity> players, int food) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setFoodLevel(food);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.set.food.success", food, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.set.food.fail")).create();
        }

        return i;
    }

    private static int setExhaustion(ServerCommandSource source, Collection<ServerPlayerEntity> players, float exhaustion) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setExhaustion(exhaustion);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.set.exhaustion.success", exhaustion, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.set.exhaustion.fail")).create();
        }

        return i;
    }

    private static int setSaturation(ServerCommandSource source, Collection<ServerPlayerEntity> players, float saturation) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setSaturationLevel(saturation);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.set.saturation.success", saturation, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.set.saturation.fail")).create();
        }

        return i;
    }

    private static int addFood(ServerCommandSource source, Collection<ServerPlayerEntity> players, int food) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setFoodLevel(food + hungerManager.getFoodLevel());
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.add.food.success", food, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.add.food.fail")).create();
        }

        return i;
    }

    private static int addExhaustion(ServerCommandSource source, Collection<ServerPlayerEntity> players, float exhaustion) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setExhaustion(exhaustion + hungerManager.getExhaustion());
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.add.exhaustion.success", exhaustion, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.add.exhaustion.fail")).create();
        }

        return i;
    }

    private static int addSaturation(ServerCommandSource source, Collection<ServerPlayerEntity> players, float saturation) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setSaturationLevel(saturation + hungerManager.getSaturationLevel());
            i++;
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.hunger.add.saturation.success", saturation, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.hunger.add.saturation.fail")).create();
        }

        return i;
    }

    private static int queryFood(ServerCommandSource source, ServerPlayerEntity player) {
        int hunger = player.getHungerManager().getFoodLevel();
        source.sendFeedback(Text.translatable("command.hunger.query.food.success", hunger), false);
        return hunger > 0 ? 1 : 0;
    }

    private static int queryExhaustion(ServerCommandSource source, ServerPlayerEntity player) {
        float exhaustion = player.getHungerManager().getExhaustion();
        source.sendFeedback(Text.translatable("command.hunger.query.exhaustion.success", exhaustion), false);
        return exhaustion > 0 ? 1 : 0;
    }

    private static int querySaturation(ServerCommandSource source, ServerPlayerEntity player) {
        float saturation = player.getHungerManager().getSaturationLevel();
        source.sendFeedback(Text.translatable("command.hunger.query.saturation.success", saturation), false);
        return saturation > 0 ? 1 : 0;
    }
}
