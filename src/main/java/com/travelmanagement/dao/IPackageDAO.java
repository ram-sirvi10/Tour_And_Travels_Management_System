package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.Packages;

public interface IPackageDAO {
	int addPackage(Packages pkg) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;
	
	

	boolean deletePackage(Integer packageId) throws Exception;

	boolean togglePackageStatus(Integer packageId) throws Exception;

	List<Packages> getAllPackages() throws Exception;

	Packages getPackageById(Integer packageId) throws Exception;

	boolean adjustSeats(Integer packageId, Integer seatsChange) throws Exception;


	long countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom, String dateTo,
			Integer totalSeats, Boolean isActive, Boolean isAgencyView,Boolean includePast) throws Exception;

	List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset, Boolean isAgencyView,Boolean includePast)
			throws Exception;

	int updateSeatsOptimistic(Integer packageId, Integer seatsToBook, Integer currentVersion) throws Exception;
}
