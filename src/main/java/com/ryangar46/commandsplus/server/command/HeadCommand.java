package com.ryangar46.commandsplus.server.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;

public class HeadCommand {
    private static final SimpleCommandExceptionType GIVE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.head.give.fail"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(CommandManager.literal("head")
                .then(CommandManager.literal("give")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("heads", GameProfileArgumentType.gameProfile())
                                        .executes(context -> give(
                                                context.getSource(),
                                                EntityArgumentType.getPlayers(context, "targets"),
                                                GameProfileArgumentType.getProfileArgument(context, "heads")
                                        ))
                                )
                        )
                        .executes(context -> give(
                                context.getSource()
                        ))
                )
                .then(CommandManager.literal("query")
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .then(CommandManager.literal("uuid")
                                        .executes(context -> queryUUID(
                                                context.getSource(),
                                                BlockPosArgumentType.getLoadedBlockPos(context, "pos")
                                        ))
                                )
                                .then(CommandManager.literal("name")
                                        .executes(context -> queryName(
                                                context.getSource(),
                                                BlockPosArgumentType.getLoadedBlockPos(context, "pos")
                                        ))
                                )
                        )
                )
        );

        dispatcher.register(CommandManager.literal("skull").redirect(node));
    }

    private static int give(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Collection<GameProfile> profiles) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : targets) {
            for (GameProfile profile : profiles) {
                final NbtCompound nbt = new NbtCompound();
                nbt.putString("SkullOwner", profile.getName());

                final ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();
                stack.setNbt(nbt);

                player.giveItemStack(stack);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(Text.translatable("commands.head.give.success"), false);
        } else {
            throw GIVE_EXCEPTION.create();
        }

        return i;
    }

    private static int give(ServerCommandSource source) throws CommandSyntaxException {
        final Collection<GameProfile> heads = new ArrayList<>();
        final Collection<ServerPlayerEntity> targets = new ArrayList<>();
        final ServerPlayerEntity player = source.getPlayerOrThrow();

        targets.add(player);
        heads.add(player.getGameProfile());

        return give(source, targets, heads);
    }

    private static int queryUUID(ServerCommandSource source, BlockPos pos) {
        int i = -1;

        ServerWorld world = source.getWorld();

        if (world.getBlockEntity(pos) instanceof SkullBlockEntity head) {
            GameProfile owner = head.getOwner();

            if (owner != null) {
                source.sendFeedback(copyText("commands.head.query.uuid.success", owner.getId().toString()), false);
                i = 1;
            }
        }

        return i;
    }

    private static int queryName(ServerCommandSource source, BlockPos pos) {
        int i = -1;

        ServerWorld world = source.getWorld();

        if (world.getBlockEntity(pos) instanceof SkullBlockEntity head) {
            GameProfile owner = head.getOwner();

            if (owner != null) {
                source.sendFeedback(copyText("commands.head.query.name.success", owner.getName()), false);
                i = 1;
            }
        }

        return i;
    }

    private static Text copyText(String key, String copyText) {
        return Text.translatable(key, Texts.bracketed(Text.literal(String.valueOf(copyText)).styled((style) -> style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(copyText))).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(copyText))).withInsertion(String.valueOf(copyText)))));
    }
}
