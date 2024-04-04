package com.ryangar46.commandsplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

public class SetOwnerCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setowner")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("pet", EntityArgumentType.entities())
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes((context) -> setOwner(context.getSource(), EntityArgumentType.getEntities(context, "pet"), EntityArgumentType.getPlayer(context, "player"))))));
    }

    private static int setOwner(ServerCommandSource source, Collection<? extends Entity> pets, PlayerEntity player) throws CommandSyntaxException {
        int i = 0;

        for (Entity pet : pets) {
            // Checks if they are an Animal Entity
            if (pet instanceof AnimalEntity) {
                AnimalEntity animalEntity = (AnimalEntity)pet;

                NbtCompound tag = new NbtCompound();

                animalEntity.writeNbt(tag);

                tag.putUuid("Owner", player.getUuid());

                animalEntity.readNbt(tag);

                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(new TranslatableText("command.setowner.success", player.getDisplayName()), true);
        }
        else {
            throw new SimpleCommandExceptionType(new TranslatableText("command.setowner.fail")).create();
        }

        return i;
    }
}
