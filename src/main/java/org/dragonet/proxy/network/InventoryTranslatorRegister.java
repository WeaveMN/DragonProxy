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

import java.util.HashMap;
import java.util.Map;
import org.dragonet.inventory.InventoryType;
import org.dragonet.inventory.PEInventorySlot;
import org.dragonet.inventory.PEWindowConstantID;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.UpdateBlockPacket;
import org.dragonet.net.packet.minecraft.WindowClosePacket;
import org.dragonet.net.packet.minecraft.WindowItemsPacket;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.inv.ChestWindowTranslator;
import org.dragonet.proxy.network.translator.InventoryTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

public final class InventoryTranslatorRegister {

    public final static int[] HOTBAR_CONSTANTS = new int[]{36, 37, 38, 39, 40, 41, 42, 43, 44};

    public static PEPacket[] sendPlayerInventory(UpstreamSession session) {
        CachedWindow win = session.getWindowCache().getPlayerInventory();
        //Translate and send
        WindowItemsPacket ret = new WindowItemsPacket();
        ret.windowID = PEWindowConstantID.PLAYER_INVENTORY;
        ret.slots = new PEInventorySlot[45];
        for (int i = 9; i < win.slots.length; i++) {
            //TODO: Add NBT support
            if (win.slots[i] != null) {
                ret.slots[i - 9] = new PEInventorySlot((short) ItemBlockTranslator.translateToPE(win.slots[i].getId()), (byte) (win.slots[i].getAmount() & 0xFF), (short) win.slots[i].getData(), ItemBlockTranslator.translateNBT(win.slots[i].getNBT()));
            }
        }
        for (int i = 36; i < 45; i++) {
            ret.slots[i] = ret.slots[i - 9];    //Duplicate
        }
        ret.hotbar = HOTBAR_CONSTANTS;

        //TODO: Add armor support
        return new PEPacket[]{ret};
    }

    // PC Type => PE Translator
    private final static Map<WindowType, InventoryTranslator> TRANSLATORS = new HashMap<>();

    static {
        TRANSLATORS.put(WindowType.CHEST, new ChestWindowTranslator());
    }

    public static void closeOpened(UpstreamSession session, boolean byServer) {
        if (session.getDataCache().containsKey(CacheKey.WINDOW_OPENED_ID)) {
            //There is already a window opened
            int id = (int) session.getDataCache().remove(CacheKey.WINDOW_OPENED_ID);
            if (!byServer) {
                session.getDownstream().send(new ClientCloseWindowPacket(id));
            }
            if (session.getDataCache().containsKey(CacheKey.WINDOW_BLOCK_POSITION)) {
                //Already a block was replaced to Chest, reset it
                session.sendFakeBlock(
                        ((Position) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION)).getX(),
                        ((Position) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION)).getY(),
                        ((Position) session.getDataCache().get(CacheKey.WINDOW_BLOCK_POSITION)).getZ(),
                        1, //Set to stone since we don't know what it was, server will correct it once client interacts it
                        0);
            }
            if (byServer) {
                WindowClosePacket pkClose = new WindowClosePacket();
                pkClose.windowID = (byte) (id & 0xFF);
                session.sendPacket(pkClose, true);
            }
        }
    }

    public static void open(UpstreamSession session, ServerOpenWindowPacket win) {
        closeOpened(session, true);
        if (TRANSLATORS.containsKey(win.getType())) {
            CachedWindow cached = new CachedWindow(win.getWindowId(), win.getType(), win.getSlots());
            session.getWindowCache().cacheWindow(cached);
            TRANSLATORS.get(win.getType()).open(session, cached);
        } else {
            //Not supported
            session.getDownstream().send(new ClientCloseWindowPacket(win.getWindowId()));
        }
    }
}
