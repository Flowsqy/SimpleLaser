package fr.flowsqy.simplelaser.command;

import fr.flowsqy.simplelaser.Laser;
import fr.flowsqy.simplelaser.SimpleLaserPlugin;
import fr.flowsqy.simplelaser.command.parser.ArgumentParser;
import fr.flowsqy.simplelaser.command.parser.DoubleArgument;
import fr.flowsqy.simplelaser.command.parser.IntArgument;
import fr.flowsqy.simplelaser.command.parser.PositionArgument;
import org.bukkit.Location;
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
                new DoubleArgument("t")
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
        final Location start, end;
        final int distance;
        final double duration;
        try {
            parsedArgs = argumentParser.parse(0, args);
            start = ((Vector) Objects.requireNonNull(parsedArgs.get("s"), "'-s' argument is required")).toLocation(world);
            end = ((Vector) Objects.requireNonNull(parsedArgs.get("e"), "'-e' argument is required")).toLocation(world);

            distance = (int) Objects.requireNonNull(parsedArgs.get("d"), "'-d' argument is required");

            duration = (double) Objects.requireNonNull(parsedArgs.get("t"), "'-t' argument is required");
        } catch (RuntimeException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }

        final int intDuration = (int) duration;
        final int laserDuration = intDuration < 0 ? -1 : intDuration;
        final int addedTicks = (int) ((duration - intDuration) * 20);

        new Laser(plugin, start, end, laserDuration, addedTicks, distance).start(plugin);

        sender.sendMessage("Successfully summoned the laser");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
