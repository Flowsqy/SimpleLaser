package fr.flowsqy.simplelaser;

import fr.flowsqy.simplelaser.nms.Platform;
import fr.flowsqy.simplelaser.nms.PlatformLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SimpleLaserPlugin extends JavaPlugin {

    private Platform platform;

    @Override
    public void onEnable() {
        platform = new PlatformLoader().load();

        if (platform == null) {
            getLogger().severe("Can not load SimpleLaser, the server version is not supported");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Objects.requireNonNull(getCommand("laser")).setExecutor(new LaserCommand(this));
    }

    public Platform getPlatform() {
        return platform;
    }
}
