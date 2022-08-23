package fr.flowsqy.simplelaser.command;

import fr.flowsqy.simplelaser.SimpleLaserPlugin;
import fr.flowsqy.simplelaser.command.parser.ArgumentParser;
import fr.flowsqy.simplelaser.command.parser.DoubleArgument;
import fr.flowsqy.simplelaser.command.parser.IntArgument;
import fr.flowsqy.simplelaser.command.parser.PositionArgument;
import fr.flowsqy.simplelaser.laser.Duration;
import fr.flowsqy.simplelaser.laser.Laser;
import fr.flowsqy.simplelaser.laser.LaserPointData;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LaserCommand implements TabExecutor {

    private final SimpleLaserPlugin plugin;
    private final ArgumentParser argumentParser;

    public LaserCommand(SimpleLaserPlugin plugin) {
        this.plugin = plugin;
        this.argumentParser = createArgumentParser();
    }

    private ArgumentParser createArgumentParser() {
        return new ArgumentParser(
                new PositionArgument("s"),
                new PositionArgument("e"),
                new IntArgument("d"),
                new DoubleArgument("t"),
                new PositionArgument("sm"),
                new PositionArgument("em"),
                new DoubleArgument("st"),
                new DoubleArgument("et")
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final World world;
        if (sender instanceof Entity) {
            world = ((Entity) sender).getWorld();
        } else if (sender instanceof BlockCommandSender) {
            world = ((BlockCommandSender) sender).getBlock().getWorld();
        } else {
            sender.sendMessage("You can use this command only in game");
            return true;
        }

        final Map<String, Object> parsedArgs;
        final Vector start, end, startMovementPos, endMovementPos;

        final int distance;
        final double laserDuration;
        final Double startMovementDuration, endMovementDuration;
        try {
            parsedArgs = argumentParser.parse(0, args);
            start = ((Vector) Objects.requireNonNull(parsedArgs.get("s"), "'-s' argument is required"));
            end = ((Vector) Objects.requireNonNull(parsedArgs.get("e"), "'-e' argument is required"));

            distance = (int) Objects.requireNonNull(parsedArgs.get("d"), "'-d' argument is required");

            laserDuration = (double) Objects.requireNonNull(parsedArgs.get("t"), "'-t' argument is required");

            startMovementPos = (Vector) parsedArgs.get("sm");
            endMovementPos = (Vector) parsedArgs.get("em");

            startMovementDuration = (Double) parsedArgs.get("st");
            endMovementDuration = (Double) parsedArgs.get("et");
        } catch (RuntimeException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }

        final Duration startMovementDurationData = startMovementDuration == null ? null : new Duration(startMovementDuration);
        final Duration endMovementDurationData = endMovementDuration == null ? null : new Duration(endMovementDuration);

        final LaserPointData startData = new LaserPointData(start, startMovementPos, startMovementDurationData);
        final LaserPointData endData = new LaserPointData(end, endMovementPos, endMovementDurationData);

        new Laser(plugin, world, startData, endData, new Duration(laserDuration), distance).start();

        sender.sendMessage("Successfully summoned the laser");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
