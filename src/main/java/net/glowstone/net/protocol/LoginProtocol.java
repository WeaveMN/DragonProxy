package net.glowstone.net.protocol;

import net.glowstone.net.codec.login.EncryptionKeyResponseCodec;
import net.glowstone.net.codec.login.LoginStartCodec;
import net.glowstone.net.message.login.EncryptionKeyResponseMessage;
import net.glowstone.net.message.login.LoginStartMessage;

public final class LoginProtocol extends GlowProtocol {
    public LoginProtocol() {
        super("LOGIN", 5);

        outbound(0x00, LoginStartMessage.class, LoginStartCodec.class);
        outbound(0x01, EncryptionKeyResponseMessage.class, EncryptionKeyResponseCodec.class);

        //inbound(0x00, KickMessage.class, KickCodec.class);
        //inbound(0x01, EncryptionKeyRequestMessage.class, EncryptionKeyRequestCodec.class);
        //inbound(0x02, LoginSuccessMessage.class, LoginSuccessCodec.class);
        //inbound(0x03, SetCompressionMessage.class, SetCompressionCodec.class);
    }
}
