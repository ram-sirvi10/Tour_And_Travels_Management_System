<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO"%>

<button type="button" class="btn btn-secondary"
	onclick="window.history.back();">Back</button>
<style>
.profile-upload-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.profile-preview {
	width: 250px;
	height: 250px;
	border-radius: 8px;
	border: 2px dashed #ccc;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	transition: border-color 0.3s, transform 0.2s;
	margin: auto;
	background-color: #f8f9fa;
	overflow: hidden;
}

.profile-preview:hover {
	border-color: #0d6efd;
	transform: scale(1.03);
}

.preview-img {
	width: 100%;
	height: 100%;
	object-fit: cover;
	border-radius: 0;
}
</style>


<%
boolean isEdit = request.getAttribute("packageData") != null;
String heading = isEdit ? "✏️ Edit Package" : "➕ Add New Package";

Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
PackageResponseDTO pkg = (PackageResponseDTO) request.getAttribute("packageData");
List<PackageScheduleResponseDTO> schedules = (List<PackageScheduleResponseDTO>) request.getAttribute("scheduleList");

int duration = 1;
int maxDays = 10;

if (pkg != null) {
	duration = pkg.getDuration();
} else if (request.getParameter("duration") != null) {
	try {
		duration = Integer.parseInt(request.getParameter("duration"));
	} catch (Exception e) {
		duration = 1;
	}
}
if (duration > maxDays)
	duration = maxDays;
%>

<h2 class="mb-3"><%=heading%></h2>

<form id="packageForm"
	action="${pageContext.request.contextPath}/agency" method="post"
	enctype="multipart/form-data" class="shadow p-4 bg-white rounded">

	<input type="hidden" name="isScheduleSubmit" value="true"> <input
		type="hidden" name="button" id="buttonField"
		value="<%=isEdit ? "updatePackage" : "addPackage"%>">

	<%
	if (isEdit) {
	%>
	<input type="hidden" name="packageId" value="<%=pkg.getPackageId()%>">
	<%
	}
	%>


	<!-- Title & Location -->
	<div class="row mb-3">
		<div class="col-md-6">
			<label class="form-label">Title</label> <input type="text"
				name="title" class="form-control"
				value="<%=(pkg != null) ? pkg.getTitle() : request.getParameter("title") != null ? request.getParameter("title") : ""%>">
			<small class="text-danger"><%=(errors != null && errors.get("title") != null) ? errors.get("title") : ""%></small>
		</div>
		<div class="col-md-6">
			<label class="form-label">Location</label> <input type="text"
				name="location" class="form-control"
				value="<%=(pkg != null)
		? pkg.getLocation()
		: request.getParameter("location") != null ? request.getParameter("location") : ""%>">
			<small class="text-danger"><%=(errors != null && errors.get("location") != null) ? errors.get("location") : ""%></small>
		</div>
	</div>

	<!-- Price, Duration, Total Seats -->
	<div class="row mb-3">
		<div class="col-md-4">
			<label class="form-label">Price</label> <input type="number"
				name="price" step="0.01" class="form-control"
				value="<%=(pkg != null) ? pkg.getPrice() : request.getParameter("price") != null ? request.getParameter("price") : ""%>">
			<small class="text-danger"><%=(errors != null && errors.get("price") != null) ? errors.get("price") : ""%></small>
		</div>
		<div class="col-md-4">
			<label class="form-label">Duration (Days)</label> <select
				name="duration" class="form-select" onchange="changeDuration()">
				<%
				for (int i = 1; i <= maxDays; i++) {
				%>
				<option value="<%=i%>" <%=(duration == i) ? "selected" : ""%>><%=i%></option>
				<%
				}
				%>
			</select>
		</div>
		<div class="col-md-4">
			<label class="form-label">Total Seats</label> <input type="number"
				name="totalseats" min="1" class="form-control"
				value="<%=(pkg != null && pkg.getTotalSeats() != null)
		? pkg.getTotalSeats()
		: request.getParameter("totalseats") != null ? request.getParameter("totalseats") : ""%>">
			<small class="text-danger"><%=(errors != null && errors.get("totalseats") != null) ? errors.get("totalseats") : ""%></small>
		</div>
	</div>

	<!-- Departure & Last Booking -->
	<div class="row mb-3">
		<%
		String departureValue = "";
		if (pkg != null && pkg.getDepartureDate() != null)
			departureValue = pkg.getDepartureDate().toString();
		else if (request.getParameter("departure_date") != null)
			departureValue = request.getParameter("departure_date");

		String lastBookingValue = "";
		if (pkg != null && pkg.getLastBookingDate() != null)
			lastBookingValue = pkg.getLastBookingDate().toString();
		else if (request.getParameter("last_booking_date") != null)
			lastBookingValue = request.getParameter("last_booking_date");
		%>

		<div class="row mb-3">
			<div class="col-md-6">
				<label class="form-label">Departure Date & Time</label> <input
					type="datetime-local" name="departure_date" class="form-control"
					value="<%=departureValue.length() > 16 ? departureValue.substring(0, 16) : departureValue%>">
				<small class="text-danger"><%=(errors != null && errors.get("departureDate") != null) ? errors.get("departureDate") : ""%></small>
			</div>
			<div class="col-md-6">
				<label class="form-label">Last Booking Date & Time</label> <input
					type="datetime-local" name="last_booking_date" class="form-control"
					value="<%=lastBookingValue.length() > 16 ? lastBookingValue.substring(0, 16) : lastBookingValue%>">
				<small class="text-danger"><%=(errors != null && errors.get("lastBookingDate") != null) ? errors.get("lastBookingDate") : ""%></small>
			</div>
		</div>

	</div>

	<!-- Status Switch -->
	<div class="mb-3">
		<label class="form-label">Package Status</label>
		<div class="form-check form-switch">
			<input class="form-check-input" type="checkbox" name="isActive"
				id="isActive"
				<%=(pkg != null && pkg.getIsActive()) || "on".equals(request.getParameter("isActive")) ? "checked" : ""%>>
			<label class="form-check-label" for="isActive">Active</label>
		</div>
	</div>

	<!-- Description -->
	<div class="mb-3">
		<label class="form-label">Description</label>
		<textarea name="description" rows="3" class="form-control"><%=(pkg != null)
		? pkg.getDescription()
		: request.getParameter("description") != null ? request.getParameter("description") : ""%></textarea>
	</div>

	<!-- Day-wise Schedule -->
	<h5>Day-wise Schedule</h5>
	<%
	for (int i = 1; i <= duration; i++) {
		String act = "", descVal = "";
		if (schedules != null && i <= schedules.size()) {
			act = schedules.get(i - 1).getActivity();
			descVal = schedules.get(i - 1).getDescription();
		} else {
			act = request.getParameter("day" + i + "_activity") != null
			? request.getParameter("day" + i + "_activity")
			: "";
			descVal = request.getParameter("day" + i + "_desc") != null ? request.getParameter("day" + i + "_desc") : "";
		}
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
					class="text-danger"> <%=(errors != null && errors.get("day" + i + "_activity") != null) ? errors.get("day" + i + "_activity") : ""%>
				</small>
			</div>
			<div class="mb-2">
				<label>Description</label>
				<textarea name="day<%=i%>_desc" class="form-control"><%=descVal%></textarea>
				<small class="text-danger"> <%=(errors != null && errors.get("day" + i + "_desc") != null) ? errors.get("day" + i + "_desc") : ""%>
				</small>
			</div>
		</div>

	</div>
	<%
	}
	%>
	<div class="profile-upload-wrapper mb-3 text-center">
		<label class="form-label mb-2">Package Image</label>

		<!-- Hidden File Input -->
		<input type="file" name="profileImage" id="profileImageInput"
			accept="image/*" style="display: none;">

		<!-- Image Preview Box -->
		<div class="profile-preview" id="profilePreview"
			onclick="document.getElementById('profileImageInput').click();">
			<%
			String imageUrl = "";
			if (pkg != null && pkg.getImageurl() != null && !pkg.getImageurl().isEmpty()) {
				imageUrl = pkg.getImageurl();
			}
			%>

			<%
			if (imageUrl != null && !imageUrl.isEmpty()) {
			%>
			<img src="<%=imageUrl%>" alt="Package Image" class="preview-img"
				id="previewImgTag">
			<%
			} else {
			%>
			<i class="bi bi-image" id="previewIcon"
				style="font-size: 100px; color: #888;"></i>
			<%
			}
			%>
		</div>

		<!-- Upload / Remove Buttons -->
		<div class="d-flex justify-content-center gap-2 mt-2">
			<button type="button" class="btn btn-sm btn-outline-primary"
				onclick="document.getElementById('profileImageInput').click();">
				Upload</button>
			<button type="button" class="btn btn-sm btn-outline-danger"
				onclick="removeProfileImage();">Remove</button>
		</div>

		<%
		if (errors != null && errors.get("profileImage") != null) {
		%>
		<div class="text-danger mt-1"><%=errors.get("profileImage")%></div>
		<%
		}
		%>
	</div>


	<button type="submit" class="btn btn-primary mt-3"><%=isEdit ? "Update Package" : "Save Package"%></button>
