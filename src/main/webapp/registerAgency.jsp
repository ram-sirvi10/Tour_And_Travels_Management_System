<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Agency Registration</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css"
	rel="stylesheet">
<style>
body {
	background: #f0f2f5;
	font-family: Arial, sans-serif;
}

.registration-container {
	min-height: 100vh;
	display: flex;
	justify-content: center;
	align-items: flex-start;
	padding: 60px 0;
}

.registration-card {
	background: #fff;
	padding: 35px 30px;
	border-radius: 15px;
	box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
	width: 100%;
	max-width: 900px;
	transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.registration-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
}

.registration-card h2 {
	text-align: center;
	margin-bottom: 25px;
	font-weight: 700;
}

.profile-upload-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 15px;
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
	margin-bottom: 10px;
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

.fade-alert {
	transition: opacity 0.5s ease;
}

.error {
	color: red;
	font-size: 0.9em;
}
</style>
</head>
<body>

	<jsp:include page="navbar.jsp" />

	<div class="registration-container">
		<div class="registration-card">

			<div class="text-center mb-3">
				<a href="registerUser.jsp" class="btn btn-outline-primary btn-sm">Register
					As User</a>
			</div>

			<h2>Agency Registration</h2>

			<%
			Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
			Boolean showOtp = (Boolean) request.getAttribute("showOtp");
			boolean lockFields = showOtp != null && showOtp;
			String errorMessage = (String) session.getAttribute("errorMessage");
			session.removeAttribute("errorMessage");
			if (errorMessage == null)
				errorMessage = (String) request.getAttribute("errorMessage");
			if (errorMessage != null && !errorMessage.isEmpty()) {
			%>
			<div class="alert alert-danger fade-alert text-center"><%=errorMessage%></div>
			<%
			}
			String selectedCountry = request.getParameter("country") != null ? request.getParameter("country") : "";
			String selectedState = request.getParameter("state") != null ? request.getParameter("state") : "";
			String selectedCity = request.getParameter("city") != null ? request.getParameter("city") : "";
			String selectedArea = request.getParameter("area") != null ? request.getParameter("area") : "";
			String pincodeVal = request.getParameter("pincode") != null ? request.getParameter("pincode") : "";
			%>

			<form action="<%=request.getContextPath()%>/auth" method="post"
				enctype="multipart/form-data">
				<div class="row g-3">
					<!-- Left Column -->
					<div class="col-md-6">
						<label class="form-label">Profile Image:</label>
						<div class="profile-upload-wrapper">
							<input type="file" name="profileImage" accept="image/*"
								id="profileImageInput" style="display: none;"
								<%=lockFields ? "disabled" : ""%>>
							<div class="profile-preview" id="profilePreview"
								onclick="if(!<%=lockFields%>) document.getElementById('profileImageInput').click();">
								<i class="bi bi-camera" style="font-size: 30px; color: #888;"></i>
								<span style="font-size: 12px;">Click to upload</span>
							</div>
							<%
							if (errors != null && errors.get("profileImage") != null) {
							%>
							<div class="error"><%=errors.get("profileImage")%></div>
							<%
							}
							%>
						</div>

						<label class="form-label">Agency Name <span
							style="color: red;">*</span></label> <input type="text"
							name="agency_name" class="form-control mb-2"
							placeholder="Agency Name"
							value="<%=request.getParameter("agency_name") != null ? request.getParameter("agency_name") : ""%>"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("agencyName") != null) {
						%><div
							class="error"><%=errors.get("agencyName")%></div>
						<%
						}
						%>

						<label class="form-label">Owner Name <span
							style="color: red;">*</span></label> <input type="text" name="owner_name"
							class="form-control mb-2" placeholder="Owner Name"
							value="<%=request.getParameter("owner_name") != null ? request.getParameter("owner_name") : ""%>"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("ownerName") != null) {
						%><div
							class="error"><%=errors.get("ownerName")%></div>
						<%
						}
						%>

						<label class="form-label">Email <span style="color: red;">*</span></label>
						<input type="email" name="email" class="form-control mb-2"
							placeholder="Email"
							value="<%=request.getParameter("email") != null ? request.getParameter("email") : ""%>"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("email") != null) {
						%><div
							class="error"><%=errors.get("email")%></div>
						<%
						}
						%>
						<label class="form-label">Phone <span style="color: red;">*</span></label>
						<input type="text" name="phone" class="form-control mb-2"
							placeholder="Phone Number"
							value="<%=request.getParameter("phone") != null ? request.getParameter("phone") : ""%>"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("phone") != null) {
						%>
						<div class="error"><%=errors.get("phone")%></div>
						<%
						}
						%>



					</div>


					<div class="col-md-6">
						<label class="form-label">Registration Number <span
							style="color: red;">*</span></label> <input type="text"
							name="registration_number" class="form-control mb-2"
							placeholder="Registration Number"
							value="<%=request.getParameter("registration_number") != null ? request.getParameter("registration_number") : ""%>"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("registrationNumber") != null) {
						%>
						<div class="error"><%=errors.get("registrationNumber")%></div>
						<%
						}
						%>

						<label class="form-label">Country <span
							style="color: red;">*</span></label> <select id="country" name="country"
							class="form-select mb-2" <%=lockFields ? "disabled" : ""%>></select>

						<label class="form-label">State <span style="color: red;">*</span></label>
						<select id="state" name="state" class="form-select mb-2"
							<%=lockFields ? "disabled" : ""%>></select>
						<%
						if (errors != null && errors.get("state") != null) {
						%><div
							class="error"><%=errors.get("state")%></div>
						<%
						}
						%>

						<label class="form-label">City <span style="color: red;">*</span></label>
						<select id="city" name="city" class="form-select mb-2"
							<%=lockFields ? "disabled" : ""%>></select>
						<%
						if (errors != null && errors.get("city") != null) {
						%><div
							class="error"><%=errors.get("city")%></div>
						<%
						}
						%>

						<label class="form-label">Area <span style="color: red;">*</span></label>
						<select id="area" name="area" class="form-select mb-2"
							<%=lockFields ? "disabled" : ""%>></select>
						<%
						if (errors != null && errors.get("area") != null) {
						%><div
							class="error"><%=errors.get("area")%></div>
						<%
						}
						%>

						<label class="form-label">Pincode <span
							style="color: red;">*</span></label> <input type="text" id="pincode"
							name="pincode" class="form-control mb-2" readonly
							value="<%=pincodeVal%>">
						<%
						if (errors != null && errors.get("pincode") != null) {
						%><div
							class="error"><%=errors.get("pincode")%></div>
						<%
						}
						%>

						<label class="form-label">Password <span
							style="color: red;">*</span></label> <input type="password"
							name="password" class="form-control mb-2" placeholder="Password"
							<%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("password") != null) {
						%><div
							class="error"><%=errors.get("password")%></div>
						<%
						}
						%>

						<label class="form-label">Confirm Password <span
							style="color: red;">*</span></label> <input type="password"
							name="confirm_password" class="form-control mb-2"
							placeholder="Confirm Password" <%=lockFields ? "readonly" : ""%>>
						<%
						if (errors != null && errors.get("confirmPassword") != null) {
						%><div
							class="error"><%=errors.get("confirmPassword")%></div>
						<%
						}
						%>
					</div>


					<%
					if (!lockFields) {
					%>
					<button type="submit" name="button" value="registerAsAgency"
						class="btn btn-dark w-100 mt-3">Register</button>
					<p>
						Already have an account? <a href="login.jsp?role=agency">🏢
							Login as Agency</a>
					</p>
					<%
					} else {
					%>
					<div class="mt-3">
						<input type="text" name="otp" class="form-control mb-2"
							placeholder="Enter OTP">
						<button type="submit" name="button"
							value="verifyOTPAndRegisterAgency" class="btn btn-primary w-100">Verify
							OTP & Complete Registration</button>
					</div>
					<%
					}
					%>

					<%
					if (request.getAttribute("message") != null) {
					%>
					<div class="alert alert-success mt-2"><%=request.getAttribute("message")%></div>
					<%
					}
					%>
					<%
					if (request.getAttribute("error") != null) {
					%>
					<div class="alert alert-danger mt-2 fade-alert"><%=request.getAttribute("error")%></div>
					<%
					}
					%>
				
			</form>
		</div>
	</div>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
	<script>
