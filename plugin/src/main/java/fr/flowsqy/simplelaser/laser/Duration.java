package fr.flowsqy.simplelaser.laser;

public class Duration {

    private final int ticks;
    private int duration;

    public Duration(double duration) {
        ticks = (int) (duration * 20);
        this.duration = ticks;
    }

    public int getDuration() {
        return duration;
    }

    public int decrementDuration() {
        return --duration;
    }

    public boolean isSecond() {
        return (ticks - duration) % 20 == 0;
    }

    public void reset() {
        duration = ticks;
    }

}
