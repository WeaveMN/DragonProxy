//package org.dragonet.proxy.network.translator.pc;
//
//import org.dragonet.net.packet.minecraft.AddPlayerPacket;
//import org.dragonet.net.packet.minecraft.PEPacket;
//import org.dragonet.net.packet.minecraft.PlayerListPacket;
//import org.dragonet.proxy.network.UpstreamSession;
//import org.dragonet.proxy.network.cache.CachedEntity;
//import org.dragonet.proxy.network.translator.PCPacketTranslator;
//import org.dragonet.utilities.DefaultSkin;
//import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
//
//public class PCSpawnPlayerPacketTranslator implements PCPacketTranslator<ServerSpawnPlayerPacket> {
//
//    @Override
//    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket packet) {
//        try {
//            // TODO: fix newEntity() or add newPlayer() ?
//            CachedEntity entity = session.getEntityCache().newEntity(packet);
//            if(entity == null) return null;
//
//            // TODO: Do we need to register the player here ?
//
//            AddPlayerPacket pkAddPlayer = new AddPlayerPacket();
//            pkAddPlayer.uuid = packet.getUUID();
//            pkAddPlayer.eid = packet.getEntityId();
//
//            // TODO: where's player name ?
//            // pkAddPlayer.username = session.getServer().getPlayer(packet.getUUID()).getDisplayName();
//
//            pkAddPlayer.x = (float) packet.getX() / 32;
//            pkAddPlayer.y = (float) packet.getY() / 32;
//            pkAddPlayer.z = (float) packet.getZ() / 32;
//            pkAddPlayer.speedX = 0.0f;
//            pkAddPlayer.speedY = 0.0f;
//            pkAddPlayer.speedZ = 0.0f;
//            pkAddPlayer.yaw = (packet.getYaw() / 256) * 360;
//            pkAddPlayer.pitch = (packet.getPitch() / 256) * 360;
//
//            // TODO: Fix the metadata, this one of the reasons why skins weren't working properly!
//            //pkAddPlayer.metadata = EntityMetaData.getMetaDataFromPlayer((GlowPlayer) this.getSession().getPlayer().getWorld().getEntityManager().getEntity(packet.getId()));
//
//            PlayerListPacket lst = new PlayerListPacket(new PlayerListPacket.PlayerInfo(packet.getUUID(), packet.getEntityId(), pkAddPlayer.username, true, false, DefaultSkin.getDefaultSkin()));
//            return new PEPacket[]{pkAddPlayer, lst};
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}
