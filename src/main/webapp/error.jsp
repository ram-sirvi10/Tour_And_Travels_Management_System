<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.time.LocalDateTime"%>

<%
Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
if (statusCode == null) {
    statusCode = response.getStatus();
}
if (statusCode == 0)
    statusCode = 500;

String errorMessage = "";
switch (statusCode) {
    case 404: errorMessage = "Oops! Page Not Found."; break;
    case 403: errorMessage = "Access Denied! You don't have permission."; break;
    case 500: errorMessage = "Internal Server Error! Something went wrong."; break;
    default: errorMessage = "An unexpected error occurred!"; break;
}

String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
if (requestUri == null || requestUri.isEmpty()) {
    requestUri = request.getHeader("referer");
    if (requestUri == null || requestUri.isEmpty()) {
        requestUri = "Unknown";
    }
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Error <%=statusCode%> - Travel Site</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
<style>
/* Full-screen layout */
body, html {
    margin: 0;
    padding: 0;
    height: 100%;
    width: 100%;
    font-family: 'Poppins', sans-serif;
    overflow: hidden;
    position: relative;
    background: url('images/travel-background.jpg') no-repeat center center fixed;
    background-size: cover;
    color: #fff;
}

/* Dark overlay for readability */
.overlay {
    position: absolute;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(0,0,0,0.5);
    z-index: 1;
}

/* Main content - centered and full screen */
.error-content {
    position: relative;
    z-index: 2;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    padding: 20px;
}

.error-content h1 {
    font-size: 12rem;
    margin: 0;
    text-shadow: 2px 2px 20px rgba(0,0,0,0.7);
    animation: float 4s ease-in-out infinite;
}

.error-content h2 {
    font-size: 2.5rem;
    margin: 20px 0;
    display: flex;
    align-items: center;
    gap: 15px;
    text-shadow: 1px 1px 10px rgba(0,0,0,0.6);
}

.error-content p {
    font-size: 1.2rem;
    margin: 10px 0;
    background: rgba(0,0,0,0.3);
    padding: 8px 15px;
    border-radius: 10px;
}

.error-content a {
    display: inline-block;
    margin-top: 30px;
    padding: 15px 40px;
    background-color: #ff6b6b;
    color: #fff;
    text-decoration: none;
    font-weight: bold;
    border-radius: 50px;
    transition: all 0.3s ease;
}

.error-content a:hover {
    background-color: #f06595;
    transform: scale(1.05);
}

/* Plane animation across screen */
.plane {
    position: absolute;
    width: 120px;
    height: 120px;
    background: url('images/plane.png') no-repeat center center;
    background-size: contain;
    animation: fly 20s linear infinite;
    z-index: 2;
}

/* Floating animation for error code */
@keyframes float {
    0%,100% { transform: translateY(0); }
    50% { transform: translateY(-25px); }
}

/* Plane movement */
@keyframes fly {
    0% { left: -150px; top: 20%; transform: rotate(0deg); }
    50% { left: 50%; top: 10%; transform: rotate(15deg); }
    100% { left: 100%; top: 25%; transform: rotate(-15deg); }
}

/* Responsive */
@media (max-width: 768px) {
    .error-content h1 { font-size: 8rem; }
    .error-content h2 { font-size: 2rem; }
    .error-content a { padding: 12px 25px; font-size: 1rem; }
}
</style>
</head>
<body>

<div class="overlay"></div>
<div class="plane"></div>

<div class="error-content">
    <h1><%=statusCode%></h1>
    <h2><i class="fa-solid fa-plane-departure"></i> <%=errorMessage%></h2>
    <p>Requested URL: <strong><%=requestUri%></strong></p>
    <p>Time: <%=LocalDateTime.now()%></p>
    <a href="<%=request.getContextPath()%>/">✈️ Back to Home</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
