package com.travelmanagement.dto.responseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponseDTO {

	private int bookingId;
	private int userId;
	private int packageId;
	private LocalDateTime bookingDate;
	private String status;
	private int noOfTravellers;
	private String packageName;
	private String packageImage;
	private int duration;
	private double amount;
	private LocalDateTime created_at;
	private LocalDateTime departureDateAndTime;
	private LocalDateTime lastBookingDate;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private List<TravelerResponseDTO> travelers;

	public List<TravelerResponseDTO> getTravelers() {
		return travelers;
	}

	public void setTravelers(List<TravelerResponseDTO> travelers) {
		this.travelers = travelers;
	}

	public LocalDateTime getLastBookingDate() {
		return lastBookingDate;
	}

	public void setLastBookingDate(LocalDateTime lastBookingDate) {
		this.lastBookingDate = lastBookingDate;
	}

	public LocalDateTime getDepartureDateAndTime() {
		return departureDateAndTime;
	}

	public void setDepartureDateAndTime(LocalDateTime departureDateAndTime) {
		this.departureDateAndTime = departureDateAndTime;
	}

	public String getPackageName() {
		return packageName;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageImage() {
		return packageImage;
	}

	public void setPackageImage(String packageImage) {
		this.packageImage = packageImage;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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
