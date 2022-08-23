package fr.flowsqy.simplelaser.command.parser;

public class IntArgument extends CommandArgument<Integer> {

    public IntArgument(String argumentName) {
        super(argumentName, 1);
    }

    public static int getInt(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new RuntimeException("'" + arg + "' is not an integer !");
        }
    }

    @Override
    public Integer parse(int indexOffset, String[] arguments) {
        return getInt(arguments[++indexOffset]);
    }
}
