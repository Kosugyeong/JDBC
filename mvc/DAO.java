package com.test.java.mvc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import mylibrary.db.DBUtil;

//JDBC 코드 담당 > DB 업무 담당
public class DAO {
	private Connection conn = null;
	private Statement stat = null;
	private PreparedStatement pstat = null;
	private ResultSet rs = null;
	
	
	public DAO() { //생성자
		
		conn = DBUtil.open();
		
		
	}

	public int add(Address dto) {
		
		
		
		try {
			
			
			String sql = "insert into tblAddress (seq, name, age, gender, tel, address, regdate) values (seqAddress.nextVal, ?, ?, ?, ?, ?, default)";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, dto.getName());
			pstat.setString(2, dto.getAge());
			pstat.setString(3, dto.getGender());
			pstat.setString(4, dto.getTel());
			pstat.setString(5, dto.getAddress());
			
			int result = pstat.executeUpdate();
			
			return result; //1:성공 0:실패
			
			
		} catch (Exception e) {
			System.out.println("DAO.add");
			e.printStackTrace();
		}
		
		return 0;
	}

	public ArrayList<Address> list() {
		
		try {
			String sql = "select * from tblAddress order by seq asc";
			
			stat = conn.createStatement();
			
			rs = stat.executeQuery(sql);
			
			
			//ResultSet에 있는 것을 복사해서 ArrayList<Address>에 옮겨 담기
			ArrayList<Address> list = new ArrayList<Address>();
			
			while(rs.next()) {
				
				//한바퀴씩 돌 때마다 레코드 1개
				Address dto = new Address();
				
				dto.setSeq(rs.getString("seq"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getString("age"));
				dto.setGender(rs.getString("gender"));
				dto.setTel(rs.getString("tel"));
				dto.setAddress(rs.getString("address"));
				dto.setRegdate(rs.getString("regdate"));
				
				list.add(dto);
				
				
			}
			
			return list; //select의 결과를 Service.java에게 반환
			
			
		} catch (Exception e) {
			System.out.println("DAO.list");
			e.printStackTrace();
		}
		
		return null;
	}

	public Address get(String seq) {
		
		try {
			String sql = "select * from tblAddress where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, seq);
			
			rs = pstat.executeQuery();
			
			if(rs.next()) {
				
				//레코드 1줄 > Address 1개
				Address dto = new Address();
				
				dto.setSeq(rs.getString("seq"));
				dto.setName(rs.getString("name"));
				dto.setAge(rs.getString("age"));
				dto.setGender(rs.getString("gender"));
				dto.setTel(rs.getString("tel"));
				dto.setAddress(rs.getString("address"));
				dto.setRegdate(rs.getString("regdate"));
				
				return dto; //반환
				
				
			}
			
		} catch (Exception e) {
			System.out.println("DAO.get");
			e.printStackTrace();
		}
		
		return null;
	}

	public int edit(Address dto) {
		
		try {
			
			//모든 컬럼을 업데이트한다. > SQL를 1개만 만들기 위해서!!!!
			String sql = "update tblAddress set name = ?, age = ?, gender = ?, tel = ?, address = ? where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, dto.getName());
			pstat.setString(2, dto.getAge());
			pstat.setString(3, dto.getGender());
			pstat.setString(4, dto.getTel());
			pstat.setString(5, dto.getAddress());
			
			int result = pstat.executeUpdate();
			
			return result; //1:성공 0:실패
			
			
		} catch (Exception e) {
			System.out.println("DAO.add");
			e.printStackTrace();
		}
		
		return 0;
		
		
	}

	public int del(String seq) {
		
		try {
			
			String sql = "delete from tblAddress where seq = ?";
			
			pstat = conn.prepareStatement(sql);
			
			pstat.setString(1, seq);
			
			int result = pstat.executeUpdate();
			
			return result; //1.성공, 0. 실패
			
			
		} catch (Exception e) {
			System.out.println("DAO.del");
			e.printStackTrace();
		}
		
		return 0;
		
	}

}
