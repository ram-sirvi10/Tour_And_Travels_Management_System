<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register | TravelMate</title>
<!-- <link rel="stylesheet" href="style.css"> -->

<style>
.profile-upload-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 15px;
}

.profile-preview {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    border: 2px dashed #ccc;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: border-color 0.3s, transform 0.2s;
}

.profile-preview:hover {
    border-color: #0d6efd;
    transform: scale(1.05);
}

.profile-preview i {
    margin-bottom: 5px;
}

.preview-img {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
}
</style>

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

    <form action="<%= request.getContextPath() %>/auth" method="post" enctype="multipart/form-data">
    <label>Profile Image:</label>
<div class="profile-upload-wrapper">
    <input type="file" name="profileImage" accept="image/*" id="profileImageInput" style="display:none;">
    
   
    <div class="profile-preview" id="profilePreview" onclick="document.getElementById('profileImageInput').click();">
        <i class="bi bi-camera" style="font-size: 30px; color: #888;"></i>
        <span>Click to upload</span>
    </div>
</div>
<% if(errors != null && errors.get("profileImage") != null){ %>
    <div class="error"><%= errors.get("profileImage") %></div>
<% } %>
    
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


<script>
    const input = document.getElementById('profileImageInput');
    const preview = document.getElementById('profilePreview');

    input.addEventListener('change', function() {
        if(this.files && this.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                preview.innerHTML = '<img src="'+e.target.result+'" alt="Profile" class="preview-img">';
            }
            reader.readAsDataURL(this.files[0]);
        } else {
            preview.innerHTML = '<i class="bi bi-camera" style="font-size: 30px; color: #888;"></i><span>Click to upload</span>';
        }
    });
</script>

</body>
</html>
