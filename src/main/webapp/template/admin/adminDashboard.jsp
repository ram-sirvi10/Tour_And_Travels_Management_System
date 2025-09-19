<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%
UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Dashboard | TravelMate</title>

<!-- Bootstrap 5 CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

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

/* Cards */
.card {
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    transition: 0.3s;
    padding: 20px;
    margin-bottom: 20px;
    background: white;
}

.card:hover {
    transform: translateY(-5px);
    box-shadow: 0 6px 20px rgba(0,0,0,0.15);
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
@media (max-width: 768px) {
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

<!-- Navbar -->
<div class="navbar d-flex justify-content-between align-items-center">
    <h1>Admin Dashboard</h1>
    <span>Welcome, <%=user.getUserName()%>!</span>
      <a href="<%=request.getContextPath()%>/auth?button=logout" class="btn-logout mt-4">Log out</a>
</div>

<div class="dashboard-container">
    <!-- Sidebar -->
  <div class="sidebar">
    <h3>Menu</h3>
    <a href="<%=request.getContextPath()%>/admin?button=manageUsers">Manage Users</a>
    <a href="<%=request.getContextPath()%>/admin?button=manageAgencies">Manage Agencies</a>
     <a href="<%=request.getContextPath()%>/admin?button=pendingAgencies" >Panding Agency Requests</a>
</div>

    <!-- Main Content -->
    <div class="main-content">
        <h2>Dashboard Overview</h2>
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <h4>Total Users</h4>
                    <p>125</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <h4>Total Agencies</h4>
                    <p>35</p>
                </div>
            </div>
          
          <div class="col-md-4">
    
</div>
          
        </div>

       
       
          
       
    </div>
</div>

</body>
</html>
