<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="header.jsp"%>
<%@ page import="java.util.Map"%>

<%
long totalPackages = request.getAttribute("totalPackages") != null ? (long) request.getAttribute("totalPackages") : 0;
long activePackages = request.getAttribute("activePackages") != null ? (long) request.getAttribute("activePackages") : 0;
long totalBookings = request.getAttribute("totalBookings") != null ? (long) request.getAttribute("totalBookings") : 0;

// For chart, assuming monthly revenue map is set in request
Map<String, Double> monthlyRevenue = (Map<String, Double>) request.getAttribute("monthlyRevenue");
if (monthlyRevenue == null) {
    monthlyRevenue = java.util.Collections.emptyMap();
}
%>

<h2 class="mb-4">📊 Dashboard Overview</h2>

<div class="row g-3">

    <!-- Total Packages Card -->
    <div class="col-md-4">
        <a href="agency?button=viewPackages" style="text-decoration:none;">
            <div class="card text-bg-primary shadow mb-3 cursor-pointer hover-scale">
                <div class="card-body text-center">
                    <h5>Total Packages</h5>
                    <h3><%= totalPackages %></h3>
                </div>
            </div>
        </a>
    </div>

    <!-- Active Packages Card -->
    <div class="col-md-4">
        <a href="agency?button=viewPackages&active=true" style="text-decoration:none;">
            <div class="card text-bg-success shadow mb-3 cursor-pointer hover-scale">
                <div class="card-body text-center">
                    <h5>Active Packages</h5>
                    <h3><%= activePackages %></h3>
                </div>
            </div>
        </a>
    </div>

    <!-- Total Bookings Card -->
    <div class="col-md-4">
        <a href="booking?button=viewBookings" style="text-decoration:none;">
            <div class="card text-bg-warning shadow mb-3 cursor-pointer hover-scale">
                <div class="card-body text-center">
                    <h5>Total Bookings</h5>
                    <h3><%= totalBookings %></h3>
                </div>
            </div>
        </a>
    </div>

</div>


<%@ include file="footer.jsp"%>


<style>
    .cursor-pointer { cursor: pointer; }
    .hover-scale:hover { transform: scale(1.05); transition: 0.3s; }
</style>
