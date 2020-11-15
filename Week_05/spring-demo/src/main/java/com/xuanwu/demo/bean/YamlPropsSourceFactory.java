package com.xuanwu.demo.bean;

import java.io.IOException;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropsSourceFactory implements PropertySourceFactory{

	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean fBean = new YamlPropertiesFactoryBean();
		fBean.setResources(resource.getResource());
		return new PropertiesPropertySource(name, fBean.getObject());
	}

}
