<%@ include file="header.jsp"%>
<div class="card shadow">
	<div class="card-header bg-success text-white">
		<h4>View Bookings</h4>
	</div>
	<div class="card-body">
		<table class="table table-striped">
			<thead class="table-dark">
				<tr>
					<th>Booking ID</th>
					<th>User</th>
					<th>Package</th>
					<th>No. of Travellers</th>
					<th>Status</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>

				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td><span class="badge 
            bg-success"> </span></td>
					<td><a href="travellersList.jsp?bookingId="
						class="btn btn-sm btn-primary">View Travellers</a></td>
				</tr>

			</tbody>
		</table>
	</div>
</div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
