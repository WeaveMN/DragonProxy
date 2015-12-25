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

import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.packet.minecraft.WindowOpenPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;

public class TestCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession cli = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[0])[0];
        cli.sendChat("Opening window... ");
        
        WindowOpenPacket pk = new WindowOpenPacket();
        pk.windowID = (byte)69;
        pk.type = InventoryType.PEInventory.DOUBLE_CHEST;
        pk.slots = (short)12;
        cli.sendPacket(pk);
        
        WindowItemsPacket it = new WindowItemsPacket();
        it.windowID = (byte)69;
        it.slots = new PEInventorySlot[12];
        cli.sendPacket(it);
    }

}
