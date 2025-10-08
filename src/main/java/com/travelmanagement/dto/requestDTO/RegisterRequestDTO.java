package com.travelmanagement.dto.requestDTO;

public class RegisterRequestDTO {
	private String username;
	private String password;
	private String confirmpassword;
	private String email;
	private Integer userId;
	private String imageurl;
	
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getConfirmPassword() {
		return confirmpassword;
	}
	public void setConfirmPassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	@Override
	public String toString() {
		return "RegisterRequestDTO [username=" + username + ", password=" + password + ", confirmpassword="
				+ confirmpassword + ", email=" + email + "]";
	}
	

	
	
}


