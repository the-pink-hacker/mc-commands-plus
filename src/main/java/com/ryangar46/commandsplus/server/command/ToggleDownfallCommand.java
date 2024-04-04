package com.ryangar46.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ToggleDownfallCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("toggledownfall")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(ServerCommandSource source) {
        ServerWorld world = source.getWorld();

        world.setWeather(0, 0, !world.isRaining() && !world.isThundering(), false);

        source.sendFeedback(Text.translatable("commands.toggledownfall.success"), true);

        return 1;
    }
}
