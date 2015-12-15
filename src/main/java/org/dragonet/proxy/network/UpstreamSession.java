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

import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.raknet.protocol.EncapsulatedPacket;

/**
 * Maintaince the connection between the proxy and Minecraft: Pocket Edition
 * clients.
 */
public class UpstreamSession {

    @Getter
    private final DragonProxy proxy;

    @Getter
    private final String raknetID;

    @Getter
    private final InetSocketAddress remoteAddress;

    @Getter
    private final PEPacketProcessor packetProcessor;

    private final ScheduledFuture<?> packetProcessorScheule;

    @Getter
    private String username;

    @Getter
    private final DownstreamSession downstream;

    public UpstreamSession(DragonProxy proxy, String raknetID, InetSocketAddress remoteAddress) {
        this.proxy = proxy;
        this.raknetID = raknetID;
        this.remoteAddress = remoteAddress;
        packetProcessor = new PEPacketProcessor(this);
        packetProcessorScheule = proxy.getGeneralThreadPool().scheduleAtFixedRate(packetProcessor, 10, 50, TimeUnit.MILLISECONDS);
        downstream = new DownstreamSession(proxy, this);
    }

    public void sendPacket(PEPacket packet) {
        sendPacket(packet, false);
    }

    public void sendPacket(PEPacket packet, boolean immediate) {
        proxy.getNetwork().sendPacket(raknetID, packet, immediate);
    }

    public void onTick() {
        //Nothing here for now. 
    }

    public void disconnect(String reason) {
        proxy.getNetwork().closeSession(raknetID, reason);
        //RakNet server will call onDisconnect()
    }

    /**
     * Called when this client disconnects.
     * @param reason The reason of disconnection. 
     */
    public void onDisconnect(String reason) {
        proxy.getLogger().info(proxy.getLang().get(Lang.CLIENT_DISCONNECTED, username, remoteAddress, reason));
        downstream.disconnect();
        proxy.getSessionRegister().removeSession(this);
        packetProcessorScheule.cancel(true);
    }

    public void handlePacketBinary(EncapsulatedPacket packet) {
        packetProcessor.putPacket(packet.buffer);
    }

    public void onLogin(LoginPacket packet) {
        if (username != null) {
            disconnect("Error! ");
            return;
        }

        LoginStatusPacket status = new LoginStatusPacket();
        if (packet.protocol1 != Versioning.MINECRAFT_PE_PROTOCOL) {
            status.status = LoginStatusPacket.LOGIN_FAILED_CLIENT;
            sendPacket(status, true);
            disconnect(proxy.getLang().get(Lang.MESSAGE_UNSUPPORTED_CLIENT));
            return;
        }
        status.status = LoginStatusPacket.LOGIN_SUCCESS;
        sendPacket(status, true);
        
        this.username = packet.username;
        
        proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_CLIENT_CONNECTED, username, remoteAddress));
        
        downstream.connect(proxy.getRemoteServerAddress());
    }

    
}
