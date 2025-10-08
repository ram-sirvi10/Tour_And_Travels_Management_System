package com.travelmanagement.dto.responseDTO;

import java.time.LocalDate;

public class AgencyResponseDTO {

	private Integer agencyId;
	private String agencyName;
	private String ownerName;
	private String email;
	private String phone;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String registrationNumber;
	private String status;
	private String area;
	private Boolean isActive;
	private Boolean isDelete;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String imageurl;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean isDelete() {
		return isDelete;
	}

	public void setDelete(Boolean isDelete) {
		this.isDelete = isDelete;
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
		return "AgencyResponseDTO [agencyId=" + agencyId + ", agencyName=" + agencyName + ", ownerName=" + ownerName
				+ ", email=" + email + ", phone=" + phone + ", city=" + city + ", state=" + state + ", country="
				+ country + ", pincode=" + pincode + ", registrationNumber=" + registrationNumber + ", status=" + status
				+ ", isActive=" + isActive + ", isDelete=" + isDelete + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}

}
