package fr.flowsqy.simplelaser.nms.v1_18_R2;

import fr.flowsqy.simplelaser.nms.FakeGuardian;
import fr.flowsqy.simplelaser.nms.FakeSquid;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Guardian;

import java.lang.reflect.Field;

public class FakeGuardianImpl extends FakeEntityImpl implements FakeGuardian {

    private final static EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET;

    static {
        try {
            final Field dataIdAttackTargetField = Guardian.class.getDeclaredField(ObfuscateFieldNames.DATA_ID_ATTACK_TARGET);
            dataIdAttackTargetField.setAccessible(true);
            DATA_ID_ATTACK_TARGET = forceCast(dataIdAttackTargetField.get(null));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void target(FakeSquid squid) {
        final ClientboundSetEntityDataPacket dataPacket = createDataPacket();
        injectList(dataPacket).add(new SynchedEntityData.DataItem<>(DATA_ID_ATTACK_TARGET, squid.getId()));
        queuePacket(dataPacket);
    }

    @Override
    protected EntityType<?> getEntityType() {
        return EntityType.GUARDIAN;
    }
}
