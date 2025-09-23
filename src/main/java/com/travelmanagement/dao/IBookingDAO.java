package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Booking;

public interface IBookingDAO {
	
	Boolean createBooking(Booking booking) throws Exception;

	Booking getBookingById(int id) throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	List<Booking> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers ,String status, String keyword, String startDate,
			String endDate) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;

}
