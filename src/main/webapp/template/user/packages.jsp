<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ page
	import="java.util.*, com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>

<button type="button" class="btn btn-secondary mb-3"
	onclick="window.history.back();">Back</button>

<%
user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}
Map<Integer, BookingResponseDTO> bookingMap = (Map<Integer, BookingResponseDTO>) request.getAttribute("bookingMap");
List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
String title = request.getAttribute("title") != null ? (String) request.getAttribute("title") : "";
String location = request.getAttribute("location") != null ? (String) request.getAttribute("location") : "";
String dateFrom = request.getAttribute("dateFrom") != null ? (String) request.getAttribute("dateFrom") : "";
String dateTo = request.getAttribute("dateTo") != null ? (String) request.getAttribute("dateTo") : "";

int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 6;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "packageList";

String queryParams = "keyword=" + (keyword != null ? keyword : "") + "&title=" + (title != null ? title : "")
		+ "&location=" + (location != null ? location : "") + "&dateFrom=" + (dateFrom != null ? dateFrom : "")
		+ "&dateTo=" + (dateTo != null ? dateTo : "") + "&pageSize=" + pageSize;
%>

<%-- Error Message --%>
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

<h2 class="mb-4">Explore Our Packages</h2>

<%-- Filters Form --%>
<form method="get" action="<%=request.getContextPath()%>/package"
	class="row g-2 mb-4">
	<input type="hidden" name="button" value="packageList">

	<div class="col-md-2">
		<label for="title" class="form-label">Title</label> <input type="text"
			name="title" id="title" class="form-control" value="<%=title%>">
	</div>

	<div class="col-md-2">
		<label for="location" class="form-label">City</label> <input
			type="text" name="location" id="location" class="form-control"
			value="<%=location%>">
	</div>




	<div class="col-md-2">
		<label for="dateFrom" class="form-label">Departure Date From </label>
		<input type="date" name="dateFrom" class="form-control"
			placeholder="Start Date" value="<%=dateFrom%>" id="dateFrom"
			onchange="document.getElementById('dateTo').min = this.value;">
	</div>

	<div class="col-md-2">
		<label for="dateTo" class="form-label"> To </label> <input type="date"
			name="dateTo" class="form-control" value="<%=dateTo%>" id="dateTo">
	</div>




	<div class="col-md-2">
		<label for="keyword" class="form-label">Search</label> <input
			type="text" name="keyword" id="keyword" class="form-control"
			placeholder="Keyword..." value="<%=keyword%>">
	</div>

	<div class="col-md-2 d-flex align-items-end">
		<button type="submit" class="btn btn-primary w-100">Apply
			Filters</button>
	</div>
	<label for="pageSize">Records per page:</label> <select name="pageSize"
		id="pageSize" onchange="this.form.submit()">
		<option value="10" <%=pageSize == 10 ? "selected" : ""%>>10</option>
		<option value="20" <%=pageSize == 20 ? "selected" : ""%>>20</option>
		<option value="30" <%=pageSize == 30 ? "selected" : ""%>>30</option>
		<option value="40" <%=pageSize == 40 ? "selected" : ""%>>40</option>
	</select>
</form>

