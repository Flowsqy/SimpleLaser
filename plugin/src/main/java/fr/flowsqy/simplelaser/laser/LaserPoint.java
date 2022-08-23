package fr.flowsqy.simplelaser.laser;

import fr.flowsqy.simplelaser.nms.FakeEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Set;

public class LaserPoint {

    private static final Vector ZERO = new Vector();

    private final Vector position;
    private final Vector delta;
    private final Vector origin;
    private final Duration duration;
    private final FakeEntity fakeEntity;
    private final Set<Player> viewers;
    private boolean finished;

    public LaserPoint(LaserPointData laserPointData, FakeEntity fakeEntity, Set<Player> viewers) {
        this.origin = laserPointData.getStart().clone();
        this.position = origin.clone();
        this.duration = laserPointData.getDuration();
        this.delta = calculateDelta(laserPointData.getEnd());
        this.fakeEntity = fakeEntity;
        this.viewers = viewers;
    }

    private Vector calculateDelta(Vector endPosition) {
        if (duration == null || duration.getDuration() <= 0 || endPosition == null || origin.equals(endPosition)) {
            return ZERO;
        }
        return endPosition.clone().subtract(origin).multiply(1.0 / duration.getDuration());
    }

    public Vector getPosition() {
        return position;
    }

    public void tick() {
        // Point don't move or have finish moving
        if (delta == ZERO || finished) {
            fakeEntity.teleport(position);
            return;
        }

        // Teleport
        fakeEntity.teleport(position);
        fakeEntity.update(viewers);

        // Update current position
        position.add(delta);

        // Check end
        if (duration.decrementDuration() == 0) {
            finished = true;
            stop();
        }

    }

    private void stop() {
        // Teleport to exact pos
        fakeEntity.teleport(position);
        fakeEntity.update(viewers);
    }

    public void reset() {
        finished = false;
        if (duration != null) {
            duration.reset();
        }
        position.copy(origin);
    }


}
