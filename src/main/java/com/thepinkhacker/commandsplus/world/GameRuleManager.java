package com.thepinkhacker.commandsplus.world;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class GameRuleManager {
    public static final GameRules.Key<GameRules.IntRule> ITEM_DESPAWN_AGE = GameRuleRegistry.register(
            "itemDespawnAge",
            GameRules.Category.DROPS,
            TimeRule.create(6_000)
    );

    public static final GameRules.Key<GameRules.BooleanRule> DO_ENDERMAN_PICKUP = GameRuleRegistry.register(
            "doEndermanPickup",
            GameRules.Category.MOBS,
            GameRules.BooleanRule.create(true)
    );

    // Java is weird and won't init the variables unless this runs
    // Otherwise it would try to init after the registry is frozen
    public static void register() {}
}
