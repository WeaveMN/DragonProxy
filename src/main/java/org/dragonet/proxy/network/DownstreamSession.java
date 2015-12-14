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

import com.flowpowered.networking.ConnectionManager;
import com.flowpowered.networking.Message;
import com.flowpowered.networking.protocol.AbstractProtocol;
import com.flowpowered.networking.session.BasicSession;
import com.flowpowered.networking.session.Session;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import lombok.Getter;
import net.glowstone.net.message.handshake.HandshakeMessage;
import net.glowstone.net.message.login.LoginStartMessage;
import net.glowstone.net.pipeline.CodecsHandler;
import net.glowstone.net.pipeline.CompressionHandler;
import net.glowstone.net.pipeline.GlowChannelInitializer;
import net.glowstone.net.protocol.GlowProtocol;
import net.glowstone.net.protocol.HandshakeProtocol;
import net.glowstone.net.protocol.LoginProtocol;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.Versioning;

/**
 * Maintaince the connection between the proxy and remote Minecraft server.
 */
public class DownstreamSession implements ConnectionManager {

    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final UpstreamSession upstream;

    private DynamicSession session;

    public DownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
        bootstrap.
                group(workerGroup).
                channel(NioSocketChannel.class)
                .handler(new GlowChannelInitializer(this));
    }

    @Override
    public Session newSession(Channel c) {
        session = new DynamicSession(this, c, new HandshakeProtocol());

        session.send(new HandshakeMessage(Versioning.MINECRAFT_PC_PROTOCOL, proxy.getRemoteServerAddress().getHostString(), proxy.getRemoteServerAddress().getPort(), 2));
        session.setProtocol(new LoginProtocol());

        session.send(new LoginStartMessage(upstream.getUsername()));

        return session;
    }

    public void onError(Throwable err) {
        err.printStackTrace();
        proxy.getLogger().info(String.format("%s[%s]: ", upstream.getUsername(), upstream.getRemoteAddress()) + proxy.getLang().get(Lang.MESSAGE_REMOTE_ERROR));
        upstream.disconnect(proxy.getLang().get(Lang.MESSAGE_REMOTE_ERROR));
        session.disconnect();
        shutdown();
    }

    @Override
    public void sessionInactivated(Session session) {
        proxy.getLogger().info(String.format("%s[%s]: ", upstream.getUsername(), upstream.getRemoteAddress()) + proxy.getLang().get(Lang.MESSAGE_REMOTE_DISCONNECTED));
        upstream.disconnect(proxy.getLang().get(Lang.MESSAGE_REMOTE_DISCONNECTED));
    }

    public ChannelFuture connect(final SocketAddress address) {
        return bootstrap.connect(address).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> f) throws Exception {
                if (f.isSuccess()) {
                    proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));
                } else {
                    proxy.getLogger().info(String.format("%s[%s]: ", upstream.getUsername(), upstream.getRemoteAddress()) + proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECT_FAILURE));
                    upstream.disconnect(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECT_FAILURE));
                }
            }
        });
    }

    public void disconnect() {
        if (session != null && session.isActive()) {
            session.disconnect();
        }
    }

    @Override
    public void shutdown() {
        workerGroup.shutdownGracefully();
    }

    public static class DynamicSession extends BasicSession {

        @Getter
        private final DownstreamSession downstream;

        public DynamicSession(DownstreamSession downstream, Channel channel, AbstractProtocol bootstrapProtocol) {
            super(channel, bootstrapProtocol);
            this.downstream = downstream;
        }

        @Override
        public void setProtocol(AbstractProtocol protocol) {
            if (!GlowProtocol.class.isAssignableFrom(protocol.getClass())) {
                return;
            }
            updatePipeline("codecs", new CodecsHandler((GlowProtocol) protocol));
            super.setProtocol(protocol);
        }
        
        public void updatePipeline(String name, ChannelHandler newHandler){
            getChannel().flush();
            getChannel().pipeline().replace(name, name, newHandler);
        }

        @Override
        public void messageReceived(Message message) {
            System.out.println("Received PC message: " + message.getClass().getSimpleName());
            super.messageReceived(message);
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

        public void enableCompression(int threshold) {
            updatePipeline("compression", new CompressionHandler(threshold));
        }
    }
}
