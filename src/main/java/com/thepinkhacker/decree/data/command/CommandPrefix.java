package com.thepinkhacker.decree.data.command;

public class CommandPrefix {
    public final String prefix;
    public final boolean optional;

    private CommandPrefix(String prefix, boolean optional) {
        this.prefix = prefix;
        this.optional = optional;
    }

    public static CommandPrefix of(String prefix, boolean optional) {
        return new CommandPrefix(prefix, optional);
    }
}
