package com.thepinkhacker.commandsplus.command.argument;

import com.thepinkhacker.commandsplus.CommandsPlus;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.Identifier;

public class ArgumentTypeManager {
    public static void register() {
        CommandsPlus.LOGGER.info("Registering argument types");
        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(CommandsPlus.MOD_ID, "gamerule_preset"),
                GameRulePresetArgumentType.class,
                ConstantArgumentSerializer.of(GameRulePresetArgumentType::preset)
        );
        ArgumentTypeRegistry.registerArgumentType(
                new Identifier(CommandsPlus.MOD_ID, "teleport_rule"),
                TeleportRuleArgumentType.class,
                ConstantArgumentSerializer.of(TeleportRuleArgumentType::teleportRule)
        );
    }
}