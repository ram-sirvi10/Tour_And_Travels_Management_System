package com.travelmanagement.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
			case "viewProfile":
				request.getRequestDispatcher("template/user/profileManagement.jsp").forward(request, response);
				;
				return;
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
		PackageServiceImpl packageService = new PackageServiceImpl();
		BookingServiceImpl bookingService = new BookingServiceImpl();
		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

		try {
			List<PackageResponseDTO> packages = packageService.searchPackages(null, null, null, null, null, null, null,
					true, 8, 0, false);
			request.setAttribute("packages", packages);

			List<BookingResponseDTO> allBookings = bookingService.getAllBookings(user.getUserId(), null, null,
					"CONFIRMED", null, null, 100, 0);

			LocalDateTime now = LocalDateTime.now();
			List<BookingResponseDTO> upcomingBookings = new ArrayList<>();
			Map<Integer, BookingResponseDTO> bookingMap = new HashMap<>(); 

			for (BookingResponseDTO booking : allBookings) {
				PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
				if (pkg != null && pkg.getDepartureDate() != null && pkg.getDepartureDate().isAfter(now)) {
					booking.setPackageName(pkg.getTitle());
					booking.setPackageImage(pkg.getImageurl());
					booking.setDuration(pkg.getDuration());
					booking.setAmount(pkg.getPrice() * booking.getNoOfTravellers());
					booking.setDepartureDateAndTime(pkg.getDepartureDate());
					upcomingBookings.add(booking);
					bookingMap.put(booking.getPackageId(), booking); 
				}
			}

			if (upcomingBookings.size() > 5) {
				upcomingBookings = upcomingBookings.subList(0, 5);
			}

			request.setAttribute("bookings", upcomingBookings);
			request.setAttribute("bookingMap", bookingMap);

			request.getRequestDispatcher("template/user/userDashboard.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