<%-- Packages Display --%>
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
				<%
				java.time.LocalDateTime lastBookingDate = pkg.getLastBookingDate();
				%>

				

				<%-- If user has booked this package --%>
				<%
				BookingResponseDTO userBooking = (BookingResponseDTO) bookingMap.get(pkg.getPackageId());
				if (userBooking != null) {
					java.time.LocalDateTime departure = userBooking.getDepartureDateAndTime();
				%>
				<div class="mt-2 p-2 bg-light rounded shadow-sm text-center">
					<small class="d-block">Booked: $<%=userBooking.getAmount()%></small>
					<small class="d-block">Status: <%=userBooking.getStatus()%></small>
					<small class="d-block"
						id="carousel-countdown-<%=pkg.getPackageId()%>"></small>
				</div>

				<script>
                                    const countdownCarousel<%=pkg.getPackageId()%> = () => {
                                        const departure = new Date("<%=departure%>");
                                        const now = new Date();
                                        const el = document.getElementById("carousel-countdown-<%=pkg.getPackageId()%>");
                                        if (isNaN(departure) || now >= departure) {
                                            el.innerText = "Departed";
                                            return;
                                        }
                                        const diff = departure - now;
                                        const days = Math.floor(diff / (1000*60*60*24));
                                        const hours = Math.floor((diff % (1000*60*60*24)) / (1000*60*60));
                                        const minutes = Math.floor((diff % (1000*60*60)) / (1000*60));
                                        const seconds = Math.floor((diff % (1000*60)) / 1000);
                                        el.innerText = days + "d " + hours + "h " + minutes + "m " + seconds + "s to go";
                                    };
                                    setInterval(countdownCarousel<%=pkg.getPackageId()%>, 1000);
                                    countdownCarousel<%=pkg.getPackageId()%>();
                                </script>
				<%
				} else {
				lastBookingDate = pkg.getLastBookingDate();
				%>

				<div class="mt-2 p-2 bg-light rounded shadow-sm text-center">
					<small class="d-block fw-bold">Last Booking Till:</small> <small
						class="d-block"><%=lastBookingDate != null ? lastBookingDate.toLocalDate() + " " + lastBookingDate.toLocalTime() : "N/A"%></small>
					<small class="d-block text-danger"
						id="lastbooking-countdown-<%=pkg.getPackageId()%>"></small>
				</div>

				<script>
        const lastBooking<%=pkg.getPackageId()%> = new Date("<%=lastBookingDate != null ? lastBookingDate.toString().replace(" ", "T") : ""%>");
        const elLast<%=pkg.getPackageId()%> = document.getElementById("lastbooking-countdown-<%=pkg.getPackageId()%>");
        if (!isNaN(lastBooking<%=pkg.getPackageId()%>.getTime())) {
            const intervalLast<%=pkg.getPackageId()%> = setInterval(() => {
                const now = new Date();
                if (now >= lastBooking<%=pkg.getPackageId()%>) {
                    elLast<%=pkg.getPackageId()%>.innerText = "Booking Closed";
                    clearInterval(intervalLast<%=pkg.getPackageId()%>);
                    return;
                }
                const diff = lastBooking<%=pkg.getPackageId()%> - now;
                const d = Math.floor(diff / (1000*60*60*24));
                const h = Math.floor((diff % (1000*60*60*24)) / (1000*60*60));
                const m = Math.floor((diff % (1000*60*60)) / (1000*60));
                const s = Math.floor((diff % (1000*60)) / 1000);
                elLast<%=pkg.getPackageId()%>.innerText = d+"d "+h+"h "+m+"m "+s+"s left";
            }, 1000);
        }
    </script>
				<%
				}
				%>

				<h5 class="card-title fw-bold"><%=pkg.getTitle()%></h5>
				<p class="text-muted mb-1">
					<i class="fas fa-map-marker-alt"></i>
					<%=pkg.getLocation()%></p>
				<p class="mb-1">
					<i class="fas fa-clock"></i>
					<%=pkg.getDuration()%>
					Days
				</p>
				<p class="mb-1">
					<i class="fas fa-dollar-sign"></i> $<%=pkg.getPrice()%></p>
				<p class="mb-1">
					<i class="fas fa-chair"></i> Available Seats:
					<%=pkg.getTotalSeats()%></p>
				<p>
								<strong>Departure Date:</strong>

								<%=pkg.getDepartureDate() != null ? pkg.getDepartureDate().toLocalDate() : "Not specified"%></p>


							<p>
								<strong>Departure Time:</strong>

								<%=pkg.getDepartureDate() != null ? pkg.getDepartureDate().toLocalTime() : "Not specified"%></p>
						

				<form action="<%=request.getContextPath()%>/booking" method="post">
					<input type="hidden" name="packageId"
						value="<%=pkg.getPackageId()%>">
					<button type="button" class="btn btn-info w-100 mt-2"
						data-bs-toggle="modal"
						data-bs-target="#packageModal<%=pkg.getPackageId()%>">View
						Details</button>

					<%
					if (userBooking == null || !"CONFIRMED".equalsIgnoreCase(userBooking.getStatus())) {
					%>

					<button type="submit" name="button" value="viewBookingForm"
						class="btn btn-primary w-100 mt-2">Book Now</button>
					<%
					}else{
					%>
					<p class="btn btn-primary w-100 mt-2" >Already Booked</p>
					<%} %>



				</form>
			</div>
		</div>
	</div>

	<%-- Modal for Package Details --%>
	<div class="modal fade" id="packageModal<%=pkg.getPackageId()%>"
		tabindex="-1"
		aria-labelledby="packageModalLabel<%=pkg.getPackageId()%>"
		aria-hidden="true">
		<div class="modal-dialog modal-lg modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"
						id="packageModalLabel<%=pkg.getPackageId()%>"><%=pkg.getTitle()%></h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div class="row">
						<div class="col-md-5">
							<img
								src="<%=pkg.getImageurl() != null ? pkg.getImageurl() : "images/default.jpg"%>"
								class="img-fluid rounded mb-3" alt="<%=pkg.getTitle()%>">
						</div>
						<div class="col-md-7">
							<p>
								<strong>Location:</strong>
								<%=pkg.getLocation()%></p>
							<p>
								<strong>Duration:</strong>
								<%=pkg.getDuration()%>
								Days
							</p>
							<p>
								<strong>Price:</strong> $<%=pkg.getPrice()%></p>
							<p>
								<strong>Available Seats:</strong>
								<%=pkg.getTotalSeats()%></p>
							<p>
								<strong>Description:</strong>
								<%=pkg.getDescription()%></p>
							<p>
								<strong>Departure Date:</strong>

								<%=pkg.getDepartureDate() != null ? pkg.getDepartureDate().toLocalDate() : "Not specified"%></p>


							<p>
								<strong>Departure Time:</strong>

								<%=pkg.getDepartureDate() != null ? pkg.getDepartureDate().toLocalTime() : "Not specified"%></p>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<form action="<%=request.getContextPath()%>/booking" method="get">
						<input type="hidden" name="packageId"
							value="<%=pkg.getPackageId()%>">
						<button type="submit" name="button" value="viewBookingForm"
							class="btn btn-primary w-100 mt-2">Book Now</button>
					</form>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>

	<%
	}
	} else {
	%>
	<p>
		No packages found for "<%=keyword%>".
	</p>
	<%
	}
	%>
</div>

<%-- Pagination Section --%>
<nav>
	<ul class="pagination justify-content-center">
		<%
		if (currentPage > 1) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage - 1%>&button=<%=buttonParam%>">Prev</a></li>
		<%
		}
		for (int i = startPage; i <= endPage; i++) {
		%>
		<li class="page-item <%=(i == currentPage) ? "active" : ""%>"><a
			class="page-link"
			href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
		</li>
		<%
		}
		if (currentPage < totalPages) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage + 1%>&button=<%=buttonParam%>">Next</a></li>
		<%
		}
		%>
	</ul>
</nav>

<%@ include file="footer.jsp"%>
