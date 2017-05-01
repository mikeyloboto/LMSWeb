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

import com.gcit.library.entity.Borrower;
import com.gcit.library.entity.Publisher;
import com.gcit.library.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addBorrower", "/editBorrower", "/removeBorrower", "/searchBorrowers" })
public class AdminBorrowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminBorrowerServlet() {
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
		String forwardPath = "/adminBorrowerManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pageBorrowers":
			// pageAuthors(request);
			forwardPath = "/adminBorrowerManage.jsp";
			break;
		case "/searchBorrowers":
			// System.out.println("test");
			String table = searchBorrowers(request);
			String pagination = pageBorrowers(request);
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");
			response.getWriter().write(table + '\n' + pagination);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
			break;
		case "/removeBorrower":
			removeBorrower(request);
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
		String forwardPath = "/adminBorrowerManage.jsp";
		switch (reqUrl) {

		case "/addBorrower":
			addBorrower(request);
			break;
		case "/editBorrower":
			editBorrower(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private String pageBorrowers(HttpServletRequest request) {
		String searchString = request.getParameter("searchString");
		// System.out.println(searchString);
		AdminService service = new AdminService();
		StringBuffer strBuf = new StringBuffer();
		try {
			// request.setAttribute("authors", service.getAuthorsByName(1,
			// searchString));
			Integer count = service.getBorrowersFromNameCount(searchString);
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

				strBuf.append("<li><a href=\"#\" onclick=\"searchBorrower(" + i + ")\">" + i + "</a></li>");
			}

			strBuf.append("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchBorrowers(HttpServletRequest request) {
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
			List<Borrower> bors = service.getBorrowersFromName(pageNo, searchString);
			//System.out.println(bors.size());
			// strBuf.append("<thead><tr><th>#</th><th>Author
			// Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
			for (Borrower a : bors) {
				strBuf.append("<tr><td>" + a.getCardNo() + "</td><td>"
						+ a.getName() + "</td>");
				strBuf.append("<td>" + a.getAddress() + "</td><td>" + a.getPhone() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#editBorrowerModal\""
						+ " href=\"adminBorrowerEdit.jsp?borrowerId=" + a.getCardNo() + "&pageNo=" + pageNo
						+ "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removeBorrower?borrowerId="
						+ a.getCardNo() + "\">Delete</a></td></tr>");
			}
			// strBuf.append("</tbody>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private void removeBorrower(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer borrowerId = Integer.parseInt((String) request.getParameter("borrowerId"));
			service.removeBorrower(borrowerId);
			request.setAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower successfully deleted. </div>");
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

	private void editBorrower(HttpServletRequest request) {
		Borrower g = new Borrower();
		g.setCardNo(Integer.parseInt(request.getParameter("borrowerId")));
		g.setName(request.getParameter("borrowerName"));
		g.setAddress(request.getParameter("borrowerAddress"));
		g.setPhone(request.getParameter("borrowerPhone"));

		AdminService service = new AdminService();
		try {
			service.modBorrower(g);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower details successfully updated. </div>");
	}

	private void addBorrower(HttpServletRequest request) {
		Borrower g = new Borrower();
		g.setName(request.getParameter("borrowerName"));
		g.setAddress(request.getParameter("borrowerAddress"));
		g.setPhone(request.getParameter("borrowerPhone"));
		AdminService service = new AdminService();
		try {
			service.addBorrower(g);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower successfully added. </div>");
	}

}
