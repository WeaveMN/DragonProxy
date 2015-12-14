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
    
    public final static String INIT_MC_PC_SUPPORT = "init_mc_pc_support";
    
    public final static String INIT_MC_PE_SUPPORT = "init_mc_pe_support";
    
    public final static String INIT_CREATING_THREAD_POOL = "init_creating_thread_pool";
    
    public final static String INIT_BINDING = "init_binding";
    
    public final static String INIT_DONE = "init_done";
    
    public final static String MESSAGE_CLIENT_CONNECTED = "message_client_connected";
    
    public final static String MESSAGE_REMOTE_CONNECTED = "message_remote_connected";
    
    public final static String MESSAGE_REMOTE_CONNECT_FAILURE = "message_remote_connect_failure";
    
    public final static String MESSAGE_KICKED = "message_kicked";
    
    public final static String MESSAGE_CLIENT_DISCONNECT = "message_client_disconnect";
    
    public final static String MESSAGE_REMOTE_ERROR = "message_remote_error";
    
    public final static String MESSAGE_REMOTE_DISCONNECTED = "message_remote_disconnected";
    
    public final static String CLIENT_DISCONNECTED = "client_disconnected";
    
    public final static String COMMAND_NOT_FOUND = "command_not_found";
    
    public final static String SHUTTING_DOWN = "shutting_down";
    
    public final static String MESSAGE_UNSUPPORTED_CLIENT = "message_unsupported_client";
    
    public Lang(String langFileName) throws IOException{
        super("/resources/en_US.properties", langFileName, false);
    }
    
    public String get(String key){
        return getConfig().getProperty(key).replace("[PROJNAME]", getConfig().getProperty("project_name"));
    }
    
    public String get(String key, Object... repl){
        return String.format(get(key), repl);
    }
}
