package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Traveler;

public interface ITravelerDAO {
	boolean createTravelers(List<Traveler> travelers) throws Exception;

	Traveler getTravelerById(int travelerId) throws Exception;

	boolean updateTraveler(Traveler traveler) throws Exception;

	boolean deleteTraveler(int travelerId) throws Exception;

	List<Traveler> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, Integer paymentId, String bookingStatus, String paymentStatus, String keyword,
			String startDate, String endDate, int limit, int offset) throws Exception;

	int getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId, Integer agencyId,
			Integer paymentId, String bookingStatus, String paymentStatus, String keyword, String startDate,
			String endDate) throws Exception;

	boolean isTravelerAlreadyBooked(String email, int packageId) throws Exception;
}
