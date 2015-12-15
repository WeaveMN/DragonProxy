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
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.translator.PEPacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCJoinGamePacketTranslator;
import org.dragonet.proxy.network.translator.pc.PCSpawnPositionPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.packetlib.packet.Packet;

public final class TranslatorRegister {
    private final static Map<Class<? extends Packet>, PCPacketTranslator> PC_TO_PE_TRANSLATOR = new HashMap<>();
    private final static Map<Class<? extends PEPacket>, PEPacketTranslator> PE_TO_PC_TRANSLATOR = new HashMap<>();
    
    static{
        /* PC to PE */
        //Login Phase: 
        PC_TO_PE_TRANSLATOR.put(ServerJoinGamePacket.class, new PCJoinGamePacketTranslator());
        PC_TO_PE_TRANSLATOR.put(ServerSpawnPositionPacket.class, new PCSpawnPositionPacketTranslator());
        
        
    }
    
    public static PEPacket[] translateToPE(UpstreamSession session, Packet packet){
        if(packet == null) return null;
        PCPacketTranslator target = PC_TO_PE_TRANSLATOR.get(packet.getClass());
        if(target == null) return null;
        return target.translate(session, packet);
    }
    
    public static Packet[] translateToPC(UpstreamSession session, PEPacket packet){
        if(packet == null) return null;
        PEPacketTranslator target = PE_TO_PC_TRANSLATOR.get(packet.getClass());
        if(target == null) return null;
        return target.translate(session, packet);
    }
}
