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

import java.io.IOException;

public class Lang extends ConfigFile {
    public final static String INIT_LOADING = "init_loading";
    
    public Lang(String langFileName) throws IOException{
        super("/resources/en_US.properties", langFileName, false);
    }
    
    public String get(String key){
        return getConfig().getProperty(key).replace("[PROJNAME]", getConfig().getProperty("project_name"));
    }
    
    public String get(String key, String... repl){
        return String.format(get(key), (Object[]) repl);
    }
}
