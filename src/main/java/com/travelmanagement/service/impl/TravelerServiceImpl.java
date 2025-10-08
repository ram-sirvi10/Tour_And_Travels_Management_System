package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.ITravelerDAO;
import com.travelmanagement.dao.impl.TravelerDAOImpl;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.model.Traveler;
import com.travelmanagement.service.ITravelerService;
import com.travelmanagement.util.Mapper;

public class TravelerServiceImpl implements ITravelerService {
	private ITravelerDAO travelerDAO = new TravelerDAOImpl();

	@Override
	public List<TravelerResponseDTO> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId,
			Integer packageId, Integer agencyId, String bookingStatus, String keyword, int limit, int offset)
			throws Exception {

		List<Traveler> travelers = travelerDAO.getAllTravelers(travelerId, bookingId, userId, packageId, agencyId,
				bookingStatus, keyword, limit, offset);

		List<TravelerResponseDTO> travelerDTOs = new ArrayList<>();
		if (travelers != null) {
			for (Traveler t : travelers) {

				travelerDTOs.add(Mapper.mapTravelerToTravelerResponseDTO(t));
			}
		}
		return travelerDTOs;
	}

	@Override
	public long getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, String bookingStatus, String keyword) throws Exception {
		return travelerDAO.getTravelerCount(travelerId, bookingId, userId, packageId, agencyId, bookingStatus, keyword);
	}

	public boolean isTravelerAlreadyBooked(String email, int packageId) {
		try {
			return travelerDAO.isTravelerAlreadyBooked(email, packageId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void updateTravelerStatus(Integer travelerId, Integer bookingId, String status) throws Exception {
		if (travelerId != null) {
			Traveler traveler = travelerDAO.getTravelerById(travelerId);

			if (traveler == null) {
				throw new BadRequestException("Traveler Not Found !");
			}
		}
		travelerDAO.updateTravelerStatus(travelerId, bookingId, status);
	}

	@Override
	public TravelerResponseDTO getTravelerById(Integer travelerId) {
		try {
			Traveler traveler = travelerDAO.getTravelerById(travelerId);
			if (traveler != null) {

				return Mapper.mapTravelerToTravelerResponseDTO(traveler);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
