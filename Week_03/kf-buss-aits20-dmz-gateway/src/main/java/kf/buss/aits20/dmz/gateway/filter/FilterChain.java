package kf.buss.aits20.dmz.gateway.filter;

import java.util.Iterator;
import java.util.LinkedList;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class FilterChain {
	
	private LinkedList<HttpRequestFilter> filterList = new LinkedList<HttpRequestFilter>();

	public void addFilter(HttpRequestFilter filter) {
		filterList.add(filter);
	}
	
	public void doFilterChain(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
		Iterator<HttpRequestFilter> it = filterList.iterator();
		while(it.hasNext()) {
			it.next().doFilter(fullRequest, ctx);
		}
	}
}
