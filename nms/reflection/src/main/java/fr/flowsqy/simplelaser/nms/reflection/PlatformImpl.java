package fr.flowsqy.simplelaser.nms.reflection;

import fr.flowsqy.simplelaser.nms.FakeLaser;
import fr.flowsqy.simplelaser.nms.Platform;

public class PlatformImpl implements Platform {
    @Override
    public FakeLaser createLaser() {
        return new FakeLaserImpl();
    }
}
