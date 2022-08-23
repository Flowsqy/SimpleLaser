package fr.flowsqy.simplelaser.command.parser;

import java.util.Objects;

public abstract class CommandArgument<T> {

    private final String argumentName;
    private final int elementNumber;

    public CommandArgument(String argumentName, int elementNumber) {
        this.argumentName = "-" + argumentName;
        if (elementNumber < 0) {
            elementNumber = 0;
        }
        this.elementNumber = elementNumber;
    }

    public String getArgumentName() {
        return argumentName;
    }

    public int getElementNumber() {
        return elementNumber;
    }

    public boolean isArgument(String arg) {
        return argumentName.equalsIgnoreCase(arg);
    }

    public boolean hasEnoughArgs(int indexOffset, String[] args) {
        Objects.requireNonNull(args);
        return indexOffset + elementNumber < args.length;
    }

    public abstract T parse(int indexOffset, String[] arguments);

}
