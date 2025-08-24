package fr.flowsqy.simplelaser.nms;

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
