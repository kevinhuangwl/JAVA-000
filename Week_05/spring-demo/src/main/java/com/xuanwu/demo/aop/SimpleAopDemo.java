package com.xuanwu.demo.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class SimpleAopDemo {

	public static void main(String[] args) {
		List<String> list =  (List) Proxy.newProxyInstance(SimpleAopDemo.class.getClassLoader(), new Class[] {List.class}, new Handler(new ArrayList()));
		list.add("aaa");
		list.size();
		list.remove(0);
	}
}

class Handler implements InvocationHandler{
	
	private List list;
	public Handler(List list) {
		this.list = list;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(String.format("调用%s方法", method.getName()));
		return method.invoke(list, args);
	}
	
}
