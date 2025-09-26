package com.travelmanagement.dto.responseDTO;

import java.time.LocalDate;

public class BookingResponseDTO {

	private int bookingId;
	private int userId;
	private int packageId;
	private LocalDate bookingDate;
	private String status;
	private int noOfTravellers;

	public int getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(int noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

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

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
