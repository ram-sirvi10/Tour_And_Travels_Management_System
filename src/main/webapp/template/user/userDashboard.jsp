<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%@ include file="header.jsp"%>
<div class="text-center mb-5">
	<h1 class="fw-bold">
		Welcome,
		<%=user.getUserName()%></h1>
	<p class="text-muted">Plan your trips, manage bookings, and explore
		exciting destinations.</p>
</div>
<div class="container mt-5">
    <div class="row">
        <!-- Left Column: New Packages Carousel -->
        <div class="col-lg-8 mb-4">
            <h2 class="fw-bold mb-4">New Packages</h2>

            <div id="packageCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
                <div class="carousel-inner">
                <%
                int totalPackages = 9;
                int cardsPerSlide = 3; // show 3 cards per slide
                int slideCount = (int) Math.ceil(totalPackages / (double) cardsPerSlide);
                for (int slide = 0; slide < slideCount; slide++) {
                %>
                    <div class="carousel-item <%= slide == 0 ? "active" : "" %>">
                        <div class="row g-4">
                        <%
                        for (int j = 0; j < cardsPerSlide; j++) {
                            int i = slide * cardsPerSlide + j + 1;
                            if (i > totalPackages) break;
                        %>
                            <div class="col-md-4">
                                <div class="card h-100 shadow-sm">
                                    <img src="https://media.istockphoto.com/id/517188688/photo/mountain-landscape.jpg?s=612x612&w=0&k=20&c=A63koPKaCyIwQWOTFBRWXj_PwCrR4cEoOw2S9Q7yVl8=" class="card-img-top package-img" alt="Paris">
                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title fw-bold">Paris Delight</h5>
                                        <p class="text-muted mb-1"><i class="fas fa-map-marker-alt"></i> France</p>
                                        <p class="mb-1"><i class="fas fa-clock"></i> 5 Days</p>
                                        <p class="mb-1"><i class="fas fa-dollar-sign"></i> $1200</p>
                                        <p class="card-text text-truncate mb-3">Explore the city of love, visit Eiffel Tower, Louvre Museum, and enjoy local cuisine.</p>
                                        <a href="booking.jsp?packageId=<%=i%>" class="btn btn-primary mt-auto w-100">Book Now</a>
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
                </div>

                <!-- Carousel Controls -->
                <button class="carousel-control-prev" type="button" data-bs-target="#packageCarousel" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#packageCarousel" data-bs-slide="next">
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
                <div class="list-group-item d-flex justify-content-between align-items-center">
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





