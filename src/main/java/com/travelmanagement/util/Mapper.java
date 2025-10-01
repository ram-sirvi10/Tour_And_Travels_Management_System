package com.travelmanagement.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.travelmanagement.dto.requestDTO.AgencyRegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.BookingRequestDTO;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.PackageRegisterDTO;
import com.travelmanagement.dto.requestDTO.PaymentRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.requestDTO.TravelerRequestDTO;
import com.travelmanagement.dto.responseDTO.AgencyResponseDTO;
import com.travelmanagement.dto.responseDTO.BookingResponseDTO;
import com.travelmanagement.dto.responseDTO.PackageResponseDTO;
import com.travelmanagement.dto.responseDTO.PaymentResponseDTO;
import com.travelmanagement.dto.responseDTO.TravelerResponseDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.Agency;
import com.travelmanagement.model.Booking;
import com.travelmanagement.model.Packages;
import com.travelmanagement.model.Payment;
import com.travelmanagement.model.Traveler;
import com.travelmanagement.model.User;

public class Mapper {
	
	
	public static LocalDate parseAnyDate(String dateStr) throws Exception{

		String[] dateFormats = { "yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy/MM/dd",
				"dd.MM.yyyy", "yyyy.MM.dd", "dd MMM yyyy", "MMM dd, yyyy" };

		for (String format : dateFormats) {
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
				LocalDate localDate = LocalDate.parse(dateStr, formatter);
				return localDate;
			} catch (DateTimeParseException e) {
			}
		}

		throw new IllegalArgumentException("Date format not recognized: " + dateStr);
	}
	
	
	public static User mapRegisterDtoToUser(RegisterRequestDTO dto) {
		User user = new User();
		user.setUserId(dto.getUserId());
		user.setUserName(dto.getUsername());
		user.setUserEmail(dto.getEmail());
		user.setUserPassword(dto.getPassword());
		user.setImageurl(dto.getImageurl());
		return user;
	}

	public static UserResponseDTO mapUserToUserResponseDTO(User user) {
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setUserId(user.getUserId());
		userResponseDTO.setUserName(user.getUserName());
		userResponseDTO.setUserEmail(user.getUserEmail());
		userResponseDTO.setUserRole(user.getUserRole());
		userResponseDTO.setActive(user.isActive());
		userResponseDTO.setUserPassword(user.getUserPassword());
		userResponseDTO.setDelete(user.isDelete());
		userResponseDTO.setCreatedAt(user.getCreatedAt());
		userResponseDTO.setUpdatedAt(user.getUpdatedAt());
		userResponseDTO.setImageurl(user.getImageurl());
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
		agency.setImageurl(dto.getImageurl());
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
		dto.setImageurl(agency.getImageurl());
		return dto;
	}

	public static Packages mapToModel(PackageRegisterDTO dto) {
		Packages pkg = new Packages();
		if (dto.getPackageId() != null && !dto.getPackageId().isEmpty()) {
			pkg.setPackageId(Integer.parseInt(dto.getPackageId()));
		}
		pkg.setTitle(dto.getTitle());
		pkg.setAgencyId(Integer.parseInt(dto.getAgencyId()));
		pkg.setDescription(dto.getDescription());
		pkg.setPrice(Double.parseDouble(dto.getPrice()));
		pkg.setLocation(dto.getLocation());
		pkg.setDuration(Integer.parseInt(dto.getDuration()));
		pkg.setActive(Boolean.parseBoolean(dto.getIsActive()));
		pkg.setImageurl(dto.getImageurl());
		pkg.setTotalSeats(dto.getTotalSeats());
		return pkg;
	}

	public static PackageResponseDTO toResponseDTO(Packages pkg) {
		PackageResponseDTO dto = new PackageResponseDTO();
		dto.setPackageId(pkg.getPackageId());
		dto.setTitle(pkg.getTitle());
		dto.setAgencyId(pkg.getAgencyId());
		dto.setDescription(pkg.getDescription());
		dto.setPrice(pkg.getPrice());
		dto.setLocation(pkg.getLocation());
		dto.setDuration(pkg.getDuration());
		dto.setIsActive(pkg.isActive());
		dto.setImageurl(pkg.getImageurl());
		dto.setTotalSeats(pkg.getTotalSeats());
		dto.setVersion(pkg.getVersion());
		dto.setDepartureDate(pkg.getDepartureDate());
		dto.setLastBookingDate(pkg.getLastBookingDate());
		return dto;
	}

	public static Booking mapBookingRequestToBooking(BookingRequestDTO dto) {
		Booking booking = new Booking();
		booking.setUserId(dto.getUserId());
		booking.setNoOfTravellers(dto.getNumberOfTravelers());
		booking.setPackageId(dto.getPackageId());
		return booking;

	}

	public static BookingResponseDTO mapBookingToBookingResponse(Booking booking) {
		BookingResponseDTO bookingDTO = new BookingResponseDTO();
		bookingDTO.setUserId(booking.getUserId());
		bookingDTO.setBookingDate(booking.getBookingDate());
		bookingDTO.setNoOfTravellers(booking.getNoOfTravellers());
		bookingDTO.setBookingId(booking.getBookingId());
		bookingDTO.setPackageId(booking.getPackageId());
		bookingDTO.setStatus(booking.getStatus());
		bookingDTO.setCreated_at(booking.getCreated_at());
		return bookingDTO;
	}

	public static Payment mapPaymentRequestDTOToPayment(PaymentRequestDTO dto) {
		Payment payment = new Payment();
		payment.setAmount(dto.getAmount());
		payment.setBookingId(dto.getBookingId());
		payment.setStatus(dto.getStatus());
		
		
	
		
		return payment;

	}

	public static PaymentResponseDTO mapPaymentToPaymentResponse(Payment payment) {
		PaymentResponseDTO dto = new PaymentResponseDTO();
		
		dto.setPaymentId(payment.getPaymentId());
		dto.setBookingId(payment.getBookingId());
		dto.setAmount(payment.getAmount());
		dto.setStatus(payment.getStatus());
		dto.setPaymentDate(payment.getPaymentDate());
		return dto;

	}

	public static Traveler mapTravelerDtoToModel(TravelerRequestDTO dto) {
		Traveler traveler = new Traveler();
		traveler.setName(dto.getName());
		traveler.setEmail(dto.getEmail());
		traveler.setAge(dto.getAge());
		traveler.setMobile(dto.getMobile());
		return traveler;
	}
	public static TravelerResponseDTO mapTravelerToTravelerResponseDTO(Traveler traveler) {
		TravelerResponseDTO dto = new TravelerResponseDTO();
		dto.setId(traveler.getId());
		dto.setBookingId(traveler.getBookingId());
		dto.setName(traveler.getName());
		dto.setEmail(traveler.getEmail());
		dto.setMobile(traveler.getMobile());
		dto.setAge(traveler.getAge());
		dto.setStatus(traveler.getStatus());
		return dto;
	}
}
