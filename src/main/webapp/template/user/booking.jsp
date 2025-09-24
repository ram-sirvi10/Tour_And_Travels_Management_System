<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="header.jsp" %>
<%
    int packageId = Integer.parseInt(request.getParameter("packageId"));
%>
<h2 class="mb-4">Booking Form</h2>
<div class="card shadow p-4 bg-white">
    <form action="BookingServlet" method="post">
        <input type="hidden" name="packageId" value="<%=packageId%>">
        <div class="mb-3">
            <label for="no_of_travelers" class="form-label">Number of Travelers:</label>
            <select class="form-select" id="no_of_travelers" name="no_of_travelers" onchange="generateTravelers(this.value)">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
            </select>
        </div>
        <div id="travelersContainer"></div>
        <button type="submit" class="btn btn-success mt-3">Confirm Booking</button>
    </form>
</div>

<script>
function generateTravelers(count) {
    const container = document.getElementById('travelersContainer');
    container.innerHTML = '';
    for (let i = 1; i <= count; i++) {
        container.innerHTML += `
            <div class="border p-3 mb-3 rounded">
                <h5 class="fw-bold">Traveler ${i}</h5>
                <div class="mb-2">
                    <label>Name:</label>
                    <input type="text" name="traveler_name${i}" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label>Email:</label>
                    <input type="email" name="traveler_email${i}" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label>Mobile:</label>
                    <input type="text" name="traveler_mobile${i}" class="form-control" required>
                </div>
                <div class="mb-2">
                    <label>Age:</label>
                    <input type="number" name="traveler_age${i}" class="form-control" required>
                </div>
            </div>
        `;
    }
}
window.onload = function() { generateTravelers(1); }
</script>
<%@ include file="footer.jsp" %>
