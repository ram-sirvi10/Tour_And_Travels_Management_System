package com.travelmanagement.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.razorpay.Order;
import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.scheduler.BookingScheduler;
import com.travelmanagement.service.IUserService;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.PaymentServiceImpl;
import com.travelmanagement.service.impl.TravelerServiceImpl;
import com.travelmanagement.service.impl.UserServiceImpl;
import com.travelmanagement.util.BookingInvoiceUtil;
import com.travelmanagement.util.Constants;
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
	private IUserService userService = new UserServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String button = request.getParameter("button");

		try {
			switch (button) {

			case Constants.ACTION_CREATE_BOOKING:
				createBooking(request, response);
				break;
			case Constants.ACTION_AUTO_CANCEL_BOOKING:
				autoCancelBooking(request, response);
				break;
			case Constants.ACTION_VERIFY_PAYMENT:
				verifyPayment(request, response);
				break;
			case Constants.ACTION_PAYMENT_CONFIRM:
				processPayment(request, response, true);
				break;
			case Constants.ACTION_PAYMENT_REJECT:
				processPayment(request, response, false);
				break;
			case Constants.ACTION_CANCEL_BOOKING:
				cancelBooking(request, response);
				break;
			case Constants.ACTION_CANCEL_TRAVELER:
				cancelTraveler(request, response);
				break;
			case Constants.ACTION_VIEW_BOOKING_FORM:
				viewBookingForm(request, response);
				break;
			case Constants.ACTION_BOOKING_HISTORY:
				showBookingHistory(request, response);
				break;
			case Constants.ACTION_VIEW_TRAVELERS:
				showTravelersList(request, response);
				break;
			case Constants.ACTION_PAYMENT_HISTORY:
				showPaymentHistory(request, response);
				break;
			case Constants.ACTION_DOWNLOAD_INVOICE:
				downloadInvoice(request, response);
				break;

			default:
				throw new IllegalArgumentException("Unexpected value: " + button);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
	}

	private void downloadInvoice(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO sessionUser = (UserResponseDTO) session.getAttribute("user");

		if (sessionUser == null) {

			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		String bookingIdParam = request.getParameter("bookingId");
		if (bookingIdParam == null || bookingIdParam.isEmpty()) {
			request.getSession().setAttribute("errorMessage", Constants.ERROR_BOOKING_ID_MISSING);
			response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
			return;
		}

		int bookingId;
		try {
			bookingId = Integer.parseInt(bookingIdParam);
		} catch (NumberFormatException e) {
			request.getSession().setAttribute("errorMessage", Constants.ERROR_INVALID_BOOKING_ID);
			response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
			return;
		}

		try {
			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
			if (booking == null) {
				request.getSession().setAttribute("errorMessage", Constants.ERROR_BOOKING_NOT_FOUND);
				response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
				return;
			}

			if (booking.getUserId() != sessionUser.getUserId()) {
				request.getSession().setAttribute("errorMessage", Constants.ERROR_NOT_AUTHORIZED_DOWNLOAD);
				response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
				return;
			}
			booking.setTravelers(travelerService.getAllTravelers(null, bookingId, booking.getUserId(), null, null, null,
					null, 200, 0));
			PackageResponseDTO packageResponseDTO = packageService.getPackageById(booking.getPackageId());
			booking.setAmount(packageResponseDTO.getPrice() * booking.getNoOfTravellers());
			booking.setPackageName(packageResponseDTO.getTitle());
			booking.setPackageImage(packageResponseDTO.getImageurl());
			booking.setDepartureDateAndTime(packageResponseDTO.getDepartureDate());
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=Booking_Invoice_" + bookingId + ".pdf");

			BookingInvoiceUtil.generateInvoice(booking, response.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errorMessage", Constants.ERROR_UNABLE_GENERATE_INVOICE);
			response.sendRedirect(request.getContextPath() + "/booking?button=bookingHistroy");
		}
	}

	private void cancelTraveler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int travelerId = Integer.parseInt(request.getParameter("travelerId"));
			int bookingId = Integer.parseInt(request.getParameter("bookingId"));
			TravelerResponseDTO traveler = travelerService.getTravelerById(travelerId);
			if (traveler == null || traveler.getBookingId() != bookingId) {
				request.setAttribute("errorMessage", Constants.ERROR_INVALID_TRAVELER);

				showTravelersList(request, response);
				return;
			}

			if ("CANCELLED".equalsIgnoreCase(traveler.getStatus())) {
				request.setAttribute("errorMessage", Constants.ERROR_TRAVELER_ALREADY_CANCELLED);

				showTravelersList(request, response);
				return;
			}

			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
			if (booking == null) {
//				request.setAttribute("errorMessage", "Booking not found!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", Constants.ERROR_BOOKING_NOT_FOUND);
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
				request.getSession().setAttribute("errorMessage", Constants.ERROR_UNAUTHORIZED_CANCEL);
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);
				return;
			}

			PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
			if (pkg == null) {
//				request.setAttribute("errorMessage", "Package not found!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
				response.sendRedirect(
						request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

				return;
			}

			LocalDateTime now = LocalDateTime.now();
			if (pkg.getLastBookingDate().isBefore(now)) {
//				request.setAttribute("errorMessage", "Cannot cancel traveler for past booking!");
//				showTravelersList(request, response);

				request.getSession().setAttribute("errorMessage", Constants.ERROR_CANNOT_CANCEL_PAST_BOOKING);
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
					Constants.SUCCESS_TRAVELER_CANCELLED + String.format("%.2f", refundAmount));

			response.sendRedirect(request.getContextPath() + "/booking?button=viewTravelers&bookingId=" + bookingId);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
			showTravelersList(request, response);
		}
	}

//	private void cancelBooking(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		try {
//			int bookingId = Integer.parseInt(request.getParameter("bookingId"));
//
//			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
//
//			if (booking == null) {
////				request.setAttribute("errorMessage", "Booking not found!");
////				showBookingHistory(request, response);
////				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
//
//				request.getSession().setAttribute("errorMessage", Constants.ERROR_BOOKING_NOT_FOUND);
//				response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
//				return;
//			}
//			PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
//			if (pkg == null) {
////				request.setAttribute("errorMessage", "Unavailable Package cannot delete booking !");
////				showBookingHistory(request, response);
////				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
//				request.getSession().setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
//				response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
//				return;
//			}
//
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime lastBookingDate = pkg.getLastBookingDate();
//
//			if (lastBookingDate.isBefore(now)) {
////				request.setAttribute("errorMessage", "Cannot cancel past bookings!");
////				showBookingHistory(request, response);
////				request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
//				request.getSession().setAttribute("errorMessage", Constants.ERROR_CANNOT_CANCEL_PAST_BOOKINGS);
//				response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
//
//				return;
//			}
//
//			long daysDiff = Duration
//					.between(now.toLocalDate().atStartOfDay(), lastBookingDate.toLocalDate().atStartOfDay()).toDays();
//			double totalAmount = pkg.getPrice() * booking.getNoOfTravellers();
//			double amountAfterGST = totalAmount / 1.18;
//
//			System.out.println("Cancel Booking ======= >>>>>>");
//			System.out.println("Last Booking Date of package -> " + lastBookingDate);
//			System.out.println("Remaning Days -> " + daysDiff);
//			System.out.println("Total Amount -> " + totalAmount);
//			System.out.println("Amount After GST -> " + amountAfterGST);
//			double refundPercent = 0;
//			if (daysDiff >= 7) {
//				refundPercent = 100;
//			} else if (daysDiff >= 3) {
//				refundPercent = 50;
//			} else if (daysDiff >= 1) {
//				refundPercent = 25;
//			} else {
//				refundPercent = 0;
//			}
//
//			double refundAmount = 0.0;
//			if (pkg.getPrice() > 0) {
//				double taxable = pkg.getPrice() / 1.18;
//
//				refundAmount = (taxable * booking.getNoOfTravellers()) * (refundPercent / 100.0);
//			}
//
//			System.out.println("Refundable Amount -> " + refundAmount);
//			bookingService.updateBookingStatus(bookingId, "CANCELLED");
//			travelerService.updateTravelerStatus(null, bookingId, "CANCELLED");
//			if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
//				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
//			}
//
//			PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
//			paymentDTO.setAmount(refundAmount);
//			paymentDTO.setBookingId(bookingId);
//			paymentDTO.setStatus("REFUNDED");
//			paymentService.addPayment(paymentDTO);
//
////			request.setAttribute("successMessage",
////					"Your booking is canceled! Refund: " + String.format("%.2f", refundAmount));
////			showBookingHistory(request, response);
////			request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
//
//			request.getSession().setAttribute("successMessage",
//					Constants.SUCCESS_BOOKING_CANCELLED + String.format("%.2f", refundAmount));
//			response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
//			return;
//
//		} catch (Exception e) {
//			e.printStackTrace();
////			request.setAttribute("errorMessage", "Something went wrong while canceling booking.");
////			request.getRequestDispatcher("/bookingHistory.jsp").forward(request, response);
////			showBookingHistory(request, response);
//			request.getSession().setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
//			response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
//			return;
//		}
//	}

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
			request.setAttribute("errorMessage", Constants.ERROR_START_AFTER_END_DATE);
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

			long totalRecords = paymentService.getPaymentHistoryCount(user.getUserId(), null, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null);
			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

			request.setAttribute("payments", paymentList);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);

			request.getRequestDispatcher("template/user/paymentHistory.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", Constants.ERROR_FETCH_PAYMENT_HISTORY);
			request.getRequestDispatcher("template/user/paymentHistory.jsp").forward(request, response);
		}
	}

	private void viewBookingForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String packageIdParam = request.getParameter("packageId");
		HttpSession session = request.getSession();
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}

		int packageId;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}
		try {
			PackageResponseDTO packageResponseDTO = packageService.getPackageById(packageId);
			if (packageResponseDTO == null || !packageResponseDTO.getIsActive()) {
				request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
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
			request.setAttribute("errorMessage", e.getMessage());
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
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int packageId;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}
		try {
			packageResponseDTO = packageService.getPackageById(packageId);
			if (packageResponseDTO == null || !packageResponseDTO.getIsActive()) {
				request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
				request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
				return;
			}
//			request.setAttribute("package", packageResponseDTO);
//			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
//			return;
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		/////////////////////////////////////////

		String userIdParam = request.getParameter("userId");
		if (userIdParam == null || userIdParam.isEmpty()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_INVALID_USER);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int userId;
		try {
			userId = Integer.parseInt(userIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_INVALID_USER);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (sessionUser.getUserId() != userId) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		String noOfTravellersParam = request.getParameter("noOfTravellers");

		if (bookingService.hasExistingBooking(userId, packageId)) {
			request.setAttribute("errorMessage", Constants.ERROR_ALREADY_BOOKED);
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (noOfTravellersParam == null || noOfTravellersParam.isEmpty()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_NO_OF_TRAVELLERS_MISSING);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		int noOfTravellers;
		try {
			noOfTravellers = Integer.parseInt(noOfTravellersParam);
		} catch (NumberFormatException e) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_INVALID_NO_OF_TRAVELLERS);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		PackageResponseDTO pkg = packageService.getPackageById(packageId);
		if (pkg == null || !pkg.getIsActive()) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (noOfTravellers > pkg.getTotalSeats()) {
			request.setAttribute("errorMessage", Constants.ERROR_NOT_ENOUGH_SEATS);
			request.setAttribute("package", pkg);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		boolean seatsAdjusted = false;
		int retries = 3;
		while (retries-- > 0) {
			int rowsAffected = packageService.updateSeatsOptimistic(packageId, noOfTravellers, pkg.getVersion());
			if (rowsAffected > 0) {
				seatsAdjusted = true;
				break;
			} else {
				pkg = packageService.getPackageById(packageId);
				if (noOfTravellers > pkg.getTotalSeats()) {
					request.setAttribute("errorMessage", Constants.ERROR_NOT_ENOUGH_SEATS);
					request.setAttribute("package", pkg);
					request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
					return;
				}
			}
		}
		if (!seatsAdjusted) {
			request.setAttribute("errorMessage", Constants.ERROR_CONCURRENT_BOOKING);
			request.setAttribute("package", pkg);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		bookingDTO.setUserId(userId);
		bookingDTO.setPackageId(packageId);
		bookingDTO.setNumberOfTravelers(noOfTravellers);

		packageResponseDTO = packageService.getPackageById(bookingDTO.getPackageId());
		if (packageResponseDTO == null) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			request.setAttribute("package", packageResponseDTO);
			request.getRequestDispatcher("template/user/booking.jsp").forward(request, response);
			return;
		}

		if (bookingDTO.getNumberOfTravelers() > packageResponseDTO.getTotalSeats()) {
			request.setAttribute("package", packageResponseDTO);
			request.setAttribute("errorMessage", Constants.ERROR_NOT_ENOUGH_SEATS);
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
//			packageService.adjustSeats(bookingDTO.getPackageId(), -bookingDTO.getNumberOfTravelers());
			System.out.println("number of traveler in create booking == " + bookingDTO.getNumberOfTravelers());
			BookingScheduler scheduler = BookingScheduler.getInstance();
			scheduler.scheduleAutoCancel(createdBookingId);

			double totalAmount = packageResponseDTO.getPrice() * bookingDTO.getNumberOfTravelers();

////			 Razorpay Order Create
			Order order = PaymentGatewayUtil.createOrder(totalAmount, "INR", "for booking " + createdBookingId);
			request.setAttribute("razorpayOrderId", order.get("id"));
			request.setAttribute("razorpayKey", "rzp_test_RNQiHnsfjn3up2");

			request.setAttribute("bookingId", createdBookingId);
			request.setAttribute("amount", totalAmount);
			session.setAttribute("bookingId", createdBookingId);
			session.setAttribute("amount", totalAmount);
// //========================= for payment gateway ========
			request.getRequestDispatcher("template/user/paymentGate.jsp").forward(request, response);
////===================for normal  payment===========
//			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else {

			packageService.adjustSeats(bookingDTO.getPackageId(), bookingDTO.getNumberOfTravelers());
			request.setAttribute("errorMessage", Constants.ERROR_BOOKING_FAILED);
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
			request.setAttribute("errorMessage", Constants.ERROR_BOOKING_ID_AMOUNT_MISSING);
			request.setAttribute("bookingId", session.getAttribute("bookingId"));
			request.setAttribute("amount", session.getAttribute("amount"));
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		int bookingId = Integer.parseInt(bookingIdParam);
		double amount = Double.parseDouble(amountParam);
		BookingResponseDTO bookingResponseDTO = bookingService.getBookingById(bookingId);

		if (bookingResponseDTO == null) {
			request.setAttribute("errorMessage", Constants.ERROR_INVALID_BOOKING);
			showBookingHistory(request, response);
			return;
		}
		PackageResponseDTO packageResponseDTO = packageService.getPackageById(bookingResponseDTO.getPackageId());

		if (packageResponseDTO == null) {
			request.setAttribute("errorMessage", Constants.ERROR_PACKAGE_NOT_FOUND);
			showBookingHistory(request, response);
			return;
		}

		double expectedAmount = packageResponseDTO.getPrice() * bookingResponseDTO.getNoOfTravellers();

		if (Math.abs(amount - expectedAmount) > 0.01) {
			request.setAttribute("errorMessage", Constants.ERROR_PAYMENT_PROCESSING_FAILED);
			showBookingHistory(request, response);
			return;
		}
		if (!confirm && !"PENDING".equals(bookingResponseDTO.getStatus())) {
			request.setAttribute("errorMessage", Constants.ERROR_BOOKING_ALREADY_CANCELLED);
			showBookingHistory(request, response);
			return;
		}

		PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
		paymentDTO.setBookingId(bookingId);
		paymentDTO.setAmount(amount);
		paymentDTO.setStatus(confirm ? "SUCCESSFUL" : "FAILED");
		packageResponseDTO = packageService.getPackageById(bookingResponseDTO.getPackageId());
		boolean success = paymentService.addPayment(paymentDTO);

		if (confirm && success) {
			bookingService.updateBookingStatus(bookingId, "CONFIRMED");

			List<TravelerResponseDTO> travelers = travelerService.getAllTravelers(null, bookingId, null, null, null,
					null, null, 100, 0);
			bookingResponseDTO.setAmount(packageResponseDTO.getPrice() * bookingResponseDTO.getNoOfTravellers());
			bookingResponseDTO.setPackageName(packageResponseDTO.getTitle());
			bookingResponseDTO.setPackageImage(packageResponseDTO.getImageurl());
			bookingResponseDTO.setTravelers(travelers);
			bookingResponseDTO.setDepartureDateAndTime(packageResponseDTO.getDepartureDate());

			request.setAttribute("confirmedBooking", bookingResponseDTO);
			request.setAttribute("message", Constants.SUCCESS_PAYMENT_CONFIRMED);
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else if (!confirm && success) {
			if ("PENDING".equals(bookingResponseDTO.getStatus())) {

				packageService.adjustSeats(bookingResponseDTO.getPackageId(), bookingResponseDTO.getNoOfTravellers());
				bookingService.cancelBooking(bookingId);
			}
			request.setAttribute("message", Constants.SUCCESS_PAYMENT_REJECTED);
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		} else {
			request.setAttribute("message", Constants.ERROR_PAYMENT_PROCESSING_FAILED);
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}
	}

	private void showBookingHistory(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
		AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");

		int pageSize = 10;
		int currentPage = 1;
		String status = request.getParameter("status");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		String packageIdParam = request.getParameter("packageId");
		Integer packageId = null;
		if (packageIdParam != null && !packageIdParam.isEmpty()) {
			try {
				packageId = Integer.parseInt(packageIdParam);
			} catch (NumberFormatException e) {
				packageId = null;
			}
		}

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
				request.setAttribute("errorMessage", Constants.ERROR_START_AFTER_END_DATE);
				start = null;
				end = null;
			}

			Integer agencyId = (agency != null) ? agency.getAgencyId() : null;
			Integer userId = (user != null) ? user.getUserId() : null;

			List<BookingResponseDTO> bookings = bookingService.getAllBookings(agencyId, userId, packageId, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null, pageSize, offset);

			long totalRecords = bookingService.getAllBookingsCount(agencyId, userId, packageId, null, status,
					start != null ? start.toString() : null, end != null ? end.toString() : null);

			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
			totalPages = (totalPages == 0) ? 1 : totalPages;
			System.out.println("Booking Histroy In Booking Servlet ");
			for (BookingResponseDTO booking : bookings) {
				System.out.println("Booking ID: " + booking.getBookingId() + ", User ID: " + booking.getUserId()
						+ ", Package ID: " + booking.getPackageId());
				PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
				if (pkg != null) {
					booking.setPackageName(pkg.getTitle());
					booking.setPackageImage(pkg.getImageurl());
					booking.setDuration(pkg.getDuration());
					booking.setAmount(pkg.getPrice() * booking.getNoOfTravellers());
					booking.setDepartureDateAndTime(pkg.getDepartureDate());
					booking.setLastBookingDate(pkg.getLastBookingDate());
				}

				UserResponseDTO bookedUser = userService.getById(booking.getUserId());
				if (bookedUser != null) {
					booking.setUserName(bookedUser.getUserName());
					booking.setUserEmail(bookedUser.getUserEmail());
				}
			}
			if (agency != null) {
				List<PackageResponseDTO> packages = packageService.searchPackages(null, agency.getAgencyId(), null,
						null, null, null, null, null, 2000, 0, true, null);
				request.setAttribute("packages", packages);
				System.err.println("Packages in show Booking histroy for agency filter ");
				System.out.println(packages);
				packages.toString();

				long totalConfirmedBookings = bookingService.getAllBookingsCount(agencyId, userId, packageId, null,
						"CONFIRMED", start != null ? start.toString() : null, end != null ? end.toString() : null);
				long totalCancelledBookings = bookingService.getAllBookingsCount(agencyId, userId, packageId, null,
						"CANCELLED", start != null ? start.toString() : null, end != null ? end.toString() : null);

				long totalConfirmedTravellers = travelerService.getTravelerCount(null, null, null, packageId, agencyId,
						"CONFIRMED", null);

				long totalCancelledTravellers = travelerService.getTravelerCount(null, null, null, packageId, agencyId,
						"CANCELLED", null);

				request.setAttribute("totalConfirmedBookings", totalConfirmedBookings);
				request.setAttribute("totalCancelledBookings", totalCancelledBookings);

				request.setAttribute("totalConfirmedTravellers", totalConfirmedTravellers);
				request.setAttribute("totalCancelledTravellers", totalCancelledTravellers);

				request.setAttribute("selectedPackageId", packageId);
			}

			request.setAttribute("bookings", bookings);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);

			if (user != null) {
				request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			} else if (agency != null) {
				request.getRequestDispatcher("template/agency/manageBooking.jsp").forward(request, response);
			}
			return;

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", Constants.ERROR_FETCH_BOOKING_HISTORY);
			if (user != null) {
				request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
			} else if (agency != null) {
				request.getRequestDispatcher("template/agency/manageBooking.jsp").forward(request, response);
			}
			return;
		}
	}

	private void showTravelersList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserResponseDTO user = (UserResponseDTO) session.getAttribute("user");
		AgencyResponseDTO agency = (AgencyResponseDTO) session.getAttribute("agency");
		if (user == null && agency == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		Integer bookingId = null;
		Integer agencyId = null;
		Integer userId = null;
		Integer packageId = null;
		String bookingStatus = null;
		String keyword = null;
		String bookingIdParam = request.getParameter("bookingId");
		String packageIdParam = request.getParameter("packageId");
		if (user != null) {
			userId = user.getUserId();
		}
		if (agency != null) {
			agencyId = agency.getAgencyId();
		}

		keyword = request.getParameter("keyword") != null ? request.getParameter("keyword").trim() : null;
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

//		if (bookingIdParam == null || bookingIdParam.isEmpty()) {
//			request.setAttribute("errorMessage", Constants.ERROR_BOOKING_ID_MISSING);
//			request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
//			return;
//		}

		try {
			bookingId = Integer.parseInt(bookingIdParam);
			BookingResponseDTO bookingResponseDTO = bookingService.getBookingById(bookingId);
			if (user != null) {
				if (bookingResponseDTO.getUserId() != userId) {
					request.setAttribute("errorMessage", Constants.ERROR_INVALID_BOOKING_ID);
					request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
					return;
				}
			}
		} catch (Exception e) {
			if (user != null) {
				request.setAttribute("errorMessage", Constants.ERROR_INVALID_BOOKING_ID);
				request.getRequestDispatcher("template/user/bookingHistory.jsp").forward(request, response);
				return;
			}
			bookingId = null;
		}

		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (Exception e) {
			packageId = null;
		}

		try {

			long totalRecords = travelerService.getTravelerCount(null, bookingId, userId, packageId, agencyId,
					bookingStatus, keyword);

			int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
			if (currentPage > totalPages && totalPages > 0)
				currentPage = totalPages;

			int offset = (currentPage - 1) * pageSize;
			List<TravelerResponseDTO> travelers = travelerService.getAllTravelers(null, bookingId, userId, packageId,
					agencyId, bookingStatus, keyword, pageSize, offset);

			PackageResponseDTO pkg = packageService
					.getPackageById(bookingService.getBookingById(bookingId).getPackageId());

			request.setAttribute("package", pkg);
			request.setAttribute("travelers", travelers);
			request.setAttribute("keyword", keyword);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("totalPages", totalPages);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("bookingId", bookingId);
			if (agency == null) {
				request.getRequestDispatcher("template/user/TravelersList.jsp").forward(request, response);
				return;
			} else {
				request.getRequestDispatcher("template/agency/travelerList.jsp").forward(request, response);
				return;
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", e.getMessage());
			if (user != null) {
				request.getRequestDispatcher("template/user/TravelersList.jsp").forward(request, response);
				return;
			} else {
				request.getRequestDispatcher("template/agency/travelersList.jsp").forward(request, response);
				return;
			}
		}
	}

	private void verifyPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			String paymentId = request.getParameter("razorpay_payment_id");
			String orderId = request.getParameter("razorpay_order_id");
			String signature = request.getParameter("razorpay_signature");

			System.out.println(" Verifying Razorpay payment ");

			if (paymentId == null || orderId == null || signature == null) {
				System.err.println(" Missing Razorpay parameters in callback");
				response.getWriter().print(Constants.ERROR_PAYMENT_PROCESSING_FAILED);
				return;
			}

			Map<String, String> params = Map.of("razorpay_order_id", orderId, "razorpay_payment_id", paymentId,
					"razorpay_signature", signature);

			boolean isValid = PaymentGatewayUtil.verifyPaymentSignature(params);
			if (!isValid) {
				System.err.println("razorpay signature verification failed");
				response.getWriter().print(Constants.ERROR_PAYMENT_VERIFICATION_FAILED);
				return;
			}

			HttpSession session = request.getSession(false);
			if (session == null) {
				System.err.println(" Payment session expired.");
				response.getWriter().print(Constants.ERROR_PAYMENT_SESSION_EXPIRED);
				return;
			}

			Integer bookingId = (Integer) session.getAttribute("bookingId");
			Double amount = (Double) session.getAttribute("amount");

			if (bookingId == null || amount == null) {
				System.err.println(" Booking or amount missing from session.");
				response.getWriter().print(Constants.ERROR_PAYMENT_SESSION_EXPIRED);
				return;
			}

			
			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
			if (booking == null) {
				System.err.println(" Invalid booking ID: " + bookingId);
				response.getWriter().print(Constants.ERROR_INVALID_BOOKING);
				return;
			}

			PackageResponseDTO packageInfo = packageService.getPackageById(booking.getPackageId());
			if (packageInfo == null) {
				System.err.println(" Package not found for booking: " + bookingId);
				response.getWriter().print(Constants.ERROR_PACKAGE_NOT_FOUND);
				return;
			}

			double expectedAmount = packageInfo.getPrice() * booking.getNoOfTravellers();

		
			if (Math.abs(amount - expectedAmount) > 0.01) {
				System.err.println(" Payment amount mismatch! Expected: " + expectedAmount + ", Got: " + amount);
				response.getWriter().print(Constants.ERROR_PAYMENT_PROCESSING_FAILED);
				return;
			}

		
			if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
				System.err.println(" Booking already cancelled. Refunding payment...");

				try {
					PaymentGatewayUtil.refundPayment(paymentId, amount);
				} catch (Exception refundEx) {
					refundEx.printStackTrace();
				}

				PaymentRequestDTO refundPayment = new PaymentRequestDTO();
				refundPayment.setBookingId(bookingId);
				refundPayment.setAmount(amount);
				refundPayment.setStatus("REFUNDED");
				refundPayment.setRazorpayPaymentId(paymentId);
				paymentService.addPayment(refundPayment);

				response.getWriter().print(Constants.ERROR_BOOKING_ALREADY_CANCELLED);
				return;
			}

			
			PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
			paymentDTO.setBookingId(bookingId);
			paymentDTO.setAmount(amount);
			paymentDTO.setStatus("SUCCESSFUL");
			paymentDTO.setRazorpayPaymentId(paymentId);
			paymentService.addPayment(paymentDTO);

			bookingService.updateBookingStatus(bookingId, "CONFIRMED");

			System.out.println(" Payment verified successfully for Booking ID: " + bookingId);
			response.getWriter().print(Constants.SUCCESS_PAYMENT_CONFIRMED);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(" Exception during payment verification: " + e.getMessage());
			response.getWriter().print(Constants.ERROR_PAYMENT_VERIFICATION_FAILED);
		}
	}

	private void autoCancelBooking(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		HttpSession session = request.getSession();
		UserResponseDTO sessionUser = (UserResponseDTO) session.getAttribute("user");

		Integer bookingId = (Integer) session.getAttribute("bookingId");
		if (bookingId == null) {
			response.getWriter().write("No booking to cancel.");
			return;
		}
		System.err.println("AutoCancel Booking -> ");
		BookingResponseDTO booking = bookingService.getBookingById(bookingId);
		if (booking != null && "PENDING".equalsIgnoreCase(booking.getStatus())
				&& booking.getUserId() == sessionUser.getUserId()) {

			packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
			bookingService.updateBookingStatus(bookingId, "CANCELLED");
			travelerService.updateTravelerStatus(null, bookingId, "CANCELLED");
			PaymentRequestDTO failedPayment = new PaymentRequestDTO();
			failedPayment.setBookingId(bookingId);
			failedPayment.setAmount(
					booking.getNoOfTravellers() * packageService.getPackageById(booking.getPackageId()).getPrice());
			failedPayment.setStatus("FAILED");
			paymentService.addPayment(failedPayment);
			response.getWriter().write("Booking auto-cancelled successfully.");
		} else {
			response.getWriter().write("Booking cannot be cancelled.");
		}
	}

	private void cancelBooking(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			int bookingId = Integer.parseInt(request.getParameter("bookingId"));
			BookingResponseDTO booking = bookingService.getBookingById(bookingId);

			if (booking == null) {
				request.getSession().setAttribute("errorMessage", Constants.ERROR_BOOKING_NOT_FOUND);
				response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
				return;
			}

			PackageResponseDTO pkg = packageService.getPackageById(booking.getPackageId());
			if (pkg == null) {
				request.getSession().setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
				response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
				return;
			}

			double totalAmount = pkg.getPrice() * booking.getNoOfTravellers();
			double refundAmount = 0.0;

			// Refund calculation logic (based on days left)
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime lastBookingDate = pkg.getLastBookingDate();
			long daysDiff = Duration
					.between(now.toLocalDate().atStartOfDay(), lastBookingDate.toLocalDate().atStartOfDay()).toDays();
			double refundPercent = (daysDiff >= 7) ? 100 : (daysDiff >= 3) ? 50 : (daysDiff >= 1) ? 25 : 0;

			double amountAfterGST = totalAmount / 1.18;
			refundAmount = (amountAfterGST * refundPercent) / 100;

			bookingService.updateBookingStatus(bookingId, "CANCELLED");
			travelerService.updateTravelerStatus(null, bookingId, "CANCELLED");

			if ("PENDING".equalsIgnoreCase(booking.getStatus())) {

				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());

				PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
				paymentDTO.setBookingId(bookingId);
				paymentDTO.setAmount(totalAmount);
				paymentDTO.setStatus("FAILED");
				paymentService.addPayment(paymentDTO);

				request.getSession().setAttribute("successMessage",
						"Booking cancelled. No payment made, seats restored.");
			} else if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {

				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());

				PaymentResponseDTO paymentRecord = paymentService.getPaymentByBookingId(bookingId);

				if (paymentRecord != null && "SUCCESSFUL".equalsIgnoreCase(paymentRecord.getStatus())) {
					String razorpayPaymentId = paymentRecord.getRazorpayPaymentId();
					try {
						PaymentGatewayUtil.refundPayment(razorpayPaymentId, refundAmount);
					} catch (Exception e) {
						e.printStackTrace();
					}
					PaymentRequestDTO refundPayment = new PaymentRequestDTO();
					refundPayment.setBookingId(bookingId);
					refundPayment.setAmount(refundAmount);
					refundPayment.setStatus("REFUNDED");
					refundPayment.setRazorpayPaymentId(razorpayPaymentId);
					paymentService.addPayment(refundPayment);
				}

				request.getSession().setAttribute("successMessage",
						Constants.SUCCESS_BOOKING_CANCELLED + " Refund: " + String.format("%.2f", refundAmount));
			}

			response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
			return;

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errorMessage", Constants.ERROR_SOMETHING_WENT_WRONG);
			response.sendRedirect(request.getContextPath() + "/booking?button=viewBookings");
		}

	}

}
