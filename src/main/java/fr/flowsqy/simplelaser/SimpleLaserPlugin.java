package fr.flowsqy.simplelaser;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleLaserPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("laser").setExecutor(new LaserCommand(this));
    }
}
