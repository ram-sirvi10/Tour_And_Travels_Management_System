package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;

public interface ITravelerService {

	List<TravelerResponseDTO> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, Integer paymentId, String bookingStatus, String paymentStatus, String keyword,
			String startDate, String endDate, int limit, int offset) throws Exception;

	int getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId, Integer agencyId,
			Integer paymentId, String bookingStatus, String paymentStatus, String keyword, String startDate,
			String endDate) throws Exception;

}
