<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login | TravelMate</title>
<!-- <link rel="stylesheet" href="style.css"> -->
</head>
<body>
<jsp:include page="navbar.jsp" />

<div class="container">
    <h2>Login</h2>
    <%
        Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
        String globalError = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
        String emailValue = request.getParameter("email") != null ? request.getParameter("email") : "";
    %>

    <form action="AuthServlet" method="post">
        <input type="email" name="email" placeholder="Email" value="<%= emailValue %>">
        <% if(errors != null && errors.get("email") != null){ %>
            <div class="error"><%= errors.get("email") %></div>
        <% } %>

        <input type="password" name="password" placeholder="Password">
        <% if(errors != null && errors.get("password") != null){ %>
            <div class="error"><%= errors.get("password") %></div>
        <% } %>

		   <% if(errors != null && errors.get("loginError") != null){ %>
            <div class="error"><%= errors.get("loginError") %></div>
        <% } %>
        <% if(globalError != null){ %>
            <div class="error"><%= globalError %></div>
        <% } %>

        <% if(success != null){ %>
            <div class="success"><%= success %></div>
        <% } %>

        <button type="submit" name="button" value="login">Login</button>
    </form>

   <p>Don't have an account? <a href="registerUser.jsp">Register Here</a></p>

</div>
</body>
</html>
