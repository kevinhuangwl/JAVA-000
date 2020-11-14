package com.xuanwu.demo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MyObject {

	private String demo;
	
	private int age;
	
	private List<String> demoList;
}
