<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <form action="AuthServlet" method="post">
        <input type="text" name="agency_name" placeholder="Agency Name" value="<%= agencyName %>">
        <input type="text" name="owner_name" placeholder="Owner Name" value="<%= ownerName %>">
        <input type="email" name="email" placeholder="Email" value="<%= email %>">
        <input type="text" name="phone" placeholder="Phone" value="<%= phone %>">
        <input type="text" name="city" placeholder="City" value="<%= city %>">
        <input type="text" name="state" placeholder="State" value="<%= state %>">
        <input type="text" name="country" placeholder="Country" value="<%= country %>">
        <input type="text" name="pincode" placeholder="Pincode" value="<%= pincode %>">
        <input type="text" name="registration_number" placeholder="Registration Number" value="<%= regNumber %>">
        <input type="password" name="password" placeholder="Password">
        <input type="password" name="confirm_password" placeholder="Confirm Password">
        <button type="submit" name="button" value="registerAsAgency">Register</button>
    </form>
</div>

</body>
</html>
