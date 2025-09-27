<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>

<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ include file="header.jsp"%>
<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-danger border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body">
				<%=errorMessage%>
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>

<h2 class="mb-4">My Booking History</h2>
<form method="get" action="booking" class="row g-2 mb-3">
	<input type="hidden" name="button" value="bookingHistroy">

	<div class="col-md-2">
		<select name="status" class="form-select">
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
</form>

<div class="row g-4">
	<%
	List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
	if (bookings != null && !bookings.isEmpty()) {
		for (BookingResponseDTO booking : bookings) {
	%>
	<div class="col-md-6">
		<div class="card shadow-sm">
			<div class="row g-0">
				<div class="col-md-4">
					<img src="<%=booking.getPackageImage()%>"
						class="img-fluid rounded-start"
						alt="<%=booking.getPackageName()%>">
				</div>
				<div class="col-md-8">
					<div class="card-body">
						<h5 class="card-title fw-bold"><%=booking.getPackageName()%></h5>
						<p class="mb-1">
							<i class="fas fa-clock"></i>
							<%=booking.getDuration()%>
							Days
						</p>
						<p class="mb-1">
							<i class="fas fa-users"></i>
							<%=booking.getNoOfTravellers()%>
							Travelers
						</p>
						<p class="mb-1">
							<i class="fas fa-dollar-sign"></i> $<%=booking.getAmount()%></p>

						<%
						String statusClass = "bg-danger";
						if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
							statusClass = "bg-success";
						} else if ("PENDING".equalsIgnoreCase(booking.getStatus())) {
							statusClass = "bg-warning text-dark";
						}
						%>
						<span class="badge <%=statusClass%>"><%=booking.getStatus()%></span>

						<a
							href="<%=request.getContextPath()%>/booking?button=viewTravelers&bookingId=<%=booking.getBookingId()%>"
							class="btn btn-sm btn-primary float-end">View Travelers</a>

					</div>
				</div>
			</div>
		</div>
	</div>
	<%
	}
	} else {
	%>
	<p class="text-center">No bookings found.</p>
	<%
	}
	%>
</div>
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

		<li class="page-item <%=currentPage == 1 ? "disabled" : ""%>">
			<a class="page-link"
			href="booking?button=bookingHistroy&page=<%=currentPage - 1%>&<%=queryParams%>">Previous</a>
		</li>

		<%
		for (int i = 1; i <= totalPages; i++) {
		%>
		<li class="page-item <%=currentPage == i ? "active" : ""%>"><a
			class="page-link"
			href="booking?button=bookingHistroy&page=<%=i%>&<%=queryParams%>"><%=i%></a>
		</li>
		<%
		}
		%>

		<li
			class="page-item <%=currentPage == totalPages ? "disabled" : ""%>">
			<a class="page-link"
			href="booking?button=bookingHistroy&page=<%=currentPage + 1%>&<%=queryParams%>">Next</a>
		</li>
	</ul>
</nav>

<%@ include file="footer.jsp"%>
