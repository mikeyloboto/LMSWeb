package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Genre;

public class GenreDAO extends BaseDAO
{
	public GenreDAO(Connection conn) {
		super(conn);
	}

	public void addGenre(Genre genre) throws ClassNotFoundException, SQLException{
		save("insert into tbl_genre (genre_name) values (?)", new Object[] {genre.getGenreName()});
	}
	
	public Integer addGenreWithID(Genre genre) throws ClassNotFoundException, SQLException{
		return saveWithID("insert into tbl_genre (genre_name) values (?)", new Object[] {genre.getGenreName()});
	}
	
//	public void addGenreAuthors(Integer genreId, Integer authorId) throws ClassNotFoundException, SQLException{
//		save("insert into tbl_genre_authors values (?, ?)", new Object[] {GenreId, authorId});
//	}

	public void addBookGenre(Genre g, Integer bookId) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_genres values (?, ?)", new Object[] {g.getGenreId(), bookId});
	}
	
	public void updateGenre(Genre genre) throws ClassNotFoundException, SQLException{
		save("update tbl_genre set genre_name = ? where genre_id = ?", new Object[]{genre.getGenreName(), genre.getGenreId()});
	}
	
	public void deleteGenre(Genre genre) throws ClassNotFoundException, SQLException{
		save("delete * from tbl_genre where genre_id = ?", new Object[] {genre.getGenreId()});
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Genre> genres = new ArrayList<>();
		BookDAO bdao = new BookDAO(conn);
		while(rs.next()){
			Genre g = new Genre();
			g.setGenreName(rs.getString("genre_name"));
			g.setGenreId(rs.getInt("genre_id"));
			g.setBooks(bdao.readFirstLevel("select * from tbl_book where bookId IN (Select bookId from tbl_book_genres where genre_id = ?)", new Object[]{g.getGenreId()}));
			genres.add(g);
		}
		return genres;
	}
	
	@Override
	public List<Genre> extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Genre> genres = new ArrayList<>();
		while(rs.next()){
			Genre b = new Genre();
			b.setGenreName(rs.getString("genre_name"));
			b.setGenreId(rs.getInt("genre_id"));
			genres.add(b);
		}
		return genres;
	}

}