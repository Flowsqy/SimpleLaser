package fr.flowsqy.simplelaser.nms;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PlatformLoader {
    public Platform load() {
        final String mappingsVersion = getMappingsVersion();

        if (mappingsVersion == null) {
            return null;
        }

        return getPlatform(mappingsVersion);
    }

    private String getMappingsVersion() {
        try {
            final String craftMagicNumbersClassName = Bukkit.getServer().getClass().getPackageName() + ".util.CraftMagicNumbers";
            final Class<?> craftMagicNumbersClass = Class.forName(craftMagicNumbersClassName);
            final Method method = craftMagicNumbersClass.getDeclaredMethod("getMappingsVersion");
            method.setAccessible(true);
            final Field instanceField = craftMagicNumbersClass.getDeclaredField("INSTANCE");
            instanceField.setAccessible(true);
            return (String) method.invoke(instanceField.get(null));
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private Platform getPlatform(String mappingsVersion) {
        return switch (mappingsVersion) {
            case "acd6e6c27e5a0a9440afba70a96c27c9" -> new fr.flowsqy.simplelaser.nms.v1_17_R1.PlatformImpl();
            case "f0e3dfc7390de285a4693518dd5bd126" -> new fr.flowsqy.simplelaser.nms.v1_17_1_R1.PlatformImpl();
            default -> null;
        };
    }

}
