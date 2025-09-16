package com.travelmanagement.dao.impl;

import java.util.List;

import com.travelmanagement.dao.IBookingDAO;
import com.travelmanagement.model.Booking;

public class BookingDAOImpl implements IBookingDAO{

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
	public Boolean addTourist(int bookingId, String touristName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Booking> getBookingsByUserId(int userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Booking> getAllBookings() throws Exception {
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

}
