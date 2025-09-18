package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Booking;

public interface IBookingDAO {
	Boolean createBooking(Booking booking) throws Exception;

	Booking getBookingById(int id) throws Exception;
	
	Boolean addTourist(int bookingId, String touristName) throws Exception;

	List<Booking> getBookingsByUserId(int userId) throws Exception;

	List<Booking> getAllBookings() throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;
	
}
