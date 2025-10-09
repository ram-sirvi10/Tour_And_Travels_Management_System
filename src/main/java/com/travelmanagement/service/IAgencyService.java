package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;

public interface IAgencyService {

	AgencyResponseDTO register(AgencyRegisterRequestDTO dto) throws Exception;

	AgencyResponseDTO login(LoginRequestDTO dto) throws Exception;

	boolean deleteAgency(Integer agencyId) throws Exception;

//	List<AgencyResponseDTO> getAllAgencies(int limit, int offset) throws Exception;

	AgencyResponseDTO getAgencyByEmail(String email) throws Exception;

	AgencyResponseDTO getAgencyById(Integer agencyId) throws Exception;

	boolean updateAgencyActiveState(Integer agencyId, Boolean active) throws Exception;

//	List<AgencyResponseDTO> getAgenciesByStatus(String status, int limit, int offset) throws Exception;

	boolean updateAgencyStatus(Integer agencyId, String status) throws Exception;

//	List<AgencyResponseDTO> getAgenciesByActiveState(Boolean isActive, int limit, int offset) throws Exception;
//
//	List<AgencyResponseDTO> searchAgenciesByKeyword(String keyword, int limit, int offset) throws Exception;
//
//	List<AgencyResponseDTO> getDeletedAgencies(String keyword, int limit, int offset) throws Exception;

	AgencyResponseDTO getAgencyByRegistrationNumber(String regNo) throws Exception;

	List<AgencyResponseDTO> filterAgencies(String status, Boolean active, String startDate, String endDate,
			String keyword, Boolean delete, int limit, int offset) throws Exception;

	long countAgencies(String status, Boolean activeState, Boolean isDeleted, String keyword, String startDate,
			String endDate) throws Exception;

	boolean updateAgency(AgencyRegisterRequestDTO dto) throws Exception;

	boolean changePassword(Integer agencyId, String newPassword) throws Exception;

}
