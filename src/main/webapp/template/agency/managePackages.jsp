<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.travelmanagement.dto.responseDTO.PackageResponseDTO" %>
<%@ page import="java.util.List" %>

<%
    List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
    String errorMsg = (String) request.getAttribute("errorMsg");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Packages</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-4">
    <h2 class="mb-4">Manage Packages</h2>

    <% if (errorMsg != null) { %>
        <div class="alert alert-danger"><%= errorMsg %></div>
    <% } %>

    <!-- Add Package Form -->
    <div class="card mb-4">
        <div class="card-header">Add New Package</div>
        <div class="card-body">
            <form action="agency" method="post">
                <input type="hidden" name="button" value="addPackage">
                <div class="row mb-3">
                    <div class="col">
                        <input type="text" name="title" class="form-control" placeholder="Package Title" required>
                    </div>
                    <div class="col">
                        <input type="text" name="location" class="form-control" placeholder="Location" required>
                    </div>
                </div>
                <div class="mb-3">
                    <textarea name="description" class="form-control" placeholder="Description" required></textarea>
                </div>
                <div class="row mb-3">
                    <div class="col">
                        <input type="number" step="0.01" name="price" class="form-control" placeholder="Price" required>
                    </div>
                    <div class="col">
                        <input type="number" name="duration" class="form-control" placeholder="Duration (days)" required>
                    </div>
                </div>
                <div class="mb-3">
                    <input type="text" name="imageUrl" class="form-control" placeholder="Image URL">
                </div>
                <div class="mb-3">
                    <select name="isActive" class="form-select">
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Add Package</button>
            </form>
        </div>
    </div>

    <!-- List of Packages -->
    <div class="card">
        <div class="card-header">Existing Packages</div>
        <div class="card-body">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th><th>Title</th><th>Location</th><th>Price</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <% if (packages != null && !packages.isEmpty()) { 
                       for (PackageResponseDTO pkg : packages) { %>
                    <tr>
                        <td><%= pkg.getPackageId() %></td>
                        <td><%= pkg.getTitle() %></td>
                        <td><%= pkg.getLocation() %></td>
                        <td>₹<%= pkg.getPrice() %></td>
                       
                        <td>
                            <!-- Update -->
                            <form action="agency" method="post" style="display:inline;">
                                <input type="hidden" name="button" value="updatePackage">
                                <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
                                <button type="submit" class="btn btn-sm btn-warning">Update</button>
                            </form>

                            <!-- Delete -->
                            <form action="agency" method="post" style="display:inline;">
                                <input type="hidden" name="button" value="deletePackage">
                                <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
                                <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                            </form>

                            <!-- Toggle Status -->
                            <form action="agency" method="post" style="display:inline;">
                                <input type="hidden" name="button" value="toggleStatus">
                                <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
                                <button type="submit" class="btn btn-sm btn-secondary">
                                    <%= pkg.getIsActive() ? "Deactivate" : "Activate" %>
                                </button>
                            </form>
                        </td>
                    </tr>
                <% } } else { %>
                    <tr><td colspan="6" class="text-center">No packages found.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
