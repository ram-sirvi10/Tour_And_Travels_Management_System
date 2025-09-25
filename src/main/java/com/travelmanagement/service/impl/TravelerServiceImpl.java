package com.travelmanagement.service.impl;

import java.util.List;

import com.travelmanagement.dao.ITravelerDAO;
import com.travelmanagement.dao.impl.TravelerDAOImpl;
import com.travelmanagement.model.Traveler;
import com.travelmanagement.service.ITravelerService;

public class TravelerServiceImpl implements ITravelerService {
	private ITravelerDAO travelerDAO = new TravelerDAOImpl();

	@Override
	public List<Traveler> getTravelersByBookingId(int bookingId) throws Exception {
		return travelerDAO.getAllTravelers(null, bookingId, null, null, null, null, null, null, null, null, null, 1000,
				0);
	}
}
