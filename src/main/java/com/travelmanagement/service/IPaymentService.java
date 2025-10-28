package com.travelmanagement.service;

import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;

public interface IPaymentService {
    boolean addPayment(PaymentRequestDTO paymentDTO) throws Exception;
	long getPaymentHistoryCount(Integer userId, Integer agencyId, Integer packageId, String status, String startDate,
			String endDate) throws Exception;

	double getTotalRevenue(
			Integer agencyId, Integer packageId, String startDate, String endDate, Integer month,
			Integer year) throws Exception;
}
