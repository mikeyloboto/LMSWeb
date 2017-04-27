<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Book"%>
<%@page import="com.gcit.library.service.AdminService"%>
<%
AdminService adminService = new AdminService();
List<Book> books = adminService.getAllBooks();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manage Books</title>
</head>
<body>
	<h1>Book Management</h1>
	<form action="addBook" method="post">
		<input type="text" name="bookName"> <br />
		<button type="submit">Add New Book</button>
	</form>
	Or:<br>
	Manage Existing Book: <br>
	<form method="post">
	 <select name="bookId">
		<%for (Book b : books) {%>
			<option value=<%=b.getBookId()%>><% out.println(b.getDescription()); %></option>
		<%}%>
	</select><br>
		<button type="submit" name="editButton" formaction="bookedit.jsp">Edit</button>
		<button type="submit" name="removeButton" formaction="removeBook">Remove</button>
	</form>
</body>
</html>