<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="header.jsp"%>
<html>
<head>
<title>Complete Payment</title>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<style>
body {
	font-family: Arial, sans-serif;
	text-align: center;
	margin-top: 50px;
}

.card {
	border: 1px solid #ccc;
	border-radius: 10px;
	padding: 30px;
	display: inline-block;
}

button {
	background: #528FF0;
	color: #fff;
	border: none;
	padding: 12px 20px;
	border-radius: 5px;
	font-size: 16px;
	cursor: pointer;
}

button:hover {
	background: #2a6adf;
}
</style>
</head>
<body>
	<div class="card">
		<h2>Processing Payment...</h2>
		<p>Please wait while we open Razorpay checkout.</p>
	</div>

	<script>
var totalTime = 5 * 60;
var timerInterval;
var autoCancelTimeout;
let paymentCompleted = false; 
let rzp1;


function startCountdown() {
    Swal.fire({
        title: 'Complete your payment',
        html: 'Remaining time: <b></b> seconds',
        timer: totalTime * 1000,
        didOpen: () => {
            const b = Swal.getHtmlContainer().querySelector('b');
            timerInterval = setInterval(() => { 
                totalTime--; 
                b.textContent = totalTime; 
                if(totalTime <= 0) clearInterval(timerInterval); 
            }, 1000);
        },
        willClose: () => { clearInterval(timerInterval); },
        showConfirmButton: false,
        allowOutsideClick: false,
        allowEscapeKey: false
    });
}


function cancelBooking(msgTitle, msgText) {
    fetch('<%=request.getContextPath()%>/booking?button=autoCancelBooking', { method: 'POST' })
    .then(res => res.text())
    .then(msg => {
        Swal.fire({ title: msgTitle, html:'<p>'+msgText+'</p>', icon:'error', confirmButtonText:'OK' })
        .then(()=>{ window.location.href="<%=request.getContextPath()%>/booking?button=bookingHistroy"; });
    })
    .catch(err => {
        Swal.fire({title:'Error', text:'Something went wrong!', icon:'error', confirmButtonText:'OK'});
    });
}


startCountdown();

window.onload = function() {
    var options = {
        "key": "<%=request.getAttribute("razorpayKey")%>",
        "amount": <%=((Number) request.getAttribute("amount")).intValue() * 100%>, 
        "currency": "INR",
        "name": "Tour Booking",
        "description": "Booking ID: <%=request.getAttribute("bookingId")%>",
        "order_id": "<%=request.getAttribute("razorpayOrderId")%>",
        "handler": function(response) {
            paymentCompleted = true;
            clearInterval(timerInterval);
            clearTimeout(autoCancelTimeout);

          
            const formData = new URLSearchParams({
                razorpay_payment_id: response.razorpay_payment_id,
                razorpay_order_id: response.razorpay_order_id,
                razorpay_signature: response.razorpay_signature
            });

            fetch('<%=request.getContextPath()%>/booking?button=verifyPayment', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData
            })
            .then(res => res.text())
            .then(msg => {
                const success = msg.includes("Successful");
                Swal.fire({
                    title: success ? 'Success!' : 'Oops!',
                    text: msg,
                    icon: success ? 'success' : 'error',
                    confirmButtonText: 'OK'
                }).then(() => {
                    window.location.href='<%=request.getContextPath()%>/booking?button=viewBookings';
                });
            });
        },
        "modal": { 
            "ondismiss": function() { 
                if(!paymentCompleted) cancelBooking('Booking Cancelled','Payment not completed'); 
            } 
        }
    };

    rzp1 = new Razorpay(options);
    rzp1.open();

    
    autoCancelTimeout = setTimeout(()=>{
        if(!paymentCompleted){
            try{ rzp1.close(); }catch(e){}
            cancelBooking('Booking Cancelled','Payment time expired');
        }
    }, 5*60*1000);
};


const bookingId = "<%=request.getAttribute("bookingId")%>";


function markPaymentCompleted() { paymentCompleted = true; }


window.addEventListener("beforeunload", function (e) {
    if (!paymentCompleted && bookingId) {
        const url = "<%=request.getContextPath()%>/booking?button=autoCancelBooking";
        const data = new FormData();
        data.append("bookingId", bookingId);
        data.append("reason", "Payment window closed");
        navigator.sendBeacon(url, data);
    }
});
</script>

	<%@ include file="footer.jsp"%>
</body>
</html>
