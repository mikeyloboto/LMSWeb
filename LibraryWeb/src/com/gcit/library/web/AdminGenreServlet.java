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
@WebServlet({ "/addGenre", "/editGenre", "/removeGenre", "/searchGenres" })
public class AdminGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminGenreServlet() {
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
		String forwardPath = "/adminGenreManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pageGenres":
			//pageAuthors(request);
			forwardPath = "/adminGenreManage.jsp";
			break;
		case "/searchGenres":
			//String data = searchAuthors(request);
		//	response.getWriter().write(data);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
			break;
		case "/removeGenre":
			removeGenre(request);
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
		String forwardPath = "/adminGenreManage.jsp";
		switch (reqUrl) {

		case "/addGenre":
			addGenre(request);
			break;
		case "/editGenre":
			editGenre(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void removeGenre(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer genreId = Integer.parseInt((String)request.getParameter("genreId"));
			service.removeGenre(genreId);
			request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre successfully deleted. </div>");
		} catch (NumberFormatException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
	}

	private void editGenre(HttpServletRequest request) {
		Genre g = new Genre();
		g.setGenreId(Integer.parseInt(request.getParameter("genreId")));
		g.setGenreName(request.getParameter("genreName"));

		AdminService service = new AdminService();
		try {
			service.modGenre(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre details successfully updated. </div>");
	}

	private void addGenre(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Genre g = new Genre();
		g.setGenreName(request.getParameter("genreName"));
		AdminService service = new AdminService();
		try {
			service.addGenre(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre successfully added. </div>");
	}

}
