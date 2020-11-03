package kf.buss.aits20.dmz.gateway;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import kf.buss.aits20.dmz.gateway.config.ProxySetting;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class RouterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private List<ProxySetting> proxyList;
	
	private OkHttpClient client = new OkHttpClient.Builder().build();
	
	public RouterHandler(List<ProxySetting> proxyList) {
		this.proxyList = proxyList;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		HttpHeaders headers = req.headers();
		for(Entry<String, String> e : headers.entries()) {
			log.info("header: {} = {}", e.getKey(), e.getValue());
		}
		
		String uri = req.uri();
		String targetEndpoint = null;
		for(ProxySetting proxySetting : proxyList) {
			if(proxySetting.isMatchPath(uri)) {
				targetEndpoint = proxySetting.getTarget() + uri;
				log.info("匹配路径：{}, 转发路径：{}", proxySetting.getPath(), targetEndpoint);
				break;
			}
		}
		
		FullHttpResponse response;
		
		if(StringUtils.isBlank(targetEndpoint)) {
			log.warn("没有匹配的转发路径对应请求路径：{}", uri);
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		String method = req.method().name();
		String contentType = headers.get("Content-Type", "*/*");
		RequestBody body = RequestBody.create(MediaType.parse(contentType), req.content().array());
		Request serverReq = req.method() == HttpMethod.GET 
				? new Request.Builder().url(targetEndpoint).get().build()
				: new Request.Builder().url(targetEndpoint).method(method, body).build();
		
		client.newCall(serverReq).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SERVICE_UNAVAILABLE);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Headers headers = response.headers();
				byte[] content = response.body().bytes();
				DefaultHttpHeaders wrapHeaders = new DefaultHttpHeaders();
				for(String name : headers.names()) {
					wrapHeaders.add(name, headers.get(name));
				}
				ByteBuf wrapContent = Unpooled.wrappedBuffer(content);
				FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, wrapContent, wrapHeaders, wrapHeaders);
				
				ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
			}}
		);
		

	}

}
