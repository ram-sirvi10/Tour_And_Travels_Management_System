package com.travelmanagement.dao.impl;

import java.util.List;

import com.travelmanagement.dao.IPaymentDAO;
import com.travelmanagement.model.Payment;

public class PaymentDAOImpl implements IPaymentDAO{

	@Override
	public int createPayment(Payment payment) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Payment getPaymentById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payment getPaymentByBookingId(int bookingId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Payment> getAllPayments() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updatePaymentStatus(int paymentId, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
