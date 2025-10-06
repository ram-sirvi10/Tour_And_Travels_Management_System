package com.travelmanagement.dao;

import com.travelmanagement.model.Packages;
import java.util.List;

public interface IPackageDAO {
	int addPackage(Packages pkg) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;
	
	

	boolean deletePackage(int packageId) throws Exception;

	boolean togglePackageStatus(int packageId) throws Exception;

	List<Packages> getAllPackages() throws Exception;

	Packages getPackageById(int packageId) throws Exception;

	boolean adjustSeats(int packageId, int seatsChange) throws Exception;


//	int updateSeatsOptimistic(int packageId, int seatsToBook, int version) throws Exception;

	int countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom, String dateTo,
			Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception;

	List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset, Boolean isAgencyView)
			throws Exception;

	int updateSeatsOptimistic(int packageId, int seatsToBook, int currentVersion) throws Exception;
}
