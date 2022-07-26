package com.ryangar46.commandsplus.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRuleManager {
    public static final GameRules.Key<GameRules.IntRule> ITEM_DESPAWN_AGE = GameRuleRegistry.register("itemDespawnAge", GameRules.Category.DROPS, GameRuleFactory.createIntRule(6_000, 0));

    // Java is weird and won't init the variables unless this runs
    // Otherwise it would try to init after the registry is frozen
    public static void register() {}
}
