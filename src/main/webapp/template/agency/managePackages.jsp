<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.*, java.time.format.DateTimeFormatter"%>
<%@ page import="java.util.List"%>
<%@ page import="com.travelmanagement.dto.responseDTO.*"%>
<%@ include file="header.jsp"%>

<%
ZoneId istZone = ZoneId.of("Asia/Kolkata");
DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
LocalDateTime nowIST = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null)
	errorMessage = (String) request.getAttribute("errorMessage");

String successMessage = (String) session.getAttribute("successMessage");
session.removeAttribute("successMessage");
if (successMessage == null)
	successMessage = (String) request.getAttribute("successMessage");

int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 6;

String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
String title = request.getAttribute("title") != null ? (String) request.getAttribute("title") : "";

String buttonParam = "viewPackages";
String queryParams = "keyword=" + keyword + "&title=" + title + "&pageSize=" + pageSize + "&active="
		+ (request.getParameter("active") != null ? request.getParameter("active") : "");

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
%>

<h2 class="mb-3">📦 Manage Packages</h2>

<!-- Toast Messages -->
<%
if (errorMessage != null) {
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
if (successMessage != null) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-success border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body"><%=successMessage%></div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>

<!-- Filter Form -->
<div class="mb-3">
	<form method="get" action="<%=request.getContextPath()%>/agency"
		class="row g-2 mb-4">
		<input type="hidden" name="button" value="<%=buttonParam%>">
		<div class="col-md-2">
			<label class="form-label">Status</label> <select name="active"
				class="form-select" onchange="this.form.submit()">
				<option value="">All</option>
				<option value="true"
					<%="true".equals(request.getParameter("active")) ? "selected" : ""%>>Active</option>
				<option value="false"
					<%="false".equals(request.getParameter("active")) ? "selected" : ""%>>Inactive</option>
			</select>
		</div>
		<div class="col-md-2">
			<label class="form-label">Search</label> <input type="text"
				name="keyword" class="form-control" placeholder="Keyword..."
				value="<%=keyword%>">
		</div>
		<div class="col-md-2 d-flex align-items-end">
			<button type="submit" class="btn btn-primary w-100">Apply</button>
		</div>
		<div class="col-md-2 d-flex align-items-end">
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

<!-- Packages Table -->
<table class="table table-hover shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>#</th>
			<th>Title</th>
			<th>Location</th>
			<th>Price</th>
			<th>Status</th>
			<th>Info</th>
			<th>Actions</th>
		</tr>
	</thead>
	<tbody>
		<%
		if (packages != null && !packages.isEmpty()) {
			int srNo = 0;
			for (PackageResponseDTO pkg : packages) {
				
				srNo++;
				boolean isDeparted = false;
				LocalDateTime departureEnd = null;
				if (pkg.getDepartureDate() != null) {
			departureEnd = pkg.getDepartureDate();
			if (nowIST.isAfter(departureEnd) || nowIST.isEqual(departureEnd)) {
				isDeparted = true;
			}
				}
				String departureStr = pkg.getDepartureDate() != null ? pkg.getDepartureDate().atZone(ZoneId.systemDefault())
				.withZoneSameInstant(istZone).format(displayFormatter) : "-";
				String lastBookingStr = pkg.getLastBookingDate() != null ? pkg.getLastBookingDate()
				.atZone(ZoneId.systemDefault()).withZoneSameInstant(istZone).format(displayFormatter) : "-";
		%>

		<tr>

			<td><%=srNo%></td>
			<td><%=pkg.getTitle()%></td>
			<td><%=pkg.getLocation()%></td>
			<td>₹<%=pkg.getPrice()%></td>
			<td>
				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="button" value="packageAction"> <input
						type="hidden" name="actionType" value="toggle"> <input
						type="hidden" name="packageId" value="<%=pkg.getPackageId()%>">
					<input type="hidden" name="keyword" value="<%=keyword%>"> <input
						type="hidden" name="page" value="<%=currentPage%>"> <input
						type="hidden" name="pageSize" value="<%=pageSize%>"> <input
						type="hidden" name="active"
						value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">

					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switch<%=pkg.getPackageId()%>"
							<%=pkg.getIsActive() ? "checked" : ""%>
							onchange="<%if (isDeparted) {%> alert('Package already departed. Cannot change status.'); this.checked = !this.checked;<%} else {%> if(confirm('Are you sure to <%=pkg.getIsActive() ? "deactivate" : "activate"%> this package?')){ this.form.submit(); } else { this.checked = !this.checked; } <%}%>">
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
					data-bs-target="#detailsModal<%=pkg.getPackageId()%>">Details</button>
	
			</td>
			<td>
				<!-- Edit Button --> <a
				href="agency?button=editPackageForm&id=<%=pkg.getPackageId()%>"
				class="btn btn-sm btn-info <%=isDeparted ? "disabled" : ""%>">Edit</a>
				<!-- View Bookings --> <!-- Delete Button -->
				<form action="agency" method="post" style="display: inline;">
					<input type="hidden" name="button" value="packageAction"> <input
						type="hidden" name="actionType" value="delete"> <input
						type="hidden" name="packageId" value="<%=pkg.getPackageId()%>">
					<input type="hidden" name="keyword" value="<%=keyword%>"> <input
						type="hidden" name="page" value="<%=currentPage%>"> <input
						type="hidden" name="pageSize" value="<%=pageSize%>"> <input
						type="hidden" name="active"
						value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">

					<button type="submit" class="btn btn-sm btn-danger"
						onclick="return confirm('Are you sure you want to delete this package?');"
						<%=isDeparted ? "disabled" : ""%>>Delete</button>
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
							<%=pkg.getTitle()%>
							<%
							if (isDeparted) {
							%>(Already Departed)<%
							}
							%>
						</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal"
							aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<%
						if (pkg.getImageurl() != null) {
						%>
						<img src="<%=pkg.getImageurl()%>" alt="Package Image"
							class="img-fluid mb-3"
							style="max-height: 300px; border-radius: 8px;">
						<%
						}
						%>
						<p>
							<strong>Departure Date:</strong>
							<%=departureStr%></p>
						<p>
							<strong>Last Booking Date:</strong>
							<%=lastBookingStr%></p>
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
								for (PackageScheduleResponseDTO s : pkg.getPackageSchedule()) {
							%>
							<li><strong>Day <%=s.getDayNumber()%>:
							</strong> <%=s.getActivity()%> - <%=s.getDescription()%></li>
							<%
							}
							} else {
							%>
							<li>No schedule available.</li>
							<%
							}
							%>
						</ul>
					</div>
					<div class="modal-footer">
						<a href="agency?button=editPackageForm&id=<%=pkg.getPackageId()%>"
							class="btn btn-primary <%=isDeparted ? "disabled" : ""%>">Edit
							Package</a>
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>

		<%
		}
		} else {
		%>
		<tr>
			<td colspan="7" class="text-center text-muted">No packages
				available.</td>
		</tr>
		<%
		}
		%>
	</tbody>
</table>

<!-- Pagination -->
<nav>
	<ul class="pagination justify-content-center">
		<%
		if (currentPage > 1) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage - 1%>&button=<%=buttonParam%>">Prev</a></li>
		<%
		}
		%>
		<%
		int windowSize = 3;
		int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
		int endPage = Math.min(startPage + windowSize - 1, totalPages);
		for (int i = startPage; i <= endPage; i++) {
		%>
		<li class="page-item <%=i == currentPage ? "active" : ""%>"><a
			class="page-link"
			href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
		</li>
		<%
		}
		%>
		<%
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
