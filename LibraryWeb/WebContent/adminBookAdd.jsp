<%@page import="java.util.List" %>
<%@page import="com.gcit.library.entity.Author"%>
<%@page import="com.gcit.library.entity.Book"%>
<%@page import="com.gcit.library.entity.Genre"%>
<%@page import="com.gcit.library.entity.Publisher"%>
<%@page import="com.gcit.library.service.AdminService"%>
<%
	AdminService service = new AdminService();
	List<Author> authors = service.getAllAuthors(null);
	List<Genre> genres = service.getAllGenres(null);
	List<Publisher> publishers = service.getAllPublishers(null);
	//Integer bookId = Integer.parseInt(request.getParameter("bookId"));
	//Book book = service.getBookFromID(bookId);
	//System.out.println("New Modal Init");
%>
<div>
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
		<h4 class="modal-title">Add New Book</h4>
	</div>
	<form action="addBook" method="post">
		<div class="modal-body">
			<p>Enter the title of Book:</p>
			<input class="form-control" type="text" name="bookName" required="required"> <br />
			<!-- Put shite here -->
			<p>Select Author(s):</p>
			<select multiple class="form-control" name="authors">
			<%for (Author a : authors) {%>
				<option value="<%=a.getAuthorId()%>"><%=a.getAuthorName()%></option>
				<%} %>
			</select> <br />
				<p>Select Genre(s):</p>
			<select multiple class="form-control" name="genres">
			<%for (Genre g : genres) {%>
				<option value="<%=g.getGenreId()%>"><%=g.getGenreName()%></option>
				<%} %>
			</select> <br />
				<p>Select Publisher:</p>
			<select class="form-control" name="publisher">
			<%for (Publisher p : publishers) {%>
				<option value="<%=p.getPublisherId()%>"><%=p.getPublisherName()%></option>
				<%} %>
			</select> <br />
			<br>
			<div class="alert alert-info" role="alert">
			<strong>Hint,</strong> you can use "Ctrl" button to select multiple authors and genres.
			</div>
			

		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			<button type="submit" class="btn btn-primary">Create</button>
		</div>
	</form>
</div>