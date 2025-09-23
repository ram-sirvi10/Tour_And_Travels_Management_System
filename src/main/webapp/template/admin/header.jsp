<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Admin Dashboard</title>

<!-- Bootstrap CSS & Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

<style>
body { font-family:'Segoe UI'; background:#f7f9fc; margin:0; padding:0; }
.navbar { background:#0d6efd; color:white; padding:15px 20px; display:flex; justify-content:space-between; align-items:center; }
.navbar h1 { margin:0; font-size:1.8rem; }

.dashboard-container { display:flex; min-height:100vh; }
.sidebar { background:#0d6efd; color:white; width:220px; padding:20px; flex-shrink:0; }
.sidebar h3 { margin-bottom:20px; }
.sidebar a { display:block; color:white; text-decoration:none; padding:10px; border-radius:8px; margin-bottom:10px; transition:0.3s; }
.sidebar a:hover { background:#084298; }

.main-content { flex-grow:1; padding:30px; }

.card { border-radius:12px; box-shadow:0 4px 15px rgba(0,0,0,0.1); padding:20px; margin-bottom:20px; background:white; }

.btn-logout { background:#ff3b2e; color:white; border:none; padding:10px 20px; border-radius:50px; cursor:pointer; }
.btn-logout:hover { background:#e0291a; }

.btn-profile {
    color: white;
    background: transparent;
    border: none;
    padding: 8px 12px;
    display: flex;
    align-items: center;
    cursor: pointer;
}

.dropdown { position: relative; } /* Ensures dropdown works inside flex navbar */

.profile-card {
    background: #ffffff;
    transition: transform 0.3s, box-shadow 0.3s;
}
.profile-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 40px rgba(0,0,0,0.25);
}

.profile-view-img {
    width: 150px;
    height: 150px;
    border-radius: 50%;
    object-fit: cover;
    cursor: pointer;
    border: 4px solid #0d6efd;
    transition: transform 0.2s;
}
.profile-view-img:hover {
    transform: scale(1.05);
}

.profile-view-icon {
    font-size: 150px;
    color: #0d6efd;
}
</style>
</head>
<body>
<!-- Navbar -->
<div class="navbar">
    <h1>Admin Dashboard</h1>

    <div class="dropdown ms-auto" id="profileDropdownWrapper">
       <button class="btn btn-primary dropdown-toggle d-flex align-items-center" 
        type="button" id="profileDropdown" data-bs-toggle="dropdown" aria-expanded="false">

    <% if (user.getImageurl() != null && !user.getImageurl().isEmpty()) {%>
    
            <img src="<%=user.getImageurl()%>" alt="Profile" 
                 style="width:40px; height:40px; border-radius:50%; object-fit:cover;">
        <% } else {%>
            <i class="bi bi-person-circle" style="font-size:1.5rem;"></i>
        <% } %>
    <span><%= user != null ? user.getUserName() : "Admin" %></span>
</button>

        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/template/admin/profileManagement.jsp?button=viewProfile">View Profile</a></li>
            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/template/admin/profileManagement.jsp?button=updateProfile">Edit Profile</a></li>
            <li><a class="dropdown-item" href="<%=request.getContextPath()%>/template/admin/profileManagement.jsp?button=changePassword">Change Password</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item text-danger" href="<%=request.getContextPath()%>/auth?button=logout">Logout</a></li>
        </ul>
    </div>
</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Hover Dropdown Script -->
<script>
document.addEventListener("DOMContentLoaded", function() {
    const dropdownWrapper = document.getElementById('profileDropdownWrapper');
    const dropdownToggle = dropdownWrapper.querySelector('.dropdown-toggle');
    const dropdown = new bootstrap.Dropdown(dropdownToggle);

    dropdownWrapper.addEventListener('mouseenter', () => {
        dropdown.show();
    });

    dropdownWrapper.addEventListener('mouseleave', () => {
        dropdown.hide();
    });
});
</script>
</body>
</html>
