<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
%>
<% 
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage != null && !errorMessage.isEmpty()) {
%>
    <div style="color: red; font-weight: bold; margin: 10px 0;">
        <%= errorMessage %>
    </div>
<% 
    }
%>
<h2 class="mb-4">Explore Our Packages</h2>
<div class="row g-4">
	<%
	if (packages != null && !packages.isEmpty()) {
		for (PackageResponseDTO pkg : packages) {
	%>
	<div class="col-md-4">
		<div class="card h-100 shadow-sm">
			<img
				src="<%=pkg.getImageurl() != null ? pkg.getImageurl() : "images/default.jpg"%>"
				class="card-img-top" alt="<%=pkg.getTitle()%>">
			<div class="card-body">
				<h5 class="card-title fw-bold"><%=pkg.getTitle()%></h5>
				<p class="text-muted mb-1">
					<i class="fas fa-map-marker-alt"></i>
					<%=pkg.getLocation()%></p>
				<p class="mb-1">
					<i class="fas fa-clock"></i>
					<%=pkg.getDuration()%> Days
				</p>
				<p class="mb-1">
					<i class="fas fa-dollar-sign"></i> $<%=pkg.getPrice()%></p>
                <p class="mb-1">
                    <i class="fas fa-chair"></i> Available Seats: <%= pkg.getTotalSeats() %>
                </p>
				<p class="card-text text-truncate"><%=pkg.getDescription()%></p>
				<form action="<%=request.getContextPath()%>/booking" method="post">
				<input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
				<button type="submit" name="button" value="viewBookingForm" class="btn btn-primary w-100 mt-2">Book Now</button>
				</form>
				

			</div>
		</div>
	</div>
	<%
	}
	} else {
	%>
	<p>No packages available.</p>
	<%
	}
	%>
</div>

<%@ include file="footer.jsp"%>
