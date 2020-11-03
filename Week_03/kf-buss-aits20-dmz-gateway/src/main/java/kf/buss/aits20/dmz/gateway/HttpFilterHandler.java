package kf.buss.aits20.dmz.gateway;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import kf.buss.aits20.dmz.gateway.filter.FilterChain;

public class HttpFilterHandler extends ChannelInboundHandlerAdapter {
	
	private FilterChain filterChain;
	
	public HttpFilterHandler(FilterChain filterChain) {
		this.filterChain = filterChain;
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	try {    		
    		FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
    		filterChain.doFilterChain(fullHttpRequest, ctx);

    		ctx.fireChannelRead(fullHttpRequest);
    	} catch(Exception e) {
    		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY);
    		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    	}
    	
    }
}