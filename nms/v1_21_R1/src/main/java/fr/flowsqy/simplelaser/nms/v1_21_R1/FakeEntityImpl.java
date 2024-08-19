package fr.flowsqy.simplelaser.nms.v1_21_R1;

import fr.flowsqy.simplelaser.nms.FakeEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FakeEntityImpl implements FakeEntity {

    private final static AtomicInteger ENTITY_COUNTER;
    private final static EntityDataAccessor<Boolean> DATA_NO_GRAVITY;
    private final static EntityDataAccessor<Boolean> DATA_SILENT;
    private final static byte INVISIBLE_FLAG = 0b100000;
    private final static EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;

    static {
        try {
            final Field entityCounterField = Entity.class.getDeclaredField(ObfuscateFieldNames.ENTITY_COUNTER);
            entityCounterField.setAccessible(true);
            ENTITY_COUNTER = (AtomicInteger) entityCounterField.get(null);

            final Field dataNoGravityField = Entity.class.getDeclaredField(ObfuscateFieldNames.DATA_NO_GRAVITY);
            dataNoGravityField.setAccessible(true);
            DATA_NO_GRAVITY = forceCast(dataNoGravityField.get(null));

            final Field dataSilentField = Entity.class.getDeclaredField(ObfuscateFieldNames.DATA_SILENT);
            dataSilentField.setAccessible(true);
            DATA_SILENT = forceCast(dataSilentField.get(null));

            final Field dataSharedFlagsId = Entity.class.getDeclaredField(ObfuscateFieldNames.DATA_SHARED_FLAGS_ID);
            dataSharedFlagsId.setAccessible(true);
            DATA_SHARED_FLAGS_ID = forceCast(dataSharedFlagsId.get(null));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    protected final int entityId;
    private final List<Packet<?>> packetQueue;

    public FakeEntityImpl() {
        entityId = ENTITY_COUNTER.incrementAndGet();
        packetQueue = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    protected static <T> T forceCast(Object o) {
        return (T) o;
    }

    @Override
    public void update(Iterable<Player> viewers) {
        sendPacket(viewers);
        packetQueue.clear();
    }

    @Override
    public void create(Vector position) {
        // Spawn
        final ClientboundAddEntityPacket spawnPacket = createSpawnPacket(position);
        queuePacket(spawnPacket);
        // Data
        final ClientboundSetEntityDataPacket dataPacket = createDataPacket();
        final List<SynchedEntityData.DataValue<?>> packedItems = injectList(dataPacket);
        // Default flags
        packedItems.add(SynchedEntityData.DataValue.create(DATA_NO_GRAVITY, true));
        packedItems.add(SynchedEntityData.DataValue.create(DATA_SILENT, true));
        packedItems.add(SynchedEntityData.DataValue.create(DATA_SHARED_FLAGS_ID, INVISIBLE_FLAG));

        queuePacket(dataPacket);
    }

    private ClientboundAddEntityPacket createSpawnPacket(Vector position) {
        return new ClientboundAddEntityPacket(
                entityId,
                UUID.randomUUID(),
                position.getX(),
                position.getY(),
                position.getZ(),
                0f,
                0f,
                getEntityType(),
                0,
                Vec3.ZERO,
                0d
        );
    }

    protected abstract EntityType<?> getEntityType();

    protected ClientboundSetEntityDataPacket createDataPacket() {
        return new ClientboundSetEntityDataPacket(entityId, new LinkedList<>());
    }

    protected List<SynchedEntityData.DataValue<?>> injectList(ClientboundSetEntityDataPacket dataPacket) {
        return dataPacket.packedItems();
    }

    @Override
    public void remove() {
        final ClientboundRemoveEntitiesPacket removePacket = new ClientboundRemoveEntitiesPacket(entityId);
        queuePacket(removePacket);
    }

    @Override
    public void teleport(Vector position) {
        final FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        buffer.writeVarInt(entityId);
        buffer.writeDouble(position.getX());
        buffer.writeDouble(position.getY());
        buffer.writeDouble(position.getZ());
        buffer.writeByte(0);
        buffer.writeByte(0);
        buffer.writeBoolean(false);
        final ClientboundTeleportEntityPacket teleportEntityPacket = ClientboundTeleportEntityPacket.STREAM_CODEC.decode(buffer);
        queuePacket(teleportEntityPacket);
    }

    protected void queuePacket(Packet<?> packet) {
        packetQueue.add(packet);
    }

    private void sendPacket(Iterable<Player> receivers) {
        for (Player receiver : receivers) {
            final ServerGamePacketListenerImpl connection = ((CraftPlayer) receiver).getHandle().connection;
            for (Packet<?> packet : packetQueue) {
                connection.send(packet);
            }
        }
    }

}
