package com.xuanwu.jdbc;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBTest {
	
	ConnectionFactory cf1 = new RawConnectionFactory();
	ConnectionFactory cf2 = new HikariConnectionFactory();
	String url = "jdbc:mysql://192.168.1.58:3306/AITS2?useUnicode=true&characterEncoding=UTF8&verifyServerCertificate=false&useSSL=false&serverTimezone=Asia/Shanghai";
	String userName = "develop2";
	String password = "develop2";
	String querySql = "select * from kf_t_issue";
	
	@Before
	public void warmup() throws SQLException {
		cf2.getConnection(url, userName, password).close();
	}
	
	public String getCreateSql(boolean confirm) {
		return confirm 
				? String.format("insert into t_test (id, value) values ('%s', %d)", UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt())
				: "insert into t_test (id, value) values (?, ?)";
	}
	
	@Test
	public void testCreateConnectionAndStatementQuery() throws SQLException {
		Connection conn = cf1.getConnection(url, userName, password);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(querySql);
		System.out.println("查询结果：" + rs.getFetchSize());
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndStatementQuery() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(querySql);
		System.out.println("查询结果：" + rs.getFetchSize());
		conn.close();
	}
	
	@Test
	public void testCreateConnectionAndStatementUpdate() throws SQLException {
		Connection conn = cf1.getConnection(url, userName, password);
		Statement st = conn.createStatement();
		int result = st.executeUpdate(getCreateSql(true));
		System.out.println("操作结果：" + result);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndStatementUpdate() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		Statement st = conn.createStatement();
		int result = st.executeUpdate(getCreateSql(true));
		System.out.println("操作结果：" + result);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementQuery() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		PreparedStatement ps = conn.prepareStatement(querySql);
		ResultSet rs = ps.executeQuery();
		System.out.println("查询结果：" + rs.getFetchSize());
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementUpdate() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		PreparedStatement ps = conn.prepareStatement(getCreateSql(true));
		int rs = ps.executeUpdate();
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementUpdateMany() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		PreparedStatement ps = conn.prepareStatement(getCreateSql(false));
		int rs = 0;
		for(int i=0; i<1000; i++) {	
			ps.setString(1, UUID.randomUUID().toString());
			ps.setInt(2, ThreadLocalRandom.current().nextInt());
			rs += ps.executeUpdate();
		}
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementBatchUpdate() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		PreparedStatement ps = conn.prepareStatement(getCreateSql(false));
		for(int i=0; i<1000; i++) {			
			ps.setString(1, UUID.randomUUID().toString());
			ps.setInt(2, ThreadLocalRandom.current().nextInt());
			ps.addBatch();
		}
		int rs = ps.executeUpdate();
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementUpdateManyWithManualCommit() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		conn.setAutoCommit(false);
		PreparedStatement ps = conn.prepareStatement(getCreateSql(false));
		int rs = 0;
		for(int i=0; i<1000; i++) {			
			ps.setString(1, UUID.randomUUID().toString());
			ps.setInt(2, ThreadLocalRandom.current().nextInt());
			rs += ps.executeUpdate();
		}
		conn.commit();
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementUpdateManyWithSavePointCommit() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		conn.setAutoCommit(false);
		Savepoint sp = null;
		PreparedStatement ps = conn.prepareStatement(getCreateSql(false));
		int rs = 0;
		int randomEx = ThreadLocalRandom.current().nextInt(1000);
		try {			
			for(int i=0; i<1000; i++) {			
				ps.setString(1, UUID.randomUUID().toString());
				ps.setInt(2, ThreadLocalRandom.current().nextInt());
				rs += ps.executeUpdate();
				if(i % 100 == 0) {
					sp = conn.setSavepoint();
				}
				if(i > randomEx) {
					throw new Exception();
				}
			}
			conn.commit();
		}catch(Exception e) {
			conn.rollback(sp);
			conn.commit();
		}
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
	@Test
	public void testGetConnectionAndPrepareStatementUpdateManyWithRollBack() throws SQLException {
		Connection conn = cf2.getConnection(url, userName, password);
		conn.setAutoCommit(false);
		PreparedStatement ps = conn.prepareStatement(getCreateSql(false));
		int rs = 0;
		int randomEx = ThreadLocalRandom.current().nextInt(1000);
		try {			
			for(int i=0; i<1000; i++) {			
				ps.setString(1, UUID.randomUUID().toString());
				ps.setInt(2, ThreadLocalRandom.current().nextInt());
				rs += ps.executeUpdate();
				if(i > randomEx) {
					throw new Exception();
				}
			}
			conn.commit();
		}catch(Exception e) {
			conn.rollback();
		}
		System.out.println("操作结果：" + rs);
		conn.close();
	}
	
}
