package com.travelmanagement.dto.responseDTO;

import java.time.LocalDateTime;
import java.util.List;

import com.travelmanagement.model.PackageSchedule;

public class PackageResponseDTO {
	private int packageId;
	private String title;
	private int agencyId;
	private String description;
	private double price;
	private String location;
	private int duration;
	private boolean isActive;
	private String imageurl;
	private int totalSeats;
	private List<PackageSchedule> packageSchedule;
	private int version;

	private LocalDateTime departureDate;

	private LocalDateTime lastBookingDate;

	public List<PackageSchedule> getPackageSchedule() {
		return packageSchedule;
	}

	public void setPackageSchedule(List<PackageSchedule> packageSchedule) {
		this.packageSchedule = packageSchedule;
	}

	public void setActive(boolean isActive) {
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setPackageId(Package[] packages) {
		// TODO Auto-generated method stub

	}

	private int totalBookings;

	public int getTotalBookings() {
		return totalBookings;
	}

	public void setTotalBookings(int totalBookings) {
		this.totalBookings = totalBookings;
	}

	public void setTotalRevenue(double totalRevenue) {
		// TODO Auto-generated method stub

	}

}
