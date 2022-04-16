package fr.flowsqy.simplelaser.nms.v1_17_1_R1;

import fr.flowsqy.simplelaser.nms.FakeLaser;
import fr.flowsqy.simplelaser.nms.Platform;

public class PlatformImpl implements Platform {
    @Override
    public FakeLaser createLaser() {
        return new FakeLaserImpl();
    }
}
