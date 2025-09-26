package com.travelmanagement.controller;

import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.service.impl.PackageServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/package")
public class PackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IPackageService packageService = new PackageServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String button = request.getParameter("button");
		try {

			switch (button) {
			case "packageList":
				listPackages(request, response);
				break;

			default:
				listPackages(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/user/userDashboard.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void listPackages(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String keyword = request.getParameter("keyword");
			String pageParam = request.getParameter("page");
			String pageSizeParam = request.getParameter("pageSize");
			int page = 1;
			if (pageParam != null && !pageParam.isEmpty()) {
				try {
					page = Integer.parseInt(pageParam);
					if (page < 1)
						page = 1;
				} catch (NumberFormatException e) {
					page = 1;
				}
			}

			int limit = 6;
			if (pageSizeParam != null && !pageSizeParam.isEmpty()) {
				try {
					limit = Integer.parseInt(pageSizeParam);
					if (limit <= 0)
						limit = 6;
				} catch (NumberFormatException e) {
					limit = 6;
				}
			}

			int offset = (page - 1) * limit;

			List<PackageResponseDTO> packages = packageService.searchPackages(null, null, null, keyword, null, null,
					null, null, true, limit, offset, false);

			int totalPackages = packageService.countPackages(null, null, null, keyword, null, null, null, null, true,
					false);

			int totalPages = (int) Math.ceil((double) totalPackages / limit);

			request.setAttribute("packages", packages);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("keyword", keyword);
			request.setAttribute("pageSize", limit);

			request.getRequestDispatcher("/template/user/packages.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Error fetching packages. Please try again.");
			request.getRequestDispatcher("/template/user/packages.jsp").forward(request, response);
		}
	}

}
