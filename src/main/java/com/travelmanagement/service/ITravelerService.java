package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.model.Traveler;

public interface ITravelerService {

	List<Traveler> getTravelersByBookingId(int bookingId) throws Exception;

}
