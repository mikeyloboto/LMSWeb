package com.gcit.library.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.gcit.library.dao.BranchDAO;
import com.gcit.library.entity.Branch;

public class LibrarianService {
	public void modBranch(Branch g) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BranchDAO brdao = new BranchDAO(conn);
			brdao.updateBranch(g);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
