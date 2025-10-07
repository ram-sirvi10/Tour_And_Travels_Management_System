<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>

<%
    String message = (String) request.getAttribute("message");
    boolean showDashboard = (message != null);

    String alertClass = "alert-info";
    if (message != null) {
        if (message.toLowerCase().contains("successful")) {
            alertClass = "alert-success";
        } else {
            alertClass = "alert-danger";
        }
    }

    Integer bookingIdAttr = showDashboard ? null : (Integer) request.getAttribute("bookingId");
    Double amountAttr = showDashboard ? null : (Double) request.getAttribute("amount");
    int bookingId = (bookingIdAttr != null) ? bookingIdAttr : 0;
    double amount = (amountAttr != null) ? amountAttr : 0.0;

    com.travelmanagement.dto.responseDTO.BookingResponseDTO confirmedBooking =
        (com.travelmanagement.dto.responseDTO.BookingResponseDTO) request.getAttribute("confirmedBooking");
%>

<div class="container mt-5">
	<h2 class="mb-4">Payment Details</h2>

	<% if (message != null) { %>
	<div class="alert <%= alertClass %> alert-dismissible fade show"
		role="alert">
		<%= message %>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<% } %>

	<% if (confirmedBooking != null) { %>
	<div class="mb-3">
		<a
			href="<%= request.getContextPath() %>/booking?button=downloadInvoice&bookingId=<%= confirmedBooking.getBookingId() %>"
			class="btn btn-primary">Download Invoice</a>
	</div>

	<div class="card shadow p-4 bg-light mt-2">
		<h4 class="mb-3">Booking Invoice</h4>
		<p>
			<strong>Booking ID:</strong>
			<%= confirmedBooking.getBookingId() %></p>
		<p>
			<strong>Package:</strong>
			<%= confirmedBooking.getPackageName() %></p>
		<p>
			<strong>Departure:</strong>
			<%= confirmedBooking.getDepartureDateAndTime() %></p>
		<p>
			<strong>Booking Date:</strong>
			<%= confirmedBooking.getBookingDate()%></p>

		<p>
			<strong>No of Travelers:</strong>
			<%= confirmedBooking.getNoOfTravellers()%></p>
		<p>
			<strong>Total Amount:</strong> ₹
			<%= confirmedBooking.getAmount() %></p>

		<% if (confirmedBooking.getPackageImage() != null && !confirmedBooking.getPackageImage().isEmpty()) { %>
		<div class="mb-3">
			<img src="<%= confirmedBooking.getPackageImage() %>"
				alt="Package Image" class="img-fluid rounded"
				style="max-width: 300px;">
		</div>
		<% } %>
		<h5 class="mt-3">Travelers List</h5>
		<table class="table table-bordered">
			<thead>
				<tr>
					<th>Name</th>
					<th>Email</th>
					<th>Mobile</th>
					<th>Age</th>
				</tr>
			</thead>
			<tbody>
				<% for (com.travelmanagement.dto.responseDTO.TravelerResponseDTO t : confirmedBooking.getTravelers()) { %>
				<tr>
					<td><%= t.getName() %></td>
					<td><%= t.getEmail() %></td>
					<td><%= t.getMobile() %></td>
					<td><%= t.getAge() %></td>
				</tr>
				<% } %>
			</tbody>
		</table>
	</div>
	<% } %>

	<% if (bookingId > 0) { %>
	<div class="card shadow p-4 bg-white mt-4">
		<h5>
			Booking ID: <strong><%= bookingId %></strong>
		</h5>
		<%
                double amountWithoutGST = amount / 1.18;
                double gst = amount - amountWithoutGST;
            %>
		<p class="mt-3">
			Amount (Excl. GST): ₹
			<%= String.format("%.2f", amountWithoutGST) %></p>
		<p class="mt-3">
			GST (18%): ₹
			<%= String.format("%.2f", gst) %></p>
		<h4 class="mt-3">
			Total Amount to Pay: <strong>₹ <%= amount %></strong>
		</h4>

		<div class="mb-3">
			<strong>Time Remaining: </strong> <span id="countdown">05:00</span>
		</div>

		<form action="<%= request.getContextPath() %>/booking" method="post"
			class="mt-4" id="paymentForm">
			<input type="hidden" name="bookingId" value="<%= bookingId %>">
			<input type="hidden" name="amount" value="<%=amount %>">

			<button type="submit" name="button" value="paymentConfirm"
				class="btn btn-success me-3">Confirm Payment</button>
			<button type="submit" name="button" value="paymentReject"
				class="btn btn-danger">Reject Payment</button>
		</form>

		<script>
                let timeLeft = 5 * 60;
                const countdownElem = document.getElementById('countdown');
                const paymentForm = document.getElementById('paymentForm');

                const timer = setInterval(() => {
                    let minutes = Math.floor(timeLeft / 60);
                    let seconds = timeLeft % 60;

                    countdownElem.textContent =
                        (minutes < 10 ? '0' + minutes : minutes) + ':' +
                        (seconds < 10 ? '0' + seconds : seconds);

                    if (timeLeft <= 0) {
                        clearInterval(timer);
                        countdownElem.textContent = "00:00";

                        paymentForm.innerHTML = `
                            <div class="alert alert-danger" role="alert">
                                Payment time expired! Booking automatically cancelled.
                            </div>
                            <a href="<%= request.getContextPath() %>/user?button=dashboard"
                               class="btn btn-primary mt-3">Go to Dashboard</a>
                        `;
                    }

                    timeLeft--;
                }, 1000);
            </script>
	</div>
	<%
	}
	%>
</div>

<%@ include file="footer.jsp"%>
