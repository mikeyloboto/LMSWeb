package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Genre;
import com.gcit.library.entity.Publisher;
import com.gcit.library.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addBook", "/editBook", "/removeBook", "/searchBooks" })
public class AdminBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBookServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(request.getContextPath().length(),
				request.getRequestURI().length());
		String forwardPath = "/adminBookManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pageAuthors":
			//pageAuthors(request);
			forwardPath = "/adminBookManage.jsp";
			break;
		case "/searchAuthors":
			//String data = searchAuthors(request);
		//	response.getWriter().write(data);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
			break;
		case "/removeBook":
			removeBook(request);
			break;
		default:
			break;
		}
		if (!isAjax) {
			RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(request.getContextPath().length(),
				request.getRequestURI().length());
		String forwardPath = "/adminBookManage.jsp";
		switch (reqUrl) {

		case "/addBook":
			addBook(request);
			break;
		case "/editBook":
			editBook(request);
			break;
		case "/removeBook":
			removeBook(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void removeBook(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer bookId = Integer.parseInt((String)request.getParameter("bookId"));
			System.out.println("removing " + bookId);
			service.removeBook(bookId);
			request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Book successfully deleted. </div>");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void editBook(HttpServletRequest request) {
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		book.setTitle(request.getParameter("bookName"));
		String[] authorIds = request.getParameterValues("authors");
		List<Author> authors = new ArrayList<>();
		if (authorIds != null && authorIds.length > 0) {
			for (int i = 0; i < authorIds.length; i++) {
				Author a = new Author();
				a.setAuthorId(Integer.parseInt(authorIds[i]));
				System.out.println("adding authorId " + authorIds[i]);
				authors.add(a);
			}
		}

		String[] genreIds = request.getParameterValues("genres");
		List<Genre> genres = new ArrayList<>();
		if (genreIds != null && genreIds.length > 0) {

			for (int i = 0; i < genreIds.length; i++) {
				Genre g = new Genre();
				g.setGenreId(Integer.parseInt(genreIds[i]));
				genres.add(g);
			}
		}
		Publisher pub = new Publisher();
		pub.setPublisherId(Integer.parseInt(request.getParameter("publisher")));

		book.setAuthors(authors);
		book.setGenres(genres);
		book.setPublisher(pub);

		AdminService service = new AdminService();
		try {
			service.modBook(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Book details successfully updated. </div>");
	}

	private void addBook(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Book book = new Book();
		book.setTitle(request.getParameter("bookName"));
		String[] authorIds = request.getParameterValues("authors");
		List<Author> authors = new ArrayList<>();
		if (authorIds != null && authorIds.length > 0) {
			for (int i = 0; i < authorIds.length; i++) {
				Author a = new Author();
				a.setAuthorId(Integer.parseInt(authorIds[i]));
				System.out.println("adding authorId " + authorIds[i]);
				authors.add(a);
			}
		}

		String[] genreIds = request.getParameterValues("genres");
		List<Genre> genres = new ArrayList<>();
		if (genreIds != null && genreIds.length > 0) {

			for (int i = 0; i < genreIds.length; i++) {
				Genre g = new Genre();
				g.setGenreId(Integer.parseInt(genreIds[i]));
				genres.add(g);
			}
		}
		Publisher pub = new Publisher();
		pub.setPublisherId(Integer.parseInt(request.getParameter("publisher")));

		book.setAuthors(authors);
		book.setGenres(genres);
		book.setPublisher(pub);

		AdminService service = new AdminService();
		try {
			service.addBook(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Book successfully added. </div>");
	}

}
