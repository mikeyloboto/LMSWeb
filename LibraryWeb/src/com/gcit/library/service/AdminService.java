package com.gcit.library.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.gcit.library.dao.AuthorDAO;
import com.gcit.library.dao.BookDAO;
import com.gcit.library.dao.GenreDAO;
import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Genre;

public class AdminService {

	public void addAuthor(Author author) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			AuthorDAO adao = new AuthorDAO(conn);
			adao.addAuthor(author);
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

	public void addBook(Book book) throws SQLException {
		Connection conn = null;
		try {
			conn = ConnectionUtil.getConnection();
			BookDAO bdao = new BookDAO(conn);
			Integer bookId = bdao.addBookWithID(book);
			if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
				for (Author a : book.getAuthors()) {
					bdao.addBookAuthors(book.getBookId(), a.getAuthorId());
				}
			}
			// repeat for Genres
			GenreDAO gdao = new GenreDAO(conn);
			if (book.getGenres() != null && !book.getGenres().isEmpty()) {
				for (Genre g : book.getGenres()) {
					gdao.addBookGenre(g, bookId);
				}
			}
			// repeat for Publisher
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
