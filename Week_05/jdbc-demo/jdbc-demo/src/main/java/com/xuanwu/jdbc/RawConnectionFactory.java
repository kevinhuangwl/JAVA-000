package com.xuanwu.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RawConnectionFactory implements ConnectionFactory{

	@Override
	public Connection getConnection(String url, String userName, String password) throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

}
