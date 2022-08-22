package fr.flowsqy.simplelaser;

import fr.flowsqy.simplelaser.nms.FakeLaser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Laser {


    private final int duration;
    private final int addedTicks;
    private final int distanceSquared;
    private final FakeLaser fakeLaser;
    private final Location start;
    private final Location end;

    private BukkitRunnable runnable;

    /**
     * Create a Laser instance
     *
     * @param plugin     The plugin instance to get the laser implementation
     * @param start      Location where laser will start
     * @param end        Location where laser will end
     * @param duration   Duration of laser in seconds (<i>-1 if infinite</i>)
     * @param addedTicks Number of ticks to wait before removing the laser, after the duration is over
     * @param distance   Distance where laser will be visible
     */
    public Laser(SimpleLaserPlugin plugin, Location start, Location end, int duration, int addedTicks, int distance) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        if (addedTicks < 0) {
            addedTicks = 0;
        }
        if (addedTicks > 20) {
            addedTicks = 20;
        }
        this.addedTicks = addedTicks;
        distanceSquared = distance * distance;
        fakeLaser = plugin.getPlatform().createLaser();
    }

    public void start(Plugin plugin) {
        if (runnable == null) {
            throw new IllegalStateException("Task already started");
        }
        runnable = new BukkitRunnable() {
            final Set<Player> show = new HashSet<>();
            int time = duration;

            @Override
            public void run() {
                if (time <= 0) {
                    cancel();
                    return;
                }
                final List<Player> createList = new LinkedList<>(), removeList = new LinkedList<>();
                for (Player p : Objects.requireNonNull(start.getWorld()).getPlayers()) {
                    if (isCloseEnough(p.getLocation())) {
                        if (show.add(p)) {
                            createList.add(p);
                        }
                    } else if (show.remove(p)) {
                        removeList.add(p);
                    }
                }

                if (!createList.isEmpty()) {
                    fakeLaser.create(createList, start, end);
                }

                if (!removeList.isEmpty()) {
                    fakeLaser.remove(removeList);
                }

                time--;
            }

            @Override
            public synchronized void cancel() throws IllegalStateException {
                super.cancel();
                if (addedTicks == 0) {
                    removeLaser(show);
                } else {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> removeLaser(show), addedTicks);
                }

                runnable = null;
            }
        };
        runnable.runTaskTimer(plugin, 0L, 20L);
    }

    public void stop() {
        if (runnable == null) {
            throw new IllegalStateException("Task not started");
        }
        runnable.cancel();
    }

    public boolean isStarted() {
        return runnable != null;
    }

    private void removeLaser(Set<Player> show) {
        if (!show.isEmpty()) {
            fakeLaser.remove(show);
        }
    }

    private boolean isCloseEnough(Location location) {
        return start.distanceSquared(location) <= distanceSquared ||
                end.distanceSquared(location) <= distanceSquared;
    }

}
