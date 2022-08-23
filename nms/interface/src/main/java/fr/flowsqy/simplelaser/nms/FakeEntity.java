package fr.flowsqy.simplelaser.nms;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface FakeEntity {

    void update(Iterable<Player> viewers);

    void create(Vector location);

    void remove();

    void move(Vector delta);

    void teleport(Vector position);

}
