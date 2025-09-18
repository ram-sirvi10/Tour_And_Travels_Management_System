package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.model.Agency;

public interface IAgencyService {

    AgencyResponseDTO register(AgencyRegisterRequestDTO dto) throws Exception;

    AgencyResponseDTO login(LoginRequestDTO dto) throws Exception;

	boolean approveAgency(int agencyId) throws Exception;

	boolean declineAgency(int agencyId) throws Exception;

	boolean enableAgency(int agencyId) throws Exception;

	boolean disableAgency(int agencyId) throws Exception;

	List<AgencyResponseDTO> getPendingAgencies() throws Exception;

	boolean deleteAgency(int agencyId) throws Exception;

	boolean updateAgency(Agency agency) throws Exception;

	List<AgencyResponseDTO> getAllAgencies() throws Exception;

	AgencyResponseDTO getAgencyByEmail(String email) throws Exception;

	AgencyResponseDTO getAgencyById(int agencyId) throws Exception;

}
