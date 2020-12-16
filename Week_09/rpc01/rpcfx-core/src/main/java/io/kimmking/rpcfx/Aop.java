package io.kimmking.rpcfx;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Aspect
public class Aop {
	
	private String url;
	
	private static Map<Class<?>, String> classUrlMap = new HashMap<>();
	
	public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
	
	public static <T> T make(Class<T> clz, String url) {
		classUrlMap.put(clz, url);
		return mock(clz);
	}

	@Pointcut("@annotation(io.kimmking.rpcfx.Rpc)")
	public void pointcut() {
	}
	
	@Around("pointcut()")
	public Object advice(ProceedingJoinPoint joinPoint) throws Exception {
		System.out.println("切面方法");
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		Class clz = signature.getDeclaringType();
		Method method = signature.getMethod();
		RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(clz.getName());
        request.setMethod(method.getName());
        request.setParams(joinPoint.getArgs());

        RpcfxResponse response = post(request, url);

        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        if(!response.isStatus()) {
        	throw response.getException();
        }

        return response.getResult();
	}
	
	private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        System.out.println("req json: "+reqJson);

        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSONTYPE, reqJson))
                .build();
        String respJson = client.newCall(request).execute().body().string();
        System.out.println("resp json: "+respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
