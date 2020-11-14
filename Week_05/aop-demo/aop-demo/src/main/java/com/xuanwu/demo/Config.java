package com.xuanwu.demo;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	public MyObject getBean() {
		MyObject obj = new MyObject();
		obj.setAge(33);
		obj.setDemo("sus");
		obj.setDemoList(Arrays.asList("北京","上海"));
		return obj;
	}
}
