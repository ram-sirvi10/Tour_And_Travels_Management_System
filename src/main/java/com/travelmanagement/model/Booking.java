package com.travelmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {
	private int bookingId;
	private int userId;
	private int packageId;
	private LocalDate bookingDate;
	private String status;
	private int noOfTravellers;
	private LocalDateTime created_at;

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

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
