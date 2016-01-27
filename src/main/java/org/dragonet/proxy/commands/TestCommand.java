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
package org.dragonet.proxy.commands;

import org.dragonet.net.packet.minecraft.ChangeDimensionPacket;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;

public class TestCommand implements ConsoleCommand {

    @Override
    public void execute(DragonProxy proxy, String[] args) {
        UpstreamSession cli = proxy.getSessionRegister().getAll().values().toArray(new UpstreamSession[0])[0];
        cli.sendPacket(new ChangeDimensionPacket(0f, 64f, 0f, (byte)0));
        LoginStatusPacket stat = new LoginStatusPacket();
        stat.status = LoginStatusPacket.PLAYER_SPAWN;
        cli.sendPacket(stat);
    }

}
