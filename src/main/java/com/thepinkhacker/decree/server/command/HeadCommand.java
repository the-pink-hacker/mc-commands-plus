package com.thepinkhacker.decree.server.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.util.command.AliasUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class HeadCommand implements CommandRegistrationCallback {
    private final int PERMISSION_LEVEL = 2;

    private static final SimpleCommandExceptionType GIVE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.head.give.failed"));

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(CommandManager.literal("head")
                .then(CommandManager.literal("give")
                        .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
                        .then(CommandManager.argument("targets", EntityArgumentType.players())
                                .then(CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                                        .executes(context -> give(
                                                context.getSource(),
                                                EntityArgumentType.getPlayers(context, "targets"),
                                                GameProfileArgumentType.getProfileArgument(context, "player")
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
                                // TODO: Add is up-to-date
                        )
                )
                .then(CommandManager.literal("update")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                                .then(CommandManager.argument("player", GameProfileArgumentType.gameProfile())
                                        .executes(context -> updateHead(
                                                context.getSource(),
                                                BlockPosArgumentType.getLoadedBlockPos(context, "pos"),
                                                GameProfileArgumentType.getProfileArgument(context, "player").stream().findFirst().orElseThrow()
                                        ))
                                )
                                .executes(context -> updateHead(
                                        context.getSource(),
                                        BlockPosArgumentType.getLoadedBlockPos(context, "pos")
                                ))
                        )
                )
        );

        AliasUtils.createAlias(dispatcher, node, "skull");
    }

    private static int give(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Collection<GameProfile> profiles) throws CommandSyntaxException {
        int i = 0;

        for (ServerPlayerEntity player : targets) {
            for (GameProfile profile : profiles) {
                final ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();
                stack.set(DataComponentTypes.PROFILE, new ProfileComponent(profile));

                player.giveItemStack(stack);
                i++;
            }
        }

        if (i > 0) {
            source.sendFeedback(() -> Text.translatable("commands.head.give.success"), false);
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
            ProfileComponent owner = head.getOwner();

            if (owner != null) {
                source.sendFeedback(() -> copyText("commands.head.query.uuid.success", owner.gameProfile().getId().toString()), false);
                i = 1;
            }
        }

        return i;
    }

    private static int queryName(ServerCommandSource source, BlockPos pos) {
        int i = -1;

        ServerWorld world = source.getWorld();

        if (world.getBlockEntity(pos) instanceof SkullBlockEntity head) {
            ProfileComponent owner = head.getOwner();

            if (owner != null) {
                source.sendFeedback(() -> copyText("commands.head.query.name.success", owner.gameProfile().getName()), false);
                i = 1;
            }
        }

        return i;
    }

    private static Text copyText(String key, String copyText) {
        return Text.translatable(
                key,
                Texts.bracketed(Text.literal(String.valueOf(copyText))
                        .styled((style) -> style
                                .withColor(Formatting.GREEN)
                                .withClickEvent(new ClickEvent(
                                        ClickEvent.Action.COPY_TO_CLIPBOARD,
                                        String.valueOf(copyText)
                                ))
                                .withHoverEvent(new HoverEvent(
                                        HoverEvent.Action.SHOW_TEXT,
                                        Text.translatable(copyText)
                                ))
                                .withInsertion(String.valueOf(copyText))
                        )
                )
        );
    }

    private static int updateHead(ServerCommandSource source, BlockPos pos, ProfileComponent profile) {
        int i = 0;

        if (source.getWorld().getBlockEntity(pos) instanceof SkullBlockEntity entity) {
            // TODO: Fix heads stay cached until reloading the world
            entity.setOwner(profile);

            i = 1;
        }

        return i;
    }

    private static int updateHead(ServerCommandSource source, BlockPos pos, GameProfile profile) {
        return updateHead(source, pos, new ProfileComponent(profile));
    }

    private static int updateHead(ServerCommandSource source, BlockPos pos) {
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) return -1;

        return updateHead(source, pos, player.getGameProfile());
    }
}
