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

            BufferedReader in = new BufferedReader(new InputStreamReader(DefaultSkin.class.getResourceAsStream("/defaults/SKIN.bin")));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            String hex = in.readLine();
            defaultSkin = Binary.hexStringToBytes(hex);
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
