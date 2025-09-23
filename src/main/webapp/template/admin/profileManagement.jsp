<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page import="com.travelmanagement.dto.requestDTO.RegisterRequestDTO"%>
<%@ page import="java.util.Map"%>

<%
UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
if(user == null || !"ADMIN".equals(user.getUserRole())){
    response.sendRedirect("login.jsp");
    return;
}

Map<String,String> errors = (Map<String,String>) request.getAttribute("errors");
String successMessage = (String) request.getAttribute("successMessage");
String errorMessage = (String) request.getAttribute("errorMessage");
String actionType = request.getParameter("button");
RegisterRequestDTO formData = (RegisterRequestDTO) request.getAttribute("formData");
%>

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
</style>

<jsp:include page="header.jsp" />
<div class="dashboard-container">
    <jsp:include page="sidebar.jsp" />

    <div class="main-content">
        <%-- Success/Error Messages --%>
        <% if (successMessage != null) { %>
            <div class="alert alert-success"><%= successMessage %></div>
        <% } %>
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger"><%= errorMessage %></div>
        <% } %>

        <%-- VIEW PROFILE --%>
        <% if("viewProfile".equals(actionType)) { %>
            <div class="d-flex justify-content-center align-items-center vh-100">
                <div class="card profile-card p-5 shadow-lg" style="width: 450px; border-radius: 20px;">
                    <div class="d-flex flex-column align-items-center">
                        <% if(user.getImageurl() != null && !user.getImageurl().isEmpty()) { %>
                            <img src="<%= user.getImageurl() %>" alt="Profile" 
                                 class="profile-view-img mb-4"
                                 onclick="showProfileModal('<%= user.getImageurl() %>')">
                        <% } else { %>
                            <i class="bi bi-person-circle profile-view-icon mb-4" style="font-size: 80px;"></i>
                        <% } %>
                        <h3 class="card-title mb-2 text-center"><%= user.getUserName().toUpperCase() %></h3>
                        <p class="text-muted mb-3 text-center"><%= user.getUserEmail() %></p>
                        <div class="d-flex gap-3">
                            <a href="<%=request.getContextPath()%>/template/admin/profileManagement.jsp?button=updateProfile" 
                               class="btn btn-outline-primary btn-lg">Edit Profile</a>
                            <a href="<%=request.getContextPath()%>/template/admin/profileManagement.jsp?button=changePassword"  
                               class="btn btn-outline-secondary btn-lg">Change Password</a>
                        </div>
                    </div>
                </div>
            </div>

        <%-- UPDATE PROFILE --%>
        <% } else if ("updateProfile".equals(actionType)) { %>
            <div class="d-flex justify-content-center mt-5">
                <div class="card profile-card p-4 shadow-lg" style="width: 450px; border-radius: 20px;">
                    <h3 class="text-center mb-4">Edit Profile</h3>

                    <form method="post" action="<%= request.getContextPath() %>/admin" enctype="multipart/form-data">
                      

                        <div class="profile-upload-wrapper mb-3 text-center">
                            <label class="form-label mb-2">Update Pic</label>
                            <input type="file" name="profileImage" id="profileImageInput" accept="image/*" style="display:none;">
                            <div class="profile-preview" id="profilePreview">
                                <% if (user.getImageurl() != null && !user.getImageurl().isEmpty()) { %>
                                    <img src="<%= user.getImageurl() %>" alt="Profile" class="preview-img">
                                <% } else { %>
                                    <i class="bi bi-person-circle" style="font-size: 50px; color: #888;"></i>
                                <% } %>
                            </div>
                            <div class="d-flex justify-content-center gap-2 mt-2">
                                <button type="button" class="btn btn-sm btn-outline-primary" onclick="document.getElementById('profileImageInput').click();">Upload</button>
                                <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeProfileImage();">Remove</button>
                            </div>
                            <% if (errors != null && errors.get("profileImage") != null) { %>
                                <div class="text-danger mt-1"><%= errors.get("profileImage") %></div>
                            <% } %>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" name="userName" class="form-control" value="<%= formData != null ? formData.getUsername() : user.getUserName() %>">
                            <% if (errors != null && errors.get("userName") != null) { %>
                                <div class="text-danger"><%= errors.get("userName") %></div>
                            <% } %>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="userEmail" class="form-control" value="<%= formData != null ? formData.getEmail() : user.getUserEmail() %>">
                            <% if (errors != null && errors.get("userEmail") != null) { %>
                                <div class="text-danger"><%= errors.get("userEmail") %></div>
                            <% } %>
                        </div>

                        <div class="d-flex justify-content-between">
                        <input type="hidden" name="button" value="updateProfile">
                            <button type="submit"  name="button" value="updateProfile" class="btn btn-primary">Update</button>
                            <a href="<%= request.getContextPath() %>/admin?button=dashboard" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>

        <%-- CHANGE PASSWORD --%>
        <% } else if ("changePassword".equals(actionType)) { %>
            <div class="d-flex justify-content-center mt-5">
                <div class="card profile-card p-4 shadow-lg" style="width: 450px; border-radius: 20px;">
                    <h3 class="text-center mb-4">Change Password</h3>

                    <form method="post" action="<%=request.getContextPath()%>/admin">
                     

                        <div class="mb-3">
                            <label class="form-label">Old Password</label>
                            <input type="password" name="oldPassword" class="form-control" >
                            <% if (errors != null && errors.get("oldPassword") != null) { %>
                                <div class="text-danger"><%= errors.get("oldPassword") %></div>
                            <% } %>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">New Password</label>
                            <input type="password" name="newPassword" class="form-control">
                            <% if (errors != null && errors.get("newPassword") != null) { %>
                                <div class="text-danger"><%= errors.get("newPassword") %></div>
                            <% } %>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Confirm Password</label>
                            <input type="password" name="confirmPassword" class="form-control">
                            <% if (errors != null && errors.get("confirmPassword") != null) { %>
                                <div class="text-danger"><%= errors.get("confirmPassword") %></div>
                            <% } %>
                        </div>

                        <div class="d-flex justify-content-between">
                            <button type="submit" name="button" value="changePassword" class="btn btn-primary">Change Password</button>
                            <a href="<%=request.getContextPath()%>/admin?button=dashboard" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        <% } %>
    </div>
</div>

<!-- Profile Image Modal -->
<div class="modal fade" id="profileImageModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-body text-center">
        <img id="modalProfileImage" src="" alt="Profile Image" style="max-width:100%; max-height:500px; border-radius:8px;">
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
