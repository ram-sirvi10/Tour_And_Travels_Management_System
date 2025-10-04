package com.travelmanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.util.CloudinaryUtil;
import com.travelmanagement.util.Constants;
import com.travelmanagement.util.PasswordHashing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/admin")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 5, // 5 MB
		maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
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
			case Constants.ACTION_DASHBOARD:
				dashboard(request, response);
				break;

			case Constants.ACTION_MANAGE_USERS:
				manageUsers(request, response);
				break;

			case Constants.ACTION_USER_ACTION:
				userAction(request, response);
				break;

			case Constants.ACTION_MANAGE_AGENCIES:
				manageAgencies(request, response);
				break;

			case Constants.ACTION_AGENCY_ACTION:
				agencyAction(request, response);
				break;

			case Constants.ACTION_PENDING_AGENCIES:
				pendingAgencies(request, response);
				break;

			case Constants.ACTION_DELETED_AGENCIES:
				deletedAgencies(request, response);
				break;

			case Constants.ACTION_DELETED_USERS:
				deletedUsers(request, response);
				break;

			case Constants.ACTION_UPDATE_PROFILE:
				updateProfile(request, response);
				break;

			case Constants.ACTION_CHANGE_PASSWORD:
				changePassword(request, response);
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/admin/adminDashboard.jsp");
		}
	}

	public void changePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		String oldPassword = request.getParameter("oldPassword");
		HttpSession session = request.getSession();
		AuthServiceImpl authService = new AuthServiceImpl();
		UserServiceImpl userService = new UserServiceImpl();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
		try {
			UserResponseDTO dbUser = userService.getByEmail(user.getUserEmail());

			Map<String, String> errors = authService.validateChangePassword(newPassword, confirmPassword, oldPassword);

			System.out.println("old pass" + oldPassword);
			System.out.println("dp pass" + dbUser.getUserPassword());
			if (oldPassword != null && !oldPassword.isEmpty()) {
				if (!PasswordHashing.checkPassword(oldPassword, dbUser.getUserPassword())) {
					errors.put("oldPassword", Constants.ERROR_OLD_PASSWORD);
				}
			}

			if (!errors.isEmpty()) {
				request.setAttribute("actionType", Constants.ACTION_CHANGE_PASSWORD);
				request.setAttribute("errors", errors);
				request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
				return;
			}

			boolean updated = userService.changePassword(user.getUserId(), newPassword);
			if (updated) {
				request.setAttribute("successMessage", Constants.SUCCESS_PASSWORD_CHANGE);
			} else {
				request.setAttribute("errorMessage", Constants.ERROR_PASSWORD_CHANGE);
			}
			request.setAttribute("actionType", Constants.ACTION_CHANGE_PASSWORD);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("actionType", Constants.ACTION_CHANGE_PASSWORD);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			e.printStackTrace();
			return;
		}
	}

	public void updateProfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			HttpSession session = request.getSession();
			UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("user");
			RegisterRequestDTO dto = new RegisterRequestDTO();
			dto.setUserId(currentUser.getUserId());
			dto.setUsername(request.getParameter("userName"));
			dto.setEmail(request.getParameter("userEmail"));

			AuthServiceImpl authService = new AuthServiceImpl();
			UserServiceImpl userService = new UserServiceImpl();

			Map<String, String> errors = authService.validateUpdateUser(dto);
			if (!request.getContentType().startsWith("multipart/")) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form must be multipart/form-data");
				return;
			}
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
				request.setAttribute("actionType", Constants.ACTION_UPDATE_PROFILE);
				request.setAttribute("errors", errors);
				request.setAttribute("formData", dto);
				request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
				return;
			}

			boolean updated = userService.update(dto);
			if (updated) {
				UserResponseDTO updatedUser = userService.getById(dto.getUserId());
				session.setAttribute("user", updatedUser);
				request.setAttribute("successMessage", Constants.SUCCESS_PROFILE_UPDATE);
			} else {
				request.setAttribute("errorMessage", Constants.ERROR_PROFILE_UPDATE);
			}
			request.setAttribute("actionType", Constants.ACTION_UPDATE_PROFILE);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("actionType", Constants.ACTION_UPDATE_PROFILE);
			request.getRequestDispatcher("template/admin/profileManagement.jsp").forward(request, response);
			return;
		}
	}

	private void pendingAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
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
			int pageSize = 5;
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
						pageSize = 5;
				} catch (NumberFormatException e) {
					pageSize = 5;
				}
			}

			int offset = (page - 1) * pageSize;

			long totalItems = agencyService.countAgencies(status, active, false, keyword, startDate, endDate);
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
			List<AgencyResponseDTO> agenciesList = new ArrayList<AgencyResponseDTO>();
			if ("REJECTED".equalsIgnoreCase(status)) {

				agenciesList = agencyService.filterAgencies(status, false, startDate, endDate, keyword, true, pageSize,
						offset);
			} else {
				agenciesList = agencyService.filterAgencies(status, false, startDate, endDate, keyword, true, pageSize,
						offset);
			}

			System.out.println("pending  agency ==== ");
			System.out.println("status == " + status);
			System.out.println("active == " + active);
			System.out.println("keyword == " + keyword);
			System.out.println("startDate == " + startDate);
			System.out.println("endDate == " + endDate);
			System.out.println("pagination in pending agency -===");
			System.out.println("pagesize" + pageSize);
			System.out.println("page" + page);
			System.out.println("list size  = " + agenciesList.size());

			request.setAttribute("agenciesList", agenciesList);
			request.setAttribute("listType", status + " Agencies");
			request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("agenciesList", new ArrayList<AgencyResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 5);
			request.setAttribute("listType", "Manage Agencies");
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("status", "PENDING");

			request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
			return;
		}
	}

	private void dashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// ================= CARDS =================
			long totalUsers = userService.countUser(true, false, "");
			long totalAgencies = agencyService.countAgencies("APPROVED", null, false, "", null, null);
			long pendingAgencies = agencyService.countAgencies("PENDING", false, true, "", null, null);
			long rejectedAgencies = agencyService.countAgencies("REJECTED", false, true, "", null, null);

			long activeUsers = userService.countUser(true, false, "");
			long inactiveUsers = userService.countUser(false, false, "");
			long activeAgencies = agencyService.countAgencies(null, true, false, "", null, null);
			long inactiveAgencies = agencyService.countAgencies(null, false, false, "", null, null);

			request.setAttribute("totalUsers", totalUsers);
			request.setAttribute("totalAgencies", totalAgencies);
			request.setAttribute("pendingAgencies", pendingAgencies);
			request.setAttribute("rejectedAgencies", rejectedAgencies);

			request.setAttribute("activeUsers", activeUsers);
			request.setAttribute("inactiveUsers", inactiveUsers);
			request.setAttribute("activeAgencies", activeAgencies);
			request.setAttribute("inactiveAgencies", inactiveAgencies);

			List<AgencyResponseDTO> recentPending = agencyService.filterAgencies("PENDING", false, null, null, "", true,
					10, 0);
			request.setAttribute("agenciesList", recentPending);

			request.getRequestDispatcher("template/admin/adminDashboard.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/admin/adminDashboard.jsp");
		}
	}

	private void deletedUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String keyword = request.getParameter("keyword");
			if (keyword != null) {
				keyword = keyword.trim();
			}
			int page = 1;
			int pageSize = 5;
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
						pageSize = 5;
				} catch (NumberFormatException e) {
					pageSize = 5;
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
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("usersList", new ArrayList<UserResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 5);
			request.setAttribute("keyword", "");
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("listType", "Deleted Users");
			request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
			return;
		}
	}

	private void manageUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
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
			int pageSize = 5;
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
						pageSize = 5;
				} catch (NumberFormatException e) {
					pageSize = 5;
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
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("usersList", new ArrayList<UserResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 5);
			request.setAttribute("keyword", "");
			request.setAttribute("listType", "Manage Users");
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
		}
	}

	private void userAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {

			String actionType = request.getParameter("action");

			String userIdStr = request.getParameter("userId");
			HttpSession session = request.getSession();
			UserResponseDTO sessionUser = (UserResponseDTO) session.getAttribute("user");

			System.out.println(actionType);
			if (actionType != null && userIdStr != null) {
				int userId = Integer.parseInt(userIdStr);
				if (sessionUser.getUserId() != userId) {

					switch (actionType) {
					case Constants.USER_ACTIVATE:
						userService.updateUserActiveState(userId, true);
						break;
					case Constants.USER_DEACTIVATE:
						userService.updateUserActiveState(userId, false);
						break;
					case Constants.USER_DELETE:
						userService.delete(userId);
						break;
					default:
						request.setAttribute("errorMessage", Constants.ERROR_INVALID_ACTION);
						break;

					}
				} else {
					request.setAttribute("errorMessage", Constants.ERROR_INVALID_ACTION);
				}
			}

			manageUsers(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			manageUsers(request, response);
		}
	}

	private void deletedAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String keyword = request.getParameter("keyword");
			if (keyword != null) {
				keyword = keyword.trim();
			}
			int page = 1;
			int pageSize = 5;
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
						pageSize = 5;
				} catch (NumberFormatException e) {
					pageSize = 5;
				}
			}

			int offset = (page - 1) * pageSize;

			long totalItems = agencyService.countAgencies("APPROVED", false, true, keyword, null, null);
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
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("agenciesList", new ArrayList<AgencyResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 10);
			request.setAttribute("listType", "Deleted Agencies");
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
		}

	}

	private void manageAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
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
			LocalDate start = null, end = null;
			try {
				if (startDate != null && !startDate.isEmpty())
					start = LocalDate.parse(startDate);
				if (endDate != null && !endDate.isEmpty())
					end = LocalDate.parse(endDate);
			} catch (Exception e) {

			}

			if (start != null && end != null && start.isAfter(end)) {
				request.setAttribute("errorMessage", Constants.ERROR_START_AFTER_END_DATE);
				start = null;
				end = null;
			}

			if (keyword != null) {
				keyword = keyword.trim();
			}
			int page = 1;
			int pageSize = 5;
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
						pageSize = 5;
				} catch (NumberFormatException e) {
					pageSize = 5;
				}
			}

			int offset = (page - 1) * pageSize;

			List<AgencyResponseDTO> agenciesList = agencyService.filterAgencies(status, active,
					start != null ? start.toString() : null, end != null ? end.toString() : null, keyword, false,
					pageSize, offset);
			System.out.println("manage agency ==== ");
			System.out.println("status == " + status);
			System.out.println("active == " + active);
			System.out.println("keyword == " + keyword);
			System.out.println("startDate == " + startDate);
			System.out.println("endDate == " + endDate);
			System.out.println("pagination in manage agency -===");
			System.out.println("pagesize" + pageSize);
			System.out.println("page" + page);
			System.out.println("list size  = " + agenciesList.size());
			System.out.println(agenciesList.toString());
			long totalItems = agencyService.countAgencies(status, active, false, keyword,
					start != null ? start.toString() : null, end != null ? end.toString() : null);
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
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("agenciesList", new ArrayList<AgencyResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 10);
			request.setAttribute("status", "ALL");
			request.setAttribute("listType", "Manage Agencies");
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
		}
	}

	private void agencyAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("agenciesList", new ArrayList<AgencyResponseDTO>());
			request.setAttribute("currentPage", 1);
			request.setAttribute("totalPages", 1);
			request.setAttribute("pageSize", 5);
			request.setAttribute("listType", "Manage Agencies");
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
		}
	}

}
