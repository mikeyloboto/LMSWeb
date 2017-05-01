package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			// pageAuthors(request);
			forwardPath = "/adminGenreManage.jsp";
			break;
		case "/searchGenres":
			// System.out.println("test");
			String table = searchGenres(request);
			String pagination = pageGenres(request);
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");
			response.getWriter().write(table + '\n' + pagination);
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

	private String pageGenres(HttpServletRequest request) {
		String searchString = request.getParameter("searchString");
		// System.out.println(searchString);
		AdminService service = new AdminService();
		StringBuffer strBuf = new StringBuffer();
		try {
			// request.setAttribute("authors", service.getAuthorsByName(1,
			// searchString));
			Integer count = service.getGenresFromNameCount(searchString);
			Integer pages = 1;
			if (count != 0) {
				if (count % 10 == 0) {
					pages = count / 10;
				} else {
					pages = count / 10 + 1;
				}
			}
			// strBuf.append("<thead><tr><th>#</th><th>Author
			// Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
			strBuf.append(
					"<li><a href=\"#\" aria-label=\"Previous\"> <span	aria-hidden=\"true\">&laquo;</span></a></li>");
			for (int i = 1; i <= pages; i++) {

				strBuf.append("<li><a href=\"#\" onclick=\"searchGenre(" + i + ")\">" + i + "</a></li>");
			}

			strBuf.append("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchGenres(HttpServletRequest request) {
		String pageNoPar = request.getParameter("pageNo");
		//System.out.println(pageNoPar);
		Integer pageNo;
		if (pageNoPar == null)
			pageNo = 1;
		else
			pageNo = Integer.parseInt(pageNoPar);
		String searchString = request.getParameter("searchString");
		// System.out.println(searchString);
		AdminService service = new AdminService();
		StringBuffer strBuf = new StringBuffer();
		try {
			// request.setAttribute("authors", service.getAuthorsByName(1,
			// searchString));
			List<Genre> gens = service.getGenresFromName(pageNo, searchString);
			//System.out.println(gens.size());
			// strBuf.append("<thead><tr><th>#</th><th>Author
			// Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
			for (Genre a : gens) {
				strBuf.append("<tr><td>" + (gens.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getGenreName() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#editGenreModal\""
						+ " href=\"adminGenreEdit.jsp?genreId=" + a.getGenreId() + "&pageNo=" + pageNo
						+ "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeGenre?genreId="
						+ a.getGenreId() + "\">Delete</a></td></tr>");
			}
			// strBuf.append("</tbody>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
	
	private void removeGenre(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer genreId = Integer.parseInt((String) request.getParameter("genreId"));
			service.removeGenre(genreId);
			request.setAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre successfully deleted. </div>");
		} catch (NumberFormatException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
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
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre details successfully updated. </div>");
	}

	private void addGenre(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Genre g = new Genre();
		g.setGenreName(request.getParameter("genreName"));
		AdminService service = new AdminService();
		try {
			service.addGenre(g);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Genre successfully added. </div>");
	}

}
