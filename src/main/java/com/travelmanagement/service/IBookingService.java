package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.model.Traveler;

public interface IBookingService {

	BookingResponseDTO getBookingById(int id) throws Exception;

	boolean updateBookingStatus(int bookingId, String status) throws Exception;

	boolean cancelBooking(int bookingId) throws Exception;

	List<BookingResponseDTO> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			 String startDate, String endDate, int limit, int offset) throws Exception;

	int getAllBookingsCount(Integer userId, Integer packageId, Integer noOfTravellers, String status, 
			String startDate, String endDate) throws Exception;

	int createBooking(BookingRequestDTO dto) throws Exception;

	void decrementTravelerCount(int bookingId) throws Exception;

}
