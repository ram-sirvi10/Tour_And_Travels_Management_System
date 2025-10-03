<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login | TravelMate</title>
<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<style>
  body {
      background: linear-gradient(to right, #6a11cb, #2575fc);
      min-height: 100vh;
      display: flex;
      justify-content: center;
      align-items: center;
      font-family: Arial, sans-serif;
  }
  .login-card {
      background: #fff;
      padding: 30px;
      border-radius: 15px;
      box-shadow: 0 8px 25px rgba(0,0,0,0.2);
      width: 100%;
      max-width: 400px;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
  }
  .login-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 35px rgba(0,0,0,0.3);
  }
  .login-card h2 {
      margin-bottom: 25px;
      font-weight: 700;
      text-align: center;
      color: #333;
  }
  .login-card .form-control {
      margin-bottom: 15px;
  }
  .login-card button {
      width: 100%;
  }
  .toast-container {
      position: fixed;
      top: 1rem;
      right: 1rem;
      z-index: 1080;
  }
</style>
</head>
<body>

<jsp:include page="navbar.jsp" />

<%
    String role = request.getParameter("role");
    if (role == null) role = "user";

    String heading = "Login " + ("agency".equals(role) ? " as Agency" : "");
    String buttonLabel = "Login " + ("agency".equals(role) ? " as Agency" : "");

    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String globalError = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String emailValue = request.getParameter("email") != null ? request.getParameter("email") : "";

    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("errorMessage");
    if(errorMessage == null) errorMessage =(String)request.getAttribute("errorMessage");
%>

<!-- Toast for error message -->
<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
<div class="toast-container">
    <div class="toast show align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000" data-bs-autohide="true">
        <div class="d-flex">
            <div class="toast-body"><%=errorMessage%></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>
<% } %>

<!-- Login Card -->
<div class="login-card">
    <h2><%= heading %></h2>
    <form action="./auth" method="post">
        <input type="hidden" name="action" value="login">
        <input type="hidden" name="role" value="<%= role %>">

        <input type="email" name="email" class="form-control" placeholder="Email" value="<%= emailValue %>">
        <% if(errors != null && errors.get("email") != null){ %>
            <div class="text-danger mb-2"><%= errors.get("email") %></div>
        <% } %>

        <input type="password" name="password" class="form-control" placeholder="Password">
        <% if(errors != null && errors.get("password") != null){ %>
            <div class="text-danger mb-2"><%= errors.get("password") %></div>
        <% } %>

        <% if(errors != null && errors.get("loginError") != null){ %>
            <div class="text-danger mb-2"><%= errors.get("loginError") %></div>
        <% } %>
        <% if(globalError != null){ %>
            <div class="text-danger mb-2"><%= globalError %></div>
        <% } %>
   

        <input type="hidden" name="button" value="login">
        <input type="hidden" name="role" value="<%= role %>">
        <button type="submit" class="btn btn-primary mt-2"><%= buttonLabel %></button>
    </form>

    <div class="mt-3 text-center">
    <% if ("agency".equals(role)) { %>
        <p>Don't have an agency account? <a href="registerAgency.jsp">Register Here</a></p>
    <% } else { %>
        <p>Don't have an account? <a href="registerUser.jsp">Register Here</a></p>
    <% } %>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
