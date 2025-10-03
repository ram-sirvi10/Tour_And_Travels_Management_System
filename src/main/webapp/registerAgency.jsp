<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Agency Registration</title>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet">

<style>
body {
    background: #f0f2f5;
    font-family: Arial, sans-serif;
}

.registration-container {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding: 60px 0; /* navbar space */
}

.registration-card {
    background: #fff;
    padding: 35px 30px;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.2);
    width: 100%;
    max-width: 900px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.registration-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 35px rgba(0,0,0,0.3);
}

.registration-card h2 {
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
	margin-bottom: 10px;
}

.profile-preview:hover {
	border-color: #0d6efd;
	transform: scale(1.05);
}

.preview-img {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	object-fit: cover;
}

.fade-alert {
    transition: opacity 0.5s ease;
}

.error {
	color: red;
	font-size: 0.9em;
}
</style>
</head>
<body>

<jsp:include page="navbar.jsp" />

<div class="registration-container">
    <div class="registration-card">

        <div class="text-center mb-3">
            <a href="registerUser.jsp" class="btn btn-outline-primary btn-sm">Register As User</a>
        </div>

        <h2>Agency Registration</h2>

<%
Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
Boolean showOtp = (Boolean) request.getAttribute("showOtp");
boolean lockFields = showOtp != null && showOtp;
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="alert alert-danger fade-alert text-center"><%=errorMessage%></div>
<% } %>

<form action="<%=request.getContextPath()%>/auth" method="post" enctype="multipart/form-data">

<div class="row g-3">
    <!-- Left Column -->
    <div class="col-md-6">

        <!-- Profile Image -->
        <label class="form-label">Profile Image:</label>
        <div class="profile-upload-wrapper">
            <input type="file" name="profileImage" accept="image/*" id="profileImageInput" style="display: none;" <%=lockFields ? "disabled" : ""%>>
            <div class="profile-preview" id="profilePreview" onclick="if(!<%=lockFields%>) document.getElementById('profileImageInput').click();">
                <i class="bi bi-camera" style="font-size: 30px; color: #888;"></i>
                <span style="font-size:12px;">Click to upload</span>
            </div>
            <% if (errors != null && errors.get("profileImage") != null) { %>
                <div class="error"><%=errors.get("profileImage")%></div>
            <% } %>
        </div>

        <input type="text" name="agency_name" class="form-control mb-2" placeholder="Agency Name" value="<%=request.getParameter("agency_name") != null ? request.getParameter("agency_name") : ""%>" <%=lockFields ? "readonly" : ""%>>
        <% if(errors != null && errors.get("agencyName") != null){ %><div class="error"><%=errors.get("agencyName")%></div><% } %>

        <input type="text" name="owner_name" class="form-control mb-2" placeholder="Owner Name" value="<%=request.getParameter("owner_name") != null ? request.getParameter("owner_name") : ""%>" <%=lockFields ? "readonly" : ""%>>
        <% if(errors != null && errors.get("ownerName") != null){ %><div class="error"><%=errors.get("ownerName")%></div><% } %>

        <input type="email" name="email" class="form-control mb-2" placeholder="Email" value="<%=request.getParameter("email") != null ? request.getParameter("email") : ""%>" <%=lockFields ? "readonly" : ""%>>
        <% if(errors != null && errors.get("email") != null){ %><div class="error"><%=errors.get("email")%></div><% } %>

    </div>

    <!-- Right Column -->
    <div class="col-md-6">
        <input type="text" name="country" class="form-control mb-2" placeholder="Country" value="<%=request.getParameter("country") != null ? request.getParameter("country") : ""%>" <%=lockFields ? "readonly" : ""%>>

        <select id="state" name="state" class="form-select mb-2" <%=lockFields ? "disabled" : ""%>></select>
        <% if(errors != null && errors.get("state") != null){ %><div class="error"><%=errors.get("state")%></div><% } %>

        <select id="city" name="city" class="form-select mb-2" <%=lockFields ? "disabled" : ""%>></select>
        <% if(errors != null && errors.get("city") != null){ %><div class="error"><%=errors.get("city")%></div><% } %>

        <input type="text" id="pincode" name="pincode" class="form-control mb-2" readonly value="<%=request.getParameter("pincode") != null ? request.getParameter("pincode") : ""%>">
        <% if(errors != null && errors.get("pincode") != null){ %><div class="error"><%=errors.get("pincode")%></div><% } %>

        <input type="password" name="password" class="form-control mb-2" placeholder="Password" <%=lockFields ? "readonly" : ""%>>
        <% if(errors != null && errors.get("password") != null){ %><div class="error"><%=errors.get("password")%></div><% } %>

        <input type="password" name="confirm_password" class="form-control mb-2" placeholder="Confirm Password" <%=lockFields ? "readonly" : ""%>>
        <% if(errors != null && errors.get("confirmPassword") != null){ %><div class="error"><%=errors.get("confirmPassword")%></div><% } %>

    </div>
</div>

<% if(!lockFields){ %>
    <button type="submit" name="button" value="registerAsAgency" class="btn btn-dark w-100 mt-3">Register</button>
<% } else { %>
    <div class="mt-3">
        <input type="text" name="otp" class="form-control mb-2" placeholder="Enter OTP">
        <button type="submit" name="button" value="verifyOTPAndRegisterAgency" class="btn btn-primary w-100">Verify OTP & Complete Registration</button>
    </div>
<% } %>

<% if(request.getAttribute("message") != null){ %>
    <div class="alert alert-success mt-2 fade-alert"><%=request.getAttribute("message")%></div>
<% } %>
<% if(request.getAttribute("error") != null){ %>
    <div class="alert alert-danger mt-2 fade-alert"><%=request.getAttribute("error")%></div>
<% } %>

</form>
</div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

<script>
$(document).ready(function() {
    $('#state, #city').select2({width: '100%'});

    function fetchData(type, value) {
        let params = new URLSearchParams();
        params.append("type", type);
        params.append("value", value || "");
        return fetch("location", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: params
        }).then(res => res.json());
    }

    fetchData("states").then(data => {
        if(data.data && data.data.states) {
            data.data.states.forEach(s => { $('#state').append(new Option(s.name, s.name)); });
        }
    });

    $('#state').on('change', function() {
        $('#city').empty().append(new Option("Select City", ""));
        let state = this.value;
        if(state) {
            fetchData("cities", state).then(data => {
                if(Array.isArray(data.data)) {
                    data.data.forEach(c => { $('#city').append(new Option(c, c)); });
                }
            });
        }
    });

    $('#city').on('change', function() {
        let city = this.value;
        if(city) {
            fetchData("pincode", city).then(data => {
                if(data[0] && data[0].Status === "Success") {
                    $('#pincode').val(data[0].PostOffice[0].Pincode);
                } else { $('#pincode').val("Not Found"); }
            });
        }
    });
});

// Profile Image Preview
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
        preview.innerHTML = '<i class="bi bi-camera" style="font-size:30px;color:#888;"></i><span>Click to upload</span>';
    }
});

// Auto-hide alerts
document.querySelectorAll('.fade-alert').forEach(alert => {
    setTimeout(() => { alert.style.opacity = 0; setTimeout(() => { alert.style.display = 'none'; }, 500); }, 3000);
});
</script>
</body>
</html>
