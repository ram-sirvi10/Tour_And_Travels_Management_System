package com.travelmanagement.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.service.impl.AuthServiceImpl;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.PaymentServiceImpl;

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

	private void viewBookingForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String packageIdParam = request.getParameter("packageId");
		if (packageIdParam == null || packageIdParam.isEmpty()) {
			request.setAttribute("errorMessage", "Package ID is missing!");
			request.getRequestDispatcher("template/user/packages.jsp").forward(request, response);
			return;
		}

		int packageId;
		try {
			packageId = Integer.parseInt(packageIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Package ID!");
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
		Map<String, String> errors = authService.validateTravelers(travelersDTO);
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
		int createdBookingId = bookingService.createBooking(bookingDTO);

		if (createdBookingId > 0) {
			packageService.adjustSeats(bookingDTO.getPackageId(), -bookingDTO.getNumberOfTravelers());
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

		int bookingId;
		double amount;

		try {
			bookingId = Integer.parseInt(bookingIdParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Booking ID!");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		BookingResponseDTO bookingResponseDTO;
		try {
			bookingResponseDTO = bookingService.getBookingById(bookingId);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Invalid Booking!");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}

		try {
			amount = Double.parseDouble(amountParam);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid Amount!");
			request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
			return;
		}
		try {
			double amountToPay = packageService.getPackageById(bookingResponseDTO.getPackageId()).getPrice()
					* bookingResponseDTO.getNoOfTravellers();
			if (amountToPay != amount) {
				request.setAttribute("errorMessage", "Invalid Amount!");
				request.getRequestDispatcher("template/user/payment.jsp").forward(request, response);
				return;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
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

			BookingResponseDTO booking = bookingService.getBookingById(bookingId);
			if (booking != null) {
				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
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

}
