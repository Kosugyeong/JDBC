package com.test.java;

import java.sql.Connection;


public class Ex02 {
	
	public static void main(String[] args) {
		
		try {
			
			Connection conn = DBUtil.open();
			
			System.out.println(conn.isClosed() ? "연결 안됨" : "연결 됨");
			
			DBUtil.close();
			
			//오류
			//1. 서버 주소 틀렸을 때 
			// - IO 오류: The Network Adapter could not establish the connection
			
			//2. 아이디/암호 틀렸을 때
			// - ORA-01017: invalid username/password; logon denied
			
			//3. 서버 중지(오라클 서버 꺼져있을 때)
			// - Listener refused the connection with the following error:
			//4. 연결 문자열 오타
			//부적합한 Oracle URL이 지정되었습니다
			//5. 포트번호 틀렸을 때
			// - IO 오류: The Network Adapter could not establish the connection
			//6. SID 틀렸을 때
			// - Listener refused the connection with the following error:
			// - ORA-12505, TNS:listener does not currently know of SID given in connect descriptor
			//7. 드라이브가 틀렸을 때
			// - oracle.jdbc.driver.OracleDriver
			//8. ojdbc.jar를 안가져왔을 때
			// - oracle.jdbc.driver.OracleDriver
			conn = DBUtil.open("localhost", "hr", "java1234");
			
			System.out.println(conn.isClosed() ? "연결 안됨" : "연결 됨");
			
			DBUtil.close();
			
		} catch (Exception e) {
			System.out.println("Ex02.main");
			e.printStackTrace();
		}
		
		
	}

}
