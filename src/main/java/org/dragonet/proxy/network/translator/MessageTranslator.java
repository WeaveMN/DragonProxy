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
package org.dragonet.proxy.network.translator;

import java.util.Iterator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.spacehq.mc.protocol.data.message.Message;

public final class MessageTranslator {

    public static String translate(Message message) {
        String ret = message.getFullText();
        /*
         * It is a JSON message?
         */
        try {
            /*
             * Do not ask me why, but json strings has colors.
             * Changing this allows colors in plain texts! yay!
             */
            JSONObject jObject = null;
            if (message.getFullText().startsWith("{") && message.getFullText().endsWith("}")) {
                jObject = new JSONObject(message.getFullText());
            } else {
                jObject = new JSONObject(message.toJsonString());
            }
            /*
             * Let's iterate!
             */
            ret = handleKeyObject(jObject);
        } catch (JSONException e) {
            /*
             * If any exceptions happens, then:
             * * The JSON message is buggy or
             * * It isn't a JSON message
             * 
             * So, if any exceptions happens, we send the original message
             */
        }
        return ret;
    }

    public static String handleKeyObject(JSONObject jObject) throws JSONException {
        String chatMessage = "";
        Iterator<String> iter = jObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                if (key.equals("color")) {
                    String color = jObject.getString(key);
                    if (color.equals("light_purple")) {
                        chatMessage = chatMessage + "§d";
                    }

                    if (color.equals("blue")) {
                        chatMessage = chatMessage + "§9";
                    }
                    if (color.equals("aqua")) {
                        chatMessage = chatMessage + "§b";
                    }
                    if (color.equals("gold")) {
                        chatMessage = chatMessage + "§6";
                    }
                    if (color.equals("green")) {
                        chatMessage = chatMessage + "§a";
                    }
                    if (color.equals("white")) {
                        chatMessage = chatMessage + "§f";
                    }
                    if (color.equals("yellow")) {
                        chatMessage = chatMessage + "§e";
                    }
                    if (color.equals("gray")) {
                        chatMessage = chatMessage + "§7";
                    }
                    if (color.equals("red")) {
                        chatMessage = chatMessage + "§c";
                    }
                    if (color.equals("black")) {
                        chatMessage = chatMessage + "§0";
                    }

                    if (color.equals("dark_green")) {
                        chatMessage = chatMessage + "§2";
                    }
                    if (color.equals("dark_gray")) {
                        chatMessage = chatMessage + "§8";
                    }
                    if (color.equals("dark_red")) {
                        chatMessage = chatMessage + "§4";
                    }
                    if (color.equals("dark_blue")) {
                        chatMessage = chatMessage + "§1";
                    }
                    if (color.equals("dark_aqua")) {
                        chatMessage = chatMessage + "§3";
                    }
                    if (color.equals("dark_purple")) {
                        chatMessage = chatMessage + "§5";
                    }
                }

                if (key.equals("bold")) {
                    String bold = jObject.getString(key);
                    if (bold.equals("true")) {
                        chatMessage = chatMessage + "§l";
                    }
                }
                if (key.equals("italic")) {
                    String bold = jObject.getString(key);
                    if (bold.equals("true")) {
                        chatMessage = chatMessage + "§o";
                    }
                }
                if (key.equals("underlined")) {
                    String bold = jObject.getString(key);
                    if (bold.equals("true")) {
                        chatMessage = chatMessage + "§n";
                    }
                }
                if (key.equals("strikethrough")) {
                    String bold = jObject.getString(key);
                    if (bold.equals("true")) {
                        chatMessage = chatMessage + "§m";
                    }
                }
                if (key.equals("obfuscated")) {
                    String bold = jObject.getString(key);
                    if (bold.equals("true")) {
                        chatMessage = chatMessage + "§k";
                    }
                }

                if (key.equals("text")) {
                    /*
                     * We only need the text message from the JSON.
                     */
                    String jsonMessage = jObject.getString(key);
                    chatMessage = chatMessage + jsonMessage;
                    continue;
                }
                if (jObject.get(key) instanceof JSONArray) {
                    chatMessage += handleKeyArray(jObject.getJSONArray(key));
                }
                if (jObject.get(key) instanceof JSONObject) {
                    chatMessage += handleKeyObject(jObject.getJSONObject(key));
                }
            } catch (JSONException e) {
            }
        }
        return chatMessage;
    }

    public static String handleKeyArray(JSONArray jObject) throws JSONException {
        String chatMessage = "";
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
        return chatMessage;
    }
}
