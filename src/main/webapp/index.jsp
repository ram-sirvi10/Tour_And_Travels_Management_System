<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Tour & Travel Management System</title>
</head>
<body>
<jsp:include page="navbar.jsp" />
<%
Object user = session.getAttribute("user");
Object agency = session.getAttribute("agency");

%>

<%
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) {
	errorMessage = (String) request.getAttribute("errorMessage");
}
if (errorMessage != null && !errorMessage.isEmpty()) {
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
<header>
  <h1>Tour & Travel Management System</h1>
  <p>Book your dream destinations with ease and comfort</p>
  <%if(user==null&&agency==null){ %>
  <a href="login.jsp?role=agency" class="btn-started">🏢 Login as Agency</a><%} else{%>
  <a href="login.jsp" class="btn-started">🚀 Get Started</a>
  <%} %>
  
</header>

<section id="features">
  <h2>Our Features</h2>
  <div class="features">
    <div class="feature-box">
      <img src="https://cdn-icons-png.flaticon.com/512/3176/3176296.png">
      <h3>User Friendly</h3>
      <p>Easy registration, secure login, and personalized dashboards.</p>
    </div>
    <div class="feature-box">
      <img src="https://cdn-icons-png.flaticon.com/512/684/684908.png">
      <h3>Browse Packages</h3>
      <p>Find travel packages by location, price, and availability.</p>
    </div>
    <div class="feature-box">
      <img src="https://cdn-icons-png.flaticon.com/512/2169/2169870.png">
      <h3>Booking & Payments</h3>
      <p>Seamless booking and secure payment gateway integration.</p>
    </div>
    <div class="feature-box">
      <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png">
      <h3>Admin Dashboard</h3>
      <p>Manage packages, bookings, inquiries, and reports easily.</p>
    </div>
  </div>
</section>

<section class="cta">
  <h2>Plan Your Next Journey</h2>
  <p>Discover exciting travel packages and start your adventure today.</p>
  
 
  
</section>

<div class="footer">
  &copy; 2025 Tour & Travel Management System | All Rights Reserved
</div>
</body>
</html>
