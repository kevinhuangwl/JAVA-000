package com.xuanwu.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

	Connection getConnection(String url, String userName, String password) throws SQLException;
}
