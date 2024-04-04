package com.ryangar46.commandsplus;

import com.ryangar46.commandsplus.server.command.HealthCommand;
import com.ryangar46.commandsplus.server.command.HungerCommand;
import com.ryangar46.commandsplus.server.command.NameCommand;
import com.ryangar46.commandsplus.server.command.SetOwnerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandsPlus implements ModInitializer {
    public static final String MOD_ID = "commandsplus";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            HealthCommand.register(dispatcher);
            HungerCommand.register(dispatcher);
            NameCommand.register(dispatcher);
            SetOwnerCommand.register(dispatcher);
            LOGGER.info("Registered commands");
        });
    }
}
