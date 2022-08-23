package fr.flowsqy.simplelaser.nms.v1_17_R1;

import fr.flowsqy.simplelaser.nms.FakeSquid;
import net.minecraft.world.entity.EntityType;

public class FakeSquidImpl extends FakeEntityImpl implements FakeSquid {
    @Override
    public int getId() {
        return entityId;
    }

    @Override
    protected EntityType<?> getEntityType() {
        return EntityType.SQUID;
    }
}
