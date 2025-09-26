<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>

<%
    Integer bookingIdAttr = (Integer) request.getAttribute("bookingId");
    Double amountAttr = (Double) request.getAttribute("amount");
    String message = (String) request.getAttribute("message");

    int bookingId = (bookingIdAttr != null) ? bookingIdAttr : 0;
    double amount = (amountAttr != null) ? amountAttr : 0.0;

    String alertClass = "alert-info";
    boolean showDashboard = false;

    if (message != null) {
        showDashboard = true; // show dashboard button only after payment result
        if (message.toLowerCase().contains("successful")) {
            alertClass = "alert-success";
        } else if (message.toLowerCase().contains("rejected") || message.toLowerCase().contains("cancelled")) {
            alertClass = "alert-danger";
        }
    }
%>

<div class="container mt-5">
    <h2 class="mb-4">Payment Details</h2>

    <% if (message != null) { %>
        <div class="alert <%= alertClass %> alert-dismissible fade show" role="alert">
            <%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <div class="card shadow p-4 bg-white">
        <h5>Booking ID: <strong><%= bookingId %></strong></h5>
        <h4 class="mt-3">Total Amount to Pay: <strong>₹ <%= amount %></strong></h4>

        <% if (!showDashboard && bookingId > 0) { %>
        <form action="<%= request.getContextPath() %>/booking" method="post" class="mt-4">
            <input type="hidden" name="bookingId" value="<%= bookingId %>">
            <input type="hidden" name="amount" value="<%= amount %>">

            <button type="submit" name="button" value="paymentConfirm" class="btn btn-success me-3">
                Confirm Payment
            </button>

            <button type="submit" name="button" value="paymentReject" class="btn btn-danger">
                Reject Payment
            </button>
        </form>
        <% } else if (showDashboard) { %>
            <a href="<%= request.getContextPath() %>/user/dashboard.jsp" class="btn btn-primary mt-3">
                Go to Dashboard
            </a>
        <% } %>
    </div>
</div>

<%@ include file="footer.jsp" %>
