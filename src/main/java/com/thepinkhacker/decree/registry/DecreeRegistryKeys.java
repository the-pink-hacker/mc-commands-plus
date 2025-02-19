package com.thepinkhacker.decree.registry;

import com.thepinkhacker.decree.Decree;
import com.thepinkhacker.decree.data.command.CommandConfig;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class DecreeRegistryKeys {
    public static final RegistryKey<Registry<CommandConfig>> COMMAND_CONFIG = of("command_config");

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(Decree.id(id));
    }
}
