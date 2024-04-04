package com.thepinkhacker.commandsplus.world;

import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import java.util.function.BiConsumer;

public class TimeRule extends GameRules.IntRule {
    public TimeRule(GameRules.Type<GameRules.IntRule> rule, int initialValue) {
        super(rule, initialValue);
    }

    public static GameRules.Type<GameRules.IntRule> create(
            int initialValue,
            BiConsumer<MinecraftServer, GameRules.IntRule> changeCallback
    ) {
        return new GameRules.Type<>(
                TimeArgumentType::time,
                type -> new TimeRule(type, initialValue),
                changeCallback,
                GameRules.Visitor::visitInt
        );
    }

    public static GameRules.Type<GameRules.IntRule> create(int initialValue) {
        return create(initialValue, (server, rule) -> {});
    }

    @Override
    public String serialize() {
        return this.value + " ticks";
    }
}
