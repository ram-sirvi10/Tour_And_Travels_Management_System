package com.travelmanagement.service;

import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.model.User;
import java.util.List;

public interface IUserService {
	void register(RegisterRequestDTO registerRequestDTO) throws Exception;

	User login(LoginRequestDTO loginRequestDTO) throws Exception;

	User getById(int id) throws Exception;

	User getByEmail(String email) throws Exception;

	List<User> getAll() throws Exception;

	boolean update(User user) throws Exception;

	boolean delete(int id) throws Exception;
}
