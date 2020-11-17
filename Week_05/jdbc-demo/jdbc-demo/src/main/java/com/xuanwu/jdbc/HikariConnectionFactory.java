package com.xuanwu.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class HikariConnectionFactory implements ConnectionFactory{
	
	HikariDataSource ds = new HikariDataSource();

	@Override
	public Connection getConnection(String url, String userName, String password) throws SQLException {
		ds.setJdbcUrl(url);
		ds.setUsername(userName);
		ds.setPassword(password);
		return ds.getConnection();
	}

}
