package com.ryangar46.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class HealthCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("health")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.argument("health", FloatArgumentType.floatArg(0.0f))
                                        .executes(context -> setHealth(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                FloatArgumentType.getFloat(context, "health"))
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("add")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.argument("health", FloatArgumentType.floatArg())
                                        .executes(context -> addHealth(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                FloatArgumentType.getFloat(context, "health"))
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("query")
                        .then(CommandManager.argument("target", EntityArgumentType.entity())
                                .executes(context -> queryHealth(
                                        context.getSource(),
                                        EntityArgumentType.getEntity(context, "target"))
                                )
                        )
                )
        );
    }

    private static int setHealth(ServerCommandSource source, Collection<? extends Entity> entities, float health) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setHealth(health);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.health.set.success", health, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.health.set.fail")).create();
        }

        return i;
    }

    private static int addHealth(ServerCommandSource source, Collection<? extends Entity> entities, float health) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setHealth(health + livingEntity.getHealth());
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("command.health.add.success", health, i), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("command.health.add.fail")).create();
        }

        return i;
    }

    private static int queryHealth(ServerCommandSource source, Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            source.sendFeedback(Text.translatable("command.health.query.success", livingEntity.getHealth()), false);
            return 1;
        }

        throw new SimpleCommandExceptionType(Text.translatable("command.health.query.fail")).create();
    }
}
