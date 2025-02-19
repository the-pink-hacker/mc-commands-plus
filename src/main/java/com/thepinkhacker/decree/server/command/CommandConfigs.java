package com.thepinkhacker.decree.server.command;

import com.thepinkhacker.decree.Decree;
import com.thepinkhacker.decree.data.command.CommandConfig;
import com.thepinkhacker.decree.registry.DecreeRegistries;
import net.minecraft.registry.Registry;

public class CommandConfigs {
    public static final CommandConfig NAME = register("name");

    private static CommandConfig register(String id) {
        return Registry.register(DecreeRegistries.COMMAND_CONFIG, Decree.id(id), new CommandConfig());
    }

    public static CommandConfig registerAndGetDefault(Registry<CommandConfig> registry) {
        return NAME;
    }
}
