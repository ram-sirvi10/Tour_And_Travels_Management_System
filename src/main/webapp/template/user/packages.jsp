<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="header.jsp"%>
<%@ page import="java.util.*, com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page import="com.travelmanagement.dto.responseDTO.UserResponseDTO"%>

<%
 user = (UserResponseDTO) session.getAttribute("user");
if (user == null) {
    response.sendRedirect(request.getContextPath() + "/login.jsp");
    return;
}

List<PackageResponseDTO> packages = (List<PackageResponseDTO>) request.getAttribute("packages");
String keyword = request.getAttribute("keyword") != null ? (String) request.getAttribute("keyword") : "";
int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
int pageSize = request.getAttribute("pageSize") != null ? (int) request.getAttribute("pageSize") : 10;

int windowSize = 3;
int startPage = ((currentPage - 1) / windowSize) * windowSize + 1;
int endPage = Math.min(startPage + windowSize - 1, totalPages);

String buttonParam = "packageList"; 
String queryParams = "keyword=" + (request.getParameter("keyword") != null ? request.getParameter("keyword") : "")
                    + "&pageSize=" + pageSize;

%>

<% String errorMessage = (String) request.getAttribute("errorMessage");
if (errorMessage != null && !errorMessage.isEmpty()) { %>
    <div style="color: red; font-weight: bold; margin: 10px 0;">
        <%= errorMessage %>
    </div>
<% } %>

<h2 class="mb-4">Explore Our Packages</h2>

<form method="get" action="<%=request.getContextPath()%>/package" class="row g-2 mb-4">
    <input type="hidden" name="button" value="packageList">
    <div class="col-md-4">
        <input type="text" name="keyword" class="form-control"
               placeholder="Search packages..." value="<%= keyword %>">
    </div>
    <div class="col-md-2">
        <button type="submit" class="btn btn-primary w-100">Search</button>
    </div>
    <div class="col-md-2"> Record per page : 
        <select name="pageSize" class="form-select" onchange="this.form.submit()">
            <option value="10" <%= pageSize==10 ? "selected" : "" %>>10</option>
            <option value="20" <%= pageSize==20 ? "selected" : "" %>>20</option>
            <option value="30" <%= pageSize==30 ? "selected" : "" %>>30</option>
            <option value="40" <%= pageSize==40 ? "selected" : "" %>>40</option>
        </select>
    </div>
</form>

<div class="row g-4">
<%
if (packages != null && !packages.isEmpty()) {
    int serial = (currentPage - 1) * pageSize + 1;
    for (PackageResponseDTO pkg : packages) {
%>
    <div class="col-md-4">
        <div class="card h-100 shadow-sm">
            <img src="<%= pkg.getImageurl() != null ? pkg.getImageurl() : "images/default.jpg" %>"
                 class="card-img-top" alt="<%= pkg.getTitle() %>">
            <div class="card-body">
                <h5 class="card-title fw-bold"><%= pkg.getTitle() %></h5>
                <p class="text-muted mb-1"><i class="fas fa-map-marker-alt"></i> <%= pkg.getLocation() %></p>
                <p class="mb-1"><i class="fas fa-clock"></i> <%= pkg.getDuration() %> Days</p>
                <p class="mb-1"><i class="fas fa-dollar-sign"></i> $<%= pkg.getPrice() %></p>
                <p class="mb-1"><i class="fas fa-chair"></i> Available Seats: <%= pkg.getTotalSeats() %></p>
                <p class="card-text text-truncate"><%= pkg.getDescription() %></p>
                <form action="<%=request.getContextPath()%>/booking" method="post">
                    <input type="hidden" name="packageId" value="<%= pkg.getPackageId() %>">
                    <button type="submit" name="button" value="viewBookingForm" class="btn btn-primary w-100 mt-2">Book Now</button>
                </form>
            </div>
        </div>
    </div>
<%
        serial++;
    }
} else {
%>
    <p>No packages found for "<%= keyword %>".</p>
<%
}
%>
</div>


<%-- Pagination Section --%>
<nav>
    <ul class="pagination justify-content-center">
        <% if(currentPage > 1) { %>
            <li class="page-item">
                <a class="page-link" href="?<%=queryParams%>&page=<%=currentPage-1%>&button=<%=buttonParam%>">Prev</a>
            </li>
        <% } %>

        <% for(int i=startPage; i<=endPage; i++) { %>
            <li class="page-item <%= (i==currentPage) ? "active" : "" %>">
                <a class="page-link" href="?<%=queryParams%>&page=<%=i%>&button=<%=buttonParam%>"><%=i%></a>
            </li>
        <% } %>

        <% if(currentPage < totalPages) { %>
            <li class="page-item">
                <a class="page-link" href="?<%=queryParams%>&page=<%=currentPage+1%>&button=<%=buttonParam%>">Next</a>
            </li>
        <% } %>
    </ul>
</nav>

<%@ include file="footer.jsp"%>
