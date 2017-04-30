package com.gcit.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gcit.library.dao.BorrowerDAO;
import com.gcit.library.dao.BranchDAO;
import com.gcit.library.entity.Borrower;
import com.gcit.library.entity.Branch;

public class BorrowerService {
	public List<Branch> getAllBranches(Integer pageNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BranchDAO brdao = new BranchDAO(conn);
			return brdao.readAllBranches(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	
	public Borrower getBorrowerFromID(Integer cardNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BorrowerDAO brdao = new BorrowerDAO(conn);
			return brdao.readBorrowerByID(cardNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
}
