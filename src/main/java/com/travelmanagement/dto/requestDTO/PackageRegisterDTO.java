package com.travelmanagement.dto.requestDTO;

import java.time.LocalDateTime;
import java.util.List;

import com.travelmanagement.model.PackageSchedule;

public class PackageRegisterDTO {

	private Integer packageId;
	private String title;
	private Integer agencyId;
	private String description;
	private Double price;
	private String location;
	private Integer duration;
	private Boolean isActive;
	private Integer totalSeats;
	private String imageurl;
	private LocalDateTime departureDate;
	private List<PackageScheduleRequestDTO> packageSchedule;
	private LocalDateTime lastBookingDate;

	public List<PackageScheduleRequestDTO> getPackageSchedule() {
		return packageSchedule;
	}

	public void setPackageSchedule(List<PackageScheduleRequestDTO> packageSchedule) {
		this.packageSchedule = packageSchedule;
	}

	public LocalDateTime getLastBookingDate() {
		return lastBookingDate;
	}

	public void setLastBookingDate(LocalDateTime lastBookingDate) {
		this.lastBookingDate = lastBookingDate;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}

	public Integer getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(Integer totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price2) {
		this.price = price2;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration2) {
		this.duration = duration2;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
