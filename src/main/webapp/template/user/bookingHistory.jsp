<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.travelmanagement.dto.responseDTO.BookingResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>
<%@ include file="header.jsp"%>

<button type="button" class="btn btn-secondary" onclick="window.history.back();">Back</button>

<%
String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null && !errorMessage.isEmpty()) {
%>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
    <div class="toast show align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000" data-bs-autohide="true">
        <div class="d-flex">
            <div class="toast-body"><%=errorMessage%></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>
<%
}
%>

<h2 class="mb-4">My Booking History</h2>
<form method="get" action="booking" class="row g-2 mb-3">
    <input type="hidden" name="button" value="bookingHistroy">

    <div class="col-md-2">
        <select name="status" class="form-select">
            <option value="">All Status</option>
            <option value="PENDING" <%= "PENDING".equals(request.getParameter("status")) ? "selected" : "" %>>Pending</option>
            <option value="CONFIRMED" <%= "CONFIRMED".equals(request.getParameter("status")) ? "selected" : "" %>>Confirmed</option>
            <option value="CANCELLED" <%= "CANCELLED".equals(request.getParameter("status")) ? "selected" : "" %>>Cancelled</option>
        </select>
    </div>

    <div class="col-md-2">
        <label for="startDate" class="form-label"> From </label>
        <input type="date" name="startDate" class="form-control" placeholder="Start Date"
               value="<%=request.getParameter("startDate") != null ? request.getParameter("startDate") : ""%>"
               id="startDate" onchange="document.getElementById('endDate').min = this.value;">
    </div>

    <div class="col-md-2">
        <label for="endDate" class="form-label"> To </label>
        <input type="date" name="endDate" class="form-control" placeholder="End Date"
               value="<%=request.getParameter("endDate") != null ? request.getParameter("endDate") : ""%>"
               id="endDate">
    </div>

    <div class="col-md-2">
        <label for="pageSize">Records per page:</label>
        <select name="pageSize" id="pageSize" onchange="this.form.submit()">
            <option value="10" <%= "10".equals(request.getParameter("pageSize")) ? "selected" : "" %>>10</option>
            <option value="20" <%= "20".equals(request.getParameter("pageSize")) ? "selected" : "" %>>20</option>
            <option value="50" <%= "50".equals(request.getParameter("pageSize")) ? "selected" : "" %>>50</option>
        </select>
    </div>

    <div class="col-md-1">
        <button type="submit" class="btn btn-primary w-100">Filter</button>
    </div>
</form>

