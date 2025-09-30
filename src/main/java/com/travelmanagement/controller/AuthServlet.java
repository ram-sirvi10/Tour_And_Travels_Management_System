package com.travelmanagement.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.IAgencyService;
import com.travelmanagement.service.IUserService;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.util.CloudinaryUtil;
import com.travelmanagement.util.EmailUtil;
import com.travelmanagement.util.OTPUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/auth")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 5, // 5 MB
		maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserService userService = new UserServiceImpl();
	private AuthServiceImpl authService = new AuthServiceImpl();
	private IAgencyService agencyService = new AgencyServiceImpl();

//	public AuthServlet() {
//		createAdmin();
//	}

	// USE THIS METHOD TO ADD ADMIN
//	 private void createAdmin() {
//	    	System.out.println("CREATE ADMIN=============");
//			if(!adminService.isAdminAvailable())
//			{
//				adminService.saveAdmins(new Admin(null,"admin", "admin123@gmail.com", Validation.encryptPassword("admin123")));
//			}
//		}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.sendRedirect("index.jsp");
//		return;
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String button = request.getParameter("button");
		System.out.println("AUTH SERVLETS BUTTON ==> " + button);

		if (button == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		try {
			switch (button) {

			case "sendOTPUser":
				handleSendOTP(request, response, "user");
				request.setAttribute("showOtp", true);
				request.getRequestDispatcher("registerUser.jsp").forward(request, response);
				break;
			case "verifyOTPUser":
				boolean verified = handleVerifyOTP(request, "user");
				request.setAttribute("showOtp", true);
				if (verified)
					request.setAttribute("showOtp", false);
					request.setAttribute("message", "OTP verified!");
				request.getRequestDispatcher("registerUser.jsp").forward(request, response);
				break;
			case "sendOTPAgency":
				handleSendOTP(request, response, "agency");
				request.setAttribute("showOtp", true);
				request.getRequestDispatcher("registerAgency.jsp").forward(request, response);
				break;
			case "verifyOTPAgency":
				boolean verifiedAgency = handleVerifyOTP(request, "agency");
				request.setAttribute("showOtp", true);
				if (verifiedAgency)
					request.setAttribute("message", "OTP verified!");
				request.getRequestDispatcher("registerAgency.jsp").forward(request, response);
				break;

			case "registerAsUser":
				handleRegisterAsUser(request, response);
				break;
			case "registerAsAgency":
				handleRegisterAsAgency(request, response);
				break;
			case "login":
				handleLogin(request, response);
				break;
			case "logout":
				handleLogout(request, response);
				break;
			default:
				response.sendRedirect("index.jsp");
				return;

			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
	}

	private void handleSendOTP(HttpServletRequest request, HttpServletResponse response, String type)
			throws IOException, ServletException {

		String email = request.getParameter("email");
		if (email == null || email.isEmpty()) {
			request.setAttribute("showOtp", true);
			request.setAttribute("error", "Email is required to send OTP.");
			return;
		}

		HttpSession session = request.getSession();
		String otp = OTPUtil.generateOTP(session);

		switch (otp) {
		case "WAIT":
			request.setAttribute("error", "Please wait 1 minute before requesting another OTP.");
			return;
		case "LIMIT":
			request.setAttribute("error", "You have reached maximum OTP requests for today.");
			return;
		default:
			boolean sent = EmailUtil.sendOTP(email, otp);
			if (sent) {
				request.setAttribute("message", "OTP sent successfully to " + email);
				request.setAttribute("showOtp", true);
			} else {
				request.setAttribute("error", "Failed to send OTP. Please try again.");
			}
		}
	}

	private boolean handleVerifyOTP(HttpServletRequest request, String type) {
		String inputOtp = request.getParameter("otp");
		HttpSession session = request.getSession();

		String result = OTPUtil.verifyOTP(session, inputOtp);

		switch (result) {
		case "SUCCESS":
			request.setAttribute("message", "OTP verified successfully!");
			session.setAttribute(type + "OtpVerified", true); 
			return true;
		case "EXPIRED":
			request.setAttribute("error", "OTP expired. Please request a new one.");
			break;
		case "INVALID":
			request.setAttribute("error", "Invalid OTP. Please try again.");
			request.setAttribute("showOtp", true);
			break;
		case "ATTEMPTS_EXCEEDED":
			request.setAttribute("error", "Too many wrong attempts. Please request a new OTP.");
			break;
		case "NO_OTP":
			request.setAttribute("error", "No OTP found. Please request again.");
			break;
		}
		return false;
	}

	private void handleRegisterAsUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.getContentType().startsWith("multipart/")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form must be multipart/form-data");
			return;
		}

		Boolean otpVerified = (Boolean) request.getSession().getAttribute("userOtpVerified");
		if (otpVerified == null || !otpVerified) {
			request.setAttribute("showOtp", true);
			request.setAttribute("error", "Please verify OTP before registering.");
			request.getRequestDispatcher("registerUser.jsp").forward(request, response);
			return;
		}

		RegisterRequestDTO dto = new RegisterRequestDTO();
		dto.setUsername(request.getParameter("name"));
		dto.setEmail(request.getParameter("email"));
		dto.setPassword(request.getParameter("password"));
		dto.setConfirmPassword(request.getParameter("confirmPassword"));
		Map<String, String> errors = authService.validateRegisterDto(dto);

		String imageUrl = null;
		try {
			Part filePart = request.getPart("profileImage");
			imageUrl = CloudinaryUtil.uploadImage(filePart);
		} catch (Exception e) {
			e.printStackTrace();
			errors.put("profileImage", e.getMessage());
		}
		dto.setImageurl(imageUrl);
//		User user = new User();
//		user.setUserName(dto.getUsername());
//		user.setUserEmail(dto.getEmail());
//		user.setUserPassword(PasswordHashing.ecryptPassword(dto.getPassword()));
//		user.setImageurl(dto.getImageurl());
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("registerUser.jsp").forward(request, response);
			return;
		}

		userService.register(dto);
		request.setAttribute("success", "User registered successfully. Please login.");
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return;
	}

	private void handleRegisterAsAgency(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!request.getContentType().startsWith("multipart/")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form must be multipart/form-data");
			return;
		}
		Boolean otpVerified = (Boolean) request.getSession().getAttribute("agencyOtpVerified");
		if (otpVerified == null || !otpVerified) {
			request.setAttribute("showOtp", true);
			request.setAttribute("error", "Please verify OTP before registering.");
			request.getRequestDispatcher("registerAgency.jsp").forward(request, response);
			return;
		}

		AgencyRegisterRequestDTO dto = new AgencyRegisterRequestDTO();
		dto.setAgencyName(request.getParameter("agency_name"));
		dto.setOwnerName(request.getParameter("owner_name"));
		dto.setEmail(request.getParameter("email"));
		dto.setPhone(request.getParameter("phone"));
		dto.setCity(request.getParameter("city"));
		dto.setState(request.getParameter("state"));
		dto.setCountry(request.getParameter("country"));
		dto.setPincode(request.getParameter("pincode"));
		dto.setRegistrationNumber(request.getParameter("registration_number"));
		dto.setPassword(request.getParameter("password"));
		dto.setConfirmPassword(request.getParameter("confirm_password"));
		Map<String, String> errors = authService.validateRegisterAgencyDto(dto);

		String imageUrl = null;
		try {
			Part filePart = request.getPart("profileImage");
			imageUrl = CloudinaryUtil.uploadImage(filePart);
		} catch (Exception e) {
			e.printStackTrace();
			errors.put("profileImage", e.getMessage());
		}
		dto.setImageurl(imageUrl);
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("registration_number", dto.getRegistrationNumber());
			request.getRequestDispatcher("registerAgency.jsp").forward(request, response);
			return;
		}

		agencyService.register(dto);
		request.setAttribute("success", "Agency registered successfully! Waiting for admin approval.");
		request.getRequestDispatcher("login.jsp?role=agency").forward(request, response);
		return;

	}

	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginRequestDTO dto = new LoginRequestDTO();
		RequestDispatcher rd = null;
		dto.setEmail(request.getParameter("email"));
		dto.setPassword(request.getParameter("password"));
		dto.setRole(request.getParameter("role"));
		Map<String, String> errors = new HashMap<>();
		if ("user".equalsIgnoreCase(dto.getRole())) {
			errors = authService.validateLoginDto(dto);
		} else if ("agency".equalsIgnoreCase(dto.getRole())) {
			errors = authService.validateLoginAgencyDto(dto);
		} else {
			errors.put("role", "Invalid role selected!");
		}

		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}
		if ("user".equalsIgnoreCase(dto.getRole())) {
			UserResponseDTO loggedInUser = userService.login(dto);
			HttpSession session = request.getSession();
			session.setAttribute("user", loggedInUser);
			if ("ADMIN".equalsIgnoreCase(loggedInUser.getUserRole())) {
//				rd = request.getRequestDispatcher("template/admin/adminDashboard.jsp");
//				rd.forward(request, response);
				response.sendRedirect("admin?button=dashboard");
				return;
//                 response.sendRedirect("template/admin/adminDashboard.jsp");
			} else {
//                 response.sendRedirect("template/user/userDashboard.jsp");
//				rd = request.getRequestDispatcher("template/user/userDashboard.jsp");
				response.sendRedirect("user?button=dashboard");
//				rd.forward(request, response);
				return;
			}
		} else if ("agency".equalsIgnoreCase(dto.getRole())) {
			AgencyResponseDTO loggedInAgency = agencyService.login(dto);
			HttpSession session = request.getSession();
			session.setAttribute("agency", loggedInAgency);
//             response.sendRedirect("template/agency/agencyDashboard.jsp");
//			rd = request.getRequestDispatcher("template/agency/agencyDashboard.jsp");
//			rd.forward(request, response);
			response.sendRedirect("agency?button=dashboard");
			return;
		} else {
			errors.put("loginError", "Invalid credentials ");
			request.setAttribute("errors", errors);
			System.out.println("ERROR IN AUTH_SERVLETS => " + errors.get("loginError"));
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}

	}

	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect(request.getContextPath() + "/login.jsp");

		return;
	}
}
