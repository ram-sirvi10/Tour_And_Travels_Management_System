package com.travelmanagement.filter;

import java.io.IOException;

import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({ "/auth", "/login.jsp", "/register.jsp", "/forgotPassword.jsp", "/resetPassword.jsp" })
public class PublicAccessFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String action = req.getParameter("button"); // check for logout

		// Allow logout to pass
		if ("logout".equals(action) || "registerAsUser".equals(action) || "registerAsAgency".equals(action)) {
			chain.doFilter(request, response);
			return;
		}

		UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("user") : null;
		AgencyResponseDTO agency = (session != null) ? (AgencyResponseDTO) session.getAttribute("agency") : null;

		if (user != null || agency != null) {
			String role = null;

			if (user != null) {
				role = user.getUserRole(); // get role from UserResponseDTO
			} else if (agency != null) {
				role = "SUBADMIN"; // get role from AgencyResponseDTO
			}

			// Redirect based on role
			if ("ADMIN".equalsIgnoreCase(role)) {
				res.sendRedirect(req.getContextPath() + "/admin?button=dashboard");
				return;
			} else if ("SUBADMIN".equalsIgnoreCase(role)) {
				res.sendRedirect(req.getContextPath() + "/agency?button=dashboard");
				return;
			} else if ("USER".equalsIgnoreCase(role)) {
				res.sendRedirect(req.getContextPath() + "/user?button=dashboard");
				return;
			}
		}

		// No session or not logged in → allow access
		chain.doFilter(request, response);
	}
}
