package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.User;

public interface IUserDAO {
	boolean createUser(User user) throws Exception;

	User getUserById(int id) throws Exception;

	User getUserByEmail(String email) throws Exception;

	boolean updateUser(User user) throws Exception;

	boolean deleteUser(int id) throws Exception;

	boolean changePassword(int userId, String newPassword) throws Exception;

	List<User> searchUsers(String Keyword ,int limit, int offset ) throws Exception;

	List<User> getAllUsers(int limit, int offset) throws Exception;

	boolean updateUserActiveState(int userId, boolean active) throws Exception;

}
