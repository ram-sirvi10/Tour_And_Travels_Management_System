package com.travelmanagement.dto.responseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponseDTO {

	private Integer bookingId;
	private Integer userId;
	private Integer packageId;
	private LocalDateTime bookingDate;
	private String status;
	private Integer noOfTravellers;
	private String packageName;
	private String packageImage;
	private Integer duration;
	private Double amount;
	private LocalDateTime created_at;
	private LocalDateTime departureDateAndTime;
	private LocalDateTime lastBookingDate;
	private String userName;
	private String userEmail;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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
