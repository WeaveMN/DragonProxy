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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.dragonet.net.packet.minecraft.BatchPacket;
import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.net.packet.minecraft.LoginPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.StartGamePacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.network.cache.EntityCache;
import org.dragonet.proxy.network.cache.WindowCache;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.raknet.protocol.EncapsulatedPacket;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.protocol.MinecraftProtocol;

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

    /* =======================================================================================================
     * |                                 Caches for Protocol Compatibility                                   |
    /* ======================================================================================================= */
    
    @Getter
    private final Map<String, Object> dataCache = Collections.synchronizedMap(new HashMap<String, Object>());

    @Getter
    private final EntityCache entityCache = new EntityCache(this);
    
    @Getter
    private final WindowCache windowCache = new WindowCache(this);

    /* ======================================================================================================= */
    
    private MinecraftProtocol protocol;

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

    public void sendAllPacket(PEPacket[] packets, boolean immediate) {
        if (packets.length < 5) {
            for (PEPacket packet : packets) {
                sendPacket(packet);
            }
        } else {
            BatchPacket batch = new BatchPacket();
            boolean mustImmediate = immediate;
            if (!mustImmediate) {
                for (PEPacket packet : packets) {
                    if (packet.isShouldSendImmidate()) {
                        batch.packets.add(packet);
                        mustImmediate = true;
                        break;
                    }
                }
            }
            sendPacket(batch, mustImmediate);
        }
    }

    public void onTick() {
        entityCache.onTick();
    }

    public void disconnect(String reason) {
        proxy.getNetwork().closeSession(raknetID, reason);
        //RakNet server will call onDisconnect()
    }

    /**
     * Called when this client disconnects.
     *
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
        if (proxy.isOnlineMode()) {
            StartGamePacket pkStartGame = new StartGamePacket();
            pkStartGame.eid = 0; //Use EID 0 for eaisier management
            pkStartGame.dimension = (byte) 0;
            pkStartGame.seed = 0;
            pkStartGame.generator = 1;
            pkStartGame.spawnX = 0;
            pkStartGame.spawnY = 0;
            pkStartGame.spawnZ = 0;
            pkStartGame.x = 0.0f;
            pkStartGame.y = 72.0f;
            pkStartGame.z = 0.0f;
            sendPacket(pkStartGame, true);

            LoginStatusPacket pkStat = new LoginStatusPacket();
            pkStat.status = LoginStatusPacket.PLAYER_SPAWN;
            sendPacket(pkStat, true);

            dataCache.put(CacheKey.AUTHENTICATION_STATE, "email");

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_NOTICE, username));
            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_EMAIL));
        } else {
            protocol = new MinecraftProtocol(username);
            downstream.connect(protocol, proxy.getRemoteServerAddress());
        }
    }

    public void sendChat(String chat) {
        if (chat.contains("\n")) {
            String[] lines = chat.split("\n");
            for (String line : lines) {
                sendChat(line);
            }
            return;
        }
        ChatPacket pk = new ChatPacket();
        pk.type = ChatPacket.TextType.CHAT;
        pk.source = "";
        pk.message = chat;
        sendPacket(pk, true);
    }

    public void authenticate(String password) {
        proxy.getGeneralThreadPool().execute(() -> {
            try {
                protocol = new MinecraftProtocol((String) dataCache.get(CacheKey.AUTHENTICATION_EMAIL), password, false);
            } catch (RequestException ex) {
                if (ex.getMessage().toLowerCase().contains("invalid")) {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_FAILD));
                    return;
                } else {
                    sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    disconnect(proxy.getLang().get(Lang.MESSAGE_ONLINE_ERROR));
                    return;
                }
            }

            if (!username.equals(protocol.getProfile().getName())) {
                username = protocol.getProfile().getName();
                sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_USERNAME));
            }

            sendChat(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS, username));

            proxy.getLogger().info(proxy.getLang().get(Lang.MESSAGE_ONLINE_LOGIN_SUCCESS_CONSOLE, username, remoteAddress, username));
            downstream.connect(protocol, proxy.getRemoteServerAddress());
        });
    }
}
