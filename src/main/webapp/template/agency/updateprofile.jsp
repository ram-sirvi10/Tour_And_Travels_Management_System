<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8" session="true"%>
<%@ page import="com.travelmanagement.dto.responseDTO.AgencyResponseDTO"%>
<%@ page
	import="com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO"%>
<%@ page import="java.util.Map"%>

<%
AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");
if (agency == null) {
	response.sendRedirect("login.jsp");
	return;
}

Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
String successMessage = (String) request.getAttribute("successMessage");
String errorMessage = (String) request.getAttribute("errorMessage");
String actionType = request.getParameter("button"); // viewProfile / updateProfile
AgencyRegisterRequestDTO formData = (AgencyRegisterRequestDTO) request.getAttribute("formData");
%>

<jsp:include page="header.jsp" />
<link
	href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
	rel="stylesheet" />
<style>
.profile-upload-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
}

.profile-preview {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	border: 2px dashed #ccc;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	transition: border-color 0.3s, transform 0.2s;
	margin: auto;
}

.profile-preview:hover {
	border-color: #0d6efd;
	transform: scale(1.05);
}

.preview-img {
	width: 120px;
	height: 120px;
	border-radius: 50%;
	object-fit: cover;
}

.profile-card {
	background: #ffffff;
	transition: transform 0.3s, box-shadow 0.3s;
}

.profile-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 20px 40px rgba(0, 0, 0, 0.25);
}

.profile-view-img {
	width: 150px;
	height: 150px;
	border-radius: 50%;
	object-fit: cover;
	cursor: pointer;
	border: 4px solid #0d6efd;
	transition: transform 0.2s;
}

.profile-view-img:hover {
	transform: scale(1.05);
}

.profile-view-icon {
	font-size: 150px;
	color: #0d6efd;
}
</style>
<div class="container mt-4">
	<%-- Success/Error Messages --%>
	<%
	if (successMessage != null) {
	%>
	<div class="alert alert-success alert-dismissible fade show"
		role="alert">
		<%=successMessage%>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<%
	}
	%>
	<%
	if (errorMessage != null) {
	%>
	<div class="alert alert-danger alert-dismissible fade show"
		role="alert">
		<%=errorMessage%>
		<button type="button" class="btn-close" data-bs-dismiss="alert"
			aria-label="Close"></button>
	</div>
	<%
	}
	%>


	<%
	String selectedCountry = request.getParameter("country") != null ? request.getParameter("country") : "";
	String selectedState = request.getParameter("state") != null ? request.getParameter("state") : "";
	String selectedCity = request.getParameter("city") != null ? request.getParameter("city") : "";
	String selectedArea = request.getParameter("area") != null ? request.getParameter("area") : "";
	String pincodeVal = request.getParameter("pincode") != null ? request.getParameter("pincode") : "";
	%>
	<div class="registration-container">
		<div class="registration-card">

			<h2 class="text-center mb-4">Edit Profile</h2>
			<form method="post" action="<%=request.getContextPath()%>/agency"
				enctype="multipart/form-data">

				<div class="row g-3">
					<!-- Left Column -->
					<div class="col-md-6">
						<!-- Profile Image -->
						<label class="form-label">Profile Image:</label>
						<div class="profile-upload-wrapper mb-3 text-center">
							<label class="form-label mb-2">Update Pic</label> <input
								type="file" name="profileImage" id="profileImageInput"
								accept="image/*" style="display: none;">
							<div class="profile-preview" id="profilePreview">
								<%
								if (agency.getImageurl() != null && !agency.getImageurl().isEmpty()) {
								%>
								<img src="<%=agency.getImageurl()%>" alt="Profile"
									class="preview-img">
								<%
								} else {
								%>
								<i class="bi bi-person-circle"
									style="font-size: 50px; color: #888;"></i>
								<%
								}
								%>
							</div>
							<div class="d-flex justify-content-center gap-2 mt-2">
								<button type="button" class="btn btn-sm btn-outline-primary"
									onclick="document.getElementById('profileImageInput').click();">Upload</button>
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


						<label class="form-label">Agency Name</label> <input type="text"
							name="agencyName" class="form-control mb-2"
							value="<%=formData != null ? formData.getAgencyName() : agency.getAgencyName()%>">
						<%
						if (errors != null && errors.get("agencyName") != null) {
						%>
						<div class="error"><%=errors.get("agencyName")%></div>
						<%
						}
						%>

						<label class="form-label">Owner Name</label> <input type="text"
							name="ownerName" class="form-control mb-2"
							value="<%=formData != null ? formData.getOwnerName() : agency.getOwnerName()%>">
						<%
						if (errors != null && errors.get("ownerName") != null) {
						%>
						<div class="error"><%=errors.get("ownerName")%></div>
						<%
						}
						%>

						<label class="form-label">Phone</label> <input type="text"
							name="phone" class="form-control mb-2"
							value="<%=formData != null ? formData.getPhone() : agency.getPhone()%>">
						<%
						if (errors != null && errors.get("phone") != null) {
						%>
						<div class="error"><%=errors.get("phone")%></div>
						<%
						}
						%>
					</div>

					<!-- Right Column -->
					<div class="col-md-6">
						<label class="form-label">Country <span
							style="color: red;">*</span></label> <select id="country" name="country"
							class="form-select mb-2"></select> <label class="form-label">State
							<span style="color: red;">*</span>
						</label> <select id="state" name="state" class="form-select mb-2"></select>
						<%
						if (errors != null && errors.get("state") != null) {
						%><div class="error"><%=errors.get("state")%></div>
						<%
						}
						%>

						<label class="form-label">City <span style="color: red;">*</span></label>
						<select id="city" name="city" class="form-select mb-2"></select>
						<%
						if (errors != null && errors.get("city") != null) {
						%><div class="error"><%=errors.get("city")%></div>
						<%
						}
						%>

						<label class="form-label">Area <span style="color: red;">*</span></label>
						<select id="area" name="area" class="form-select mb-2"></select>
						<%
						if (errors != null && errors.get("area") != null) {
						%><div class="error"><%=errors.get("area")%></div>
						<%
						}
						%>

						<label class="form-label">Pincode <span
							style="color: red;">*</span></label> <input type="text" id="pincode"
							name="pincode" class="form-control mb-2" readonly
							value="<%=pincodeVal%>">
						<%
						if (errors != null && errors.get("pincode") != null) {
						%><div class="error"><%=errors.get("pincode")%></div>
						<%
						}
						%>
					</div>
				</div>

				<div class="mt-3">
					<input type="hidden" name="button" value="updateProfile">
					<button type="submit" name="button" value="updateProfile"
						class="btn btn-dark w-100 mt-3">Update</button>
					<a href="<%=request.getContextPath()%>/agency?button=viewProfile"
						class="btn btn-secondary">Cancel</a>
				</div>

			</form>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<script
		src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
	<script>
