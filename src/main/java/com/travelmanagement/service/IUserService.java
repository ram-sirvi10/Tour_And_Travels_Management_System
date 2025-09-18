package com.travelmanagement.service;

import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;
import com.travelmanagement.model.User;
import java.util.List;

public interface IUserService {
	UserResponseDTO register(RegisterRequestDTO registerRequestDTO) throws Exception;

	UserResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception;

	UserResponseDTO getById(int id) throws Exception;

	UserResponseDTO getByEmail(String email) throws Exception;

	List<UserResponseDTO> getAll(int limit,int offset) throws Exception;

	boolean update(User user) throws Exception;

	boolean delete(int id) throws Exception;
}
