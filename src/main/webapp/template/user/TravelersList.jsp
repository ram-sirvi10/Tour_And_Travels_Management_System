<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.TravelerResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ include file="header.jsp"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>


<%
String errorMessage = (String) session.getAttribute("errorMessage");
session.removeAttribute("errorMessage");
if (errorMessage == null) {
	errorMessage = (String) request.getAttribute("errorMessage");
}
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
<%
String successMessage = (String) session.getAttribute("successMessage");
session.removeAttribute("successMessage");
if (successMessage == null) {
	successMessage = (String) request.getAttribute("successMessage");
}
%>


<%
if (successMessage != null && !successMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-success border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body">
				<%=successMessage%>
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>



<h2 class="mb-4">Travelers List</h2>
<%
String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 10;
int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "viewTravelers";
String queryParams = "keyword=" + (request.getParameter("keyword") != null ? request.getParameter("keyword") : "")
		+ "&pageSize=" + pageSize;
%>
<%
PackageResponseDTO packageDTO = (PackageResponseDTO) request.getAttribute("package");
%>

<!-- Package / Booking Information -->
<%
if (packageDTO != null) {
%>
<div class="card mb-4 shadow-sm">
	<div class="card-body">
		<h5 class="card-title fw-bold"><%=packageDTO.getTitle()%></h5>
		<p class="mb-1">
			<strong>Amount:</strong> ₹<%=packageDTO.getPrice()%></p>
		<p class="mb-1">
			<strong>Last Booking Date:</strong>
			<%=packageDTO.getLastBookingDate()%></p>
	</div>
</div>
<%
}
%>

<div class="card shadow p-3 bg-white">
	<div class="table-responsive">
		<table class="table table-hover align-middle">
			<thead class="table-dark">
				<tr>
					<th>#</th>
					<th>Name</th>
					<th>Email</th>
					<th>Mobile</th>
					<th>Age</th>
					<th>Package</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<%
				List<TravelerResponseDTO> travelers = (List<TravelerResponseDTO>) request.getAttribute("travelers");
				if (travelers != null && !travelers.isEmpty()) {
					int count = (currentPage - 1) * pageSize + 1;
					for (TravelerResponseDTO t : travelers) {
				%>
				<tr>
					<td><%=count++%></td>
					<td><%=t.getName()%></td>
					<td><%=t.getEmail()%></td>
					<td><%=t.getMobile()%></td>
					<td><%=t.getAge()%></td>
					<td><%=packageDTO != null ? packageDTO.getTitle() : ""%></td>
					<td>
						<%
						if (!"CANCELLED".equalsIgnoreCase(t.getStatus())) {
						%>
						<button type="button" class="btn btn-sm btn-danger"
							onclick="showTravelerCancelModal(<%=t.getId()%>, <%=packageDTO.getPrice()%>, '<%=packageDTO.getLastBookingDate()%>')">
							Cancel Traveler</button> <%
 } else {
 %> <span class="badge bg-secondary">Cancelled</span>
						<%
						}
						%>
					</td>

				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="7" class="text-center">No travelers found.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>
</div>

<!-- Pagination Section -->
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
		<li class="page-item <%=(i == currentPage) ? "active" : ""%>"><a
			class="page-link"
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

<!-- Traveler Cancel Modal -->
<div class="modal fade" id="travelerCancelModal" tabindex="-1"
	aria-labelledby="travelerCancelModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title">Cancel Traveler</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<p id="travelerTotalAmount"></p>
				<p id="travelerAmountAfterGST"></p>
				<p id="travelerCancellationFee"></p>
				<hr>
				<p id="travelerRefundableAmount"></p>
			</div>
			<div class="modal-footer">
				<form id="confirmTravelerCancelForm" method="post"
					action="<%=request.getContextPath()%>/booking">
					<input type="hidden" name="button" value="cancelTraveler">
					<input type="hidden" name="travelerId" id="modalTravelerId">
					<input type="hidden" name="bookingId"
						value="<%=request.getParameter("bookingId")%>">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Close</button>
					<button type="submit" class="btn btn-danger">Confirm
						Cancel</button>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- Bootstrap JS -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
function showTravelerCancelModal(travelerId, amount, lastBookingDateStr) {
	let totalAmount = Number(amount) || 0;
	let lastBookingDate = new Date(lastBookingDateStr.replace(' ', 'T'));
	let now = new Date(); 
	let daysDiff = Math.ceil((lastBookingDate - now) / (1000 * 60 * 60 * 24));

	let refundPercent = 0;
	if (daysDiff >= 7) refundPercent = 100;
	else if (daysDiff >= 3) refundPercent = 50;
	else if (daysDiff >= 1) refundPercent = 25;

	let amountAfterGST = totalAmount / 1.18;
	let refundAmount = (amountAfterGST * refundPercent) / 100;
	let cancelFee = amountAfterGST - refundAmount;

	document.getElementById("travelerTotalAmount").innerText = 'Total Amount: ₹' + totalAmount.toFixed(2);
	document.getElementById("travelerAmountAfterGST").innerText = 'Amount After GST(18%): ₹' + amountAfterGST.toFixed(2);
	document.getElementById("travelerCancellationFee").innerText = 'Cancellation Fee: ₹' + cancelFee.toFixed(2);
	document.getElementById("travelerRefundableAmount").innerText = 'Refundable Amount: ₹' + refundAmount.toFixed(2);

	document.getElementById("modalTravelerId").value = travelerId;

	let modal = new bootstrap.Modal(document.getElementById("travelerCancelModal"), { backdrop: 'static' });
	modal.show();
}



</script>

<%@ include file="footer.jsp"%>
