<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>

<%@ include file="header.jsp"%>

<%
user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
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
				for (int i = 1; i <= 5; i++) {
				%>
				<div
					class="list-group-item d-flex justify-content-between align-items-center">
					<div>
						<h6 class="mb-1">Paris Delight</h6>
						<small class="text-muted">Booked on: 2025-09-24</small>
					</div>
					<span class="badge bg-primary rounded-pill">$1200</span>
				</div>
				<%
				}
				%>
			</div>
		</div>
	</div>
</div>

<%@ include file="footer.jsp"%>
