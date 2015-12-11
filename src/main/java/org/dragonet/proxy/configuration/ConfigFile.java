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
package org.dragonet.proxy.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.Getter;

public class ConfigFile {
    
    @Getter
    private final Properties config;
    
    public ConfigFile(String defaultResourcePath, String fileName, boolean saveDefault) throws IOException{
        Properties defaultConfig = new Properties();
        defaultConfig.load(ConfigFile.class.getResourceAsStream(defaultResourcePath));
        config = new Properties(defaultConfig);
        File file = new File(fileName);
        if(file.exists()){
            config.load(new FileInputStream(fileName));
        }else{
            if(saveDefault) defaultConfig.store(new FileOutputStream(fileName), "");
        }
    }
}
