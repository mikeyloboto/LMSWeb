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

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addLoan", "/editLoan", "/closeLoan", "/searchLoans" })
public class AdminLoanServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminLoanServlet() {
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
		String forwardPath = "/adminLoanManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pageLoans":
			//pageAuthors(request);
			forwardPath = "/adminLoanManage.jsp";
			break;
		case "/searchLoans":
			//String data = searchAuthors(request);
		//	response.getWriter().write(data);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
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
		String forwardPath = "/adminLoanManage.jsp";
		switch (reqUrl) {

		case "/addLoan":
			addLoan(request);
			break;
		case "/editLoan":
			editLoan(request);
			break;
		case "/closeLoan":
			closeLoan(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void closeLoan(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	private void editLoan(HttpServletRequest request) {
		Loan g = new Loan();
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		g.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		g.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchNo(Integer.parseInt(request.getParameter("branchNo")));
		g.setBranch(branch);
		LocalDateTime dateOut = LocalDateTime.parse(request.getParameter("dateOut"));
		g.setDateOut(dateOut);
		LocalDate dueDate = LocalDate.parse(request.getParameter("dueDate"));
		g.setDateDue(dueDate);
		LocalDate dateIn = LocalDate.parse(request.getParameter("dateIn"));
		g.setDateIn(dateIn);
		
		AdminService service = new AdminService();
		try {
			service.modLoan(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Loan details successfully updated. </div>");
	}

	private void addLoan(HttpServletRequest request) {
		Loan g = new Loan();
		Book book = new Book();
		book.setBookId(Integer.parseInt(request.getParameter("bookId")));
		g.setBook(book);
		Borrower borrower = new Borrower();
		borrower.setCardNo(Integer.parseInt(request.getParameter("cardNo")));
		g.setBorrower(borrower);
		Branch branch = new Branch();
		branch.setBranchNo(Integer.parseInt(request.getParameter("branchNo")));
		g.setBranch(branch);
		LocalDateTime dateOut = LocalDateTime.parse(request.getParameter("dateOut"));
		g.setDateOut(dateOut);
		AdminService service = new AdminService();
		try {
			service.addLoan(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Loan successfully added. </div>");
	}

}
