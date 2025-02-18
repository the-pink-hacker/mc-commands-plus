package com.thepinkhacker.decree.command.argument;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

public class TeleportRuleArgumentType extends EnumArgumentType<TeleportRuleArgumentType.TeleportRule> {
    private TeleportRuleArgumentType() {
        super(TeleportRule.CODEC, TeleportRule::values);
    }

    public static EnumArgumentType<TeleportRule> teleportRule() {
        return new TeleportRuleArgumentType();
    }

    public static TeleportRule getTeleportRule(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, TeleportRule.class);
    }

    public enum TeleportRule implements StringIdentifiable {
        TELEPORT_RIDE("teleport_ride"),
        TELEPORT_RIDER("teleport_rider");

        private final String id;
        public static final com.mojang.serialization.Codec<TeleportRule> CODEC = StringIdentifiable.createCodec(TeleportRule::values);

        TeleportRule(String id) {
            this.id = id;
        }

        @Override
        public String asString() {
            return id;
        }
    }
}
