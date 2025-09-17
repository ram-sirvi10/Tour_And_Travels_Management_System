package com.travelmanagement.service.impl;

import java.util.List;

import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.dao.impl.UserDAOImpl;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.exception.UserNotFoundException;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IUserService;
import com.travelmanagement.util.PasswordHashing;

public class UserServiceImpl implements IUserService {

	private AuthServiceImpl authService = new AuthServiceImpl();
	private IUserDAO userDAO = new UserDAOImpl();

	@Override
	public void register(RegisterRequestDTO dto) throws Exception {

		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new BadRequestException("Passwords do not match!");
		}
		User user = authService.mapRegisterDtoToUser(dto);

		user.setUserPassword(PasswordHashing.ecryptPassword(user.getUserPassword()));

		boolean created = userDAO.createUser(user);
		if (!created) {
			throw new BadRequestException("User registration failed!");
		} else
			return;

	}

	@Override
	public User login(LoginRequestDTO dto) throws Exception {

		User loginUser = authService.mapLoginDtoToUser(dto);

		User dbUser = userDAO.getUserByEmail(loginUser.getUserEmail());
		if (dbUser == null) {
			throw new UserNotFoundException("User not found!");
		}

		if (!PasswordHashing.checkPassword(loginUser.getUserPassword(), dbUser.getUserPassword())) {
			throw new BadRequestException("Invalid credentials!");
		}

		return dbUser;
	}

	@Override
	public User getById(int id) throws Exception {
		User user = userDAO.getUserById(id);
		if (user == null) {
			throw new UserNotFoundException("User not found with id: " + id);
		}
		return user;
	}

	@Override
	public User getByEmail(String email) throws Exception {
		User user = userDAO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}
		return user;
	}

	@Override
	public List<User> getAll() throws Exception {
		return userDAO.getAllUsers();
	}

	@Override
	public boolean update(User user) throws Exception {
		return userDAO.updateUser(user);
	}

	@Override
	public boolean delete(int id) throws Exception {
		return userDAO.deleteUser(id);
	}
}
