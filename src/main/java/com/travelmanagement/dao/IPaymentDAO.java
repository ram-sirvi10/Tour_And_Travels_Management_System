package com.travelmanagement.dao;

import com.travelmanagement.model.Payment;
import java.util.List;

public interface IPaymentDAO {

    boolean createPayment(Payment payment) throws Exception;

    Payment getPaymentById(Integer id) throws Exception;

    Payment getPaymentByBookingId(Integer bookingId) throws Exception;

    boolean updatePaymentStatus(Integer paymentId, String status) throws Exception;

	List<Payment> getPaymentHistory(Integer userId, Integer agencyId, Integer packageId, String status,
			String startDate, String endDate, int limit, int offset) throws Exception;

	long getPaymentHistoryCount(Integer userId, Integer agencyId, Integer packageId, String status, String startDate,
			String endDate) throws Exception;

	
}
