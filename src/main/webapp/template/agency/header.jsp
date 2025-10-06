<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Agency Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f6f9;
}

.sidebar {
	height: 100vh;
	position: fixed;
	left: 0;
	top: 0;
	width: 250px;
	background: #212529;
	color: #fff;
	padding-top: 20px;
}

.sidebar a {
	display: block;
	padding: 12px 20px;
	color: #adb5bd;
	text-decoration: none;
	transition: 0.3s;
}

.sidebar a:hover {
	background: #495057;
	color: #fff;
}

.content {
	margin-left: 260px;
	padding: 20px;
}

.navbar {
	margin-left: 250px;
}
</style>
</head>
<body>

	<!-- Sidebar -->
	<div class="sidebar">
		<h3 class="text-center text-white mb-4">Agency Panel</h3>
		<a href="<%=request.getContextPath()%>/agency?button=dashboard">🏠
			Dashboard</a> <a
			href="<%=request.getContextPath()%>/template/agency/addPackage.jsp">➕
			Add Package</a> <a
			href="<%=request.getContextPath()%>/agency?button=viewPackages">📦
			Manage Packages</a> <a
			href="<%=request.getContextPath()%>/booking?button=viewBookings">📑
			View Bookings</a>
	</div>

	<!-- Top Navbar -->
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
		<div class="container-fluid">
			<a class="navbar-brand" href="agencyDashboard.jsp">Travel
				Management</a>
			<div class="d-flex">
				<span class="text-white me-3"> Welcome, <%=((com.travelmanagement.dto.responseDTO.AgencyResponseDTO) session.getAttribute("agency")).getAgencyName()%>
				</span> <a href="<%=request.getContextPath()%>/auth?button=logout"
					class="btn btn-sm btn-outline-light"
					onclick="event.preventDefault(); 
            fetch(this.href, {method:'POST'}).then(()=>{window.location='<%=request.getContextPath()%>/login.jsp'});">
					Logout </a>
			</div>
		</div>
	</nav>

	<!-- Page Content Wrapper -->
	<div class="content">