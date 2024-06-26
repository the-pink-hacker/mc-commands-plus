package com.thepinkhacker.commandsplus.command.argument;

import com.thepinkhacker.commandsplus.CommandsPlus;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class ArgumentTypeManager {
    public static void register() {
        CommandsPlus.LOGGER.info("Registering argument types");
        ArgumentTypeRegistry.registerArgumentType(
                CommandsPlus.identifier("gamerule_preset"),
                GameRulePresetArgumentType.class,
                ConstantArgumentSerializer.of(GameRulePresetArgumentType::preset)
        );
        ArgumentTypeRegistry.registerArgumentType(
                CommandsPlus.identifier("teleport_rule"),
                TeleportRuleArgumentType.class,
                ConstantArgumentSerializer.of(TeleportRuleArgumentType::teleportRule)
        );
    }
}
