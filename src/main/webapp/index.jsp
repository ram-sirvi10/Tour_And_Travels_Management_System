<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Tour & Travel Management System</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script src="https://kit.fontawesome.com/a076d05399.js"
	crossorigin="anonymous"></script>
<style>
/* ===== Body ===== */
body {
	font-family: 'Segoe UI', sans-serif;
	background-color: #f5f7fb;
	margin: 0;
	padding: 0;
}

/* ===== Hero Section ===== */
.hero {
	position: relative;
	height: 100vh;
	background:
		url('https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1650&q=80')
		center/cover no-repeat;
	display: flex;
	justify-content: center;
	align-items: center;
	text-align: center;
	color: white;
}

.hero::after {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.45);
	z-index: 1;
}

.hero-content {
	position: relative;
	z-index: 2;
}

.hero-content h1 {
	font-size: 3rem;
	margin-bottom: 20px;
	font-weight: 700;
}

.hero-content p {
	font-size: 1.3rem;
	margin-bottom: 30px;
	max-width: 600px;
}

.hero-content .btn-started {
	padding: 14px 28px;
	border-radius: 50px;
	font-weight: 600;
	background: linear-gradient(90deg, #ff6f61, #ff3b2e);
	color: white;
	text-decoration: none;
	transition: 0.3s;
}

.hero-content .btn-started:hover {
	transform: translateY(-5px);
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
}

/* ===== Packages Carousel ===== */
#packagesCarousel .carousel-item {
	padding: 40px 0;
}

.package-card {
	background-color: #fff;
	border-radius: 15px;
	overflow: hidden;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
	transition: transform 0.3s, box-shadow 0.3s;
}

.package-card:hover {
	transform: translateY(-10px);
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
}

.package-card img {
	width: 100%;
	height: 220px;
	object-fit: cover;
}

.package-card-body {
	padding: 20px;
}

.package-card-body h5 {
	font-weight: 600;
	margin-bottom: 10px;
}

.package-card-body p {
	font-size: 0.9rem;
	color: #555;
	margin-bottom: 10px;
}

.package-card-body .price-location {
	font-weight: 500;
	color: #ff6f61;
	font-size: 0.95rem;
}

.card {
	border-radius: 20px;
	min-height: 220px;
	transition: all 0.3s ease;
}

.card:hover {
	transform: translateY(-8px);
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.card h4 {
	font-size: 1.6rem; /* Title बड़ा */
	font-weight: 700; /* Bold */
	letter-spacing: 1px;
	margin-bottom: 20px;
	color: #fff; /* Gradient bg पर readable */
	text-transform: uppercase; /* Stylish caps */
}

.card p {
	font-size: 3rem; /* Number बहुत बड़ा */
	font-weight: 800; /* Heavy bold */
	color: #fff;
	margin-bottom: 15px;
	text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.3); /* Glow effect */
}

.card i {
	font-size: 4rem; /* Icon बड़ा */
	opacity: 0.25; /* Background watermark जैसा */
	position: absolute;
	top: 20px;
	right: 25px;
}

