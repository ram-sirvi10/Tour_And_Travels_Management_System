package com.travelmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.dao.ILocationDAO;
import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.dao.impl.AgencyDAOImpl;
import com.travelmanagement.dao.impl.LocationDAOImpl;
import com.travelmanagement.dao.impl.UserDAOImpl;
import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.requestDTO.PackageScheduleRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IAuthService;
import com.travelmanagement.util.ValidationUtil;

public class AuthServiceImpl implements IAuthService {

	public Map<String, String> validatePackageFields(PackageRegisterDTO dto, LocalDateTime oldDeparture,
			LocalDateTime oldBooking) {

		Map<String, String> errors = new HashMap<>();

		if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()
				|| !ValidationUtil.isValidPackageActivity(dto.getTitle())) {
			errors.put("title", "Invalid title");
		}

		if (dto.getLocation() == null || dto.getLocation().trim().isEmpty()
				|| !ValidationUtil.isValidCityOrState(dto.getLocation())) {
			errors.put("location", "Invalid location");
		}

		if (dto.getPrice() == null || dto.getPrice() <= 0) {
			errors.put("price", "Price must be greater than zero");
		}

		if (dto.getDuration() == null || dto.getDuration() <= 0) {
			errors.put("duration", "Duration must be at positive integer");
		}

		if (dto.getTotalSeats() == null || dto.getTotalSeats() <= 0) {
			errors.put("totalseats", "Total seats must be at positive integer ");
		}

		LocalDateTime now = LocalDateTime.now();

		
		LocalDateTime dep = dto.getDepartureDate();
		LocalDateTime book = dto.getLastBookingDate();

		if (dep == null) {
			errors.put("departureDate", "Departure date is required");
		} else if (dep.isBefore(now)) {
			errors.put("departureDate", "Departure date must be in the future");
		}

		if (book == null) {
			errors.put("lastBookingDate", "Last booking date is required");
		} else if (book.isBefore(now)) {
			errors.put("lastBookingDate", "Last booking date must be in the future");
		}

		// Last booking must be before departure (not after)
		if (dep != null && book != null && book.isAfter(dep)) {
			errors.put("lastBookingDate", "Last booking date cannot be after departure date");
		}

		// ------------------ OLD DATES VALIDATION (UPDATE CASE) ------------------
		if (oldDeparture != null && dep != null && dep.isBefore(oldDeparture)) {
			errors.put("departureDate",
					"Departure date cannot be earlier than the previous one (" + oldDeparture.toLocalDate() + ")");
		}

		if (oldBooking != null && book != null && book.isBefore(oldBooking)) {
			errors.put("lastBookingDate",
					"Last booking date cannot be earlier than the previous one (" + oldBooking.toLocalDate() + ")");
		}

		// ------------------ DESCRIPTION & STATUS ------------------
		if (dto.getDescription() == null || !ValidationUtil.isValidDescription(dto.getDescription())) {
			errors.put("description", "Description must have at least 10 words");
		}

		if (dto.getIsActive() == null) {
			errors.put("isActive", "Package status is required");
		}

		// ------------------ SCHEDULE VALIDATION ------------------
		List<PackageScheduleRequestDTO> scheduleList = dto.getPackageSchedule();
		if (scheduleList == null || scheduleList.size() != dto.getDuration()) {
			errors.put("schedule", "Schedule must have exactly " + dto.getDuration() + " days");
		} else {
			for (int i = 0; i < scheduleList.size(); i++) {
				PackageScheduleRequestDTO day = scheduleList.get(i);

				if (day.getActivity() == null || !ValidationUtil.isValidPackageActivity(day.getActivity())) {
					errors.put("day" + (i + 1) + "_activity", "Activity for day " + (i + 1) + " is invalid or empty");
				}

				if (day.getDescription() == null || !ValidationUtil.isValidDescription(day.getDescription())) {
					errors.put("day" + (i + 1) + "_desc",
							"Description for day " + (i + 1) + " must have at least 10 words");
				}
			}
		}

		return errors;
	}

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
			errors.put("password",
					"Invalid password (min 6 chars, at least 1 upper, 1 lower, 1 digit, 1 special char)");
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
		}

		return errors;
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

		IAgencyDAO agencyDAO = new AgencyDAOImpl();

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

		if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
			errors.put("phone", "Phone number cannot be empty");
		} else if (!ValidationUtil.isValidMob(dto.getPhone())) {
			errors.put("phone", "Invalid phone number");
		}

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
		errors.putAll(validateLocation(dto));
		return errors;
	}

	public Map<String, String> validateUpdateAgencyDto(AgencyRegisterRequestDTO dto) {
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
		if (dto.getPhone() == null || dto.getPhone().trim().isEmpty())
			errors.put("phone", "Phone cannot be empty");
		if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
			errors.put("phone", "Phone number cannot be empty");
		} else if (!ValidationUtil.isValidMob(dto.getPhone())) {
			errors.put("phone", "Invalid phone number");
		}
		errors.putAll(validateLocation(dto));
		return errors;
	}

	public Map<String, String> validateLocation(AgencyRegisterRequestDTO dto) {
		Map<String, String> errors = new HashMap<>();
		ILocationDAO locationDAO = new LocationDAOImpl();
		try {

			List<String> countries = locationDAO.getLocations("countries", null);
			if (!countries.contains(dto.getCountry())) {
				errors.put("country", "Invalid country selected");
			}

			List<String> states = locationDAO.getLocations("states", dto.getCountry());
			if (!states.contains(dto.getState())) {
				errors.put("state", "State does not belong to selected country");
			}

			List<String> cities = locationDAO.getLocations("cities", dto.getState());
			if (!cities.contains(dto.getCity())) {
				errors.put("city", "City does not belong to selected state");
			}

			List<String> areas = locationDAO.getLocations("areas", dto.getCity());
			if (!areas.contains(dto.getArea())) {
				errors.put("area", "Area does not belong to selected city");
			}

			List<String> pincodes = locationDAO.getLocations("pincode", dto.getArea());
			if (!pincodes.contains(dto.getPincode())) {
				errors.put("pincode", "Invalid pincode for selected area");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errors.put("location", "Error validating location hierarchy");
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
