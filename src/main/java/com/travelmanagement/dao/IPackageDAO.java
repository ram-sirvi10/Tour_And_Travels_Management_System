package com.travelmanagement.dao;

import com.travelmanagement.model.Packages;
import java.util.List;

public interface IPackageDAO {
	int addPackage(Packages pkg) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;
	
	

	boolean deletePackage(Integer packageId) throws Exception;

	boolean togglePackageStatus(Integer packageId) throws Exception;

	List<Packages> getAllPackages() throws Exception;

	Packages getPackageById(Integer packageId) throws Exception;

	boolean adjustSeats(Integer packageId, Integer seatsChange) throws Exception;


	long countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom, String dateTo,
			Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception;

	List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset, Boolean isAgencyView)
			throws Exception;

	int updateSeatsOptimistic(Integer packageId, Integer seatsToBook, Integer currentVersion) throws Exception;
}
