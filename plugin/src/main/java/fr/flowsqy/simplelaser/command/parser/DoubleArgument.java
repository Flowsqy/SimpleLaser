package fr.flowsqy.simplelaser.command.parser;

public class DoubleArgument extends CommandArgument<Double> {

    public DoubleArgument(String argumentName) {
        super(argumentName, 1);
    }

    public static double getDouble(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw new RuntimeException("'" + arg + "' is not a decimal number !");
        }
    }

    @Override
    public Double parse(int indexOffset, String[] arguments) {
        return getDouble(arguments[++indexOffset]);
    }
}
