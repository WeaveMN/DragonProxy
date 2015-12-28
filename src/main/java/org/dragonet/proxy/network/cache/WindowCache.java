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
package org.dragonet.proxy.network.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.dragonet.proxy.network.UpstreamSession;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

public final class WindowCache {

    @Getter
    private final UpstreamSession upstream;

    public Map<Integer, CachedWindow> windows = Collections.synchronizedMap(new HashMap<Integer, CachedWindow>());

    public WindowCache(UpstreamSession upstream) {
        this.upstream = upstream;

        CachedWindow inv = new CachedWindow(0, -1, 36);
        windows.put(0, inv);
    }
    
    public CachedWindow getPlayerInventory(){
        return windows.get(0);
    }

    public CachedWindow newWindow(ServerOpenWindowPacket packet) {
        //TODO
        return null;
    }

    public CachedWindow get(int id) {
        return windows.get(id);
    }

    public boolean hasWindow(int id) {
        return windows.containsKey(id);
    }
}
