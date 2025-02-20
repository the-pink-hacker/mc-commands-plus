package com.thepinkhacker.decree.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.thepinkhacker.decree.util.command.DecreeUtils;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ToggleDownfallCommand implements CommandRegistrationCallback {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        DecreeUtils.register(dispatcher, CommandConfigs.TOGGLE_DOWNFALL, command -> command
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(ServerCommandSource source) {
        ServerWorld world = source.getWorld();

        world.setWeather(0, 0, !world.isRaining() && !world.isThundering(), false);

        source.sendFeedback(() -> Text.translatable("commands.decree.toggledownfall.success"), true);

        return 1;
    }
}
