package com.travelmanagement.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dao.impl.AgencyDAOImpl;
import com.travelmanagement.dao.impl.UserDAOImpl;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//@WebFilter({ "/AdminServlet/*", "/AgencyServlet/*", "/UserServlet/*", "/BookingServlet/*", "/PackageServlet/*" })
//public class AuthRoleFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        HttpSession session = req.getSession(false);
//
//        // Get user/agency from session
//        UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("user") : null;
//        AgencyResponseDTO agency = (session != null) ? (AgencyResponseDTO) session.getAttribute("agency") : null;
//
//        // If no session or no logged-in object, redirect to login
//        if (user == null && agency == null) {
//            res.sendRedirect(req.getContextPath() + "/login.jsp?error=notLoggedIn");
//            return;
//        }
//
//        // Get role from object
//        String role = null;
//        if (user != null) {
//            role = user.getUserRole();
//        } else if (agency != null) {
//            role = "SUBADMIN";
//        }
//
//        String path = req.getRequestURI();
//        
//        System.out.println("PATH ==> "+path+" , ROLE ==> "+role);
//
//        boolean allowed = switch (role) {
//            case "ADMIN" -> path.startsWith(req.getContextPath() + "/AdminServlet")
//                    || path.startsWith(req.getContextPath() + "/AgencyServlet")
//                    || path.startsWith(req.getContextPath() + "/UserServlet");
//            case "SUBADMIN" -> path.startsWith(req.getContextPath() + "/AgencyServlet")
//                    || path.startsWith(req.getContextPath() + "/BookingServlet")
//                    || path.startsWith(req.getContextPath() + "/PackageServlet")
//                    || path.startsWith(req.getContextPath() + "/UserServlet");
//            case "USER" -> path.startsWith(req.getContextPath() + "/UserServlet")
//                    || path.startsWith(req.getContextPath() + "/BookingServlet")
//                    || path.startsWith(req.getContextPath() + "/PackageServlet");
//            default -> false;
//        };
//
//        if (allowed) {
//            chain.doFilter(request, response);
//        } else {
//            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
//        }
//    }
//}

@WebFilter({ "/admin/*", "/agency/*", "/user/*", "/booking/*", "/package/*" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 5, // 5 MB
		maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class AuthRoleFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

	
//	        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
//	        res.setHeader("Pragma", "no-cache"); 
//	        res.setDateHeader("Expires", 0); 
		
		UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("user") : null;
		AgencyResponseDTO agency = (session != null) ? (AgencyResponseDTO) session.getAttribute("agency") : null;

		if (user == null && agency == null) {
			res.sendRedirect(req.getContextPath() + "/login.jsp?error=notLoggedIn");
			return;
		}

		boolean isActive = true;
		if (user != null) {
			UserDAOImpl userDAO = new UserDAOImpl();
			User latestUser;
			try {
				latestUser = userDAO.getUserById(user.getUserId());
				if (latestUser == null || !latestUser.isActive()) {
					isActive = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (agency != null) {
			AgencyDAOImpl agencyDAO = new AgencyDAOImpl();
			Agency latestAgency;
			try {
				latestAgency = agencyDAO.getAgencyById(agency.getAgencyId());
				if (latestAgency == null || !latestAgency.isActive()) {
					isActive = false;
				}
			} catch (Exception e) {
			
				e.printStackTrace();
			}

		}

		if (!isActive) {
			if (session != null) {
				session.invalidate();
			}
			res.sendRedirect(req.getContextPath() + "/login.jsp?error=inactive");
			return;
		}

		String role = (user != null) ? user.getUserRole() : "SUBADMIN";
		String path = req.getRequestURI();
		String button = req.getParameter("button"); // get the button param
		System.out.println("filter -> " + button);
		String context = req.getContextPath();

		Map<String, List<String>> adminAccess = new HashMap<>();
		adminAccess.put(context + "/admin",
				List.of("dashboard", "manageUsers", "manageAgencies", "userAction", "agencyAction", "pendingAgencies",
						"deletedAgencies", "deletedUsers", "updateProfile", "changePassword"));
//		adminAccess.put(context + "/agency", List.of("dashboard"));
//		adminAccess.put(context + "/user", List.of("dashboard"));

		Map<String, List<String>> subAdminAccess = new HashMap<>();
		subAdminAccess.put(context + "/agency", List.of("dashboard", "addPackage"));
		subAdminAccess.put(context + "/booking", List.of("viewBookings"));
		subAdminAccess.put(context + "/package", List.of("viewPackages"));
		subAdminAccess.put(context + "/user", List.of("viewUsers"));

		Map<String, List<String>> userAccess = new HashMap<>();
		userAccess.put(context + "/user", List.of("dashboard", "profile", "updateProfile", "changePassword","viewProfile"));
		userAccess.put(context + "/booking", List.of("book", "viewBookings", "createBooking", "paymentReject",
				"paymentConfirm", "viewBookingForm", "bookingHistroy", "viewTravelers","paymentHistory"));
		userAccess.put(context + "/package", List.of("viewPackages", "packageList"));

		boolean allowed = switch (role) {
		case "ADMIN" -> checkAccess(path, button, adminAccess);
		case "SUBADMIN" -> path.startsWith(context + "/agency")
        || checkAccess(path, button, subAdminAccess);

		case "USER" -> checkAccess(path, button, userAccess);
		default -> false;
		};

		if (allowed) {
			chain.doFilter(request, response);
		} else {
			res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied!");
		}
	}

	private boolean checkAccess(String path, String button, Map<String, List<String>> accessMap) {
		for (Map.Entry<String, List<String>> entry : accessMap.entrySet()) {
			if (path.startsWith(entry.getKey())) {
				System.out.println(button);
				List<String> buttons = entry.getValue();
				// if button param is null, allow only if list contains empty string or null`
				if (button == null) {
					return buttons.contains(null) || buttons.contains("");
				}
				return buttons.contains(button);
			}
		}
		return false;
	}
}
