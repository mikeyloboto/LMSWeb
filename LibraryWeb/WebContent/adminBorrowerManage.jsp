<%@include file="include.html"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.gcit.library.entity.Borrower"%>
<%@page import="com.gcit.library.service.AdminService"%>


<%
	AdminService service = new AdminService();
	Integer borrowerCount = service.getBorrowerCount();
	Integer numOfPages = 0;
	if (borrowerCount % 10 > 0) {
		numOfPages = borrowerCount / 10 + 1;
	} else {
		numOfPages = borrowerCount / 10;
	}
	List<Borrower> borrowers = new ArrayList<>();
	//System.out.println(request.getParameter("pageNo"));
	if (request.getParameter("pageNo") != null) {
		borrowers = service.getAllBorrowers(Integer.parseInt((String) request.getParameter("pageNo")));
	} else {
		borrowers = service.getAllBorrowers(1);
	}
%>
${message}
<ol class="breadcrumb">
	<li><a href="index.jsp">Home</a></li>
	<li><a href="admin.jsp">Administrator</a></li>
	<li class="active">Borrower Management</li>
</ol>
<div class="container">
	<div>
		<button type="button" class="btn btn-success" data-toggle="modal"
			data-target="#addBorrowerModal" href="adminBorrowerAdd.jsp">Add
			New Borrower</button>
		<div class="page-header">
			<h1>List of Existing Borrowers in LMS</h1>
		</div>
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<li><a href="#" aria-label="Previous"> <span
						aria-hidden="true">&laquo;</span>
				</a></li>
				<%
					for (int i = 1; i <= numOfPages; i++) {
				%>
				<li><a href="adminBorrowerManage.jsp?pageNo=<%=i%>"><%=i%></a></li>
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
					<th>Borrower Name</th>
					<th>Address</th>
					<th>Phone</th>
					<th>Actions</th>
					<!-- <th>Delete</th> -->
				</tr>
			</thead>
			<tbody>
				<%
					for (Borrower p : borrowers) {
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
						Integer id = borrowers.indexOf(p) + 1 + ((mod - 1) * 10);
					%>
					<td><%=id%></td>
					<td><%=p.getName()%></td>
					<td><%=p.getAddress()%></td>
					<td><%=p.getPhone()%></td>
					<td><button type="button" class="btn btn-primary"
							data-toggle="modal" data-target="#editBorrowerModal"
							href="adminBorrowerEdit.jsp?borrowerId=<%=p.getCardNo()%>&pageNo=<%=pageNo%>">Update</button>
						<a type="button" class="btn btn-danger"
						href="removeBorrower?borrowerId=<%=p.getCardNo()%>">Delete</a></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>

	<div class="modal fade bs-example-modal-lg" id="editBorrowerModal"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">....</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-lg" id="addBorrowerModal"
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