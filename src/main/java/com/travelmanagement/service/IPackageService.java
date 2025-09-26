package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.model.Packages;

public interface IPackageService {

	boolean addPackage(Packages pkg) throws Exception;


	PackageResponseDTO getPackageById(int id) throws Exception;


	List<Packages> getAllPackages() throws Exception;

	List<Packages> searchPackages(String keyword) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;

	boolean deletePackage(int id) throws Exception;

	boolean togglePackageStatus(int id) throws Exception;
	List<PackageResponseDTO> getAllPackages(int agencyId) throws Exception;
    List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception;
    List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, int limit, int offset) throws Exception;
}
