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
			// pageAuthors(request);
			forwardPath = "/adminPublisherManage.jsp";
			break;
		case "/searchPublishers":
			// System.out.println("test");
			String table = searchPublishers(request);
			String pagination = pagePublishers(request);
			// response.setContentType("application/json");
			// response.setCharacterEncoding("UTF-8");
			response.getWriter().write(table + '\n' + pagination);
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

	private String pagePublishers(HttpServletRequest request) {
		String searchString = request.getParameter("searchString");
		// System.out.println(searchString);
		AdminService service = new AdminService();
		StringBuffer strBuf = new StringBuffer();
		try {
			// request.setAttribute("authors", service.getAuthorsByName(1,
			// searchString));
			Integer count = service.getPublishersFromNameCount(searchString);
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

				strBuf.append("<li><a href=\"#\" onclick=\"searchPublisher(" + i + ")\">" + i + "</a></li>");
			}

			strBuf.append("<li><a href=\"#\" aria-label=\"Next\"> <span aria-hidden=\"true\">&raquo;</span></a></li>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}

	private String searchPublishers(HttpServletRequest request) {
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
			List<Publisher> pubs = service.getPublishersFromName(pageNo, searchString);
			System.out.println(pubs.size());
			// strBuf.append("<thead><tr><th>#</th><th>Author
			// Name</th><th>Edit</th><th>Delete</th></tr></thead><tbody>");
			for (Publisher a : pubs) {
				strBuf.append("<tr><td>" + (pubs.indexOf(a) + 1 + (pageNo - 1) * 10) + "</td><td>"
						+ a.getPublisherName() + "</td>");
				strBuf.append("<td>" + a.getPublisherAddress() + "</td><td>" + a.getPublisherPhone() + "</td>");

				strBuf.append("<td><button type=\"button\" class=\"btn btn-primary\""
						+ " data-toggle=\"modal\" data-target=\"#editPublisherModal\""
						+ " href=\"adminPublisherEdit.jsp?publisherId=" + a.getPublisherId() + "&pageNo=" + pageNo
						+ "\">Update</button> ");
				strBuf.append("<a type=\"button\" class=\"btn btn-danger\"" + " href=\"removePublisher?publisherId="
						+ a.getPublisherId() + "\">Delete</a></td></tr>");
			}
			// strBuf.append("</tbody>");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return strBuf.toString();
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
			Integer publisherId = Integer.parseInt((String) request.getParameter("publisherId"));
			service.removePublisher(publisherId);
			request.setAttribute("message",
					"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher successfully deleted. </div>");
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
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher details successfully updated. </div>");
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
			request.setAttribute("message",
					"<div class=\"alert alert-danger\" role=\"alert\"> <strong>Oops!</strong> Something went wrong. </div>");
			e.printStackTrace();
		}
		request.setAttribute("message",
				"<div class=\"alert alert-success\" role=\"alert\"> <strong>Success!</strong> Publisher successfully added. </div>");
	}

}
