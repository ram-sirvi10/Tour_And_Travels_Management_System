<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="java.util.*, com.travelmanagement.dto.responseDTO.*"%>

<%
UserResponseDTO admin = (UserResponseDTO) session.getAttribute("user");
if (admin == null || !"ADMIN".equals(admin.getUserRole())) {
	response.sendRedirect("login.jsp");
	return;
}

// Get agencies list from request
List<AgencyResponseDTO> agenciesList = (List<AgencyResponseDTO>) request.getAttribute("agenciesList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Agencies | Admin Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
		rel="stylesheet">
<style>
body {
	font-family: 'Segoe UI', sans-serif;
	background: #f7f9fc;
	margin: 0;
	padding: 0;
}

.navbar {
	background: #0d6efd;
	color: white;
	padding: 15px 20px;
}

.navbar h1 {
	margin: 0;
	font-size: 1.8rem;
}

.dashboard-container {
	display: flex;
	min-height: 100vh;
}

/* Sidebar */
.sidebar {
	background: #0d6efd;
	color: white;
	width: 220px;
	padding: 20px;
	flex-shrink: 0;
}

.sidebar h3 {
	margin-bottom: 20px;
}

.sidebar a {
	display: block;
	color: white;
	text-decoration: none;
	padding: 10px;
	border-radius: 8px;
	margin-bottom: 10px;
	transition: 0.3s;
}

.sidebar a:hover {
	background: #084298;
}

/* Main content */
.main-content {
	flex-grow: 1;
	padding: 30px;
}

.main-content h2 {
	color: #0d6efd;
	margin-bottom: 20px;
}

/* Table */
table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 12px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

a.action-link {
	text-decoration: none;
	margin-right: 10px;
	color: #0d6efd;
}

a.action-link:hover {
	text-decoration: underline;
}

a.text-danger:hover {
	color: red;
}

/* Buttons */
.btn-logout {
	background: #ff3b2e;
	color: white;
	border: none;
	padding: 10px 20px;
	border-radius: 50px;
	cursor: pointer;
	transition: 0.3s;
}

.btn-logout:hover {
	background: #e0291a;
}

/* Responsive */
@media ( max-width : 768px) {
	.dashboard-container {
		flex-direction: column;
	}
	.sidebar {
		width: 100%;
	}
}
</style>
</head>
<body>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<!-- Navbar -->
	<div class="navbar d-flex justify-content-between align-items-center">
		<h1>Admin Dashboard</h1>
		<div>
			<span>Welcome, <%=admin.getUserName()%>!
			</span> <a href="<%=request.getContextPath()%>/auth?button=logout"
				class="btn-logout ms-3">Log out</a>
		</div>
	</div>

	<div class="dashboard-container">
		<!-- Sidebar -->
		<div class="sidebar">
			<h3>Menu</h3>
			<a href="<%=request.getContextPath()%>/admin?button=dashboard">Dashboard
				Overview</a>
			<!-- Dropdown for Manage Users -->
			<div class="dropdown">
				<a href="#" class="dropdown-toggle" data-bs-toggle="collapse"
					data-bs-target="#userMenu" aria-expanded="false"> Manage Users
				</a>
				<div id="userMenu" class="collapse ms-3">
					<a href="<%=request.getContextPath()%>/admin?button=manageUsers">User
						List</a> <a
						href="<%=request.getContextPath()%>/admin?button=deletedUsers">Deleted
						History</a>
				</div>
			</div>

			<!-- Dropdown for Manage Agencies -->
			<div class="dropdown">
				<a href="#" class="dropdown-toggle" data-bs-toggle="collapse"
					data-bs-target="#agencyMenu" aria-expanded="false"> Manage
					Agencies </a>
				<div id="agencyMenu" class="collapse ms-3">
					<a href="<%=request.getContextPath()%>/admin?button=manageAgencies">Agency
						List</a> <a
						href="<%=request.getContextPath()%>/admin?button=pendingAgencies">Pending
						Requests</a> <a
						href="<%=request.getContextPath()%>/admin?button=deletedAgencies">Deleted
						History</a>
				</div>
			</div>
		</div>


		<!-- Main Content -->
		<div class="main-content">


			<%
			String currentList = (String) request.getAttribute("listType");
			%>


			<!-- Search bar -->



			<!-- Show filter bar only if NOT Deleted Agencies -->
			
			<div class="mb-3">
				<form method="post" action="<%=request.getContextPath()%>/admin"
					class="row g-2">
					<input type="hidden" name="button"
						value="<%=request.getParameter("button")%>" />
<%
			if (!"Deleted Agencies".equalsIgnoreCase(currentList)) {
			%>
					<%
					if ("Pending Agencies".equalsIgnoreCase(currentList)) {
					%>
					<!-- Only Status filter -->
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
					<!-- Active, City, Date filters for All Agencies -->
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
						<input type="date" name="startDate" class="form-control"
							value="<%=request.getParameter("startDate") != null ? request.getParameter("startDate") : ""%>" />
					</div>
					<div class="col-md-2">
						<input type="date" name="endDate" class="form-control"
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
					<div class="mb-3">

						<input type="hidden" name="button"
							value="<%=request.getParameter("button")%>" />

						<div class="col-md-4">
							<input type="text" name="keyword" class="form-control"
								placeholder="Search  Agency "
								value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>" />
						</div>
						<div class="col-md-2">
							<button type="submit" class="btn btn-primary w-100">Search</button>
						</div>

					</div>
				</form>
			</div>
		




			<h2>Manage Agencies</h2>
			<table class="table table-bordered table-striped mt-3">
				<thead class="table-primary">

					<tr>
						<th>S.No</th>
						<th>Agency Name</th>
						<th>Owner Name</th>
						<th>Email</th>
						<th>City</th>
						<%
						if (!"Deleted Agencies".equalsIgnoreCase(currentList)) {
						%>
						<th>Actions</th>
						<%
						}
						%>
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
						<td><%=agency.getCity()%></td>

						<td>
							<%
							if (!"Deleted Agencies".equalsIgnoreCase(currentList)) {
								if ("PENDING".equalsIgnoreCase(agency.getStatus())) {
							%> <a class="action-link"
							href="<%=request.getContextPath()%>/admin?button=agencyAction&agencyId=<%=agency.getAgencyId()%>&action=approve">Approve</a>
							<a class="action-link text-danger"
							href="<%=request.getContextPath()%>/admin?button=agencyAction&agencyId=<%=agency.getAgencyId()%>&action=reject">Reject</a>
							<%
							} else {
							%> <a class="action-link"
							href="<%=request.getContextPath()%>/admin?button=agencyAction&agencyId=<%=agency.getAgencyId()%>&action=<%=agency.isActive() ? "deactivate" : "activate"%>">
								<%=agency.isActive() ? "Deactivate" : "Activate"%>
						</a> <a class="action-link text-danger"
							href="<%=request.getContextPath()%>/admin?button=agencyAction&agencyId=<%=agency.getAgencyId()%>&action=delete"
							onclick="return confirm('Are you sure you want to permanently delete this agency?');">
								Delete </a> <%
 }
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
		</div>
	</div>

</body>
</html>
