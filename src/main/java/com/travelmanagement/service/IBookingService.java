package com.travelmanagement.service;

import com.travelmanagement.model.Booking;
import java.util.List;

public interface IBookingService {

	Boolean createBooking(Booking booking) throws Exception;

	Booking getBookingById(int id) throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;

	List<Booking> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			String keyword, String startDate, String endDate, int limit, int offset) throws Exception;

	int getAllBookingsCount(Integer userId, Integer packageId, Integer noOfTravellers, String status, String keyword,
			String startDate, String endDate) throws Exception;

}
