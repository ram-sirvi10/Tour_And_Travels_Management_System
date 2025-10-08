package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Booking;

public interface IBookingDAO {

	int createBooking(Booking booking) throws Exception;

	Booking getBookingById(Integer id) throws Exception;

	boolean updateBookingStatus(Integer bookingId, String status) throws Exception;

	boolean cancelBooking(Integer bookingId) throws Exception;

	List<Booking> getAllBookings(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status,
			 String startDate, String endDate, int limit, int offset) throws Exception;

	long getAllBookingsCount(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status, 
			String startDate, String endDate) throws Exception;

	void decrementTravelerCount(Integer bookingId) throws Exception;

	
}



