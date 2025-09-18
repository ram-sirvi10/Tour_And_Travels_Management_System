package com.travelmanagement.dao;

import com.travelmanagement.model.Agency;
import java.util.List;

public interface IPackageDAO {
	int createPackage(Agency p) throws Exception;

	Agency getPackageById(int id) throws Exception;

	List<Agency> getAllPackages() throws Exception;

	List<Agency> searchPackages(String keyword) throws Exception;

	boolean updatePackage(Agency p) throws Exception;

	boolean deletePackage(int id) throws Exception;
	
	List<Agency> getAllPackagesByAgency(int agencyId) throws Exception;
	
	
	
}
