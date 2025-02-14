package com.thepinkhacker.commandsplus.world;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thepinkhacker.commandsplus.CommandsPlus;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import java.util.function.BiConsumer;

public class TimeRule extends GameRules.IntRule {
    public TimeRule(GameRules.Type<GameRules.IntRule> rule, int initialValue) {
        super(rule, initialValue);
    }

    private static GameRules.Type<GameRules.IntRule> create(
            int initialValue,
            BiConsumer<MinecraftServer, GameRules.IntRule> changeCallback
    ) {
        return new GameRules.Type<>(
                TimeArgumentType::time,
                type -> new TimeRule(type, initialValue),
                changeCallback,
                GameRules.Visitor::visitInt,
                FeatureSet.empty()
        );
    }

    public static GameRules.Type<GameRules.IntRule> create(int initialValue) {
        return create(initialValue, (server, rule) -> {});
    }

    @Override
    public String serialize() {
        return this.value + " ticks";
    }

    @Override
    protected void deserialize(String raw) {
        StringReader reader = new StringReader(raw);

        try {
            this.value = TimeArgumentType.time().parse(reader);
        } catch (CommandSyntaxException e) {
            CommandsPlus.LOGGER.warn("Failed to parse time {}", raw);
        }
    }
}
