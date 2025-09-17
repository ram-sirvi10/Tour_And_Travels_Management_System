package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IUserDAO;
import com.travelmanagement.model.User;

public class UserDAOImpl implements IUserDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public boolean createUser(User user) throws Exception {

		try{
			connection = DatabaseConfig.getConnection();
			String sql = "INSERT INTO users (user_name, user_email, user_password) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getUserEmail());
			preparedStatement.setString(3, user.getUserPassword());
		

			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public User getUserById(int id) throws Exception {
		User user = null;
		try {
			connection = DatabaseConfig.getConnection();
			String sql = "SELECT * FROM users WHERE user_id=? AND is_delete=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setBoolean(2, false);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserEmail(resultSet.getString("user_email"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setUserRole(resultSet.getString("user_role"));
				user.setActive(resultSet.getBoolean("is_active"));
				user.setDelete(resultSet.getBoolean("is_delete"));
				if (resultSet.getDate("created_at") != null)
					user.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					user.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return user;
	}

	@Override
	public User getUserByEmail(String email) throws Exception {
		User user = null;
		try {
			connection = DatabaseConfig.getConnection();
			String sql = "SELECT * FROM users WHERE user_email=? AND is_delete=? ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setBoolean(2, false);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserEmail(resultSet.getString("user_email"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setUserRole(resultSet.getString("user_role"));
				user.setActive(resultSet.getBoolean("is_active"));
				user.setDelete(resultSet.getBoolean("is_delete"));
				
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return user;
	}

	@Override
	public List<User> getAllUsers() throws Exception {
		List<User> users = new ArrayList<>();
		try {
			connection = DatabaseConfig.getConnection();
			String sql = "SELECT * FROM users WHERE is_delete= ? ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, false);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUserName(resultSet.getString("user_name"));
				user.setUserEmail(resultSet.getString("user_email"));
				user.setUserPassword(resultSet.getString("user_password"));
				user.setUserRole(resultSet.getString("user_role"));
				user.setActive(resultSet.getBoolean("is_active"));
				user.setDelete(resultSet.getBoolean("is_delete"));
				if (resultSet.getDate("created_at") != null)
					user.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					user.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
				users.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return users;
	}

	@Override
	public boolean updateUser(User user) throws Exception {

		try {
			connection = DatabaseConfig.getConnection();
			String sql = "UPDATE users SET user_name=?, user_email=?, user_password=? , WHERE user_id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getUserEmail());
			preparedStatement.setString(3, user.getUserPassword());

			preparedStatement.setInt(4, user.getUserId());

			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public boolean deleteUser(int id) throws Exception {

		try {
			connection = DatabaseConfig.getConnection();
			String sql = "UPDATE users SET is_delete=? WHERE user_id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setInt(2, id);

			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public boolean authenticate(String email, String password) throws Exception {

	
		return false;
	}
}
