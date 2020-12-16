package com.xuanwu.masterslavedb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@EnableConfigurationProperties
public class DataSourceConfig extends BaseDataSourceConfig{
	
	@Bean(name="masterProp")
	@ConfigurationProperties("db.master")
	public PoolingDataSourceProperties masterDataSourceProperties() {
	    return new PoolingDataSourceProperties();
	}

	@Bean(name="masterDS")
	public DataSource getMasterDataSource(@Qualifier("masterProp") PoolingDataSourceProperties props) {
		return createDataSource(props);	
	}
	
	@Bean(name="masterTemplate")
	public JdbcTemplate getMasterJdbcTemplate(@Qualifier("masterDS") DataSource ds) {
		return new JdbcTemplate(ds);
	}
	
	
	@Bean(name="slaveProp")
	@ConfigurationProperties("db.slave")
	public PoolingDataSourceProperties slaveDataSourceProperties() {
	    return new PoolingDataSourceProperties();
	}

	@Bean(name="slaveDS")
	public DataSource getSlaveDataSource(@Qualifier("slaveProp") PoolingDataSourceProperties props) {
		return createDataSource(props);	
	}
	
	@Bean(name="slaveTemplate")
	public JdbcTemplate getSlaveJdbcTemplate(@Qualifier("slaveDS") DataSource ds) {
		return new JdbcTemplate(ds);
	}
}
