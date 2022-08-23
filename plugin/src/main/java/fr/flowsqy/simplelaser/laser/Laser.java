package fr.flowsqy.simplelaser.laser;

import fr.flowsqy.simplelaser.SimpleLaserPlugin;
import fr.flowsqy.simplelaser.nms.FakeGuardian;
import fr.flowsqy.simplelaser.nms.FakeSquid;
import fr.flowsqy.simplelaser.nms.Platform;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Laser {

    private final Plugin plugin;
    private final LaserPoint start, end;
    private final ViewerChecker viewerChecker;
    private final Duration duration;
    private BukkitTask task;

    public Laser(SimpleLaserPlugin plugin, World world, LaserPointData start, LaserPointData end, Duration duration, int distance) {
        this.plugin = Objects.requireNonNull(plugin);
        Objects.requireNonNull(world);
        final Platform platform = plugin.getPlatform();
        final FakeGuardian fakeGuardian = platform.createGuardian();
        final FakeSquid fakeSquid = platform.createSquid();
        final Set<Player> viewers = new HashSet<>();
        this.start = new LaserPoint(plugin, Objects.requireNonNull(start), fakeGuardian, viewers);
        this.end = new LaserPoint(plugin, Objects.requireNonNull(end), fakeSquid, viewers);
        viewerChecker = new ViewerChecker(world, this.start.getPosition(), this.end.getPosition(), distance, fakeGuardian, fakeSquid, viewers);
        this.duration = duration;
    }

    public void start() {
        if (task != null) {
            throw new IllegalStateException("Task already started");
        }

        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, 20L);
    }

    private void tick() {
        viewerChecker.tick();
        start.tick();
        end.tick();

        if (duration.getDuration() == 1) {
            if (duration.getAddedTicks() == 0) {
                cancel();
            } else {
                Bukkit.getScheduler().runTaskLater(plugin, this::cancel, duration.getAddedTicks());
            }
        }
        if (duration.getDuration() > 0) {
            duration.decrementDuration();
        }
    }

    public void cancel() {
        if (task == null) {
            throw new IllegalStateException("Task not started");
        }
        // Cancel global task
        task.cancel();

        // Reset sub-tasks and duration
        duration.reset();
        viewerChecker.reset();
        start.reset();
        end.reset();
    }

    public boolean isStarted() {
        return task != null;
    }

}
