package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.model.Traveler;

public interface IBookingService {

	BookingResponseDTO getBookingById(Integer id) throws Exception;

	boolean updateBookingStatus(Integer bookingId, String status) throws Exception;

	boolean cancelBooking(Integer bookingId) throws Exception;

	List<BookingResponseDTO> getAllBookings(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status,
			 String startDate, String endDate, int limit, int offset) throws Exception;

	long getAllBookingsCount(Integer agencyId,Integer userId, Integer packageId, Integer noOfTravellers, String status, 
			String startDate, String endDate) throws Exception;

	int createBooking(BookingRequestDTO dto) throws Exception;

	void decrementTravelerCount(Integer bookingId) throws Exception;

	boolean hasExistingBooking(Integer userId, Integer packageId) throws Exception;

}
