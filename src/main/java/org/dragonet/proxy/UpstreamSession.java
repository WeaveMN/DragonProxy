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
package org.dragonet.proxy;

import java.net.InetSocketAddress;
import lombok.Getter;

public class UpstreamSession {
    @Getter
    private final DragonProxy proxy;
    
    @Getter
    private final String raknetID;

    @Getter
    private final InetSocketAddress remoteAddress;
    
    public UpstreamSession(DragonProxy proxy, String raknetID, InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
    }
    
    public void onTick(){
        
    }
    
    /**
     * Called when this client disconnects.
     * Only callable from class RaknetInterface. 
     */
    public void onDisconnect(){
        //TODO
    }
    
}
