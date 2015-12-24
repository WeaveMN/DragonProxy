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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
        MovePlayerPacket pk = new MovePlayerPacket(0, (float) packet.getX(), (float) packet.getY(), (float) packet.getZ(), packet.getYaw(), packet.getPitch(), packet.getYaw(), false);
        session.getEntityCache().getPlayer().x = (float)packet.getX();
        session.getEntityCache().getPlayer().y = (float)packet.getY();
        session.getEntityCache().getPlayer().z = (float)packet.getZ();
        session.getEntityCache().getPlayer().yaw = packet.getYaw();
        session.getEntityCache().getPlayer().pitch = packet.getPitch();
        return new PEPacket[]{pk};
    }

}
