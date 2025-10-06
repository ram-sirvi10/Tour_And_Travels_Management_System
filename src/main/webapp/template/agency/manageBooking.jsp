<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="header.jsp"%>
<%@ page
	import="java.util.List, com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<h2 class="mb-3">📑 View Bookings</h2>


<form method="get" action="booking" class="row g-2 mb-3">
	<input type="hidden" name="button" value="viewBookings">

	<div class="col-md-2">
		<label for="keyword" class="form-label">Status</label> <select
			name="status" class="form-select">
			<option value="">All Status</option>
			<option value="PENDING"
				<%="PENDING".equals(request.getParameter("status")) ? "selected" : ""%>>Pending</option>
			<option value="CONFIRMED"
				<%="CONFIRMED".equals(request.getParameter("status")) ? "selected" : ""%>>Confirmed</option>
			<option value="CANCELLED"
				<%="CANCELLED".equals(request.getParameter("status")) ? "selected" : ""%>>Cancelled</option>
		</select>
	</div>

	<div class="col-md-2">
		<label for="startDate" class="form-label"> From </label> <input
			type="date" name="startDate" class="form-control"
			placeholder="Start Date"
			value="<%=request.getParameter("startDate") != null ? request.getParameter("startDate") : ""%>"
			id="startDate"
			onchange="document.getElementById('endDate').min = this.value;">
	</div>

	<div class="col-md-2">
		<label for="endDate" class="form-label"> To </label> <input
			type="date" name="endDate" class="form-control"
			placeholder="End Date"
			value="<%=request.getParameter("endDate") != null ? request.getParameter("endDate") : ""%>"
			id="endDate">
	</div>

	<div class="col-md-2">
		<label for="pageSize">Records per page:</label> <select
			name="pageSize" id="pageSize" onchange="this.form.submit()">
			<option value="10"
				<%="10".equals(request.getParameter("pageSize")) ? "selected" : ""%>>10</option>
			<option value="20"
				<%="20".equals(request.getParameter("pageSize")) ? "selected" : ""%>>20</option>
			<option value="50"
				<%="50".equals(request.getParameter("pageSize")) ? "selected" : ""%>>50</option>
		</select>
	</div>

	<div class="col-md-1">
		<button type="submit" class="btn btn-primary w-100">Filter</button>
	</div>
	<div class="col-md-1">

    <a href="booking?button=viewBookings" class="btn btn-secondary w-100">Reset</a>
</div>
</form>

<table class="table table-hover shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>User</th>
			<th>Package</th>
			<th>Status</th>
			<th>No. of Travellers</th>
			<th>Action</th>
		</tr>
	</thead>
	<tbody>
		<%
		List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
		if (bookings != null && !bookings.isEmpty()) {
			for (BookingResponseDTO booking : bookings) {
		%>
		<tr>
			<td><%=booking.getBookingId()%></td>
			<td>User #<%=booking.getUserId()%></td>
			<td>Package #<%=booking.getPackageId()%></td>
			<td><span
				class="badge 
                    <%="CONFIRMED".equals(booking.getStatus()) ? "bg-success"
		: "PENDING".equals(booking.getStatus()) ? "bg-warning"
				: "CANCELLED".equals(booking.getStatus()) ? "bg-danger" : "bg-secondary"%>">
					<%=booking.getStatus()%>
			</span></td>
			<td><%=booking.getNoOfTravellers()%></td>
			<td><a
				href="<%=request.getContextPath()%>/booking?button=viewTravelers&bookingId=<%=booking.getBookingId()%>"
				class="btn btn-sm btn-info">View Travelers</a></td>
		</tr>
		<%
		}
		} else {
		%>
		<tr>
			<td colspan="6" class="text-center text-muted">No bookings
				found.</td>
		</tr>
		<%
		}
		%>
	</tbody>
</table>
<!-- Pagination -->
<nav>
	<ul class="pagination justify-content-center mt-3">
		<%
		Integer currentPageAttr = (Integer) request.getAttribute("currentPage");
		int currentPage = (currentPageAttr != null) ? currentPageAttr : 1;

		Integer totalPagesAttr = (Integer) request.getAttribute("totalPages");
		int totalPages = (totalPagesAttr != null) ? totalPagesAttr : 1;

		String statusParam = (request.getParameter("status") != null) ? request.getParameter("status") : "";
		String startDateParam = (request.getParameter("startDate") != null) ? request.getParameter("startDate") : "";
		String endDateParam = (request.getParameter("endDate") != null) ? request.getParameter("endDate") : "";
		String pageSizeParam = (request.getParameter("pageSize") != null) ? request.getParameter("pageSize") : "10";

		String queryParams = "status=" + statusParam + "&startDate=" + startDateParam + "&endDate=" + endDateParam
				+ "&pageSize=" + pageSizeParam;
		%>

		<li class="page-item <%=currentPage == 1 ? "disabled" : ""%>"><a
			class="page-link"
			href="booking?button=viewBookings&page=<%=currentPage - 1%>&<%=queryParams%>">Previous</a>
		</li>

		<%
		for (int i = 1; i <= totalPages; i++) {
		%>
		<li class="page-item <%=currentPage == i ? "active" : ""%>"><a
			class="page-link"
			href="booking?button=viewBookings&page=<%=i%>&<%=queryParams%>"><%=i%></a>
		</li>
		<%
		}
		%>

		<li class="page-item <%=currentPage == totalPages ? "disabled" : ""%>">
			<a class="page-link"
			href="booking?button=viewBookings&page=<%=currentPage + 1%>&<%=queryParams%>">Next</a>
		</li>
	</ul>
</nav>
<%@ include file="footer.jsp"%>
