package com.travelmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Packages {

	private Integer packageId;
	private String title;
	private Integer agencyId;
	private String description;
	private Double price;
	private String location;
	private Integer duration;
	private Boolean isActive;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private Integer totalSeats;
	private String imageurl;
	private LocalDateTime lastBookingDate;
	private Integer version;
	private LocalDateTime departureDate;
	private Integer totalBookings;
	private Boolean isDelete;

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public void setPrice(Double price) {
		this.price = price;
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

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Packages [packageId=" + packageId + ", title=" + title + ", agencyId=" + agencyId + ", description="
				+ description + ", price=" + price + ", location=" + location + ", duration=" + duration + ", isActive="
				+ isActive + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", totalSeats=" + totalSeats
				+ ", imageurl=" + imageurl + ", lastBookingDate=" + lastBookingDate + ", version=" + version
				+ ", departureDate=" + departureDate + ", totalBookings=" + totalBookings + ", isDelete=" + isDelete
				+ "]";
	}

}
