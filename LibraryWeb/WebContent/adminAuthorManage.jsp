<%@include file="include.html"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Author"%>
<%@page import="com.gcit.library.service.AdminService"%>


<%
	AdminService service = new AdminService();
	Integer authorCount = service.getAuthorCount();
	Integer numOfPages = 0;
	if (authorCount % 10 > 0) {
		numOfPages = authorCount / 10 + 1;
	} else {
		numOfPages = authorCount / 10;
	}
	List<Author> authors = new ArrayList<>();
	//System.out.println(request.getParameter("pageNo"));
	if (request.getParameter("pageNo") != null) {
		authors = service.getAllAuthors(Integer.parseInt((String) request.getParameter("pageNo")));
	} else {
		authors = service.getAllAuthors(1);
	}
%>
${message}
<ol class="breadcrumb">
	<li><a href="index.jsp">Home</a></li>
	<li><a href="admin.jsp">Administrator</a></li>
	<li class="active">Author Management</li>
</ol>
<div class="container">
	<div>
		<button type="button" class="btn btn-success" data-toggle="modal"
			data-target="#addAuthorModal" href="adminAuthorAdd.jsp">Add
			New Author</button>
		<div class="page-header">
			<h1>List of Existing Authors in LMS</h1>
		</div>
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<li><a href="#" aria-label="Previous"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<%
					for (int i = 1; i <= numOfPages; i++) {
				%>
				<li><a href="adminAuthorManage.jsp?pageNo=<%=i%>"><%=i%></a></li>
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
					<th>Author Name</th>
					<th>Actions</th>
					<!-- <th>Delete</th> -->
				</tr>
			</thead>
			<tbody>
				<%
					for (Author a : authors) {
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
						Integer id = authors.indexOf(a) + 1 + ((mod - 1) * 10);
					%>
					<td><%=id%></td>
					<td><%=a.getAuthorName()%></td>
					<td><button type="button" class="btn btn-primary"
							data-toggle="modal" data-target="#editAuthorModal"
							href="adminAuthorEdit.jsp?authorId=<%=a.getAuthorId()%>&pageNo=<%=pageNo%>">Update</button>
						<a type="button" class="btn btn-danger"
						href="removeAuthor?authorId=<%=a.getAuthorId()%>">Delete</a></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>

	<div class="modal fade bs-example-modal-lg" id="editAuthorModal"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">....</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-lg" id="addAuthorModal"
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