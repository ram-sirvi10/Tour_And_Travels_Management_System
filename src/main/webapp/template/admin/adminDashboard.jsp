<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.model.Agency" %>

<%
    Agency user = (Agency) session.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ADMIN Dashboard</title>
</head>
<body>
    <h1>Welcome, <%= user.getAgencyName()%>!</h1>

    <p>This is your ADMIN dashboard.</p>

    <!-- Logout Form -->
    <form action="<%= request.getContextPath() %>/AuthServlet" method="post">
        <input type="hidden" name="button" value="logout">
        <input type="submit" value="Logout">
    </form>
</body>
</html>
