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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import org.dragonet.proxy.network.SessionRegister;
import org.dragonet.proxy.network.RaknetInterface;
import org.dragonet.proxy.configuration.Lang;
import org.dragonet.proxy.configuration.ServerConfig;
import org.dragonet.proxy.utilities.Versioning;
import org.dragonet.proxy.commands.CommandRegister;

import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.data.status.handler.ServerPingTimeHandler;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import org.mcstats.Metrics;
import lombok.Getter;

public class DragonProxy {

    public static void main(String[] args) {
        new DragonProxy().run(args);
    }
    public final static boolean IS_RELEASE = false;

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
    private InetSocketAddress remoteServerAddress;

    @Getter
    private boolean onlineMode;
    
    private ConsoleManager console;

    private Metrics metrics;

    private String motd;
	
	private boolean isDebug = false;

    public void run(String[] args) {
		
        //Need to initialize config before console
        try {
            config = new ServerConfig();
        } catch (IOException ex) {
            logger.severe("Failed to load configuration file!");
            ex.printStackTrace();
            return;
        }
		
		checkArguments(args);

        //Initialize console
        console = new ConsoleManager(this);
        console.startConsole();

        if(config.getConfig().getProperty("log_console").toLowerCase().contains("true")){
            console.startFile("console.log");
            logger.info("Saving console.log enabled"); //TODO: Translations
        } else {
            logger.info("Saving console.log disabled");
        }

        try {
            lang = new Lang(config.getConfig().getProperty(ServerConfig.LANG_FILE));
        } catch (IOException ex) {
            logger.severe("Failed to load language file!");
            ex.printStackTrace();
            return;
        }
        logger.info(lang.get(Lang.INIT_LOADING, Versioning.RELEASE_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PC_SUPPORT, Versioning.MINECRAFT_PC_VERSION));
        logger.info(lang.get(Lang.INIT_MC_PE_SUPPORT, Versioning.MINECRAFT_PE_VERSION));
        onlineMode = config.getConfig().getProperty("online_mode").toLowerCase().contains("true") || config.getConfig().getProperty("online_mode").trim().equals("1");
        remoteServerAddress = new InetSocketAddress(config.getConfig().getProperty("remote_ip"), Integer.parseInt(config.getConfig().getProperty("remote_port")));
        sessionRegister = new SessionRegister(this);
        commandRegister = new CommandRegister(this);
        if (IS_RELEASE) {
            try {
                metrics = new ServerMetrics(this);
                metrics.start();
            } catch (IOException ex) { }
        } else {
			logger.info("-----------------------------");
			logger.info(" This is a DEVELOPMENT build ");
			logger.info("     It may contain bugs     ");
			logger.info("-----------------------------");
		}

        //Create thread pool
        logger.info(lang.get(Lang.INIT_CREATING_THREAD_POOL, Integer.parseInt(config.getConfig().getProperty("thread_pool_size"))));
        generalThreadPool = Executors.newScheduledThreadPool(Integer.parseInt(config.getConfig().getProperty("thread_pool_size")));

        //Bind
        logger.info(lang.get(Lang.INIT_BINDING, config.getConfig().getProperty("udp_bind_ip"), config.getConfig().getProperty("udp_bind_port")));
        network = new RaknetInterface(this,
                config.getConfig().getProperty("udp_bind_ip"), //IP
                Integer.parseInt(config.getConfig().getProperty("udp_bind_port"))); //Port

        // MOTD
        motd = config.getConfig().getProperty("motd");
        motd = motd.replace("&", "§");
        motd = motd.replace("%ip%", remoteServerAddress.getHostString());
        motd = motd.replace("%port%", remoteServerAddress.getPort() + "");

        network.setBroadcastName(motd, -1, -1);
        ticker.start();
        logger.info(lang.get(Lang.INIT_DONE));

	//Ping the PC server to show the players online
        pingPCServer();
    }
	
	public boolean isDebug(){
		return isDebug;
	}

    public void onTick() {
        network.onTick();
        sessionRegister.onTick();
    }
	
	public void checkArguments(String[] args){
		for(String args : arg){
			if(arg.contains("--debug") || arg.contains("-debug")){
				isDebug = true;
				logger.info("Debug mode enabled");
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

    public void pingPCServer() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                if (shuttingDown) {
                    return;
                }
                try {
                    status();
                    Thread.sleep(5000);
                    pingPCServer();
                } catch (InterruptedException e) {
                }
            }
        });
        t.start();
    }
    public Logger getLogger() {
        return logger;
    }

    private void status() {
        MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
        Client client = new Client(remoteServerAddress.getHostString(), remoteServerAddress.getPort(), protocol, new TcpSessionFactory(Proxy.NO_PROXY));
        client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
        client.getSession().setConnectTimeout(10000);
        client.getSession().setFlag(MinecraftConstants.SERVER_INFO_HANDLER_KEY, new ServerInfoHandler() {
            @Override
            public void handle(Session session, ServerStatusInfo info) {
                network.setBroadcastName(motd, info.getPlayerInfo().getOnlinePlayers(), info.getPlayerInfo().getMaxPlayers());
                return;
            }
        });
        client.getSession().setFlag(MinecraftConstants.SERVER_PING_TIME_HANDLER_KEY, new ServerPingTimeHandler() {
            public void handle(Session session, long pingTime) {
            }
        });
        client.getSession().connect();
        while(client.getSession().isConnected()) {
            try {
                Thread.sleep(5);
            } catch(InterruptedException e) {
            }
        }
    }
}
