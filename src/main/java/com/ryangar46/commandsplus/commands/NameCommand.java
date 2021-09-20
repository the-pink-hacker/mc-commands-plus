package com.ryangar46.commandsplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class NameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("name")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(CommandManager.literal("item")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.argument("name", MessageArgumentType.message())
                                        .executes((context) -> nameItem(context.getSource(), EntityArgumentType.getEntities(context, "targets"), MessageArgumentType.getMessage(context, "name").getString())))))
                .then(CommandManager.literal("entity")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .then(CommandManager.argument("name", MessageArgumentType.message())
                                        .executes((context) -> nameEntity(context.getSource(), EntityArgumentType.getEntities(context, "targets"), MessageArgumentType.getMessage(context, "name").getString()))))));
    }

    private static int nameItem(ServerCommandSource source, Collection<? extends Entity> target, String name) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : target) {
            // Checks if they are a Living Entity
            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                ItemStack itemstack = livingentity.getMainHandStack();
                // Checks if slot is empty
                if (!itemstack.isEmpty()) {
                    i++;
                    itemstack.setCustomName(Text.of(name));
                }
            }
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.name.item.success", name), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.name.item.fail")).create();
        }

        return i;
    }

    private static int nameEntity(ServerCommandSource source, Collection<? extends Entity> target, String name) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : target) {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;

                livingEntity.setCustomName(Text.of(name));

                livingEntity.setCustomNameVisible(true);

                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.name.entity.success", name), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.name.entity.fail")).create();
        }

        return i;
    }
}
