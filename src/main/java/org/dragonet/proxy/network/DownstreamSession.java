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
package org.dragonet.proxy.network;

import com.flowpowered.networking.NetworkClient;
import com.flowpowered.networking.session.Session;
import io.netty.channel.Channel;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;

/**
 * Maintaince the connection between the proxy and remote Minecraft server. 
 */
public class DownstreamSession extends NetworkClient {
    
    @Getter
    private final DragonProxy proxy;
    
    @Getter
    private final UpstreamSession upstream;
    
    private Session session;

    public DownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }
    
    @Override
    public Session newSession(Channel c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sessionInactivated(Session session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
