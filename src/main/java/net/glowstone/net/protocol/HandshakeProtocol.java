package net.glowstone.net.protocol;

public final class HandshakeProtocol extends GlowProtocol {
    public HandshakeProtocol() {
        super("HANDSHAKE", 0);
        //inbound(0x00, HandshakeMessage.class, HandshakeCodec.class, HandshakeHandler.class);
    }
}