$(document).ready(async function() {
    $('#country,#state,#city,#area').select2({width:'100%'});

    async function fetchData(type,value){
        try {
            let res = await fetch('<%=request.getContextPath()%>/location', {
                method:"POST",
                headers:{"Content-Type":"application/x-www-form-urlencoded"},
                body: new URLSearchParams({type:type,value:value||""})
            });
            if(!res.ok) throw new Error('Network response was not ok');
            return await res.json();
        } catch(e){
            console.error("Fetch error:", e);
            return [];
        }
    }

    // Populate Countries
    let countries = await fetchData("countries","");
    $('#country').html('').append(new Option('--Select Country--',''));
    countries.forEach(c => {
        const selected = c === '<%=selectedCountry%>';
        $('#country').append(new Option(c,c,false,selected));
    });

    async function loadStates(selState){
        $('#state').html('').append(new Option('--Select State--',''));
        $('#city').html('').append(new Option('--Select City--',''));
        $('#area').html('').append(new Option('--Select Area--',''));
        $('#pincode').val('');
        if($('#country').val()){
            let states = await fetchData("states",$('#country').val());
            states.forEach(s => {
                const sel = s === selState;
                $('#state').append(new Option(s,s,false,sel));
            });
            if(selState) loadCities('<%=selectedCity%>');
        }
    }

    async function loadCities(selCity){
        $('#city').html('').append(new Option('--Select City--',''));
        $('#area').html('').append(new Option('--Select Area--',''));
        $('#pincode').val('');
        if($('#state').val()){
            let cities = await fetchData("cities",$('#state').val());
            cities.forEach(c => {
                const sel = c === selCity;
                $('#city').append(new Option(c,c,false,sel));
            });
            if(selCity) loadAreas('<%=selectedArea%>');
        }
    }

    async function loadAreas(selArea){
        $('#area').html('').append(new Option('--Select Area--',''));
        $('#pincode').val('');
        if($('#city').val()){
            let areas = await fetchData("areas",$('#city').val());
            areas.forEach(a => {
                const sel = a === selArea;
                $('#area').append(new Option(a,a,false,sel));
            });
            if(selArea){
                let pin = await fetchData("pincode",selArea);
                $('#pincode').val(pin[0]||'Not Found');
            }
        }
    }

    $('#country').on('change',()=>loadStates(''));
    $('#state').on('change',()=>loadCities(''));
    $('#city').on('change',()=>loadAreas(''));
    $('#area').on('change', async function(){
        let pin = await fetchData("pincode",$(this).val());
        $('#pincode').val(pin[0]||'Not Found');
    });

    // Initial load if previous values exist (error case)
    if('<%=selectedCountry%>') loadStates('<%=selectedState%>');

    // Profile Image Preview
    const input=document.getElementById('profileImageInput');
    const preview=document.getElementById('profilePreview');
    input.addEventListener('change',function(){
        if(this.files&&this.files[0]){
            const reader=new FileReader();
            reader.onload=function(e){ preview.innerHTML='<img src="'+e.target.result+'" alt="Profile" class="preview-img">'; }
            reader.readAsDataURL(this.files[0]);
        } else { preview.innerHTML='<i class="bi bi-camera" style="font-size:30px;color:#888;"></i><span>Click to upload</span>'; }
    });

    // Auto-hide alerts
    document.querySelectorAll('.fade-alert').forEach(alert=>{
        setTimeout(()=>{ 
            alert.style.opacity=0; 
            setTimeout(()=>{ alert.style.display='none'; },500); 
        },3000);
    });
});
</script>


</body>
</html>
