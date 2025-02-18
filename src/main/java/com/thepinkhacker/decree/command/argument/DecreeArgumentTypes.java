package com.thepinkhacker.decree.command.argument;

import com.thepinkhacker.decree.Decree;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;

public class DecreeArgumentTypes {
    public static void register() {
        Decree.LOGGER.info("Registering argument types");
        ArgumentTypeRegistry.registerArgumentType(
                Decree.id("gamerule_preset"),
                GameRulePresetArgumentType.class,
                ConstantArgumentSerializer.of(GameRulePresetArgumentType::preset)
        );
        ArgumentTypeRegistry.registerArgumentType(
                Decree.id("teleport_rule"),
                TeleportRuleArgumentType.class,
                ConstantArgumentSerializer.of(TeleportRuleArgumentType::teleportRule)
        );
    }
}
