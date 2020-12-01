package com.xuanwu.demo.bean;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class MyObject{
	
	@Value("${obj.demo}")
	private String demo;
	@Value("${obj.age}")
	private int age;
	
	private List<String> demoList;

}
