package com.travelmanagement.service;

import com.travelmanagement.model.User;
import java.util.List;

public interface IUserService {
	User register(User user) throws Exception;

	User login(String email, String password) throws Exception;

	User getById(int id) throws Exception;

	User getByEmail(String email) throws Exception;

	List<User> getAll() throws Exception;

	boolean update(User user) throws Exception;

	boolean delete(int id) throws Exception;
}
