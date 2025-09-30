<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.TravelerResponseDTO"%>
<%@ include file="header.jsp"%>
<button type="button" class="btn btn-secondary" onclick="window.history.back();">Back</button>
<% 
    String errorMessage = (String) request.getAttribute("errorMessage");
 if (errorMessage != null && !errorMessage.isEmpty()) { %>
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080">
  <div class="toast show align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-delay="3000" data-bs-autohide="true">
    <div class="d-flex">
      <div class="toast-body">
        <%= errorMessage %>
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  </div>
</div>
<% } %>

<h2 class="mb-4">Travelers List</h2>

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
		+ "&pageSize=" + pageSize;
%>

<!-- Search Form -->
<form method="get" action="traveler" class="row g-2 mb-4">
	<input type="hidden" name="button" value="viewTravelers"> <input
		type="hidden" name="bookingId"
		value="<%=request.getParameter("bookingId")%>">
	<div class="col-md-4">
		<input type="text" name="keyword" class="form-control"
			placeholder="Search travelers..." value="<%=keyword%>">
	</div>
	<div class="col-md-2">
		<button type="submit" class="btn btn-primary w-100">Search</button>
	</div>
	<div class="col-md-2">
		Records per page: <select name="pageSize" class="form-select"
			onchange="this.form.submit()">
			<option value="10" <%=pageSize == 10 ? "selected" : ""%>>10</option>
			<option value="20" <%=pageSize == 20 ? "selected" : ""%>>20</option>
			<option value="30" <%=pageSize == 30 ? "selected" : ""%>>30</option>
			<option value="50" <%=pageSize == 50 ? "selected" : ""%>>50</option>
		</select>
	</div>
</form>

<div class="card shadow p-3 bg-white">
	<div class="table-responsive">
		<table class="table table-hover align-middle">
			<thead class="table-dark">
				<tr>
					<th>#</th>
					<th>Name</th>
					<th>Email</th>
					<th>Mobile</th>
					<th>Age</th>
					<th>Package</th>
				</tr>
			</thead>
			<tbody>
				<%
				List<TravelerResponseDTO> travelers = (List<TravelerResponseDTO>) request.getAttribute("travelers");
				if (travelers != null && !travelers.isEmpty()) {
					int count = (currentPage - 1) * pageSize + 1;
					for (TravelerResponseDTO t : travelers) {
				%>
				<tr>
					<td><%=count++%></td>
					<td><%=t.getName()%></td>
					<td><%=t.getEmail()%></td>
					<td><%=t.getMobile()%></td>
					<td><%=t.getAge()%></td>
					<td><%=request.getAttribute("packageName") != null ? request.getAttribute("packageName") : ""%></td>
				</tr>
				<%
				}
				} else {
				%>
				<tr>
					<td colspan="6" class="text-center">No travelers found.</td>
				</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>
</div>

<%-- Pagination Section --%>
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
