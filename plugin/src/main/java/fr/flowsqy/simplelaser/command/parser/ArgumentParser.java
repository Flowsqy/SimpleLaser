package fr.flowsqy.simplelaser.command.parser;

import java.util.*;

public class ArgumentParser {

    private final List<CommandArgument<?>> arguments;

    public ArgumentParser(CommandArgument<?>... args) {
        this.arguments = new LinkedList<>(Arrays.asList(args));
    }

    public Map<String, Object> parse(int indexOffset, String[] args) {
        Objects.requireNonNull(args);
        if (args.length <= indexOffset) {
            return Collections.emptyMap();
        }
        final Map<String, Object> parsedArgs = new HashMap<>();
        for (int index = indexOffset; index < args.length; index++) {
            final CommandArgument<?> cmdArg = getArgument(args[index]);
            if (!cmdArg.hasEnoughArgs(index, args)) {
                throw new RuntimeException(cmdArg.getArgumentName() + " requires " + cmdArg.getElementNumber() + " elements");
            }
            parsedArgs.put(cmdArg.getArgumentName().substring(1), cmdArg.parse(index, args));
            index += cmdArg.getElementNumber();
        }

        return parsedArgs;
    }

    private CommandArgument<?> getArgument(String arg) {
        for (CommandArgument<?> cmdArg : arguments) {
            if (cmdArg.isArgument(arg)) {
                return cmdArg;
            }
        }
        throw new RuntimeException("Unknown argument: '" + arg + "'");
    }

}
