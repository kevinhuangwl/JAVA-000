package com.xuanwu.demo.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.xuanwu.starter.ISchool;

@SpringBootApplication
@Component
public class App implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	ISchool school;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("运行");
		school.ding();
	}
}
