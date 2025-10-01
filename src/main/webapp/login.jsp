<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Login | TravelMate</title>
</head>
<body>
<jsp:include page="navbar.jsp" />

<%
    String role = request.getParameter("role");
    if (role == null) {
        role = "user"; 
    }

    String heading = "Login " + ("agency".equals(role) ? " as Agency" : "");
    String buttonLabel = "Login  " + ("agency".equals(role) ? " as Agency" : "");

    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    String globalError = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String emailValue = request.getParameter("email") != null ? request.getParameter("email") : "";
%>

<%
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) {
	errorMessage = (String) request.getAttribute("errorMessage");
}
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-danger border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body"><%=errorMessage%></div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>
<div class="container">
    <h2><%= heading %></h2>

    <form action="./auth" method="post">
        <input type="hidden" name="action" value="login">
        <input type="hidden" name="role" value="<%= role %>"> 

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
   <input type="hidden" name="button" value="login">
        <input type="hidden" name="role" value="<%= role %>">
        <button type="submit"><%= buttonLabel %></button>
    </form>

    <% if ("agency".equals(role)) { %>
        <p>Don't have an agency account? <a href="registerAgency.jsp">Register Here</a></p>
    <% } else { %>
        <p>Don't have an account? <a href="registerUser.jsp">Register Here</a></p>
    <% } %>
</div>
</body>
</html>
