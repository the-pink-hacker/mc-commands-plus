package com.ryangar46.commandsplus;

import com.ryangar46.commandsplus.command.argument.ArgumentTypeManager;
import com.ryangar46.commandsplus.server.command.*;
import com.ryangar46.commandsplus.server.dedicated.command.CPStopCommand;
import com.ryangar46.commandsplus.util.command.AliasUtils;
import com.ryangar46.commandsplus.world.GameRuleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
            // Commands
            ClearSpawnPointCommand.register(dispatcher);
            DayLockCommand.register(dispatcher);
            GameRulePresetCommand.register(dispatcher);
            HeadCommand.register(dispatcher);
            HealthCommand.register(dispatcher);
            HungerCommand.register(dispatcher);
            NameCommand.register(dispatcher);
            RideCommand.register(dispatcher);
            SetOwnerCommand.register(dispatcher);
            ToggleDownfallCommand.register(dispatcher);

            // Dedicated server
            if (environment.dedicated) {
                CPStopCommand.register(dispatcher);
            }

            // Aliases
            AliasUtils.createAlias(dispatcher, "gamemode", "gm");
            AliasUtils.createAlias(dispatcher, "help", "?");

            LOGGER.info("Registered commands");
        });
    }
}
