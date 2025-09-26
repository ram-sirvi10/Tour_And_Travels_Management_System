package com.travelmanagement.controller;

import java.io.IOException;
import java.util.List;

import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.service.impl.PackageServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/user")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
maxFileSize = 1024 * 1024 * 5, // 5 MB
maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("button");
		System.out.println("USER ACTION => " + action);
		try {
			switch (action) {

			case "dashboard": {
				dashboard(request, response);
				break;
			}
			case "updateProfile":
				updateProfile(request, response);
				break;
			case "changePassword":
				changePassword(request, response);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + action);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("template/user/userDashboard.jsp");
			return;
		}

	}

	private void changePassword(HttpServletRequest request, HttpServletResponse response) {
		AdminServlet adminServlet = new AdminServlet();
		try {
			adminServlet.changePassword(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateProfile(HttpServletRequest request, HttpServletResponse response) {
		AdminServlet adminServlet = new AdminServlet();
		try {
			adminServlet.updateProfile(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void dashboard(HttpServletRequest request, HttpServletResponse response) {
		PackageServiceImpl serviceImpl =  new PackageServiceImpl();
		try {
			List<PackageResponseDTO> packageResponseDTO = serviceImpl.searchPackages(null, null, null, null, null, null,null,null,true ,8, 0,false);
			request.setAttribute("packages", packageResponseDTO);
			request.getRequestDispatcher("template/user/userDashboard.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
