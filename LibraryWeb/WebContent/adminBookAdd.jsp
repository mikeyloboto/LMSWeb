<%@page import="com.gcit.library.entity.Book"%>
<%@page import="com.gcit.library.service.AdminService"%>
<%AdminService service = new AdminService();
	//Integer bookId = Integer.parseInt(request.getParameter("bookId"));
	//Book book = service.getBookFromID(bookId);
	System.out.println("New Modal Init");
%>
<div>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">Add New Book%></h4>
	</div>
	<form action="editBook" method="post">
		<div class="modal-body">
			<p>Enter the details of your Book:</p>
			<input type="text" name="bookName" required="required"> <br />
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="submit" class="btn btn-primary">Create</button>
		</div>
	</form>
</div>