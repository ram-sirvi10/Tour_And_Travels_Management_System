package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.model.Agency;

public interface IAgencyService {

    AgencyResponseDTO register(AgencyRegisterRequestDTO dto) throws Exception;

    AgencyResponseDTO login(LoginRequestDTO dto) throws Exception;

	boolean deleteAgency(int agencyId) throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	List<AgencyResponseDTO> getAllAgencies(int limit,int offset) throws Exception;

	AgencyResponseDTO getAgencyByEmail(String email) throws Exception;

	AgencyResponseDTO getAgencyById(int agencyId) throws Exception;

	boolean updateAgencyActiveState(int agencyId, boolean active) throws Exception;

	List<AgencyResponseDTO> getAgenciesByStatus(String status, int limit, int offset) throws Exception;

	boolean updateAgencyStatus(int agencyId, String status) throws Exception;

}
