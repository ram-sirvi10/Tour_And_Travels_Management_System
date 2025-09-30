<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%@ page import="java.util.List"%>

<%
    AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");

%>

<%
    // Assume you already have a DAO or Service to fetch packages/bookings dynamically

    List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
    Integer totalBookings = (Integer) request.getAttribute("totalBookings");
    Double totalRevenue = (Double) request.getAttribute("totalRevenue");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Agency Dashboard - <%= (agency != null ? agency.getAgencyName() : "Agency") %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body { background: #f7f9fc; }
        .sidebar { min-height: 100vh; background: #0d6efd; color: #fff; }
        .sidebar h3 { font-size: 1.4rem; }
        .nav-link { color: rgba(255,255,255,0.9); }
        .nav-link:hover { color: #fff; background: rgba(255,255,255,0.1); border-radius: 6px; }
        .card-analytics { border-left: 4px solid #0d6efd; }
        .small-muted { font-size: 0.85rem; color: #6c757d; }
        .table td, .table th { vertical-align: middle; }
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
                <li class="nav-item"><a href="<%=request.getContextPath() %>/agency?button>" class="nav-link">Add Package</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Bookings</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Payments</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Reports</a></li>
                <li class="nav-item"><a href="#" class="nav-link">Support</a></li>
            </ul>
        </div>

        <!-- Main Content -->
        <div class="col-12 col-md-9 col-lg-10 p-4">
            <h2 class="mb-4">Welcome, <%= (agency != null ? agency.getAgencyName() : "Agency") %></h2>

            <% if(error != null) { %>
                <div class="alert alert-danger"><%= error %></div>
            <% } %>

            <!-- Add New Package Form -->
            <div class="card mb-4 p-3 shadow-sm" id="addPackageForm">
                <h5 class="mb-3">Add New Package</h5>
                <form action="agency" method="post">
                    <input type="hidden" name="button" value="addPackage">
                    <div class="row g-2">
                        <div class="col-md-6">
                            <label class="form-label">Title:</label>
                            <input type="text" class="form-control" name="title" >
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Location:</label>
                            <input type="text" class="form-control" name="location" >
                        </div>
                        <div class="col-12">
                            <label class="form-label">Description:</label>
                            <textarea class="form-control" name="description" rows="3" ></textarea>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Price:</label>
                            <input type="number" class="form-control" name="price" step="0.01" >
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Duration (days):</label>
                            <input type="number" class="form-control" name="duration" >
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Active:</label>
                            <select class="form-select" name="isActive">
                                <option value="true">Yes</option>
                                <option value="false">No</option>
                            </select>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary mt-3">Add Package</button>
                </form>
            </div>

            <!-- Analytics Cards -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h6>Total Packages</h6>
                        <h2><%= (packages != null ? packages.size() : 0) %></h2>
                        <p class="small-muted">Active packages you manage</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h6>Total Bookings</h6>
                        <h2><%= (totalBookings != null ? totalBookings : 0) %></h2>
                        <p class="small-muted">Bookings this month</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-analytics p-3 shadow-sm">
                        <h6>Total Revenue</h6>
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


            <!-- Packages Table -->
            <div class="card p-3 shadow-sm">
                <h5>My Packages</h5>
                <div class="table-responsive">
                    <table class="table table-striped align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Description</th>
                                <th>Location</th>
                                <th>Price</th>
                                <th>Duration</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                if (packages != null && !packages.isEmpty()) {
                                    for (PackageResponseDTO p : packages) {
                            %>
                            <tr>
                                <td>PCK-<%= p.getPackageId() %></td>
                                <td><%= p.getTitle() %></td>
                                <td><%= p.getDescription() %></td>
                                <td><%= p.getLocation() %></td>
                                <td>₹<%= p.getPrice() %></td>
                                <td><%= p.getDuration() %> days</td>
                                <td>
                                    <!-- Edit Button triggers modal -->
                                    <button type="button" class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#editModal<%=p.getPackageId()%>">Edit</button>

                                    <!-- Toggle Active/Inactive -->
                                    <form action="agency" method="post" class="d-inline">
                                        <input type="hidden" name="button" value="togglePackage">
                                        <input type="hidden" name="packageId" value="<%= p.getPackageId() %>">
                                        <button type="submit" class="btn btn-sm <%= p.getIsActive() ? "btn-warning" : "btn-success" %>">
                                            <%= p.getIsActive() ? "Deactivate" : "Activate" %>
                                        </button>
                                    </form>

                                    <!-- Delete -->
                                    <form action="agency" method="post" class="d-inline">
                                        <input type="hidden" name="button" value="deletePackage">
                                        <input type="hidden" name="packageId" value="<%= p.getPackageId() %>">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </td>
                            </tr>

                            <!-- Edit Modal -->
                            <div class="modal fade" id="editModal<%=p.getPackageId()%>" tabindex="-1" aria-hidden="true">
                              <div class="modal-dialog">
                                <div class="modal-content">
                                  <div class="modal-header">
                                    <h5 class="modal-title">Edit Package - <%= p.getTitle() %></h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                  </div>
                                  <form action="agency" method="post">
                                      <div class="modal-body">
                                          <input type="hidden" name="button" value="updatePackage">
                                          <input type="hidden" name="packageId" value="<%= p.getPackageId() %>">
                                          <div class="mb-2">
                                              <label class="form-label">Title:</label>
                                              <input type="text" class="form-control" name="title" value="<%= p.getTitle() %>" >
                                          </div>
                                          <div class="mb-2">
                                              <label class="form-label">Description:</label>
                                              <textarea class="form-control" name="description" rows="3" ><%= p.getDescription() %></textarea>
                                          </div>
                                          <div class="mb-2">
                                              <label class="form-label">Location:</label>
                                              <input type="text" class="form-control" name="location" value="<%= p.getLocation() %>" >
                                          </div>
                                          <div class="mb-2">
                                              <label class="form-label">Price:</label>
                                              <input type="number" class="form-control" name="price" value="<%= p.getPrice() %>" step="0.01" >
                                          </div>
                                          <div class="mb-2">
                                              <label class="form-label">Duration (days):</label>
                                              <input type="number" class="form-control" name="duration" value="<%= p.getDuration() %>" >
                                          </div>
                                          <div class="mb-2">
                                              <label class="form-label">Active:</label>
                                              <select class="form-select" name="isActive">
                                                  <option value="true" <%= p.getIsActive() ? "selected" : "" %>>Active</option>
                                                  <option value="false" <%= !p.getIsActive() ? "selected" : "" %>>Inactive</option>
                                              </select>
                                          </div>
                                      </div>
                                      <div class="modal-footer">
                                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                          <button type="submit" class="btn btn-primary">Save Changes</button>
                                      </div>
                                  </form>
                                </div>
                              </div>
                            </div>

                            <%
                                    }
                                } else {
                            %>
                            <tr><td colspan="7" class="text-center">No packages found</td></tr>
                            <%

                                }
                            %>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>






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
                fill: true,
                tension: 0.3
            }]
        }
    });

    new Chart(document.getElementById('revenueChart'), {
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
