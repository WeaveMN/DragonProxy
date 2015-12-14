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
package org.dragonet.proxy.network.handler.play;

import com.flowpowered.networking.Message;
import com.flowpowered.networking.MessageHandler;
import org.dragonet.proxy.network.DownstreamSession;

public class NOPHandler implements MessageHandler<DownstreamSession.DynamicSession, Message> {

    public final static NOPHandler INSTANCE = new NOPHandler();
    
    @Override
    public void handle(DownstreamSession.DynamicSession arg0, Message arg1) {
    }

}
