package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;
import com.gcit.library.service.AdminService;
import com.gcit.library.service.LibrarianService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/editBranchLibrarian", "/updateStock", "/addBookStock"})
public class LibrarianBranchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LibrarianBranchServlet() {
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
		String forwardPath = "/librarianMain.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
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
		String forwardPath = "/librarianMain.jsp";
		switch (reqUrl) {

		case "/editBranchLibrarian":
			editBranch(request);
			break;
		case "/updateStock":
			forwardPath = "/librarianStockManage.jsp";
			editStock(request);
			break;
		case "/addBookStock":
			forwardPath = "/librarianStockManage.jsp";
			addStock(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void editStock(HttpServletRequest request) {
		Branch br = new Branch();
		br.setBranchNo(Integer.parseInt(request.getParameter("branchId")));
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		Integer copies = Integer.parseInt(request.getParameter("noOfCopies"));
		LibrarianService service = new LibrarianService();
		try {
			service.updateCopies(br, book, copies);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Stock successfully updated. </div>");
	
	}
	
	private void addStock(HttpServletRequest request) {
		Branch br = new Branch();
		br.setBranchNo(Integer.parseInt(request.getParameter("branchId")));
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		Integer copies = Integer.parseInt(request.getParameter("noOfCopies"));
		LibrarianService service = new LibrarianService();
		try {
			service.incrementCopies(br, book, copies);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Book(s) successfully added to your branch. </div>");
	
	}

	private void editBranch(HttpServletRequest request) {
		Branch g = new Branch();
		g.setBranchNo(Integer.parseInt(request.getParameter("branchId")));
		g.setBranchName(request.getParameter("branchName"));
		g.setBranchAddress(request.getParameter("branchAddress"));

		LibrarianService service = new LibrarianService();
		try {
			service.modBranch(g);
		} catch (SQLException e) {
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Branch details successfully updated. </div>");
	}
}
