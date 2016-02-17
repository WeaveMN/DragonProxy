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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.proxy.commands.CommandRegister;

import org.mcstats.Metrics;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

public class DragonProxy {

    public static void main(String[] args) {
        new DragonProxy().run(args);
    }
    public final static boolean IS_RELEASE = false; //DO NOT CHANGE, ONLY ON A RELEASE

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
    private ScheduledExecutorService generalThreadPool;

    @Getter
    private CommandRegister commandRegister;

    @Getter
    private String authMode;

    private ConsoleManager console;

    private Metrics metrics;

    private String motd;

    private boolean isDebug = false;

    public void run(String[] args) {

        //Need to initialize config before console
        try {
            config = new Yaml().loadAs(new FileInputStream("config.yml"), ServerConfig.class);
        } catch (IOException ex) {
            logger.severe("Failed to load configuration file!");
            ex.printStackTrace();
            return;
        }

        //Initialize console
        console = new ConsoleManager(this);
        console.startConsole();

        checkArguments(args);

        if(config.isLog_console()){
            console.startFile("console.log");
            logger.info("Saving console output enabled"); //TODO: Translations
        } else {
            logger.info("Saving console output disabled");
        }

        try {
            lang = new Lang(config.getLang());
        } catch (IOException ex) {
            logger.severe("Failed to load language file: " + config.getLang() + "!");
            ex.printStackTrace();
            return;
        }
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, Versioning.MINECRAFT_PC_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, Versioning.MINECRAFT_PE_VERSION));
        authMode = config.getMode().toLowerCase();
        if(!authMode.equals("cls") && !authMode.equals("online") && !authMode.equals("offline")){
            logger.severe("Invalid 'mode' option detected, must be cls/online/offline, you set it to '" + authMode + "'! ");
            return;
        }
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);
        if (IS_RELEASE) {
            try {
                metrics = new ServerMetrics(this);
                metrics.start();
            } catch (IOException ex) { }
        } else {
            logger.info("This is a development build. It may contain bugs. Do not use on production");
        }

        //Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, config.getThread_pool_size()));
        generalThreadPool = Executors.newScheduledThreadPool(config.getThread_pool_size());

        //Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getUdp_bind_ip(), config.getUdp_bind_port()));
        network = new RaknetInterface(this,
                config.getUdp_bind_ip(), //IP
                config.getUdp_bind_port()); //Port

        //MOTD
        motd = config.getMotd();
        motd = motd.replace("&", "§");

        network.setBroadcastName(motd, -1, -1);
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));
    }

    public boolean isDebug(){
        return isDebug;
    }

    public void onTick() {
        network.onTick();
        sessionRegister.onTick();
    }

    public void checkArguments(String[] args){
        for(String arg : args){
            if(arg.toLowerCase().contains("--debug")){
                isDebug = true;
                logger.info("--- DEBUG MODE ENABLED ---");
            }
        }
    }

    public void shutdown() {
        logger.info(lang.get(Lang.SHUTTING_DOWN));

        isDebug = false;
        this.shuttingDown = true;
        network.shutdown();
        try{
            Thread.sleep(2000); //Wait for all clients disconnected
        } catch (Exception e) {
        }
        System.exit(0);
    }
    
    public Logger getLogger() {
        return logger;
    }
}
