<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="header.jsp"%>

<%@ page import="java.util.Map"%>
<%@ page import="com.travelmanagement.dto.requestDTO.PackageRegisterDTO"%>
<%@ page import="com.travelmanagement.model.PackageSchedule"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<%
Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
 PackageRegisterDTO oldData = (PackageRegisterDTO) request.getAttribute("oldData");
 
// Duration logic
 int duration = 1;
 int maxDays = 10;
String durationParam = request.getParameter("duration");
if (durationParam != null && !durationParam.isEmpty()) {
	try {
		duration = Integer.parseInt(durationParam);
	} catch (Exception e) {
		duration = 1;
	}
}
if (duration > maxDays)
	duration = maxDays; 
	
%>



<h2 class="mb-3">➕ Add New Package</h2>

<form id="packageForm" action="${pageContext.request.contextPath}/agency" method="post" 
	class="shadow p-4 bg-white rounded" >
 <%-- <label class="form-label">Profile Image:</label>
    <div class="profile-upload-wrapper">
        <input type="file" name="profileImage" accept="image/*" id="profileImageInput" style="display:none;" >
        <div class="profile-preview" id="profilePreview" onclick=" document.getElementById('profileImageInput').click();">
            <i class="bi bi-camera" style="font-size: 24px; color: #888;"></i>
            <span style="font-size: 12px;">Click to upload</span>
        </div>
        <% if(errors != null && errors.get("profileImage") != null) { %>
            <div class="text-danger mb-2"><%= errors.get("profileImage") %></div>
        <% } %>
    </div> --%>
	<input type="hidden" name="isScheduleSubmit" value="true">
	<!-- Title & Location -->
	<div class="row mb-3">
		<div class="col-md-6">
			<label class="form-label">Title</label> <input type="text"
				name="title" class="form-control"
				value="<%=request.getParameter("title") != null ? request.getParameter("title") : ""%>">
			<small class="text-danger"><%=errors != null && errors.get("title") != null ? errors.get("title") : ""%></small>
		</div>
		<div class="col-md-6">
			<label class="form-label">Location</label> <input type="text"
				name="location" class="form-control"
				value="<%=request.getParameter("location") != null ? request.getParameter("location") : ""%>">
			<small class="text-danger"><%=errors != null && errors.get("location") != null ? errors.get("location") : ""%></small>
		</div>
	</div>

	<!-- Price, Duration, Total Seats -->
	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Price</label> <input type="number"
				name="price" step="0.01" class="form-control"
				value="<%=request.getParameter("price") != null ? request.getParameter("price") : ""%>">
			<small class="text-danger"><%=errors != null && errors.get("price") != null ? errors.get("price") : ""%></small>
		</div>
		<div class="col-md-4">
			  <input type="hidden" name="isScheduleSubmit" value="true">

   <label class="form-label">Duration (Days)</label>
<select name="duration" class="form-select" onchange="changeDuration()">
    <% for (int i = 1; i <= maxDays; i++) { %>
        <option value="<%=i%>" <%= (duration == i ? "selected" : "") %>><%=i%></option>
    <% } %>
</select>
			 <small class="text-danger"><%=errors != null && errors.get("duration") != null ? errors.get("duration") : ""%></small>
		</div>
		<div class="col-md-4">
			<label class="form-label">Total Seats</label> <input type="number"
				name="totalseats" min="1" class="form-control"
				value="<%=oldData != null && oldData.getTotalSeats() != null
		? oldData.getTotalSeats()
		: (request.getParameter("totalseats") != null ? request.getParameter("totalseats") : "")%>">
			<small class="text-danger"><%=errors != null && errors.get("totalseats") != null ? errors.get("totalseats") : ""%></small>
		</div>
	</div>

	<!-- Departure & Last Booking -->
	<div class="row mb-3">
		<div class="col-md-6">
			<label class="form-label">Departure Date & Time</label> <input
				type="datetime-local" name="departure_date" class="form-control"
				value="<%=request.getParameter("departure_date") != null ? request.getParameter("departure_date") : ""%>">
			<small class="text-danger"><%=errors != null && errors.get("departure_date") != null ? errors.get("departure_date") : ""%></small>
		</div>
		<div class="col-md-6">
			<label class="form-label">Last Booking Date & Time</label> <input
				type="datetime-local" name="last_booking_date" class="form-control"
				value="<%=request.getParameter("last_booking_date") != null ? request.getParameter("last_booking_date") : ""%>">
			<small class="text-danger"><%=errors != null && errors.get("last_booking_date") != null ? errors.get("last_booking_date") : ""%></small>
		</div>
	</div>
	<!-- Package Active Status -->
	<div class="mb-3">
		<label class="form-label">Package Status</label>
		<div class="form-check form-switch">
			<input class="form-check-input" type="checkbox" name="isActive"
				id="isActive"
				<%="on".equals(request.getParameter("isActive")) ? "checked" : ""%>>
			<label class="form-check-label" for="isActive">Active</label>
		</div>
		<small class="text-danger"> <%=errors != null && errors.get("isActive") != null ? errors.get("isActive") : ""%>
		</small>
	</div>

	<!-- Description -->
	<div class="mb-3">
		<label class="form-label">Description</label>
		<textarea name="description" rows="3" class="form-control"><%=request.getParameter("description") != null ? request.getParameter("description") : ""%></textarea>
		<small class="text-danger"><%=errors != null && errors.get("description") != null ? errors.get("description") : ""%></small>
	</div>

	<!-- Day-wise Schedule -->
	<h5>Day-wise Schedule</h5>
	<%
	for (int i = 1; i <= duration; i++) {
		String act = request.getParameter("day" + i + "_activity") != null
		? request.getParameter("day" + i + "_activity")
		: "";
		String descVal = request.getParameter("day" + i + "_desc") != null
		? request.getParameter("day" + i + "_desc")

		: "";
	%>
	<div class="card mb-3 shadow-sm">
		<div class="card-header bg-secondary text-white">
			Day
			<%=i%>
			Schedule
		</div>
		<div class="card-body">
			<div class="mb-2">
				<label>Activity</label> <input type="text" name="day<%=i%>_activity"
					class="form-control" value="<%=act%>"> <small
					class="text-danger"><%=errors != null && errors.get("day" + i + "_activity") != null ? errors.get("day" + i + "_activity") : ""%></small>
			</div>
			<div class="mb-2">
				<label>Description</label>
				<textarea name="day<%=i%>_desc" class="form-control"><%=descVal%></textarea>
				<small class="text-danger"><%=errors != null && errors.get("day" + i + "_desc") != null ? errors.get("day" + i + "_desc") : ""%></small>
			</div>
		</div>
	</div>
	<%
	}
	%>
<input type="hidden" name="button" value="addPackage">
	<button type="submit" class="btn btn-primary mt-3">Save
		Package</button>

</form>

<script>
function changeDuration() {
    const form = document.getElementById("packageForm");
    form.isScheduleSubmit.value = "false";   
    form.submit();
}
</script>
<%@ include file="footer.jsp"%>
