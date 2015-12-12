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
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.dragonet.raknet.server.RakNetServer;
import org.dragonet.raknet.server.ServerHandler;
import org.dragonet.raknet.server.ServerInstance;

public class RaknetInterface implements ServerInstance {
    
    @Getter
    private final DragonProxy proxy;
    
    @Getter
    private final RakNetServer rakServer;
    
    @Getter
    private final ServerHandler handler;
    
    public RaknetInterface(DragonProxy proxy, String ip, int port)  {
        this.proxy = proxy;
        rakServer = new RakNetServer(port, ip);
        handler = new ServerHandler(rakServer, this);
    }
    
    public void onTick(){
        while(handler.handlePacket()){}
    }

    @Override
    public void openSession(String identifier, String address, int port, long clientID) {
        UpstreamSession session = new UpstreamSession(proxy, identifier, new InetSocketAddress(address, port));
        proxy.getSessionRegister().newSession(session);
    }

    @Override
    public void closeSession(String identifier, String reason) {
        
    }

    @Override
    public void handleEncapsulated(String identifier, EncapsulatedPacket packet, int flags) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleRaw(String address, int port, byte[] payload) {
    }

    @Override
    public void notifyACK(String identifier, int identifierACK) {
    }

    @Override
    public void handleOption(String option, String value) {
    }
    
}
