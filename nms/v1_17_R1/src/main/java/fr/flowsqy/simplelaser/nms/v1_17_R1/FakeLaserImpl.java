package fr.flowsqy.simplelaser.nms.v1_17_R1;

import fr.flowsqy.simplelaser.nms.FakeLaser;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FakeLaserImpl implements FakeLaser {

    private final static AtomicInteger ENTITY_COUNTER;
    private final static EntityDataAccessor<Boolean> DATA_NO_GRAVITY;
    private final static EntityDataAccessor<Boolean> DATA_SILENT;
    private final static byte INVISIBLE_FLAG = 0b100000;
    private final static EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;
    private final static EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;
    private final static Field packedItemField;

    static {
        try {
            final Field entityCounterField = Entity.class.getDeclaredField("b"); // ENTITY_COUNTER
            entityCounterField.setAccessible(true);
            ENTITY_COUNTER = (AtomicInteger) entityCounterField.get(null);

            final Field dataNoGravityField = Entity.class.getDeclaredField("aM"); // DATA_NO_GRAVITY
            dataNoGravityField.setAccessible(true);
            DATA_NO_GRAVITY = forceCast(dataNoGravityField.get(null));

            final Field dataSilentField = Entity.class.getDeclaredField("aL"); // DATA_SILENT
            dataSilentField.setAccessible(true);
            DATA_SILENT = forceCast(dataSilentField.get(null));

            final Field dataSharedFlagsId = Entity.class.getDeclaredField("Z"); // DATA_SHARED_FLAGS_ID
            dataSharedFlagsId.setAccessible(true);
            DATA_SHARED_FLAGS_ID = forceCast(dataSharedFlagsId.get(null));

            final Field dataIdAttackTargetField = Guardian.class.getDeclaredField("e"); // DATA_ID_ATTACK_TARGET
            dataIdAttackTargetField.setAccessible(true);
            DATA_ID_ATTACK_TARGET = forceCast(dataIdAttackTargetField.get(null));

            packedItemField = ClientboundSetEntityDataPacket.class.getDeclaredField("b"); // packedItems
            packedItemField.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private final int squidId, guardianId;

    public FakeLaserImpl() {
        squidId = ENTITY_COUNTER.incrementAndGet();
        guardianId = ENTITY_COUNTER.incrementAndGet();
    }

    @SuppressWarnings("unchecked")
    protected static <T> T forceCast(Object o) {
        return (T) o;
    }

    @Override
    public void create(Iterable<Player> viewer, Location start, Location end) {
        final ClientboundAddEntityPacket spawnSquidPacket, spawnGuardianPacket;
        final ClientboundSetEntityDataPacket dataSquidPacket, dataGuardianPacket;
        // Spawn
        spawnSquidPacket = createSpawnPacket(squidId, start, EntityType.SQUID);
        spawnGuardianPacket = createSpawnPacket(guardianId, end, EntityType.GUARDIAN);
        // Data
        final SynchedEntityData NULL_DATA = new SynchedEntityData(null);

        dataSquidPacket = new ClientboundSetEntityDataPacket(squidId, NULL_DATA, false);
        dataGuardianPacket = new ClientboundSetEntityDataPacket(guardianId, NULL_DATA, false);
        setDefaultDataItems(dataSquidPacket);
        setDefaultDataItems(dataGuardianPacket)
                .add(new SynchedEntityData.DataItem<>(DATA_ID_ATTACK_TARGET, squidId)); // Link to make the laser

        // Send packets
        sendPacket(viewer, spawnSquidPacket, spawnGuardianPacket, dataSquidPacket, dataGuardianPacket);
    }

    private ClientboundAddEntityPacket createSpawnPacket(int entityId, Location loc, EntityType<?> entityType) {
        return new ClientboundAddEntityPacket(
                entityId,
                UUID.randomUUID(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                0f,
                0f,
                entityType,
                0,
                Vec3.ZERO
        );
    }

    private List<SynchedEntityData.DataItem<?>> setDefaultDataItems(ClientboundSetEntityDataPacket dataPacket) {
        final List<SynchedEntityData.DataItem<?>> packedItems = new LinkedList<>();
        // Default flags
        packedItems.add(new SynchedEntityData.DataItem<>(DATA_NO_GRAVITY, true));
        packedItems.add(new SynchedEntityData.DataItem<>(DATA_SILENT, true));
        packedItems.add(new SynchedEntityData.DataItem<>(DATA_SHARED_FLAGS_ID, INVISIBLE_FLAG));

        // Put the list in the packet
        try {
            packedItemField.set(dataPacket, packedItems);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return packedItems;
    }

    @Override
    public void remove(Iterable<Player> viewer) {
        final ClientboundRemoveEntityPacket removeSquidPacket = new ClientboundRemoveEntityPacket(squidId), removeGuardianPacket = new ClientboundRemoveEntityPacket(guardianId);
        sendPacket(viewer, removeSquidPacket, removeGuardianPacket);
    }

    private void sendPacket(Iterable<Player> receivers, Packet<?>... packets) {
        for (Player receiver : receivers) {
            final ServerGamePacketListenerImpl connection = ((CraftPlayer) receiver).getHandle().connection;
            for (Packet<?> packet : packets) {
                connection.send(packet);
            }
        }
    }

}
