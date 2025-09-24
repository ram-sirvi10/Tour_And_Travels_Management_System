package com.travelmanagement.service.impl;

import java.util.List;

import com.travelmanagement.model.Booking;
import com.travelmanagement.service.IBookingService;

public class BookingServiceImpl implements IBookingService{

	@Override
	public Boolean createBooking(Booking booking) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Booking getBookingById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateBookingStatus(int bookingId, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cancelBooking(int bookingId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Booking> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			String keyword, String startDate, String endDate, int limit, int offset) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAllBookingsCount(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			String keyword, String startDate, String endDate) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}}
