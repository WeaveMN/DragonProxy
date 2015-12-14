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
import net.glowstone.net.message.KickMessage;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.DownstreamSession;

public class KickHandler implements MessageHandler<DownstreamSession.DynamicSession, KickMessage> {

    @Override
    public void handle(DownstreamSession.DynamicSession session, KickMessage message) {
        session.getDownstream().getUpstream().disconnect(message.text.encode());
        session.getDownstream().getProxy().getLogger().info(session.getDownstream().getProxy().getLang().get(Lang.MESSAGE_KICKED, session.getDownstream().getUpstream().getUsername(), session.getDownstream().getUpstream().getRemoteAddress(), message.getText().asPlaintext()));
    }

}
