package kf.buss.aits20.dmz.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpRequestFilter {
    
    void doFilter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
    
}
