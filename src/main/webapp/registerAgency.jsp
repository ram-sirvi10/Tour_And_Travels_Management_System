<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>

<%
    String error = (String) request.getAttribute("error");
    String agencyName =  request.getParameter("agency_name") != null ? request.getParameter("agency_name") : "" ;
    String ownerName = request.getParameter("owner_name") != null ? request.getParameter("owner_name") : "" ;
    String email = request.getParameter("email") != null ? request.getParameter("email") : "" ;
    String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "" ;
    String city = request.getParameter("city") != null ? request.getParameter("city") : "" ;
    String state = request.getParameter("state") != null ? request.getParameter("state") : "" ;
    String country = request.getParameter("country") != null ? request.getParameter("country") : "" ;
    String pincode = request.getParameter("pincode") != null ? request.getParameter("pincode") : "" ;
    String regNumber = request.getParameter("registration_number") != null ? request.getParameter("registration_number") : "" ;
%>
<!DOCTYPE html>
<html>
<head>
  <title>Agency Registration</title>
<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
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

 <!-- Country/State/City Dropdown -->
<label>Country</label>
<select id="country" name="country" style="width:300px;" >
  <option value="India" selected>India</option>
</select>


<label>State</label>
<select id="state" name="state" style="width:300px;">
  <option value="">Select State</option>
</select>
<% if (errors != null && errors.get("state") != null) { %>
  <div class="error"><%= errors.get("state") %></div>
<% } %>

<label>City</label>
<select id="city" name="city" style="width:300px;">
  <option value="">Select City</option>
</select>
<% if (errors != null && errors.get("city") != null) { %>
  <div class="error"><%= errors.get("city") %></div>
<% } %>

<label>Pincode</label>
<input type="text" id="pincode" name="pincode" readonly value="<%= pincode %>">
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

<!-- Select2 for searchable dropdown -->

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>




<script>
$(document).ready(function() {
    $('#state, #city').select2();

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

    // Load states
    fetchData("states").then(data => {
        if(data.data && data.data.states) {
            data.data.states.forEach(s => {
                $('#state').append(new Option(s.name, s.name));
            });
        }
    });

    // Load cities when state changes
    $('#state').on('change', function() {
        $('#city').empty().append(new Option("Select City", ""));
        let state = this.value;

        if(state) {
            fetchData("cities", state).then(data => {
                if(Array.isArray(data.data)) {
                    data.data.forEach(c => {
                        $('#city').append(new Option(c, c));
                    });
                }
            });
        }
    });

    // Load pincode when city changes
    $('#city').on('change', function() {
        let city = this.value;
        if(city) {
            fetchData("pincode", city).then(data => {
                if(data[0] && data[0].Status === "Success") {
                    $('#pincode').val(data[0].PostOffice[0].Pincode);
                } else {
                    $('#pincode').val("Not Found");
                }
            });
        }
    });
});


// for profile image

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
