package com.travelmanagement.service;

import java.util.Map;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.User;

public interface IAuthService {

	User mapRegisterDtoToUser(RegisterRequestDTO registerRequestDTO);

	User mapLoginDtoToUser(LoginRequestDTO loginRequestDTO);

	Map<String, String> validateRegisterAgencyDto(AgencyRegisterRequestDTO dto);

	UserResponseDTO mapUserToUserResponseDTO(User user);
  
}
