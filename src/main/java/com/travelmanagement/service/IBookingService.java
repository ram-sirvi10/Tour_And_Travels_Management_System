package com.travelmanagement.service;

import com.travelmanagement.model.Booking;
import java.util.List;

public interface IBookingService {
	Booking createBooking(Booking booking) throws Exception;

	Booking getById(int id) throws Exception;

	List<Booking> getByUserId(int userId) throws Exception;

	List<Booking> getAllBookings() throws Exception;

	boolean updateStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;
}
