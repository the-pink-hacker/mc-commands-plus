package com.thepinkhacker.decree.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.thepinkhacker.decree.util.command.DecreeUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;

public class DayLockCommand implements CommandRegistrationCallback {
    private final int PERMISSION_LEVEL = 2;

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = DecreeUtils.register(dispatcher, CommandConfigs.DAY_LOCK, PERMISSION_LEVEL, command -> command
                .requires(source -> source.hasPermissionLevel(PERMISSION_LEVEL))
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
    }

    private static int execute(ServerCommandSource source, boolean dayLock) {
        GameRules rules = source.getWorld().getGameRules();
        rules.get(GameRules.DO_DAYLIGHT_CYCLE).set(!dayLock, source.getServer());

        // Set noon
        // Bedrock sets the game to 5,000 ticks, but this makes more sense
        // Bedrock is weird
        if (dayLock) source.getWorld().setTimeOfDay(6_000);

        String key = dayLock ? "commands.decree.daylock.enabled" : "commands.decree.daylock.disabled";
        source.sendFeedback(() -> Text.translatable(key), true);

        // Return 1 only if the value changes
        return rules.getBoolean(GameRules.DO_DAYLIGHT_CYCLE) == dayLock ? 1 : 0;
    }
}
