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
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.utilities.PatternChecker;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
        ChatPacket ret = new ChatPacket();
        ret.source = "";
        ret.message = packet.getMessage().getFullText();
        if(session.getDataCache().get(CacheKey.AUTHENTICATION_STATE) != null){
            if(session.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("email")){
                if(!PatternChecker.matchEmail(ret.message.trim())){
                    session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return null;
                }
                session.getDataCache().put(CacheKey.AUTHENTICATION_EMAIL, ret.message.trim());
                session.getDataCache().put(CacheKey.AUTHENTICATION_STATE, "password");
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_PASSWORD));
            }else if(session.getDataCache().get(CacheKey.AUTHENTICATION_STATE).equals("password")){
                if(session.getDataCache().get(CacheKey.AUTHENTICATION_EMAIL) == null || ret.message.equals(" ")){
                    session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    session.disconnect(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return null;
                }
                session.sendChat(session.getProxy().getLang().get(Lang.MESSAGE_ONLINE_LOGGIN_IN));
                session.authenticate(ret.message); //We NEVER cache password for better security. 
            }
            return null;
        }
        switch(packet.getType()){
            case CHAT:
                ret.type = ChatPacket.TextType.CHAT;
                break;
            case NOTIFICATION:
            case SYSTEM:
            default:
                ret.type = ChatPacket.TextType.CHAT;
                break;
        }
        return new PEPacket[]{ret};
    }

}
