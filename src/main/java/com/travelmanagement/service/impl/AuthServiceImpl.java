package com.travelmanagement.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IAuthService;
import com.travelmanagement.util.ValidationUtil;

public class AuthServiceImpl implements IAuthService {

	// Validate registration fields and return errors map
	public Map<String, String> validateRegisterDto(RegisterRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();

		if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
			errors.put("username", "Username cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getUsername())) {
			errors.put("username", "Invalid username");
		}

		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("email", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("email", "Invalid email");
		}

		if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			errors.put("password", "Password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(dto.getPassword())) {
			errors.put("password", "Invalid password");
		}

		if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
			errors.put("confirmPassword", "Confirm Password cannot be empty");
		} else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			errors.put("confirmPassword", "Passwords do not match");
		}

		return errors;
	}

	// Validate login fields and return errors map
	public Map<String, String> validateLoginDto(LoginRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();

		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("email", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("email", "Invalid email");
		}

		if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			errors.put("password", "Password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(dto.getPassword())) {
			errors.put("password", "Invalid password");
		}

		return errors;
	}

	@Override
	public User mapRegisterDtoToUser(RegisterRequestDTO dto) {
		User user = new User();
		user.setUserName(dto.getUsername());
		user.setUserEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		return user;
	}

	@Override
	public UserResponseDTO mapUserToUserResponseDTO(User user) {
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setUserName(user.getUserName());
		userResponseDTO.setEmail(user.getUserEmail());
		userResponseDTO.setPassword(user.getUserPassword());
		userResponseDTO.setRole(user.getUserRole());
		return userResponseDTO;
	}
	@Override
	public User mapLoginDtoToUser(LoginRequestDTO dto) {
		User user = new User();
		user.setUserEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		return user;
	}

	@Override
	public Map<String, String> validateRegisterAgencyDto(AgencyRegisterRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();

		if (dto.getAgencyName() == null || dto.getAgencyName().trim().isEmpty()) {
			errors.put("agencyName", "Agency name cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getAgencyName())) {
			errors.put("agencyName", "Invalid agency name");
		}

		if (dto.getOwnerName() == null || dto.getOwnerName().trim().isEmpty()) {
			errors.put("ownerName", "Owner name cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getOwnerName())) {
			errors.put("ownerName", "Invalid owner name");
		}

		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("email", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("email", "Invalid email");
		}

		if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
			errors.put("phone", "Phone number cannot be empty");
		} else if (!ValidationUtil.isValidMob(dto.getPhone())) {
			errors.put("phone", "Invalid phone number");
		}

		if (dto.getCity() == null || dto.getCity().trim().isEmpty()) {
			errors.put("city", "City cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getCity())) {
			errors.put("city", "Invalid city");
		}

		if (dto.getState() == null || dto.getState().trim().isEmpty()) {
			errors.put("state", "State cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getState())) {
			errors.put("state", "Invalid state");
		}

		if (dto.getCountry() == null || dto.getCountry().trim().isEmpty()) {
			errors.put("country", "Country cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getCountry())) {
			errors.put("country", "Invalid country");
		}

		if (dto.getPincode() == null || dto.getPincode().trim().isEmpty()) {
			errors.put("pincode", "Pincode cannot be empty");
		} else if (!ValidationUtil.isValidPincode(dto.getPincode())) {
			errors.put("pincode", "Invalid pincode");
		}

		if (dto.getRegistrationNumber() == null || dto.getRegistrationNumber().trim().isEmpty()) {
			errors.put("registrationNumber", "Registration number cannot be empty");
		} else if (!ValidationUtil.isValidRegistrationNumber(dto.getRegistrationNumber())) {
			errors.put("registrationNumber", "Invalid registration number");
		}

		if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			errors.put("password", "Password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(dto.getPassword())) {
			errors.put("password", "Password must contain min 6 chars, 1 upper, 1 lower, 1 digit, 1 special char");
		}

		if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
			errors.put("confirmPassword", "Confirm password cannot be empty");
		} else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			errors.put("confirmPassword", "Passwords do not match");
		}

		return errors;
	}

	public Agency mapRegisterAgencyDtoToAgency(AgencyRegisterRequestDTO dto) {
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

}
