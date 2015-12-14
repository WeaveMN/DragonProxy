package net.glowstone.net.protocol;

import net.glowstone.net.codec.KickCodec;
import net.glowstone.net.codec.SetCompressionCodec;
import net.glowstone.net.codec.login.EncryptionKeyRequestCodec;
import net.glowstone.net.codec.login.EncryptionKeyResponseCodec;
import net.glowstone.net.codec.login.LoginStartCodec;
import net.glowstone.net.codec.login.LoginSuccessCodec;
import net.glowstone.net.message.KickMessage;
import net.glowstone.net.message.SetCompressionMessage;
import net.glowstone.net.message.login.EncryptionKeyRequestMessage;
import net.glowstone.net.message.login.EncryptionKeyResponseMessage;
import net.glowstone.net.message.login.LoginStartMessage;
import net.glowstone.net.message.login.LoginSuccessMessage;
import org.dragonet.proxy.network.handler.login.EncryptionKeyRequestHandler;
import org.dragonet.proxy.network.handler.login.KickHandler;
import org.dragonet.proxy.network.handler.login.LoginSuccessHandler;
import org.dragonet.proxy.network.handler.login.SetCompressionHandler;

public final class LoginProtocol extends GlowProtocol {
    public LoginProtocol() {
        super("LOGIN", 5);

        outbound(0x00, LoginStartMessage.class, LoginStartCodec.class);
        outbound(0x01, EncryptionKeyResponseMessage.class, EncryptionKeyResponseCodec.class);

        inbound(0x00, KickMessage.class, KickCodec.class, KickHandler.class);
        inbound(0x01, EncryptionKeyRequestMessage.class, EncryptionKeyRequestCodec.class, EncryptionKeyRequestHandler.class);
        inbound(0x02, LoginSuccessMessage.class, LoginSuccessCodec.class, LoginSuccessHandler.class);
        inbound(0x03, SetCompressionMessage.class, SetCompressionCodec.class, SetCompressionHandler.class);
    }
}
