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
        if (!dbUser.isActive()) {
            throw new BadRequestException("Account is blocked! Please contact support.");
        }
        if (!PasswordHashing.checkPassword(loginUser.getUserPassword(), dbUser.getUserPassword())) {
            throw new BadRequestException("Invalid credentials!");
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
    public List<UserResponseDTO> getAll(int limit,int offset) throws Exception {
        List<User> users = userDAO.getAllUsers(limit,offset);
        List<UserResponseDTO> responseList = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO dto = Mapper.mapUserToUserResponseDTO(user);
            responseList.add(dto);
        }

        return responseList;}

    @Override
    public boolean update(User user) throws Exception {
        return userDAO.updateUser(user);
    }

    @Override
    public boolean delete(int id) throws Exception {
        return userDAO.deleteUser(id);
    }
   
    public boolean updateUserActiveState(int userId, boolean active) throws Exception {
        return userDAO.updateUserActiveState(userId, active);
    }
}
