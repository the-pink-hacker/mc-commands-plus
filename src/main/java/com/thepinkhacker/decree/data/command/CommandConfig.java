package com.thepinkhacker.decree.data.command;

public class CommandConfig {
    public final CommandPrefix prefix;

    private CommandConfig(CommandPrefix prefix) {
        this.prefix = prefix;
    }

    public static CommandConfig of(CommandPrefix prefix) {
        return new CommandConfig(prefix);
    }
}
