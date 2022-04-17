package fr.flowsqy.simplelaser;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.List;

public class LaserCommand implements TabExecutor {

    private final SimpleLaserPlugin plugin;

    public LaserCommand(SimpleLaserPlugin plugin) {
        this.plugin = plugin;
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

        if (args.length != 8) {
            return false;
        }

        final double startX, startY, startZ, endX, endY, endZ;
        final int duration, distance;

        try {
            startX = getDouble(args[0]);
            startY = getDouble(args[1]);
            startZ = getDouble(args[2]);
            endX = getDouble(args[3]);
            endY = getDouble(args[4]);
            endZ = getDouble(args[5]);

            duration = getInt(args[6]);
            distance = getInt(args[7]);
        } catch (RuntimeException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }

        final Location start = new Location(world, startX, startY, startZ);
        final Location end = new Location(world, endX, endY, endZ);

        new Laser(plugin, start, end, duration, distance).start(plugin);

        sender.sendMessage("Successfully summoned the laser");
        return true;
    }

    private double getDouble(String arg) {
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            throw new RuntimeException("'" + arg + "' is not a decimal number !");
        }
    }

    private int getInt(String arg) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            throw new RuntimeException("'" + arg + "' is not an integer !");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