$(document).ready(async function() {
    $('#country,#state,#city,#area').select2({ width: '100%' });

    async function fetchData(type, value) {
        try {
            const res = await fetch('<%=request.getContextPath()%>/location', {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: new URLSearchParams({ type, value: value || "" })
            });
            if (!res.ok) throw new Error('Network response not ok');
            return await res.json();
        } catch (e) {
            console.error("Fetch error:", e);
            return [];
        }
    }

    const selectedCountry = '<%=agency.getCountry()%>';
    const selectedState = '<%=agency.getState()%>';
    const selectedCity = '<%=agency.getCity()%>';
    const selectedArea = '<%=agency.getArea()%>';
    const selectedPincode = '<%=agency.getPincode()%>';

    // Load Countries
    const countries = await fetchData("countries", "");
    $('#country').html('').append(new Option('--Select Country--', ''));
    countries.forEach(c => $('#country').append(new Option(c, c, false, c === selectedCountry)));
    $('#country').val(selectedCountry).trigger('change');

    // Load States
    if (selectedCountry) {
        const states = await fetchData("states", selectedCountry);
        $('#state').html('').append(new Option('--Select State--', ''));
        states.forEach(s => $('#state').append(new Option(s, s, false, s === selectedState)));
        $('#state').val(selectedState).trigger('change');
    }

    // Load Cities
    if (selectedState) {
        const cities = await fetchData("cities", selectedState);
        $('#city').html('').append(new Option('--Select City--', ''));
        cities.forEach(c => $('#city').append(new Option(c, c, false, c === selectedCity)));
        $('#city').val(selectedCity).trigger('change');
    }

    // Load Areas
    if (selectedCity) {
        const areas = await fetchData("areas", selectedCity);
        $('#area').html('').append(new Option('--Select Area--', ''));
        areas.forEach(a => $('#area').append(new Option(a, a, false, a === selectedArea)));
        $('#area').val(selectedArea).trigger('change');
    }

    // Set Pincode
    if (selectedArea) {
        const pin = await fetchData("pincode", selectedArea);
        $('#pincode').val(pin[0] || selectedPincode || 'Not Found');
    }

    // Handle changes dynamically
    $('#country').on('change', async function() {
        const val = $(this).val();
        const states = await fetchData("states", val);
        $('#state').html('').append(new Option('--Select State--', ''));
        $('#city').html('').append(new Option('--Select City--', ''));
        $('#area').html('').append(new Option('--Select Area--', ''));
        $('#pincode').val('');
        states.forEach(s => $('#state').append(new Option(s, s)));
    });

    $('#state').on('change', async function() {
        const val = $(this).val();
        const cities = await fetchData("cities", val);
        $('#city').html('').append(new Option('--Select City--', ''));
        $('#area').html('').append(new Option('--Select Area--', ''));
        $('#pincode').val('');
        cities.forEach(c => $('#city').append(new Option(c, c)));
    });

    $('#city').on('change', async function() {
        const val = $(this).val();
        const areas = await fetchData("areas", val);
        $('#area').html('').append(new Option('--Select Area--', ''));
        $('#pincode').val('');
        areas.forEach(a => $('#area').append(new Option(a, a)));
    });

    $('#area').on('change', async function() {
        const pin = await fetchData("pincode", $(this).val());
        $('#pincode').val(pin[0] || 'Not Found');
    });
});
</script>



	<!-- Profile Image Modal -->
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
const input = document.getElementById('profileImageInput');
const preview = document.getElementById('profilePreview');

input.addEventListener('change', function() {
    if(this.files && this.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.innerHTML = '<img src="'+e.target.result+'" alt="Profile" class="preview-img">';
        }
        reader.readAsDataURL(this.files[0]);
    }
});

function removeProfileImage() {
    input.value = "";
    preview.innerHTML = '<i class="bi bi-person-circle" style="font-size: 50px; color: #888;"></i>';
}

function showProfileModal(imageUrl) {
    var modalImg = document.getElementById('modalProfileImage');
    if (!modalImg) return;
    modalImg.src = imageUrl;
    var myModal = new bootstrap.Modal(document.getElementById('profileImageModal'));
    myModal.show();
}
</script>

	<jsp:include page="footer.jsp" />