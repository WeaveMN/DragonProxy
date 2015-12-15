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

import lombok.Data;
import org.spacehq.mc.protocol.data.game.EntityMetadata;

@Data
public class CachedEntity {
    public final int eid;
    public final int pcType;
    public final int peType;
    
    public double x;
    public double y;
    public double z;
    
    public double motionX;
    public double motionY;
    public double motionZ;
    
    public float yaw;
    public float pitch;
    
    public EntityMetadata[] pcMeta;
    
    public CachedEntity relativeMove(double rx, double ry, double rz, float yaw, float pitch){
        x += rx;
        y += ry;
        z += rz;
        this.yaw = yaw;
        this.pitch = pitch;
        return this;
    }
    
    public CachedEntity relativeMove(double rx, double ry, double rz){
        x += rx;
        y += ry;
        z += rz;
        return this;
    }
}
