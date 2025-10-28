<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page
	import="java.util.*, com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import=" com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%@ include file="header.jsp"%>
<%
// Session validation
agency = (AgencyResponseDTO) session.getAttribute("agency");
if (agency == null) {
	response.sendRedirect("login.jsp");
	return;
}
%>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f6f9;
}

.container {
	margin-top: 30px;
}

.filter-form {
	margin-bottom: 30px;
}
</style>

<div class="container">
    <h2 class="mb-4 text-center">📊 Revenue Reports</h2>

    <%
    Double totalRevenue = (Double) request.getAttribute("totalRevenue");
    if (totalRevenue == null) totalRevenue = 0.0;
    String rangeLabel = (request.getParameter("startDate") != null && !request.getParameter("startDate").isEmpty())
            ? "Custom Date Range"
            : ("Year " + request.getAttribute("selectedYear"));
    %>

    <div class="alert alert-info text-center fw-bold fs-5 mt-3">
        💰 Total Revenue for <%= rangeLabel %>: ₹<%= String.format("%.2f", totalRevenue) %>
    </div>

    <!-- Filter Form -->
    <form method="get" action="agency" class="row g-3 filter-form">
        <input type="hidden" name="button" value="viewReports">

        <div class="col-md-2">
            <label>Year:</label>
            <select name="year" class="form-select"  onchange="this.form.submit()">
                <%
                Integer selectedYear = (Integer) request.getAttribute("selectedYear");
                Integer currentYear = (Integer) request.getAttribute("currentYear");
                for (int y = currentYear - 5; y <= currentYear; y++) {
                %>
                <option value="<%=y%>" <%= (selectedYear != null && y == selectedYear) ? "selected" : "" %>>
                    <%= y %>
                </option>
                <% } %>
            </select>
        </div>

        <div class="col-md-3">
            <label>Package:</label>
            <select name="packageId" class="form-select" onchange="this.form.submit()">
                <option value="">All Packages</option>
                <%
                List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
                Integer selectedPackage = (Integer) request.getAttribute("selectedPackage");
                for (PackageResponseDTO pkg : packages) {
                %>
                <option value="<%= pkg.getPackageId() %>" <%= (pkg.getPackageId().equals(selectedPackage) ? "selected" : "") %>>
                    <%= pkg.getTitle() %>
                </option>
                <% } %>
            </select>
        </div>

        <div class="col-md-2">
            <label>Start Date:</label>
            <input type="date" name="startDate" class="form-control" value="<%= request.getAttribute("startDate") != null ? request.getAttribute("startDate") : "" %>">
        </div>

        <div class="col-md-2">
            <label>End Date:</label>
            <input type="date" name="endDate" class="form-control" value="<%= request.getAttribute("endDate") != null ? request.getAttribute("endDate") : "" %>">
        </div>

        <div class="col-md-1 align-self-end">
            <button type="submit" class="btn btn-primary w-100">Filter</button>
        </div>
        <div class="col-md-2 d-flex align-items-end">
            <a href="agency?button=viewReports" class="btn btn-secondary w-100">Reset</a>
        </div>
    </form>

    <!-- Chart -->
    <div class="row justify-content-center mt-4">
        <div class="col-md-10">
            <canvas id="revenueChart"></canvas>
        </div>
    </div>

    <p class="text-center mt-3 fw-semibold">
        Total Revenue: ₹<%= String.format("%.2f", totalRevenue) %>
    </p>

    <script>
        const ctx = document.getElementById('revenueChart').getContext('2d');

        <% 
        Map<String, Double> monthlyRevenue = (Map<String, Double>) request.getAttribute("monthlyRevenue");
        if(monthlyRevenue == null) monthlyRevenue = new LinkedHashMap<>();

        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for(Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
            labels.append("'").append(entry.getKey()).append("',");
            data.append(entry.getValue()).append(",");
        }
        if(labels.length()>0) labels.setLength(labels.length()-1);
        if(data.length()>0) data.setLength(data.length()-1);
        %>

        const labels = [<%= labels.toString() %>];
        const dataValues = [<%= data.toString() %>];

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Revenue (₹)',
                    data: dataValues,
                    backgroundColor: 'rgba(54, 162, 235, 0.7)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    hoverBackgroundColor: 'rgba(75, 192, 192, 0.8)',
                    hoverBorderColor: 'rgba(75, 192, 192, 1)'
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: true },
                    tooltip: { enabled: true }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        title: { display:true, text:'Revenue (₹)' }
                    },
                    x: {
                        title: { display:true, text:'Month / Range' }
                    }
                }
            }
        });
    </script>
</div>

<%@ include file="footer.jsp"%>
