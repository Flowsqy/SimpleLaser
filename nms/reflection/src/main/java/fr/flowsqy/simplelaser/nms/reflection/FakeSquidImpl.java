package fr.flowsqy.simplelaser.nms.reflection;

import fr.flowsqy.simplelaser.nms.FakeSquid;

public class FakeSquidImpl extends FakeEntityImpl implements FakeSquid {

    @Override
    public int getId() {
        throw new RuntimeException("Not implemented yet");
    }

}
