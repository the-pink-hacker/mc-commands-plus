package com.thepinkhacker.commandsplus.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import java.util.function.BiConsumer;

public class GameRuleManager {
    public static final GameRules.Key<GameRules.IntRule> ITEM_DESPAWN_AGE = GameRuleRegistry.register(
            "itemDespawnAge",
            GameRules.Category.DROPS,
            TimeRule.create(6_000)
        );

    // Java is weird and won't init the variables unless this runs
    // Otherwise it would try to init after the registry is frozen
    public static void register() {}
}