<div class="row g-4">
<%
List<BookingResponseDTO> bookings = (List<BookingResponseDTO>) request.getAttribute("bookings");
java.time.LocalDateTime now = java.time.LocalDateTime.now();
if (bookings != null && !bookings.isEmpty()) {
    for (BookingResponseDTO booking : bookings) {
        java.time.LocalDateTime departure = booking.getDepartureDateAndTime();
        String statusClass = "bg-danger";
        if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) statusClass = "bg-success";
        else if ("PENDING".equalsIgnoreCase(booking.getStatus())) statusClass = "bg-warning text-dark";
%>

<div class="col-md-6">
    <div class="card shadow-sm mb-4">
        <div class="row g-0">
            <div class="col-md-4">
                <img src="<%=booking.getPackageImage()%>" class="img-fluid rounded-start h-100 object-fit-cover" alt="<%=booking.getPackageName()%>">
            </div>
            <div class="col-md-8">
                <div class="card-body d-flex flex-column justify-content-between h-100">
                    <div>
                        <% if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) { %>
                        <div class="mb-2 text-center">
                            <h6 class="mb-1">Departure In:</h6>
                            <span id="countdown-<%=booking.getBookingId()%>" class="fw-bold fs-6 text-primary"></span>
                        </div>
                        <% } else { %>
                        <h6 class="mb-1">Departure Date: <%= departure != null ? departure.toLocalDate() : "Not Mentioned" %></h6>
                        <% } %>

                        <h5 class="card-title fw-bold"><%=booking.getPackageName()%></h5>
                        <p class="mb-1"><i class="fas fa-clock"></i> <%=booking.getDuration()%> Days</p>
                        <p class="mb-1"><i class="fas fa-users"></i> <%=booking.getNoOfTravellers()%> Travelers</p>
                        <p class="mb-1"><i class="fas fa-dollar-sign"></i> $<%=booking.getAmount()%></p>
                        <p class="mb-1"><i class="fas fa-calendar"></i> Booking Date: <%=booking.getBookingDate()%></p>
                        <span class="badge <%=statusClass%>"><%=booking.getStatus()%></span>
                    </div>

                    <div class="mt-3 d-flex gap-2">
                        <a href="<%=request.getContextPath()%>/booking?button=viewTravelers&bookingId=<%=booking.getBookingId()%>"
                           class="btn btn-sm btn-primary flex-grow-1">View Travelers</a>

                        <% if (("PENDING".equalsIgnoreCase(booking.getStatus()) || "CONFIRMED".equalsIgnoreCase(booking.getStatus()))
                                && departure != null && departure.isAfter(now)) { %>
                        <button type="button" class="btn btn-sm btn-danger flex-grow-1"
                                onclick="showCancelModal(<%=booking.getBookingId()%>, '<%=booking.getLastBookingDate()%>', <%=booking.getAmount()%>)">
                            Cancel Booking
                        </button>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
(function() {
    const departureStr = "<%=departure != null ? departure.toString() : ""%>";
    if (!departureStr) return;
    const departure = new Date(departureStr.replace(' ', 'T'));
    const countdownEl = document.getElementById("countdown-<%=booking.getBookingId()%>");

    const interval = setInterval(() => {
        const now = new Date();
        if (now >= departure || isNaN(departure.getTime())) {
            countdownEl.innerText = "Departed";
            if (!countdownEl.dataset.alertShown) {
                const toastHtml = `<div class="position-fixed top-0 end-0 p-3" style="z-index:1080">
                    <div class="toast show align-items-center text-bg-info border-0" role="alert" aria-live="assertive" aria-atomic="true">
                        <div class="d-flex">
                            <div class="toast-body">Your trip for <%=booking.getPackageName()%> has started today!</div>
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
<p class="text-center">No bookings found.</p>
<%
}
%>
</div>

<!-- Pagination -->
<nav>
    <ul class="pagination justify-content-center mt-3">
        <%
        Integer currentPageAttr = (Integer) request.getAttribute("currentPage");
        int currentPage = (currentPageAttr != null) ? currentPageAttr : 1;

        Integer totalPagesAttr = (Integer) request.getAttribute("totalPages");
        int totalPages = (totalPagesAttr != null) ? totalPagesAttr : 1;

        String statusParam = (request.getParameter("status") != null) ? request.getParameter("status") : "";
        String startDateParam = (request.getParameter("startDate") != null) ? request.getParameter("startDate") : "";
        String endDateParam = (request.getParameter("endDate") != null) ? request.getParameter("endDate") : "";
        String pageSizeParam = (request.getParameter("pageSize") != null) ? request.getParameter("pageSize") : "10";

        String queryParams = "status=" + statusParam + "&startDate=" + startDateParam + "&endDate=" + endDateParam
                + "&pageSize=" + pageSizeParam;
        %>

        <li class="page-item <%=currentPage == 1 ? "disabled" : ""%>">
            <a class="page-link" href="booking?button=bookingHistroy&page=<%=currentPage - 1%>&<%=queryParams%>">Previous</a>
        </li>

        <% for (int i = 1; i <= totalPages; i++) { %>
        <li class="page-item <%=currentPage == i ? "active" : ""%>">
            <a class="page-link" href="booking?button=bookingHistroy&page=<%=i%>&<%=queryParams%>"><%=i%></a>
        </li>
        <% } %>

        <li class="page-item <%=currentPage == totalPages ? "disabled" : ""%>">
            <a class="page-link" href="booking?button=bookingHistroy&page=<%=currentPage + 1%>&<%=queryParams%>">Next</a>
        </li>
    </ul>
</nav>

<!-- SINGLE CANCEL MODAL -->
<div class="modal fade" id="cancelModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="cancelModalLabel">Cancel Booking</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <p id="totalAmount" style="margin: 0;"></p>
                <p id="amountAfterGst" style="margin: 0;"></p>
                <p id="cancellationFee" style="margin: 0;"></p>
                <hr>
                <p id="refundableAmount" style="margin: 0;"></p>
            </div>
            <div class="modal-footer">
                <form id="confirmCancelForm" method="post" action="<%=request.getContextPath()%>/booking">
                    <input type="hidden" name="button" value="cancelBooking">
                    <input type="hidden" name="bookingId" id="modalBookingId">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-danger">Confirm Cancel</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Include Bootstrap CSS and JS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
function updateModalContent(bookingId, totalAmount, amountAfterGST, cancelFee, refundAmount) {
    console.log("Updating modal with:", { bookingId, totalAmount, amountAfterGST, cancelFee, refundAmount }); // Debug the input values
    const totalAmountEl = document.getElementById("totalAmount");
    const amountAfterGstEl = document.getElementById("amountAfterGst");
    const cancellationFeeEl = document.getElementById("cancellationFee");
    const refundableAmountEl = document.getElementById("refundableAmount");
    const bookingIdEl = document.getElementById("modalBookingId");

  
    if (totalAmountEl && amountAfterGstEl && cancellationFeeEl && refundableAmountEl && bookingIdEl) {
        totalAmountEl.innerText = 'Total Amount: ₹'+totalAmount.toFixed(2);
        amountAfterGstEl.innerText = 'Amount After GST Charges(18%): ₹'+amountAfterGST.toFixed(2);
        cancellationFeeEl.innerText = 'Cancellation Charge: ₹'+cancelFee.toFixed(2);
        refundableAmountEl.innerText = 'Refundable Amount: ₹'+refundAmount.toFixed(2);
        bookingIdEl.value = bookingId;
        console.log("Modal content updated successfully");
        console.log("Modal body content:", document.getElementById("cancelModal").querySelector(".modal-body").innerHTML);
    } else {
        console.error("One or more modal elements not found:", {
            totalAmountEl, amountAfterGstEl, cancellationFeeEl, refundableAmountEl, bookingIdEl
        });
    }
}

function showCancelModal(bookingId, lastBookingDateStr, amount) {
    let totalAmount = Number(amount) || 0;
    let lastBookingDate = new Date(lastBookingDateStr.replace(' ', 'T'));
    if (isNaN(lastBookingDate.getTime())) {
        console.error("Invalid lastBookingDate:", lastBookingDateStr);
        return;
    }

    const now = new Date(); // 06:52 PM IST, September 30, 2025
    let daysDiff = Math.ceil((lastBookingDate - now) / (1000 * 60 * 60 * 24));

    let refundPercent = 0;
    if (daysDiff >= 7) refundPercent = 100;
    else if (daysDiff >= 3) refundPercent = 50;
    else if (daysDiff >= 1) refundPercent = 25;

    let amountAfterGST = totalAmount / 1.18;
    let refundAmount = (totalAmount * refundPercent) / 100;
    let cancelFee = totalAmount - refundAmount;

    // Log initial calculations
    console.log({
        totalAmount: totalAmount.toFixed(2),
        amountAfterGST: amountAfterGST.toFixed(2),
        cancelFee: cancelFee.toFixed(2),
        refundAmount: refundAmount.toFixed(2)
    });

    // Get modal
    const modal = document.getElementById('cancelModal');
    if (modal) {
        const bootstrapModal = new bootstrap.Modal(modal, { backdrop: 'static', keyboard: false });

        // Show modal and update content when shown
        bootstrapModal.show();
        modal.addEventListener('shown.bs.modal', function() {
            updateModalContent(bookingId, totalAmount, amountAfterGST, cancelFee, refundAmount);
        }, { once: true }); // Use { once: true } to remove listener after execution
    } else {
        console.error("Modal element not found!");
    }
}
</script>

<%@ include file="footer.jsp"%>