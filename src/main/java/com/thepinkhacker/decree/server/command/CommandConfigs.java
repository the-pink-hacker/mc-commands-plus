package com.thepinkhacker.decree.server.command;

import com.thepinkhacker.decree.Decree;
import com.thepinkhacker.decree.data.command.CommandConfig;
import com.thepinkhacker.decree.data.command.CommandPrefix;
import com.thepinkhacker.decree.registry.DecreeRegistries;
import com.thepinkhacker.decree.registry.DecreeRegistryKeys;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CommandConfigs {
    public static final RegistryKey<CommandConfig> CLEAR_SPAWN_POINT = register("clearspawnpoint");
    public static final RegistryKey<CommandConfig> DAY_LOCK = register("daylock", "alwaysday");
    public static final RegistryKey<CommandConfig> GAME_RULE_PRESET = register("gamerulepreset");
    public static final RegistryKey<CommandConfig> HEAD = register("head", "skull");
    public static final RegistryKey<CommandConfig> HEALTH = register("health", "hp");
    public static final RegistryKey<CommandConfig> HUNGER = register("hunger", "food");
    public static final RegistryKey<CommandConfig> NAME = register("name");
    public static final RegistryKey<CommandConfig> RIDE = register("ride", false, "mount");
    public static final RegistryKey<CommandConfig> SET_OWNER = register("setowner");
    public static final RegistryKey<CommandConfig> TOGGLE_DOWNFALL = register("toggledownfall");
    public static final RegistryKey<CommandConfig> STOP = register("stop", false);

    private static RegistryKey<CommandConfig> register(String id, String... aliases) {
        return register(id, true, aliases);
    }

    private static RegistryKey<CommandConfig> register(String id, boolean prefixOptional, String... aliases) {
        Identifier decreeId = Decree.id(id);
        Registry.register(
                DecreeRegistries.COMMAND_CONFIG,
                decreeId,
                CommandConfig.of(CommandPrefix.of(decreeId.getNamespace(), prefixOptional), aliases)
        );
        return RegistryKey.of(DecreeRegistryKeys.COMMAND_CONFIG, decreeId);
    }

    public static RegistryKey<CommandConfig> registerAndGetDefault(Registry<CommandConfig> registry) {
        return NAME;
    }

    public static void initialize() {}
}
