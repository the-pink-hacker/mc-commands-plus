package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class SignCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sign")
                .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                        .then(CommandManager.literal("edit")
                                .requires(source -> source.hasPermissionLevel(2))
                                .then(CommandManager.argument("line", IntegerArgumentType.integer(0, 4))
                                        .then(CommandManager.argument("text", MessageArgumentType.message())
                                                .executes(context -> editLineSign(
                                                        context.getSource(),
                                                        BlockPosArgumentType.getLoadedBlockPos(context, "pos"),
                                                        IntegerArgumentType.getInteger(context, "line"),
                                                        MessageArgumentType.getMessage(context, "text")
                                                ))
                                        )
                                )
                        )
                        .then(CommandManager.literal("query")
                                .then(CommandManager.literal("line"))
                                .then(CommandManager.literal("all"))
                        )
                )
        );
    }

    private static int editLineSign(ServerCommandSource source, BlockPos blockPosition, int line, Text text) {
        //NbtCompound nbtRoot = new NbtCompound();

        // Add line of text
        //source.sendFeedback(() -> Text.of(text.getString()), false);

        ServerWorld world = source.getWorld();

        world.getBlockEntity(blockPosition, BlockEntityType.SIGN).ifPresent(sign -> {
            sign.setTextOnRow(line, text);
            sign.updateListeners();
            source.sendFeedback(() -> Text.of("hi"), false);
        });

        // Not a sign
        return 0;
    }
}
