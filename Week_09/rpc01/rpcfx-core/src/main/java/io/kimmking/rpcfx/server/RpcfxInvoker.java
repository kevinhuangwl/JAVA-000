package io.kimmking.rpcfx.server;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import io.kimmking.rpcfx.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

public class RpcfxInvoker implements ApplicationContextAware{

	private ApplicationContext ctx;
	
    public RpcfxResponse<?> invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse<>();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        try {
        	Class<?> clz = Class.forName(serviceClass);
        	Object service = ctx.getBean(clz);
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            // 两次json序列化能否合并成一个
            response.setResult(result);
            response.setStatus(true);
            return response;
        } catch (Exception e) {
        	e.printStackTrace();
            // 3.Xstream

            // 2.封装一个统一的RpcfxException
            // 客户端也需要判断异常
            response.setException(new RpcfxException(e));
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

}
