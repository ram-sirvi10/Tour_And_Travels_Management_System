package com.travelmanagement.service;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.model.Agency;

public interface IAgencyService {

	void register(AgencyRegisterRequestDTO dto) throws Exception;

	Agency login(LoginRequestDTO dto) throws Exception;

}
