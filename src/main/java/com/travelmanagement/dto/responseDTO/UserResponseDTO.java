package com.travelmanagement.dto.responseDTO;

import java.time.LocalDate;

public class UserResponseDTO {
	private int userId;
	private String userName;
	private String userEmail;
//	private String userPassword;
	private String userRole; 
	private boolean isActive;
	private boolean isDelete;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
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
//	public String getUserPassword() {
//		return userPassword;
//	}
//	public void setUserPassword(String userPassword) {
//		this.userPassword = userPassword;
//	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public void setDelete(boolean isDelete) {
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
		return "UserResponseDTO [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail
				+  ", userRole=" + userRole + ", isActive=" + isActive + ", isDelete="
				+ isDelete + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}



}
