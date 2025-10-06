package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO;
import com.travelmanagement.model.PackageSchedule;
import com.travelmanagement.model.Packages;

public interface IPackageService {

	PackageResponseDTO getPackageById(int id) throws Exception;

	boolean updatePackage(Packages pkg) throws Exception;

	boolean deletePackage(int id) throws Exception;

	boolean togglePackageStatus(int id) throws Exception;

	List<PackageResponseDTO> getAllPackages(int agencyId) throws Exception;

	List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception;

	List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView) throws Exception;

	int countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom, String dateTo,
			Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception;

	List<PackageScheduleResponseDTO> getScheduleByPackage(int packageId);

	int addPackage(PackageRegisterDTO dto) throws Exception;
}
