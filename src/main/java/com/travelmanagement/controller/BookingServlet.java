package com.travelmanagement.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.travelmanagement.util.PaymentGatewayUtil;

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
//			case "verifyPayment":
//				verifyPayment(request, response);
//				break;
			case "cancelBooking":
				cancelBooking(request, response);
				break;
			case "cancelTraveler":
				cancelTraveler(request, response);
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

	private void cancelTraveler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int travelerId = Integer.parseInt(request.getParameter("travelerId"));
			int bookingId = Integer.parseInt(request.getParameter("bookingId"));
			TravelerResponseDTO traveler = travelerService.getTravelerById(travelerId);
			if (traveler == null || traveler.getBookingId() != bookingId) {
				request.setAttribute("errorMessage", "Invalid traveler or booking mismatch!");
				showTravelersList(request, response);
				return;
			}

			if ("CANCELLED".equalsIgnoreCase(traveler.getStatus())) {
				request.setAttribute("errorMessage", "Traveler already cancelled!");
				showTravelersList(request, response);
				return;
			}

			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
			if (booking == null) {
//				request.setAttribute("errorMessage", "Booking not found!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", "Booking not found!");
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

				return;
			}

			HttpSession session = request.getSession();
			UserResponseDTO userResponseDTO = (UserResponseDTO) session.getAttribute("user");
			int loggedInUserId = userResponseDTO.getUserId();
			if (booking.getUserId() != loggedInUserId) {
//				request.setAttribute("errorMessage", "Unauthorized cancellation attempt!");
//				showTravelersList(request, response);
//				return;
				request.getSession().setAttribute("errorMessage", "Unauthorized cancellation attempt!");
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);
				return;
			}

			PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
			if (pkg == null) {
//				request.setAttribute("errorMessage", "Package not found!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", "Package not found!");
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

				return;
			}

			LocalDateTime now = LocalDateTime.now();
			if (pkg.getLastBookingDate().isBefore(now)) {
//				request.setAttribute("errorMessage", "Cannot cancel traveler for past booking!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", "Cannot cancel traveler for past booking!");
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

				return;
			}

			long daysDiff = Duration
					.between(now.toLocalDate().atStartOfDay(), pkg.getLastBookingDate().toLocalDate().atStartOfDay())
					.toDays();

			double travelerAmount = pkg.getPrice();
			double amountAfterGST = travelerAmount / 1.18;
			double refundPercent = (daysDiff >= 7) ? 100 : (daysDiff >= 3) ? 50 : (daysDiff >= 1) ? 25 : 0;

			double refundAmount = (amountAfterGST * refundPercent) / 100;

			travelerService.updateTravelerStatus(travelerId, null, "CANCELLED");
			bookingService.decrementTravelerCount(bookingId);
			packageService.adjustSeats(pkg.getPackageId(), 1);
			PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
			paymentDTO.setAmount(refundAmount);
			paymentDTO.setBookingId(bookingId);
			paymentDTO.setStatus("REFUNDED");
			paymentService.addPayment(paymentDTO);

