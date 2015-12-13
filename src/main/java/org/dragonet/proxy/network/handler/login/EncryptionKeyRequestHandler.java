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
import net.glowstone.net.message.login.EncryptionKeyRequestMessage;
import org.dragonet.proxy.network.DownstreamSession;

public class EncryptionKeyRequestHandler implements MessageHandler<DownstreamSession.DynamicSession, EncryptionKeyRequestMessage>{

    @Override
    public void handle(DownstreamSession.DynamicSession session, EncryptionKeyRequestMessage message) {
        session.getDownstream().getProxy().getLogger().info("Processing EncryptionKeyRequestMessage!!!! Yay!!!! ");
    }

}
