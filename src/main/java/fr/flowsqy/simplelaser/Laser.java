package fr.flowsqy.simplelaser;

import fr.flo504.reflect.Reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class Laser {

    private final static int SQUID_ID;
    private final static int GUARDIAN_ID;

    private final static AtomicInteger entityCount;
    private final static Constructor<?> dataWatcherConstructor;
    private final static Method registerDataWatcherMethod;
    private final static Object dataSharedFlagsId;
    private final static Object dataIdAttackTarget;

    private final static Constructor<?> packetSpawnEntityLivingConstructor;
    private final static Constructor<?> packetMetaDataConstructor;

    private final static Field idField;
    private final static Field uuidField;
    private final static Field typeField;
    private final static Field xField;
    private final static Field yField;
    private final static Field zField;
    private final static Field xdField;
    private final static Field ydField;
    private final static Field zdField;
    private final static Field yRotField;
    private final static Field xRotField;
    private final static Field yHeadRotField;

    private final static Method getHandleMethod;
    private final static Field playerConnectionField;
    private final static Method sendPacketMethod;

    static {
        // Constants set (For 1.16.5)
        SQUID_ID = 81;
        GUARDIAN_ID = 31;
        final String entityCountFieldName = "entityCount";
        final String registerDataWatcherMethodName = "register";
        final String dataSharedFlagsIdFieldName = "S";
        final String dataIdAttackTargetFieldName = "d";


        final Class<?> entityClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "Entity");
        final Class<?> entityGuardianClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "EntityGuardian");
        final Class<?> dataWatcherClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "DataWatcher");
        final Class<?> dataWatcherObjectClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "DataWatcherObject");
        final Class<?> packetSpawnEntityLivingClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "PacketPlayOutSpawnEntityLiving");
        final Class<?> packetMetaDataClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "PacketPlayOutEntityMetadata");

        // Entity id counter

        final Field entityCountField = Reflect.getField(entityClass, entityCountFieldName);
        entityCountField.setAccessible(true);
        entityCount = (AtomicInteger) Reflect.getStatic(entityCountField);

        // DataWatcher constructor
        dataWatcherConstructor = Reflect.getConstructor(dataWatcherClass, entityClass);

        dataWatcherConstructor.setAccessible(true);

        // DataWatcher register method
        registerDataWatcherMethod = Reflect.getMethod(dataWatcherClass, registerDataWatcherMethodName, dataWatcherObjectClass, Object.class);

        registerDataWatcherMethod.setAccessible(true);

        // DataWatcher identifier

        // Invisible state
        final Field dataSharedFlagsIdField = Reflect.getField(entityClass, dataSharedFlagsIdFieldName);
        dataSharedFlagsIdField.setAccessible(true);
        dataSharedFlagsId = Reflect.getStatic(dataSharedFlagsIdField);

        // Target for the guardian beam
        final Field dataIdAttackTargetField = Reflect.getField(entityGuardianClass, dataIdAttackTargetFieldName);
        dataIdAttackTargetField.setAccessible(true);
        dataIdAttackTarget = Reflect.getStatic(dataIdAttackTargetField);

        // Packets

        // Spawn

        packetSpawnEntityLivingConstructor = Reflect.getConstructor(packetSpawnEntityLivingClass);

        idField = Reflect.getField(packetSpawnEntityLivingClass, "a");
        uuidField = Reflect.getField(packetSpawnEntityLivingClass, "b");
        typeField = Reflect.getField(packetSpawnEntityLivingClass, "c");
        xField = Reflect.getField(packetSpawnEntityLivingClass, "d");
        yField = Reflect.getField(packetSpawnEntityLivingClass, "e");
        zField = Reflect.getField(packetSpawnEntityLivingClass, "f");
        xdField = Reflect.getField(packetSpawnEntityLivingClass, "g");
        ydField = Reflect.getField(packetSpawnEntityLivingClass, "h");
        zdField = Reflect.getField(packetSpawnEntityLivingClass, "i");
        yRotField = Reflect.getField(packetSpawnEntityLivingClass, "j");
        xRotField = Reflect.getField(packetSpawnEntityLivingClass, "k");
        yHeadRotField = Reflect.getField(packetSpawnEntityLivingClass, "l");


        packetSpawnEntityLivingConstructor.setAccessible(true);

        idField.setAccessible(true);
        uuidField.setAccessible(true);
        typeField.setAccessible(true);
        xField.setAccessible(true);
        yField.setAccessible(true);
        zField.setAccessible(true);
        xdField.setAccessible(true);
        ydField.setAccessible(true);
        zdField.setAccessible(true);
        yRotField.setAccessible(true);
        xRotField.setAccessible(true);
        yHeadRotField.setAccessible(true);

        // MetaData

        packetMetaDataConstructor = Reflect.getConstructor(packetMetaDataClass, int.class, dataWatcherClass, boolean.class);

        packetMetaDataConstructor.setAccessible(true);

        // Send packets stuff

        final Class<?> craftPlayerClass = Reflect.getClass(Reflect.Commons.CRAFTBUKKIT + "entity.CraftPlayer");
        final Class<?> entityPlayerClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "EntityPlayer");
        final Class<?> playerConnectionClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "PlayerConnection");
        final Class<?> packetClass = Reflect.getClass(Reflect.Commons.MINECRAFT + "Packet");

        getHandleMethod = Reflect.getMethod(craftPlayerClass, "getHandle");
        playerConnectionField = Reflect.getField(entityPlayerClass, "playerConnection");
        sendPacketMethod = Reflect.getMethod(playerConnectionClass, "sendPacket", packetClass);

        getHandleMethod.setAccessible(true);
        playerConnectionField.setAccessible(true);
        sendPacketMethod.setAccessible(true);
    }

    
}
