package fr.flowsqy.simplelaser.nms.v1_18_R2;

import fr.flowsqy.simplelaser.nms.FakeGuardian;
import fr.flowsqy.simplelaser.nms.FakeSquid;
import fr.flowsqy.simplelaser.nms.Platform;

public class PlatformImpl implements Platform {

    @Override
    public FakeGuardian createGuardian() {
        return new FakeGuardianImpl();
    }

    @Override
    public FakeSquid createSquid() {
        return new FakeSquidImpl();
    }
}
