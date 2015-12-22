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

import org.dragonet.proxy.network.translator.PCPacketTranslator;
import java.util.HashMap;
import java.util.Map;
import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.network.translator.pc.*;
import org.dragonet.proxy.network.translator.pe.PEChatPacketTranslator;
import org.dragonet.proxy.network.translator.pe.PEMovePlayerPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.packetlib.packet.Packet;

public final class TranslatorRegister {

    private final static Map<Class<? extends Packet>, PCPacketTranslator> PC_TO_PE_TRANSLATOR = new HashMap<>();
    private final static Map<Class<? extends PEPacket>, PEPacketTranslator> PE_TO_PC_TRANSLATOR = new HashMap<>();

    /**
     * PC to PE
     */
    static {
        // Login phase
        PC_TO_PE_TRANSLATOR.put(ServerJoinGamePacket.class, new PCJoinGamePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPositionPacket.class, new PCSpawnPositionPacketTranslator());

        // Chat
        PC_TO_PE_TRANSLATOR.put(ServerChatPacket.class, new PCChatPacketTranslator());

        // Map
        PC_TO_PE_TRANSLATOR.put(ServerMultiChunkDataPacket.class, new PCMultiChunkDataPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerUpdateTimePacket.class, new PCUpdateTimePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerBlockChangePacket.class, new PCBlockChangePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerMultiBlockChangePacket.class, new PCMultiBlockChangePacketTranslator());

        // Entity
        PC_TO_PE_TRANSLATOR.put(ServerPlayerPositionRotationPacket.class, new PCPlayerPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnMobPacket.class, new PCSpawnMobPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPlayerPacket.class, new PCSpawnPlayerPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerDestroyEntitiesPacket.class, new PCDestroyEntitiesPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionRotationPacket.class, new PCEntityPositionRotationPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityPositionPacket.class, new PCEntityPositionPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityVelocityPacket.class, new PCEntityVelocityPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityEffectPacket.class, new PCEntityEffectPacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerEntityRemoveEffectPacket.class, new PCEntityRemoveEffectPacketTranslator());

        //Inventory
        PC_TO_PE_TRANSLATOR.put(ServerWindowItemsPacket.class, new PCWindowItemsTranslator());
    }

    /**
     * PE to PC
     */
    static {
        // Chat
        PE_TO_PC_TRANSLATOR.put(ChatPacket.class, new PEChatPacketTranslator());

        // Entity
        PE_TO_PC_TRANSLATOR.put(MovePlayerPacket.class, new PEMovePlayerPacketTranslator());
    }

    public static PEPacket[] translateToPE(UpstreamSession session, Packet packet) {
        if (packet == null) {
            return null;
        }
        PCPacketTranslator target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            return null;
        }
        try {
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Packet[] translateToPC(UpstreamSession session, PEPacket packet) {
        if (packet == null) {
            return null;
        }
        PEPacketTranslator target = PE_TO_PC_TRANSLATOR.get(packet.getClass());
        if (target == null) {
            return null;
        }
        try {
            return target.translate(session, packet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
