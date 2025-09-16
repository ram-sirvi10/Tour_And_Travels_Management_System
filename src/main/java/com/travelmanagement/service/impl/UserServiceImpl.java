package com.travelmanagement.service.impl;

import java.util.List;

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

	@Override
	public User register(User user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(String email, String password) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getByEmail(String email) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(User user) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
