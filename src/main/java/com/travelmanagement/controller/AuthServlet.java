package com.travelmanagement.controller;

import java.io.IOException;
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

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AuthServlet")
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserService userService = new UserServiceImpl();
	private AuthServiceImpl authService = new AuthServiceImpl();
	private IAgencyService agencyService = new AgencyServiceImpl();
	
//	public AuthServlet() {
//		createAdmin();
//	}
	
	
	//USE THIS METHOD TO ADD ADMIN
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

	private void handleRegisterAsUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		RegisterRequestDTO dto = new RegisterRequestDTO();
		dto.setUsername(request.getParameter("name"));
		dto.setEmail(request.getParameter("email"));
		dto.setPassword(request.getParameter("password"));
		dto.setConfirmPassword(request.getParameter("confirmPassword"));

		Map<String, String> errors = authService.validateRegisterDto(dto);

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
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("agency_name", dto.getAgencyName());
			request.setAttribute("owner_name", dto.getOwnerName());
			request.setAttribute("email", dto.getEmail());
			request.setAttribute("phone", dto.getPhone());
			request.setAttribute("city", dto.getCity());
			request.setAttribute("state", dto.getState());
			request.setAttribute("country", dto.getCountry());
			request.setAttribute("pincode", dto.getPincode());
			request.setAttribute("registration_number", dto.getRegistrationNumber());
			request.getRequestDispatcher("registerAgency.jsp").forward(request, response);
			return;
		}

		agencyService.register(dto);
		request.setAttribute("success", "Agency registered successfully! Waiting for admin approval.");
		request.getRequestDispatcher("login.jsp").forward(request, response);
		return;

	}

	private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginRequestDTO dto = new LoginRequestDTO();
		RequestDispatcher rd = null;
		dto.setEmail(request.getParameter("email"));
		dto.setPassword(request.getParameter("password"));
		dto.setRole(request.getParameter("role"));
		Map<String, String> errors = authService.validateLoginDto(dto);

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
				response.sendRedirect("AdminServlet?button=dashboard");
				return;
//                 response.sendRedirect("template/admin/adminDashboard.jsp");
			} else {
//                 response.sendRedirect("template/user/userDashboard.jsp");
//				rd = request.getRequestDispatcher("template/user/userDashboard.jsp");
				response.sendRedirect("UserServlet?button=dashboard");
//				rd.forward(request, response);
				return;
			}
		} else if ("agency".equalsIgnoreCase(dto.getRole())) {
			AgencyResponseDTO loggedInAgency = agencyService.login(dto);
			HttpSession session = request.getSession();
			session.setAttribute("user", loggedInAgency);
//             response.sendRedirect("template/agency/agencyDashboard.jsp");
			rd = request.getRequestDispatcher("template/agency/agencyDashboard.jsp");
			rd.forward(request, response);
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
		HttpSession session  	= request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		response.sendRedirect(request.getContextPath() + "/login.jsp");

		return;
	}
}
