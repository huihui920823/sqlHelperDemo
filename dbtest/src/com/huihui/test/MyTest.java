package com.huihui.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MyTest {
	
	public static final String DBDRIVER = "com.mysql.jdbc.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/xp";
	public static final String DBUSER = "root";
	public static final String DBPASS = "123";
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			stmt.executeUpdate("update sal set sale = sale-10 where name='ming'");
			int i = 9/0;
			stmt.executeUpdate("update sql set sale = sale+10 where name='hui'");
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} finally{
			
		}
		
		
		
		
		
	}
	
	
		
	

}
