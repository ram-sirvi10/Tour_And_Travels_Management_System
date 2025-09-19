<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.model.Agency"%>
<%@ page import="java.util.List"%>

<%
    // Get the logged-in agency from session
    Agency agency = (Agency) session.getAttribute("agency");

    // Assume you already have a DAO or Service to fetch packages/bookings dynamically
    List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
    Integer totalBookings = (Integer) request.getAttribute("totalBookings");
    Double totalRevenue = (Double) request.getAttribute("totalRevenue");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agency Dashboard - <%= (agency != null ? agency.getAgencyName() : "Agency") %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body { background: #f7f9fc; }
        .sidebar { min-height: 100vh; background: #0d6efd; color: #fff; }
        .nav-link { color: rgba(255,255,255,0.9); }
        .card-analytics { border-left: 4px solid #0d6efd; }
        .small-muted { font-size: 0.85rem; color: #6c757d; }
        .status-confirmed { color: green; font-weight: 600; }
        .status-pending { color: orange; font-weight: 600; }
        .status-cancelled { color: red; font-weight: 600; }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-12 col-md-3 col-lg-2 p-0 sidebar">
            <div class="p-4">
                <h3><%= (agency != null ? agency.getAgencyName() : "Travel Agency") %></h3>
                <p class="small">Agency Dashboard</p>
            </div>
            <ul class="nav flex-column px-2">
                <li class="nav-item"><a href="?button=dashboard" class="nav-link">Dashboard</a></li>
                <li class="nav-item"><a href="#" class="nav-link">My Packages</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Bookings</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Payments</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Reports</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Support</a></li>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="col-12 col-md-9 col-lg-10 p-4">
            <h2 class="mb-4">Welcome, <%= (agency != null ? agency.getAgencyName() : "Agency") %></h2>

            <!-- Analytics Cards -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h5>Total Packages</h5>
                        <h2><%= (packages != null ? packages.size() : 0) %></h2>
                        <p class="small-muted">Active packages you manage</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h5>Total Bookings</h5>
                        <h2><%= (totalBookings != null ? totalBookings : 0) %></h2>
                        <p class="small-muted">Bookings this month</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h5>Total Revenue</h5>
                        <h2>₹<%= (totalRevenue != null ? totalRevenue.intValue() : 0) %></h2>
                        <p class="small-muted">Revenue generated</p>
                    </div>
                </div>
            </div>

            <!-- Charts -->
            <div class="row mb-4">
                <div class="col-md-6">
                    <div class="card p-3 shadow-sm">
                        <h5>Monthly Bookings</h5>
                        <canvas id="bookingsChart"></canvas>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card p-3 shadow-sm">
                        <h5>Revenue by Package</h5>
                        <canvas id="revenueChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Recent Bookings Table -->
            <div class="card p-3 shadow-sm">
                <h5>Recent Bookings</h5>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th><th>Customer</th><th>Package</th><th>Date</th><th>Status</th><th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (packages != null) {
                                for (PackageResponseDTO p : packages) {
                        %>
                        <tr>
                            <td>PCK-<%= p.getPackageId() %></td>
                            <td>—</td>
                            <td><%= p.getTitle() %></td>
                            <td>—</td>
                            <td class="<%= p.isIsActive() ? "status-confirmed" : "status-pending" %>">
                                <%= p.isIsActive() ? "Active" : "Inactive" %>
                            </td>
                            <td>₹<%= p.getPrice() %></td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr><td colspan="6">No packages found</td></tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

<!-- Charts Script -->
<script>
    const bookingsChart = new Chart(document.getElementById('bookingsChart'), {
        type: 'line',
        data: {
            labels: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep'],
            datasets: [{
                label: 'Bookings',
                data: [12,18,20,16,22,14,10,25,28],
                borderColor: 'blue',
                backgroundColor: 'rgba(0,123,255,0.2)',
                fill: true
            }]
        }
    });

    const revenueChart = new Chart(document.getElementById('revenueChart'), {
        type: 'bar',
        data: {
            labels: [
                <% if(packages != null){ 
                    for(int i=0;i<packages.size();i++){ %>
                        '<%= packages.get(i).getTitle() %>'<%= i < packages.size()-1 ? "," : "" %>
                <% }} %>
            ],
            datasets: [{
                label: 'Revenue',
                data: [
                    <% if(packages != null){ 
                        for(int i=0;i<packages.size();i++){ %>
                            <%= packages.get(i).getPrice() %><%= i < packages.size()-1 ? "," : "" %>
                    <% }} %>
                ],
                backgroundColor: ['#007bff','#28a745','#ffc107','#17a2b8','#6f42c1']
            }]
        }
    });
</script>
</body>
</html>
