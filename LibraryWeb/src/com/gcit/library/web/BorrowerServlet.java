package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.library.entity.Book;
import com.gcit.library.entity.Borrower;
import com.gcit.library.entity.Branch;
import com.gcit.library.entity.Loan;
import com.gcit.library.service.AdminService;
import com.gcit.library.service.BorrowerService;
import com.gcit.library.service.LibrarianService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/borrowBook", "/returnBook", "/authenticateBorrower"})
public class BorrowerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BorrowerServlet() {
		
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reqUrl = request.getRequestURI().substring(request.getContextPath().length(),
				request.getRequestURI().length());
		String forwardPath = "/borrowerMain.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {
		case "/returnBook":
			forwardPath = "/borrowerMain.jsp?branchId=" + request.getParameter("retBranchNo");
			//System.out.println("get");
			returnBook(request);
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
		String forwardPath = "/borrowerMain.jsp";
		switch (reqUrl) {

		
		// TODO edit all that bootleg shite here
		case "/borrowBook":
			forwardPath = "/borrowerMain.jsp?branchId=" + request.getParameter("branchNo");
			borrowBook(request);
			break;
		
		case "/authenticateBorrower":
			if (authenticate(request)) {
				forwardPath = "/borrowerMain.jsp";
			}
			else {
				forwardPath = "/borrower.jsp";
			}
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void returnBook(HttpServletRequest request) {
		Loan loan = new Loan();
		Book book = new Book();
		//System.out.println(request.getParameter("bookId"));
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		loan.setBook(book);
		Borrower borrower = new Borrower();
		//System.out.println(request.getParameter("cardNo"));
		borrower.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		loan.setBorrower(borrower);
		Branch branch = new Branch();
		//System.out.println(request.getParameter("branchNo"));
		branch.setBranchNo(Integer.parseInt(request.getParameter("branchNo")));
		loan.setBranch(branch);
		//System.out.println(request.getParameter("dateOut"));
		LocalDateTime dateOut = LocalDateTime.parse(request.getParameter("dateOut"));
		loan.setDateOut(dateOut);
		System.out.println(request.getParameter("retBranchNo"));
		Integer retBranch = Integer.parseInt(request.getParameter("retBranchNo"));
		BorrowerService service = new BorrowerService();
		try {
			service.closeLoan(retBranch, loan);
			request.setAttribute("authCardNo", request.getParameter("cardNo"));
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Loan successfully closed. </div>");
	}

	private boolean authenticate(HttpServletRequest request) {
		BorrowerService service = new BorrowerService();
		try {
			Borrower retrieved = service.getBorrowerFromID(Integer.parseInt(request.getParameter("cardNo")));
			if (retrieved != null) {
				request.setAttribute("authCardNo", retrieved.getCardNo());
				request.setAttribute("message",
						"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Welcome, " + retrieved.getName() +" </div>");
				return true;
			}
			else {
				request.setAttribute("message",
						"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Card number not found. </div>");
				return false;
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
		return false;
	}

	private void borrowBook(HttpServletRequest request) {
		Loan loan = new Loan();
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		loan.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		loan.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchNo(Integer.parseInt(request.getParameter("branchNo")));
		loan.setBranch(branch);
		BorrowerService service = new BorrowerService();
		try {
			service.startLoan(loan);
			request.setAttribute("authCardNo", request.getParameter("cardNo"));
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Have fun with the book :D. Please return it in 7 days. </div>");
	}
}
