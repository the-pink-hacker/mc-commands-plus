package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.commandsplus.util.command.AliasUtils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

public class DayLockCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(CommandManager.literal("daylock")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("lock", BoolArgumentType.bool())
                        .executes(context -> execute(
                                context.getSource(),
                                BoolArgumentType.getBool(context, "lock")
                        ))
                )
                .executes(context -> execute(
                        context.getSource(),
                        true
                ))
        );

        AliasUtils.createAlias(dispatcher, node, "alwaysday");
    }

    private static int execute(ServerCommandSource source, boolean dayLock) {
        GameRules rules = source.getWorld().getGameRules();
        rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(!dayLock, source.getServer());

        // Set noon
        // Bedrock sets the game to 5,000 ticks, but this makes more sense
        // Bedrock is weird
        if (dayLock) source.getWorld().setTimeOfDay(6_000);

        source.sendFeedback(Text.translatable("commands.daylock.success", !dayLock), true);

        // Return 1 only if the value changes
        return rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE) == dayLock ? 1 : 0;
    }
}
