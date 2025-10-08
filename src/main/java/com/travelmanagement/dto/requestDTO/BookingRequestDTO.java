package com.travelmanagement.dto.requestDTO;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRequestDTO {
	private Integer userId;
	private Integer packageId;
	private LocalDateTime bookingDate;
	private Integer numberOfTravelers;
	private List<TravelerRequestDTO> travelers;
	private String status;
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime localDate) {
		this.bookingDate = localDate;
	}

	public int getNumberOfTravelers() {
		return numberOfTravelers;
	}

	public void setNumberOfTravelers(Integer numberOfTravelers) {
		this.numberOfTravelers = numberOfTravelers;
	}

	public List<TravelerRequestDTO> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<TravelerRequestDTO> travelersDTO) {
		this.travelers = travelersDTO;
	}

	@Override
	public String toString() {
		return "BookingRequestDTO [userId=" + userId + ", packageId=" + packageId + ", bookingDate=" + bookingDate
				+ ", numberOfTravelers=" + numberOfTravelers + ", travelers=" + travelers + "]";
	}

}
