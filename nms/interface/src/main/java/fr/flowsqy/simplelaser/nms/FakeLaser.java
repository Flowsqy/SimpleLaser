package fr.flowsqy.simplelaser.nms;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface FakeLaser {

    void create(Iterable<Player> viewer, Location start, Location end);

    void remove(Iterable<Player> viewer);

}
