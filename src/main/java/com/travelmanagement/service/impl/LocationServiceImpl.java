package com.travelmanagement.service.impl;

import java.util.List;

import com.travelmanagement.dao.ILocationDAO;
import com.travelmanagement.dao.impl.LocationDAOImpl;
import com.travelmanagement.service.ILocationService;

public class LocationServiceImpl implements ILocationService {

	private ILocationDAO locationDAO = new LocationDAOImpl();

	@Override
	public List<String> getLocations(String type, String value) throws Exception {
		return locationDAO.getLocations(type, value);
	}
}
