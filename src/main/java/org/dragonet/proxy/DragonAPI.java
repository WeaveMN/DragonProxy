package org.dragonet.proxy;

import static org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.net.packet.minecraft.ChatPacket;

public class DragonAPI {
	
	private DragonProxy proxy;
	
	public DragonAPI(DragonProxy proxy){
		this.proxy = proxy;
	}
	
	public static void sendChat(String chat){
	    if (chat.contains("\n")){
		    String[] lines = chat.split("\n");
		    for (String line : lines) {
			    sendChat(line);
		    }
		}
		return;
		
	    ChatPacket pk = new ChatPacket();
	    pk.type = ChatPacket.TextType.CHAT;
	    pk.source = "";
		pk.message = chat;
	    sendPacket(pk, true);
	}
	
}