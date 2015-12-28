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

import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;

public final class InventoryTranslatorRegister {

    public final static int[] HOTBAR_CONSTANTS = new int[]{36, 37, 38, 39, 40, 41, 42, 43, 44};

    public static PEPacket[] sendPlayerInventory(UpstreamSession session) {
        CachedWindow win = session.getWindowCache().getPlayerInventory();
        //Translate and send
        WindowItemsPacket ret = new WindowItemsPacket();
        ret.windowID = PEWindowConstantID.PLAYER_INVENTORY;
        ret.slots = new PEInventorySlot[45];
        for (int i = 9; i < 45; i++) {
            //TODO: Add NBT support
            if (win.slots[i] != null) {
                ret.slots[i - 9] = new PEInventorySlot((short) ItemBlockTranslator.translateToPE(win.slots[i].getId()), (byte) (win.slots[i].getAmount() & 0xFF), (short) win.slots[i].getData(), ItemBlockTranslator.translateNBT(win.slots[i].getNBT()));
            }
        }
        for(int i = 36; i < 45; i++){
            ret.slots[i] = ret.slots[i - 9];    //Duplicate
        }
        ret.hotbar = HOTBAR_CONSTANTS;
        
        //TODO: Add armor support
        
        return new PEPacket[]{ret};
    }
}
