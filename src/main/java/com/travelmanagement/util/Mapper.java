package com.travelmanagement.util;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.User;

public class Mapper {
	public static User mapRegisterDtoToUser(RegisterRequestDTO dto) {
		User user = new User();
		user.setUserName(dto.getUsername());
		user.setUserEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		return user;
	}
	
	public static UserResponseDTO mapUserToUserResponseDTO(User user) {
	    UserResponseDTO userResponseDTO = new UserResponseDTO();
	    userResponseDTO.setUserId(user.getUserId());          
	    userResponseDTO.setUserName(user.getUserName());
	    userResponseDTO.setUserEmail(user.getUserEmail());
	    userResponseDTO.setUserRole(user.getUserRole());
	    userResponseDTO.setActive(user.isActive());         
	    userResponseDTO.setDelete(user.isDelete());
	    userResponseDTO.setCreatedAt(user.getCreatedAt());
	    userResponseDTO.setUpdatedAt(user.getUpdatedAt());

	    return userResponseDTO;
	}


	
	public static User mapLoginDtoToUser(LoginRequestDTO dto) {
		User user = new User();
		user.setUserEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		return user;
	}
	
	
		public static Agency mapRegisterAgencyDtoToAgency(AgencyRegisterRequestDTO dto) {
			Agency agency = new Agency();
			agency.setAgencyName(dto.getAgencyName());
			agency.setOwnerName(dto.getOwnerName());
			agency.setEmail(dto.getEmail());
			agency.setPhone(dto.getPhone());
			agency.setCity(dto.getCity());
			agency.setState(dto.getState());
			agency.setCountry(dto.getCountry());
			agency.setPincode(dto.getPincode());
			agency.setRegistrationNumber(dto.getRegistrationNumber());
			agency.setPassword(dto.getPassword());
			return agency;
		}
		
		 
		public static AgencyResponseDTO mapAgencyToAgencyResponseDTO(Agency agency) {
		    AgencyResponseDTO dto = new AgencyResponseDTO();
		    dto.setAgencyId(agency.getAgencyId());
		    dto.setAgencyName(agency.getAgencyName());
		    dto.setOwnerName(agency.getOwnerName());
		    dto.setEmail(agency.getEmail());
		    dto.setPhone(agency.getPhone());
		    dto.setCity(agency.getCity());
		    dto.setState(agency.getState());
		    dto.setCountry(agency.getCountry());
		    dto.setPincode(agency.getPincode());
		    dto.setRegistrationNumber(agency.getRegistrationNumber());
		    dto.setStatus(agency.getStatus());
		    dto.setActive(agency.isActive());
		    dto.setDelete(agency.isDelete());
		    dto.setCreatedAt(agency.getCreatedAt());
		    dto.setUpdatedAt(agency.getUpdatedAt());
		    return dto;
		}
}
