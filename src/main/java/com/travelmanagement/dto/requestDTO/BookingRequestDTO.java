package com.travelmanagement.dto.requestDTO;

import java.util.Date;
import java.util.List;

public class BookingRequestDTO {
	private int userId;
	private int packageId;
	private Date bookingDate;
	private int numberOfTravelers;
	private List<TravelerRequestDTO> travelers;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
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
