package com.travelmanagement.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserServiceImpl userService = new UserServiceImpl();
	private AgencyServiceImpl agencyService = new AgencyServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("button");
		System.out.println("ADMIN ACTION => " + action);

		try {
			switch (action) {
			case "dashboard":
				dashboard(request, response);
				break;

			case "manageUsers":
				manageUsers(request, response);
				break;

			case "userAction":
				userAction(request, response);
				break;

			case "manageAgencies":
				manageAgencies(request, response);
				break;

			case "agencyAction":
				agencyAction(request, response);
				break;
			case "pendingAgencies":

				pendingAgencies(request, response);
				break;
			case "deletedAgencies":
				deletedAgencies(request, response);

				break;
			case "deletedUsers":
				deletedUsers(request, response);
				break;

			case "updateProfile":
				updateProfile(request, response);
				break;
			case "changePassword":
				changePassword(request, response);
			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/admin/adminDashboard.jsp");
		}
	}

	private void changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		HttpSession session = request.getSession();
		AuthServiceImpl authService = new AuthServiceImpl();
		UserServiceImpl userService = new UserServiceImpl();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

		Map<String, String> errors = authService.validateChangePassword(newPassword, confirmPassword);

		if (!errors.isEmpty()) {
			request.setAttribute("actionType", "changePassword");
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			return;
		}

		boolean updated = userService.changePassword(user.getUserId(), newPassword);
		if (updated) {
			request.setAttribute("successMessage", "Password changed successfully!");
		} else {
			request.setAttribute("errorMessage", "Failed to change password!");
		}
		request.setAttribute("actionType", "changePassword");
		request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
		return;
	}

	private void updateProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("user");

		RegisterRequestDTO dto = new RegisterRequestDTO();
		dto.setUserId(currentUser.getUserId());
		dto.setUsername(request.getParameter("userName"));
		dto.setEmail(request.getParameter("userEmail"));

		AuthServiceImpl authService = new AuthServiceImpl();
		UserServiceImpl userService = new UserServiceImpl();

		Map<String, String> errors = authService.validateUpdateUser(dto);

		if (!errors.isEmpty()) {
			request.setAttribute("actionType", "updateProfile");
			request.setAttribute("errors", errors);
			request.setAttribute("formData", dto);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			return;
		}

		boolean updated = userService.update(dto);
		if (updated) {

			UserResponseDTO updatedUser = userService.getById(dto.getUserId());
			session.setAttribute("user", updatedUser);
			request.setAttribute("successMessage", "Profile updated successfully!");
		} else {
			request.setAttribute("errorMessage", "Failed to update profile!");
		}
		request.setAttribute("actionType", "updateProfile");
		request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
		return;
	}

	private void pendingAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String status = request.getParameter("status");
		Boolean active = null;
		if (request.getParameter("active") != null) {
			active = Boolean.parseBoolean(request.getParameter("active"));
		}
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			keyword = keyword.trim();
		}
		if (status == null || status.isEmpty()) {
			status = "PENDING";
		}

		int page = 1;
		int pageSize = 1;
		String pageParam = request.getParameter("page");
		String sizeParam = request.getParameter("pageSize");

		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
				if (page < 1)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		if (sizeParam != null) {
			try {
				pageSize = Integer.parseInt(sizeParam);
				if (pageSize < 1)
					pageSize = 1;
			} catch (NumberFormatException e) {
				pageSize = 1;
			}
		}

		int offset = (page - 1) * pageSize;

		/////// change
		long totalItems = agencyService.countAgencies(status, active, false, keyword);
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		
		if (totalPages < 1) {
		    totalPages = 1;
		}

		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("keyword", keyword);
		request.setAttribute("status", status);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);

		List<AgencyResponseDTO> pendingAgencies = agencyService.filterAgencies(status, active, startDate, endDate,
				keyword, false,pageSize, offset);
		request.setAttribute("agenciesList", pendingAgencies);
		request.setAttribute("listType", "Pending Agencies");
		request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}

	private void dashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			long totalUsers = userService.countUser(true, false, "");

			long totalAgencies = agencyService.countAgencies("APPROVED", null, false, "");

			long pendingAgencies = agencyService.countAgencies("PENDING", false, true, "");

			long rejectedAgencies = agencyService.countAgencies("REJECTED", false, true, "");

			System.out.println("totalUsers === " + totalUsers);
			System.out.println("totalAgencies === " + totalAgencies);
			System.out.println("pendingAgencies === " + pendingAgencies);
			System.out.println("rejectedAgencies === " + rejectedAgencies);

			request.setAttribute("totalUsers", totalUsers);
			request.setAttribute("totalAgencies", totalAgencies);
			request.setAttribute("pendingAgencies", pendingAgencies);
			request.setAttribute("rejectedAgencies", rejectedAgencies);

			request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/admin/adminDashboard.jsp");
		}
	}

	private void deletedUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			keyword = keyword.trim();
		}
		int page = 1;
		int pageSize = 1;
		String pageParam = request.getParameter("page");
		String sizeParam = request.getParameter("pageSize");

		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
				if (page < 1)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		if (sizeParam != null) {
			try {
				pageSize = Integer.parseInt(sizeParam);
				if (pageSize < 1)
					pageSize = 1;
			} catch (NumberFormatException e) {
				pageSize = 1;
			}
		}

		int offset = (page - 1) * pageSize;

		List<UserResponseDTO> usersList = userService.getDeletedUsers(keyword, pageSize, offset);
		System.out.println("manage user=====>>");
		System.out.println("keyword===" + keyword);

		System.out.println(usersList.toString());
		long totalUsers = userService.countUser(false, true, keyword);
		int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
		if (totalPages < 1) {
		    totalPages = 1;
		}
		request.setAttribute("usersList", usersList);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("keyword", keyword);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("listType", "Deleted Users");

		request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
	}

	private void manageUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String activeParam = request.getParameter("active");
		Boolean active = null;
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			keyword = keyword.trim();
		}

		if ("true".equals(activeParam)) {
			active = true;
		} else if ("false".equals(activeParam)) {
			active = false;
		}

		int page = 1;
		int pageSize = 1;
		String pageParam = request.getParameter("page");
		String sizeParam = request.getParameter("pageSize");

		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
				if (page < 1)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}

		if (sizeParam != null) {
			try {
				pageSize = Integer.parseInt(sizeParam);
				if (pageSize < 1)
					pageSize = 1;
			} catch (NumberFormatException e) {
				pageSize = 1;
			}
		}

		int offset = (page - 1) * pageSize;
		List<UserResponseDTO> usersList = userService.getAll(active, false, keyword, pageSize, offset);

		long totalUsers = userService.countUser(active, false, keyword);
		int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
		if (totalPages < 1) {
		    totalPages = 1;
		}
		System.out.println("manage user=====>>");
		System.out.println("keyword===" + keyword);
		System.out.println("active===" + active);

		System.out.println(usersList.toString());
		request.setAttribute("usersList", usersList);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("keyword", keyword);
		request.setAttribute("listType", "Manage Users");

		request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
	}

	private void userAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actionType = request.getParameter("action");
		String userIdStr = request.getParameter("userId");
		System.out.println(actionType);
		if (actionType != null && userIdStr != null) {
			int userId = Integer.parseInt(userIdStr);
			switch (actionType) {
			case "activate":
				userService.updateUserActiveState(userId, true);
				break;
			case "deactivate":
				userService.updateUserActiveState(userId, false);
				break;
			case "delete":
				userService.delete(userId);
				break;

			default:
				break;
			}
		}
		
		
		manageUsers(request, response);
	}

	private void deletedAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String keyword = request.getParameter("keyword");
		if (keyword != null) {
			keyword = keyword.trim();
		}
		int page = 1;
		int pageSize = 1;
		String pageParam = request.getParameter("page");
		String sizeParam = request.getParameter("pageSize");

		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
				if (page < 1)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		if (sizeParam != null) {
			try {
				pageSize = Integer.parseInt(sizeParam);
				if (pageSize < 1)
					pageSize = 1;
			} catch (NumberFormatException e) {
				pageSize = 1;
			}
		}

		int offset = (page - 1) * pageSize;

		long totalItems = agencyService.countAgencies(null, false, true, keyword);
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		if (totalPages < 1) {
		    totalPages = 1;
		}
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("keyword", keyword);
		List<AgencyResponseDTO> deletedAgencies = agencyService.getDeletedAgencies(keyword, pageSize, offset);
		request.setAttribute("agenciesList", deletedAgencies);
		request.setAttribute("listType", "Deleted Agencies");
		request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}

	private void manageAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String status = request.getParameter("status");
		String activeParam = request.getParameter("active");
		Boolean active = null;
		if ("true".equals(activeParam))
			active = true;
		else if ("false".equals(activeParam))
			active = false;

		String keyword = request.getParameter("keyword");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if (keyword != null) {
			keyword = keyword.trim();
		}
		int page = 1;
		int pageSize = 1;
		String pageParam = request.getParameter("page");
		String sizeParam = request.getParameter("pageSize");

		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
				if (page < 1)
					page = 1;
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		if (sizeParam != null) {
			try {
				pageSize = Integer.parseInt(sizeParam);
				if (pageSize < 1)
					pageSize = 1;
			} catch (NumberFormatException e) {
				pageSize = 1;
			}
		}

		int offset = (page - 1) * pageSize;

		List<AgencyResponseDTO> agenciesList = agencyService.filterAgencies(status, active, startDate, endDate, keyword,
				false,pageSize, offset);
		System.out.println("manage agency ==== ");
		System.out.println("status == " + status);
		System.out.println("active == " + active);
		System.out.println("keyword == " + keyword);
		System.out.println("startDate == " + startDate);
		System.out.println("endDate == " + endDate);
		System.out.println("pagination in manage agency -===");
		System.out.println("pagesize"+pageSize);
		System.out.println("page"+page);
		System.out.println("list size  = "+agenciesList.size());
		System.out.println(agenciesList.toString());
		long totalItems = agencyService.countAgencies(status, active, false, keyword);
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		if (totalPages < 1) {
		    totalPages = 1;
		}
		request.setAttribute("agenciesList", agenciesList);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("keyword", keyword);
		request.setAttribute("status", status);
		request.setAttribute("active", activeParam);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
		request.setAttribute("listType", "Manage Agencies");

		request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}

	private void agencyAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actionType = request.getParameter("action");
		String agencyIdStr = request.getParameter("agencyId");
	
		if (actionType != null && agencyIdStr != null) {
			int agencyId = Integer.parseInt(agencyIdStr);

			switch (actionType) {
			case "activate":
				agencyService.updateAgencyActiveState(agencyId, true);
				request.setAttribute(agencyIdStr, actionType);
				break;
			case "deactivate":
				agencyService.updateAgencyActiveState(agencyId, false);
				break;
			case "delete":
				agencyService.deleteAgency(agencyId);
				break;
			case "approve":
				agencyService.updateAgencyStatus(agencyId, "APPROVED");
				break;
			case "reject":
				agencyService.updateAgencyStatus(agencyId, "REJECTED");
				break;
			default:
				break;
			}
		}
	
		if ("approve".equalsIgnoreCase(actionType) || "reject".equalsIgnoreCase(actionType)) {
			pendingAgencies(request, response);
		} else {
			manageAgencies(request, response);
		}
	}

}
