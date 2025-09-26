package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.IBookingDAO;
import com.travelmanagement.dao.ITravelerDAO;
import com.travelmanagement.dao.impl.BookingDAOImpl;
import com.travelmanagement.dao.impl.TravelerDAOImpl;
import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.model.Booking;
import com.travelmanagement.model.Traveler;
import com.travelmanagement.service.IBookingService;
import com.travelmanagement.util.Mapper;

public class BookingServiceImpl implements IBookingService {

	private IBookingDAO bookingDAO = new BookingDAOImpl();
	private ITravelerDAO travelerDAO = new TravelerDAOImpl();

	@Override
	public int createBooking(BookingRequestDTO dto) throws Exception {
		// Map DTO to Booking model
		Booking booking = Mapper.mapBookingRequestToBooking(dto);
		int bookingId = bookingDAO.createBooking(booking);

		if (bookingId > 0) {
			List<Traveler> travelerModels = new ArrayList<>();
			for (TravelerRequestDTO t : dto.getTravelers()) {
				Traveler traveler = Mapper.mapTravelerDtoToModel(t);
				traveler.setBookingId(bookingId);
				travelerModels.add(traveler);
			}

			boolean travelersCreated = travelerDAO.createTravelers(travelerModels);
			if (!travelersCreated) {

				bookingDAO.cancelBooking(bookingId);
				throw new BadRequestException("Failed to create travelers for booking");
			}
			return bookingId;
		}

		return 0;
	}

	@Override
	public BookingResponseDTO getBookingById(int bookingId) throws Exception {
		Booking booking = bookingDAO.getBookingById(bookingId);
		if (booking == null) {
			throw new BadRequestException("Booking not found with ID: " + bookingId);
		}
		return Mapper.mapBookingToBookingResponse(booking);
	}

	@Override
	public boolean cancelBooking(int bookingId) throws Exception {
		return bookingDAO.cancelBooking(bookingId);
	}

	

	@Override
	public boolean updateBookingStatus(int bookingId, String status) throws Exception {
		Booking booking = bookingDAO.getBookingById(bookingId);
		if (booking == null) {
			throw new BadRequestException("Booking not found with ID: " + bookingId);
		}
		booking.setStatus(status);
		return bookingDAO.updateBookingStatus(bookingId, status);
	}

	@Override
	public List<BookingResponseDTO> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers,
			String status, String keyword, String startDate, String endDate, int limit, int offset) throws Exception {

		List<Booking> bookings = bookingDAO.getAllBookings(userId, packageId, noOfTravellers, status, keyword,
				startDate, endDate, limit, offset);
		List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();

		for (Booking booking : bookings) {
			bookingResponseDTOs.add(Mapper.mapBookingToBookingResponse(booking));
		}

		return bookingResponseDTOs;
	}

	@Override
	public int getAllBookingsCount(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			String keyword, String startDate, String endDate) throws Exception {
		return bookingDAO.getAllBookingsCount(userId, packageId, noOfTravellers, status, keyword, startDate, endDate);
	}
}
