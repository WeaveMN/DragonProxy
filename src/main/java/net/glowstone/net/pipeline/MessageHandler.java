package net.glowstone.net.pipeline;

import com.flowpowered.networking.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import org.dragonet.proxy.network.DownstreamSession;

/**
 * Experimental pipeline component, based on flow-net's MessageHandler.
 */
public final class MessageHandler extends SimpleChannelInboundHandler<Message> {

    @Getter
    private final DownstreamSession session;
    
    public MessageHandler(DownstreamSession session){
        this.session = session;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message i) {
        session.messageReceived(i);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //((GlowSession) session.get()).idle(); // todo: find a more elegant way to do this in the future
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        session.onError(cause);
    }

}
