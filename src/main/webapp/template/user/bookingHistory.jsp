<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<h2 class="mb-4">My Booking History</h2>

<div class="row g-4">
    <%-- Sample static bookings; replace with DB fetch dynamically --%>
    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="row g-0">
                <div class="col-md-4">
                    <img src="images/paris.jpg" class="img-fluid rounded-start" alt="Paris Delight">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title fw-bold">Paris Delight</h5>
                        <p class="mb-1"><i class="fas fa-calendar-alt"></i> Booking ID: 101</p>
                        <p class="mb-1"><i class="fas fa-clock"></i> 5 Days</p>
                        <p class="mb-1"><i class="fas fa-users"></i> 2 Travelers</p>
                        <p class="mb-1"><i class="fas fa-dollar-sign"></i> $1200</p>
                        <span class="badge bg-success">Confirmed</span>
                        <a href="TravelersList.jsp?bookingId=101" class="btn btn-sm btn-primary float-end">View Travelers</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="row g-0">
                <div class="col-md-4">
                    <img src="images/rome.jpg" class="img-fluid rounded-start" alt="Rome Adventure">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title fw-bold">Rome Adventure</h5>
                        <p class="mb-1"><i class="fas fa-calendar-alt"></i> Booking ID: 102</p>
                        <p class="mb-1"><i class="fas fa-clock"></i> 4 Days</p>
                        <p class="mb-1"><i class="fas fa-users"></i> 1 Traveler</p>
                        <p class="mb-1"><i class="fas fa-dollar-sign"></i> $950</p>
                        <span class="badge bg-warning text-dark">Pending</span>
                        <a href="TravelersList.jsp?bookingId=102" class="btn btn-sm btn-primary float-end">View Travelers</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
