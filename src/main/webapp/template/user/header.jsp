
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


.logo-text strong { font-weight: 700; letter-spacing: 0.4px; }
.logo-text .accent { color: #ffd6cf; margin-left: 2px; font-weight: 600; }

/* hover effect */
nav .logo:hover .logo-icon { transform: translateY(-3px) scale(1.03); transition: transform .25s ease; }
nav .logo:hover .logo-text { text-decoration: none; opacity: 0.95; }

</style>
</head>
<body>
	<nav class="navbar">
	

	<a href="<%=request.getContextPath()%>/user?button=dashboard" class="logo d-flex align-items-center gap-2"
		aria-label="TravelMate - Home"> <!-- SVG icon --> <svg
			class="logo-icon" xmlns="http://www.w3.org/2000/svg"
			viewBox="0 0 64 64" width="36" height="36" aria-hidden="true">
    <defs>
      <linearGradient id="g1" x1="0" x2="1">
        <stop offset="0" stop-color="#ff6f61" />
        <stop offset="1" stop-color="#ff9a8b" />
      </linearGradient>
    </defs>
    <!-- location pin -->
    <path fill="url(#g1)"
				d="M32 4C22 4 14 12 14 22c0 12 14 26 16 28 2-2 16-16 16-28 0-10-8-18-14-18z" />
    <circle cx="32" cy="22" r="6" fill="#fff" />
    <!-- airplane silhouette (white) -->
    <path fill="#fff"
				d="M45 30c0 .6-.3 1.1-.8 1.4L40 34l1 4-5-3-5 3 1-4-4.2-2.6A1.6 1.6 0 0 1 27 30V28c0-.9.8-1.6 1.6-1.6L35 28l6-3-2 5 4 0c.6 0 1.1.3 1.4.8.2.5.2 1.1 0 1.4z" />
  </svg> <!-- text --><span class="logo-text fw-bold fs-4" style="color:#222;">
    Travel<span class="accent" style="color:#ff6f61;">Mate</span>
</span>
	
	</a>



		<div class="d-flex align-items-center gap-3">
			<%-- Optional navigation links --%>
			<a href="<%=request.getContextPath()%>/user?button=dashboard"
				class="nav-link">Home</a>
			<a href="<%=request.getContextPath()%>/package?button=packageList" class="nav-link">Packages</a>
 <a
				href="<%=request.getContextPath()%>/booking?button=bookingHistroy"
				class="nav-link">Bookings</a> <a
				href="<%=request.getContextPath()%>/booking?button=paymentHistory"
				class="nav-link">Payments</a>

			<%-- Profile image --%>
			<a
				href="<%=request.getContextPath()%>/user?button=viewProfile">
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