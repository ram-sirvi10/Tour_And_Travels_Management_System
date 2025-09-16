package com.travelmanagement.dao;

import com.travelmanagement.model.Booking;
import java.util.List;

public interface IBookingDAO {
	Boolean createBooking(Booking booking) throws Exception;

	Booking getBookingById(int id) throws Exception;
	
	Boolean addTourist(int bookingId, String touristName) throws Exception;

	List<Booking> getBookingsByUserId(int userId) throws Exception;

	List<Booking> getAllBookings() throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;
}
