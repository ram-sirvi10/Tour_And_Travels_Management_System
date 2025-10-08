package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.User;

public interface IUserDAO {
	Boolean createUser(User user) throws Exception;

	User getUserById(Integer id) throws Exception;

	User getUserByEmail(String email) throws Exception;

	Boolean updateUser(User user) throws Exception;

	Boolean deleteUser(Integer id) throws Exception;

	Boolean changePassword(Integer userId, String newPassword) throws Exception;

	Boolean updateUserActiveState(Integer userId, Boolean active) throws Exception;

	List<User> getDeletedUsers(int limit, int offset) throws Exception;

	List<User> getUsersByState(Boolean active, int limit, int offset) throws Exception;

	List<User> getAllUsers(Boolean active, Boolean deleted, String keyword, int limit, int offset) throws Exception;

	long countUser(Boolean active, Boolean deleted, String keyword);
}
