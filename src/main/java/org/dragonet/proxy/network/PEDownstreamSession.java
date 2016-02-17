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

import org.dragonet.net.packet.minecraft.PEPacket;

public class PEDownstreamSession implements DownstreamSession<PEPacket> {

    @Override
    public void connect(String addr, int port) {
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void send(PEPacket packet) {
    }

    @Override
    public void send(PEPacket... packets) {
    }

    @Override
    public void sendChat(String chat) {
    }

    @Override
    public void disconnect() {
    }

}
