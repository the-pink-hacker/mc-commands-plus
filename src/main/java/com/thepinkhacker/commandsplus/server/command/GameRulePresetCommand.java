package com.thepinkhacker.commandsplus.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.thepinkhacker.commandsplus.CommandsPlus;
import com.thepinkhacker.commandsplus.command.argument.GameRulePresetArgumentType;
import com.thepinkhacker.commandsplus.world.GameRulePreset;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class GameRulePresetCommand {
    private static final DynamicCommandExceptionType FAILED_TO_LOAD_EXCEPTION = new DynamicCommandExceptionType(preset -> Text.translatable("commands.gamerulepreset.load.fail", preset));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gamerulepreset")
                .then(CommandManager.literal("save")
                        .requires(source -> source.hasPermissionLevel(4))
                        .then(CommandManager.argument("preset", GameRulePresetArgumentType.preset())
                                .executes(context -> save(
                                        context.getSource(),
                                        GameRulePresetArgumentType.getPreset(context, "preset"))
                                )
                        )
                )
                .then(CommandManager.literal("load")
                        .requires(source -> source.hasPermissionLevel(2))
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
        source.sendFeedback(Text.translatable("commands.gamerulepreset.save.success", FilenameUtils.getBaseName(path.toString())), true);
        return 1;
    }

    private static int load(ServerCommandSource source, Path path) throws CommandSyntaxException {
        int i = GameRulePreset.load(path, source);

        if (i > 1) {
            source.sendFeedback(Text.translatable("commands.gamerulepreset.load.success", FilenameUtils.getBaseName(path.toString())), true);
            return i;
        } else if (i == 0) {
            source.sendFeedback(Text.translatable("commands.gamerulepreset.load.noChange", FilenameUtils.getBaseName(path.toString())), true);
            return i;
        }

        CommandsPlus.LOGGER.info(i);
        throw FAILED_TO_LOAD_EXCEPTION.create(FilenameUtils.getBaseName(path.toString()));
    }
}
