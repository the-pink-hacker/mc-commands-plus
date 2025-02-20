package com.thepinkhacker.decree.data.command;

public class CommandConfig {
    public final CommandPrefix prefix;
    public final String[] aliases;

    private CommandConfig(CommandPrefix prefix, String[] aliases) {
        this.prefix = prefix;
        this.aliases = aliases;
    }

    public static CommandConfig of(CommandPrefix prefix, String[] aliases) {
        return new CommandConfig(prefix, aliases);
    }
}
