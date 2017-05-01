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

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Publisher;
import com.gcit.library.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addAuthor", "/editAuthor", "/removeAuthor", "/searchAuthors" })
public class AdminAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminAuthorServlet() {
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
		String forwardPath = "/adminAuthorManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pageAuthors":
			// pageAuthors(request);
			forwardPath = "/adminAuthorManage.jsp";
			break;
		case "/searchAuthors":
			// System.out.println("test");
			String table = searchAuthors(request);
			String pagination = pageAuthors(request);
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");
			response.getWriter().write(table + '\n' + pagination);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
			break;
		case "/removeAuthor":
			removeAuthor(request);
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
		String forwardPath = "/adminAuthorManage.jsp";
		switch (reqUrl) {

		case "/addAuthor":
			addAuthor(request);
			break;
		case "/editAuthor":
			editAuthor(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}
	private String pageAuthors(HttpServletRequest request) {
		String searchString = request.getParameter("searchString");
		// System.out.println(searchString);
		AdminService service = new AdminService();
		StringBuffer strBuf = new StringBuffer();
		try {
			// request.setAttribute("authors", service.getAuthorsByName(1,
			// searchString));
			Integer count = service.getAuthorsFromNameCount(searchString);
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

				strBuf.append("<li><a href=\"#\" onclick=\"searchAuthor(" + i + ")\">" + i + "</a></li>");
			}

			strBuf.append("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchAuthors(HttpServletRequest request) {
		String pageNoPar = request.getParameter("pageNo");
		System.out.println(pageNoPar);
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
			List<Author> auths = service.getAuthorsFromName(pageNo, searchString);
			//System.out.println(auths.size());
			// strBuf.append("<thead><tr><th>#</th><th>Author
			// Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
			for (Author a : auths) {
				strBuf.append("<tr><td>" + (auths.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getAuthorName() + "</td>");
				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#editAuthorModal\""
						+ " href=\"adminAuthorEdit.jsp?authorId=" + a.getAuthorId() + "&pageNo=" + pageNo
						+ "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeAuthor?authorId="
						+ a.getAuthorId() + "\">Delete</a></td></tr>");
			}
			// strBuf.append("</tbody>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}
	private void removeAuthor(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer authorId = Integer.parseInt((String) request.getParameter("authorId"));
			service.removeAuthor(authorId);
			request.setAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Author successfully deleted. </div>");
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

	private void editAuthor(HttpServletRequest request) {
		Author author = new Author();
		author.setAuthorId(Integer.parseInt(request.getParameter("authorId")));
		author.setAuthorName(request.getParameter("authorName"));

		AdminService service = new AdminService();
		try {
			service.modAuthor(author);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Author details successfully updated. </div>");
	}

	private void addAuthor(HttpServletRequest request) {
		// TODO Auto-generated method stub
		Author a = new Author();
		a.setAuthorName(request.getParameter("authorName"));
		AdminService service = new AdminService();
		try {
			service.addAuthor(a);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Author successfully added. </div>");
	}

}
