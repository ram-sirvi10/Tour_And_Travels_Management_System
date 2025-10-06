<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page
	import="java.util.*, com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
UserResponseDTO admin = (UserResponseDTO) session.getAttribute("user");
if (admin == null || !"ADMIN".equals(admin.getUserRole())) {
	response.sendRedirect("login.jsp");
	return;
}

// Get users list from request
List<UserResponseDTO> usersList = (List<UserResponseDTO>) request.getAttribute("usersList");
String currentList = request.getAttribute("listType") != null
		? (String) request.getAttribute("listType")
		: "Manage Users";

int currentPage = request.getAttribute("currentPage") != null ? (Integer) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (Integer) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (Integer) request.getAttribute("pageSize") : 5;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "manageUsers";
if ("Deleted Users".equalsIgnoreCase(currentList)) {
	buttonParam = "deletedUsers";
}
String queryParams = "keyword=" + (request.getParameter("keyword") != null ? request.getParameter("keyword") : "")
		+ "&active=" + (request.getParameter("active") != null ? request.getParameter("active") : "") + "&pageSize="
		+ pageSize;
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Manage Users | Admin Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

</head>
<body>

	<div class="dashboard-container">
		<jsp:include page="sidebar.jsp" />
		<div class="main-content">
			<jsp:include page="header.jsp" />
<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
			<h2>Manage Users</h2>

			<div class="mb-3">
				<form method="get" action="<%=request.getContextPath()%>/admin"
					class="row g-2">
					<input type="hidden" name="button"
						value="<%=request.getParameter("button")%>" />
					<%
					if (!"Deleted Users".equalsIgnoreCase(currentList)) {
					%>
					<div class="col-md-2">
						<select name="active" class="form-select">
							<option value="">All</option>
							<option value="true"
								<%="true".equals(request.getParameter("active")) ? "selected" : ""%>>Active</option>
							<option value="false"
								<%="false".equals(request.getParameter("active")) ? "selected" : ""%>>Inactive</option>
						</select>
					</div>
					<div class="col-md-2">
						<button type="submit" class="btn btn-primary w-100">Apply</button>
					</div>
					<a href="booking?button=<%=request.getParameter("button")%>"
						class="btn btn-secondary w-100">Reset</a>
					<%
					}
					%>
					<div class="mb-3">
						<div class="col-md-4">
							<input type="text" name="keyword" class="form-control"
								placeholder="Search "
								value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>" />
						</div>
						<div class="col-md-2">
							<button type="submit" class="btn btn-primary w-100">Search</button>
						</div>
					</div>
					<a href="booking?button=<%=request.getParameter("button")%>"
						class="btn btn-secondary w-100">Reset</a>
				</form>
			</div>


			<table class="table table-bordered table-striped mt-3">
				<thead class="table-primary">
					<tr>
						<th>S.No</th>
						<th>Profile Image</th>
						<th>Name</th>
						<th>Email</th>
						<th>Register Date</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<%
					int serial = 1;
					if (usersList != null && !usersList.isEmpty()) {
						for (UserResponseDTO user : usersList) {
					%>
					<tr>
						<td><%=serial++%></td>
						<td>
							<%
							if (user.getImageurl() != null && !user.getImageurl().isEmpty()) {
							%> <img src="<%=user.getImageurl()%>" alt="Profile"
							class="table-profile-img"
							style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover; cursor: pointer;"
							onclick="showProfileImageModal('<%=user.getImageurl()%>')">

							<%
							} else {
							%> <i class="bi bi-person-circle" style="font-size: 1.5rem;"></i>
							<%
							}
							%>
						</td>
						<td><%=user.getUserName()%></td>
						<td><%=user.getUserEmail()%></td>
						<td><%=(user.getCreatedAt() != null) ? user.getCreatedAt().toString() : "-"%></td>
						<td>
							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="userAction"> <input
									type="hidden" name="userId" value="<%=user.getUserId()%>">
								<input type="hidden" name="keyword"
									value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
								<input type="hidden" name="active"
									value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">
								<input type="hidden" name="pageSize" value="<%=pageSize%>">

								<button type="submit" name="action"
									value="<%=user.isActive() ? "deactivate" : "activate"%>"
									class="btn btn-sm btn-primary">
									<%=user.isActive() ? "Deactivate" : "Activate"%>
								</button>
							</form>

							<form method="post" action="<%=request.getContextPath()%>/admin"
								style="display: inline;">
								<input type="hidden" name="button" value="userAction"> <input
									type="hidden" name="userId" value="<%=user.getUserId()%>">
								<input type="hidden" name="keyword"
									value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
								<input type="hidden" name="active"
									value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">
								<input type="hidden" name="pageSize" value="<%=pageSize%>">

								<button type="submit" name="action" value="delete"
									class="btn btn-sm btn-danger"
									onclick="return confirm('Are you sure you want to permanently delete this user?');">
									Delete</button>
							</form>
						</td>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="5" class="text-center">No users found</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>

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


			<%

			%>
			<form method="post" action="admin">
				<input type="hidden" name="button" value="<%=buttonParam%>">
				<input type="hidden" name="keyword"
					value="<%=request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
				<input type="hidden" name="active"
					value="<%=request.getParameter("active") != null ? request.getParameter("active") : ""%>">

				<label for="pageSize">Records per page:</label> <select
					name="pageSize" id="pageSize" onchange="this.form.submit()">
					<option value="5" <%=pageSize == 5 ? "selected" : ""%>>5</option>
					<option value="10" <%=pageSize == 10 ? "selected" : ""%>>10</option>
					<option value="20" <%=pageSize == 20 ? "selected" : ""%>>20</option>
					<option value="50" <%=pageSize == 50 ? "selected" : ""%>>50</option>
				</select>
			</form>

			<%-- Pagination Section --%>
			<nav>
				<ul class="pagination justify-content-center">
					<%
					if (currentPage > 1) {
					%>
					<li class="page-item"><a class="page-link"
						href="?<%=queryParams%>&page=<%=currentPage - 1%>&button=<%=buttonParam%>">Prev</a>
					</li>
					<%
					}
					%>

					<%
					for (int i = startPage; i <= endPage; i++) {
					%>
					<li class="page-item <%=(i == currentPage) ? "active" : ""%>">
						<a class="page-link"
						href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
					</li>
					<%
					}
					%>

					<%
					if (currentPage < totalPages) {
					%>
					<li class="page-item"><a class="page-link"
						href="?<%=queryParams%>&page=<%=currentPage + 1%>&button=<%=buttonParam%>">Next</a>
					</li>
					<%
					}
					%>
				</ul>
			</nav>


		</div>

	</div>
	<script>
		function showProfileImageModal(imageUrl) {
			var modalImg = document.getElementById('modalProfileImage');
			modalImg.src = imageUrl;

			var myModal = new bootstrap.Modal(document
					.getElementById('profileImageModal'));
			myModal.show();
		}
	</script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
