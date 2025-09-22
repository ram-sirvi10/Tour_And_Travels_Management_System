<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
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
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>

<div class="dashboard-container">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <jsp:include page="header.jsp" />

        <h2>Dashboard Overview</h2>
        <div class="row g-4">
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=manageUsers" style="text-decoration:none;color:inherit;">
                    <div class="card">
                        <h4>Total Users</h4>
                        <p><%= totalUsers != null ? totalUsers : 0 %></p>
                    </div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=manageAgencies" style="text-decoration:none;color:inherit;">
                    <div class="card">
                        <h4>Total Agencies</h4>
                        <p><%= totalAgencies != null ? totalAgencies : 0 %></p>
                    </div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=pendingAgencies" style="text-decoration:none;color:inherit;">
                    <div class="card">
                        <h4>Pending Agencies</h4>
                        <p><%= pendingAgencies != null ? pendingAgencies : 0 %></p>
                    </div>
                </a>
            </div>
            <div class="col-md-4">
                <a href="<%=request.getContextPath()%>/admin?button=deletedAgencies" style="text-decoration:none;color:inherit;">
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
