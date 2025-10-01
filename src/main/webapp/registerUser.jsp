<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Register | TravelMate</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">

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

	<div class="container mt-4">
		<div class="text-center mb-3">
			<a href="registerAgency.jsp" class="btn btn-outline-primary">Register
				As Company</a>
		</div>

		<h2 class="mb-3">Register User</h2>

		<%
		Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
		Boolean showOtp = (Boolean) request.getAttribute("showOtp");
		boolean lockFields = showOtp != null && showOtp; // lock fields after OTP sent
		%>

		<form action="<%=request.getContextPath()%>/auth" method="post"
			enctype="multipart/form-data"
			class="border p-4 rounded shadow-sm bg-light">

			<!-- Profile Image -->
			<label class="form-label">Profile Image:</label>
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

			<!-- Name -->
			<label class="form-label">Name:</label> <input type="text"
				name="name" class="form-control mb-2"
				value="<%=request.getParameter("name") != null ? request.getParameter("name") : ""%>"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("username") != null) {
			%>
			<div class="error"><%=errors.get("username")%></div>
			<%
			}
			%>

			<!-- Email -->
			<label class="form-label">Email:</label> <input type="email"
				name="email" class="form-control mb-2"
				value="<%=request.getParameter("email") != null ? request.getParameter("email") : ""%>"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("email") != null) {
			%>
			<div class="error"><%=errors.get("email")%></div>
			<%
			}
			%>

			<!-- Password -->
			<label class="form-label">Password:</label> <input type="password"
				name="password" class="form-control mb-2"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("password") != null) {
			%>
			<div class="error"><%=errors.get("password")%></div>
			<%
			}
			%>

			<!-- Confirm Password -->
			<label class="form-label">Confirm Password:</label> <input
				type="password" name="confirmPassword" class="form-control mb-2"
				<%=lockFields ? "readonly" : ""%>>
			<%
			if (errors != null && errors.get("confirmPassword") != null) {
			%>
			<div class="error"><%=errors.get("confirmPassword")%></div>
			<%
			}
			%>

			<!-- Register Button -->
			<%
			if (!lockFields) {
			%>
			<button type="submit" name="button" value="registerAsUser"
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
				<button type="submit" name="button" value="verifyOTPAndRegisterUser"
					class="btn btn-primary w-100">Verify OTP & Complete
					Registration</button>
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
