package com.xuanwu.mysql_demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Strings;

public class InsertDataTest {
	
	String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF8&verifyServerCertificate=false&useSSL=false&serverTimezone=Asia/Shanghai";
	String user = "root";
	String password = "";
	
	ExecutorService exec;
	
	@Before
	public void prepare() {
		exec = Executors.newFixedThreadPool(10);
	}
	
	CountDownLatch cdl;
	
	AtomicInteger atmInt = new AtomicInteger(0);
	
	Runnable add10kOrder = () -> {
		try {			
			Connection conn = DriverManager.getConnection(url, user, password);
			PreparedStatement ps = conn.prepareStatement("insert into t_order (code, state, user_id) values (?,?,?)");
			Random r = new Random();
			for(int j=0; j<100; j++) {				
				for(int i=0; i<100; i++) {
					ps.setString(1, "KF" + Strings.padStart(String.valueOf(atmInt.incrementAndGet()), 10, '0'));
					ps.setString(2, String.valueOf(r.nextInt(5)));
					ps.setString(3, String.valueOf(r.nextInt(Integer.MAX_VALUE)));
					ps.addBatch();
				}
				int[] eff = ps.executeBatch();
			}
			conn.close();
			cdl.countDown();
		}catch(Exception e) {
			
		}
	};
	
	
	@Test
	public void test() throws SQLException, InterruptedException {
		cdl = new CountDownLatch(100);
		for(int i=0; i<100; i++) {			
			exec.execute(add10kOrder);
		}
		cdl.await();
	}
	
	
}
