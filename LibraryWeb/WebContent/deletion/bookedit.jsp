<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Enumeration"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Book"%>
<%@page import="com.gcit.library.service.AdminService"%>
<%
	AdminService service = new AdminService();
%>
<%
	Book editing = service.getBookFromID(Integer.parseInt(request.getParameter("bookId")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Editing <%=editing.getDescription()%></title>
</head>
<body>
<h1>Currently editing <%=editing.getDescription()%></h1>
<form method="post" action="/admin/updateBook">
		<input >
		<button type="submit" name="removeButton">Apply</button>
	</form>

</body>
</html>