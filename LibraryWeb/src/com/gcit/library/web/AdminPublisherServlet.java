package com.gcit.library.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gcit.library.entity.Publisher;
import com.gcit.library.service.AdminService;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet({ "/addPublisher", "/editPublisher", "/removePublisher", "/searchPublishers" })
public class AdminPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminPublisherServlet() {
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
		String forwardPath = "/adminPublisherManage.jsp";
		Boolean isAjax = Boolean.FALSE;
		switch (reqUrl) {

		case "/pagePublishers":
			//pageAuthors(request);
			forwardPath = "/adminPublisherManage.jsp";
			break;
		case "/searchPublishers":
			//String data = searchAuthors(request);
		//	response.getWriter().write(data);
			// forwardPath = "/viewauthors.jsp";
			isAjax = Boolean.TRUE;
			break;
		case "/removePublisher":
			removePublisher(request);
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
		String forwardPath = "/adminPublisherManage.jsp";
		switch (reqUrl) {

		case "/addPublisher":
			addPublisher(request);
			break;
		case "/editPublisher":
			editPublisher(request);
			break;
		default:
			break;
		}
		RequestDispatcher rd = request.getRequestDispatcher(forwardPath);
		rd.forward(request, response);
	}

	private void removePublisher(HttpServletRequest request) {
		// TODO Auto-generated method stub
		AdminService service = new AdminService();
		try {
			Integer publisherId = Integer.parseInt((String)request.getParameter("publisherId"));
			service.removePublisher(publisherId);
			request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher successfully deleted. </div>");
		} catch (NumberFormatException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
	}

	private void editPublisher(HttpServletRequest request) {
		Publisher g = new Publisher();
		g.setPublisherId(Integer.parseInt(request.getParameter("publisherId")));
		g.setPublisherName(request.getParameter("publisherName"));
		g.setPublisherAddress(request.getParameter("publisherAddress"));
		g.setPublisherPhone(request.getParameter("publisherPhone"));

		AdminService service = new AdminService();
		try {
			service.modPublisher(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher details successfully updated. </div>");
	}

	private void addPublisher(HttpServletRequest request) {
		Publisher g = new Publisher();
		g.setPublisherName(request.getParameter("publisherName"));
		g.setPublisherAddress(request.getParameter("publisherAddress"));
		g.setPublisherPhone(request.getParameter("publisherPhone"));
		AdminService service = new AdminService();
		try {
			service.addPublisher(g);
		} catch (SQLException e) {
			request.setAttribute("message", "<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message", "<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher successfully added. </div>");
	}

}
