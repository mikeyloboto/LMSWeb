<%@include file="include.html"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Book"%>
<%@page import="com.gcit.library.service.AdminService"%>


<%
	AdminService service = new AdminService();
	Integer bookCount = service.getBookCount();
	Integer numOfPages = 0;
	if (bookCount % 10 > 0) {
		numOfPages = bookCount / 10 + 1;
	} else {
		numOfPages = bookCount / 10;
	}
	List<Book> books = new ArrayList<>();
	//System.out.println(request.getParameter("pageNo"));
	if (request.getParameter("pageNo") != null) {
		books = service.getAllBooks(Integer.parseInt((String) request.getParameter("pageNo")));
	} else {
		books = service.getAllBooks(1);
	}
%>
${message}
<ol class="breadcrumb">
  <li><a href="index.jsp">Home</a></li>
  <li><a href="admin.jsp">Administrator</a></li>
  <li class="active">Book Management</li>
</ol>
<div class="container">
	<div>
		<button type="button" class="btn btn-success" data-toggle="modal"
			data-target="#editBookModal" href="adminBookAdd.jsp">Add New
			Book</button>
	<div class="page-header">
		<h1>List of Existing Books in LMS</h1>
	</div>
	<nav aria-label="Page navigation">
		<ul class="pagination">
			<li><a href="#" aria-label="Previous"> <span
					aria-hidden="true">&laquo;</span>
			</a></li>
			<%
				for (int i = 1; i <= numOfPages; i++) {
			%>
			<li><a href="adminBookManage.jsp?pageNo=<%=i%>"><%=i%></a></li>
			<%
				}
			%>
			<li><a href="#" aria-label="Next"> <span aria-hidden="true">&raquo;</span>
			</a></li>
		</ul>
	</nav>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>#</th>
				<th>Book Name</th>
				<th>Genre(s)</th>
				<th>Publisher</th>
				<th>Actions</th>
				<!-- <th>Delete</th> -->
			</tr>
		</thead>
		<tbody>
			<%
				for (Book b : books) {
					Integer mod = 1;
					if (request.getParameter("pageNo") != null) {
						mod = Integer.parseInt(request.getParameter("pageNo"));
					}
					Integer pageNo = 1;
					if (request.getParameter("pageNo") != null) {
						pageNo = Integer.parseInt(request.getParameter("pageNo"));
					}
			%>
			<tr>
				<%
					Integer id = books.indexOf(b) + 1 + ((mod - 1) * 10);
				%>
				<td><%=id%></td>
				<td><%=b.getDescription()%></td>
				<td><%=b.getGenreList() %></td>
				<td><%=b.getPublisher().getPublisherName() %></td>
				<td><button type="button" class="btn btn-primary"
						data-toggle="modal" data-target="#editBookModal"
						href="adminBookEdit.jsp?bookId=<%=b.getBookId()%>&pageNo=<%=pageNo%>">Update</button>
					<a type="button" class="btn btn-danger"
						href="removeBook?bookId=<%=b.getBookId()%>">Delete</a></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
</div>

<div class="modal fade bs-example-modal-lg" id="editBookModal"
	tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">....</div>
	</div>
</div>
<div class="modal fade bs-example-modal-lg" id="addBookModal"
	tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">....</div>
	</div>
</div>
</div>
<script>
	$(document).ready(function() {
		// codes works on all bootstrap modal windows in application
		$('.modal').on('hidden.bs.modal', function(e) {
			$(this).removeData();
		});

	});
</script>