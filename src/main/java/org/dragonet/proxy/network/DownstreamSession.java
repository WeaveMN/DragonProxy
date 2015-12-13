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

import com.flowpowered.networking.Message;
import com.flowpowered.networking.NetworkClient;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.session.BasicSession;
import com.flowpowered.networking.session.Session;
import io.netty.channel.Channel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import net.glowstone.net.message.handshake.HandshakeMessage;
import net.glowstone.net.pipeline.CodecsHandler;
import net.glowstone.net.pipeline.FramingHandler;
import net.glowstone.net.pipeline.MessageHandler;
import net.glowstone.net.pipeline.NoopHandler;
import net.glowstone.net.protocol.GlowProtocol;
import net.glowstone.net.protocol.LoginProtocol;
import net.glowstone.net.protocol.ProtocolType;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.utilities.Versioning;

/**
 * Maintaince the connection between the proxy and remote Minecraft server. 
 */
public class DownstreamSession extends NetworkClient {
    
    /**
     * The time in seconds which are elapsed before a client is disconnected due 
     * to a read timeout.
     */
    private static final int READ_TIMEOUT = 20;

    /**
     * The time in seconds which are elapsed before a client is deemed idle due 
     * to a write timeout.
     */
    private static final int WRITE_IDLE_TIMEOUT = 15;
    
    @Getter
    private final DragonProxy proxy;
    
    @Getter
    private final UpstreamSession upstream;
    
    private DynamicSession session;

    public DownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
    }
    
    @Override
    public Session newSession(Channel c) {
        session = new DynamicSession(c, new LoginProtocol());
        MessageHandler handler = new MessageHandler(this);
        CodecsHandler codecs = new CodecsHandler(ProtocolType.LOGIN.getProtocol());
        FramingHandler framing = new FramingHandler();

        c.pipeline()
                .addLast("encryption", NoopHandler.INSTANCE)
                .addLast("framing", framing)
                .addLast("compression", NoopHandler.INSTANCE)
                .addLast("codecs", codecs)
                .addLast("readtimeout", new ReadTimeoutHandler(READ_TIMEOUT))
                .addLast("writeidletimeout", new IdleStateHandler(0, WRITE_IDLE_TIMEOUT, 0))
                .addLast("handler", handler);
        
        c.write(new HandshakeMessage(Versioning.MINECRAFT_PC_PROTOCOL, proxy.getRemoteServerAddress().getHostName(), proxy.getRemoteServerAddress().getPort(), 0));
        
        return session;
    }
    
    public void onError(Throwable err){
        err.printStackTrace();
        //TODO
    }

    @Override
    public void sessionInactivated(Session session) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void messageReceived(Message message){
        if(message == null) return;
        System.out.println("Received PC message: " + message.getClass().getSimpleName());
        //TODO
    }
    
    
    public class DynamicSession extends BasicSession {

        public DynamicSession(Channel channel, AbstractProtocol bootstrapProtocol) {
            super(channel, bootstrapProtocol);
        }

        @Override
        public void setProtocol(AbstractProtocol protocol) {
            if(!GlowProtocol.class.isAssignableFrom(protocol.getClass())) return;
            getChannel().flush();
            getChannel().pipeline().replace("codec", "codec", new CodecsHandler((GlowProtocol)protocol));
            super.setProtocol(protocol);
        }

        @Override
        public void onHandlerThrowable(Message message, com.flowpowered.networking.MessageHandler<?, ?> handle, Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onOutboundThrowable(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onInboundThrowable(Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
