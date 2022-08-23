package fr.flowsqy.simplelaser.laser;

import fr.flowsqy.simplelaser.nms.FakeEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Set;

public class LaserPoint {

    private static final Vector ZERO = new Vector();

    private final Plugin plugin;
    private final Vector position;
    private final Vector delta;
    private final Vector origin;
    private final Duration duration;
    private final FakeEntity fakeEntity;
    private final Set<Player> viewers;
    private boolean finished;

    public LaserPoint(Plugin plugin, LaserPointData laserPointData, FakeEntity fakeEntity, Set<Player> viewers) {
        this.plugin = plugin;
        this.origin = laserPointData.getStart().clone();
        this.position = origin.clone();
        this.duration = laserPointData.getDuration();
        this.delta = calculateDelta(laserPointData.getEnd());
        this.fakeEntity = fakeEntity;
        this.viewers = viewers;
    }

    private Vector calculateDelta(Vector endPosition) {
        if (duration == null || duration.getTotalTicks() <= 0 || endPosition == null || origin.equals(endPosition)) {
            return ZERO;
        }
        final int totalTicks = duration.getTotalTicks();
        return endPosition.clone().subtract(origin).divide(new Vector(totalTicks, totalTicks, totalTicks));
    }

    public Vector getPosition() {
        return position;
    }

    public void tick() {
        // Point don't move or have finish moving
        if (delta == ZERO || finished) {
            return;
        }

        // Teleport if needed (maybe for future release)

        // Add velocity
        fakeEntity.move(delta);
        fakeEntity.update(viewers);

        // Update current position
        position.add(delta);

        // Check end
        if (duration.decrementDuration() == 0) {
            finished = true;
            if (duration.getAddedTicks() == 0) {
                stop();
            } else {
                Bukkit.getScheduler().runTaskLater(plugin, this::stop, duration.getAddedTicks());
            }
        }

    }

    private void stop() {
        // Set 0 velocity
        fakeEntity.move(ZERO);
        // Teleport to exact pos
        fakeEntity.teleport(position);

        fakeEntity.update(viewers);
    }

    public void reset() {
        finished = false;
        if(duration != null) {
            duration.reset();
        }
        position.copy(origin);
    }


}
