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
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.service.impl.AgencyServiceImpl;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class LandingPageServlet
 */
@WebServlet("/")
public class LandingPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IPackageService packageService = new PackageServiceImpl();
	private UserServiceImpl userService = new UserServiceImpl();
	private AgencyServiceImpl agencyService = new AgencyServiceImpl();
	private BookingServiceImpl bookingService = new BookingServiceImpl();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LandingPageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			HttpSession session = request.getSession();
			UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
			long totalUsers = userService.countUser(true, false, "");
			long totalAgencies = agencyService.countAgencies("APPROVED", null, false, "", null, null);
			List<PackageResponseDTO> packages = packageService.searchPackages(null, null, null, null, null, null, null,
					true, 10, 0, false);
			List<BookingResponseDTO> allBookings = null;
			if (user != null) {
				allBookings = bookingService.getAllBookings(user.getUserId(), null, null, "CONFIRMED", null, null, 100,
						0);

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
				request.setAttribute("bookingMap", bookingMap);
			}

			request.setAttribute("packages", packages);
			request.setAttribute("totalUsers", totalUsers);
			request.setAttribute("totalAgencies", totalAgencies);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
