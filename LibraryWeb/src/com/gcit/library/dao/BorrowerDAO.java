package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Borrower;

public class BorrowerDAO extends BaseDAO {

	public BorrowerDAO(Connection conn) {
		super(conn);
	}

	public void addBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		save("insert into tbl_borrower (name, address, phone) values (?, ?, ?)",
				new Object[] { borrower.getName(), borrower.getAddress(),
						borrower.getPhone() });
	}

	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		save("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?",
				new Object[] { borrower.getName(), borrower.getAddress(),
						borrower.getPhone(), borrower.getCardNo() });
	}

	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		save("delete from tbl_borrower where cardNo = ?", new Object[] { borrower.getCardNo() });
	}

	public void deleteBorrower(Integer borrowerId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_borrower where cardNo = ?", new Object[] { borrowerId });
	}

	public List<Borrower> readAllBorrowers(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_borrower", null);
	}

	public Borrower readBorrowerByID(Integer borrowerID) throws ClassNotFoundException, SQLException {
		List<Borrower> borrowers = read("select * from tbl_borrower where cardNo = ?",
				new Object[] { borrowerID });
		if (borrowers != null && !borrowers.isEmpty()) {
			return borrowers.get(0);
		}
		return null;
	}

	public List<Borrower> readBorrowersByName(String borrowerName, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_borrower where name like ?", new Object[] { borrowerName });
	}

	public Integer readBorrowersCountByName(String string) throws ClassNotFoundException, SQLException {
		return readInt("select count(*) from tbl_borrower where name like ?", new Object[] { string });
	}

	
	@Override
	public List extractData(ResultSet rs) throws SQLException, ClassNotFoundException {

		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			Borrower a = new Borrower();
			a.setCardNo(rs.getInt("cardNo"));
			a.setName(rs.getString("name"));
			a.setAddress(rs.getString("address"));
			a.setPhone(rs.getString("phone"));
			// TODO Fix that shite!!!!!!
			a.setLoans(
					bdao.readFirstLevel("select * from tbl_book where pubId = ?", new Object[] { a.getCardNo() }));
			borrowers.add(a);
		}
		return borrowers;
	}

	@Override
	public List extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Borrower> borrowers = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			Borrower a = new Borrower();
			a.setCardNo(rs.getInt("cardNo"));
			a.setName(rs.getString("name"));
			a.setAddress(rs.getString("address"));
			a.setPhone(rs.getString("phone"));
			borrowers.add(a);
		}
		return borrowers;
	}

	public Integer getBorrowerCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_borrower", null);
	}


}
