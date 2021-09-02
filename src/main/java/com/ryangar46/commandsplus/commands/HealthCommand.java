package com.ryangar46.commandsplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.FloatRangeArgument;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.NumberRangeArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class HealthCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("health")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("target", EntityArgumentType.entities())
                        .then(CommandManager.literal("get")
                                .executes((context) -> {
                                    return getHealth(context.getSource(), EntityArgumentType.getEntity(context, "target")); }))
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("health", FloatArgumentType.floatArg(0f, Float.MAX_VALUE))
                                        .executes((context) -> {
                                            return setHealth(context.getSource(), EntityArgumentType.getEntities(context, "target"), FloatArgumentType.getFloat(context, "health")); })))
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("health", FloatArgumentType.floatArg(-Float.MAX_VALUE, Float.MAX_VALUE))
                                        .executes((context) -> {
                                            return addHealth(context.getSource(), EntityArgumentType.getEntities(context, "target"), FloatArgumentType.getFloat(context, "health")); })))));
    }

    private static int setHealth(ServerCommandSource source, Collection<? extends Entity> target, float health) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : target) {
            // Checks if they are a Living Entity
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.setHealth(health);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.health.set.success", i, health), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.health.set.fail")).create();
        }

        return i;
    }

    private static int getHealth(ServerCommandSource source, Entity target) throws CommandSyntaxException {
        float health = -1;

        // Checks if they are a Living Entity
        if (target instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)target;
            health = livingentity.getHealth();
        }

        if (health >= 0f) {
            source.sendFeedback(new TranslatableText("command.health.get.success", health), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.health.get.fail")).create();
        }

        return (int)health;
    }

    private static int addHealth(ServerCommandSource source, Collection<? extends Entity> target, float health) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : target) {
            // Checks if they are a Living Entity
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;
                livingentity.setHealth(livingentity.getHealth() + health);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.health.add.success", health, i), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.health.add.fail")).create();
        }

        return i;
    }
}
