<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<%
AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");
if (agency == null) {
	response.sendRedirect("login.jsp");
	return;
}
%>
<title>Agency Dashboard</title>
<!-- Add this in <head> -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
	rel="stylesheet">

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

.profile-img {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	object-fit: cover;
	border: 2px solid #fff;
	cursor: pointer;
	transition: transform 0.2s;
}

.profile-img:hover {
	transform: scale(1.1);
}

.profile-icon {
	font-size: 1.5rem;
	color: white;
	cursor: pointer;
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
			View Bookings</a><a
			href="<%=request.getContextPath()%>/agency?button=viewReports">📊
			View Reports</a>

	</div>

	<!-- Top Navbar -->

	<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
		<div class="container-fluid">
			<a class="navbar-brand" href="agencyDashboard.jsp">Travel
				Management</a>
			<div class="d-flex align-items-center gap-3 ms-auto">
				<span class="text-white">Welcome, <%=agency.getAgencyName()%></span>
				<a href="<%=request.getContextPath()%>/agency?button=viewProfile">
					<%
					if (agency.getImageurl() != null && !agency.getImageurl().isEmpty()) {
					%> <img src="<%=agency.getImageurl()%>" alt="Profile"
					class="profile-img"> <%
 } else {
 %> <i class="bi bi-person-circle profile-icon"></i> <%
 }
 %>
				</a>
			</div>
		</div>
	</nav>

	<!-- Make sure to include Bootstrap JS at the bottom of the page -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>


	<!-- Page Content Wrapper -->
	<div class="content">