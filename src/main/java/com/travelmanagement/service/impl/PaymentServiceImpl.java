package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.impl.PaymentDAOImpl;
import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
import com.travelmanagement.model.Payment;
import com.travelmanagement.service.IPaymentService;
import com.travelmanagement.util.Mapper;

public class PaymentServiceImpl implements IPaymentService {

	private PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

	@Override
	public boolean addPayment(PaymentRequestDTO paymentDTO) throws Exception {
		Payment payment = Mapper.mapPaymentRequestDTOToPayment(paymentDTO);
		return paymentDAO.createPayment(payment);
	}

	public PaymentResponseDTO getPaymentByBookingId(int bookingId) throws Exception {
		Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
		PaymentResponseDTO responseDTO = new PaymentResponseDTO();
		if (payment != null) {
			responseDTO = Mapper.mapPaymentToPaymentResponse(payment);
		}
		return responseDTO;
	}

	public void updatePaymentStatus(int paymentId, String string) throws Exception {

		paymentDAO.updatePaymentStatus(paymentId, string);

	}

	public List<PaymentResponseDTO> getPaymentHistory(Integer userId, Integer agencyId, Integer packageId,
			String status, String startDate, String endDate, int limit, int offset) throws Exception {

		List<PaymentResponseDTO> paymentDTOs = new ArrayList<>();

		List<Payment> payments = paymentDAO.getPaymentHistory(userId, agencyId, packageId, status, startDate, endDate,
				limit, offset);

		for (Payment p : payments) {
			
			paymentDTOs.add(Mapper.mapPaymentToPaymentResponse(p));
		}

		return paymentDTOs;
	}

}
