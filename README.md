![Logo](http://dragonet.org/assets/img/Dragonet.png)

# DragonProxy
[![Build status indicator](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master.svg?style=badge)](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master)  
**If on mobile, open to show the whole file**

IRC: \#Dragonet on irc.freenode.net


A proxy for **Minecraft: Pocket Edition/Windows 10 Edition** connecting to **Minecraft PC/Mac** servers.


Test servers: https://github.com/DragonetMC/DragonProxy/blob/master/TestServers.md

![Screenshot](https://raw.githubusercontent.com/DragonetMC/DragonProxy/master/screenshots/Hypixel.png)
#### Hypixel on Pocket/Win 10 Edition! 
#### Join on Pocket/Win 10 Edition here `hypixel.dragonet.org`, port `19132`(default). 

## Installation
[![Build status indicator](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master.svg?style=badge)](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master) [Download from CircleCI](https://circleci.com/gh/DragonetMC/DragonProxy/tree/master)

#### Supported Remote Server Types
- `Bukkit`/`Spigot`/`Glowstone`/`SpongeVanilla`/`BungeeCord`

## Usage
Note: Target server must be off-line mode for now, we may add online authentication later. 
* Step 1. Install Java 8(or higher), download the JAR file. 
* Step 2. Run the proxy by running command: `java -Xmx256M -jar dragonproxy-x.x.x.jar` where `256M` indicates memory size and `dragonproxy-x.x.x.jar` is the file name. 
* Step 3. Close the server by typing `stop` in the console. 
* Step 4. Edit `config.properties` and change `remote_ip` and `remote_port` to your Minecraft PC/Mac server, such as `127.0.0.1` and `25565`. 
* Step 5. Do `Step 3` again, and you should be able to connect to a PC/Mac server through your computer/server from Pocket/Win 10 Edition. 

## Community

### Website: [http://dragonet.org](http://dragonet.org)
### Forums: [http://forums.dragonet.org](http://forums.dragonet.org)

## Development

### Cloning the project

```
git clone git@github.com:DragonetMC/DragonProxy.git
```

### Using IntelliJ IDEA

* Download and install [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/#chooseYourEdition) (free download)
* Just "open" the project in **IntelliJ**, it should work and compile seamlessly
* Add "Lombok Plugin" to add first class support for Lombok annotations in **IntelliJ**

## Credits
* Netty Networking Library([Link](http://netty.io))
* Google Guava([Link](http://code.google.com/p/guava-libraries/))