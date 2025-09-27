package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.ITravelerDAO;
import com.travelmanagement.dao.impl.TravelerDAOImpl;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;
import com.travelmanagement.model.Traveler;
import com.travelmanagement.service.ITravelerService;

public class TravelerServiceImpl implements ITravelerService {
	private ITravelerDAO travelerDAO = new TravelerDAOImpl();

	@Override
	public List<TravelerResponseDTO> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId,
			Integer packageId, Integer agencyId, Integer paymentId, String bookingStatus, String paymentStatus,
			String keyword, String startDate, String endDate, int limit, int offset) throws Exception {

		List<Traveler> travelers = travelerDAO.getAllTravelers(travelerId, bookingId, userId, packageId, agencyId,
				paymentId, bookingStatus, paymentStatus, keyword, startDate, endDate, limit, offset);

		List<TravelerResponseDTO> travelerDTOs = new ArrayList<>();
		if (travelers != null) {
			for (Traveler t : travelers) {
				TravelerResponseDTO dto = new TravelerResponseDTO();
				dto.setId(t.getId());
				dto.setBookingId(t.getBookingId());
				dto.setName(t.getName());
				dto.setEmail(t.getEmail());
				dto.setMobile(t.getMobile());
				dto.setAge(t.getAge());
				travelerDTOs.add(dto);
			}
		}
		return travelerDTOs;
	}

	@Override
	public int getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, Integer paymentId, String bookingStatus, String paymentStatus, String keyword,
			String startDate, String endDate) throws Exception {
		return travelerDAO.getTravelerCount(travelerId, bookingId, userId, packageId, agencyId, paymentId,
				bookingStatus, paymentStatus, keyword, startDate, endDate);
	}
	
	public boolean isTravelerAlreadyBooked(String email, int packageId)  {
	    try {
			return travelerDAO.isTravelerAlreadyBooked(email, packageId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return false;
	}

}
