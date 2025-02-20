package com.thepinkhacker.decree.server.dedicated.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.thepinkhacker.decree.server.command.CommandConfigs;
import com.thepinkhacker.decree.util.command.DecreeUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class StopCommand implements CommandRegistrationCallbackDedicated {
    private static final int PERMISSION = 4;
    private static volatile int timeLeft;
    private static final SimpleCommandExceptionType FAILED_CANCEL = new SimpleCommandExceptionType(Text.translatable("commands.decree.stop.cancel.failed"));

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        DecreeUtils.register(dispatcher, CommandConfigs.STOP, PERMISSION, command -> command
                .requires(source -> source.hasPermissionLevel(PERMISSION))
                .then(CommandManager.literal("cancel")
                        .executes(context -> cancel())
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
                final PlayerManager playerManager = server.getPlayerManager();
                timeLeft = seconds;

                for (int i = 0; i < seconds; i++) {
                    // Cancel
                    if (timeLeft == -1) {
                        sendServerMessage(playerManager, source, Text.translatable("commands.decree.stop.cancel.success"));
                        return;
                    }

                    timeLeft = seconds - i;

                    if (timeLeft <= 10 || timeLeft % 10 == 0) sendServerMessage(playerManager, source, Text.translatable("commands.decree.stop.time", timeLeft));

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                sendServerMessage(playerManager, source, Text.translatable("commands.decree.stop.stop", seconds));
                server.stop(false);
            }, "Server stop thread");

            thread.start();
        } else {
            source.sendFeedback(() -> Text.translatable("commands.decree.stop.immediate"), true);
            server.stop(false);
        }

        return 1;
    }

    private static int cancel() throws CommandSyntaxException {
        if (timeLeft > 0) {
            timeLeft = -1;
            return 1;
        }

        throw FAILED_CANCEL.create();
    }

    private static void sendServerMessage(PlayerManager playerManager, ServerCommandSource source, Text text) {
        // Todo: Make secure
        playerManager.broadcast(SignedMessage.ofUnsigned(text.toString()), source, MessageType.params(MessageType.CHAT, source));
    }
}
