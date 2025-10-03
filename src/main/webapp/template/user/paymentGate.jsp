<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="header.jsp" %>
<html>
<head>
    <title>Complete Payment</title>
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>
    <style>
        body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
        .card { border: 1px solid #ccc; border-radius: 10px; padding: 30px; display: inline-block; }
        button { background: #528FF0; color: #fff; border: none; padding: 12px 20px; 
                 border-radius: 5px; font-size: 16px; cursor: pointer; }
        button:hover { background: #2a6adf; }
    </style>
</head>
<body>
<div class="card">
    <h2>Processing Payment...</h2>
    <p>Please wait while we open Razorpay checkout.</p>
</div>

<script>
    var options = {
        "key": "<%= request.getAttribute("razorpayKey") %>", // Razorpay Key
        "amount": <%= ((Number)request.getAttribute("amount")).doubleValue() * 100 %>, // in paise
        "currency": "INR",
        "name": "Tour Booking",
        "description": "Booking ID: <%= request.getAttribute("bookingId") %>",
        "order_id": "<%= request.getAttribute("razorpayOrderId") %>",
        "handler": function(response) {
            // Payment success → verify on server
            fetch('<%= request.getContextPath() %>/booking?button=verifyPayment', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(response)
            }).then(res => res.text())
            .then(msg => {
                alert(msg);
                window.location.href = '<%= request.getContextPath() %>/booking?button=bookingHistroy';
            })
            .catch(err => alert("Payment verification failed: " + err));
        },
        "modal": {
            "ondismiss": function() {
               
                alert("Payment cancelled by user.");
                window.location.href = '<%= request.getContextPath() %>/booking?button=viewBookingForm&packageId=<%= request.getAttribute("packageId") %>';
            }
        },
        "theme": { "color": "#3399cc" }
    };

    var rzp1 = new Razorpay(options);
    rzp1.open();
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
