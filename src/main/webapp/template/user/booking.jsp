<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>

<%
PackageResponseDTO pkg = (PackageResponseDTO) session.getAttribute("package");
int availableSeats = (pkg != null) ? pkg.getTotalSeats() : 0;

Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");

String noOfTravelersStr = request.getParameter("noOfTravellers");
if (noOfTravelersStr == null || noOfTravelersStr.isEmpty()) {
	noOfTravelersStr = "1";
}
int travelersCount = Integer.parseInt(noOfTravelersStr);
if (travelersCount > availableSeats) {
	travelersCount = availableSeats;
}

user = (UserResponseDTO) session.getAttribute("user");

String errorMessage = (String) request.getAttribute("errorMessage");
%>

<%
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-danger border-0"
		role="alert">
		<div class="d-flex">
			<div class="toast-body">
				<%=errorMessage%>
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast"></button>
		</div>
	</div>
</div>
<%
}
%>


<%
if (pkg != null) {
%>
<div class="alert alert-warning shadow-sm mb-4">
	<h5 class="fw-bold text-danger">Cancellation & Refund Policy</h5>
	<p class="mb-2">
		<strong>Note:</strong> All refunds will be calculated on the base
		amount (after deducting <strong>18% GST</strong>). GST is
		non-refundable.
	</p>
	<ul class="mb-0">
		<li>Cancellation 7 days or more before last booking date → <strong>100%
				refund (excluding GST)</strong></li>
		<li>Cancellation between 3–6 days before last booking date → <strong>50%
				refund (excluding GST)</strong></li>
		<li>Cancellation between 1–2 days before last booking date → <strong>25%
				refund (excluding GST)</strong></li>
		<li>Cancellation on/after last booking date → <strong>No
				refund</strong></li>
	</ul>
</div>
<%
}
%>



<h2 class="mb-4">Booking Form</h2>

<%
if (pkg != null) {
%>
<div class="card shadow p-3 mb-4">
	<h4 class="fw-bold"><%=pkg.getTitle()%></h4>
	<p>
		<strong>Location:</strong>
		<%=pkg.getLocation()%></p>
	<p>
		<strong>Price per person:</strong> ₹<%=pkg.getPrice()%></p>
	<p>
		<strong>Duration:</strong>
		<%=pkg.getDuration()%>
		days
	</p>
	<p>
		<strong>Available Seats:</strong>
		<%=availableSeats%></p>
</div>
<%
} else {
%>
<div class="alert alert-danger">No package details found. Please
	go back and select a package.</div>
<%
}
%>

<div class="card shadow p-4 bg-white">
	<form action="<%=request.getContextPath()%>/booking" method="post">
		<input type="hidden" name="button" value="createBooking"> <input
			type="hidden" name="isBookingSubmit" value="true">
		<%
		if (pkg != null) {
		%>
		<input type="hidden" name="packageId"
			value="<%=pkg.getPackageId()%>"> <input type="hidden"
			name="packageVersion" value="<%=pkg.getVersion()%>">
		<%
		}
		%>
		<%
		if (user != null) {
		%>
		<input type="hidden" name="userId" value="<%=user.getUserId()%>">
		<%
		}
		%>

		<%
		if (availableSeats > 0) {
		%>
		<div class="mb-3">
			<label for="no_of_travelers" class="form-label"> Number of
				Travelers (Max: <%=availableSeats%>):
			</label> <select class="form-select" id="no_of_travelers"
				name="noOfTravellers" onchange="changeTravelers(this.value)">
				<%
				for (int i = 1; i <= availableSeats; i++) {
				%>
				<option value="<%=i%>"
					<%=(travelersCount == i) ? "selected" : ""%>><%=i%></option>
				<%
				}
				%>
			</select>
		</div>
		<%
		}
		%>

		<div id="travelersContainer">
			<%
			for (int i = 1; i <= travelersCount; i++) {
			%>
			<div class="border p-3 mb-3 rounded">
				<h5 class="fw-bold">
					Traveler
					<%=i%></h5>

				<div class="mb-2">
					<label>Name:</label> <input type="text" name="travelerName<%=i%>"
						class="form-control"
						value="<%=request.getParameter("travelerName" + i) != null ? request.getParameter("travelerName" + i) : ""%>">
					<span class="text-danger"><%=(errors != null && errors.get("travelerName" + i) != null) ? errors.get("travelerName" + i) : ""%></span>
				</div>

				<div class="mb-2">
					<label>Email:</label> <input type="email"
						name="travelerEmail<%=i%>" class="form-control"
						value="<%=request.getParameter("travelerEmail" + i) != null ? request.getParameter("travelerEmail" + i) : ""%>">
					<span class="text-danger"><%=(errors != null && errors.get("travelerEmail" + i) != null) ? errors.get("travelerEmail" + i) : ""%></span>
				</div>

				<div class="mb-2">
					<label>Mobile:</label> <input type="text"
						name="travelerMobile<%=i%>" class="form-control"
						value="<%=request.getParameter("travelerMobile" + i) != null ? request.getParameter("travelerMobile" + i) : ""%>">
					<span class="text-danger"><%=(errors != null && errors.get("travelerMobile" + i) != null) ? errors.get("travelerMobile" + i) : ""%></span>
				</div>

				<div class="mb-2">
					<label>Age:</label> <input type="number" name="travelerAge<%=i%>"
						class="form-control"
						value="<%=request.getParameter("travelerAge" + i) != null ? request.getParameter("travelerAge" + i) : ""%>">
					<span class="text-danger"><%=(errors != null && errors.get("travelerAge" + i) != null) ? errors.get("travelerAge" + i) : ""%></span>
				</div>
			</div>
			<%
			}
			%>
		</div>

		<%
		if (pkg != null && availableSeats > 0) {
		%>
		<button type="submit" class="btn btn-success mt-3">Confirm
			Booking</button>
		<%
		}
		%>
	</form>
</div>

<script>
	window
			.addEventListener(
					"pageshow",
					function(event) {
						if (event.persisted
								|| (window.performance && window.performance
										.getEntriesByType("navigation")[0].type === "back_forward")) {

							window.location.reload();
						}
					});

	function changeTravelers(count) {
		const form = document.forms[0];
		form.isBookingSubmit.value = "false";
		form.submit();
	}
</script>

<%@ include file="footer.jsp"%>
