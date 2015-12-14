package net.glowstone.net.pipeline;

import com.flowpowered.networking.Message;
import com.flowpowered.networking.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import org.dragonet.proxy.network.DownstreamSession;

/**
 * Experimental pipeline component, based on flow-net's MessageHandler.
 */
public final class MessageHandler extends SimpleChannelInboundHandler<Message> {

    private final AtomicReference<Session> sessionRef = new AtomicReference<>(null);
    
    @Getter
    private final DownstreamSession downstream;
    
    public MessageHandler(DownstreamSession session){
        this.downstream = session;
    }

        @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel c = ctx.channel();
        Session s = downstream.newSession(c);
        if (!sessionRef.compareAndSet(null, s)) {
            throw new IllegalStateException("Session may not be set more than once");
        }
        s.onReady();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Session session = this.sessionRef.get();
        session.onDisconnect();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message i) {
        sessionRef.get().messageReceived(i);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //((GlowSession) session.get()).idle(); // todo: find a more elegant way to do this in the future
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        downstream.onError(cause);
    }

}
