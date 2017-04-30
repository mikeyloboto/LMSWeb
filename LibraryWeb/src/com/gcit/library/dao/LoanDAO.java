package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Loan;

public class LoanDAO extends BaseDAO {

	public LoanDAO(Connection conn) {
		super(conn);
	}

	public void addLoanBase(Loan loan) throws ClassNotFoundException, SQLException {
		LocalDate due = LocalDate.now().plusDays(7);
		Date dueDate = Date.valueOf(due);
		save("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dateIn, dueDate) values (?, ?, ?, CURTIME(), NULL, ?)",
				new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchNo(),
						loan.getBorrower().getCardNo(), dueDate });
	}

	public void updateLoan(Loan loan) throws ClassNotFoundException, SQLException {
		save("update tbl_book_loans set dueDate = ?, dateIn = ? where bookId = ? and branchId = ? and cardNo = ? and dateOut = ?",
				new Object[] { Date.valueOf(loan.getDateDue()), Date.valueOf(loan.getDateIn()),
						loan.getBook().getBookId(), loan.getBranch().getBranchNo(), loan.getBorrower().getCardNo(),
						Timestamp.valueOf(loan.getDateOut()) });
	}

	public void deleteLoan(Loan loan) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_loans where bookId = ? and branchNo = ? and cardNo = ? and dateOut = ?",
				new Object[] { loan.getBook().getBookId(), loan.getBranch().getBranchNo(),
						loan.getBorrower().getCardNo(), Timestamp.valueOf(loan.getDateOut()) });
	}

	public List<Loan> readAllLoans(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_book_loans", null);
	}

	public List<Loan> readLoansByCardNo(Integer cardNo, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_book_loans where cardNo = ?", new Object[] { cardNo });
	}

	@Override
	public List extractData(ResultSet rs) throws SQLException, ClassNotFoundException {

		List<Loan> loans = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			BorrowerDAO bordao = new BorrowerDAO(conn);
			BranchDAO brdao = new BranchDAO(conn);
			Loan a = new Loan();
			a.setBook(bdao.readBookFromId(rs.getInt("bookId")));
			a.setBorrower(bordao.readBorrowerByID(rs.getInt("branchId")));
			a.setBranch(brdao.readBranchByID(rs.getInt("cardNo")));
			a.setDateOut(rs.getTimestamp("dateOut").toLocalDateTime());
			a.setDateDue(rs.getDate("dueDate").toLocalDate());
			a.setDateIn(rs.getDate("dateIn").toLocalDate());
			loans.add(a);
		}
		return loans;
	}

	@Override
	public List extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Loan> loans = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			BorrowerDAO bordao = new BorrowerDAO(conn);
			BranchDAO brdao = new BranchDAO(conn);
			Loan a = new Loan();
			a.setBook(bdao.readBookFromId(rs.getInt("bookId")));
			a.setBorrower(bordao.readBorrowerByID(rs.getInt("branchId")));
			a.setBranch(brdao.readBranchByID(rs.getInt("cardNo")));
			a.setDateOut(rs.getTimestamp("dateOut").toLocalDateTime());
			a.setDateDue(rs.getDate("dueDate").toLocalDate());
			a.setDateIn(rs.getDate("dateIn").toLocalDate());
			loans.add(a);
		}
		return loans;
	}

	public Integer getLoanCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_book_loans where dateIn IS NULL", null);
	}

}
