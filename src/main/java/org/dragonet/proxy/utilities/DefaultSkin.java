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
package org.dragonet.proxy.utilities;

import java.io.*;

public class DefaultSkin {

    protected static byte[] defaultSkin;

    static {
        loadSkin();
    }

    public static byte[] getDefaultSkin() {
        return defaultSkin;
    }

    private static void loadSkin() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream ins = DefaultSkin.class.getResourceAsStream("/resources/defaults/SKIN.BIN");
            int d = -1;
            while((d = ins.read()) != -1){
                bos.write(d);
            }
            ins.close();
            defaultSkin = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
