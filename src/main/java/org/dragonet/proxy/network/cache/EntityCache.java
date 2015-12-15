/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.dragonet.proxy.entity.EntityType;
import org.dragonet.proxy.network.UpstreamSession;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;

public final class EntityCache {

    @Getter
    private final UpstreamSession upstream;

    @Getter
    private final Map<Integer, CachedEntity> entities = Collections.synchronizedMap(new HashMap<Integer, CachedEntity>());

    public EntityCache(UpstreamSession upstream) {
        this.upstream = upstream;
    }

    public CachedEntity get(int eid) {
        return entities.get(eid);
    }

    /**
     * Cache a new entity by its spawn packet.
     *
     * @param packet
     * @return Returns null if that entity isn't supported on MCPE yet.
     */
    public CachedEntity newEntity(ServerSpawnMobPacket packet) {
        EntityType peType = EntityType.convertToPE(packet.getType());
        if (peType == null) {
            return null; //Not supported
        }
        CachedEntity e = new CachedEntity(packet.getEntityId(), MagicValues.value(Integer.class, packet.getType()), peType.getPeType());
        e.x = packet.getX();
        e.y = packet.getY();
        e.z = packet.getZ();
        e.motionX = packet.getMotionX();
        e.motionY = packet.getMotionY();
        e.motionZ = packet.getMotionZ();
        e.yaw = packet.getYaw();
        e.pitch = packet.getPitch();
        entities.put(e.eid, e);
        return e;
    }
    
    public void onTick(){
        entities.values().stream().map((e) -> {
            e.x += e.motionX;
            e.y += e.motionY;
            e.z += e.motionZ;
            return e;
        });
    }
}
