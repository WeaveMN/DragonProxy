# DragonProxy
[![Build status indicator](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master.svg?style=badge)](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master)  
A proxy for Minecraft: Pocket Edition connecting to Minecraft PC/Mac servers. 

### This project is under early-development stage, it CAN NOT work yet. 

## Usage
* Step 1. Install Java 8(or higher), download the JAR file. 
* Step 2. Set up a normal Minecraft PC/Mac server. You can also use `Bukkit`/`Spigot`/`Glowstone`/`SpongeVanilla`. 
* Step 3. Run the proxy by running command: `java -Xmx256M -jar dragonproxy-x.x.x.jar` where `256M` indicates memory size and `dragonproxy-x.x.x.jar` is the file name. 
* Step 4. Close the server by typing `stop` in the console. 
* Step 5. Edit `config.properties` and change `remote_ip` and `remote_port` to your Minecraft PC/Mac server, such as `127.0.0.1` and `25565`. 
* Step 6. Do `Step 3` again, and you should be able to connect to a PC/Mac server through your computer/server from a MCPE. 

## Installation
[![Build status indicator](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master.svg?style=badge)](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master) [Download from CircleCI](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master)

### Credits
* Glowstone's Netty Networking Library in Java([Link](http://github.com/GlowstonePlusPlus/GlowstonePlusPlus))
* Glowstone's fork of Bukkit API([Link](http://github.com/GlowstonePlusPlus/Glowkit))
