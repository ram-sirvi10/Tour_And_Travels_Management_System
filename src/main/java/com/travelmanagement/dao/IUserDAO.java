package com.travelmanagement.dao;

import com.travelmanagement.model.User;
import java.util.List;

public interface IUserDAO {
	boolean createUser(User user) throws Exception;

	User getUserById(int id) throws Exception;

	User getUserByEmail(String email) throws Exception;

	List<User> getAllUsers() throws Exception;

	boolean updateUser(User user) throws Exception;

	boolean deleteUser(int id) throws Exception;

	boolean authenticate(String email, String password) throws Exception;
}
