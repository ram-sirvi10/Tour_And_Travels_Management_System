package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Payment;

public interface IPaymentDAO {

    boolean createPayment(Payment payment) throws Exception;

    Payment getPaymentById(Integer id) throws Exception;

    Payment getPaymentByBookingId(Integer bookingId) throws Exception;

    boolean updatePaymentStatus(Integer paymentId, String status) throws Exception;

	List<Payment> getPaymentHistory(Integer userId, Integer agencyId, Integer packageId, String status,
			String startDate, String endDate, int limit, int offset) throws Exception;

	long getPaymentHistoryCount(Integer userId, Integer agencyId, Integer packageId, String status, String startDate,
			String endDate) throws Exception;

	double getRevenue(Integer agencyId, Integer packageId, String startDate, String endDate, Integer month,
			Integer year) throws Exception;

	double getRefund(Integer agencyId, Integer packageId, String startDate, String endDate, Integer month, Integer year)
			throws Exception;

	
}
