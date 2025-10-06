<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="header.jsp"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<%@ page
	import="java.util.List,com.travelmanagement.dto.responseDTO.PackageResponseDTO,com.travelmanagement.service.impl.PackageServiceImpl"%>

<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%
int agencyId = ((AgencyResponseDTO) session.getAttribute("agency")).getAgencyId();
List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");

String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
String title = request.getAttribute("title") != null ? (String) request.getAttribute("title") : "";
String location = request.getAttribute("location") != null ? (String) request.getAttribute("location") : "";
String dateFrom = request.getAttribute("dateFrom") != null ? (String) request.getAttribute("dateFrom") : "";
String dateTo = request.getAttribute("dateTo") != null ? (String) request.getAttribute("dateTo") : "";

int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 6;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "viewPackages";

String queryParams = "keyword=" + (keyword != null ? keyword : "") + "&title=" + (title != null ? title : "")
		+ "&location=" + (location != null ? location : "") + "&dateFrom=" + (dateFrom != null ? dateFrom : "")
		+ "&dateTo=" + (dateTo != null ? dateTo : "") + "&pageSize=" + pageSize + "&active="
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
			class="btn btn-secondary w-100">Reset</a></div>
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
		<%
		for (PackageResponseDTO p : packages) {
		%>
		<tr>
			<td><%=p.getPackageId()%></td>
			<td><%=p.getTitle()%></td>
			<td><%=p.getLocation()%></td>
			<td>₹<%=p.getPrice()%></td>
			<td>
				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="action" value="toggleStatus"> <input
						type="hidden" name="packageId" value="<%=p.getPackageId()%>">
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox" name="isActive"
							onchange="this.form.submit();"
							<%=p.getIsActive() ? "checked" : ""%>>
					</div>
				</form>
			</td>
			<td>
				<!-- Details Button -->
				<button type="button" class="btn btn-sm btn-secondary"
					data-bs-toggle="modal"
					data-bs-target="#detailsModal<%=p.getPackageId()%>">
					Details</button> <!-- Edit Button --> <a
				href="agency?action=editPackage&id=<%=p.getPackageId()%>"
				class="btn btn-sm btn-info">Edit</a> <!-- Delete Button --> <a
				href="<%=request.getContextPath()%>/agency?button=viewBookings&packageId=<%=p.getPackageId()%>"
				class="btn btn-sm btn-primary">View Bookings</a>

				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="action" value="deletePackage"> <input
						type="hidden" name="packageId" value="<%=p.getPackageId()%>">
					<button type="submit" class="btn btn-sm btn-danger"
						onclick="return confirm('Are you sure you want to delete this package?');">Delete</button>
				</form>
			</td>
		</tr>

		<!-- Details Modal -->
		<div class="modal fade" id="detailsModal<%=p.getPackageId()%>"
			tabindex="-1"
			aria-labelledby="detailsModalLabel<%=p.getPackageId()%>"
			aria-hidden="true">
			<div class="modal-dialog modal-lg modal-dialog-scrollable">
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title"
							id="detailsModalLabel<%=p.getPackageId()%>">
							Package Details:
							<%=p.getTitle()%></h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<p>
							<strong>Departure Date:</strong>
							<%=p.getDepartureDate()%></p>
						<p>
							<strong>Last Booking Date:</strong>
							<%=p.getLastBookingDate()%></p>
						<p>
							<strong>Total Seats:</strong>
							<%=p.getTotalSeats()%></p>
						<p>
							<strong>Description:</strong>
							<%=p.getDescription()%></p>
						<hr>
						<h6>Day-wise Schedule:</h6>
						<ul>
							<%
							if (p.getPackageSchedule() != null && !p.getPackageSchedule().isEmpty()) {
							%>
							<%
							for (PackageScheduleResponseDTO schedule : p.getPackageSchedule()) {
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
						<a href="agency?action=editPackage&id=<%=p.getPackageId()%>"
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
