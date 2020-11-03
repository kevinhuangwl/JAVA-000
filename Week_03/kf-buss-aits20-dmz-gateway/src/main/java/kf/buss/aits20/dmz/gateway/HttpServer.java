package kf.buss.aits20.dmz.gateway;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import kf.buss.aits20.dmz.gateway.config.ProxySetting;
import kf.buss.aits20.dmz.gateway.filter.FilterChain;
import kf.buss.aits20.dmz.gateway.filter.HttpRequestFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServer {

	private int port;
	
	private FilterChain filterChain = new FilterChain();
	private List<ProxySetting> proxyList;

	public HttpServer(int port, List<HttpRequestFilter> filterList, List<ProxySetting> proxyList) {
		this.port = port;
		this.proxyList = proxyList;
		for(HttpRequestFilter filter : filterList) {
			filterChain.addFilter(filter);
		}
	}

	public void startup() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline()
								.addLast("codec", new HttpServerCodec())// HTTP 编解码
								.addLast("compressor", new HttpContentCompressor()) // HttpContent 压缩
								.addLast("aggregator", new HttpObjectAggregator(65536)) // HTTP 消息聚合
								.addLast(new HttpFilterHandler(filterChain)) //
								.addLast(new RouterHandler(proxyList));
						}

					});
			
			ChannelFuture f = b.bind(port).sync();
			log.info("Http服务开启，监听端口：{}", port);
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			throw new RuntimeException("Netty服务启动异常", e);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
