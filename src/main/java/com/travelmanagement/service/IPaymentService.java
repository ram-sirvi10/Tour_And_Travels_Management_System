package com.travelmanagement.service;

import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;

public interface IPaymentService {
    boolean addPayment(PaymentRequestDTO paymentDTO) throws Exception;
}
