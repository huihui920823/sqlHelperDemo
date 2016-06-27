package com.huihui.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqlHelper {

	private static String DBDRIVER = "";
	private static String DBURL = "";
	private static String DBUSER = "";
	private static String DBPASS = "";

	// 定义需要的变量
	private static Connection conn = null;
	private static PreparedStatement pstmt = null;
	private static ResultSet rs = null;

	public static Connection getConn() {
		return conn;
	}

	public static PreparedStatement getPstmt() {
		return pstmt;
	}

	public static ResultSet getRs() {
		return rs;
	}

	

	private static Properties pp = null;
	private static FileInputStream fis = null;

	// 加载驱动，只需要一次
	static {
		try {
			// 从dbinfo.properties文件中读取配置信息
			pp = new Properties();
			fis = new FileInputStream("dbinfo.properties");
			pp.load(fis);
			DBDRIVER = pp.getProperty("driver");
			DBURL = pp.getProperty("url");
			DBUSER = pp.getProperty("user");
			DBPASS = pp.getProperty("pass");

			Class.forName(DBDRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			fis = null;
		}
	}

	// 得到连接
	public static Connection getConnection() {
		try {
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 先写一个update/delete/insert
	// sql格式： update 表名 set 字段名=？ where 字段=？
	public static void executeUpdate(String sql, String[] parameters) {
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			// 给？赋值
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setString(i + 1, parameters[i]);
				}
			}
			// 执行
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			// 关闭资源
			close(rs, pstmt, conn);
		}
	}

	// 如果有多个update/delete/insert语句【需要考虑事务】
	public static void executeUpdate2(String[] sqls, String[][] parameters) {
		try {
			conn = getConnection();
			// 因为这时用户传入的可能是多个sql语句
			conn.setAutoCommit(false);
			for (int i = 0; i < sqls.length; i++) {
				if (parameters[i] != null) {
					pstmt = conn.prepareStatement(sqls[i]);
				}
				for (int j = 0; j < parameters[i].length; j++) {

					pstmt.setString(j + 1, parameters[i][j]);
				}
				pstmt.executeUpdate();
			}

			conn.commit();
		} catch (Exception e) {
			try {
				// 回滚
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			close(rs, pstmt, conn);
		}
	}

	// 统一的select
	public static ResultSet executeQuery(String sql, String[] parameters) {
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setString(i + 1, parameters[i]);
				}
			}
			rs = pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			// 如果这里关闭了资源，就没法使用ResultSet了，也就没法return了
			// close(rs, pstmt, conn);
		}
		return rs;
	}

	// 关闭资源
	public static void close(ResultSet rs, Statement pstmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			pstmt = null;
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}

}
