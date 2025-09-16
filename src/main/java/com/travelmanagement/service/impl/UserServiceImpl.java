package com.travelmanagement.service.impl;

import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IUserService;
public class UserServiceImpl  implements IUserService{
	
	public User mapRegisterDtoToUser(RegisterRequestDTO request)
	{
		 User user = new User();
		 user.setUserName(request.getUsername());
		 return user;
	}

}
