package com.thepinkhacker.decree.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.util.command.DecreeUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public class HealthCommand implements CommandRegistrationCallback {
    private final int PERMISSION_LEVEL = 2;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = DecreeUtils.register(dispatcher, "health", command -> command
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("health", FloatArgumentType.floatArg(0.0f))
                                .executes(context -> setHealth(
                                        context.getSource(),
                                        FloatArgumentType.getFloat(context, "health"))
                                )
                        )
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
                        .then(CommandManager.argument("health", FloatArgumentType.floatArg())
                                .executes(context -> addHealth(
                                        context.getSource(),
                                        FloatArgumentType.getFloat(context, "health"))
                                )
                        )
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
                        .executes(context -> queryHealth(context.getSource()))
                )
        );

        DecreeUtils.createAlias(dispatcher, node, "hp", PERMISSION_LEVEL);
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
            source.sendFeedback(() -> Text.translatable("commands.decree.health.set.success", health), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.decree.health.set.failed")).create();
        }

        return i;
    }

    private static int setHealth(ServerCommandSource source, float health) throws CommandSyntaxException {
        Collection<Entity> entities = new ArrayList<>();
        entities.add(source.getEntity());

        return setHealth(source, entities, health);
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
            source.sendFeedback(() -> Text.translatable("commands.decree.health.add.success", health), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.decree.health.add.failed")).create();
        }

        return i;
    }

    private static int addHealth(ServerCommandSource source, float health) throws CommandSyntaxException {
        Collection<Entity> entities = new ArrayList<>();
        entities.add(source.getEntity());

        return addHealth(source, entities, health);
    }

    private static int queryHealth(ServerCommandSource source, Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            source.sendFeedback(() -> Text.translatable("commands.decree.health.query.success", livingEntity.getHealth()), false);
            return 1;
        }

        throw new SimpleCommandExceptionType(Text.translatable("commands.decree.health.query.failed")).create();
    }

    private static int queryHealth(ServerCommandSource source) throws CommandSyntaxException {
        return queryHealth(source, source.getEntity());
    }
}
