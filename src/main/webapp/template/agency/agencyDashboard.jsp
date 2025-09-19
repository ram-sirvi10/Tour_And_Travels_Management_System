<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO" %>

<%
AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");
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
                <li class="nav-item"><a href="#" class="nav-link">Dashboard</a></li>
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
                        <h2>24</h2>
                        <p class="small-muted">Active packages you manage</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h5>Total Bookings</h5>
                        <h2>1287</h2>
                        <p class="small-muted">+5% since last month</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h5>Total Revenue</h5>
                        <h2>₹12,547,850</h2>
                        <p class="small-muted">+8% growth</p>
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
                        <tr><td>BKG-1001</td><td>Anita</td><td>Goa Beach Escape</td><td>2025-08-01</td><td class="status-confirmed">Confirmed</td><td>₹12,000</td></tr>
                        <tr><td>BKG-1002</td><td>Rohan</td><td>Himalaya Trek</td><td>2025-09-10</td><td class="status-pending">Pending</td><td>₹22,000</td></tr>
                        <tr><td>BKG-1003</td><td>Sita</td><td>Kerala Backwaters</td><td>2025-07-20</td><td class="status-cancelled">Cancelled</td><td>₹15,000</td></tr>
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

<!-- Charts Script -->
<script>
new Chart(document.getElementById('bookingsChart'), {
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
new Chart(document.getElementById('revenueChart'), {
    type: 'bar',
    data: {
        labels: ['Goa Escape','Himalaya Trek','Kerala Tour','Rajasthan Heritage','Andaman Package'],
        datasets: [{
            label: 'Revenue',
            data: [320000,250000,180000,210000,350000],
            backgroundColor: ['#007bff','#28a745','#ffc107','#17a2b8','#6f42c1']
        }]
    }
});
</script>
</body>
</html>
