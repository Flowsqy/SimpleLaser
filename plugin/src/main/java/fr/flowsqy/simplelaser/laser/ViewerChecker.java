package fr.flowsqy.simplelaser.laser;

import fr.flowsqy.simplelaser.nms.FakeGuardian;
import fr.flowsqy.simplelaser.nms.FakeSquid;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ViewerChecker {

    private final World world;
    private final Vector start, end;
    private final double distanceSquared;
    private final FakeGuardian fakeGuardian;
    private final FakeSquid fakeSquid;
    private final Set<Player> viewers;

    public ViewerChecker(World world, Vector start, Vector end, double distance, FakeGuardian fakeGuardian, FakeSquid fakeSquid, Set<Player> viewers) {
        this.world = world;
        this.start = start;
        this.end = end;
        this.distanceSquared = distance * distance;
        this.fakeGuardian = fakeGuardian;
        this.fakeSquid = fakeSquid;
        this.viewers = viewers;
    }

    public void tick() {
        final List<Player> createList = new LinkedList<>(), removeList = new LinkedList<>();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getUID().equals(world.getUID()) && isCloseEnough(p.getLocation().toVector())) {
                if (viewers.add(p)) {
                    createList.add(p);
                }
            } else if (viewers.remove(p)) {
                removeList.add(p);
            }
        }

        if (!createList.isEmpty()) {
            createLaser(createList);
        }

        if (!removeList.isEmpty()) {
            removeLaser(removeList);
        }
    }

    public void reset() {
        removeLaser(viewers);
        viewers.clear();
    }

    private void createLaser(Iterable<Player> receivers) {
        fakeSquid.create(end);
        fakeGuardian.create(start);
        fakeGuardian.target(fakeSquid);
        // Create the squid before the guardian, otherwise the guardian can not target the squid
        fakeSquid.update(receivers);
        fakeGuardian.update(receivers);
    }

    private void removeLaser(Iterable<Player> receivers) {
        fakeGuardian.remove();
        fakeGuardian.update(receivers);
        fakeSquid.remove();
        fakeSquid.update(receivers);
    }

    private boolean isCloseEnough(Vector position) {
        return start.distanceSquared(position) <= distanceSquared ||
                end.distanceSquared(position) <= distanceSquared;
    }

}
