<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page import="java.util.Map"%>

<%
    UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
    Map<String,String> errors = (Map<String,String>) request.getAttribute("errors");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String actionType = request.getParameter("button"); 
%>

<jsp:include page="header.jsp" />

<div class="dashboard-container">
    <jsp:include page="sidebar.jsp" />

    <div class="main-content">
        <% if("viewProfile".equals(actionType)) { %>
            <div class="card">
                <h2>Profile Info</h2>
                <p><strong>Name:</strong> <%= user.getUserName() %></p>
                <p><strong>Email:</strong> <%= user.getUserEmail() %></p>
                <p><strong>Role:</strong> <%= user.getUserRole() %></p>
            </div>
        <% } else if("updateProfile".equals(actionType) || "changePassword".equals(actionType)) { %>
            <div class="card">
                <h2><%= "updateProfile".equals(actionType) ? "Edit Profile" : "Change Password" %></h2>
                <form method="post" action="<%=request.getContextPath()%>/admin">
                    <input type="hidden" name="button" value="<%= actionType %>" />
                    <% if("updateProfile".equals(actionType)) { %>
                        <div class="mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" name="userName" class="form-control" value="<%= user.getUserName() %>">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="userEmail" class="form-control" value="<%= user.getUserEmail() %>">
                        </div>
                    <% } else { %>
                        <div class="mb-3">
                            <label class="form-label">New Password</label>
                            <input type="password" name="newPassword" class="form-control">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Confirm Password</label>
                            <input type="password" name="confirmPassword" class="form-control">
                        </div>
                    <% } %>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-primary">
                            <%= "updateProfile".equals(actionType) ? "Update" : "Change Password" %>
                        </button>
                        <a href="<%=request.getContextPath()%>/admin?button=dashboard" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        <% } %>
    </div>
</div>