/* Gradient colors for different cards */
.card.users {
	background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.card.agencies {
	background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.card.pending {
	background: linear-gradient(135deg, #f7971e, #ffd200);
}

.card.rejected {
	background: linear-gradient(135deg, #f5576c, #f093fb);
}

a.card-link {
	text-decoration: none;
}

/* ===== Features Section ===== */
#features {
	padding: 60px 10%;
}

#features h2 {
	text-align: center;
	margin-bottom: 50px;
	font-size: 2.5rem;
	color: #ff6f61;
	font-weight: 700;
}

.features-grid {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
	gap: 30px;
}

.feature-box {
	background: #fff;
	padding: 30px 20px;
	border-radius: 15px;
	text-align: center;
	box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
	transition: all 0.3s ease;
}

.feature-box:hover {
	transform: translateY(-10px);
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
}

.feature-box img {
	width: 80px;
	height: 80px;
	margin-bottom: 20px;
}

.feature-box h3 {
	font-size: 1.3rem;
	margin-bottom: 15px;
	color: #333;
}

.feature-box p {
	font-size: 0.95rem;
	color: #555;
}

/* ===== CTA Section ===== */
.cta {
	position: relative;
	background:
		url('https://images.pexels.com/photos/346885/pexels-photo-346885.jpeg?auto=format&fit=crop&w=1650&q=80')
		center/cover no-repeat;
	padding: 80px 20px;
	border-radius: 12px;
	margin: 50px 10%;
	text-align: center;
	color: white;
}

.cta::after {
	content: "";
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.45);
	z-index: 1;
}

.cta h2, .cta p, .cta a {
	position: relative;
	z-index: 2;
}

.cta a {
	display: inline-block;
	margin-top: 20px;
	padding: 14px 28px;
	background: linear-gradient(90deg, #ff6f61, #ff3b2e);
	color: white;
	font-weight: 600;
	border-radius: 50px;
	text-decoration: none;
	transition: transform 0.3s;
}

.cta a:hover {
	transform: translateY(-5px);
	box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
}

/* ===== Footer ===== */
.footer {
	background: #333;
	color: white;
	text-align: center;
	padding: 20px;
	margin-top: 50px;
}

/* ===== Responsive ===== */
@media ( max-width :768px) {
	.hero-content h1 {
		font-size: 2.2rem;
	}
	.hero-content p {
		font-size: 1rem;
	}
}
<style>
/* Book Now Button Gradient */
.btn-book-now {
    background: linear-gradient(90deg, #ff6f61, #ff3b2e); /* bright orange */
    color: white;
    font-weight: 600;
    border: 2px solid transparent; /* initial border */
    border-radius: 50px;
    transition: transform 0.3s, box-shadow 0.3s, border 0.3s;
}

.btn-book-now:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25);
    border: 2px solid #fff; /* hover border */
}

/* Already Booked Button - muted/darker orange */
.btn-booked {
    background: linear-gradient(90deg, #ff8c42, #e07b30); /* darker/muted orange */
    color: white;
    font-weight: 600;
    border: 2px solid transparent;
    border-radius: 50px;
    pointer-events: none; /* Disable click */
}

.btn-booked:hover {
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
}
}
</style>
</head>
<body>

	<jsp:include page="navbar.jsp" />

	<%
	Object user = session.getAttribute("user");
	Object agency = session.getAttribute("agency");
	String errorMessage = (String) session.getAttribute("errorMessage");
	session.removeAttribute("errorMessage");
	Long totalUsers = (Long) request.getAttribute("totalUsers");
	Long totalAgencies = (Long) request.getAttribute("totalAgencies");
	List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
	List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
	Map<Integer, BookingResponseDTO> bookingMap = (Map<Integer, BookingResponseDTO>) request.getAttribute("bookingMap");
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

	<!-- Hero Section -->
	<section class="hero">
		<div class="hero-content">
			<h1>Tour & Travel Management System</h1>
			<p>Book your dream destinations with ease and comfort</p>
			<%
			if (user == null && agency == null) {
			%>
			<a href="login.jsp?role=agency" class="btn-started">🏢 Login as
				Agency</a>
			<%
			} else {
			%>
			<a href="login.jsp" class="btn-started">🚀 Get Started</a>
			<%
			}
			%>
		</div>
	</section>


	<%
	if (agency == null) {
	%>
	<section class="my-5 px-3">
		<h2 class="text-center fw-bold mb-5">Our Top Packages</h2>
		<div id="packagesCarousel" class="carousel slide"
			data-bs-ride="carousel" data-bs-interval="2000" data-bs-pause="hover">
			<div class="carousel-inner">
				<%
				if (packages != null && !packages.isEmpty()) {
					int idx = 0;
					for (int i = 0; i < packages.size(); i += 3) {
				%>
				<div class="carousel-item <%=(idx == 0) ? "active" : ""%>">
					<div class="row justify-content-center gx-4">
						<%
						for (int j = i; j < i + 3 && j < packages.size(); j++) {
							PackageResponseDTO pkg = packages.get(j);
							BookingResponseDTO userBooking = (bookingMap != null) ? bookingMap.get(pkg.getPackageId()) : null;
						%>
						<div class="col-12 col-md-6 col-lg-4 mb-4">
							<div class="package-card">
								<img
									src="<%=pkg.getImageurl() != null ? pkg.getImageurl() : "images/default.jpg"%>"
									class="card-img-top" alt="<%=pkg.getTitle()%>">
								<div class="package-card-body">
									<h5><%=pkg.getTitle()%></h5>
									<p class="price-location">
										<i class="fas fa-map-marker-alt"></i>
										<%=pkg.getLocation()%>
										| <i class="fas fa-clock"></i>
										<%=pkg.getDuration()%>
										Days | $<%=pkg.getPrice()%>
									</p>
									<p><%=pkg.getDescription()%></p>

									<%
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
                                (function(){
                                    const departure = new Date("<%=departure%>");
                                    const el = document.getElementById("carousel-countdown-<%=pkg.getPackageId()%>");
                                    const updateCountdown = () => {
                                        const now = new Date();
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
                                    setInterval(updateCountdown, 1000);
                                    updateCountdown();
                                })();
                                </script>
									<%
									} else {
									java.time.LocalDateTime lastBookingDate = pkg.getLastBookingDate();
									%>
									<div class="mt-2 p-2 bg-light rounded shadow-sm text-center">
										<small class="d-block fw-bold">Last Booking Till:</small> <small
											class="d-block"><%=lastBookingDate != null ? lastBookingDate.toLocalDate() + " " + lastBookingDate.toLocalTime() : "N/A"%></small>
										<small class="d-block text-danger"
											id="lastbooking-countdown-<%=pkg.getPackageId()%>"></small>
									</div>
									<script>
                                (function(){
                                    const lastBooking = new Date("<%=lastBookingDate != null ? lastBookingDate.toString().replace(" ", "T") : ""%>");
                                    const elLast = document.getElementById("lastbooking-countdown-<%=pkg.getPackageId()%>");
                                    if (!isNaN(lastBooking.getTime())) {
                                        const intervalLast = setInterval(() => {
                                            const now = new Date();
                                            if (now >= lastBooking) {
                                                elLast.innerText = "Booking Closed";
                                                clearInterval(intervalLast);
                                                return;
                                            }
                                            const diff = lastBooking - now;
                                            const d = Math.floor(diff / (1000*60*60*24));
                                            const h = Math.floor((diff % (1000*60*60*24)) / (1000*60*60));
                                            const m = Math.floor((diff % (1000*60*60)) / (1000*60));
                                            const s = Math.floor((diff % (1000*60)) / 1000);
                                            elLast.innerText = d+"d "+h+"h "+m+"m "+s+"s left";
                                        }, 1000);
                                    }
                                })();
                                </script>
									<%
									}
									%>

									<%
									if (userBooking == null || !"CONFIRMED".equalsIgnoreCase(userBooking.getStatus())) {
									%>
									<form action="<%=request.getContextPath()%>/booking"
										method="post" class="mt-auto">
										<input type="hidden" name="packageId"
											value="<%=pkg.getPackageId()%>">
										<button type="submit" name="button" value="viewBookingForm"
											class="btn-book-now w-100">Book Now</button>
									</form>
									<%
									} else {
									%>
									<button class="btn-booked w-100">Already Booked</button>
									<%
									}
									%>

								</div>
							</div>
						</div>
						<%
						}
						%>
					</div>
				</div>
				<%
				idx++;
				}
				} else {
				%>
				<p class="text-center">No packages available.</p>
				<%
				}
				%>
			</div>

			<!-- Carousel Controls -->
			<button class="carousel-control-prev" type="button"
				data-bs-target="#packagesCarousel" data-bs-slide="prev">
				<span class="carousel-control-prev-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Previous</span>
			</button>
			<button class="carousel-control-next" type="button"
				data-bs-target="#packagesCarousel" data-bs-slide="next">
				<span class="carousel-control-next-icon" aria-hidden="true"></span>
				<span class="visually-hidden">Next</span>
			</button>
	</section>

	<%
	}
	%>
	<!-- Stats Cards Section -->
	<section class="my-5">
		<div class="container-fluid px-5">
			<div class="row justify-content-center g-4">

				<!-- Users Card -->
				<div class="col-12 col-md-6 col-lg-6">
					<div class="card users text-center p-5 shadow-lg">
						<h4>Total Users Join Us</h4>
						<p class="fs-2 fw-bold"><%=totalUsers != null ? totalUsers : 0%></p>
						<i class="fa-solid fa-users fa-3x text-primary"></i>
					</div>
				</div>

				<!-- Agencies Card -->
				<div class="col-12 col-md-6 col-lg-6">
					<div class="card agencies text-center p-5 shadow-lg">
						<h4>Total Agencies Join Us</h4>
						<p class="fs-2 fw-bold"><%=totalAgencies != null ? totalAgencies : 0%></p>
						<i class="fa-solid fa-building fa-3x text-success"></i>
					</div>
				</div>

			</div>
		</div>
	</section>


	<!-- Features Section -->
	<section  id="features">
		<h2>Our Features</h2>
		<div class="features-grid">
			<div class="feature-box">
				<img src="https://cdn-icons-png.flaticon.com/512/3176/3176296.png">
				<h3>User Friendly</h3>
				<p>Easy registration, secure login, and personalized dashboards.</p>
			</div>
			<div class="feature-box">
				<img src="https://cdn-icons-png.flaticon.com/512/684/684908.png">
				<h3>Browse Packages</h3>
				<p>Find travel packages by location, price, and availability.</p>
			</div>
			<div class="feature-box">
				<img src="https://cdn-icons-png.flaticon.com/512/2169/2169870.png">
				<h3>Booking & Payments</h3>
				<p>Seamless booking and secure payment gateway integration.</p>
			</div>
			<div class="feature-box">
				<img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png">
				<h3>Admin Dashboard</h3>
				<p>Manage packages, bookings, inquiries, and reports easily.</p>
			</div>
		</div>
	</section>



<section id="about" style="padding:80px 10%; background:#f5f5f5;">
    <h2 style="text-align:center; color:#ff6f61; margin-bottom:50px;">About Us</h2>
    <div class="features-grid">
        <!-- Mission Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/190/190411.png" alt="Mission Icon">
            <h3>Our Mission</h3>
            <p>To make travel planning simple, secure, and enjoyable for every traveler.</p>
        </div>
        <!-- Services Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/684/684908.png" alt="Services Icon">
            <h3>Our Services</h3>
            <p>We provide verified travel packages, booking assistance, and personalized recommendations.</p>
        </div>
        <!-- User Friendly Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/3176/3176296.png" alt="User Friendly Icon">
            <h3>User Friendly</h3>
            <p>Browse packages, check details, and make bookings easily with our intuitive platform.</p>
        </div>
        <!-- Support Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="Support Icon">
            <h3>24/7 Support</h3>
            <p>Our team is available around the clock to help with queries or travel planning assistance.</p>
        </div>
    </div>
</section>



<section id="privacy" style="padding:80px 10%; background:#fff;">
    <h2 style="text-align:center; color:#ff6f61; margin-bottom:50px;">Privacy & Platform Fee</h2>
    <div class="features-grid">
        <!-- Security Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/3064/3064197.png" alt="Security Icon">
            <h3>Data Security</h3>
            <p>All personal and booking information is encrypted and securely stored.</p>
        </div>
        <!-- Platform Fee Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/1170/1170576.png" alt="Fee Icon">
            <h3>Platform Fee</h3>
            <p>A 5% fee applies to all bookings, ensuring platform maintenance and secure transactions.</p>
        </div>
        <!-- Transparency Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/2910/2910762.png" alt="Transparency Icon">
            <h3>Transparency</h3>
            <p>All fees are clearly displayed during booking, with detailed receipts provided.</p>
        </div>
        <!-- Privacy Card -->
        <div class="feature-box">
            <img src="https://cdn-icons-png.flaticon.com/512/565/565547.png" alt="Privacy Icon">
            <h3>User Privacy</h3>
            <p>Your data will never be shared without consent, ensuring complete confidentiality.</p>
        </div>
    </div>
</section>




	<!-- CTA Section -->
	<section class="cta">
		<h2>Plan Your Next Journey</h2>
		<p>Discover exciting travel packages and start your adventure
			today.</p>
	</section>

	<!-- Footer -->
	<div class="footer">&copy; 2025 Tour & Travel Management System |
		All Rights Reserved</div>
	<script>#packagesCarousel .carousel-item {
    transition: transform 0.4s ease-in-out; /* faster sliding */
}</script>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
