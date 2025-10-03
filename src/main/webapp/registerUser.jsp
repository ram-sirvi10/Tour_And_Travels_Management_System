<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register | TravelMate</title>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body {
    background: #f0f2f5;
    font-family: Arial, sans-serif;
}

.register-container {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 60px; /* navbar ke liye */
}

.register-card {
    background: #fff;
    padding: 30px 25px;
    border-radius: 15px;
    box-shadow: 0 8px 25px rgba(0,0,0,0.2);
    width: 100%;
    max-width: 420px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.register-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0,0,0,0.3);
}

.register-card h2 {
    text-align: center;
    margin-bottom: 25px;
    font-weight: 700;
}

.profile-upload-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 15px;
}

.profile-preview {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    border: 2px dashed #ccc;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: border-color 0.3s, transform 0.2s;
    margin-bottom: 10px;
}

.profile-preview:hover {
    border-color: #0d6efd;
    transform: scale(1.05);
}

.preview-img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    object-fit: cover;
}

.fade-alert {
    transition: opacity 0.5s ease;
}
</style>
</head>
<body>

<jsp:include page="navbar.jsp" />

<div class="register-container">
    <div class="register-card">

        <h2>Register User</h2>

        <div class="text-center mb-3">
            <a href="registerAgency.jsp" class="btn btn-outline-primary btn-sm">Register As Company</a>
        </div>

<%
Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
Boolean showOtp = (Boolean) request.getAttribute("showOtp");
boolean lockFields = showOtp != null && showOtp;
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) errorMessage = (String) request.getAttribute("errorMessage");
%>

<!-- Error Alert -->
<% if (errorMessage != null && !errorMessage.isEmpty()) { %>
<div id="errorAlert" class="alert alert-danger text-center fade-alert">
    <%= errorMessage %>
</div>
<% } %>

<form action="<%=request.getContextPath()%>/auth" method="post" enctype="multipart/form-data">

    <!-- Profile Image -->
    <label class="form-label">Profile Image:</label>
    <div class="profile-upload-wrapper">
        <input type="file" name="profileImage" accept="image/*" id="profileImageInput" style="display:none;" <%=lockFields ? "disabled" : ""%>>
        <div class="profile-preview" id="profilePreview" onclick="if(!<%=lockFields%>) document.getElementById('profileImageInput').click();">
            <i class="bi bi-camera" style="font-size: 24px; color: #888;"></i>
            <span style="font-size: 12px;">Click to upload</span>
        </div>
        <% if(errors != null && errors.get("profileImage") != null) { %>
            <div class="text-danger mb-2"><%= errors.get("profileImage") %></div>
        <% } %>
    </div>

    <!-- Name -->
    <input type="text" name="name" class="form-control mb-2" placeholder="Name"
           value="<%=request.getParameter("name") != null ? request.getParameter("name") : ""%>"
           <%=lockFields ? "readonly" : ""%>>
    <% if(errors != null && errors.get("username") != null){ %>
        <div class="text-danger mb-2"><%= errors.get("username") %></div>
    <% } %>

    <!-- Email -->
    <input type="email" name="email" class="form-control mb-2" placeholder="Email"
           value="<%=request.getParameter("email") != null ? request.getParameter("email") : ""%>"
           <%=lockFields ? "readonly" : ""%>>
    <% if(errors != null && errors.get("email") != null){ %>
        <div class="text-danger mb-2"><%= errors.get("email") %></div>
    <% } %>

    <!-- Password -->
    <input type="password" name="password" class="form-control mb-2" placeholder="Password"
           <%=lockFields ? "readonly" : ""%>>
    <% if(errors != null && errors.get("password") != null){ %>
        <div class="text-danger mb-2"><%= errors.get("password") %></div>
    <% } %>

    <!-- Confirm Password -->
    <input type="password" name="confirmPassword" class="form-control mb-2" placeholder="Confirm Password"
           <%=lockFields ? "readonly" : ""%>>
    <% if(errors != null && errors.get("confirmPassword") != null){ %>
        <div class="text-danger mb-2"><%= errors.get("confirmPassword") %></div>
    <% } %>

    <!-- Register / OTP buttons -->
    <% if(!lockFields){ %>
        <button type="submit" name="button" value="registerAsUser" class="btn btn-dark w-100 mt-3">Register</button>
    <% } else { %>
        <div class="mt-3">
            <input type="text" name="otp" placeholder="Enter OTP" class="form-control mb-2">
            <button type="submit" name="button" value="verifyOTPAndRegisterUser" class="btn btn-primary w-100">Verify OTP & Complete Registration</button>
        </div>
    <% } %>

    <!-- Success/Error messages -->
    <% if(request.getAttribute("message") != null){ %>
        <div id="successAlert" class="alert alert-success mt-2 fade-alert"><%=request.getAttribute("message")%></div>
    <% } %>
    <% if(request.getAttribute("error") != null){ %>
        <div id="errorAlert2" class="alert alert-danger mt-2 fade-alert"><%=request.getAttribute("error")%></div>
    <% } %>

</form>
</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
const input = document.getElementById('profileImageInput');
const preview = document.getElementById('profilePreview');

input.addEventListener('change', function() {
    if(this.files && this.files[0]){
        const reader = new FileReader();
        reader.onload = function(e){
            preview.innerHTML = '<img src="'+e.target.result+'" alt="Profile" class="preview-img">';
        }
        reader.readAsDataURL(this.files[0]);
    } else {
        preview.innerHTML = '<i class="bi bi-camera" style="font-size: 24px; color: #888;"></i><span style="font-size: 12px;">Click to upload</span>';
    }
});

// Auto-hide alerts after 3 seconds
const fadeAlerts = document.querySelectorAll('.fade-alert');
fadeAlerts.forEach(alert => {
    setTimeout(() => {
        alert.style.opacity = 0;
        setTimeout(() => { alert.style.display = 'none'; }, 500);
    }, 3000);
});
</script>
</body>
</html>
