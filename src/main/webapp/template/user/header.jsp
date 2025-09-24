
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}
%>


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>TravelMate</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
body {
	font-family: 'Segoe UI', sans-serif;
	margin: 0;
	padding: 0;
}

.navbar {
	background: #0d6efd;
	padding: 10px 20px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.navbar .brand {
	color: white;
	font-weight: bold;
	font-size: 1.6rem;
	text-decoration: none;
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

a.nav-link {
	color: white;
	text-decoration: none;
	margin-left: 15px;
	transition: 0.2s;
}

a.nav-link:hover {
	text-decoration: underline;
}
.package-img {
    height: 180px;
    object-fit: cover;
}
.card-text.text-truncate {
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
}
/* Make carousel arrows visible */
.carousel-control-prev-icon,
.carousel-control-next-icon {
    background-color: rgba(0,0,0,0.5); /* semi-transparent dark background */
    border-radius: 50%;                 /* make it circular */
    width: 40px;
    height: 40px;
}



</style>
</head>
<body>
	<nav class="navbar">
		<a href="<%=request.getContextPath()%>/user?button=dashboard"
			class="brand">TravelMate</a>

		<div class="d-flex align-items-center gap-3">
			<%-- Optional navigation links --%>
			<a href="<%=request.getContextPath()%>/template/user/userDashboard.jsp"
				class="nav-link">Home</a>
			<a href="<%=request.getContextPath()%>/template/user/packages.jsp"
				class="nav-link">Packages</a> <a
				href="<%=request.getContextPath()%>/template/user/bookingHistory.jsp"
				class="nav-link">Bookings</a> <a
				href="<%=request.getContextPath()%>/template/user/paymentHistory.jsp"
				class="nav-link">Payments</a>

			<%-- Profile image --%>
			<a
				href="<%=request.getContextPath()%>/template/user/profileManagement.jsp?button=viewProfile">
				<%
				if (user.getImageurl() != null && !user.getImageurl().isEmpty()) {
				%>
				<img src="<%=user.getImageurl()%>" alt="Profile" class="profile-img">
				<%
				} else {
				%> <i class="bi bi-person-circle profile-icon"></i> <%
 }
 %>
			</a>
		</div>
	</nav>

	<!-- Container for page content -->
	<div class="container mt-4">