<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    if(user == null || !"ADMIN".equals(user.getUserRole())){
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

<!-- Bootstrap 5 CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

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

.navbar h1 { margin: 0; font-size: 1.8rem; }

.dashboard-container { display: flex; min-height: 100vh; }

/* Sidebar */
.sidebar {
    background: #0d6efd;
    color: white;
    width: 220px;
    padding: 20px;
    flex-shrink: 0;
}

.sidebar h3 { margin-bottom: 20px; }

.sidebar a {
    display: block;
    color: white;
    text-decoration: none;
    padding: 10px;
    border-radius: 8px;
    margin-bottom: 10px;
    transition: 0.3s;
}

.sidebar a:hover { background: #084298; }

/* Main content */
.main-content { flex-grow: 1; padding: 30px; }

.main-content h2 { color: #0d6efd; margin-bottom: 20px; }

/* Cards */
.card {
    border-radius: 12px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    transition: 0.3s;
    padding: 20px;
    margin-bottom: 20px;
    background: white;
    cursor: pointer;
    text-align: center;
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

.btn-logout:hover { background: #e0291a; }

/* Responsive */
@media (max-width:768px) {
    .dashboard-container { flex-direction: column; }
    .sidebar { width: 100%; }
}
</style>
</head>
<body>

<!-- Navbar -->
<div class="navbar d-flex justify-content-between align-items-center">
    <h1>Admin Dashboard</h1>
    <div>
        <span>Welcome, <%=user.getUserName()%>!</span>
        <a href="<%=request.getContextPath()%>/auth?button=logout" class="btn-logout ms-3">Log out</a>
    </div>
</div>

<div class="dashboard-container">
    <!-- Sidebar -->
    <!-- Sidebar -->
<div class="sidebar">
    <h3>Menu</h3>
    <a href="<%=request.getContextPath()%>/admin?button=dashboard">Dashboard Overview</a>
    
    <!-- Dropdown for Manage Users -->
    <div class="dropdown">
        <a href="#" class="dropdown-toggle" data-bs-toggle="collapse" data-bs-target="#userMenu" aria-expanded="false">
            Manage Users
        </a>
        <div id="userMenu" class="collapse ms-3">
            <a href="<%=request.getContextPath()%>/admin?button=manageUsers">User List</a>
            <a href="<%=request.getContextPath()%>/admin?button=deletedUsers">Deleted History</a>
        </div>
    </div>

    <!-- Dropdown for Manage Agencies -->
    <div class="dropdown">
        <a href="#" class="dropdown-toggle" data-bs-toggle="collapse" data-bs-target="#agencyMenu" aria-expanded="false">
            Manage Agencies
        </a>
        <div id="agencyMenu" class="collapse ms-3">
            <a href="<%=request.getContextPath()%>/admin?button=manageAgencies">Agency List</a>
            <a href="<%=request.getContextPath()%>/admin?button=pendingAgencies">Pending Requests</a>
            <a href="<%=request.getContextPath()%>/admin?button=deletedAgencies">Deleted History</a>
        </div>
    </div>
</div>


    <!-- Main Content -->
    <div class="main-content">
        <h2>Dashboard Overview</h2>
        <div class="row g-4">
            <!-- Total Users -->
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=manageUsers" style="text-decoration:none; color:inherit;">
                    <div class="card">
                        <h4>Total Users</h4>
                        <p><%= totalUsers != null ? totalUsers : 0 %></p>
                    </div>
                </a>
            </div>

            <!-- Total Agencies -->
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=manageAgencies" style="text-decoration:none; color:inherit;">
                    <div class="card">
                        <h4>Total Agencies</h4>
                        <p><%= totalAgencies != null ? totalAgencies : 0 %></p>
                    </div>
                </a>
            </div>

            <!-- Pending Agencies -->
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=pendingAgencies" style="text-decoration:none; color:inherit;">
                    <div class="card">
                        <h4>Pending Agencies</h4>
                        <p><%= pendingAgencies != null ? pendingAgencies : 0 %></p>
                    </div>
                </a>
            </div>

            <!-- Rejected/Deleted Agencies -->
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=deletedAgencies" style="text-decoration:none; color:inherit;">
                    <div class="card">
                        <h4>Rejected Agencies</h4>
                        <p><%= rejectedAgencies != null ? rejectedAgencies : 0 %></p>
                    </div>
                </a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
