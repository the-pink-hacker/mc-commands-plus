package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.commandsplus.util.command.AliasUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

public class SetOwnerCommand implements CommandRegistrationCallback {
    private final int PERMISSION_LEVEL = 2;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(CommandManager.literal("setowner")
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                .then(CommandManager.argument("pets", EntityArgumentType.entities())
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> setOwner(
                                        context.getSource(),
                                        EntityArgumentType.getEntities(context, "pets"),
                                        EntityArgumentType.getPlayer(context, "player"))
                                )
                        )
                        .executes(context -> setOwner(
                                context.getSource(),
                                EntityArgumentType.getEntities(context, "pets"))
                        )
                )
        );

        AliasUtils.createAlias(dispatcher, node, "tame", PERMISSION_LEVEL);
    }

    private static int setOwner(ServerCommandSource source, Collection<? extends Entity> entities, ServerPlayerEntity player) throws CommandSyntaxException {
        int i = 0;

        for (Entity entity : entities) {
            if (entity instanceof TameableEntity pet) {
                if (!pet.isOwner(player)) {
                    pet.setOwner(player);
                    i++;
                }
            }
        }

        if (i > 0) {
            source.sendFeedback(() -> Text.translatable("commands.setowner.success", player.getDisplayName()), false);
        } else {
            throw new SimpleCommandExceptionType(Text.translatable("commands.setowner.fail")).create();
        }

        return i;
    }

    private static int setOwner(ServerCommandSource source, Collection<? extends Entity> entities) throws CommandSyntaxException {
        return setOwner(source, entities, source.getPlayer());
    }
}
