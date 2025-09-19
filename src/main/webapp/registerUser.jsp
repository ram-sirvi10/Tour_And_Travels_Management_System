<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register | TravelMate</title>
<!-- <link rel="stylesheet" href="style.css"> -->
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="container">
    
    <div style="text-align:center; margin-bottom:20px;">
   
        <a href="registerAgency.jsp" class="btn">Register As Company</a>
    </div>
<h2>Register User</h2>
    <%
        Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    %>

    <form action="<%= request.getContextPath() %>/auth" method="post">
        <label>Name:</label>
        <input type="text" name="name" value="<%= request.getParameter("name") != null ? request.getParameter("name") : "" %>">
        <% if(errors != null && errors.get("username") != null){ %>
            <div class="error"><%= errors.get("username") %></div>
        <% } %>

        <label>Email:</label>
        <input type="email" name="email" value="<%= request.getParameter("email") != null ? request.getParameter("email") : "" %>">
        <% if(errors != null && errors.get("email") != null){ %>
            <div class="error"><%= errors.get("email") %></div>
        <% } %>

        <label>Password:</label>
        <input type="password" name="password">
        <% if(errors != null && errors.get("password") != null){ %>
            <div class="error"><%= errors.get("password") %></div>
        <% } %>

        <label>Confirm Password:</label>
        <input type="password" name="confirmPassword">
        <% if(errors != null && errors.get("confirmPassword") != null){ %>
            <div class="error"><%= errors.get("confirmPassword") %></div>
        <% } %>

        <button type="submit" name="button" value="registerAsUser">Register</button>
    </form>
</div>
</body>
</html>
