package io.kimmking.rpcfx.client;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.DisposableBean;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class NettyClient implements DisposableBean{
	
	private Bootstrap b;
	
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private ConcurrentHashMap<Channel, CompletableFuture<FullHttpResponse>> futureMap = new ConcurrentHashMap<>();
	
	public void init() {
		b = new Bootstrap(); 
		b.group(workerGroup); 
		b.channel(NioSocketChannel.class); 
		b.option(ChannelOption.SO_KEEPALIVE, true); 
		b.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
					.addLast(new LogHandler())
					.addLast(new HttpClientCodec())
					.addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
					.addLast(new ServerResponseHandler(futureMap));
			}
		});
	}
	
	public CompletableFuture<FullHttpResponse> send(URL url, FullHttpRequest req) throws Exception {
		ChannelFuture channelFuture = b.connect(url.getHost(), url.getPort());
		channelFuture.sync();
		Channel ch = channelFuture.channel();
		ch.writeAndFlush(req).sync();
		CompletableFuture<FullHttpResponse> future = new CompletableFuture<>();
		futureMap.put(ch, future);
		return future;
	}

	@Override
	public void destroy() throws Exception {
		workerGroup.shutdownGracefully();
	}
	
	public static class ServerResponseHandler extends ChannelInboundHandlerAdapter{
		
		private ConcurrentHashMap<Channel, CompletableFuture<FullHttpResponse>> futureMap;
		
		public ServerResponseHandler(ConcurrentHashMap<Channel, CompletableFuture<FullHttpResponse>> futureMap) {
			this.futureMap = futureMap;
		}
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			FullHttpResponse response = (FullHttpResponse) msg;
			CompletableFuture<FullHttpResponse> future = futureMap.remove(ctx.channel());
        	future.complete(response);
        	ctx.channel().close();
		}

	}
	
	public static class LogHandler extends ChannelOutboundHandlerAdapter{

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			ByteBuf buf0 = buf.copy();
			byte[] data = new byte[buf0.capacity()];
			buf0.readBytes(data);
			System.out.println("log:" + new String(data));
			super.write(ctx, msg, promise);
		}
		
	}
	
	
}
