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
package org.dragonet.proxy.network.translator;

import java.util.HashMap;
import java.util.Map;

public class ItemBlockTranslator {

    public final static int UNSUPPORTED_BLOCK_ID = 165;

    public final static Map<Integer, Integer> PC_TO_PE_OVERRIDE = new HashMap<>();
    public final static Map<Integer, Integer> PE_TO_PC_OVERRIDE = new HashMap<>();

    static {
        swap(125, 157); //Double Slab <-> Activator Rail
        swap(126, 158); //Slab <-> NULL
        preventPc(158);
        onewayOverride(119, 90); //End portal -> Nether portal
        onewayOverride(176, 63); //Sign         =\_
        onewayOverride(177, 68); //Wall sign    =/ We send banner as sign [Banner]
    }

    private static void swap(int pcId, int peId) {
        PC_TO_PE_OVERRIDE.put(pcId, peId);
        PE_TO_PC_OVERRIDE.put(peId, pcId);
    }

    private static void onewayOverride(int fromPc, int toPe) {
        PC_TO_PE_OVERRIDE.put(fromPc, toPe);
    }

    private static void preventPc(int pc) {
        PC_TO_PE_OVERRIDE.put(pc, UNSUPPORTED_BLOCK_ID); // Block 165 isn't exist in PE so it will be come update block
    }

    // Query handler
    public static int translateToPE(int pcItemBlockId) {
        if (!PC_TO_PE_OVERRIDE.containsKey(pcItemBlockId)) {
            return pcItemBlockId;
        }
        int ret = PC_TO_PE_OVERRIDE.get(pcItemBlockId);
        if (pcItemBlockId > 255 && ret == UNSUPPORTED_BLOCK_ID) {
            ret = 0;   //Unsupported item becomes air
        }
        return ret;
    }
}
