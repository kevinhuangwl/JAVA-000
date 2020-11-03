package kf.buss.aits20.dmz.gateway.filter;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AddHeaderFilter implements HttpRequestFilter  {
	

	@Override
	public void doFilter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
		log.info("add header read");
		HttpHeaders headers = fullRequest.headers();
		headers.add("nio", "huangwl");
		
	}

	
}
