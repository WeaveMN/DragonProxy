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
package org.dragonet.proxy.network.translator.pc;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dragonet.net.packet.minecraft.ChatPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;

public class PCChatPacketTranslator implements PCPacketTranslator<ServerChatPacket> {
	String chatMessage = "";
	
    @Override
    public PEPacket[] translate(UpstreamSession session, ServerChatPacket packet) {
        ChatPacket ret = new ChatPacket();
        /*
         * Reset the chat message so we can parse the JSON again (if needed)
         */
        chatMessage = "";
        ret.source = "";
        ret.message = packet.getMessage().getFullText();
        switch (packet.getType()) {
            case CHAT:
                ret.type = ChatPacket.TextType.CHAT;
                /*
                 * It is a JSON message?
                 */
				try {
					JSONObject jObject = new JSONObject(ret.message);
					/*
					 * Let's iterate!
					 */
					handleKeyObject(jObject);
					ret.message = chatMessage;
				} catch (JSONException e) {
					/*
					 * If any exceptions happens, then:
					 * * The JSON message is buggy or
					 * * It isn't a JSON message
					 * 
					 * So, if any exceptions happens, we send the original message
					 */
				} 
                break;
            case NOTIFICATION:
            case SYSTEM:
            default:
                ret.type = ChatPacket.TextType.CHAT;
                break;
        }
        return new PEPacket[]{ret};
    }
    
    public void handleKeyObject(JSONObject jObject) throws JSONException {
    	Iterator<String> iter = jObject.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    try {
		    	if (key.equals("text")) {
		    		/*
		    		 * We only need the text message from the JSON.
		    		 */
		    		String jsonMessage = jObject.getString(key);
		    		chatMessage = chatMessage + jsonMessage;
		    		continue;
		    	}
		    	if (jObject.get(key) instanceof JSONArray) {
		    		handleKeyArray(jObject.getJSONArray(key));
		    	}
		    	if (jObject.get(key) instanceof JSONObject) {
		    		handleKeyObject(jObject.getJSONObject(key));
		    	}
		    } catch (JSONException e) {
		    }
		}
    }
    
    
    public void handleKeyArray(JSONArray jObject) throws JSONException {
    	JSONObject jsonObject = jObject.toJSONObject(jObject);
    	Iterator<String> iter = jsonObject.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    try {
	    		/*
	    		 * We only need the text message from the JSON.
	    		 */
		    	if (key.equals("text")) {
		    		String jsonMessage = jsonObject.getString(key);
		    		chatMessage = chatMessage + jsonMessage;
		    		continue;
		    	}
		    	if (jsonObject.get(key) instanceof JSONArray) {
		    		handleKeyArray(jsonObject.getJSONArray(key));
		    	}
		    	if (jsonObject.get(key) instanceof JSONObject) {
		    		handleKeyObject(jsonObject.getJSONObject(key));
		    	}
		    } catch (JSONException e) {
		    }
		}
    }
}
