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
package org.dragonet.proxy;

import java.io.IOException;
import lombok.Getter;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.Versioning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DragonProxy {
    public static void main(String[] args){
        new DragonProxy().run(args);
    }
    
    private final Logger logger = LoggerFactory.getLogger("DragonProxy");
    
    @Getter
    private ServerConfig config;
    
    @Getter
    private Lang lang;
    
    public void run(String[] args){
        try {
            config = new ServerConfig();
        } catch (IOException ex) {
            logger.error("Faild to load configuration file! ");
            ex.printStackTrace();
            return;
        }
        try {
            lang = new Lang(config.getConfig().getProperty(ServerConfig.LANG_FILE));
        } catch (IOException ex) {
            logger.error("Faild to load language file! ");
            ex.printStackTrace();
            return;
        }
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        
    }
}
