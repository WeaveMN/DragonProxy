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
import org.dragonet.net.packet.minecraft.BlockEntityDataPacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.net.packet.minecraft.WindowOpenPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.nbt.tag.CompoundTag;
import org.dragonet.proxy.network.UpstreamSession;

public class TestCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession cli = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[0])[0];
        cli.sendChat("Opening window... ");

        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int z = Integer.parseInt(args[2]);
        
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "Chest");
        tag.putInt("x", x);
        tag.putInt("y", y);
        tag.putInt("z", z);
        tag.putString("Text1", "TEST 1");
        tag.putString("Text2", "TEST 2");
        tag.putString("Text3", "TEST 3");
        tag.putString("Text4", "TEST 4");
        
        BlockEntityDataPacket bed = new BlockEntityDataPacket(x, y, z, tag);
        cli.sendPacket(bed);
        
        WindowOpenPacket pk = new WindowOpenPacket();
        pk.windowID = (byte)69;
        pk.type = InventoryType.PEInventory.DOUBLE_CHEST;
        pk.slots = (short)12;
        pk.x = x;
        pk.y = y;
        pk.z = z;
        cli.sendPacket(pk, true);
        
        WindowItemsPacket it = new WindowItemsPacket();
        it.windowID = (byte)69;
        it.slots = new PEInventorySlot[12];
        cli.sendPacket(it, true);
    }

}
