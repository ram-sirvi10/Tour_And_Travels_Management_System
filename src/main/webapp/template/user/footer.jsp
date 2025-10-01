</div>
<!-- container -->

<footer class="footer mt-5 bg-dark text-white pt-5 pb-3">
	<div class="container">
		<div class="row">
			<!-- About -->
			<div class="col-md-4 mb-4">
				<h5 class="fw-bold">About TravelBuddy</h5>
				<p>TravelBuddy is your ultimate tour and travel companion.
					Explore destinations, book packages, and manage your trips
					seamlessly.</p>
			</div>

			<!-- Quick Links -->
			<div class="col-md-2 mb-4">
				<h5 class="fw-bold">Quick Links</h5>
				<ul class="list-unstyled">
					<li><a
						href="<%=request.getContextPath()%>/user?button=dashboard"
						class="text-white text-decoration-none">Dashboard</a></li>
					<li><a
						href="<%=request.getContextPath()%>/package?button=packageList"
						class="text-white text-decoration-none">Packages</a></li>
					<li><a
						href="<%=request.getContextPath()%>/booking?button=bookingHistroy"
						class="text-white text-decoration-none">My Bookings</a></li>
					<li><a
						href="<%=request.getContextPath()%>/booking?button=paymentHistory"
						class="text-white text-decoration-none">Payments</a></li>
				</ul>
			</div>

			<!-- Contact Info -->
			<div class="col-md-3 mb-4">
				<h5 class="fw-bold">Contact Us</h5>
				<p>
					<i class="fas fa-map-marker-alt"></i> 5, Vishnu Puri, 103, Sukhmani
					Apartment, Above Union Bank AB Road, Bhawarkua Main Rd, Indore,
					Madhya Pradesh 452014
				</p>
				<p>
					<i class="fas fa-phone"></i> +1 234 567 890
				</p>
				<p>
					<i class="fas fa-envelope"></i> support@travelmate.com
				</p>
			</div>

			<!-- Newsletter -->
			<div class="col-md-3 mb-4">
				<h5 class="fw-bold">Newsletter</h5>
				<p>Subscribe to get latest travel deals and updates:</p>
				<form class="d-flex" action="#" method="post">
					<input type="email" class="form-control me-2" placeholder="Email"
						required>
					<button class="btn btn-primary" type="submit">Subscribe</button>
				</form>
				<div class="mt-3">
					<a href="#" class="text-white me-3"><i
						class="fab fa-facebook fa-lg"></i></a> <a href="#"
						class="text-white me-3"><i class="fab fa-twitter fa-lg"></i></a> <a
						href="#" class="text-white me-3"><i
						class="fab fa-instagram fa-lg"></i></a> <a href="#" class="text-white"><i
						class="fab fa-linkedin fa-lg"></i></a>
				</div>
			</div>
		</div>

		<hr class="bg-secondary">
		<div class="text-center">&copy; 2025 TravelBuddy. All Rights
			Reserved. Designed by YourTeam.</div>
	</div>
</footer>
<script>

window.addEventListener("pageshow", function(event) {
    if (event.persisted || (window.performance && window.performance.getEntriesByType("navigation")[0].type === "back_forward")) {

        window.location.reload();
    }
});
</script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
