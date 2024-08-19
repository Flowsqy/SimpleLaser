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
            case "acd6e6c27e5a0a9440afba70a96c27c9" -> // 1.17
                    new fr.flowsqy.simplelaser.nms.v1_17_R1.PlatformImpl();
            case "f0e3dfc7390de285a4693518dd5bd126" -> // 1.17.1
                    new fr.flowsqy.simplelaser.nms.v1_17_R2.PlatformImpl();
            case "9e9fe6961a80f3e586c25601590b51ec", "20b026e774dbf715e40a0b2afe114792" -> // 1.18 ; 1.18.1
                    new fr.flowsqy.simplelaser.nms.v1_18_R1.PlatformImpl();
            case "eaeedbff51b16ead3170906872fda334" -> // 1.18.2
                    new fr.flowsqy.simplelaser.nms.v1_18_R2.PlatformImpl();
            case "7b9de0da1357e5b251eddde9aa762916", "4cc0cc97cac491651bff3af8b124a214",
                 "69c84c88aeb92ce9fa9525438b93f4fe" -> // 1.19 ; 1.19.1 ; 1.19.2 (v1_19_R1)
                    new fr.flowsqy.simplelaser.nms.v1_19_R1.PlatformImpl();
            case "3478a65bfd04b15b431fe107b3617dfc" -> //1.20.2 (v1_20_R2)
                    new fr.flowsqy.simplelaser.nms.v1_20_R2.PlatformImpl();
            case "7092ff1ff9352ad7e2260dc150e6a3ec" -> //1.21.1 (v1_21_R1)
                    new fr.flowsqy.simplelaser.nms.v1_21_R1.PlatformImpl();
            default -> null;
        };
    }

}
