package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;

public class BookDAO extends BaseDAO {

	public BookDAO(Connection conn) {
		super(conn);
	}

	public void addBook(Book book) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book (title, pubId) values (?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId() });
	}

	public Integer addBookWithID(Book book) throws ClassNotFoundException, SQLException {
		return saveWithID("insert into tbl_book (title, pubId) values (?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getPublisherId() });
	}

	public void addBookAuthors(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_authors values (?, ?)", new Object[] { bookId, authorId });
	}

	public void updateBook(Book book) throws ClassNotFoundException, SQLException {
		save("update tbl_book set title = ? where bookId = ?", new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book where bookId = ?", new Object[] { book.getBookId() });
	}

	public void deleteBook(Integer bookId) throws ClassNotFoundException, SQLException {
		System.out.println("deleting");
		save("delete from tbl_book where bookId = ?", new Object[] { bookId });
	}

	public Integer getBookCopies(Book book, Branch branch) throws ClassNotFoundException, SQLException {
		return readInt("select noOfCopies from tbl_book_copies where bookId = ? and branchId = ?",
				new Object[] { book.getBookId(), branch.getBranchNo() });
	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		AuthorDAO adao = new AuthorDAO(conn);
		PublisherDAO pdao = new PublisherDAO(conn);
		GenreDAO gdao = new GenreDAO(conn);
		while (rs.next()) {
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			b.setAuthors(adao.readFirstLevel(
					"select * from tbl_author where authorId IN (Select authorId from tbl_book_authors where bookId = ?)",
					new Object[] { b.getBookId() }));
			b.setPublisher(pdao.readPublisherByID(rs.getInt("pubId")));
			b.setGenres(gdao.readFirstLevel(
					"select * from tbl_genre where genre_id IN (select genre_id from tbl_book_genres where bookId = ?)",
					new Object[] { b.getBookId() }));
			books.add(b);
		}
		return books;
	}

	@Override
	public List<Book> extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setBookId(rs.getInt("bookId"));
			books.add(b);
		}
		return books;
	}

	public Map<Book, Integer> readCopiesFirstLevel(Branch branch, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		Map<Book, Integer> map = new HashMap<>();
		List<Book> books = read(
				"select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ?)",
				new Object[] { branch.getBranchNo() });
		for (Book b : books) {
			Integer c = getBookCopies(b, branch);
			map.put(b, c);
		}
		return map;
	}
	
	public Map<Book, Integer> readCopiesFirstLevelNotZero(Branch branch, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		Map<Book, Integer> map = new HashMap<>();
		List<Book> books = read(
				"select * from tbl_book where bookId in (select bookId from tbl_book_copies where branchId = ? and noOfCopies > 0)",
				new Object[] { branch.getBranchNo() });
		for (Book b : books) {
			Integer c = getBookCopies(b, branch);
			map.put(b, c);
		}
		return map;
	}
	
	public Boolean modCopies(Branch branch, Book book, Integer copies) throws ClassNotFoundException, SQLException {
		Integer bookExistence = readInt(
				"select count(*) from tbl_book_copies where branchId = ? and bookId = ?",
				new Object[] { branch.getBranchNo(), book.getBookId()});
		if (bookExistence == 0) {
			// add fresh
			save("insert into tbl_book_copies (branchId, bookId, noOfCopies) values (?, ?, ?)", new Object[] {branch.getBranchNo(), book.getBookId(), copies});
		}
		else
		{
			//update
			save("update tbl_book_copies set noOfCopies = ? where branchId = ? and bookId = ?", new Object[] {copies, branch.getBranchNo(), book.getBookId()});
		}
		return Boolean.TRUE;
	}
	
	public Boolean incrementCopies(Branch branch, Book book, Integer increment) throws ClassNotFoundException, SQLException {
		Integer bookExistence = readInt(
				"select count(*) from tbl_book_copies where branchId = ? and bookId = ?",
				new Object[] { branch.getBranchNo(), book.getBookId()});
		if (bookExistence == 0) {
			// add fresh
			save("insert into tbl_book_copies (branchId, bookId, noOfCopies) values (?, ?, ?)", new Object[] {branch.getBranchNo(), book.getBookId(), increment});
		}
		else
		{
			Integer copies = readInt(
					"select noOfCopies from tbl_book_copies where branchId = ? and bookId = ?",
					new Object[] { branch.getBranchNo(), book.getBookId()}) + increment;
			//update
			save("update tbl_book_copies set noOfCopies = ? where branchId = ? and bookId = ?", new Object[] {copies, branch.getBranchNo(), book.getBookId()});
		}
		return Boolean.TRUE;
	}

	public List<Book> readAllBooks(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_book", null);

	}

	public Book readBookFromId(Integer id) throws ClassNotFoundException, SQLException {
		List<Book> books = read("select * from tbl_book where bookId = ?", new Object[] { id });
		if (books != null && !books.isEmpty()) {
			return books.get(0);
		}
		return null;
	}

	public Integer readBookCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_book", null);
	}

	public void removeBookAuthors(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_authors where bookId = ?", new Object[] { bookId });
	}

	public void updateBookPublisher(Book book) throws ClassNotFoundException, SQLException {
		save("update tbl_book set pubId = ? where bookId = ?",
				new Object[] { book.getPublisher().getPublisherId(), book.getBookId() });
	}

	public Integer readBookCopiesCountInBranch(Branch branch) throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_book_copies where branchId = ? and noOfCopies > 0", new Object[]{branch.getBranchNo()});
	}


}
