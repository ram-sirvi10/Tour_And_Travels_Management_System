package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.model.Packages;

public interface IPackageService {

	boolean addPackage(Packages pkg) throws Exception;

	Packages getPackageById(int id) throws Exception;

	List<Packages> getAllPackages() throws Exception;

	List<Packages> searchPackages(String keyword) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;

	boolean deletePackage(int id) throws Exception;

	boolean togglePackageStatus(int id) throws Exception;
}
