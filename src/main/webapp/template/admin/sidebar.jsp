<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Sidebar -->
<div class="sidebar">
    <h3>Menu</h3>
    <a href="<%=request.getContextPath()%>/admin?button=dashboard">Dashboard Overview</a>

    <!-- Dropdown for Manage Users -->
    <div class="dropdown">
        <a href="#" class="dropdown-toggle" data-bs-toggle="collapse" data-bs-target="#userMenu" aria-expanded="false">Manage Users</a>
        <div id="userMenu" class="collapse ms-3">
            <a href="<%=request.getContextPath()%>/admin?button=manageUsers">User List</a>
            <a href="<%=request.getContextPath()%>/admin?button=deletedUsers">Deleted History</a>
        </div>
    </div>

    <!-- Dropdown for Manage Agencies -->
    <div class="dropdown">
        <a href="#" class="dropdown-toggle" data-bs-toggle="collapse" data-bs-target="#agencyMenu" aria-expanded="false">Manage Agencies</a>
        <div id="agencyMenu" class="collapse ms-3">
            <a href="<%=request.getContextPath()%>/admin?button=manageAgencies">Agency List</a>
            <a href="<%=request.getContextPath()%>/admin?button=pendingAgencies">Pending Requests</a>
            <a href="<%=request.getContextPath()%>/admin?button=deletedAgencies">Deleted History</a>
        </div>
    </div>
</div>
