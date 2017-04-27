package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Publisher;

public class PublisherDAO extends BaseDAO {

	public PublisherDAO(Connection conn) {
		super(conn);
	}

	public void addPublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values (?, ?, ?)",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(),
						publisher.getPublisherPhone() });
	}

	public void updatePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { publisher.getPublisherName(), publisher.getPublisherAddress(), publisher.getPublisherPhone(), publisher.getPublisherId() });
	}

	public void deletePublisher(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("delete * from tbl_publisher where publisherId = ?", new Object[] { publisher.getPublisherId() });
	}

	public List<Publisher> readAllPublishers() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_publisher", null);
	}

	public Publisher readPublisherByID(Integer PublisherID) throws ClassNotFoundException, SQLException {
		List<Publisher> Publishers = read("select * from tbl_publisher where publisherId = ?",
				new Object[] { PublisherID });
		if (Publishers != null && !Publishers.isEmpty()) {
			return Publishers.get(0);
		}
		return null;
	}

	public List<Publisher> readPublishersByName(String PublisherName) throws ClassNotFoundException, SQLException {
		return read("select * from tbl_publisher where publisherName like ?", new Object[] { PublisherName });
	}

	@Override
	public List extractData(ResultSet rs) throws SQLException, ClassNotFoundException {

		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			Publisher a = new Publisher();
			a.setPublisherId(rs.getInt("publisherId"));
			a.setPublisherName(rs.getString("publisherName"));
			a.setPublisherAddress(rs.getString("publisherAddress"));
			a.setPublisherPhone(rs.getString("publisherPhone"));
			a.setBooks(bdao.readFirstLevel(
					"select * from tbl_book where publisherId = ?)",
					new Object[] { a.getPublisherId() }));
			publishers.add(a);
		}
		return publishers;
	}

	@Override
	public List extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			BookDAO bdao = new BookDAO(conn);
			Publisher a = new Publisher();
			a.setPublisherId(rs.getInt("publisherId"));
			a.setPublisherName(rs.getString("publisherName"));
			a.setPublisherAddress(rs.getString("publisherAddress"));
			a.setPublisherPhone(rs.getString("publisherPhone"));
			publishers.add(a);
		}
		return publishers;
	}

}