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

	// ������Ҫ�ı���
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

	// ����������ֻ��Ҫһ��
	static {
		try {
			// ��dbinfo.properties�ļ��ж�ȡ������Ϣ
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

	// �õ�����
	public static Connection getConnection() {
		try {
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// ��дһ��update/delete/insert
	// sql��ʽ�� update ���� set �ֶ���=�� where �ֶ�=��
	public static void executeUpdate(String sql, String[] parameters) {
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			// ������ֵ
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setString(i + 1, parameters[i]);
				}
			}
			// ִ��
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			// �ر���Դ
			close(rs, pstmt, conn);
		}
	}

	// ����ж��update/delete/insert��䡾��Ҫ��������
	public static void executeUpdate2(String[] sqls, String[][] parameters) {
		try {
			conn = getConnection();
			// ��Ϊ��ʱ�û�����Ŀ����Ƕ��sql���
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
				// �ع�
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

	// ͳһ��select
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
			// �������ر�����Դ����û��ʹ��ResultSet�ˣ�Ҳ��û��return��
			// close(rs, pstmt, conn);
		}
		return rs;
	}

	// �ر���Դ
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
