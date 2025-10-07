package com.travelmanagement.dto.responseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class PackageResponseDTO {
	@Override
	public String toString() {
		return "PackageResponseDTO [packageId=" + packageId + ", title=" + title + ", agencyId=" + agencyId
				+ ", description=" + description + ", price=" + price + ", location=" + location + ", duration="
				+ duration + ", isActive=" + isActive + ", imageurl=" + imageurl + ", totalSeats=" + totalSeats
				+ ", packageSchedule=" + packageSchedule + ", version=" + version + ", departureDate=" + departureDate
				+ ", lastBookingDate=" + lastBookingDate + ", totalBookings=" + totalBookings + "]";
	}

	private Integer packageId;
	private String title;
	private Integer agencyId;
	private String description;
	private Double price;
	private String location;
	private Integer duration;
	private Boolean isActive;
	private String imageurl;
	private Integer totalSeats;
	private List<PackageScheduleResponseDTO> packageSchedule;
	private Integer version;
	private Boolean isDelete;

	private LocalDateTime departureDate;

	private LocalDateTime lastBookingDate;

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public List<PackageScheduleResponseDTO> getPackageSchedule() {
		return packageSchedule;
	}

	public void setPackageSchedule(List<PackageScheduleResponseDTO> packageSchedule) {
		this.packageSchedule = packageSchedule;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	private Integer totalBookings;

	public Integer getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(Integer totalBookings) {
		this.totalBookings = totalBookings;
	}

}
