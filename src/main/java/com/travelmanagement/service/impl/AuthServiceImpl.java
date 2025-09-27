package com.travelmanagement.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.dao.impl.AgencyDAOImpl;
import com.travelmanagement.dao.impl.UserDAOImpl;
import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IAuthService;
import com.travelmanagement.util.ValidationUtil;

public class AuthServiceImpl implements IAuthService {

	@Override
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
		} else {
			IUserDAO userDAO = new UserDAOImpl();
			try {
				User existingUser = userDAO.getUserByEmail(dto.getEmail());
				if (existingUser != null) {
					if (existingUser.isDelete()) {

					} else if (!existingUser.isActive()) {

						errors.put("email", "Account is blocked! You cannot register with this email.");
					} else {

						errors.put("email", "Email already exists");
					}
				}
			} catch (Exception e) {

			}
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

	@Override
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
			errors.put("password",
					"Invalid password (min 6 chars, at least 1 upper, 1 lower, 1 digit, 1 special char)");
		}

		return errors;
	}

	@Override
	public Map<String, String> validateRegisterAgencyDto(AgencyRegisterRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();

		// Agency Name
		if (dto.getAgencyName() == null || dto.getAgencyName().trim().isEmpty()) {
			errors.put("agencyName", "Agency name cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getAgencyName())) {
			errors.put("agencyName", "Invalid agency name");
		}

		// Owner Name
		if (dto.getOwnerName() == null || dto.getOwnerName().trim().isEmpty()) {
			errors.put("ownerName", "Owner name cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getOwnerName())) {
			errors.put("ownerName", "Invalid owner name");
		}

		IAgencyDAO agencyDAO = new AgencyDAOImpl();

		// Email
		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("email", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("email", "Invalid email");
		} else {
			try {
				Agency existingAgency = agencyDAO.getAgencyByField("email", dto.getEmail());

				if (existingAgency != null) {
					if ("pending".equalsIgnoreCase(existingAgency.getStatus())) {
						errors.put("email", "Registration request for this email already pending approval!");
					}

					else if (existingAgency.isDelete()) {

					} else if (!existingAgency.isActive()) {
						errors.put("email", "Agency account is blocked! You cannot register with this email.");

					} else {
						errors.put("email", "Email already exists");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Phone
		if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
			errors.put("phone", "Phone number cannot be empty");
		} else if (!ValidationUtil.isValidMob(dto.getPhone())) {
			errors.put("phone", "Invalid phone number");
		}

		// City
		if (dto.getCity() == null || dto.getCity().trim().isEmpty()) {
			errors.put("city", "City cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getCity())) {
			errors.put("city", "Invalid city");
		}

		// State
		if (dto.getState() == null || dto.getState().trim().isEmpty()) {
			errors.put("state", "State cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getState())) {
			errors.put("state", "Invalid state");
		}

		// Country
		if (dto.getCountry() == null || dto.getCountry().trim().isEmpty()) {
			errors.put("country", "Country cannot be empty");
		} else if (!ValidationUtil.isValidCityOrState(dto.getCountry())) {
			errors.put("country", "Invalid country");
		}

		// Pincode
		if (dto.getPincode() == null || dto.getPincode().trim().isEmpty()) {
			errors.put("pincode", "Pincode cannot be empty");
		} else if (!ValidationUtil.isValidPincode(dto.getPincode())) {
			errors.put("pincode", "Invalid pincode");
		}

		// Registration Number
		if (dto.getRegistrationNumber() == null || dto.getRegistrationNumber().trim().isEmpty()) {
			errors.put("registrationNumber", "Registration number cannot be empty");
		} else if (!ValidationUtil.isValidRegistrationNumber(dto.getRegistrationNumber())) {
			errors.put("registrationNumber", "Invalid registration number");
		} else {
			try {
				Agency existingAgency = agencyDAO.getAgencyByField("registration_number", dto.getRegistrationNumber());
				if (existingAgency != null) {
					if (existingAgency.isDelete()) {

					} else if (!existingAgency.isActive()) {
						errors.put("registrationNumber",
								"Agency account is blocked! You cannot register with this registration number.");
					} else if ("pending".equalsIgnoreCase(existingAgency.getStatus())) {
						errors.put("registrationNumber",
								"Registration request for this registrationNumber already pending approval!");
					} else {
						errors.put("registrationNumber", "Registration number already exists");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Password
		if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			errors.put("password", "Password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(dto.getPassword())) {
			errors.put("password", "Password must contain min 6 chars, 1 upper, 1 lower, 1 digit, 1 special char");
		}

		// Confirm Password
		if (dto.getConfirmPassword() == null || dto.getConfirmPassword().trim().isEmpty()) {
			errors.put("confirmPassword", "Confirm password cannot be empty");
		} else if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			errors.put("confirmPassword", "Passwords do not match");
		}

		return errors;
	}

	@Override
	public Map<String, String> validateLoginAgencyDto(LoginRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();

		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("email", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("email", "Invalid email");
		}

		if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
			errors.put("password", "Password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(dto.getPassword())) {
			errors.put("password",
					"Invalid password (min 6 chars, at least 1 upper, 1 lower, 1 digit, 1 special char)");
		}

		IAgencyDAO agencyDAO = new AgencyDAOImpl();
//		    try {
//		    	 Agency dbAgency = agencyDAO.getAgencyByField("email", dto.getEmail());
//		        if (dbAgency != null) {
//		             	
//		           if (dbAgency.isDelete()) {
//		                errors.put("email", "Agency not found!");
//		            } else if (!dbAgency.isActive()) {
//		                errors.put("email", "Agency account is blocked! Please contact support.");
//		            }
//		        }
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		    }

		try {
			Agency existingAgency = agencyDAO.getAgencyByField("email", dto.getEmail());
			if (existingAgency != null) {
				if ("pending".equalsIgnoreCase(existingAgency.getStatus())) {
					errors.put("email", " Request for this email is in pending approval!");
				} else if (existingAgency.isDelete()) {
					errors.put("email", "Agency not found!");
				} else if (!existingAgency.isActive()) {
					errors.put("email", "Agency account is blocked! You cannot login with this email.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return errors;
	}

	public Map<String, String> validateChangePassword(String newPassword, String confirmPassword, String oldPassword) {
		Map<String, String> errors = new HashMap<>();

		if (oldPassword == null || oldPassword.trim().isEmpty()) {
			errors.put("oldPassword", "Old password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(newPassword)) {
			errors.put("oldPassword", "Password must have min 6 chars, 1 upper, 1 lower, 1 digit, 1 special char");
		}

		if (newPassword == null || newPassword.trim().isEmpty()) {
			errors.put("newPassword", "New password cannot be empty");
		} else if (!ValidationUtil.isValidPassword(newPassword)) {
			errors.put("newPassword", "Password must have min 6 chars, 1 upper, 1 lower, 1 digit, 1 special char");
		}

		if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
			errors.put("confirmPassword", "Confirm password cannot be empty");
		} else if (!newPassword.equals(confirmPassword)) {
			errors.put("confirmPassword", "Passwords do not match");
		}

		return errors;
	}

	public Map<String, String> validateUpdateUser(RegisterRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();
		IUserDAO userDAO = new UserDAOImpl();

		if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
			errors.put("userName", "Username cannot be empty");
		} else if (!ValidationUtil.isValidName(dto.getUsername())) {
			errors.put("userName", "Invalid username");
		}

		if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
			errors.put("userEmail", "Email cannot be empty");
		} else if (!ValidationUtil.isValidEmail(dto.getEmail())) {
			errors.put("userEmail", "Invalid email");
		} else {
			try {
				User existingUser = userDAO.getUserByEmail(dto.getEmail());
				if (existingUser != null) {

					if (existingUser.getUserId() != dto.getUserId() && !existingUser.isDelete()) {
						if (!existingUser.isActive()) {
							errors.put("userEmail", "Account is blocked! Cannot use this email.");
						} else {
							errors.put("userEmail", "Email already exists");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return errors;
	}

	public Map<String, String> validateTravelers(List<TravelerRequestDTO> travelerRequestDTOs, int packageId) {
		Map<String, String> errors = new HashMap<>();
		Set<String> emailSet = new HashSet<>(); 
		TravelerServiceImpl serviceImpl = new TravelerServiceImpl();
		int i = 1;

		for (TravelerRequestDTO t : travelerRequestDTOs) {

			if (t.getName() == null || t.getName().trim().isEmpty() || !ValidationUtil.isValidName(t.getName())) {
				errors.put("travelerName" + i, "Invalid name");
			}

			String email = t.getEmail();
			if (email == null || email.trim().isEmpty() || !ValidationUtil.isValidEmail(email)) {
				errors.put("travelerEmail" + i, "Invalid email");
			} else {

				if (serviceImpl.isTravelerAlreadyBooked(email, packageId)) {
					errors.put("travelerEmail" + i, "Traveler already booked this package with email " + email);
				}

				else if (emailSet.contains(email)) {
					errors.put("travelerEmail" + i, "Duplicate email in request list: " + email);
				} else {
					emailSet.add(email);
				}
			}

			if (t.getMobile() == null || !ValidationUtil.isValidMob(t.getMobile())) {
				errors.put("travelerMobile" + i, "Invalid mobile number");
			}
			if (t.getAge() <= 3) {
				errors.put("travelerAge" + i, "Invalid age");
			}

			i++;
		}

		return errors;
	}

}
