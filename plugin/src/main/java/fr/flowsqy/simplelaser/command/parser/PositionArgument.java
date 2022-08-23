package fr.flowsqy.simplelaser.command.parser;

import org.bukkit.util.Vector;

public class PositionArgument extends CommandArgument<Vector> {

    public PositionArgument(String argumentName) {
        super(argumentName, 3);
    }

    @Override
    public Vector parse(int indexOffset, String[] arguments) {
        final double x, y, z;
        x = DoubleArgument.getDouble(arguments[++indexOffset]);
        y = DoubleArgument.getDouble(arguments[++indexOffset]);
        z = DoubleArgument.getDouble(arguments[++indexOffset]);
        return new Vector(x, y, z);
    }

}
