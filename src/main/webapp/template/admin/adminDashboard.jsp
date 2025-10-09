<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%
UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
if (user == null || !"ADMIN".equals(user.getUserRole())) {
	response.sendRedirect("login.jsp");
	return;
}

Long totalUsers = (Long) request.getAttribute("totalUsers");
Long totalAgencies = (Long) request.getAttribute("totalAgencies");
Long pendingAgencies = (Long) request.getAttribute("pendingAgencies");
Long rejectedAgencies = (Long) request.getAttribute("rejectedAgencies");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard | TravelMate</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>
<body>
	<!-- Include Font Awesome for icons -->
	<link rel="stylesheet"
		href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />

	<style>
.dashboard-container {
	display: flex;
}

.main-content {
	flex: 1;
	padding: 20px;
	background-color: #f1f5f9; /* light background */
	min-height: 100vh;
}

.card {
	border-radius: 15px;
	padding: 25px 20px;
	color: #fff;
	text-align: center;
	position: relative;
	overflow: hidden;
	box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
	transition: transform 0.3s, box-shadow 0.3s;
}

.card h4 {
	font-size: 1.3rem;
	font-weight: 600;
	margin-bottom: 15px;
}

.card p {
	font-size: 2rem;
	font-weight: 700;
}

.card i {
	position: absolute;
	font-size: 3.5rem;
	opacity: 0.2;
	top: 15px;
	right: 20px;
}

.card:hover {
	transform: translateY(-5px);
	box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2);
}

/* Gradient colors for different cards */
.card.users {
	background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.card.agencies {
	background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.card.pending {
	background: linear-gradient(135deg, #f7971e, #ffd200);
}

.card.rejected {
	background: linear-gradient(135deg, #f5576c, #f093fb);
}

a.card-link {
	text-decoration: none;
}
</style>

	<div class="dashboard-container">
		<jsp:include page="sidebar.jsp" />
		<div class="main-content">
			<jsp:include page="header.jsp" />
			
			<h2 class="mb-4">Dashboard Overview</h2>
			<div class="row g-4">
				<div class="col-md-6 col-sm-12">
					<a href="<%=request.getContextPath()%>/admin?button=manageUsers"
						class="card-link">
						<div class="card users">
							<h4>Total Users</h4>
							<p><%=totalUsers != null ? totalUsers : 0%></p>
							<i class="fas fa-users"></i>
						</div>
					</a>
				</div>

				<div class="col-md-6 col-sm-12">
					<a href="<%=request.getContextPath()%>/admin?button=manageAgencies"
						class="card-link">
						<div class="card agencies">
							<h4>Total Agencies</h4>
							<p><%=totalAgencies != null ? totalAgencies : 0%></p>
							<i class="fas fa-building"></i>
						</div>
					</a>
				</div>

				<div class="col-md-6 col-sm-12">
					<a
						href="<%=request.getContextPath()%>/admin?button=pendingAgencies"
						class="card-link">
						<div class="card pending">
							<h4>Pending Agencies</h4>
							<p><%=pendingAgencies != null ? pendingAgencies : 0%></p>
							<i class="fas fa-hourglass-half"></i>
						</div>
					</a>
				</div>

				<div class="col-md-6 col-sm-12">
					<a
						href="<%=request.getContextPath()%>/admin?button=deletedAgencies"
						class="card-link">
						<div class="card rejected">
							<h4>Rejected Agencies</h4>
							<p><%=rejectedAgencies != null ? rejectedAgencies : 0%></p>
							<i class="fas fa-ban"></i>
						</div>
					</a>
				</div>
			</div>

		</div>
	</div>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
