package com.gcit.library.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.gcit.library.dao.BookDAO;
import com.gcit.library.dao.BorrowerDAO;
import com.gcit.library.dao.BranchDAO;
import com.gcit.library.dao.LoanDAO;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Borrower;
import com.gcit.library.entity.Branch;
import com.gcit.library.entity.Loan;

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
	

	public Map<Book, Integer> getAllBooksInBranch(Branch branch, Integer pageNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			return bdao.readCopiesFirstLevelNotZero(branch, pageNo);
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
	
	public Integer getLoanCount(Borrower borrower) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			LoanDAO ldao = new LoanDAO(conn);
			return ldao.getLoanCountByID(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}
	public List<Loan> getAllLoans(Borrower borrower, Integer pageNo) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			LoanDAO ldao = new LoanDAO(conn);
			return ldao.readLoansByCardNo(borrower.getCardNo(), pageNo);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return null;
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
	public void closeLoan(Integer newBranch, Loan g) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			LoanDAO ldao = new LoanDAO(conn);
			BookDAO bdao = new BookDAO(conn);
			Branch branch = new Branch();
			branch.setBranchNo(newBranch);
			ldao.closeLoan(g);
			bdao.incrementCopies(branch, g.getBook(), 1);
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

	public void startLoan(Loan loan) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			LoanDAO ldao = new LoanDAO(conn);
			BookDAO bdao = new BookDAO(conn);
			ldao.addLoanBase(loan);
			bdao.incrementCopies(loan.getBranch(), loan.getBook(), -1);
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
