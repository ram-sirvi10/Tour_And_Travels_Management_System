<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Package</title>
</head>
<body>
    <h2>Add New Package</h2>
    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="agency" method="post">
        <input type="hidden" name="button" value="addPackage">
        <label>Name:</label>
        <input type="text" name="name" ><br>
        <label>Description:</label>
        <textarea name="description" ></textarea><br>
        <label>Price:</label>
        <input type="number" name="price" step="0.01" ><br>
        <label>Duration (days):</label>
        <input type="number" name="duration" ><br>
        <button type="submit">Add Package</button>
    </form>
</body>
</html>