package com.travelmanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.PackageSchedule;
import com.travelmanagement.service.IBookingService;
import com.travelmanagement.service.IPackageService;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/package")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
		maxFileSize = 1024 * 1024 * 5, // 5 MB
		maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class PackageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IPackageService packageService = new PackageServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String button = request.getParameter("button");
		try {
			if (button == null)
				button = "packageList";
			switch (button) {
			case Constants.ACTION_PACKAGES_LIST:
				listPackages(request, response);
				break;
			default:
				listPackages(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			listPackages(request, response);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void listPackages(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			IBookingService bookingService = new BookingServiceImpl();
			UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
			String keyword = trimOrNull(request.getParameter("keyword"));
			String title = trimOrNull(request.getParameter("title"));
			String location = trimOrNull(request.getParameter("location"));
			String startDate = trimOrNull(request.getParameter("dateFrom"));
			String endDate = trimOrNull(request.getParameter("dateTo"));
			String departureDate = trimOrNull(request.getParameter("departureDate"));
			Integer agencyId = parseIntSafe(request.getParameter("agencyId"));
			Integer totalSeats = parseIntSafe(request.getParameter("totalSeats"));
			Boolean isActive = true;

			int page = parseIntSafe(request.getParameter("page"), 1);
			int limit = parseIntSafe(request.getParameter("pageSize"), 10);
			if (limit <= 0)
				limit = 10;
			if (page < 1)
				page = 1;
			int offset = (page - 1) * limit;
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
			System.out.println(start);
			List<PackageResponseDTO> packages = packageService.searchPackages(title, agencyId, location, keyword,
					start != null ? start.toString() : null, end != null ? end.toString() : null, totalSeats, isActive,
					limit, offset, false);

			int totalPackages = packageService.countPackages(title, agencyId, location, keyword,
					start != null ? start.toString() : null, end != null ? end.toString() : null, totalSeats, isActive,
					false);

			int totalPages = (int) Math.ceil((double) totalPackages / limit);

			List<BookingResponseDTO> allBookings = bookingService.getAllBookings(null,user.getUserId(), null, null,
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
			for (PackageResponseDTO pkg : packages) {
				List<PackageScheduleResponseDTO> schedule = packageService.getScheduleByPackage(pkg.getPackageId());
				pkg.setPackageSchedule(schedule);
			}

			request.setAttribute("bookingMap", bookingMap);
			request.setAttribute("packages", packages);
			request.setAttribute("currentPage", page);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", limit);
			request.setAttribute("keyword", keyword);
			request.setAttribute("title", title);
			request.setAttribute("location", location);
			request.setAttribute("dateFrom", startDate);
			request.setAttribute("dateTo", endDate);
			request.setAttribute("departureDate", departureDate);

			request.getRequestDispatcher("/template/user/packages.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", Constants.ERROR_FETCH_PACKAGES);
			request.getRequestDispatcher("/template/user/packages.jsp").forward(request, response);
		}
	}

	private String trimOrNull(String param) {
		return (param != null && !param.trim().isEmpty()) ? param.trim() : null;
	}

	private Integer parseIntSafe(String param) {
		return parseIntSafe(param, null);
	}

	private Integer parseIntSafe(String param, Integer defaultValue) {
		try {
			if (param != null && !param.trim().isEmpty()) {
				return Integer.parseInt(param.trim());
			}
		} catch (NumberFormatException e) {
			return defaultValue;
		}
		return defaultValue;
	}

}
