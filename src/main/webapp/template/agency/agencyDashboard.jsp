<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="header.jsp"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<%@ page
	import="com.travelmanagement.service.impl.PackageServiceImpl,com.travelmanagement.service.impl.BookingServiceImpl"%>
<%
int totalPackages = request.getAttribute("totalPackages") != null ? (Integer) request.getAttribute("totalPackages") : 0;
int activePackages = request.getAttribute("activePackages") != null ? (Integer) request.getAttribute("activePackages")
		: 0;
int totalBookings = request.getAttribute("totalBookings") != null ? (Integer) request.getAttribute("totalBookings") : 0;
%>

<h2 class="mb-4">📊 Dashboard Overview</h2>
<div class="row">
	<div class="col-md-4">
		<div class="card text-bg-primary shadow mb-3">
			<div class="card-body">
				<h5>Total Packages</h5>
				<h3><%=totalPackages%></h3>
			</div>
		</div>
	</div>
	<div class="col-md-4">
		<div class="card text-bg-success shadow mb-3">
			<div class="card-body">
				<h5>Active Packages</h5>
				<h3><%=activePackages%></h3>
			</div>
		</div>
	</div>
	<div class="col-md-4">
		<div class="card text-bg-warning shadow mb-3">
			<div class="card-body">
				<h5>Total Bookings</h5>
				<h3><%=totalBookings%></h3>
			</div>
		</div>
	</div>
</div>

<%@ include file="footer.jsp"%>
