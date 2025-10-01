<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="java.util.*, com.travelmanagement.dto.responseDTO.*"%>

<%
UserResponseDTO admin = (UserResponseDTO) session.getAttribute("user");
if (admin == null || !"ADMIN".equals(admin.getUserRole())) {
	response.sendRedirect("login.jsp");
	return;
}

List<AgencyResponseDTO> agenciesList = (List<AgencyResponseDTO>) request.getAttribute("agenciesList");

String currentList = request.getAttribute("listType") != null ? (String) request.getAttribute("listType") : "Manage Agencies";
int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (Integer) request.getAttribute("pageSize") : 5;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "manageAgencies";
if (currentList != null) {
	if ("Deleted Agencies".equalsIgnoreCase(currentList)) {
		buttonParam = "deletedAgencies";
	} else if ("Pending Agencies".equalsIgnoreCase(currentList)) {
		buttonParam = "pendingAgencies";
	}
}
String keywordParam = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
String statusParam = "Pending Agencies".equalsIgnoreCase(currentList)
		? "PENDING"
		: (request.getParameter("status") != null ? request.getParameter("status") : "");
String activeParam = request.getParameter("active") != null ? request.getParameter("active") : "";
String startDateParam = request.getParameter("startDate") != null ? request.getParameter("startDate") : "";
String endDateParam = request.getParameter("endDate") != null ? request.getParameter("endDate") : "";
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Agencies | Admin Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
</style>
</head>
<body>

	<div class="dashboard-container">
		<jsp:include page="sidebar.jsp" />
		<div class="main-content">
			<jsp:include page="header.jsp" />
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
						<button type="button"
							class="btn-close btn-close-white me-2 m-auto"
							data-bs-dismiss="toast" aria-label="Close"></button>
					</div>
				</div>
			</div>
			<%
			}
			%>
			<h2>Manage Agencies</h2>

			<!-- Search & Filters -->

			<div class="row g-2 mb-2">
				<h6>Filter Option</h6>
				<form method="post" action="<%=request.getContextPath()%>/admin"
					class="row g-2">
					<input type="hidden" name="button"
						value="<%=request.getParameter("button")%>" />

					<%
					if (!"Deleted Agencies".equalsIgnoreCase(currentList)) {
					%>
					<%
					if ("Pending Agencies".equalsIgnoreCase(currentList) || "REJECTED Agencies".equalsIgnoreCase(currentList)) {
					%>
					<div class="col-md-2">
						<select name="status" class="form-select">
							<option value="PENDING"
								<%="PENDING".equals(request.getParameter("status")) ? "selected" : ""%>>Pending</option>
							<option value="REJECTED"
								<%="REJECTED".equals(request.getParameter("status")) ? "selected" : ""%>>Rejected</option>
						</select>
					</div>
					<%
					} else {
					%>
					<div class="col-md-2">
						<select name="active" class="form-select">
							<option value="">All</option>
							<option value="true"
								<%="true".equals(request.getParameter("active")) ? "selected" : ""%>>Active</option>
							<option value="false"
								<%="false".equals(request.getParameter("active")) ? "selected" : ""%>>Inactive</option>
						</select>
					</div>
					<div class="col-md-2">
						<label for="startDate">Reg. Date From </label> <input type="date"
							id="startDate" name="startDate" class="form-control"
							value="<%=request.getParameter("startDate") != null ? request.getParameter("startDate") : ""%>" />
					</div>
					<div class="col-md-2">
						<label for="endDate">To </label> <input type="date" id="endDate"
							name="endDate" class="form-control"
							value="<%=request.getParameter("endDate") != null ? request.getParameter("endDate") : ""%>" />
					</div>
					<%
					}
					%>
					<div class="col-md-2">
						<button type="submit" class="btn btn-primary w-100">Apply</button>
					</div>
					<%
					}
					%>
				</form>
			</div>


			<div class="row g-2 mb-3">
				<form method="post" action="<%=request.getContextPath()%>/admin"
					class="row g-2">
					<input type="hidden" name="button"
						value="<%=request.getParameter("button")%>" />
					<div class="col-md-4">
						<input type="text" name="keyword" class="form-control"
							placeholder="Search Agency"
							value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>" />
					</div>
					<div class="col-md-2">
						<button type="submit" class="btn btn-primary w-100">Search</button>
					</div>
				</form>
			</div>


			<table class="table table-bordered table-striped mt-3">
				<thead class="table-primary">
					<tr>
						<th>S.No</th>
						<th>Agency Name</th>
						<th>Owner Name</th>
						<th>Email</th>
						<th>Mobile No.</th>
						<th>Address</th>
						<th>Registration No.</th>
						<th>Registration Date</th>
						<th>Actions</th>

					</tr>
				</thead>
				<tbody>
					<%
					int serial = 1;
					if (agenciesList != null && !agenciesList.isEmpty()) {
						for (AgencyResponseDTO agency : agenciesList) {
					%>
					<tr>
						<td><%=serial++%></td>
						<td><%=agency.getAgencyName()%></td>
						<td><%=agency.getOwnerName()%></td>
						<td><%=agency.getEmail()%></td>
						<td><%=agency.getPhone()%></td>
						<td><%=agency.getCity()%>,<%=agency.getState()%>,<%=agency.getCountry()%>,<%=agency.getPincode()%></td>
						<td><%=agency.getRegistrationNumber()%></td>
						<td><%=agency.getCreatedAt()%></td>
						<td>
							<button type="button" class="btn btn-sm btn-info details-btn"
								data-agencyname="<%=agency.getAgencyName()%>"
								data-owner="<%=agency.getOwnerName()%>"
								data-email="<%=agency.getEmail()%>"
								data-phone="<%=agency.getPhone()%>"
								data-city="<%=agency.getCity()%>"
								data-state="<%=agency.getState()%>"
								data-country="<%=agency.getCountry()%>"
								data-pincode="<%=agency.getPincode()%>"
								data-regno="<%=agency.getRegistrationNumber()%>"
								data-created="<%=agency.getCreatedAt()%>">Details</button> <%
 if (!"Deleted Agencies".equalsIgnoreCase(currentList) && !"REJECTED Agencies".equalsIgnoreCase(currentList)) {
 %> <%
 if ("PENDING".equalsIgnoreCase(agency.getStatus())) {
 %>
							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="agencyAction">
								<input type="hidden" name="agencyId"
									value="<%=agency.getAgencyId()%>"> <input type="hidden"
									name="keyword" value="<%=keywordParam%>"> <input
									type="hidden" name="status" value="<%=statusParam%>"> <input
									type="hidden" name="pageSize" value="<%=pageSize%>">

								<button type="submit" name="action" value="approve"
									class="btn btn-sm btn-success"
									onclick="return confirm('Are you sure you want to approve this agency?');">Approve</button>
							</form>
							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="agencyAction">
								<input type="hidden" name="agencyId"
									value="<%=agency.getAgencyId()%>"> <input type="hidden"
									name="keyword" value="<%=keywordParam%>"> <input
									type="hidden" name="status" value="<%=statusParam%>"> <input
									type="hidden" name="pageSize" value="<%=pageSize%>">
								<button type="submit" name="action" value="reject"
									class="btn btn-sm btn-danger"
									onclick="return confirm('Are you sure you want to reject this agency?');">Reject</button>
							</form> <%
 } else {
 %>
							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="agencyAction">
								<input type="hidden" name="agencyId"
									value="<%=agency.getAgencyId()%>"> <input type="hidden"
									name="keyword" value="<%=keywordParam%>"> <input
									type="hidden" name="active" value="<%=activeParam%>"> <input
									type="hidden" name="startDate" value="<%=startDateParam%>">
								<input type="hidden" name="endDate" value="<%=endDateParam%>">
								<input type="hidden" name="pageSize" value="<%=pageSize%>">
								<button type="submit" name="action"
									value="<%=agency.isActive() ? "deactivate" : "activate"%>"
									class="btn btn-sm btn-primary">
									<%=agency.isActive() ? "Deactivate" : "Activate"%>
								</button>
							</form>
							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="agencyAction">
								<input type="hidden" name="agencyId"
									value="<%=agency.getAgencyId()%>"> <input type="hidden"
									name="keyword" value="<%=keywordParam%>"> <input
									type="hidden" name="active" value="<%=activeParam%>"> <input
									type="hidden" name="startDate" value="<%=startDateParam%>">
								<input type="hidden" name="endDate" value="<%=endDateParam%>">
								<input type="hidden" name="pageSize" value="<%=pageSize%>">
								<button type="submit" name="action" value="delete"
									class="btn btn-sm btn-danger"
									onclick="return confirm('Are you sure you want to permanently delete this agency?');">
									Delete</button>
							</form> <%
 }
 %> <%
 }
 %>
						</td>

					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="8" class="text-center">No agencies found</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>

			<!-- Page Size Selector -->
			<div class="row mb-3">
				<div class="col-md-2">
					<form method="get" action="<%=request.getContextPath()%>/admin">
						<input type="hidden" name="button" value="<%=buttonParam%>">
						<input type="hidden" name="keyword" value="<%=keywordParam%>">
						<input type="hidden" name="status" value="<%=statusParam%>">
						<input type="hidden" name="active" value="<%=activeParam%>">
						<input type="hidden" name="startDate" value="<%=startDateParam%>">
						<input type="hidden" name="endDate" value="<%=endDateParam%>">
						<label for="pageSize" class="form-label">Records per page:</label>
						<select name="pageSize" id="pageSize" class="form-select"
							onchange="this.form.submit()">
							<option value="5" <%=(pageSize == 5) ? "selected" : ""%>>5</option>
							<option value="10" <%=(pageSize == 10) ? "selected" : ""%>>10</option>
							<option value="15" <%=(pageSize == 15) ? "selected" : ""%>>15</option>
							<option value="20" <%=(pageSize == 20) ? "selected" : ""%>>20</option>
						</select>
					</form>
				</div>
			</div>

			<!-- Pagination -->
			<nav>
				<ul class="pagination justify-content-center">
					<%
					if (currentPage > 1) {
					%>
					<li class="page-item"><a class="page-link"
						href="?button=<%=buttonParam%>&keyword=<%=keywordParam%>&status=<%=statusParam%>&active=<%=activeParam%>&startDate=<%=startDateParam%>&endDate=<%=endDateParam%>&pageSize=<%=pageSize%>&page=<%=currentPage - 1%>">Prev</a>
					</li>
					<%
					}
					%>

					<%
					for (int i = startPage; i <= endPage; i++) {
					%>
					<li class="page-item <%=(i == currentPage) ? "active" : ""%>">
						<a class="page-link"
						href="?button=<%=buttonParam%>&keyword=<%=keywordParam%>&status=<%=statusParam%>&active=<%=activeParam%>&startDate=<%=startDateParam%>&endDate=<%=endDateParam%>&pageSize=<%=pageSize%>&page=<%=i%>"><%=i%></a>
					</li>
					<%
					}
					%>

					<%
					if (currentPage < totalPages) {
					%>
					<li class="page-item"><a class="page-link"
						href="?button=<%=buttonParam%>&keyword=<%=keywordParam%>&status=<%=statusParam%>&active=<%=activeParam%>&startDate=<%=startDateParam%>&endDate=<%=endDateParam%>&pageSize=<%=pageSize%>&page=<%=currentPage + 1%>">Next</a>
					</li>
					<%
					}
					%>
				</ul>
			</nav>



			<!-- Details Modal -->
			<div class="modal fade" id="detailsModal" tabindex="-1"
				aria-hidden="true">
				<div class="modal-dialog modal-dialog-centered modal-lg">
					<div class="modal-content">
						<div class="modal-header bg-primary text-white">
							<h5 class="modal-title">Agency Details</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body">
							<table class="table table-bordered">
								<tr>
									<th>Agency Name</th>
									<td id="m_agencyname"></td>
								</tr>
								<tr>
									<th>Owner Name</th>
									<td id="m_owner"></td>
								</tr>
								<tr>
									<th>Email</th>
									<td id="m_email"></td>
								</tr>
								<tr>
									<th>Phone</th>
									<td id="m_phone"></td>
								</tr>
								<tr>
									<th>City</th>
									<td id="m_city"></td>
								</tr>
								<tr>
									<th>State</th>
									<td id="m_state"></td>
								</tr>
								<tr>
									<th>Country</th>
									<td id="m_country"></td>
								</tr>
								<tr>
									<th>Pin code</th>
									<td id="m_pincode"></td>
								</tr>
								<tr>
									<th>Registration No</th>
									<td id="m_regno"></td>
								</tr>
								<tr>
									<th>Registration Date</th>
									<td id="m_created"></td>
								</tr>

							</table>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script>
document.querySelectorAll('.details-btn').forEach(btn=>{
    btn.addEventListener('click', ()=>{
        const fields = ['agencyname','owner','email','phone','city','state','country','pincode','regno','created'];
        fields.forEach(f=>{
            document.getElementById('m_'+f).innerText = btn.dataset[f];
        });
        new bootstrap.Modal(document.getElementById('detailsModal')).show();
    });
});
</script>
</body>
</html>
