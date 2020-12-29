package io.kimmking.rpcfx.client;


import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.EmptyHttpHeaders;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }
    
    private static final NettyClient client = new NettyClient();
    
    static {
    	client.init();
    }

    public static <T> T create(final Class<T> serviceClass, final String url) {

    	Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(serviceClass);
        enhancer.setCallback(new MethodInterceptor() {
        	
			@Override
			public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				RpcfxRequest request = new RpcfxRequest();
	            request.setServiceClass(serviceClass.getName());
	            request.setMethod(method.getName());
	            request.setParams(args);

	            RpcfxResponse response = post(request, url);

	            // 这里判断response.status，处理异常
	            // 考虑封装一个全局的RpcfxException
	            if(!response.isStatus()) {
	            	throw response.getException();
	            }

	            return response.getResult();
			}
			
			private RpcfxResponse post(RpcfxRequest rpcReq, String url) throws Exception {
				HttpHeaders header = new DefaultHttpHeaders();
				String reqStr = JSON.toJSONString(rpcReq);
				byte[] buf = reqStr.getBytes();
				header.add("Host", "127.0.0.1");
				header.add("Content-Type", "application/json; charset=utf-8");
				header.add("Content-Length", buf.length);
				FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "", Unpooled.wrappedBuffer(buf)
						, header, header );
				System.out.println(req);
				CompletableFuture<FullHttpResponse> future = client.send(new URL(url), req);
				FullHttpResponse response = future.get();
				String respStr = response.content().toString(io.netty.util.CharsetUtil.UTF_8);
				System.out.println("resp json: "+ respStr );
				return JSON.parseObject(respStr, RpcfxResponse.class);
	        }
		});
        return (T) enhancer.create();
    }
    
    public static void stop() throws Exception {
    	client.destroy();
    }
}
