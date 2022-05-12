package com.test.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Ex05_PreparedStatement {
	
	public static void main(String[] args) {
		
		//m1();
		m2();
		
		
		
	}

	private static void m2() {
		
		Connection conn = null;
		Statement stat = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBUtil.open();
			
			//정적 쿼리 > Statement로 하기!!!!
			String sql = "select name from tblInsa where num = 1001";
			
			stat = conn.createStatement(); //객체 생성
			
			rs = stat.executeQuery(sql);
			
			if(rs.next()) {
				System.out.println(rs.getString("name"));
				
			}
			
			rs.close();
			stat.close();
			
			//동적 쿼리
			sql = "select name from tblInsa where num = ?";
			
			pstat = conn.prepareStatement(sql);
			
			//java.sql.SQLException: 인덱스에서 누락된 IN 또는 OUT 매개변수:: 1
			pstat.setString(1, "1001"); //그냥 자료형 신경 안쓰고 setString이랑 setInt 둘 다 써도 됨 알아서 동작해줌.
			//pstat.setInt(1, 1001);
			
			
			rs = pstat.executeQuery();
			
			if(rs.next()) {
				System.out.println(rs.getString("name"));
			}
			rs.close();
			pstat.close();
			
			
			//인자 없는 쿼리 > pstat로 실행 > 그래도 잘 돌아간다. 
			sql = "select count(*) as cnt from tblInsa";
			
			pstat = conn.prepareStatement(sql);
			
			rs = pstat.executeQuery();
			
			if(rs.next()) {
				System.out.println(rs.getString("cnt"));
			}
			
			rs.close();
			pstat.close();
			
			conn.close();
			
			
		} catch (Exception e) {
			System.out.println("Ex05_PreparedStatement.m2");
			e.printStackTrace();
		}
		
	}

	private static void m1() {
		
		//Statement vs PreparedStatement
		//- SQL 실행
		//- 매개변수 처리 지원 유무
		//- Statement > 정적 SQL
		//- PreparedStatement > 동적 쿼리
		
		//insert > 사용자 입력
		String name = "하하하";
		String age = "20";
		String gender = "m";
		String tel = "010-1111-2222";
		String address = "서울시 동대문구 이문's동"; //Statement에서는 문자열 안에 '들어가면 안된다. 하지만 PreparedStatement는 가능
		
		//address = address.replace("'", "''");
		
		Connection conn = null;
		Statement stat = null;
		PreparedStatement pstat = null;
		
		try {
			
			conn = DBUtil.open(); //DB 연결
			
			
			//*****Statement로!
			String sql = String.format("insert into tblAddress (seq, name, age, gender, tel, address, regdate) values (seqAddress.nextVal, '%s', %s, '%s', '%s', '%s', default)", name, age, gender, tel, address);
			
			stat = conn.createStatement(); //A.
			
			//int result = stat.executeUpdate(sql); //A. 실행 시 SQL 대입
			
			//System.out.println(result);
			
			//---------------------------------------------
			//**********PreparedStatement로!
			//- ? : 오라클 매개변수
			//- SQL 작성이 용이하다. 어떤게 문자이고 숫자인지 확인안해도 됨 > String.format() 유사
			//- 매개변수값으로 부적절한 값이 있어도 자동으로 이스케이프를 시켜준다.(****)
			sql = "insert into tblAddress (seq, name, age, gender, tel, address, regdate) values (seqAddress.nextVal, ?, ?, ?, ?, ?, default)"; 
			
			pstat = conn.prepareStatement(sql); //B. 미리 SQL 대입
			
			//인자값에 대한 처리 추가 진행(***) > 안정성 높다. 
			pstat.setString(1, name); //1번째 물음표에 name을 넣어라.
			pstat.setString(2, age);
			pstat.setString(3, gender);
			pstat.setString(4, tel);
			pstat.setString(5, address);
			
			int result = pstat.executeUpdate(); //B. 
			
			System.out.println(result);
			
			
		} catch (Exception e) {
			System.out.println("Ex05_PreparedStatement.m1");
			e.printStackTrace();
		}
		
	}

}
