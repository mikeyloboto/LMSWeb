package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Genre;
import com.gcit.library.entity.Publisher;

public class GenreDAO extends BaseDAO {
	public GenreDAO(Connection conn) {
		super(conn);
	}

	public void addGenre(Genre genre) throws ClassNotFoundException, SQLException {
		save("insert into tbl_genre (genre_name) values (?)", new Object[] { genre.getGenreName() });
	}

	public Integer addGenreWithID(Genre genre) throws ClassNotFoundException, SQLException {
		return saveWithID("insert into tbl_genre (genre_name) values (?)", new Object[] { genre.getGenreName() });
	}

	// public void addGenreAuthors(Integer genreId, Integer authorId) throws
	// ClassNotFoundException, SQLException{
	// save("insert into tbl_genre_authors values (?, ?)", new Object[]
	// {GenreId, authorId});
	// }

	public void addBookGenre(Genre g, Integer bookId) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_genres values (?, ?)", new Object[] { g.getGenreId(), bookId });
	}

	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException {
		save("update tbl_genre set genre_name = ? where genre_id = ?",
				new Object[] { genre.getGenreName(), genre.getGenreId() });
	}

	public void deleteGenre(Genre genre) throws ClassNotFoundException, SQLException {
		save("delete from tbl_genre where genre_id = ?", new Object[] { genre.getGenreId() });
	}

	public void deleteGenre(Integer genreId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_genre where genre_id = ?", new Object[] { genreId });
	}

	public Genre readGenreByID(Integer id) throws ClassNotFoundException, SQLException {
		List<Genre> genres = read("select * from tbl_genre where genre_id = ?", new Object[] { id });
		if (genres != null && !genres.isEmpty()) {
			return genres.get(0);
		}
		return null;
	}

	public List<Genre> readAllGenres(Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_genre", null);

	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Genre> genres = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		while (rs.next()) {
			Genre g = new Genre();
			g.setGenreName(rs.getString("genre_name"));
			g.setGenreId(rs.getInt("genre_id"));
			g.setBooks(bdao.readFirstLevel(
					"select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genre_id = ?)",
					new Object[] { g.getGenreId() }));
			genres.add(g);
		}
		return genres;
	}

	@Override
	public List<Genre> extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Genre> genres = new ArrayList<>();
		while (rs.next()) {
			Genre b = new Genre();
			b.setGenreName(rs.getString("genre_name"));
			b.setGenreId(rs.getInt("genre_id"));
			genres.add(b);
		}
		return genres;
	}

	public void removeBookGenres(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_genres where bookId = ?", new Object[] { bookId });
	}

	public Integer readGenreCount() throws ClassNotFoundException, SQLException {
		return readInt("select count(*) as COUNT from tbl_genre", null);
	}

	public List<Genre> readGenresByName(String string, Integer pageNo) throws ClassNotFoundException, SQLException {
		setPageNo(pageNo);
		return read("select * from tbl_genre where genre_name like ?", new Object[]{string});
	}

	public Integer readGenresCountByName(String string) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return readInt("select count(*) from tbl_genre where genre_name like ?", new Object[]{string});
	}

}
