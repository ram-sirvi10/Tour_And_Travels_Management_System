<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page
	import="java.util.*, com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
UserResponseDTO admin = (UserResponseDTO) session.getAttribute("user");
if (admin == null || !"ADMIN".equals(admin.getUserRole())) {
	response.sendRedirect("login.jsp");
	return;
}

// Get users list from request
List<UserResponseDTO> usersList = (List<UserResponseDTO>) request.getAttribute("usersList");
String currentList = (String) request.getAttribute("listType"); // e.g., "Deleted Users"
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Users | Admin Dashboard</title>
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
			<h2>Manage Users</h2>


			<div class="mb-3">
				<form method="post" action="<%=request.getContextPath()%>/admin"
					class="row g-2">

					<input type="hidden" name="button"
						value="<%=request.getParameter("button")%>" />
					<%
					if (!"Deleted Users".equalsIgnoreCase(currentList)) {
					%>
					<!-- Active / Inactive filter -->
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
						<button type="submit" class="btn btn-primary w-100">Apply</button>
					</div>

					<%
					}
					%>
					<div class="mb-3">

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





			<table class="table table-bordered table-striped mt-3">
				<thead class="table-primary">
					<tr>
						<th>S.No</th>
						<th>Name</th>
						<th>Email</th>
						<th>Created Date</th>
						<%
						if (!"Deleted Users".equalsIgnoreCase(currentList)) {
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
					if (usersList != null && !usersList.isEmpty()) {
						for (UserResponseDTO user : usersList) {
					%>
					<tr>
						<td><%=serial++%></td>
						<td><%=user.getUserName()%></td>
						<td><%=user.getUserEmail()%></td>

						<td><%=(user.getCreatedAt() != null) ? user.getCreatedAt().toString() : "-"%></td>

						<%
						if (!"Deleted Users".equalsIgnoreCase(currentList)) {
						%>
						<td><a class="action-link"
							href="<%=request.getContextPath()%>/admin?button=userAction&userId=<%=user.getUserId()%>&action=<%=user.isActive() ? "deactivate" : "activate"%>">
								<%=user.isActive() ? "Deactivate" : "Activate"%>
						</a> <a class="action-link text-danger"
							href="<%=request.getContextPath()%>/admin?button=userAction&userId=<%=user.getUserId()%>&action=delete"
							onclick="return confirm('Are you sure you want to permanently delete this user?');">
								Delete </a></td>
						<%
						}
						%>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td
							colspan="<%="Deleted Users".equalsIgnoreCase(currentList) ? 6 : 7%>"
							class="text-center">No users found</td>
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
