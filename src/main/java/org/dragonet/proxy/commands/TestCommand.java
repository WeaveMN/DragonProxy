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
package org.dragonet.proxy.commands;

import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.net.packet.minecraft.AddEntityPacket;
import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;

public class TestCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession sess = proxy.getSessionRegister().getAll().get(proxy.getSessionRegister().getAll().keySet().toArray(new String[0])[0]);
        
        AddEntityPacket e = new AddEntityPacket();
        e.eid = System.currentTimeMillis() & 0xFFFF;
        e.type = Integer.parseInt(args[3]);
        e.x = Float.parseFloat(args[0]);
        e.y = Float.parseFloat(args[1]);
        e.z = Float.parseFloat(args[2]);
        e.meta = EntityMetaData.createDefault();
        //e.meta.set(EntityMetaData.Constants.DATA_NAMETAG, new ByteArrayMeta("Type" + e.type));
        proxy.getLogger().info("Sent type " + e.type);
        
        ChatPacket cp = new ChatPacket();
        cp.source = "";
        cp.type = ChatPacket.TextType.CHAT;
        cp.message = "Spawning type " + e.type;
        
        sess.sendPacket(cp, true);
        sess.sendPacket(e, true);
    }

}
