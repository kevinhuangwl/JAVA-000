package com.xuanwu.demo.bean;

import java.io.IOException;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import com.alibaba.fastjson.JSON;

public class JavaProxyDemo {
	
	
	public static void main(String[] args) throws IOException {
//		byXml();
//		byProperties();
//		byBeanDefinitionBuilder();
//		byAbstractBeanDefinition();
//		byAnnotation();
		byYaml();
	}
	
	public static void byXml() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println(ctx.getBean("demo"));
		System.out.println(JSON.toJSONString(ctx.getBean("demoList")));
		System.out.println(JSON.toJSONString(ctx.getBean("myObj")));
		System.out.println(JSON.toJSONString(ctx.getBean("emailsMap")));
		System.out.println(JSON.toJSONString(ctx.getBean("emailsSet")));
		System.out.println(JSON.toJSONString(ctx.getBean("myList")));
		System.out.println(JSON.toJSONString(ctx.getBean("myMap")));
	}
	
	public static void byProperties() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(beanFactory);
		//解决中文乱码问题
		EncodedResource encodedResource = new EncodedResource(new ClassPathResource("bean-config.properties"), "UTF-8");
		propReader.loadBeanDefinitions(encodedResource);
		System.out.println(JSON.toJSONString(beanFactory.getBean("obj")));
		System.out.println(JSON.toJSONString(beanFactory.getBean("list")));
	}

	public static void byYaml() throws IOException {
		EncodedResource encodedResource = new EncodedResource(new ClassPathResource("bean-config.yml"), "UTF-8");
		YamlPropsSourceFactory factory = new YamlPropsSourceFactory();
		PropertySource<?> ps = factory.createPropertySource("yaml", encodedResource);
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.getEnvironment().getPropertySources().addFirst(ps);
		ctx.register(MyObject.class);
		ctx.refresh();
		System.out.println(JSON.toJSONString(ctx.getBean("myObject")));
		ctx.close();
		
	}

	public static void byAnnotation() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Config.class);
		ctx.refresh();
		System.out.println(JSON.toJSONString(ctx.getBean(MyObject.class)));
		ctx.close();
	}
	
	public static void byBeanDefinitionBuilder() {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MyObject.class);
		builder.addPropertyValue("demo", "hello");
		builder.addPropertyValue("age", 20);
		BeanDefinition beanDefinition = builder.getBeanDefinition();
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("myObj", beanDefinition);
		System.out.println(JSON.toJSONString(beanFactory.getBean("myObj")));
		
	}
	
	public static void byAbstractBeanDefinition() {
		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
		beanDefinition.setBeanClass(MyObject.class);
		MutablePropertyValues values = new MutablePropertyValues();
		values.add("demo", "hello").add("age", 20);
		beanDefinition.setPropertyValues(values);
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("myObj", beanDefinition);
		System.out.println(JSON.toJSONString(beanFactory.getBean("myObj")));
	}
	
	
	
	
	
	
}

