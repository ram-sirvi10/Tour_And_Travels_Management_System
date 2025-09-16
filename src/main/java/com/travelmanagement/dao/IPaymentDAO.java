package com.travelmanagement.dao;

import com.travelmanagement.model.Payment;
import java.util.List;

public interface IPaymentDAO {
	int createPayment(Payment payment) throws Exception;

	Payment getPaymentById(int id) throws Exception;

	Payment getPaymentByBookingId(int bookingId) throws Exception;

	List<Payment> getAllPayments() throws Exception;

	boolean updatePaymentStatus(int paymentId, String status) throws Exception;
}
