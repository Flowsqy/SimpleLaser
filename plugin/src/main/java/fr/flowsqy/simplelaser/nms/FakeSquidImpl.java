package fr.flowsqy.simplelaser.nms;

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
