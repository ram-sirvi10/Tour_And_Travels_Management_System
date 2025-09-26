package com.travelmanagement.dao;

import com.travelmanagement.model.Packages;
import java.util.List;

public interface IPackageDAO {
	boolean addPackage(Packages pkg) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;
	
	

	boolean deletePackage(int packageId) throws Exception;

	boolean togglePackageStatus(int packageId) throws Exception;

	List<Packages> getAllPackages() throws Exception;

	Packages getPackageById(int packageId) throws Exception;

	List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, int limit, int offset) throws Exception;

	boolean adjustSeats(int packageId, int seatsChange) throws Exception;
}
