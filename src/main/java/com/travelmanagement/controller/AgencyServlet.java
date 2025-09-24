package com.travelmanagement.controller;

import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/agency")
public class AgencyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PackageServiceImpl packageService = new PackageServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("button");
		System.out.println("USER ACTION => " + action);
		AgencyResponseDTO agency = (AgencyResponseDTO) request.getSession().getAttribute("agency");

		try {
			switch (action) {
			case "dashboard":
				dashboard(request, response, agency);
				break;

			case "addPackage":
				addPackage(request, response, agency);
				break;

			case "deletePackage":
				deletePackage(request, response, agency);
				break;

			case "togglePackage":
				togglePackageStatus(request, response, agency);
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Error: " + e.getMessage());
			try {
				dashboard(request, response, agency);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	// -------------------- Dashboard --------------------
	private void dashboard(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		List<PackageResponseDTO> packages = packageService.getAllPackages(agency.getAgencyId());
		request.setAttribute("packages", packages);
		request.getRequestDispatcher("template/agency/agencyDashboard.jsp").forward(request, response);
		return;
	}

	// -------------------- Add Package --------------------
	private void addPackage(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		PackageRegisterDTO dto = new PackageRegisterDTO();
		dto.setTitle(request.getParameter("title"));
		dto.setDescription(request.getParameter("description"));
		dto.setPrice(request.getParameter("price"));
		dto.setDuration(request.getParameter("duration"));
		dto.setLocation(request.getParameter("location"));
		dto.setImageurl(request.getParameter("imageUrl"));
		dto.setIsActive(request.getParameter("isActive"));
		dto.setAgencyId(String.valueOf(agency.getAgencyId()));

		// Validate package data
		if (!ValidationUtil.isValidName(dto.getTitle()) || !ValidationUtil.isValidPrice(dto.getPrice())
				|| !ValidationUtil.isValidName(dto.getLocation())
				|| !ValidationUtil.isValidName(dto.getDescription())) {
			request.setAttribute("error", "Invalid package data");
			dashboard(request, response, agency);
			return;
		}

		packageService.createPackage(dto);
		response.sendRedirect("agency?button=dashboard");
	}

	// -------------------- Delete Package --------------------
	private void deletePackage(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		int packageId = Integer.parseInt(request.getParameter("packageId"));
		packageService.deletePackage(packageId);
		response.sendRedirect("agency?button=dashboard");
	}

	// -------------------- Toggle Package Status --------------------
	private void togglePackageStatus(HttpServletRequest request, HttpServletResponse response, AgencyResponseDTO agency)
			throws Exception {
		int packageId = Integer.parseInt(request.getParameter("packageId"));
		packageService.togglePackageStatus(packageId);
		response.sendRedirect("agency?button=dashboard");
	}
}
