package com.xuanwu.masterslavedb.config;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

public class BaseDataSourceConfig {

	protected DataSource createDataSource(PoolingDataSourceProperties props) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(props.getDriverClassName());
		dataSource.setUrl(props.getUrl());
		dataSource.setUsername(props.getUsername());
		dataSource.setPassword(props.getPassword());
		dataSource.setInitialSize(props.getInitialSize());
		dataSource.setMinIdle(props.getMinIdle());
		dataSource.setMaxActive(props.getMaxActive());
		dataSource.setMaxWait(props.getMaxWait());
		dataSource.setTimeBetweenEvictionRunsMillis(props.getTimeBetweenEvictionRunsMillis());
		return dataSource;
	}
}
