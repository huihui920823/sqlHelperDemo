package com.huihui.test;

import java.sql.ResultSet;

import org.junit.Test;

import com.huihui.util.SqlHelper;

public class TestSqlHelper {
	// ����SqlHelper�������Ƿ��������ʹ��
	@Test
	public void testSqlHelper1() {
		String sql = "insert into sal (name,sale)values(?,?)";

		String[] parameters = { "xiu", "30" };

		SqlHelper.executeUpdate(sql, parameters);
	}

	@Test
	public void testSqlHelper2() {
		String sql1 = "update sal set sale=sale-10 where id=?";
		String sql2 = "update sal set sale=sale+10 where id=?";

		String[] sqls = { sql1, sql2 };
		String[] sql1_paras = { "1" };
		String[] sql2_paras = { "2" };
		String[][] parameters = { sql1_paras, sql2_paras };

		SqlHelper.executeUpdate2(sqls, parameters);
	}

	@Test
 	public void testSqlHelper3(){
		
		try {
			String sql = "select * from sal";
			ResultSet rs = SqlHelper.executeQuery(sql, null);
			while (rs.next()) {
				System.out.println(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//�ر���Դ
			SqlHelper.close(SqlHelper.getRs(), SqlHelper.getPstmt(), SqlHelper.getConn());
		}
		

	}
	
}
