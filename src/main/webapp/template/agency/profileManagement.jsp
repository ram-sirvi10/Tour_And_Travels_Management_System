<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO"%>
<%@ page import="java.util.Map"%>

<%
AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");
if (agency == null) {
	response.sendRedirect("login.jsp");
	return;
}

Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
String successMessage = (String) request.getAttribute("successMessage");
String errorMessage = (String) request.getAttribute("errorMessage");
String actionType = request.getParameter("button"); 
AgencyRegisterRequestDTO formData = (AgencyRegisterRequestDTO) request.getAttribute("formData");
%>

<jsp:include page="header.jsp" />
<link
	href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
	rel="stylesheet" />
<style>
.profile-upload-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
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
	margin: auto;
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

.profile-card {
	background: #ffffff;
	transition: transform 0.3s, box-shadow 0.3s;
}

.profile-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
}

.profile-view-img {
	width: 150px;
	height: 150px;
	border-radius: 50%;
	object-fit: cover;
	cursor: pointer;
	border: 4px solid #0d6efd;
	transition: transform 0.2s;
}

.profile-view-img:hover {
	transform: scale(1.05);
}

.profile-view-icon {
	font-size: 150px;
	color: #0d6efd;
}
</style>
<div class="container mt-4">
	<%-- Success/Error Messages --%>
	<%
	if (successMessage != null) {
	%>
	<div class="alert alert-success alert-dismissible fade show"
		role="alert">
		<%=successMessage%>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<%
	}
	%>
	<%
	if (errorMessage != null) {
	%>
	<div class="alert alert-danger alert-dismissible fade show"
		role="alert">
		<%=errorMessage%>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<%
	}
	%>

	<%-- VIEW PROFILE --%>
	<%
	if ("viewProfile".equals(actionType)) {
	%>
	<div class="d-flex justify-content-center align-items-center vh-100">
		<div class="card profile-card p-5 shadow-lg"
			style="width: 450px; border-radius: 20px;">
			<div class="d-flex flex-column align-items-center">
				<%
				System.out.println(agency.getImageurl());
				if (agency.getImageurl() != null && !agency.getImageurl().isEmpty()) {
				%>
				<!-- Only change: add return false to onclick -->
				<img src="<%=agency.getImageurl()%>" alt="Profile"
					class="profile-view-img mb-4"
					onclick="showProfileModal('<%=agency.getImageurl()%>'); return false;">
				<%
				} else {
				%>
				<i class="bi bi-person-circle profile-view-icon mb-4"
					style="font-size: 80px;"></i>
				<%
				}
				%>
				<h3 class="card-title mb-2 text-center"><%=agency.getAgencyName().toUpperCase()%></h3>
				<h2 class="card-title mb-2 text-center"><%=agency.getOwnerName().toUpperCase()%></h2>
				<p class="text-muted mb-3 text-center"><%=agency.getEmail()%></p>
				<div class="d-flex gap-2">
					<a
						href="<%=request.getContextPath()%>/agency?button=updateProfilePage"
						class="btn btn-outline-primary btn-lg">Edit Profile</a> <a
						href="<%=request.getContextPath()%>/template/agency/profileManagement.jsp?button=changePassword"
						class="btn btn-outline-secondary btn-lg">Change Password</a> <a
						href="<%=request.getContextPath()%>/auth?button=logout"
						class="btn btn-danger btn-lg"
						onclick="event.preventDefault(); 
            fetch(this.href, {method:'POST'}).then(()=>{window.location='<%=request.getContextPath()%>/login.jsp'});">
						Logout </a>

				</div>

			</div>
		</div>
	</div>

	<%
	} else if ("changePassword".equals(actionType)) {
	%>
	<div class="d-flex justify-content-center mt-5">
		<div class="card profile-card p-4 shadow-lg"
			style="width: 450px; border-radius: 20px;">
			<h3 class="text-center mb-4">Change Password</h3>

			<form method="post" action="<%=request.getContextPath()%>/agency">


				<div class="mb-3">
					<label class="form-label">Old Password</label> <input
						type="password" name="oldPassword" class="form-control">
					<%
					if (errors != null && errors.get("oldPassword") != null) {
					%>
					<div class="text-danger"><%=errors.get("oldPassword")%></div>
					<%
					}
					%>
				</div>

				<div class="mb-3">
					<label class="form-label">New Password</label> <input
						type="password" name="newPassword" class="form-control">
					<%
					if (errors != null && errors.get("newPassword") != null) {
					%>
					<div class="text-danger"><%=errors.get("newPassword")%></div>
					<%
					}
					%>
				</div>

				<div class="mb-3">
					<label class="form-label">Confirm Password</label> <input
						type="password" name="confirmPassword" class="form-control">
					<%
					if (errors != null && errors.get("confirmPassword") != null) {
					%>
					<div class="text-danger"><%=errors.get("confirmPassword")%></div>
					<%
					}
					%>
				</div>

				<div class="d-flex justify-content-between">
					<button type="submit" name="button" value="changePassword"
						class="btn btn-primary">Change Password</button>
					<a href="<%=request.getContextPath()%>/agency?button=viewProfile"
						class="btn btn-secondary">Cancel</a>
				</div>
			</form>
		</div>
	</div>
	<%
	}
	%>
	<!-- Profile Image Modal -->
	<div class="modal fade" id="profileImageModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-body text-center">
					<img id="modalProfileImage" src="" alt="Profile Image"
						style="max-width: 100%; max-height: 500px; border-radius: 8px;">
				</div>
			</div>
		</div>
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
    }
});

function removeProfileImage() {
    input.value = "";
    preview.innerHTML = '<i class="bi bi-person-circle" style="font-size: 50px; color: #888;"></i>';
}

function showProfileModal(imageUrl) {
    var modalImg = document.getElementById('modalProfileImage');
    if (!modalImg) return;
    modalImg.src = imageUrl;
    var myModal = new bootstrap.Modal(document.getElementById('profileImageModal'));
    myModal.show();
}
</script>

	<jsp:include page="footer.jsp" />