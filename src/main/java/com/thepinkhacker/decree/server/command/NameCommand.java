package com.thepinkhacker.decree.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.thepinkhacker.decree.util.command.DecreeUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Collection;

public class NameCommand implements CommandRegistrationCallback {
    private static final int PERMISSION = 2;
    public static final SimpleCommandExceptionType ITEM_FAILED = new SimpleCommandExceptionType(Text.translatable("commands.decree.name.item.name.failed"));
    public static final SimpleCommandExceptionType ITEM_REMOVE_FAILED = new SimpleCommandExceptionType(Text.translatable("commands.decree.name.item.remove.failed"));
    public static final SimpleCommandExceptionType ENTITY_FAILED = new SimpleCommandExceptionType(Text.translatable("commands.decree.name.entity.name.failed"));
    public static final SimpleCommandExceptionType ENTITY_REMOVE_FAILED = new SimpleCommandExceptionType(Text.translatable("commands.decree.name.entity.remove.failed"));

    @Override
    public void register(
            CommandDispatcher<ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment
    ) {
        DecreeUtils.register(dispatcher, CommandConfigs.NAME, PERMISSION, command -> command
                .requires(source -> source.hasPermissionLevel(PERMISSION))
                .then(CommandManager.literal("item")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context -> removeNameItem(
                                        context.getSource(),
                                        EntityArgumentType.getEntities(context, "targets")
                                ))
                                .then(CommandManager.argument("slot", ItemSlotArgumentType.itemSlot())
                                        .executes(context -> removeNameItem(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                ItemSlotArgumentType.getItemSlot(context, "slot")
                                        ))
                                        .then(CommandManager.argument("name", MessageArgumentType.message())
                                                .executes(context -> nameItem(
                                                        context.getSource(),
                                                        EntityArgumentType.getEntities(context, "targets"),
                                                        ItemSlotArgumentType.getItemSlot(context, "slot"),
                                                        MessageArgumentType.getMessage(context, "name")
                                                ))
                                        )
                                )
                                .then(CommandManager.argument("name", MessageArgumentType.message())
                                        .executes(context -> nameItem(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                MessageArgumentType.getMessage(context, "name")
                                        ))
                                )
                        )
                )
                .then(CommandManager.literal("entity")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes(context -> removeNameEntity(
                                        context.getSource(),
                                        EntityArgumentType.getEntities(context, "targets")
                                ))
                                .then(CommandManager.argument("name", MessageArgumentType.message())
                                        .executes(context -> nameEntity(
                                                context.getSource(),
                                                EntityArgumentType.getEntities(context, "targets"),
                                                MessageArgumentType.getMessage(context, "name")
                                        ))
                                )
                        )
                )
        );
    }

    private static int nameItem(
            ServerCommandSource source,
            Collection<? extends Entity> targets,
            Text name
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (nameItemTarget(entity, name)) {
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(
                    () -> Text.translatable("commands.decree.name.item.name.success", name),
                    false
            );
            return i;
        } else {
            throw ITEM_FAILED.create();
        }
    }

    private static int nameItem(
            ServerCommandSource source,
            Collection<? extends Entity> targets,
            int slot,
            Text name
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (nameItemTarget(entity, slot, name)) {
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(
                    () -> Text.translatable("commands.decree.name.item.name.success", name),
                    false
            );
            return i;
        } else {
            throw ITEM_FAILED.create();
        }
    }

    private static boolean nameItemTarget(Entity entity, int slot, Text name) {
        StackReference stack = entity.getStackReference(slot);

        if (stack == StackReference.EMPTY) return false;

        return nameItemTarget(stack.get(), name);
    }

    private static boolean nameItemTarget(Entity entity, Text name) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack stack = livingEntity.getMainHandStack();
            return nameItemTarget(stack, name);
        } else {
            return nameItemTarget(entity, 0, name);
        }
    }

    private static boolean nameItemTarget(ItemStack stack, Text name) {
        if (stack.isEmpty()) return false;

        stack.set(DataComponentTypes.CUSTOM_NAME, name);
        return true;
    }

    private static int removeNameItem(
            ServerCommandSource source,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (removeNameItemTarget(entity)) {
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(() -> Text.translatable("commands.decree.name.item.remove.success"), false);
            return i;
        } else {
            throw ITEM_REMOVE_FAILED.create();
        }
    }

    private static int removeNameItem(
            ServerCommandSource source,
            Collection<? extends Entity> targets,
            int slot
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (removeNameItemTarget(entity, slot)) {
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(() -> Text.translatable("commands.decree.name.item.remove.success"), false);
            return i;
        } else {
            throw ITEM_REMOVE_FAILED.create();
        }
    }

    private static boolean removeNameItemTarget(Entity entity, int slot) {
        StackReference stack = entity.getStackReference(slot);

        if (stack == StackReference.EMPTY) return false;

        return removeNameItemTarget(stack.get());
    }

    private static boolean removeNameItemTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ItemStack stack = livingEntity.getMainHandStack();
            return removeNameItemTarget(stack);
        } else {
            return false;
        }
    }

    private static boolean removeNameItemTarget(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return stack.remove(DataComponentTypes.CUSTOM_NAME) != null;
    }

    public static int nameEntity(
            ServerCommandSource source,
            Collection<? extends Entity> targets,
            Text name
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.setCustomName(name);
                livingEntity.setCustomNameVisible(true);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(
                    () -> Text.translatable("commands.decree.name.entity.name.success", name),
                    false
            );
            return i;
        } else {
            throw ENTITY_FAILED.create();
        }
    }

    public static int removeNameEntity(
            ServerCommandSource source,
            Collection<? extends Entity> targets
    ) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : targets) {
            if (entity.getCustomName() == null) continue;

            entity.setCustomNameVisible(false);
            entity.setCustomName(null);
            i++;
        }

        if (i > 0) {
            source.sendFeedback(
                    () -> Text.translatable("commands.decree.name.entity.remove.success"),
                    false
            );
            return i;
        } else {
            throw ENTITY_REMOVE_FAILED.create();
        }
    }
}
