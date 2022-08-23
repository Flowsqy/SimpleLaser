package fr.flowsqy.simplelaser.laser;

import org.bukkit.util.Vector;

import java.util.Objects;

public class LaserPointData {

    private final Vector start, end;
    private final Duration duration;

    public LaserPointData(Vector start) {
        this(start, null, null);
    }

    public LaserPointData(Vector start, Vector end, Duration duration) {
        Objects.requireNonNull(start);
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public Vector getStart() {
        return start;
    }

    public Vector getEnd() {
        return end;
    }

    public Duration getDuration() {
        return duration;
    }
}
