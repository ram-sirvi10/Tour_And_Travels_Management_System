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
			List<PackageResponseDTO> packages = packageService.getAllPackages(0);
			request.setAttribute("packages", packages);
			request.getRequestDispatcher("/template/user/packages.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
