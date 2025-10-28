<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO"%>


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

		<div class="col-md-6">
			<label class="form-label">Departure Date & Time</label> <input
				type="datetime-local" id="departureDate" name="departure_date"
				class="form-control"
				value="<%=departureValue.length() > 16 ? departureValue.substring(0, 16) : departureValue%>">
			<small class="text-danger"><%=(errors != null && errors.get("departureDate") != null) ? errors.get("departureDate") : ""%></small>
		</div>
		<div class="col-md-6">
			<label class="form-label">Last Booking Date & Time</label> <input
				type="datetime-local" id="lastBookingDate" name="last_booking_date"
				class="form-control"
				value="<%=lastBookingValue.length() > 16 ? lastBookingValue.substring(0, 16) : lastBookingValue%>">
			<small class="text-danger"><%=(errors != null && errors.get("lastBookingDate") != null) ? errors.get("lastBookingDate") : ""%></small>
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
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("packageForm");
    const departureInput = document.getElementById("departureDate");
    const bookingInput = document.getElementById("lastBookingDate");
    const seatInput = document.querySelector("input[name='totalseats']");
    const priceInput = document.querySelector("input[name='price']");
    const titleInput = document.querySelector("input[name='title']");
    const locationInput = document.querySelector("input[name='location']");
    const descInput = document.querySelector("textarea[name='description']");
    const durationSelect = document.querySelector("select[name='duration']");

    // ---------- 🕒 Disable past date/time ----------
    const now = new Date();
    const nowLocal = new Date(now.getTime() - now.getTimezoneOffset() * 60000)
        .toISOString()
        .slice(0, 16);

    departureInput.min = nowLocal;
    bookingInput.min = nowLocal;

    // ---- When departure changes, set booking limit before it ----
    departureInput.addEventListener("change", () => {
        if (!departureInput.value) return;
        const dep = new Date(departureInput.value);
        const nowTime = new Date();

        // Prevent past date/time
        if (dep < nowTime) {
            alert("⚠️ Departure date/time cannot be in the past!");
            departureInput.value = "";
            bookingInput.max = "";
            return;
        }

        // Set booking max = departure - 1 minute
        const bookMax = new Date(dep.getTime() - 60 * 1000);
        const maxLocal = new Date(
            bookMax.getTime() - bookMax.getTimezoneOffset() * 60000
        )
            .toISOString()
            .slice(0, 16);
        bookingInput.max = maxLocal;

        // Adjust booking date if invalid
        if (bookingInput.value && new Date(bookingInput.value) > bookMax) {
            alert("⚠️ Last booking date must be before departure date!");
            bookingInput.value = "";
        }
    });

    // ---- Booking date validation ----
    bookingInput.addEventListener("change", () => {
        const nowTime = new Date();
        if (!bookingInput.value) return;

        const booking = new Date(bookingInput.value);
        if (booking < nowTime) {
            alert("⚠️ Booking date/time cannot be in the past!");
            bookingInput.value = "";
            return;
        }

        if (departureInput.value && booking >= new Date(departureInput.value)) {
            alert("⚠️ Booking date must be before departure date!");
            bookingInput.value = "";
        }
    });

    // ---------- 🧮 Field Validations ----------
    const showError = (input, message) => {
        let errorEl = input.parentElement.querySelector(".text-danger.js-error");
        if (!errorEl) {
            errorEl = document.createElement("small");
            errorEl.className = "text-danger js-error";
            input.parentElement.appendChild(errorEl);
        }
        errorEl.textContent = message;
    };

    const clearError = (input) => {
        const errorEl = input.parentElement.querySelector(".js-error");
        if (errorEl) errorEl.remove();
    };

    const validateField = () => {
        let valid = true;

        // Title
        if (!titleInput.value.trim()) {
            showError(titleInput, "Title is required!");
            valid = false;
        } else clearError(titleInput);

        // Location
        if (!locationInput.value.trim()) {
            showError(locationInput, "Location is required!");
            valid = false;
        } else clearError(locationInput);

        // Price
        const price = parseFloat(priceInput.value);
        if (isNaN(price) || price <= 0) {
            showError(priceInput, "Price must be a positive number!");
            valid = false;
        } else clearError(priceInput);

        // Duration
        const duration = parseInt(durationSelect.value);
        if (isNaN(duration) || duration < 1) {
            showError(durationSelect, "Duration must be at least 1 day!");
            valid = false;
        } else clearError(durationSelect);

        // Total Seats
        const seats = parseInt(seatInput.value);
        if (isNaN(seats) || seats <= 0) {
            showError(seatInput, "Total seats must be a positive number!");
            valid = false;
        } else clearError(seatInput);

        // Description
        if (!descInput.value.trim()) {
            showError(descInput, "Description is required!");
            valid = false;
        } else clearError(descInput);

        // Departure Date
        if (!departureInput.value) {
            showError(departureInput, "Departure date/time is required!");
            valid = false;
        } else clearError(departureInput);

        // Booking Date
        if (!bookingInput.value) {
            showError(bookingInput, "Booking date/time is required!");
            valid = false;
        } else clearError(bookingInput);

        return valid;
    };

    // ---------- 🧾 Real-time seat check ----------
    seatInput.addEventListener("input", () => {
        const val = parseInt(seatInput.value);
        if (isNaN(val) || val <= 0) {
            showError(seatInput, "Seats must be greater than 0!");
        } else clearError(seatInput);
    });

    // ---------- 🧩 Final Submit Validation ----------
    form.addEventListener("submit", (e) => {
        const valid = validateField();
        if (!valid) {
            e.preventDefault();
            alert("⚠️ Please correct highlighted errors before submitting.");
        }
    });
});
</script>


<%@ include file="footer.jsp"%>
