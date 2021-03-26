package fr.flowsqy.simplelaser;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class LaserCommand implements TabExecutor {

    private final Plugin plugin;

    public LaserCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try{
            final World world;
            if(sender instanceof Entity){
                world = ((Entity) sender).getWorld();
            }
            else if(sender instanceof BlockCommandSender){
                world = ((BlockCommandSender) sender).getBlock().getWorld();
            }
            else{
                sender.sendMessage("You can use this command only in game");
                return true;
            }

            final double startX = Double.parseDouble(args[0]);
            final double startY = Double.parseDouble(args[1]);
            final double startZ = Double.parseDouble(args[2]);
            final double endX = Double.parseDouble(args[3]);
            final double endY = Double.parseDouble(args[4]);
            final double endZ = Double.parseDouble(args[5]);
            final Location start = new Location(world, startX, startY, startZ);
            final Location end = new Location(world, endX, endY, endZ);

            final int duration = Integer.parseInt(args[6]);
            final int distance = Integer.parseInt(args[7]);

            new Laser(start, end, duration, distance).start(plugin);

            sender.sendMessage("Successfully summoned the laser");
            return true;
        }catch (Exception ignored){
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
