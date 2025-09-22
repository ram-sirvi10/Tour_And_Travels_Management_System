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

	public UserDAOImpl() {
		this.connection = DatabaseConfig.getConnection();
	}

	@Override
	public boolean createUser(User user) throws Exception {

		try {
//			connection = DatabaseConfig.getConnection();
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
//			connection = DatabaseConfig.getConnection();
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
	public List<User> getAllUsers(Boolean active, Boolean deleted, String keyword, int limit, int offset)
			throws Exception {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE user_role = ? AND is_delete = ?";

		if (active != null) {
			sql += " AND is_active = ?";
		}

		if (keyword != null && !keyword.isEmpty()) {
			sql += " AND (user_name LIKE ? OR user_email LIKE ?)";
		}

		sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";

		preparedStatement = connection.prepareStatement(sql);
		int index = 1;
		preparedStatement.setString(index++, "USER");
		preparedStatement.setBoolean(index++, deleted != null ? deleted : false);

		if (active != null) {
			preparedStatement.setBoolean(index++, active);
		}

		if (keyword != null && !keyword.isEmpty()) {
			String likeKeyword = "%" + keyword + "%";
			preparedStatement.setString(index++, likeKeyword);
			preparedStatement.setString(index++, likeKeyword);
		}

		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index++, offset);

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

		return users;
	}

	@Override
	public boolean updateUser(User user) throws Exception {

		try {

			String sql = "UPDATE users SET user_name=?, user_email=? WHERE user_id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getUserName());
			preparedStatement.setString(2, user.getUserEmail());
			preparedStatement.setInt(3, user.getUserId());

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
			String sql = "UPDATE users SET is_delete = ?, is_active = ? WHERE user_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setBoolean(2, false);
			preparedStatement.setInt(3, id);

			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateUserActiveState(int userId, boolean active) throws Exception {
		try {
			String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, active);
			preparedStatement.setInt(2, userId);

			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changePassword(int userId, String newPassword) throws Exception {
		String sql = "UPDATE USERS SET user_password=?  WHERE user_id=?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPassword);
			preparedStatement.setInt(2, userId);

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
	public List<User> getDeletedUsers(int limit, int offset) throws Exception {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE user_role = ? AND is_delete = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, "USER");
		preparedStatement.setBoolean(2, true); // fetch deleted users
		preparedStatement.setInt(3, limit);
		preparedStatement.setInt(4, offset);

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
		return users;
	}

	@Override
	public long countUser(Boolean active, Boolean deleted, String keyword) {
		int total = 0;
		String sql = "SELECT COUNT(*) AS total FROM users where user_role = ? AND is_delete = ?";

		if (active != null) {
			sql += " AND is_active = ?";
		}

		if (keyword != null && !keyword.isEmpty()) {
			sql += " AND (user_name LIKE ? OR user_email LIKE ?)";
		}

		try {
			preparedStatement = connection.prepareStatement(sql);
			int index = 1;
			preparedStatement.setString(index++, "USER");
			preparedStatement.setBoolean(index++, deleted != null ? deleted : false);

			if (active != null) {
				preparedStatement.setBoolean(index++, active);
			}

			if (keyword != null && !keyword.isEmpty()) {
				String likeKeyword = "%" + keyword + "%";
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
			}
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				total = resultSet.getInt("total");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return total;
	}

	@Override
	public List<User> getUsersByState(Boolean active, int limit, int offset) throws Exception {
		List<User> users = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE user_role = ? AND is_delete = ?";

		if (active != null) {
			sql += " AND is_active = ?";
		}

		sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";

		preparedStatement = connection.prepareStatement(sql);
		int index = 1;
		preparedStatement.setString(index++, "USER");
		preparedStatement.setBoolean(index++, false);

		if (active != null) {
			preparedStatement.setBoolean(index++, active);
		}

		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index++, offset);

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

		return users;
	}

}
