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

import java.util.Map;
import java.util.IOException;
import lombok.Getter;

public class ServerConfig extends PropertiesConfig {
	
	public static String LANG_FILE = "Placeholder";
	
	//Temporary - trying to get it to build
	public ServerConfig() throws IOException {
	    super("/resources/config.properties", "config.properties", true);
   }

    @Getter
    private String lang = "default";
    
    @Getter
    private String udp_bind_ip = "0.0.0.0";

    @Getter
    private int udp_bind_port = 19132;
    
    @Getter
    private String motd;
    
    @Getter
    private String default_server = "NONE";
    
    @Getter
    private Map<String, RemoteServer> remote_servers;
    
    @Getter
    private String mode = "cls";
    
    @Getter
    private String command_prefix = "/";
    
    @Getter
    private int max_players = -1;
    
    @Getter
    private boolean log_console = true;
    
    @Getter
    private int thread_pool_size;
	
	

}
