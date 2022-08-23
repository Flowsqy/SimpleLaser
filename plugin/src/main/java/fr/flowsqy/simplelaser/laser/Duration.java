package fr.flowsqy.simplelaser.laser;

public class Duration {

    private final int addedTicks;
    private final int originDuration;
    private int duration;

    public Duration(double duration) {
        final int intDuration = (int) duration;
        this.originDuration = intDuration < 0 ? -1 : intDuration;
        this.duration = originDuration;
        int addedTicks = (int) ((duration - intDuration) * 20);
        if (addedTicks < 0) {
            addedTicks = 0;
        }
        if (addedTicks > 20) {
            addedTicks = 20;
        }
        this.addedTicks = addedTicks;
    }

    public int getDuration() {
        return duration;
    }

    public int decrementDuration() {
        return --duration;
    }

    public int getAddedTicks() {
        return addedTicks;
    }

    public double getTotalDuration() {
        return addedTicks * 0.05 + duration;
    }

    public int getTotalTicks() {
        return duration * 20 + addedTicks;
    }

    public void reset() {
        duration = originDuration;
    }

}
