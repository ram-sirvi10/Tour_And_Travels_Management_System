package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageScheduleResponseDTO;

public interface IPackageService {

	PackageResponseDTO getPackageById(Integer id) throws Exception;

	boolean deletePackage(Integer id) throws Exception;

	boolean togglePackageStatus(Integer id) throws Exception;

//	List<PackageResponseDTO> getAllPackages(Integer agencyId) throws Exception;

//	List<PackageResponseDTO> searchPackagesByKeyword(String keyword) throws Exception;

	List<PackageResponseDTO> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView) throws Exception;

	long countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom, String dateTo,
			Integer totalSeats, Boolean isActive, Boolean isAgencyView) throws Exception;

	List<PackageScheduleResponseDTO> getScheduleByPackage(Integer packageId);

	int addPackage(PackageRegisterDTO dto) throws Exception;
}
