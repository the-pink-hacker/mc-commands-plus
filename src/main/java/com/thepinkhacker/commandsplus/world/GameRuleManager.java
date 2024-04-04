package com.thepinkhacker.commandsplus.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import java.util.function.BiConsumer;

public class GameRuleManager {
    public static final GameRules.Key<GameRules.IntRule> ITEM_DESPAWN_AGE = GameRuleRegistry.register("itemDespawnAge", GameRules.Category.DROPS, create(6_000));

    // Java is weird and won't init the variables unless this runs
    // Otherwise it would try to init after the registry is frozen
    public static void register() {}

    private static GameRules.Type<GameRules.IntRule> create(
            int initialValue,
            BiConsumer<MinecraftServer, GameRules.IntRule> changeCallback
    ) {
        return new GameRules.Type<>(
                TimeArgumentType::time,
                type -> new GameRules.IntRule(type, initialValue),
                changeCallback,
                GameRules.Visitor::visitInt
        );
    }

    private static GameRules.Type<GameRules.IntRule> create(int initialValue) {
        return create(initialValue, (server, rule) -> {});
    }
}
