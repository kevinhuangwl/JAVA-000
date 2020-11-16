package com.xuanwu.starter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Bean
	public Klass getKlass() {
		List<Student> students = new ArrayList();
		for(int i=0; i<10; i++) {
			students.add(Student.create());
		}
		Klass klz = new Klass(students);
		return klz;
	}
	
	@Bean(name="student100")
	public Student getStudent() {
		return Student.create();
	}
	
	@Bean
	public ISchool getSchool() {
		return new School();
	}
	
	
}
