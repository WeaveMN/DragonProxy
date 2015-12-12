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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.Getter;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.Versioning;
import java.util.logging.Logger;

public class DragonProxy {
    public static void main(String[] args){
        new DragonProxy().run(args);
    }
    
    private final Logger logger = Logger.getLogger("DragonProxy");
    
    private final TickerThread ticker = new TickerThread(this);
    
    @Getter
    private ServerConfig config;
    
    @Getter
    private Lang lang;
    
    @Getter
    private SessionRegister sessionRegister;
    
    @Getter
    private RaknetInterface network;
    
    @Getter
    private boolean shuttingDown;
    
    @Getter
    private Executor generalThreadPool;
    
    private ConsoleManager console;
    
    public void run(String[] args){
        //Initialize console
        console = new ConsoleManager(this);
        console.startConsole();
        console.startFile("console.log");
        
        try {
            config = new ServerConfig();
        } catch (IOException ex) {
            logger.severe("Faild to load configuration file! ");
            ex.printStackTrace();
            return;
        }
        try {
            lang = new Lang(config.getConfig().getProperty(ServerConfig.LANG_FILE));
        } catch (IOException ex) {
            logger.severe("Faild to load language file! ");
            ex.printStackTrace();
            return;
        }
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        sessionRegister = new SessionRegister(this);

        //Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, Integer.parseInt(config.getConfig().getProperty("thread_pool_size"))));
        generalThreadPool = Executors.newFixedThreadPool(Integer.parseInt(config.getConfig().getProperty("thread_pool_size")));
        
        //Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getConfig().getProperty("udp_bind_ip"), config.getConfig().getProperty("udp_bind_port")));
        network = new RaknetInterface(this, 
                config.getConfig().getProperty("udp_bind_ip"),  //IP
                Integer.parseInt(config.getConfig().getProperty("udp_bind_port"))); //Port
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }
    
    public void onTick(){
        network.onTick();
        sessionRegister.onTick();
    }
    
    public void shutdown(){
        this.shuttingDown = true;
        
    }
}
