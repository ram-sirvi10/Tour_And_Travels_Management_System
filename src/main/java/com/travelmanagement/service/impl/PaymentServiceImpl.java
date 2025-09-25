package com.travelmanagement.service.impl;

import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.model.Payment;
import com.travelmanagement.service.IPaymentService;
import com.travelmanagement.util.Mapper;
import com.travelmanagement.dao.impl.PaymentDAOImpl;

public class PaymentServiceImpl implements IPaymentService {

    private PaymentDAOImpl paymentDAO = new PaymentDAOImpl();

    @Override
    public boolean addPayment(PaymentRequestDTO paymentDTO) throws Exception {
        Payment payment = Mapper.mapPaymentRequestDTOToPayment(paymentDTO);
        return paymentDAO.createPayment(payment);
    }
}
