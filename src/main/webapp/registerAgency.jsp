<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>

<%
    String error = (String) request.getAttribute("error");
    String agencyName = request.getAttribute("agency_name") != null ? (String)request.getAttribute("agency_name") : "";
    String ownerName = request.getAttribute("owner_name") != null ? (String)request.getAttribute("owner_name") : "";
    String email = request.getAttribute("email") != null ? (String)request.getAttribute("email") : "";
    String phone = request.getAttribute("phone") != null ? (String)request.getAttribute("phone") : "";
    String city = request.getAttribute("city") != null ? (String)request.getAttribute("city") : "";
    String state = request.getAttribute("state") != null ? (String)request.getAttribute("state") : "";
    String country = request.getAttribute("country") != null ? (String)request.getAttribute("country") : "";
    String pincode = request.getAttribute("pincode") != null ? (String)request.getAttribute("pincode") : "";
    String regNumber = request.getAttribute("registration_number") != null ? (String)request.getAttribute("registration_number") : "";
%>
<!DOCTYPE html>
<html>
<head>
  <title>Agency Registration</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<jsp:include page="navbar.jsp"/>

<div class="container">
 <div style="text-align:center; margin-bottom:20px;">
        <a href="registerUser.jsp" class="btn ">Register As User</a>
       
    </div>
    <h2>Agency Registration</h2>
    <% if(error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>
    <%
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
%>

<form action="AuthServlet" method="post">
    <input type="text" name="agency_name" placeholder="Agency Name" value="<%= agencyName %>">
    <% if (errors != null && errors.get("agencyName") != null) { %>
        <div class="error"><%= errors.get("agencyName") %></div>
    <% } %>

    <input type="text" name="owner_name" placeholder="Owner Name" value="<%= ownerName %>">
    <% if (errors != null && errors.get("ownerName") != null) { %>
        <div class="error"><%= errors.get("ownerName") %></div>
    <% } %>

    <input type="email" name="email" placeholder="Email" value="<%= email %>">
    <% if (errors != null && errors.get("email") != null) { %>
        <div class="error"><%= errors.get("email") %></div>
    <% } %>

    <input type="text" name="phone" placeholder="Phone" value="<%= phone %>">
    <% if (errors != null && errors.get("phone") != null) { %>
        <div class="error"><%= errors.get("phone") %></div>
    <% } %>

    <input type="text" name="city" placeholder="City" value="<%= city %>">
    <% if (errors != null && errors.get("city") != null) { %>
        <div class="error"><%= errors.get("city") %></div>
    <% } %>

    <input type="text" name="state" placeholder="State" value="<%= state %>">
    <% if (errors != null && errors.get("state") != null) { %>
        <div class="error"><%= errors.get("state") %></div>
    <% } %>

    <input type="text" name="country" placeholder="Country" value="<%= country %>">
    <% if (errors != null && errors.get("country") != null) { %>
        <div class="error"><%= errors.get("country") %></div>
    <% } %>

    <input type="text" name="pincode" placeholder="Pincode" value="<%= pincode %>">
    <% if (errors != null && errors.get("pincode") != null) { %>
        <div class="error"><%= errors.get("pincode") %></div>
    <% } %>

    <input type="text" name="registration_number" placeholder="Registration Number" value="<%= regNumber %>">
    <% if (errors != null && errors.get("registrationNumber") != null) { %>
        <div class="error"><%= errors.get("registrationNumber") %></div>
    <% } %>

    <input type="password" name="password" placeholder="Password">
    <% if (errors != null && errors.get("password") != null) { %>
        <div class="error"><%= errors.get("password") %></div>
    <% } %>

    <input type="password" name="confirm_password" placeholder="Confirm Password">
    <% if (errors != null && errors.get("confirmPassword") != null) { %>
        <div class="error"><%= errors.get("confirmPassword") %></div>
    <% } %>

    <button type="submit" name="button" value="registerAsAgency">Register</button>
</form>

</div>

</body>
</html>
