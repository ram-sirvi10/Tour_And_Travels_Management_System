package com.travelmanagement.scheduler;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
import com.travelmanagement.service.impl.BookingServiceImpl;
import com.travelmanagement.service.impl.PackageServiceImpl;
import com.travelmanagement.service.impl.PaymentServiceImpl;
import com.travelmanagement.service.impl.TravelerServiceImpl;

public class BookingScheduler {

	private static BookingScheduler instance;

	private final ScheduledExecutorService schedulerPool;

	private BookingScheduler() {
		this.schedulerPool = Executors.newScheduledThreadPool(10);

	}

	public static synchronized BookingScheduler getInstance() {
		if (instance == null) {
			instance = new BookingScheduler();
		}
		return instance;
	}

	public void scheduleAutoCancel(int bookingId) {

		schedulerPool.schedule(() -> {
			try {

				BookingServiceImpl bookingService = new BookingServiceImpl();
				PaymentServiceImpl paymentService = new PaymentServiceImpl();
				PackageServiceImpl packageService = new PackageServiceImpl();
				TravelerServiceImpl travelerService = new TravelerServiceImpl();
				BookingResponseDTO booking = bookingService.getBookingById(bookingId);

				if (booking == null || !"PENDING".equalsIgnoreCase(booking.getStatus())) {
					System.out.println("Booking " + bookingId + " already processed.");
					return;
				}

				PaymentResponseDTO payment = paymentService.getPaymentByBookingId(bookingId);

				boolean paymentSuccess = payment != null && "SUCCESSFUL".equalsIgnoreCase(payment.getStatus());

				if (paymentSuccess) {
					System.out.println("Booking " + bookingId + " already paid.");
					return;
				}

				bookingService.updateBookingStatus(bookingId, "CANCELLED");
				packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
				travelerService.updateTravelerStatus(null, bookingId, "CANCELLED");
				System.out.println(payment);
				if (payment == null) {
					PaymentRequestDTO failedPayment = new PaymentRequestDTO();
					failedPayment.setBookingId(bookingId);
					failedPayment.setAmount(booking.getNoOfTravellers()
							* packageService.getPackageById(booking.getPackageId()).getPrice());
					failedPayment.setStatus("FAILED");
					paymentService.addPayment(failedPayment);
					System.out.println(" payment entry  " + payment);
				}

				System.out.println("Booking " + bookingId + " auto-cancelled and seats restored.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 5, TimeUnit.MINUTES);
	}

	public void startAutoCompleteTask() {
		schedulerPool.scheduleAtFixedRate(() -> {
			try {
				System.out.println("[AutoCompleteTask] Running complete check...");
				BookingServiceImpl bookingService = new BookingServiceImpl();
				PaymentServiceImpl paymentService = new PaymentServiceImpl();
				PackageServiceImpl packageService = new PackageServiceImpl();
				TravelerServiceImpl travelerService = new TravelerServiceImpl();

				var confirmedBookings = bookingService.getAllBookings(null, null, null, null, "CONFIRMED", null, null,
						10000, 0);
				System.out.println("[AutoCompleteTask] Found confirm bookings: " + confirmedBookings.size());
				LocalDateTime now = LocalDateTime.now();

				for (BookingResponseDTO booking : confirmedBookings) {

					var pkg = packageService.getPackageById(booking.getPackageId());
					if (pkg == null || pkg.getDepartureDate() == null || pkg.getDuration() == null)
						continue;

					java.time.LocalDateTime departureEnd = pkg.getDepartureDate().plusDays(pkg.getDuration());
					System.out.println("[AutoCompleteTask] Checking booking id " + booking.getBookingId()
							+ " Complete date  " + pkg.getDepartureDate().plusDays(pkg.getDuration()));
					if (now.isAfter(departureEnd) || now.isEqual(departureEnd)) {
						bookingService.updateBookingStatus(booking.getBookingId(), "COMPLETE");
						travelerService.updateTravelerStatus(null, booking.getBookingId(), "COMPLETE");
						System.out.println(
								"Booking " + booking.getBookingId() + " marked as COMPLETE with travelers updated.");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.MINUTES); // check every 5 minutes
	}

	public void startAutoCancelTask() {
		schedulerPool.scheduleAtFixedRate(() -> {
			try {
				System.out.println("[AutoCancelTask] Running cancellation check...");

				BookingServiceImpl bookingService = new BookingServiceImpl();
				PaymentServiceImpl paymentService = new PaymentServiceImpl();
				PackageServiceImpl packageService = new PackageServiceImpl();
				TravelerServiceImpl travelerService = new TravelerServiceImpl();

				var pendingBookings = bookingService.getAllBookings(null, null, null, null, "PENDING", null, null,
						10000, 0);
				System.out.println("[AutoCancelTask] Found pending bookings: " + pendingBookings.size());

				for (BookingResponseDTO booking : pendingBookings) {
					System.out.println("[AutoCancelTask] Checking booking " + booking.getBookingId() + " created at "
							+ booking.getCreated_at());

					PaymentResponseDTO payment = paymentService.getPaymentByBookingId(booking.getBookingId());

					boolean paymentSuccess = payment != null && "SUCCESSFUL".equalsIgnoreCase(payment.getStatus());

					if (!paymentSuccess && booking.getCreated_at() != null
							&& booking.getCreated_at().plusMinutes(5).isBefore(java.time.LocalDateTime.now())) {

						System.out.println("[AutoCancelTask] Auto-cancelling booking " + booking.getBookingId());
						bookingService.updateBookingStatus(booking.getBookingId(), "CANCELLED");
						packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
						travelerService.updateTravelerStatus(null, booking.getBookingId(), "CANCELLED");

						if (payment == null) {
							PaymentRequestDTO failedPayment = new PaymentRequestDTO();
							failedPayment.setBookingId(booking.getBookingId());
							failedPayment.setAmount(booking.getNoOfTravellers()
									* packageService.getPackageById(booking.getPackageId()).getPrice());
							failedPayment.setStatus("FAILED");
							paymentService.addPayment(failedPayment);
							System.out.println("[AutoCancelTask] Payment fail entry -> " + failedPayment);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, 0, 1, TimeUnit.MINUTES);
	}

	public void shutdown() {
		schedulerPool.shutdownNow();
	}
}
