package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Booking;

public interface IBookingDAO {

	int createBooking(Booking booking) throws Exception;

	Booking getBookingById(int id) throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;

	List<Booking> getAllBookings(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status,
			 String startDate, String endDate, int limit, int offset) throws Exception;

	int getAllBookingsCount(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status, 
			String startDate, String endDate) throws Exception;



	void decrementTravelerCount(int bookingId) throws Exception;

	int getTotalBookingsByPackage(int packageId) throws Exception;

	double getRevenueByPackage(int packageId) throws Exception;
	
}



