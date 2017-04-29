<%@include file="include.html"%>

<ol class="breadcrumb">
	<li><a href="index.jsp">Home</a></li>
	<li class="active">Administrator</li>
</ol>

<div class="jumbotron">
	<div class="container">
		<h1>LMS Administrator</h1>
		<p>Welcome to Library Administration Panel.</p>
	</div>
</div>

<div class="container">
	<div class="row">
		<div class="col-sm-4">

			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Manage Books</h3>
				</div>
				<div class="panel-body">
					<p>Add / Delete / Modify Books</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminBookManage.jsp">Book Management</a>
					</p>
				</div>
			</div>
		</div>
		<div class="col-sm-4">

			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Manage Authors</h3>
				</div>
				<div class="panel-body">
					<p>Add / Delete / Modify Authors</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminAuthorManage.jsp">Author Management</a>
					</p>
				</div>
			</div>

		</div>
		<div class="col-sm-4">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Manage Genres</h3>
				</div>
				<div class="panel-body">
					<p>Add / Delete / Modify Genres</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminGenreManage.jsp">Genre Management</a>
					</p>
				</div>
			</div>
		</div>
		<div class="col-sm-4">
			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Manage Publishers</h3>
				</div>
				<div class="panel-body">
					<p>Add / Delete / Modify Publishers</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminPublisherManage.jsp">Publisher Management</a>
					</p>
				</div>
			</div>
		</div>
		<div class="col-sm-4">

			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Manage Branches</h3>
				</div>
				<div class="panel-body">
					<p>Add / Delete / Modify Branches</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminBranchManage.jsp">Branch Management</a>
					</p>
				</div>
			</div>
		</div>
		<div class="col-sm-4">


			<div class="panel panel-info">
				<div class="panel-heading">
					<h3 class="panel-title">Override Loans</h3>
				</div>
				<div class="panel-body">
					<p>Override Due Dates on Book Loans</p>
					<p>
						<a type="button" class="btn btn-primary"
							href="adminLoanManage.jsp">Loan Override</a>
					</p>
				</div>
			</div>
		</div>
	</div>
</div>