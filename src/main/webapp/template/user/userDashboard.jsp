<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>

<%@ include file="header.jsp"%>

<%
user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
%>

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
<div class="text-center mb-5">
	<h1 class="fw-bold">
		Welcome,
		<%=user.getUserName()%></h1>
	<p class="text-muted">Plan your trips, manage bookings, and explore
		exciting destinations.</p>
</div>

<div class="container mt-5">
	<div class="row">
		<!-- Left Column: Carousel -->
		<div class="col-lg-8 mb-4">
			<h2 class="fw-bold mb-4">New Packages</h2>
			<div id="packageCarousel" class="carousel slide"
				data-bs-ride="carousel" data-bs-interval="3000">
				<div class="carousel-inner">
					<%
					if (packages != null && !packages.isEmpty()) {
						int maxPackages = Math.min(9, packages.size());
						int cardsPerSlide = 3;
						int slideCount = (int) Math.ceil(maxPackages / (double) cardsPerSlide);

						for (int slide = 0; slide < slideCount; slide++) {
					%>
					<div class="carousel-item <%=slide == 0 ? "active" : ""%>">
						<div class="row g-4">
							<%
							for (int j = 0; j < cardsPerSlide; j++) {
								int index = slide * cardsPerSlide + j;
								if (index >= maxPackages)
									break;
								PackageResponseDTO pkg = packages.get(index);
							%>
							<div class="col-md-4">
								<div class="card h-100 shadow-sm">
									<img
										src="<%=pkg.getImageurl() != null ? pkg.getImageurl() : "images/default.jpg"%>"
										class="card-img-top package-img" alt="<%=pkg.getTitle()%>">
									<div class="card-body d-flex flex-column">
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
										<p class="card-text text-truncate mb-3"><%=pkg.getDescription()%></p>
										<form action="<%=request.getContextPath()%>/booking"
											method="post">
											<input type="hidden" name="packageId"
												value="<%=pkg.getPackageId()%>">
											<button type="submit" name="button" value="viewBookingForm"
												class="btn btn-primary mt-auto w-100">Book Now</button>
										</form>
									</div>
								</div>
							</div>
							<%
							}
							%>
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

				<!-- Carousel Controls -->
				<button class="carousel-control-prev" type="button"
					data-bs-target="#packageCarousel" data-bs-slide="prev">
					<span class="carousel-control-prev-icon" aria-hidden="true"></span>
					<span class="visually-hidden">Previous</span>
				</button>
				<button class="carousel-control-next" type="button"
					data-bs-target="#packageCarousel" data-bs-slide="next">
					<span class="carousel-control-next-icon" aria-hidden="true"></span>
					<span class="visually-hidden">Next</span>
				</button>
			</div>
		</div>

		<!-- Right Column: Recent Bookings -->
		<div class="col-lg-4 mb-4">
    <h2 class="fw-bold mb-4">Recent Bookings</h2>
    <div class="list-group">
        <%
        if (bookings != null && !bookings.isEmpty()) {
            for (BookingResponseDTO dto : bookings) {
                java.time.LocalDateTime departure = dto.getDepartureDateAndTime();
        %>
        <div class="list-group-item mb-3 p-3 shadow-sm rounded">

            <!-- Countdown Section -->
            <div class="mb-2 text-center">
                <h6 class="mb-1">Departure In:</h6>
                <span id="countdown-<%=dto.getBookingId()%>" class="fw-bold fs-6 text-primary"></span>
            </div>

            <!-- Package Info -->
            <div class="mb-2">
                <h5 class="mb-1"><%=dto.getPackageName()%></h5>
                <small class="text-muted d-block">Booked on: <%=dto.getBookingDate()%></small>
                <small class="text-muted d-block">Departure: 
                    <%= departure != null ? departure.toLocalDate() + " " + departure.toLocalTime() : "Not Mentioned" %>
                </small>
            </div>

            <div class="d-flex justify-content-between align-items-center flex-wrap">
                <!-- Amount -->
                <div class="text-center mb-2">
                    <h6 class="mb-1">Amount</h6>
                    <span class="badge bg-primary rounded-pill">$<%=dto.getAmount()%></span>
                </div>

                <!-- Status -->
                <div class="text-center mb-2">
                    <h6 class="mb-1">Status</h6>
                    <%
                    String statusClass = "bg-danger";
                    if ("CONFIRMED".equalsIgnoreCase(dto.getStatus())) {
                        statusClass = "bg-success";
                    } else if ("PENDING".equalsIgnoreCase(dto.getStatus())) {
                        statusClass = "bg-warning text-dark";
                    }
                    %>
                    <span class="badge <%=statusClass%>"><%=dto.getStatus()%></span>
                </div>
            </div>
        </div>

    <script>
    // Countdown + Departure Alert
    const countdown<%=dto.getBookingId()%> = () => {
        const departure = new Date("<%= departure != null ? departure.toString() : "" %>");
        const now = new Date();
        const countdownEl = document.getElementById("countdown-<%=dto.getBookingId()%>");

        if (isNaN(departure) || now >= departure) {
            countdownEl.innerText = "Departed";
            
            // Show toast only once
            if (!countdownEl.dataset.alertShown) {
                const toastHtml = `
                <div class="position-fixed top-0 end-0 p-3" style="z-index:1080">
                    <div class="toast show align-items-center text-bg-info border-0" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">
                                Your trip for <%=dto.getPackageName()%> has started today!
                            </div>
                            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                    </div>
                </div>`;
                document.body.insertAdjacentHTML('beforeend', toastHtml);
                countdownEl.dataset.alertShown = "true";
            }
            return;
        }

        const diff = departure - now;

        const days = Math.floor(diff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((diff % (1000 * 60)) / 1000);

        countdownEl.innerText = days + "d " + hours + "h " + minutes + "m " + seconds + "s to go";
    }

    countdown<%=dto.getBookingId()%>();
    setInterval(countdown<%=dto.getBookingId()%>, 1000); // Update every second
</script>


        <%
    
            }
        } else {
        %>
        <p>No Bookings available.</p>
        <%
        }
        %>
    </div>
</div>


	</div>
</div>

<%@ include file="footer.jsp"%>
