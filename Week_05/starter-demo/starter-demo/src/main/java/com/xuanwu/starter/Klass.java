package com.xuanwu.starter;

import lombok.Data;

import java.util.List;

@Data
public class Klass { 
	
	private List<Student> students;
	public Klass(List<Student> students) {
		this.students = students;
	}
    
    
    public void dong(){
        System.out.println(this.getStudents());
    }
    
}
