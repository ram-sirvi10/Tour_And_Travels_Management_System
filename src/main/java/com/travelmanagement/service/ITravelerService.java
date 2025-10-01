package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;

public interface ITravelerService {

	List<TravelerResponseDTO> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, String bookingStatus, String keyword, int limit, int offset) throws Exception;

	int getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId, Integer agencyId,
			String bookingStatus, String keyword) throws Exception;


	TravelerResponseDTO getTravelerById(int travelerId);

	void updateTravelerStatus(Integer travelerId, Integer bookingId, String status) throws Exception;

}
