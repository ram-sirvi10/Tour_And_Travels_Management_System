package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.model.Packages;

public interface IPackageService {

	boolean addPackage(Packages pkg) throws Exception;

	static Packages getPackageById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	List<Packages> getAllPackages() throws Exception;

	List<Packages> searchPackages(String keyword) throws Exception;

	static boolean updatePackage(Packages pkg) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	boolean deletePackage(int id) throws Exception;

	boolean togglePackageStatus(int id) throws Exception;
}
