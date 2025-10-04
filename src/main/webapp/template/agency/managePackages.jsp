<%@ include file="header.jsp"%>
<div class="card shadow">
	<div class="card-header bg-warning">
		<h4>Manage Packages</h4>
	</div>
	<div class="card-body">
		<table class="table table-bordered table-hover">
			<thead class="table-dark">
				<tr>
					<th>ID</th>
					<th>Title</th>
					<th>Location</th>
					<th>Price</th>
					<th>Seats</th>
					<th>Status</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
			
				<tr>
					<td><%=%></td>
					<td><%=%></td>
					<td><%=%></td>
					<td>₹<%=%></td>
					<td><%=%></td>
					<td><span
						class="badge "bg-success" ">
							"Active" 
					</span></td>
					<td><a href="EditPackageServlet?id="
						class="btn btn-sm btn-info">Edit</a> <a
						href="DeletePackageServlet?id=<"
						class="btn btn-sm btn-danger">Delete</a> <a
						href="TogglePackageServlet?id="
						class="btn btn-sm btn-warning"> 
					</a></td>
				</tr>
				
			</tbody>
		</table>
	</div>
</div>
</div>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
