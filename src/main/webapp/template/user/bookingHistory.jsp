<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>

<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ include file="header.jsp"%>

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
<h2 class="mb-4">My Booking History</h2>

<div class="row g-4">
	<%
	List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
	if (bookings != null && !bookings.isEmpty()) {
		for (BookingResponseDTO booking : bookings) {
	%>
	<div class="col-md-6">
		<div class="card shadow-sm">
			<div class="row g-0">
				<div class="col-md-4">
					<img src="<%=booking.getPackageImage()%>"
						class="img-fluid rounded-start"
						alt="<%=booking.getPackageName()%>">
				</div>
				<div class="col-md-8">
					<div class="card-body">
						<h5 class="card-title fw-bold"><%=booking.getPackageName()%></h5>
						<p class="mb-1">
							<i class="fas fa-clock"></i>
							<%=booking.getDuration()%>
							Days
						</p>
						<p class="mb-1">
							<i class="fas fa-users"></i>
							<%=booking.getNoOfTravellers()%>
							Travelers
						</p>
						<p class="mb-1">
							<i class="fas fa-dollar-sign"></i> $<%=booking.getAmount()%></p>

						<%
						String statusClass = "bg-danger";
						if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
							statusClass = "bg-success";
						} else if ("PENDING".equalsIgnoreCase(booking.getStatus())) {
							statusClass = "bg-warning text-dark";
						}
						%>
						<span class="badge <%=statusClass%>"><%=booking.getStatus()%></span>

						<a
							href="<%=request.getContextPath() %>/booking?button=viewTravelers&bookingId=<%=booking.getBookingId()%>"
							class="btn btn-sm btn-primary float-end">View Travelers</a>

					</div>
				</div>
			</div>
		</div>
	</div>
	<%
	}
	} else {
	%>
	<p class="text-center">No bookings found.</p>
	<%
	}
	%>
</div>

<%@ include file="footer.jsp"%>
