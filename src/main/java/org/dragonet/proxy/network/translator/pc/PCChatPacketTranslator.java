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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
        ChatPacket ret = new ChatPacket();
        ret.source = "";
        ret.message = packet.getMessage().getFullText();
        switch(packet.getType()){
            case CHAT:
                ret.type = ChatPacket.TextType.CHAT;
                break;
            case NOTIFICATION:
                ret.type = ChatPacket.TextType.POPUP;
                break;
            case SYSTEM:
            default:
                ret.type = ChatPacket.TextType.CHAT;
                break;
        }
        return new PEPacket[]{ret};
    }

}
