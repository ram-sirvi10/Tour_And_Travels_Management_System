package com.travelmanagement.dto.requestDTO;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRequestDTO {
	private int userId;
	private int packageId;
	private LocalDateTime bookingDate;
	private int numberOfTravelers;
	private List<TravelerRequestDTO> travelers;
	private String status;
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPackageId() {
		return packageId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPackageId(int packageId) {
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

	public void setNumberOfTravelers(int numberOfTravelers) {
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
