<%@include file="include.html"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Genre"%>
<%@page import="com.gcit.library.service.AdminService"%>


<%
	AdminService service = new AdminService();
	Integer genreCount = service.getGenreCount();
	Integer numOfPages = 0;
	if (genreCount % 10 > 0) {
		numOfPages = genreCount / 10 + 1;
	} else {
		numOfPages = genreCount / 10;
	}
	List<Genre> genres = new ArrayList<>();
	//System.out.println(request.getParameter("pageNo"));
	if (request.getParameter("pageNo") != null) {
		genres = service.getAllGenres(Integer.parseInt((String) request.getParameter("pageNo")));
	} else {
		genres = service.getAllGenres(1);
	}
%>
${message}
<ol class="breadcrumb">
	<li><a href="index.jsp">Home</a></li>
	<li><a href="admin.jsp">Administrator</a></li>
	<li class="active">Genre Management</li>
</ol>
<div class="container">
	<div>
		<button type="button" class="btn btn-success" data-toggle="modal"
			data-target="#addGenreModal" href="adminGenreAdd.jsp">Add
			New Genre</button>
		<div class="page-header">
			<h1>List of Existing Genres in LMS</h1>
		</div>
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<li><a href="#" aria-label="Previous"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<%
					for (int i = 1; i <= numOfPages; i++) {
				%>
				<li><a href="adminGenreManage.jsp?pageNo=<%=i%>"><%=i%></a></li>
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
					<th>Genre Name</th>
					<th>Actions</th>
					<!-- <th>Delete</th> -->
				</tr>
			</thead>
			<tbody>
				<%
					for (Genre g : genres) {
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
						Integer id = genres.indexOf(g) + 1 + ((mod - 1) * 10);
					%>
					<td><%=id%></td>
					<td><%=g.getGenreName()%></td>
					<td><button type="button" class="btn btn-primary"
							data-toggle="modal" data-target="#editGenreModal"
							href="adminGenreEdit.jsp?genreId=<%=g.getGenreId()%>&pageNo=<%=pageNo%>">Update</button>
						<a type="button" class="btn btn-danger"
						href="removeGenre?genreId=<%=g.getGenreId()%>">Delete</a></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>

	<div class="modal fade bs-example-modal-lg" id="editGenreModal"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">....</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-lg" id="addGenreModal"
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