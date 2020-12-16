package com.xuanwu.masterslavedb.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 池化数据源配置对象
 *
 */
@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "db.common-pool-setting")
public class PoolingDataSourceProperties extends DataSourceProperties implements InitializingBean{

	//初始化大小，最小，最大
	private int initialSize;
	private int minIdle;
	private int maxActive;
	
	//配置获取连接等待超时的时间
	private int maxWait;
	
	//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
	private int timeBetweenEvictionRunsMillis;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("\n{}连接池配置:\n"
				+ "url: {}\n"
				+ "initialSize: {}\n"
				+ "minIdle: {}\n"
				+ "maxActive: {}\n"
				+ "maxWait: {}\n"
				+ "timeBetweenEvictionRunsMillis: {}", 
				getName(), getUrl(), getInitialSize(), getMinIdle(), 
				getMaxActive(), getMaxWait(), getTimeBetweenEvictionRunsMillis());
	}
}