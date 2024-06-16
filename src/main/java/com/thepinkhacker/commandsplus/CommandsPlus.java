package com.thepinkhacker.commandsplus;

import com.mojang.brigadier.CommandDispatcher;
import com.thepinkhacker.commandsplus.command.argument.ArgumentTypeManager;
import com.thepinkhacker.commandsplus.server.command.*;
import com.thepinkhacker.commandsplus.server.dedicated.command.CPStopCommand;
import com.thepinkhacker.commandsplus.server.dedicated.command.CommandRegistrationCallbackDedicated;
import com.thepinkhacker.commandsplus.util.command.AliasUtils;
import com.thepinkhacker.commandsplus.world.GameRuleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandsPlus implements ModInitializer {
    public static final String MOD_ID = "commandsplus";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ArgumentTypeManager.register();
        GameRuleManager.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerCommands(
                    dispatcher,
                    registryAccess,
                    environment,
                    new CommandRegistrationCallback[] {
                            new ClearSpawnPointCommand(),
                            new DayLockCommand(),
                            new GameRulePresetCommand(),
                            new HeadCommand(),
                            new HealthCommand(),
                            new HungerCommand(),
                            new NameCommand(),
                            new RideCommand(),
                            new SetOwnerCommand(),
                            new ToggleDownfallCommand(),
                            new CPStopCommand(),
                    }
            );

            // Aliases
            AliasUtils.createAlias(dispatcher, "gamemode", "gm");
            AliasUtils.createAlias(dispatcher, "help", "?");

            LOGGER.info("Registered commands+.");
        });
    }

    private static void registerCommands(
            CommandDispatcher<net.minecraft.server.command.ServerCommandSource> dispatcher,
            CommandRegistryAccess registryAccess,
            CommandManager.RegistrationEnvironment environment,
            CommandRegistrationCallback[] commands
    ) {
        for (CommandRegistrationCallback command : commands) {
            if (command instanceof CommandRegistrationCallbackDedicated) {
                if (environment.dedicated) command.register(dispatcher, registryAccess, environment);
            } else {
                command.register(dispatcher, registryAccess, environment);
            }
        }
    }

    public static Identifier identifier(String id) {
        return Identifier.of(MOD_ID, id);
    }
}
