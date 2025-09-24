<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<h2 class="mb-4">Explore Our Packages</h2>
<div class="row g-4">
<div class="col-md-4">
    <div class="card h-100 shadow-sm">
        <img src="images/paris.jpg" class="card-img-top" alt="Paris">
        <div class="card-body">
            <h5 class="card-title fw-bold">Paris Delight</h5>
            <p class="text-muted mb-1"><i class="fas fa-map-marker-alt"></i> France</p>
            <p class="mb-1"><i class="fas fa-clock"></i> 5 Days</p>
            <p class="mb-1"><i class="fas fa-dollar-sign"></i> $1200</p>
            <p class="card-text text-truncate">Explore the city of love, visit Eiffel Tower, Louvre Museum, and enjoy local cuisine.</p>
            <a href="booking.jsp?packageId=1" class="btn btn-primary w-100 mt-2">Book Now</a>
        </div>
    </div>
</div>
</div>
<%@ include file="footer.jsp" %>
