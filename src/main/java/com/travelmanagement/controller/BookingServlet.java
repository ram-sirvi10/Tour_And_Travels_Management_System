package com.travelmanagement.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.PaymentServiceImpl;
import com.travelmanagement.service.impl.TravelerServiceImpl;
import com.travelmanagement.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AuthServiceImpl authService = new AuthServiceImpl();
	private BookingServiceImpl bookingService = new BookingServiceImpl();
	private PackageServiceImpl packageService = new PackageServiceImpl();
	private PaymentServiceImpl paymentService = new PaymentServiceImpl();
	private TravelerServiceImpl travelerService = new TravelerServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String button = request.getParameter("button");

		try {
			switch (button) {

			case "createBooking":
				createBooking(request, response);
				break;

			case "paymentConfirm":
				processPayment(request, response, true);
				break;

			case "paymentReject":
				processPayment(request, response, false);
				break;
			case "viewBookingForm":
				viewBookingForm(request, response);
				break;
			case "bookingHistroy":
				showBookingHistory(request, response);
				break;
			case "viewTravelers":
				showTravelersList(request, response);
				break;
			case "paymentHistory":
				showPaymentHistory(request, response);
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + button);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Something went wrong: " + e.getMessage());
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
	}

	private void showPaymentHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

		int pageSize = 10;
		int currentPage = 1;

		String status = request.getParameter("status");
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
			request.setAttribute("errorMessage", "Start date cannot be after end date.");
			start = null;
			end = null;
		}
		try {
			if (request.getParameter("pageSize") != null) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
			}
			if (request.getParameter("page") != null) {
				currentPage = Integer.parseInt(request.getParameter("page"));
			}

			int offset = (currentPage - 1) * pageSize;

			List<PaymentResponseDTO> payments = paymentService.getPaymentHistory(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null, pageSize, offset);

			int totalRecords = paymentService.getPaymentHistoryCount(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null);
			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

			request.setAttribute("payments", payments);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);

			request.getRequestDispatcher("template/user/paymentHistory.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Unable to fetch payment history.");
			request.getRequestDispatcher("template/user/paymentHistory.jsp").forward(request, response);
		}
	}

	private void viewBookingForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String packageIdParam = request.getParameter("packageId");
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.setAttribute("errorMessage", "Package  is missing!");
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}

		int packageId;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Package !");
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}
		try {
			PackageResponseDTO packageResponseDTO = packageService.getPackageById(packageId);
			if (packageResponseDTO == null || !packageResponseDTO.getIsActive()) {
				request.setAttribute("errorMessage", "Package not found!");
				request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
				return;
			}
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Something went wrong: " + e.getMessage());
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}
	}

	private void createBooking(HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		UserResponseDTO sessionUser = (UserResponseDTO) session.getAttribute("user");
		BookingRequestDTO bookingDTO = new BookingRequestDTO();
		PackageResponseDTO packageResponseDTO = null;
//		PackageResponseDTO packageResponseDTO ;
		String packageIdParam = request.getParameter("packageId");
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.setAttribute("errorMessage", "Package is missing!");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int packageId;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Package !");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
		try {
			packageResponseDTO = packageService.getPackageById(packageId);
			if (packageResponseDTO == null || !packageResponseDTO.getIsActive()) {
				request.setAttribute("errorMessage", "Package not found!");
				request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
				return;
			}
//			request.setAttribute("package", packageResponseDTO);
//			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
//			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Something went wrong: " + e.getMessage());
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		/////////////////////////////////////////

		String userIdParam = request.getParameter("userId");
		if (userIdParam == null || userIdParam.isEmpty()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "User is missing!");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int userId;
		try {
			userId = Integer.parseInt(userIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "Invalid User !");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (sessionUser.getUserId() != userId) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "Something went wrong !");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		String noOfTravellersParam = request.getParameter("noOfTravellers");

		if (bookingService.hasExistingBooking(userId, packageId)) {
			request.setAttribute("errorMessage", "You have already booked this package.");
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (noOfTravellersParam == null || noOfTravellersParam.isEmpty()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "No of travellers is missing!");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int noOfTravellers;
		try {
			noOfTravellers = Integer.parseInt(noOfTravellersParam);
		} catch (NumberFormatException e) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "Invalid no of travellers !");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		bookingDTO.setUserId(userId);
		bookingDTO.setPackageId(packageId);
		bookingDTO.setNumberOfTravelers(noOfTravellers);

		packageResponseDTO = packageService.getPackageById(bookingDTO.getPackageId());
		if (packageResponseDTO == null) {
			request.setAttribute("errorMessage", "Package not found!");
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (bookingDTO.getNumberOfTravelers() > packageResponseDTO.getTotalSeats()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", "Cannot book more than available seats!");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		List<TravelerRequestDTO> travelersDTO = new ArrayList<>();
		for (int i = 1; i <= bookingDTO.getNumberOfTravelers(); i++) {
			TravelerRequestDTO traveler = new TravelerRequestDTO();
			traveler.setName(request.getParameter("travelerName" + i));
			traveler.setEmail(request.getParameter("travelerEmail" + i));
			traveler.setMobile(request.getParameter("travelerMobile" + i));
			traveler.setAge(request.getParameter("travelerAge" + i) != null
					&& !request.getParameter("travelerAge" + i).isEmpty()
							? Integer.parseInt(request.getParameter("travelerAge" + i))
							: 0);

			travelersDTO.add(traveler);
		}
		String isBookingSubmitParam = request.getParameter("isBookingSubmit");
		boolean isBookingSubmit = "true".equals(isBookingSubmitParam);

		if (!isBookingSubmit) {

			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		Map<String, String> errors = authService.validateTravelers(travelersDTO, bookingDTO.getPackageId());
		if (!errors.isEmpty()) {
			request.setAttribute("errors", errors);
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		bookingDTO.setTravelers(travelersDTO);

		if (bookingDTO.getNumberOfTravelers() > packageResponseDTO.getTotalSeats()) {
			request.setAttribute("errorMessage", "Not enough seats available!");
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
		System.out.println("Create booking ====");
		System.out.println("Total travller =  " + noOfTravellers);
		System.out.println("total available seats = " + packageResponseDTO.getTotalSeats());
		boolean seatsAdjusted = packageService.adjustSeatsOptimistic(bookingDTO.getPackageId(),
				bookingDTO.getNumberOfTravelers());
		if (!seatsAdjusted) {
			request.setAttribute("errorMessage", "Not enough seats available. Please try again.");
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
		int createdBookingId = bookingService.createBooking(bookingDTO);

		if (createdBookingId > 0) {
			packageService.adjustSeats(bookingDTO.getPackageId(), -bookingDTO.getNumberOfTravelers());
//			BookingScheduler scheduler = BookingScheduler.getInstance();
//		  scheduler.scheduleAutoCancel(createdBookingId);

			double totalAmount = packageResponseDTO.getPrice() * bookingDTO.getNumberOfTravelers();
			request.setAttribute("bookingId", createdBookingId);
			request.setAttribute("amount", totalAmount);
			session.setAttribute("bookingId", createdBookingId);
			session.setAttribute("amount", totalAmount);
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else {

			packageService.adjustSeats(bookingDTO.getPackageId(), bookingDTO.getNumberOfTravelers());
			request.setAttribute("errorMessage", "Booking failed. Please try again.");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
	}

	private void processPayment(HttpServletRequest request, HttpServletResponse response, boolean confirm)
			throws Exception {

		HttpSession session = request.getSession();
		String bookingIdParam = request.getParameter("bookingId");
		String amountParam = request.getParameter("amount");

		if (bookingIdParam == null || bookingIdParam.isEmpty() || amountParam == null || amountParam.isEmpty()) {
			request.setAttribute("errorMessage", "Booking ID or Amount is missing!");
			request.setAttribute("bookingId", session.getAttribute("bookingId"));
			request.setAttribute("amount", session.getAttribute("amount"));
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		int bookingId = Integer.parseInt(bookingIdParam);
		double amount = Double.parseDouble(amountParam);

		BookingResponseDTO bookingResponseDTO = bookingService.getBookingById(bookingId);

		if (bookingResponseDTO == null) {
			request.setAttribute("errorMessage", "Invalid Booking!");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		if (!confirm && !"PENDING".equals(bookingResponseDTO.getStatus())) {

			request.setAttribute("message", "Booking already cancelled.");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
		paymentDTO.setBookingId(bookingId);
		paymentDTO.setAmount(amount);
		paymentDTO.setStatus(confirm ? "SUCCESSFUL" : "FAILED");

		boolean success = paymentService.addPayment(paymentDTO);

		if (confirm && success) {
			request.setAttribute("message", "Payment successful!");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else if (!confirm && success) {
			if ("PENDING".equals(bookingResponseDTO.getStatus())) {

				packageService.adjustSeats(bookingResponseDTO.getPackageId(), bookingResponseDTO.getNoOfTravellers());
				bookingService.cancelBooking(bookingId);
			}
			request.setAttribute("message", "Payment rejected. Booking cancelled.");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else {
			request.setAttribute("message", "Payment processing failed. Please try again.");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}
	}

	private void showBookingHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

		int pageSize = 10;
		int currentPage = 1;
		String status = request.getParameter("status");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		try {

			if (request.getParameter("pageSize") != null) {
				int ps = Integer.parseInt(request.getParameter("pageSize"));
				if (ps > 0)
					pageSize = ps;
			}
			if (request.getParameter("page") != null) {
				int pg = Integer.parseInt(request.getParameter("page"));
				if (pg > 0)
					currentPage = pg;
			}

			if (startDate != null && startDate.isEmpty())
				startDate = null;
			if (endDate != null && endDate.isEmpty())
				endDate = null;

			int offset = (currentPage - 1) * pageSize;
			LocalDate start = null, end = null;
			try {
				if (startDate != null && !startDate.isEmpty())
					start = LocalDate.parse(startDate);
				if (endDate != null && !endDate.isEmpty())
					end = LocalDate.parse(endDate);
			} catch (Exception e) {

			}

			if (start != null && end != null && start.isAfter(end)) {
				request.setAttribute("errorMessage", "Start date cannot be after end date.");
				start = null;
				end = null;
			}

			List<BookingResponseDTO> bookings = bookingService.getAllBookings(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null, pageSize, offset);
			int totalRecords = bookingService.getAllBookingsCount(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null);

			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
			totalPages = (totalPages == 0) ? 1 : totalPages;

			for (BookingResponseDTO booking : bookings) {
				PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
				if (pkg != null) {
					booking.setPackageName(pkg.getTitle());
					booking.setPackageImage(pkg.getImageurl());
					booking.setDuration(pkg.getDuration());
					booking.setAmount(pkg.getPrice() * booking.getNoOfTravellers());
				}
			}

			request.setAttribute("bookings", bookings);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Unable to fetch booking history. Please check your filters.");
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
		}
	}

	private void showTravelersList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		String bookingIdParam = request.getParameter("bookingId");
		String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword").trim() : "";
		int pageSize = 10;
		int currentPage = 1;

		try {
			if (request.getParameter("pageSize") != null) {
				pageSize = Integer.parseInt(request.getParameter("pageSize"));
				if (!(pageSize == 10 || pageSize == 20 || pageSize == 30 || pageSize == 50)) {
					pageSize = 10;
				}
			}
		} catch (NumberFormatException e) {
			pageSize = 10;
		}

		try {
			if (request.getParameter("page") != null) {
				currentPage = Integer.parseInt(request.getParameter("page"));
				if (currentPage < 1)
					currentPage = 1;
			}
		} catch (NumberFormatException e) {
			currentPage = 1;
		}

		if (bookingIdParam == null || bookingIdParam.isEmpty()) {
			request.setAttribute("errorMessage", "Booking ID is required!");
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			return;
		}

		int bookingId;
		try {
			bookingId = Integer.parseInt(bookingIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Booking ID!");
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			return;
		}

		try {

			int totalRecords = travelerService.getTravelerCount(null, bookingId, user.getUserId(), null, null, null,
					null, null, keyword, null, null);

			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
			if (currentPage > totalPages && totalPages > 0)
				currentPage = totalPages;

			int offset = (currentPage - 1) * pageSize;

			List<TravelerResponseDTO> travelers = travelerService.getAllTravelers(null, bookingId, user.getUserId(),
					null, null, null, null, null, keyword, null, null, pageSize, offset);

			PackageResponseDTO pkg = packageService
					.getPackageById(bookingService.getBookingById(bookingId).getPackageId());
			String packageName = pkg != null ? pkg.getTitle() : "";

			request.setAttribute("travelers", travelers);
			request.setAttribute("packageName", packageName);
			request.setAttribute("keyword", keyword);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);

			request.getRequestDispatcher("template/user/TravelersList.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Unable to fetch travelers: " + e.getMessage());
			request.getRequestDispatcher("template/user/TravelersList.jsp").forward(request, response);
		}
	}

}
