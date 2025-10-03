package com.travelmanagement.service;

import java.util.List;

public interface ILocationService {
	List<String> getLocations(String type, String value) throws Exception;
}
