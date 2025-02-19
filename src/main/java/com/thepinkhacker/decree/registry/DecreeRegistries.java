package com.thepinkhacker.decree.registry;

import com.thepinkhacker.decree.data.command.CommandConfig;
import com.thepinkhacker.decree.server.command.CommandConfigs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DecreeRegistries {
    public static final Registry<CommandConfig> COMMAND_CONFIG = Registries.create(
            DecreeRegistryKeys.COMMAND_CONFIG,
            CommandConfigs::registerAndGetDefault
    );
}
