package com.ryangar46.commandsplus.server.dedicated.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

// TODO: Message all players on the server that it will close
public class CPStopCommand {
    private static volatile int timeLeft;
    private static final SimpleCommandExceptionType FAILED_CANCEL = new SimpleCommandExceptionType(Text.translatable("command.cpstop.cancel.fail"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("cpstop")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("cancel")
                        .executes(context -> cancel(context.getSource()))
                )
                .then(CommandManager.argument("time", IntegerArgumentType.integer(0))
                        .executes(context -> stopServer(
                                context.getSource(),
                                IntegerArgumentType.getInteger(context, "time")
                        ))
                )
                .executes(context -> stopServer(
                        context.getSource(),
                        0
                ))
        );
    }

    private static int stopServer(ServerCommandSource source, int seconds) {
        final MinecraftServer server = source.getServer();

        if (seconds > 0) {
            Thread thread = new Thread(() -> {
                timeLeft = seconds;

                for (int i = 0; i < seconds; i++) {
                    if (timeLeft ==  -1) return;
                    timeLeft = seconds - i;

                    source.sendFeedback(Text.translatable("command.cpstop.time", timeLeft), true);

                    try {
                        Thread.sleep(seconds * 100L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                source.sendFeedback(Text.translatable("command.cpstop.stop", seconds), true);
                server.stop(false);
            }, "Server stop thread");

            thread.start();
        } else {
            source.sendFeedback(Text.translatable("command.cpstop.immediate"), true);
            server.stop(false);
        }

        return 1;
    }

    private static int cancel(ServerCommandSource source) throws CommandSyntaxException {
        if (timeLeft > 0) {
            timeLeft = -1;
            source.sendFeedback(Text.translatable("command.cpstop.cancel.success"), true);

            return 1;
        }

        throw FAILED_CANCEL.create();
    }
}
