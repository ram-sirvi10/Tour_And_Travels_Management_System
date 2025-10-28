package com.travelmanagement.filter;

import java.io.IOException;

import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.IAgencyService;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter({ "/auth", "/login.jsp", "/registerAsUser.jsp", "/registerAsAgency.jsp" })
public class PublicAccessFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		String action = req.getParameter("button");

		if ("logout".equals(action) ) {
			chain.doFilter(request, response);
			return;
		}

		UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("user") : null;
		AgencyResponseDTO agency = (session != null) ? (AgencyResponseDTO) session.getAttribute("agency") : null;

		// ====== Session restoration from authToken cookie ======
		if (user == null && agency == null) {
			String token = null;
			Cookie[] cookies = req.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if ("authToken".equals(c.getName())) {
						token = c.getValue();
						break;
					}
				}
			}

			if (token != null) {
				try {
					Claims claims = JwtUtil.parseToken(token);
					String id = claims.getSubject();
					String role = claims.get("role", String.class);

					UserServiceImpl userService = new UserServiceImpl();
					if ("USER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
						UserResponseDTO loggedInUser = null;
						try {
							loggedInUser = userService.getById(Integer.parseInt(id));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (loggedInUser != null) {
							session = req.getSession(true);
							session.setAttribute("user", loggedInUser);
							user = loggedInUser;
						}
					} else if ("SUBADMIN".equalsIgnoreCase(role)) {
						IAgencyService agencyService = new AgencyServiceImpl();
						AgencyResponseDTO loggedInAgency = null;
						try {
							loggedInAgency = agencyService.getAgencyById(Integer.parseInt(id));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (loggedInAgency != null) {
							session = req.getSession(true);
							session.setAttribute("agency", loggedInAgency);
							agency = loggedInAgency;
						}
					}
				} catch (JwtException | NumberFormatException e) {
					res.sendRedirect(req.getContextPath()+"/");
					return;
				}
			}
		}
		

		if (user != null || agency != null) {
			String role = null;

			if (user != null) {
				role = user.getUserRole(); 
			} else if (agency != null) {
				role = "SUBADMIN"; 
			}

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

		chain.doFilter(request, response);
	}
}
