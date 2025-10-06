<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="header.jsp"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.TravelerResponseDTO"%>
<%

List<TravelerResponseDTO> travelers = (List<TravelerResponseDTO>) request.getAttribute("travelers");
%>

<h2 class="mb-3">👥 Travellers</h2>
<%
String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 10;
int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "viewTravelers";
String queryParams = "keyword=" + (request.getParameter("keyword") != null ? request.getParameter("keyword") : "")
+"&bookingId="+(request.getParameter("bookingId") != null ? request.getParameter("bookingId") : "")
		+ "&pageSize=" + pageSize;
%>

<table class="table table-bordered shadow-sm">
	<thead class="table-dark">
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Email</th>
			<th>Mobile</th>
			<th>Age</th>
			<th>Status</th>
		</tr>
	</thead>
	<tbody>
		<%
		if (travelers != null && !travelers.isEmpty()) {
			for (TravelerResponseDTO t : travelers) {
		%>
		<tr>
			<td><%=t.getId()%></td>
			<td><%=t.getName()%></td>
			<td><%=t.getEmail()%></td>
			<td><%=t.getMobile()%></td>
			<td><%=t.getAge()%></td>
			<td><span
				class="badge <%="CONFIRMED".equals(t.getStatus()) ? "bg-success" : "bg-danger"%>">
					<%=t.getStatus()%>
			</span></td>
		</tr>
		<%
		}
		} else {
		%><tr>
			<td colspan="6" class="text-center">No travelers found.</td>
		</tr>
		<%
		}
		%>
	</tbody>
</table>



<!-- Pagination Section -->
<nav>
	<ul class="pagination justify-content-center">
		<%
		if (currentPage > 1) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage - 1%>&button=<%=buttonParam%>">Prev</a>
		</li>
		<%
		}
		%>

		<%
		for (int i = startPage; i <= endPage; i++) {
		%>
		<li class="page-item <%=(i == currentPage) ? "active" : ""%>"><a
			class="page-link"
			href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
		</li>
		<%
		}
		%>

		<%
		if (currentPage < totalPages) {
		%>
		<li class="page-item"><a class="page-link"
			href="?<%=queryParams%>&page=<%=currentPage + 1%>&button=<%=buttonParam%>">Next</a>
		</li>
		<%
		}
		%>
	</ul>
</nav>
<%@ include file="footer.jsp"%>
