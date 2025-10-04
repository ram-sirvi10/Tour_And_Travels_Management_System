<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>
<%@ page import="com.travelmanagement.model.PackageSchedule"  %>
<%@ include file="header.jsp"%>

<%
user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
Map<Integer, BookingResponseDTO> bookingMap = (Map<Integer, BookingResponseDTO>) request.getAttribute("bookingMap");
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
								BookingResponseDTO userBooking = (BookingResponseDTO) bookingMap.get(pkg.getPackageId());
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
											<i class="fas fa-dollar-sign"></i> ₹<%=pkg.getPrice()%></p>
										<p class="mb-1">
											<i class="fas fa-chair"></i> Available Seats:
											<%=pkg.getTotalSeats()%></p>
										<p class="card-text text-truncate mb-3"><%=pkg.getDescription()%></p>

										<%-- If user has booked this package --%>
										<%
										if (userBooking != null) {
											java.time.LocalDateTime departure = userBooking.getDepartureDateAndTime();
										%>
										<div class="mt-2 p-2 bg-light rounded shadow-sm text-center">
											<small class="d-block">Booked: ₹<%=userBooking.getAmount()%></small>
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
										java.time.LocalDateTime lastBookingDate = pkg.getLastBookingDate(); // <-- yeh field use karna hoga
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
										<button type="button" class="btn btn-warning w-100 mt-2"
											data-bs-toggle="modal"
											data-bs-target="#itineraryModal<%=pkg.getPackageId()%>">
											View Itinerary</button>

										<%
										if (userBooking == null || !"CONFIRMED".equalsIgnoreCase(userBooking.getStatus())) {
										%>
										<form action="<%=request.getContextPath()%>/booking"
											method="post" class="mt-auto">
											<input type="hidden" name="packageId"
												value="<%=pkg.getPackageId()%>">
											<button type="submit" name="button" value="viewBookingForm"
												class="btn btn-primary w-100">Book Now</button>
										</form>
										<%
										}
										%>
									</div>
								</div>
							</div>



							<%-- Itinerary Modal --%>
							<div class="modal fade"
								id="itineraryModal<%=pkg.getPackageId()%>" tabindex="-1"
								aria-labelledby="itineraryModalLabel<%=pkg.getPackageId()%>"
								aria-hidden="true">
								<div class="modal-dialog modal-lg modal-dialog-scrollable">
									<div class="modal-content">
										<div class="modal-header">
											<h5 class="modal-title"
												id="itineraryModalLabel<%=pkg.getPackageId()%>">
												Itinerary -
												<%=pkg.getTitle()%>
											</h5>
											<button type="button" class="btn-close"
												data-bs-dismiss="modal" aria-label="Close"></button>
										</div>
										<div class="modal-body">
											<%-- Display day-wise schedule --%>
											<%
											List<PackageSchedule> schedules = pkg.getPackageSchedule();
											if (schedules != null && !schedules.isEmpty()) {
												for (PackageSchedule schedule : schedules) {
											%>
											<div class="card mb-2 shadow-sm">
												<div class="card-body">
													<strong>Day <%=schedule.getDayNumber()%>:
													</strong>
													<%=schedule.getActivity()%><br> <small
														class="text-muted"><%=schedule.getDescription()%></small>
												</div>
											</div>
											<%
											}
											} else {
											%>
											<p>No itinerary available for this package.</p>
											<%
											}
											%>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-secondary"
												data-bs-dismiss="modal">Close</button>
										</div>
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
					%>
					<%
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
			<h2 class="fw-bold mb-4">Upcoming Bookings</h2>
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
						<span id="countdown-<%=dto.getBookingId()%>"
							class="fw-bold fs-6 text-primary"></span>
					</div>

					<!-- Package Info -->
					<div class="mb-2">
						<h5 class="mb-1"><%=dto.getPackageName()%></h5>
						<small class="text-muted d-block">Booked on: <%=dto.getBookingDate()%></small>
						<small class="text-muted d-block">Departure: <%=departure != null ? departure.toLocalDate() + " " + departure.toLocalTime() : "Not Mentioned"%>
						</small>
					</div>

					<div
						class="d-flex justify-content-between align-items-center flex-wrap">
						<!-- Amount -->
						<div class="text-center mb-2">
							<h6 class="mb-1">Amount</h6>
							<span class="badge bg-primary rounded-pill">₹<%=dto.getAmount()%></span>
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
    (function() {
        const departureStr = "<%=departure != null ? departure.toString() : ""%>";
        if (!departureStr) return; // No departure date

        // Convert to JS Date using ISO format
        const departure = new Date("<%=departure != null ? departure.toString().replace(' ', 'T') : ""%>");
        const countdownEl = document.getElementById("countdown-<%=dto.getBookingId()%>");

        const interval = setInterval(() => {
            const now = new Date();
            if (now >= departure || isNaN(departure.getTime())) {
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

                clearInterval(interval);
                return;
            }

            const diff = departure - now;
            const days = Math.floor(diff / (1000 * 60 * 60 * 24));
            const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((diff % (1000 * 60)) / 1000);

            countdownEl.innerText = days + "d " + hours + "h " + minutes + "m " + seconds + "s to go";
        }, 1000);
    })();
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
