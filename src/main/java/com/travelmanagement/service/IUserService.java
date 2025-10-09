package com.travelmanagement.service;

import java.util.List;

import com.travelmanagement.dto.requestDTO.LoginRequestDTO;
import com.travelmanagement.dto.requestDTO.RegisterRequestDTO;
import com.travelmanagement.dto.responseDTO.UserResponseDTO;

public interface IUserService {
	UserResponseDTO register(RegisterRequestDTO registerRequestDTO) throws Exception;

	UserResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception;

	UserResponseDTO getById(Integer id) throws Exception;

	UserResponseDTO getByEmail(String email) throws Exception;

	boolean delete(Integer id) throws Exception;

	long countUser(Boolean active, Boolean deleted, String keyword) throws Exception;

	List<UserResponseDTO> getAll(Boolean active, Boolean deleted, String keyword, int limit, int offset)
			throws Exception;

	boolean changePassword(Integer userId, String newPassword) throws Exception;

	boolean update(RegisterRequestDTO dto) throws Exception;

	boolean updateUserActiveState(Integer userId, Boolean active) throws Exception;

	List<UserResponseDTO> getDeletedUsers(String keyword, int limit, int offset) throws Exception;
}
