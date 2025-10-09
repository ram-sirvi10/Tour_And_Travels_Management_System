package com.travelmanagement.service;

import java.util.Map;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;

public interface IAuthService {

	

	Map<String, String> validateRegisterAgencyDto(AgencyRegisterRequestDTO dto);

	

	Map<String, String> validateRegisterDto(RegisterRequestDTO dto);

	

	Map<String, String> validateLoginDto(LoginRequestDTO dto);



	Map<String, String> validateLoginAgencyDto(LoginRequestDTO dto);
  
	
}
