<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>

<%
String agencyName = request.getParameter("agency_name") != null ? request.getParameter("agency_name") : "";
String ownerName = request.getParameter("owner_name") != null ? request.getParameter("owner_name") : "";
String email = request.getParameter("email") != null ? request.getParameter("email") : "";
String phone = request.getParameter("phone") != null ? request.getParameter("phone") : "";
String city = request.getParameter("city") != null ? request.getParameter("city") : "";
String state = request.getParameter("state") != null ? request.getParameter("state") : "";
String country = request.getParameter("country") != null ? request.getParameter("country") : "";
String pincode = request.getParameter("pincode") != null ? request.getParameter("pincode") : "";
String regNumber = request.getParameter("registration_number") != null
		? request.getParameter("registration_number")
		: "";

Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");

Boolean showOtp = (Boolean) request.getAttribute("showOtp");
boolean lockFields = showOtp != null && showOtp;
%>
<!DOCTYPE html>
<html>
<head>
<title>Agency Registration</title>
<link
	href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
	rel="stylesheet" />
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

.error {
	color: red;
	font-size: 0.9em;
}
</style>
</head>
<body>

	<jsp:include page="navbar.jsp" />

	<div class="container">
		<div style="text-align: center; margin-bottom: 20px;">
			<a href="registerUser.jsp" class="btn btn-outline-primary">Register
				As User</a>
		</div>
		<h2>Agency Registration</h2>

		<%
		String errorMessage = (String) session.getAttribute("errorMessage");
		session.removeAttribute("errorMessage");
		if (errorMessage == null)
			errorMessage = (String) request.getAttribute("errorMessage");
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

		<form action="<%=request.getContextPath()%>/auth" method="post"
			enctype="multipart/form-data">

			<label>Profile Image:</label>
			<div class="profile-upload-wrapper">
				<input type="file" name="profileImage" accept="image/*"
					id="profileImageInput" style="display: none;"
					<%=lockFields ? "disabled" : ""%>>
				<div class="profile-preview" id="profilePreview"
					onclick="if(!<%=lockFields%>) document.getElementById('profileImageInput').click();">
					<i class="bi bi-camera" style="font-size: 30px; color: #888;"></i>
					<span>Click to upload</span>
				</div>
			</div>
			<%
			if (errors != null && errors.get("profileImage") != null) {
			%>
			<div class="error"><%=errors.get("profileImage")%></div>
			<%
			}
			%>

			<input type="text" name="agency_name" placeholder="Agency Name"
				value="<%=agencyName%>" <%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("agencyName") != null) {
			%>
			<div class="error"><%=errors.get("agencyName")%></div>
			<%
			}
			%>

			<input type="text" name="owner_name" placeholder="Owner Name"
				value="<%=ownerName%>" <%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("ownerName") != null) {
			%>
			<div class="error"><%=errors.get("ownerName")%></div>
			<%
			}
			%>

			<input type="email" name="email" placeholder="Email"
				value="<%=email%>" <%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("email") != null) {
			%>
			<div class="error"><%=errors.get("email")%></div>
			<%
			}
			%>

			<input type="text" name="phone" placeholder="Phone"
				value="<%=phone%>" <%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("phone") != null) {
			%>
			<div class="error"><%=errors.get("phone")%></div>
			<%
			}
			%>

			<select id="state" name="state" style="width: 300px;"
				<%=lockFields ? "disabled" : ""%>></select>
			<%
			if (errors != null && errors.get("state") != null) {
			%>
			<div class="error"><%=errors.get("state")%></div>
			<%
			}
			%>

			<select id="city" name="city" style="width: 300px;"
				<%=lockFields ? "disabled" : ""%>></select>
			<%
			if (errors != null && errors.get("city") != null) {
			%>
			<div class="error"><%=errors.get("city")%></div>
			<%
			}
			%>

			<input type="text" id="pincode" name="pincode" readonly
				value="<%=pincode%>">
			<%
			if (errors != null && errors.get("pincode") != null) {
			%>
			<div class="error"><%=errors.get("pincode")%></div>
			<%
			}
			%>

			<input type="text" name="registration_number"
				placeholder="Registration Number" value="<%=regNumber%>"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("registrationNumber") != null) {
			%>
			<div class="error"><%=errors.get("registrationNumber")%></div>
			<%
			}
			%>

			<input type="password" name="password" placeholder="Password"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("password") != null) {
			%>
			<div class="error"><%=errors.get("password")%></div>
			<%
			}
			%>

			<input type="password" name="confirm_password"
				placeholder="Confirm Password" <%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("confirmPassword") != null) {
			%>
			<div class="error"><%=errors.get("confirmPassword")%></div>
			<%
			}
			%>

			<%
			if (!lockFields) {
			%>
			<button type="submit" name="button" value="registerAsAgency"
				class="btn btn-dark w-100 mt-3">Register</button>
			<%
			}
			%>

			<!-- OTP Section -->
			<%
			if (lockFields) {
			%>
			<div class="mt-3">
				<input type="text" name="otp" placeholder="Enter OTP"
					class="form-control mb-2">
				<button type="submit" name="button"
					value="verifyOTPAndRegisterAgency" class="btn btn-primary w-100">Verify
					OTP & Complete Registration</button>
			</div>
			<%
			}
			%>

			<%-- Success/Error Messages --%>
			<%
			if (request.getAttribute("message") != null) {
			%>
			<div class="alert alert-success mt-2"><%=request.getAttribute("message")%></div>
			<%
			}
			if (request.getAttribute("error") != null) {
			%>
			<div class="alert alert-danger mt-2"><%=request.getAttribute("error")%></div>
			<%
			}
			%>

		</form>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

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
</script>

</body>
</html>
