package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.library.entity.Borrower;
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
			//pageAuthors(request);
			forwardPath = "/adminBorrowerManage.jsp";
			break;
		case "/searchBorrowers":
			//String data = searchAuthors(request);
		//	response.getWriter().write(data);
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

	private void removeBorrower(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer borrowerId = Integer.parseInt((String)request.getParameter("borrowerId"));
			service.removeBorrower(borrowerId);
			request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower successfully deleted. </div>");
		} catch (NumberFormatException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
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
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower details successfully updated. </div>");
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
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Borrower successfully added. </div>");
	}

}
