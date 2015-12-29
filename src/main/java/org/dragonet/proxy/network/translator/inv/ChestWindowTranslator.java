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
package org.dragonet.proxy.network.translator.inv;

import org.dragonet.inventory.InventoryType;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.WindowClosePacket;
import org.dragonet.net.packet.minecraft.WindowOpenPacket;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.InventoryTranslator;
import org.spacehq.mc.protocol.data.game.Position;

public class ChestWindowTranslator implements InventoryTranslator {

    @Override
    public boolean open(UpstreamSession session, CachedWindow window) {
        Position pos = new Position((int)session.getEntityCache().getClientEntity().x, session.getEntityCache().getClientEntity().y > 64.0d ? 0 : 127, (int)session.getEntityCache().getClientEntity().z);
        session.getDataCache().put(CacheKey.WINDOW_BLOCK_POSITION, pos);
        session.sendFakeBlock(pos.getX(), pos.getY(), pos.getZ(), 54, 0);
        WindowOpenPacket pk = new WindowOpenPacket();
        pk.windowID = (byte)(window.windowId & 0xFF);
        pk.slots = window.size <= 27 ? (short)(InventoryType.SlotSize.CHEST & 0xFFFF) : (short)(InventoryType.SlotSize.DOUBLE_CHEST & 0xFFFF);
        pk.type = window.size <= 27 ? InventoryType.PEInventory.CHEST : InventoryType.PEInventory.DOUBLE_CHEST;
        pk.x = pos.getX();
        pk.y = pos.getY();
        pk.z= pos.getZ();
        session.sendPacket(pk);
        return true;
    }

    @Override
    public void updateContent(UpstreamSession session, CachedWindow window) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSlot(UpstreamSession session, int slotIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
