package com.ryangar46.commandsplus;

import com.ryangar46.commandsplus.command.argument.ArgumentTypeManager;
import com.ryangar46.commandsplus.server.command.*;
import com.ryangar46.commandsplus.server.dedicated.command.CPStopCommand;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ClearSpawnPointCommand.register(dispatcher);
            GameRulePresetCommand.register(dispatcher);
            HeadCommand.register(dispatcher);
            HealthCommand.register(dispatcher);
            HungerCommand.register(dispatcher);
            NameCommand.register(dispatcher);
            RideCommand.register(dispatcher);
            SetOwnerCommand.register(dispatcher);

            // Dedicated server
            if (environment.dedicated) {
                CPStopCommand.register(dispatcher);
            }

            LOGGER.info("Registered commands");
        });
    }
}
