<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Agency Dashboard</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { background-color: #f8f9fa; }
    .sidebar {
      height: 100vh; background: #343a40; color: white; padding-top: 20px;
      position: fixed; width: 220px;
    }
    .sidebar a { color: white; display: block; padding: 10px; text-decoration: none; }
    .sidebar a:hover { background: #495057; }
    .content { margin-left: 230px; padding: 20px; }
  </style>
</head>
<body>
  <nav class="navbar navbar-dark bg-dark">
    <div class="container-fluid">
      <span class="navbar-brand mb-0 h1">Travel Agency Dashboard</span>
    </div>
  </nav>

  <div class="sidebar">
    <h5 class="px-3">Menu</h5>
    <a href="addPackage.jsp">➕ Add Package</a>
    <a href="managePackage.jsp">📦 Manage Packages</a>
    <a href="viewBookings.jsp">📖 View Bookings</a>
  </div>

  <div class="content">
