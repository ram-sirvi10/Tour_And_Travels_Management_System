package com.travelmanagement.dao.impl;

import java.util.List;

import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.model.User;

public class UserDAOImpl implements IUserDAO{

	@Override
	public int createUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User getUserById(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateUser(User user) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User authenticate(String email, String password) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
