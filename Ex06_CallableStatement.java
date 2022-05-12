package com.test.java;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

public class Ex06_CallableStatement {
	
	public static void main(String[] args) {
		
		//JDBC는 기본적으로 모든 SQL의 실행은 자동 커밋이 수행된다. > executeXXX() 호출때마다
		
		//m1();
		//m2();
		//m3();
		//m4();
		//m5();
		m6();
		
	}
	private static void m6() {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		
		try {
			
			
			conn = DBUtil.open();
			
			//부서명 입력 > 부서 직원의 보너스 지급 내역 출력
			Scanner scan = new Scanner(System.in);
			
			System.out.print("부서명: ");
			String buseo = scan.nextLine();
			
			String sql = "{ call procM6(?,?) }";
			
			stat = conn.prepareCall(sql);
			
			stat.setString(1, buseo);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			stat.executeQuery();
			
			rs = (ResultSet) stat.getObject(2);
			
			System.out.println("== 보너스 지급 내역 ==");
			System.out.println("[직원명]\t[지급액]");
			
			while(rs.next()) {
				System.out.printf("%s\t%,10d원\n"
									, rs.getString("name")
									, rs.getInt("bonus"));
			}
			
			rs.close();
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private static void m5() {
		
//		create or replace procedure procM5(
//			    pbuseo in varchar2,
//			    pcursor out sys_refcursor -- 커서 생성
//			)
//			is
//			begin
//
//			    open pcursor
//			    for 
//			        select name, jikwi, basicpay 
//			            from tblInsa 
//			                where buseo = pbuseo;
//
//			end procM5;

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBUtil.open();
			
			String sql = "{ call procM5(?,?) ";
			
			stat = conn.prepareCall(sql);
			
			stat.setString(1, "개발부");
			stat.registerOutParameter(2, OracleTypes.CURSOR); //커서 받은 것임
			
			stat.executeQuery(); //쿼리 실행
			
			//오라클 커서 == 자바 ResultSet >********
			rs = (ResultSet)stat.getObject(2); // 커서 받아오는 방법!
			
			while(rs.next()) {
				System.out.println(rs.getString("name"));
				System.out.println(rs.getString("jikwi"));
				System.out.println(rs.getString("basicpay"));
				System.out.println();
				
			}
			
			rs.close();
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.m5");
			e.printStackTrace();
		}
		
	}
	private static void m4() {
		
		// 문제. 직원 번호를 입력하면 이름, 부서, 직위, 지역을 반환 + 출력
		// Oracle Developer에서 프로시저 생성 : procM4 
		// - in 파라미터 > 직원 번호
		// - out 파라미터> 이름
		// - out 파라미터> 부서
		// - out 파라미터> 직위
		// - out 파라미터> 지역
		
		
//		create or replace procedure procM4 (
//			    pnum tblInsa.num%type,
//			    pname out varchar2,
//			    pbuseo out varchar2,
//			    pjikwi out varchar2,
//			    pcity out varchar2
//			)
//			is
//			begin
//			    select name, buseo, jikwi, city into pname, pbuseo, pjikwi, pcity 
//			        from tblInsa where num = pnum;
//			    
//			end;  
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		Scanner scan = new Scanner(System.in);
		
		try {
			
			conn = DBUtil.open();
			
			System.out.print("직원 번호 : ");
			String num = scan.nextLine();
			String sql = "{ call procM4(?,?,?,?,?) }";
			
			stat = conn.prepareCall(sql);
			
			stat.setString(1, num);
			
			stat.registerOutParameter(2, OracleTypes.VARCHAR);
			stat.registerOutParameter(3, OracleTypes.VARCHAR);
			stat.registerOutParameter(4, OracleTypes.VARCHAR);
			stat.registerOutParameter(5, OracleTypes.VARCHAR);
			
			stat.executeQuery();
			
			System.out.println("이름: " + stat.getString(2));
			System.out.println("부서: " + stat.getString(3));
			System.out.println("직위: " + stat.getString(4));
			System.out.println("지역: " + stat.getString(5));
			
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.m4");
			e.printStackTrace();
		}
		
		
		
		
	}
	//인자값(X), 반환값(O)
	private static void m3() {
		
		// developer에서 생성해놓은 프로시저
//		create or replace procedure procM3 (
//			    pcount out number
//			)
//			is
//			begin
//
//			    select count(*) into pcount from tblAddress;
//			    
//			end procM3;
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBUtil.open();
			
			String sql = "{ call procM3(?) }";
			
			stat = conn.prepareCall(sql);
			
			//in 매개변수
			//stat.setString(1, 값)
			
			//out 매개변수
			stat.registerOutParameter(1, OracleTypes.NUMBER); //out 매개변수는 count(*) 즉, pcount인데 number니까 number로. 
			
			
			//java.sql.SQLException: PLSQL 문에서 인출을 수행할 수 없습니다.: next
			//rs = stat.executeQuery();
			//System.out.println(rs.next());
			
			
			stat.executeQuery(); //rs(X)
			
			int count = stat.getInt(1); //Out Parameter 가져오는 역할
			
			System.out.println(count);
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.m3");
			e.printStackTrace();
		}
		
	}

	//인자값 있는 프로시저 코드 처리
	private static void m2() {
		
//		-- m2. 인자값(O), 반환값(X)
//		create or replace procedure procM2 (
//		    pname       tblAddress.name%type,
//		    page        tblAddress.age%type,
//		    pgender     tblAddress.gender%type,
//		    ptel        tblAddress.tel%type,
//		    paddress    tblAddress.address%type
//		)
//
//		is
//		begin
//
//		    insert into tblAddress(seq, name, age, gender, tel, address, regdate)
//		    values (seqAddress.nextVal, pname, page, pgender, ptel, paddress, default);
//		end;
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBUtil.open();
			
			//String sql = "{ call procM2('상수', 22, 'm', '0101-2222-2222', '주소') }";
			
			String sql = "{ call procM2(?, ?, ?, ?, ?) }";
			
			stat = conn.prepareCall(sql);
			
			//인덱스에서 누락된 IN 또는 OUT 매개변수:: 1
			//인덱스에서 누락된 IN 또는 OUT 매개변수:: 2
			
			stat.setString(1, "홍길동");
			stat.setString(2, "25");
			stat.setString(3, "m");
			stat.setString(4, "010-1234-5421");
			stat.setString(5, "서울시");
			
			
			int result = stat.executeUpdate();
			
			System.out.println(result);
			
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.m2");
			e.printStackTrace();
		}
		
		
	}

	private static void m1() {
		
		//Statement > 정적 쿼리
		//PreparedStatement > 동적 쿼리
		//CallableStatement > 프로시저 호출
		
		
//		-- m1. 인자값, 반환값
//		create or replace procedure procM1
//		is
//		begin
//		    insert into tblAddress (seq, name, age, gender, tel, address, regdate)
//		    values (seqAddress.nextVal, '홍길동', 20, 'm', '010-1234-5678', '서울시 강남구 역삼동 한독빌딩', default);
//		    
//		end;

		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		
		try {
			
			conn = DBUtil.open();
			
			String sql = "{ call procM1 }";
			
			stat = conn.prepareCall(sql); //매개변수 처리 능력 보유 > ? 지원
			
			int result = stat.executeUpdate();
			
			System.out.println(result);
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.m1");
			e.printStackTrace();
		}
		
	}
	
	
	private static void temp() {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		
		
		try {
			conn = DBUtil.open();
		} catch (Exception e) {
			System.out.println("Ex06_CallableStatement.temp");
			e.printStackTrace();
		}
	}

}
