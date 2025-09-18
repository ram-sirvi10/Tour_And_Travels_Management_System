<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>

<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ADMIN Dashboard</title>
<style>
button {
	padding: 8px 16px;
	font-size: 14px;
	cursor: pointer;
}
</style>
</head>
<body>
	<h1>
		Welcome,
		<%=user.getUserName()%>!
	</h1>

	<p>This is your ADMIN dashboard.</p>

	<!-- Logout Button -->
	<form action="<%=request.getContextPath()%>/AuthServlet" method="post">
		<input type="hidden" name="button" value="logout">
		<button type="submit">Logout</button>
	</form>

</body>
</html>