//			request.setAttribute("successMessage",
//					"Traveler cancelled successfully! Refund: " + String.format("%.2f", refundAmount));
//			showTravelersList(request, response);

			request.getSession().setAttribute("successMessage",
					"Traveler cancelled successfully! Refund: " + String.format("%.2f", refundAmount));
			response.sendRedirect(request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Something went wrong while cancelling traveler.");
			showTravelersList(request, response);
		}
	}

	private void cancelBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			int bookingId = Integer.parseInt(request.getParameter("bookingId"));

			BookingResponseDTO booking = bookingService.getBookingById(bookingId);

			if (booking == null) {
//				request.setAttribute("errorMessage", "Booking not found!");
//				showBookingHistory(request, response);
//				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);

				request.getSession().setAttribute("errorMessage", "Booking not found!");
				response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
				return;
			}
			PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
			if (pkg == null) {
//				request.setAttribute("errorMessage", "Unavailable Package cannot delete booking !");
//				showBookingHistory(request, response);
//				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
				request.getSession().setAttribute("errorMessage", "Unavailable Package cannot delete booking !");
				response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
				return;
			}

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime lastBookingDate = pkg.getLastBookingDate();

			if (lastBookingDate.isBefore(now)) {
//				request.setAttribute("errorMessage", "Cannot cancel past bookings!");
//				showBookingHistory(request, response);
//				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
				request.getSession().setAttribute("errorMessage", "Cannot cancel past bookings!");
				response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");

				return;
			}

			long daysDiff = Duration
					.between(now.toLocalDate().atStartOfDay(), lastBookingDate.toLocalDate().atStartOfDay()).toDays();
			double totalAmount = pkg.getPrice() * booking.getNoOfTravellers();
			double amountAfterGST = totalAmount / 1.18;
			double refundPercent = 0;
			System.out.println("Cancel Booking ======= >>>>>>");
			System.out.println("Last Booking Date of package -> " + lastBookingDate);
			System.out.println("Remaning Days -> " + daysDiff);
			System.out.println("Total Amount -> " + totalAmount);
			System.out.println("Amount After GST -> " + amountAfterGST);

			if (daysDiff >= 7) {
				refundPercent = 100;
			} else if (daysDiff >= 3) {
				refundPercent = 50;
			} else if (daysDiff >= 1) {
				refundPercent = 25;
			} else {
				refundPercent = 0;
			}
			double refundAmount = 0.0;
			if (pkg.getPrice() > 0) {
				double taxable = pkg.getPrice() / 1.18;
				refundAmount = taxable * booking.getNoOfTravellers();
			}
			System.out.println("Refundable Amount -> " + refundAmount);
			bookingService.updateBookingStatus(bookingId, "CANCELLED");
			travelerService.updateTravelerStatus(null, bookingId, "CANCELLED");
			if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
			}

			PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
			paymentDTO.setAmount(refundAmount);
			paymentDTO.setBookingId(bookingId);
			paymentDTO.setStatus("REFUNDED");
			paymentService.addPayment(paymentDTO);

