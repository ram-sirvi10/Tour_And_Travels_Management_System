package com.travelmanagement.model;

import java.time.LocalDateTime;

public class Booking {
	private Integer bookingId;
	private Integer userId;
	private Integer packageId;
	private LocalDateTime bookingDate;
	private String status;
	private Integer noOfTravellers;
	private LocalDateTime created_at;

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public Integer getNoOfTravellers() {
		return noOfTravellers;
	}

	public void setNoOfTravellers(Integer noOfTravellers) {
		this.noOfTravellers = noOfTravellers;
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public LocalDateTime getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
