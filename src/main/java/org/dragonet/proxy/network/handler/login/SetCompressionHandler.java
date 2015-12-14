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
package org.dragonet.proxy.network.handler.login;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.net.message.SetCompressionMessage;
import net.glowstone.net.protocol.PlayProtocol;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.DownstreamSession;

public class SetCompressionHandler implements MessageHandler<DownstreamSession.DynamicSession, SetCompressionMessage> {

    @Override
    public void handle(DownstreamSession.DynamicSession session, SetCompressionMessage message) {
        session.enableCompression(message.threshold);
        //Sometimes server send this packet for switching protocol
        session.getDownstream().getProxy().getLogger().info(session.getDownstream().getProxy().getLang().get(Lang.MESSAGE_JOINED, session.getDownstream().getUpstream().getUsername(), session.getDownstream().getUpstream().getRemoteAddress()));
        session.setProtocol(new PlayProtocol());
    }

}
