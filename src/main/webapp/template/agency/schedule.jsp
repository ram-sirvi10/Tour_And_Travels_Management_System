<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.model.PackageSchedule"%>
<%@ page import="java.util.List"%>

<%
    PackageResponseDTO pkg = (PackageResponseDTO) request.getAttribute("package");
    List<PackageSchedule> scheduleList = (List<PackageSchedule>) request.getAttribute("scheduleList");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Package Schedule - <%= (pkg != null ? pkg.getTitle() : "") %></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <a href="agencydashboard.jsp" class="btn btn-secondary mb-3">← Back to Dashboard</a>

    <h3>Schedule for Package: <%= (pkg != null ? pkg.getTitle() : "") %></h3>

    <% if(error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% if(success != null) { %>
        <div class="alert alert-success"><%= success %></div>
    <% } %>

    <!-- Add Schedule Form -->
    <div class="card p-3 mb-4 shadow-sm">
        <h5>Add Day-wise Schedule</h5>
        <form action="agency" method="post">
            <input type="hidden" name="button" value="addSchedule">
            <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">

            <div class="row g-2">
                <div class="col-md-2">
                    <label class="form-label">Day Number</label>
                    <input type="number" class="form-control" name="dayNumber" min="1" required>
                </div>
                <div class="col-md-4">
                    <label class="form-label">Activity</label>
                    <input type="text" class="form-control" name="activity" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Description</label>
                    <input type="text" class="form-control" name="description">
                </div>
            </div>
            <button type="submit" class="btn btn-primary mt-3">Add Schedule</button>
        </form>
    </div>

    <!-- Schedule Table -->
    <div class="card p-3 shadow-sm">
        <h5>Existing Schedule</h5>
        <div class="table-responsive">
            <table class="table table-striped align-middle">
                <thead>
                    <tr>
                        <th>Day</th>
                        <th>Activity</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(scheduleList != null && !scheduleList.isEmpty()) {
                        for(PackageSchedule s : scheduleList) { %>
                            <tr>
                                <td>Day <%= s.getDayNumber() %></td>
                                <td><%= s.getActivity() %></td>
                                <td><%= s.getDescription() %></td>
                                <td>
                                    <form action="agency" method="post" class="d-inline">
                                        <input type="hidden" name="button" value="deleteSchedule">
                                        <input type="hidden" name="scheduleId" value="<%= s.getScheduleId() %>">
                                        <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                    </form>
                                </td>
                            </tr>
                    <%  }
                    } else { %>
                        <tr><td colspan="4" class="text-center">No schedule added yet</td></tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
