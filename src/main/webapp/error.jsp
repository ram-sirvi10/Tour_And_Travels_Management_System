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

String errorMessage = "An unexpected error occurred!";
if (errorMessage == null || errorMessage.isEmpty()) {
	switch (statusCode) {
	case 404:
		errorMessage = "Oops! Page Not Found.";
		break;
	case 403:
		errorMessage = "Access Denied! You don't have permission.";
		break;
	case 500:
		errorMessage = "Internal Server Error! Something went wrong.";
		break;
	default:
		errorMessage = "An unexpected error occurred!";
	}
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
<title>Error <%=statusCode%></title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<style>
body {
	margin: 0;
	padding: 0;
	background: linear-gradient(135deg, #ff6b6b, #f06595);
	height: 100vh;
	display: flex;
	align-items: center;
	justify-content: center;
	font-family: 'Poppins', sans-serif;
	color: #fff;
	overflow: hidden;
}

.error-container {
	text-align: center;
	max-width: 600px;
	animation: fadeIn 1s ease-in-out;
}

h1 {
	font-size: 10rem;
	margin: 0;
	animation: float 3s ease-in-out infinite;
}

h2 {
	font-size: 2rem;
	margin-bottom: 20px;
}

p {
	font-size: 1.1rem;
	margin: 10px 0;
}

a {
	display: inline-block;
	margin-top: 20px;
	padding: 10px 25px;
	background-color: #fff;
	color: #ff6b6b;
	text-decoration: none;
	font-weight: bold;
	border-radius: 50px;
	transition: 0.3s;
}

a:hover {
	background-color: #f06595;
	color: #fff;
	transform: scale(1.05);
}

@
keyframes float { 0%, 100% {
	transform: translateY(0);
}

50


%
{
transform


:


translateY
(


-20px


)
;


}
}
@
keyframes fadeIn { 0% {
	opacity: 0;
	transform: scale(0.8);
}

100


%
{
opacity


:


1
;


transform


:


scale
(


1


)
;


}
}
/* Background circles */
.circle {
	position: absolute;
	border-radius: 50%;
	background: rgba(255, 255, 255, 0.1);
	animation: floatCircle 10s linear infinite;
}

.circle:nth-child(1) {
	width: 200px;
	height: 200px;
	left: 10%;
	top: 20%;
}

.circle:nth-child(2) {
	width: 300px;
	height: 300px;
	left: 70%;
	top: 10%;
}

.circle:nth-child(3) {
	width: 150px;
	height: 150px;
	left: 50%;
	top: 70%;
}

@
keyframes floatCircle { 0% {
	transform: translateY(0) translateX(0);
}
50


%
{
transform


:


translateY
(


-50px


)


translateX
(


30px


)
;


}
100


%
{
transform


:


translateY
(


0


)


translateX
(


0


)
;


}
}
@media (max-width: 576px) {
  h1 { font-size: 5rem; }
  h2 { font-size: 1.2rem; }
}

</style>
</head>
<body>

	<div class="circle"></div>
	<div class="circle"></div>
	<div class="circle"></div>

	<div class="error-container">
		<h1><%=statusCode%></h1>
		<h2><%=errorMessage%></h2>
		<p>
			Requested URL: <strong><%=requestUri%></strong>
		</p>
		<p>
			Time:
			<%=LocalDateTime.now()%></p>
		<a href="<%=request.getContextPath()%>/index.jsp">Go to Home</a>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
