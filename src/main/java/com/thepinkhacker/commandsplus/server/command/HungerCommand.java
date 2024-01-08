package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.commandsplus.util.command.AliasUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class HungerCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(CommandManager.literal("hunger")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("food", IntegerArgumentType.integer(0))
                                        .executes(context -> setFood(
                                                context.getSource(),
                                                IntegerArgumentType.getInteger(context, "food"))
                                        )
                                )
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
                                .then(CommandManager.argument("exhaustion", FloatArgumentType.floatArg(0.0f))
                                        .executes(context -> setExhaustion(
                                                context.getSource(),
                                                FloatArgumentType.getFloat(context, "exhaustion"))
                                        )
                                )
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
                                .then(CommandManager.argument("saturation", FloatArgumentType.floatArg(0.0f))
                                        .executes(context -> setSaturation(
                                                context.getSource(),
                                                FloatArgumentType.getFloat(context, "saturation"))
                                        )
                                )
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
                        .then(CommandManager.argument("food", IntegerArgumentType.integer(0))
                                .executes(context -> setFood(
                                        context.getSource(),
                                        IntegerArgumentType.getInteger(context, "food"))
                                )
                        )
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
                .then(CommandManager.literal("add")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("food", IntegerArgumentType.integer())
                                        .executes(context -> addFood(
                                                context.getSource(),
                                                IntegerArgumentType.getInteger(context, "food"))
                                        )
                                )
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
                                .then(CommandManager.argument("exhaustion", FloatArgumentType.floatArg())
                                        .executes(context -> setExhaustion(
                                                context.getSource(),
                                                FloatArgumentType.getFloat(context, "exhaustion"))
                                        )
                                )
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
                                .then(CommandManager.argument("saturation", FloatArgumentType.floatArg())
                                        .executes(context -> setSaturation(
                                                context.getSource(),
                                                FloatArgumentType.getFloat(context, "saturation"))
                                        )
                                )
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
                        .then(CommandManager.argument("food", IntegerArgumentType.integer())
                                .executes(context -> addFood(
                                        context.getSource(),
                                        IntegerArgumentType.getInteger(context, "food"))
                                )
                        )
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
                .then(CommandManager.literal("query")
                        .then(CommandManager.literal("food")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> queryFood(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                                .executes(context -> queryFood(context.getSource()))
                        )
                        .then(CommandManager.literal("exhaustion")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> queryExhaustion(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                                .executes(context -> queryExhaustion(context.getSource()))
                        )
                        .then(CommandManager.literal("saturation")
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(context -> querySaturation(
                                                context.getSource(),
                                                EntityArgumentType.getPlayer(context, "target"))
                                        )
                                )
                                .executes(context -> querySaturation(context.getSource()))
                        )
                        .executes(context -> queryFood(context.getSource()))
                )
        );

        AliasUtils.createAlias(dispatcher, node, "food");
    }

    private static int setFood(ServerCommandSource source, Collection<ServerPlayerEntity> players, int food) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setFoodLevel(food);
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.set.food.success", food, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.set.food.fail")).create();
        }

        return i;
    }

    private static int setFood(ServerCommandSource source, int food) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return setFood(source, players, food);
    }

    private static int setExhaustion(ServerCommandSource source, Collection<ServerPlayerEntity> players, float exhaustion) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setExhaustion(exhaustion);
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.set.exhaustion.success", exhaustion, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.set.exhaustion.fail")).create();
        }

        return i;
    }

    private static int setExhaustion(ServerCommandSource source, float exhaustion) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return setExhaustion(source, players, exhaustion);
    }

    private static int setSaturation(ServerCommandSource source, Collection<ServerPlayerEntity> players, float saturation) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            player.getHungerManager().setSaturationLevel(saturation);
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.set.saturation.success", saturation, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.set.saturation.fail")).create();
        }

        return i;
    }

    private static int setSaturation(ServerCommandSource source, float saturation) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return setSaturation(source, players, saturation);
    }

    private static int addFood(ServerCommandSource source, Collection<ServerPlayerEntity> players, int food) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setFoodLevel(food + hungerManager.getFoodLevel());
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.add.food.success", food, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.add.food.fail")).create();
        }

        return i;
    }

    private static int addFood(ServerCommandSource source, int food) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return addFood(source, players, food);
    }

    private static int addExhaustion(ServerCommandSource source, Collection<ServerPlayerEntity> players, float exhaustion) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setExhaustion(exhaustion + hungerManager.getExhaustion());
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.add.exhaustion.success", exhaustion, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.add.exhaustion.fail")).create();
        }

        return i;
    }

    private static int addExhaustion(ServerCommandSource source, float exhaustion) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return addExhaustion(source, players, exhaustion);
    }

    private static int addSaturation(ServerCommandSource source, Collection<ServerPlayerEntity> players, float saturation) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : players) {
            HungerManager hungerManager = player.getHungerManager();
            hungerManager.setSaturationLevel(saturation + hungerManager.getSaturationLevel());
            i++;
        }

        if (i > 0) {
            int finalI = i;
            source.sendFeedback(() -> Text.translatable("commands.hunger.add.saturation.success", saturation, finalI), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.hunger.add.saturation.fail")).create();
        }

        return i;
    }

    private static int addSaturation(ServerCommandSource source, float saturation) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = new ArrayList<>();
        players.add(source.getPlayer());

        return addSaturation(source, players, saturation);
    }

    private static int queryFood(ServerCommandSource source, ServerPlayerEntity player) {
        int hunger = player.getHungerManager().getFoodLevel();
        source.sendFeedback(() -> Text.translatable("commands.hunger.query.food.success", hunger), false);
        return hunger > 0 ? 1 : 0;
    }

    private static int queryFood(ServerCommandSource source) {
        return queryFood(source, Objects.requireNonNull(source.getPlayer()));
    }

    private static int queryExhaustion(ServerCommandSource source, ServerPlayerEntity player) {
        float exhaustion = player.getHungerManager().getExhaustion();
        source.sendFeedback(() -> Text.translatable("commands.hunger.query.exhaustion.success", exhaustion), false);
        return exhaustion > 0 ? 1 : 0;
    }

    private static int queryExhaustion(ServerCommandSource source) {
        return queryExhaustion(source, Objects.requireNonNull(source.getPlayer()));
    }

    private static int querySaturation(ServerCommandSource source, ServerPlayerEntity player) {
        float saturation = player.getHungerManager().getSaturationLevel();
        source.sendFeedback(() -> Text.translatable("commands.hunger.query.saturation.success", saturation), false);
        return saturation > 0 ? 1 : 0;
    }

    private static int querySaturation(ServerCommandSource source) {
        return querySaturation(source, Objects.requireNonNull(source.getPlayer()));
    }
}
