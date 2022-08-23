package fr.flowsqy.simplelaser.nms.reflection;

import fr.flowsqy.simplelaser.nms.FakeEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class FakeEntityImpl implements FakeEntity {

    @Override
    public void update(Iterable<Player> viewers) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void create(Vector location) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void remove() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void move(Vector delta) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void teleport(Vector position) {
        throw new RuntimeException("Not implemented yet");
    }
}
