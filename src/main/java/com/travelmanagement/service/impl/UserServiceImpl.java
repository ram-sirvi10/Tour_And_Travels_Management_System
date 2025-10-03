package com.travelmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.dao.impl.UserDAOImpl;
import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.exception.BadRequestException;
import com.travelmanagement.exception.UserNotFoundException;
import com.travelmanagement.model.User;
import com.travelmanagement.service.IUserService;
import com.travelmanagement.util.Mapper;
import com.travelmanagement.util.PasswordHashing;

public class UserServiceImpl implements IUserService {

	private IUserDAO userDAO = new UserDAOImpl();

	@Override
	public UserResponseDTO register(RegisterRequestDTO dto) throws Exception {
		if (!dto.getPassword().equals(dto.getConfirmPassword())) {
			throw new BadRequestException("Passwords do not match!");
		}

		User user = Mapper.mapRegisterDtoToUser(dto);
		user.setUserPassword(PasswordHashing.ecryptPassword(user.getUserPassword()));

		boolean created = userDAO.createUser(user);
		if (!created) {
			throw new BadRequestException("User registration failed!");
		}

		return Mapper.mapUserToUserResponseDTO(user);
	}

	@Override
	public UserResponseDTO login(LoginRequestDTO dto) throws Exception {
		User loginUser = Mapper.mapLoginDtoToUser(dto);

		User dbUser = userDAO.getUserByEmail(loginUser.getUserEmail());
		if (dbUser == null) {
			throw new UserNotFoundException("User not found!");
		}
		System.out.println(dbUser.isActive());
		if (dbUser.isActive()!=true) {
			System.out.println(dbUser.isActive());
			throw new BadRequestException("Account is blocked! Please contact support.");
		}
		if (!PasswordHashing.checkPassword(loginUser.getUserPassword(), dbUser.getUserPassword())) {
			throw new BadRequestException("Invalid Password!");
		}

		return Mapper.mapUserToUserResponseDTO(dbUser);
	}

	@Override
	public UserResponseDTO getById(int id) throws Exception {
		User user = userDAO.getUserById(id);
		if (user == null) {
			throw new UserNotFoundException("User not found with id: " + id);
		}

		return Mapper.mapUserToUserResponseDTO(user);
	}

	@Override
	public UserResponseDTO getByEmail(String email) throws Exception {
		User user = userDAO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("User not found with email: " + email);
		}

		return Mapper.mapUserToUserResponseDTO(user);
	}

	@Override
	public List<UserResponseDTO> getAll(Boolean active, Boolean deleted, String keyword, int limit, int offset)
			throws Exception {
		List<User> users = userDAO.getAllUsers(active, deleted, keyword, limit, offset);
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (User user : users) {
			responseList.add(Mapper.mapUserToUserResponseDTO(user));
		}
		return responseList;
	}

	@Override
	public boolean update(RegisterRequestDTO dto) throws Exception {
		User user = Mapper.mapRegisterDtoToUser(dto);
		if (user.getUserPassword() != null && !user.getUserPassword().isEmpty()) {
			user.setUserPassword(PasswordHashing.ecryptPassword(user.getUserPassword()));
		}
		return userDAO.updateUser(user);
	}

	@Override
	public boolean changePassword(int userId, String newPassword) throws Exception {
		String hashedPassword = PasswordHashing.ecryptPassword(newPassword);
		return userDAO.changePassword(userId, hashedPassword);
	}

	@Override
	public boolean delete(int id) throws Exception {
		User user = userDAO.getUserById(id);
		if (user == null) {
			throw new UserNotFoundException("USER NOT FOUND ! ");
		} else if (user.isDelete()) {
			throw new UserNotFoundException("USER NOT FOUND ! ");
		}
		return userDAO.deleteUser(id);
	}

	@Override
	public boolean updateUserActiveState(int userId, boolean active) throws Exception {
		User user = userDAO.getUserById(userId);
		if (user == null) {
			throw new UserNotFoundException("USER NOT FOUND ! ");
		} else if (user.isDelete()) {
			throw new UserNotFoundException("USER NOT FOUND ! ");
		} else if (user.isActive() == active) {
			String state = "ACTIVE";
			if (!active) {
				state = "INACTIVE";
			}
			throw new BadRequestException("USER ALREADY -> " + state);
		}
		return userDAO.updateUserActiveState(userId, active);
	}

	@Override
	public List<UserResponseDTO> getDeletedUsers(String keyword, int limit, int offset) throws Exception {
		List<User> users = userDAO.getAllUsers(null, true, keyword, limit, offset);
		List<UserResponseDTO> responseList = new ArrayList<>();
		for (User user : users) {
			responseList.add(Mapper.mapUserToUserResponseDTO(user));
		}
		return responseList;
	}

	@Override
	public long countUser(Boolean active, Boolean deleted, String keyword) throws Exception {
		return userDAO.countUser(active, deleted, keyword);
	}

}
