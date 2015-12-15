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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import lombok.Getter;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.ConnectedEvent;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

/**
 * Maintaince the connection between the proxy and remote Minecraft server.
 */
public class DownstreamSession  {

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final UpstreamSession upstream;

    private MinecraftProtocol protocol;
    private Client remoteClient;

    public DownstreamSession(DragonProxy proxy, UpstreamSession upstream) {
        this.proxy = proxy;
        this.upstream = upstream;
        protocol = new MinecraftProtocol(upstream.getUsername());
    }

    public void onError(Throwable err) {
        err.printStackTrace();
        proxy.getLogger().info(String.format("%s[%s]: ", upstream.getUsername(), upstream.getRemoteAddress()) + proxy.getLang().get(Lang.MESSAGE_REMOTE_ERROR));
        upstream.disconnect(proxy.getLang().get(Lang.MESSAGE_REMOTE_ERROR));
        remoteClient.getSession().disconnect("Error");
        shutdown();
    }

    public void sessionInactivated() {
        proxy.getLogger().info(String.format("%s[%s]: ", upstream.getUsername(), upstream.getRemoteAddress()) + proxy.getLang().get(Lang.MESSAGE_REMOTE_DISCONNECTED));
        upstream.disconnect(proxy.getLang().get(Lang.MESSAGE_REMOTE_DISCONNECTED));
    }

    public void connect(final SocketAddress address) {
        remoteClient = new Client(proxy.getRemoteServerAddress().getHostString(), proxy.getRemoteServerAddress().getPort(), protocol, new TcpSessionFactory());
        remoteClient.getSession().addListener(new SessionAdapter(){
            @Override
            public void connected(ConnectedEvent event) {
                proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_REMOTE_CONNECTED, upstream.getUsername(), upstream.getRemoteAddress()));
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                upstream.disconnect(proxy.getLang().get(event.getReason()));
            }

            @Override
            public void packetReceived(PacketReceivedEvent event) {
                System.out.println(event.getPacket().getClass().getSimpleName() + " > " + event.getPacket().toString());
            }
        });
    }

    public void disconnect() {
        if (remoteClient != null && remoteClient.getSession().isConnected()) {
            remoteClient.getSession().disconnect("Disconnect");;
        }
    }

    public void shutdown() {
        disconnect();
    }

}
