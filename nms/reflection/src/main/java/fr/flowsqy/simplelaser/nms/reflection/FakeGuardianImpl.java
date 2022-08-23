package fr.flowsqy.simplelaser.nms.reflection;

import fr.flowsqy.simplelaser.nms.FakeGuardian;
import fr.flowsqy.simplelaser.nms.FakeSquid;

public class FakeGuardianImpl extends FakeEntityImpl implements FakeGuardian {

    @Override
    public void target(FakeSquid squid) {
        throw new RuntimeException("Not implemented yet");
    }

}
