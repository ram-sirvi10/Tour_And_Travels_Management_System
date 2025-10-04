<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PaymentResponseDTO"%>

<%@ include file="header.jsp"%>
<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
	<div class="toast show align-items-center text-bg-danger border-0"
		role="alert" aria-live="assertive" aria-atomic="true"
		data-bs-delay="3000" data-bs-autohide="true">
		<div class="d-flex">
			<div class="toast-body">
				<%=errorMessage%>
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto"
				data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
	</div>
</div>
<%
}
%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<div class="container mt-4">
	<h2 class="mb-4">Payment History</h2>

	<%
	List<PaymentResponseDTO> payments = (List<PaymentResponseDTO>) request.getAttribute("payments");
	if (payments == null) {
		payments = new java.util.ArrayList<>();
	}
	%>
	<form method="get" action="booking" class="row g-2 mb-3">
		<input type="hidden" name="button" value="paymentHistory">

		<div class="col-md-3">
			<select name="status" class="form-select">
				<option value="">All Status</option>
				<option value="SUCCESSFUL"
					<%="SUCCESSFUL".equals(request.getParameter("status")) ? "selected" : ""%>>Successful</option>
				<option value="FAILED"
					<%="FAILED".equals(request.getParameter("status")) ? "selected" : ""%>>Failed</option>
				<option value="REFUNDED"
					<%="REFUNDED".equals(request.getParameter("status")) ? "selected" : ""%>>REFUNDED</option>
			</select>
		</div>

		<div class="col-md-2">
			<label for="startDate" class="form-label"> From </label> <input
				type="date" name="startDate" class="form-control"
				placeholder="Start Date"
				value="<%=request.getParameter("startDate") != null ? request.getParameter("startDate") : ""%>"
				id="startDate"
				onchange="document.getElementById('endDate').min = this.value;">
		</div>

		<div class="col-md-2">
			<label for="endDate" class="form-label"> To </label> <input
				type="date" name="endDate" class="form-control"
				placeholder="End Date"
				value="<%=request.getParameter("endDate") != null ? request.getParameter("endDate") : ""%>"
				id="endDate">
		</div>


		<div class="col-md-2">
			<label for="pageSize">Records per page:</label> <select
				name="pageSize" id="pageSize" onchange="this.form.submit()">
				<option value="10"
					<%="10".equals(request.getParameter("pageSize")) ? "selected" : ""%>>10</option>
				<option value="20"
					<%="20".equals(request.getParameter("pageSize")) ? "selected" : ""%>>20</option>
				<option value="50"
					<%="50".equals(request.getParameter("pageSize")) ? "selected" : ""%>>50</option>
			</select>



		</div>

		<div class="col-md-1">
			<button type="submit" class="btn btn-primary w-100">Filter</button>
		</div>
	</form>

	<div class="card shadow p-3 bg-white">
		<div class="table-responsive">
			<table class="table table-bordered align-middle">
				<thead class="table-primary">
					<tr>
						<th>#</th>
						<th>Payment ID</th>
						<th>Booking ID</th>
						<th>Package Name</th>
						<th>Amount</th>
						<th>Status</th>
						<th>Date</th>
					</tr>
				</thead>
				<tbody>
					<%
					if (!payments.isEmpty()) {
						int i = 1;
						for (PaymentResponseDTO p : payments) {
					%>
					<tr>
						<td><%=i++%></td>
						<td><%=p.getPaymentId()%></td>
						<td><%=p.getBookingId()%></td>
						<td><%=p.getPackageName() != null ? p.getPackageName() : "-"%></td>
						<td>₹<%=p.getAmount()%></td>
						<td><span
							class="badge 
        <%="SUCCESSFUL".equals(p.getStatus()) ? "bg-success" : "REFUNDED".equals(p.getStatus()) ? "bg-info" : "bg-danger"%>">
								<%=p.getStatus()%>
						</span></td>
						<td><%=p.getPaymentDate() != null ? p.getPaymentDate() : "-"%></td>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="7" class="text-center">No payments found</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>
	</div>
</div>
<nav>
	<ul class="pagination justify-content-center mt-3">
		<%
		int currentPage = (Integer) request.getAttribute("currentPage");
		int totalPages = (Integer) request.getAttribute("totalPages");
		String queryParams = "status=" + (request.getParameter("status") != null ? request.getParameter("status") : "")
				+ "&startDate=" + (request.getParameter("startDate") != null ? request.getParameter("startDate") : "")
				+ "&endDate=" + (request.getParameter("endDate") != null ? request.getParameter("endDate") : "") + "&pageSize="
				+ (request.getParameter("pageSize") != null ? request.getParameter("pageSize") : "10");
		%>
		<li class="page-item <%=currentPage == 1 ? "disabled" : ""%>"><a
			class="page-link"
			href="booking?button=paymentHistory&page=<%=currentPage - 1%>&<%=queryParams%>">Previous</a>
		</li>

		<%
		for (int i = 1; i <= totalPages; i++) {
		%>
		<li class="page-item <%=currentPage == i ? "active" : ""%>"><a
			class="page-link"
			href="booking?button=paymentHistory&page=<%=i%>&<%=queryParams%>"><%=i%></a>
		</li>
		<%
		}
		%>

		<li class="page-item <%=currentPage == totalPages ? "disabled" : ""%>">
			<a class="page-link"
			href="booking?button=paymentHistory&page=<%=currentPage + 1%>&<%=queryParams%>">Next</a>
		</li>
	</ul>
</nav>


<%@ include file="footer.jsp"%>
