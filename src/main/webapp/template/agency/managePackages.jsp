<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="java.time.LocalDateTime, java.time.ZoneId, java.time.ZonedDateTime, java.time.format.DateTimeFormatter"%>

<%
ZoneId istZone = ZoneId.of("Asia/Kolkata");
DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"); // Example: 08-Oct-2025 03:30 PM
%>
<%@ include file="header.jsp"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<%@ page
	import="java.util.List,com.travelmanagement.dto.responseDTO.PackageResponseDTO,com.travelmanagement.service.impl.PackageServiceImpl"%>

<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>



<%
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) {
	errorMessage = (String) request.getAttribute("errorMessage");
}
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-danger border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body"><%=errorMessage%></div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>
<%
String successMessage = (String) session.getAttribute("successMessage");
session.removeAttribute("successMessage");
if (successMessage == null) {
	successMessage = (String) request.getAttribute("successMessage");
}
%>


<%
if (successMessage != null && !successMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-success border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body">
				<%=successMessage%>
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>


<%
int agencyId = ((AgencyResponseDTO) session.getAttribute("agency")).getAgencyId();
List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");

String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
String title = request.getAttribute("title") != null ? (String) request.getAttribute("title") : "";

int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 6;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "viewPackages";

String queryParams = "keyword=" + (keyword != null ? keyword : "") + "&title=" + (title != null ? title : "")
		+ "&pageSize=" + pageSize + "&active="
		+ (request.getParameter("active") != null ? request.getParameter("active") : "");
%>

<h2 class="mb-3">📦 Manage Packages</h2>


<%
if (request.getAttribute("message") != null) {
%>
<div class="alert alert-success"><%=request.getAttribute("message")%></div>
<%
}
%>
<%
if (request.getAttribute("error") != null) {
%>
<div class="alert alert-danger"><%=request.getAttribute("error")%></div>
<%
}
%>

<div class="mb-3">
	<form method="get" action="<%=request.getContextPath()%>/agency"
		class="row g-2 mb-4">
		<input type="hidden" name="button" value="<%=buttonParam%>">

		<div class="col-md-2">
			<label for="keyword" class="form-label">Status</label> <select
				name="active" class="form-select">
				<option value="">All</option>
				<option value="true"
					<%="true".equals(request.getParameter("active")) ? "selected" : ""%>>Active</option>
				<option value="false"
					<%="false".equals(request.getParameter("active")) ? "selected" : ""%>>Inactive</option>
			</select>
		</div>


		<div class="col-md-2">
			<label for="keyword" class="form-label">Search</label> <input
				type="text" name="keyword" id="keyword" class="form-control"
				placeholder="Keyword..." value="<%=keyword%>">
		</div>

		<div class="col-md-2 d-flex align-items-end">
			<button type="submit" class="btn btn-primary w-100">Apply</button>

		</div>
		<div class="col-md-2  d-flex align-items-end">
			<a href="agency?button=<%=buttonParam%>"
				class="btn btn-secondary w-100">Reset</a>
		</div>
		<div class="col-md-2 d-flex align-items-end">
			<label for="pageSize">Records per page: </label> <select
				name="pageSize" id="pageSize" onchange="this.form.submit()">
				<option value="10" <%=pageSize == 10 ? "selected" : ""%>>10</option>
				<option value="20" <%=pageSize == 20 ? "selected" : ""%>>20</option>
				<option value="30" <%=pageSize == 30 ? "selected" : ""%>>30</option>
				<option value="40" <%=pageSize == 40 ? "selected" : ""%>>40</option>
			</select>
		</div>
	</form>
</div>


<table class="table table-hover shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Title</th>
			<th>Location</th>
			<th>Price</th>
			<th>Status</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody>
		<%String departureDateIST = "";
		String lastBookingDateIST = "";
		for (PackageResponseDTO pkg : packages) {
			

			if (pkg.getDepartureDate() != null) {
				departureDateIST = pkg.getDepartureDate().atZone(ZoneId.systemDefault()).withZoneSameInstant(istZone)
				.format(displayFormatter);
			}

			if (pkg.getLastBookingDate() != null) {
				lastBookingDateIST = pkg.getLastBookingDate().atZone(ZoneId.systemDefault()).withZoneSameInstant(istZone)
				.format(displayFormatter);
			}
		%>
		<tr>
			<td><%=pkg.getPackageId()%></td>
			<td><%=pkg.getTitle()%></td>
			<td><%=pkg.getLocation()%></td>
			<td>₹<%=pkg.getPrice()%></td>

			<td>
				<%-- ✅ Toggle Active/Inactive --%>
				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="button" value="packageAction"> <input
						type="hidden" name="actionType" value="toggle"> <input
						type="hidden" name="packageId" value="<%=pkg.getPackageId()%>">

					<%-- Preserve filters + pagination --%>
					<input type="hidden" name="keyword"
						value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
					<%-- <input type="hidden" name="location"
						value="<%=request.getParameter("location") != null ? request.getParameter("location") : ""%>">
					<input type="hidden" name="dateFrom"
						value="<%=request.getParameter("dateFrom") != null ? request.getParameter("dateFrom") : ""%>">
					<input type="hidden" name="dateTo"
						value="<%=request.getParameter("dateTo") != null ? request.getParameter("dateTo") : ""%>"> --%>
					<input type="hidden" name="page"
						value="<%=request.getParameter("page") != null ? request.getParameter("page") : "1"%>">
					<input type="hidden" name="pageSize"
						value="<%=request.getParameter("pageSize") != null ? request.getParameter("pageSize") : "10"%>">
					<input type="hidden" name="active"
						value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">

					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switch<%=pkg.getPackageId()%>"
							<%=pkg.getIsActive() ? "checked" : ""%>
							onchange="if(confirm('Are you sure you want to <%=pkg.getIsActive() ? "deactivate" : "activate"%> this package?')) { this.form.submit(); } else { this.checked = !this.checked; }">
						<label
							class="form-check-label fw-bold <%=pkg.getIsActive() ? "text-success" : "text-danger"%>"
							for="switch<%=pkg.getPackageId()%>"> <%=pkg.getIsActive() ? "Active" : "Inactive"%>
						</label>
					</div>
				</form>



			</td>
			<td>
				<!-- Details Button -->
				<button type="button" class="btn btn-sm btn-secondary"
					data-bs-toggle="modal"
					data-bs-target="#detailsModal<%=pkg.getPackageId()%>">
					Details</button> <!-- Edit Button --> <a
				href="agency?button=editPackageForm&id=<%=pkg.getPackageId()%>"
				class="btn btn-sm btn-info">Edit</a> <!-- Delete Button --> <a
				href="<%=request.getContextPath()%>/agency?button=viewBookings&packageId=<%=pkg.getPackageId()%>"
				class="btn btn-sm btn-primary">View Bookings</a>

				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="button" value="packageAction"> <input
						type="hidden" name="actionType" value="delete"> <input
						type="hidden" name="packageId" value="<%=pkg.getPackageId()%>">

					<!-- Preserve filters -->
					<input type="hidden" name="keyword"
						value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
					<%-- <input type="hidden" name="location"
						value="<%=request.getParameter("location") != null ? request.getParameter("location") : ""%>">
					<input type="hidden" name="dateFrom"
						value="<%=request.getParameter("dateFrom") != null ? request.getParameter("dateFrom") : ""%>">
					<input type="hidden" name="dateTo"
						value="<%=request.getParameter("dateTo") != null ? request.getParameter("dateTo") : ""%>"> --%>
					<input type="hidden" name="page"
						value="<%=request.getParameter("page") != null ? request.getParameter("page") : "1"%>">
					<input type="hidden" name="pageSize"
						value="<%=request.getParameter("pageSize") != null ? request.getParameter("pageSize") : "10"%>">
					<input type="hidden" name="active"
						value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">

					<button type="submit" class="btn btn-sm btn-danger"
						onclick="return confirm('Are you sure you want to delete this package?');">
						Delete</button>
				</form>

			</td>
		</tr>

		<!-- Details Modal -->
		<div class="modal fade" id="detailsModal<%=pkg.getPackageId()%>"
			tabindex="-1"
			aria-labelledby="detailsModalLabel<%=pkg.getPackageId()%>"
			aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-scrollable">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"
							id="detailsModalLabel<%=pkg.getPackageId()%>">
							Package Details:
							<%=pkg.getTitle()%></h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body ">
						<%-- Package Image --%>
						<%
						String imageUrl = pkg.getImageurl();
						if (imageUrl != null && !imageUrl.isEmpty()) {
						%>
						<img src="<%=imageUrl%>" alt="Package Image"
							style="max-width: 100%; max-height: 300px; border-radius: 8px; margin-bottom: 15px;">
						<%
						}
						%>
						<p>
							<strong>Departure Date:</strong>
							<%=departureDateIST%></p>
						<p>
							<strong>Last Booking Date:</strong>
							<%=lastBookingDateIST%></p>
						<p>
							<strong>Total Seats:</strong>
							<%=pkg.getTotalSeats()%></p>
						<p>
							<strong>Description:</strong>
							<%=pkg.getDescription()%></p>
						<hr>
						<h6>Day-wise Schedule:</h6>
						<ul>
							<%
							if (pkg.getPackageSchedule() != null && !pkg.getPackageSchedule().isEmpty()) {
							%>
							<%
							for (PackageScheduleResponseDTO schedule : pkg.getPackageSchedule()) {
							%>
							<li><strong>Day <%=schedule.getDayNumber()%>:
							</strong> <%=schedule.getActivity()%> - <%=schedule.getDescription()%></li>
							<%
							}
							%>
							<%
							} else {
							%>
							<li>No schedule available.</li>
							<%
							}
							%>
						</ul>

					</div>
					<div class="modal-footer">
						<a href="agency?action=editPackageForm&id=<%=pkg.getPackageId()%>"
							class="btn btn-primary">Edit Package</a>
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

		<%
		}
		%>
	</tbody>

</table>

<%-- Pagination Section --%>
<nav>
	<ul class="pagination justify-content-center">
		<%
		if (currentPage > 1) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage - 1%>&button=<%=buttonParam%>">Prev</a></li>
		<%
		}
		for (int i = startPage; i <= endPage; i++) {
		%>
		<li class="page-item <%=(i == currentPage) ? "active" : ""%>"><a
			class="page-link"
			href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
		</li>
		<%
		}
		if (currentPage < totalPages) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage + 1%>&button=<%=buttonParam%>">Next</a></li>
		<%
		}
		%>
	</ul>
</nav>

<%@ include file="footer.jsp"%>
