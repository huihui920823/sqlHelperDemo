package com.huihui.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyTest1 {
	
	public static final String DBDRIVER = "com.mysql.jdbc.Driver";
	public static final String DBURL = "jdbc:mysql://localhost:3306/xp";
	public static final String DBUSER = "root";
	public static final String DBPASS = "123";
	
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from sal where id =1 and name='sdfsdfsdf' or 1=1";
		
		try {
			Class.forName(DBDRIVER);
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt("sale"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			rs = null;
			try {
				pstmt.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			pstmt = null;
			try {
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			conn = null;
		}
		
		
		
		
		
	}
	
	
		
	

}
