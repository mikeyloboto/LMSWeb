package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;


public class BookDAO extends BaseDAO{
	
	public BookDAO(Connection conn) {
		super(conn);
	}

	public void addBook(Book book) throws ClassNotFoundException, SQLException{
		save("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()});
	}
	
	public Integer addBookWithID(Book book) throws ClassNotFoundException, SQLException{
		return saveWithID("insert into tbl_book (title) values (?)", new Object[] {book.getTitle()});
	}
	
	public void addBookAuthors(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException{
		save("insert into tbl_book_authors values (?, ?)", new Object[] {bookId, authorId});
	}
	
	public void updateBook(Book book) throws ClassNotFoundException, SQLException{
		save("update tbl_book set title = ? where bookId = ?", new Object[]{book.getTitle(), book.getBookId()});
	}
	
	public void deleteBook(Book book) throws ClassNotFoundException, SQLException{
		save("delete * from tbl_book where bookId = ?", new Object[] {book.getBookId()});
	}
	
	public Integer getBookCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		return readInt("select noOfCopies from tbl_book_copies where bookId = ? and branchId = ?", new Object[]{book.getBookId(), branch.getBranchNo()});
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		AuthorDAO adao = new AuthorDAO(conn);
		while(rs.next()){
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			b.setAuthors(adao.readFirstLevel("select * from tbl_author where authorId IN (Select authorId from tbl_book_authors where bookId = ?)", new Object[]{b.getBookId()}));
			books.add(b);
		}
		return books;
	}
	
	@Override
	public List<Book> extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		while(rs.next()){
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			books.add(b);
		}
		return books;
	}

	public Map<Book, Integer> readCopiesFirstLevel(Branch branch) throws ClassNotFoundException, SQLException {
		Map<Book, Integer> map = new HashMap<>();
		List<Book> books = read("select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ?)", new Object[]{branch.getBranchNo()});
		for (Book b : books) {
			Integer c = getBookCopies(b, branch);
			map.put(b, c);
		}
		return map;
	}
}