//			request.setAttribute("successMessage",
//					"Your booking is canceled! Refund: " + String.format("%.2f", refundAmount));
//			showBookingHistory(request, response);
//			request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);

			request.getSession().setAttribute("successMessage",
					"Your booking is canceled! Refund: " + String.format("%.2f", refundAmount));
			response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
			return;

		} catch (Exception e) {
			e.printStackTrace();
//			request.setAttribute("errorMessage", "Something went wrong while canceling booking.");
//			request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
//			showBookingHistory(request, response);
			request.getSession().setAttribute("errorMessage", "Something went wrong while canceling booking.");
			response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
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

			List<PaymentResponseDTO> paymentList = paymentService.getPaymentHistory(user.getUserId(), null, null,
					status, start != null ? start.toString() : null, end != null ? end.toString() : null, pageSize,
					offset);
			for (PaymentResponseDTO payment : paymentList) {

				BookingResponseDTO booking = bookingService.getBookingById(payment.getBookingId());
				if (booking != null) {
					PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
					if (pkg != null) {
						payment.setPackageName(pkg.getTitle());
					}
				}

			}

			int totalRecords = paymentService.getPaymentHistoryCount(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null);
			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

			request.setAttribute("payments", paymentList);
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
		HttpSession session = request.getSession();
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
//			request.setAttribute("package", packageResponseDTO);
//			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			session.setAttribute("package", packageResponseDTO);
			response.sendRedirect("template/user/booking.jsp");
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

		PackageResponseDTO pkg = packageService.getPackageById(packageId);
		if (pkg == null || !pkg.getIsActive()) {
			request.setAttribute("errorMessage", "Package not found or inactive.");
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (noOfTravellers > pkg.getTotalSeats()) {
			request.setAttribute("errorMessage", "Not enough seats available!");
			request.setAttribute("package", pkg);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

//		boolean seatsAdjusted = false;
//		int retries = 3;
//		while (retries-- > 0) {
//			int rowsAffected = packageService.updateSeatsOptimistic(packageId, noOfTravellers, pkg.getVersion());
//			if (rowsAffected > 0) {
//				seatsAdjusted = true;
//				break;
//			} else {
//
//				pkg = packageService.getPackageById(packageId);
//				if (noOfTravellers > pkg.getTotalSeats()) {
//					request.setAttribute("errorMessage", "Not enough seats available. Try again.");
//					request.setAttribute("package", pkg);
//					request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
//					return;
//				}
//			}
//		}
//
//		if (!seatsAdjusted) {
//			request.setAttribute("errorMessage", "Booking failed due to concurrent bookings. Please try again.");
//			request.setAttribute("package", pkg);
//			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
//			return;
//		}

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

			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
//			session.setAttribute("errors", errors);
//			session.setAttribute("package", packageResponseDTO);
//			response.sendRedirect("template/user/booking.jsp");
			return;
		}

		bookingDTO.setTravelers(travelersDTO);
		int createdBookingId = bookingService.createBooking(bookingDTO);

		if (createdBookingId > 0) {
			packageService.adjustSeats(bookingDTO.getPackageId(), -bookingDTO.getNumberOfTravelers());
//			BookingScheduler scheduler = BookingScheduler.getInstance();
//		  scheduler.scheduleAutoCancel(createdBookingId);

			double totalAmount = packageResponseDTO.getPrice() * bookingDTO.getNumberOfTravelers();

////			 Razorpay Order Create
//			Order order = PaymentGatewayUtil.createOrder(totalAmount, "INR", "for booking " + createdBookingId);
//			request.setAttribute("razorpayOrderId", order.get("id"));
//			request.setAttribute("razorpayKey", "rzp_test_RNQiHnsfjn3up2");

			request.setAttribute("bookingId", createdBookingId);
			request.setAttribute("amount", totalAmount);
			session.setAttribute("bookingId", createdBookingId);
			session.setAttribute("amount", totalAmount);

//			request.getRequestDispatcher("template/user/paymentGate.jsp").forward(request, response);

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
			showBookingHistory(request, response);
			return;
		}

		if (!confirm && !"PENDING".equals(bookingResponseDTO.getStatus())) {
			request.setAttribute("errorMessage", "Booking already cancelled.");
			showBookingHistory(request, response);
			return;
		}

		PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
		paymentDTO.setBookingId(bookingId);
		paymentDTO.setAmount(amount);
		paymentDTO.setStatus(confirm ? "SUCCESSFUL" : "FAILED");

		boolean success = paymentService.addPayment(paymentDTO);

		if (confirm && success) {
			bookingService.updateBookingStatus(bookingId, "CONFIRMED");
			request.setAttribute("message", "Payment successful! Booking Confirmed");
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
					booking.setDepartureDateAndTime(pkg.getDepartureDate());
					booking.setLastBookingDate(pkg.getLastBookingDate());
				}
			}

			request.setAttribute("bookings", bookings);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Unable to fetch booking history. Please retry.");
			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			return;
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
					keyword);

			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
			if (currentPage > totalPages && totalPages > 0)
				currentPage = totalPages;

			int offset = (currentPage - 1) * pageSize;

			List<TravelerResponseDTO> travelers = travelerService.getAllTravelers(null, bookingId, user.getUserId(),
					null, null, null, keyword, pageSize, offset);

			PackageResponseDTO pkg = packageService
					.getPackageById(bookingService.getBookingById(bookingId).getPackageId());

			request.setAttribute("package", pkg);
			request.setAttribute("travelers", travelers);
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

//	private void verifyPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		try {
//			String body = request.getReader().lines().reduce("", (acc, line) -> acc + line);
//			JSONObject json = new JSONObject(body);
//			Map<String, String> params = new HashMap<>();
//			params.put("razorpay_order_id", json.getString("razorpay_order_id"));
//			params.put("razorpay_payment_id", json.getString("razorpay_payment_id"));
//			params.put("razorpay_signature", json.getString("razorpay_signature"));
//
//			boolean isValid = PaymentGatewayUtil.verifyPaymentSignature(params);
//
//			int bookingId = (int) request.getSession().getAttribute("bookingId");
//			double amount = (double) request.getSession().getAttribute("amount");
//
//			if (isValid) {
//				PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
//				paymentDTO.setBookingId(bookingId);
//				paymentDTO.setAmount(amount);
//				paymentDTO.setStatus("SUCCESSFUL");
//				paymentService.addPayment(paymentDTO);
//
//				bookingService.updateBookingStatus(bookingId, "CONFIRMED");
//
//				response.getWriter().print("Payment Successful & Verified!");
//			} else {
//				response.getWriter().print("Payment Failed!");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.getWriter().print("Payment verification failed: " + e.getMessage());
//		}
//	}

}
