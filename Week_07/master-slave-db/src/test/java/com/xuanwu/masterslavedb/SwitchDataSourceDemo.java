package com.xuanwu.masterslavedb;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwitchDataSourceDemo {

	@Resource(name="masterTemplate")
	JdbcTemplate masterTemplate;
	
	@Resource(name="slaveTemplate")
	JdbcTemplate slaveTemplate;
	
	
}
