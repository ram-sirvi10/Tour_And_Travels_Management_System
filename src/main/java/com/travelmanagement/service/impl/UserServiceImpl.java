package com.travelmanagement.service.impl;

import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.model.User;
public class UserServiceImpl  {
	
	public User mapRegisterDtoToUser(RegisterRequestDTO request)
	{
		 User user = new User();
		 user.setUserName(request.getUsername());
		 return user;
	}

}
