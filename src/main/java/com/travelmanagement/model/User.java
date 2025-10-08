package com.travelmanagement.model;

import java.time.LocalDate;

public class User {
	private Integer userId;
	private String userName;
	private String userEmail;
	private String userPassword;
	private String userRole; 
	private Boolean isActive;
	private Boolean isDelete;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	 private String imageurl;
	public Integer getUserId() {
		return userId;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public Boolean isDelete() {
		return isDelete;
	}

	public void setDelete(Boolean delete) {
		isDelete = delete;
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
}
