package com.ryangar46.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.ryangar46.commandsplus.command.argument.GameRulePresetArgumentType;
import com.ryangar46.commandsplus.world.GameRulePreset;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.nio.file.Path;

public class GameRulePresetCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gamerulepreset")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("save")
                        .then(CommandManager.argument("preset", GameRulePresetArgumentType.preset())
                                .executes(context -> save(
                                        context.getSource(),
                                        GameRulePresetArgumentType.getPreset(context, "preset"))
                                )
                        )
                )
                .then(CommandManager.literal("load")
                        .then(CommandManager.argument("preset", GameRulePresetArgumentType.preset())
                                .executes(context -> load(
                                        context.getSource(),
                                        GameRulePresetArgumentType.getPreset(context, "preset"))
                                )
                        )
                )
        );
    }

    private static int save(ServerCommandSource source, Path path) {
        GameRulePreset.save(path, source.getWorld());
        return 1;
    }

    private static int load(ServerCommandSource source, Path path) {
        GameRulePreset.load(path, source.getWorld());
        return 1;
    }
}
