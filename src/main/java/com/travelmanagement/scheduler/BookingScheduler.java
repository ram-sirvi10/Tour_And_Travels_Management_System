//package com.travelmanagement.scheduler;
//
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
//import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
//import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
//import com.travelmanagement.service.impl.BookingServiceImpl;
//import com.travelmanagement.service.impl.PackageServiceImpl;
//import com.travelmanagement.service.impl.PaymentServiceImpl;
//
//public class BookingScheduler {
//
//    private static BookingScheduler instance;
//
//    private final ScheduledExecutorService bookingSchedulerPool;
//
//    private final BookingServiceImpl bookingService;
//    private final PaymentServiceImpl paymentService;
//    private final PackageServiceImpl packageService;
//
//    private BookingScheduler() {
//        this.bookingSchedulerPool = Executors.newScheduledThreadPool(50);
//        this.bookingService = new BookingServiceImpl();
//        this.paymentService = new PaymentServiceImpl();
//        this.packageService = new PackageServiceImpl();
//    }
//
//    public static synchronized BookingScheduler getInstance() {
//        if (instance == null) {
//            instance = new BookingScheduler();
//        }
//        return instance;
//    }
//
//    public void scheduleAutoCancel(int bookingId) {
//        bookingSchedulerPool.schedule(() -> {
//            try {
//                BookingResponseDTO booking = bookingService.getBookingById(bookingId);
//
//                if (booking == null || !"PENDING".equalsIgnoreCase(booking.getStatus())) {
//                    System.out.println("Booking " + bookingId + " already processed → thread exiting");
//                    return;
//                }
//
//                PaymentResponseDTO payment = paymentService.getPaymentByBookingId(bookingId);
//
//               
//                boolean paymentSuccessful = payment != null && payment.getStatus() != null
//                        && "SUCCESSFUL".equalsIgnoreCase(payment.getStatus().trim());
//
//                if (paymentSuccessful) {
//                    System.out.println("Booking " + bookingId + " payment SUCCESSFUL → thread exiting, no cancel");
//                    return; 
//                }
//
//               
//                cancelBooking(booking, payment);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }, 2, TimeUnit.MINUTES);
//    }
//
//    private void cancelBooking(BookingResponseDTO booking, PaymentResponseDTO payment) throws Exception {
//
//        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
//            System.out.println("Booking " + booking.getBookingId() + " is not PENDING → skipping cancel");
//            return;
//        }
//
//        bookingService.updateBookingStatus(booking.getBookingId(), "CANCELLED");
//        packageService.adjustSeats(booking.getPackageId(), booking.getNoOfTravellers());
//
//       
//        if (payment == null) {
//            PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
//            paymentDTO.setBookingId(booking.getBookingId());
//            paymentDTO.setAmount(booking.getAmount());
//            paymentDTO.setStatus("FAILED");
//            paymentService.addPayment(paymentDTO);
//        }
//
//        System.out.println("Booking " + booking.getBookingId() + " cancelled & seats restored");
//    }
//
//   
//    public void shutdown() {
//        bookingSchedulerPool.shutdownNow();
//    }
//}
