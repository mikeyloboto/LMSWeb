package com.gcit.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.gcit.library.dao.BookDAO;
import com.gcit.library.dao.BranchDAO;
import com.gcit.library.entity.Book;
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

	public Branch getBranchFromID(Integer id) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BranchDAO brdao = new BranchDAO(conn);
			return brdao.readBranchByID(id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Integer getBookCount(Branch branch) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			return bdao.readBookCopiesCountInBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Map<Book, Integer> getAllBooksInBranch(Branch branch, Integer pageNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			return bdao.readCopiesFirstLevel(branch, pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	public List<Book> getAllBooks(Integer pageNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			return bdao.readAllBooks(pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	public Book getBookFromID(Integer id) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			return bdao.readBookFromId(id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	public Integer getBookCountInBranch(Book book, Branch branch) {
		Integer toRet = 0;
		try {
			toRet = getAllBooksInBranch(branch, null).get(book);
			if (toRet == null)
				toRet = 0;
			return toRet;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateCopies(Branch br, Book book, Integer copies) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			bdao.modCopies(br, book, copies);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void incrementCopies(Branch br, Book book, Integer increment) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			bdao.incrementCopies(br, book, increment);
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
