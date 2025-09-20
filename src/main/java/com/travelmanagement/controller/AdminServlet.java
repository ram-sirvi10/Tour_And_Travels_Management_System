package com.travelmanagement.controller;

import java.io.IOException;
import java.util.List;

import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/admin/adminDashboard.jsp");
		}
	}

	private void pendingAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String status = request.getParameter("status");
		String active = request.getParameter("active");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String keyword = request.getParameter("keyword");
		if (status == null || status.isEmpty()) {
			status = "PENDING";
		}

		List<AgencyResponseDTO> pendingAgencies = agencyService.filterAgencies(status, active, startDate, endDate,
				keyword, 100, 0);

		request.setAttribute("agenciesList", pendingAgencies);
		request.setAttribute("listType", "Pending Agencies");
		request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}

	private void dashboard(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			long totalUsers = userService.countUser(true, false, "");

			long totalAgencies = agencyService.countAgencies("APPROVED", true, false, "");

			long pendingAgencies = agencyService.countAgencies("PENDING", null, false, "");

			long rejectedAgencies = agencyService.countAgencies("REJECTED", null, false, "");

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
		List<UserResponseDTO> deletedUsers = userService.getDeletedUsers(1000, 0);
		request.setAttribute("usersList", deletedUsers);
		request.setAttribute("listType", "Deleted Users");
		request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
	}

	private void manageUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String activeParam = request.getParameter("active");
		Boolean active = null;
		String keyword = request.getParameter("keyword");
		if ("true".equals(activeParam)) {
			active = true;
		} else if ("false".equals(activeParam)) {
			active = false;
		}
		List<UserResponseDTO> usersList = userService.getAll(active, false, keyword, 1000, 0);

		request.setAttribute("usersList", usersList);
		request.getRequestDispatcher("template/admin/manageUsers.jsp").forward(request, response);
	}

	private void userAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String actionType = request.getParameter("action");
		String userIdStr = request.getParameter("userId");
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
		List<AgencyResponseDTO> deletedAgencies = agencyService.getDeletedAgencies(1000, 0);
		request.setAttribute("agenciesList", deletedAgencies);
		request.setAttribute("listType", "Deleted Agencies");
		request.getRequestDispatcher("template/admin/manageAgencies.jsp").forward(request, response);
	}

	private void manageAgencies(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String status = request.getParameter("status");
		String active = request.getParameter("active");
		String keyword = request.getParameter("keyword");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		List<AgencyResponseDTO> agenciesList = agencyService.filterAgencies(status, active, startDate, endDate, keyword,
				100, 0);
		System.out.println(agenciesList.toString());
		agenciesList.removeIf(agency -> agency.isDelete());
		request.setAttribute("agenciesList", agenciesList);
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
