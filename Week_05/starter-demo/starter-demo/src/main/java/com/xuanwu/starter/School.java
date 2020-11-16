package com.xuanwu.starter;

import lombok.Data;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;


@Data
public class School implements ISchool {
	
	@Autowired
    Klass class1;
    
    @Resource(name = "student100")
    Student student100;

    @Override
    public void ding(){
    
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
        
    }
    
}