</form>
<div class="modal fade" id="profileImageModal" tabindex="-1"
	aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-body text-center">
				<img id="modalProfileImage" src="" alt="Profile Image"
					style="max-width: 100%; max-height: 500px; border-radius: 8px;">
			</div>
		</div>
	</div>
</div>
<script>
	function changeDuration() {
		const form = document.getElementById("packageForm");
		form.isScheduleSubmit.value = "false"; // Preserve correct button value during duration change const
		isEdit =
<%=isEdit%>
	;
		document.getElementById("buttonField").value = isEdit ? "updatePackage"
				: "addPackage";
		form.submit();
	}

	const input = document.getElementById('profileImageInput');
	const preview = document.getElementById('profilePreview');

	input.addEventListener('change', function() {
		if (this.files && this.files[0]) {
			const reader = new FileReader();
			reader.onload = function(e) {
				// Remove icon if present
				const oldIcon = document.getElementById('previewIcon');
				if (oldIcon)
					oldIcon.remove();

				// Replace or create image tag
				let img = document.getElementById('previewImgTag');
				if (!img) {
					img = document.createElement('img');
					img.id = 'previewImgTag';
					img.className = 'preview-img';
					preview.appendChild(img);
				}
				img.src = e.target.result;
			};
			reader.readAsDataURL(this.files[0]);
		}
	});

	function removeProfileImage() {
		input.value = "";
		const img = document.getElementById('previewImgTag');
		if (img)
			img.remove();

		// Add default icon back
		if (!document.getElementById('previewIcon')) {
			const icon = document.createElement('i');
			icon.id = 'previewIcon';
			icon.className = 'bi bi-image';
			icon.style.fontSize = '100px';
			icon.style.color = '#888';
			preview.appendChild(icon);
		}
	}
</script>

<%@ include file="footer.jsp"%>
