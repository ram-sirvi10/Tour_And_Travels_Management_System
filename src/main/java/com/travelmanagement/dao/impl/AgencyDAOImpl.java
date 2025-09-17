package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.model.Agency;

public class AgencyDAOImpl implements IAgencyDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public boolean createAgency(Agency agency) throws Exception {
		String sql = "INSERT INTO travelAgency "
				+ "(agency_name, owner_name, email, phone, city, state, country, pincode, registration_number, password) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			connection = DatabaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, agency.getAgencyName());
			preparedStatement.setString(2, agency.getOwnerName());
			preparedStatement.setString(3, agency.getEmail());
			preparedStatement.setString(4, agency.getPhone());
			preparedStatement.setString(5, agency.getCity());
			preparedStatement.setString(6, agency.getState());
			preparedStatement.setString(7, agency.getCountry());
			preparedStatement.setString(8, agency.getPincode());
			preparedStatement.setString(9, agency.getRegistrationNumber());
			preparedStatement.setString(10, agency.getPassword());

			int rowsInserted = preparedStatement.executeUpdate();
			return rowsInserted > 0;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public Agency getAgencyByEmail(String email) throws Exception {
		String sql = "SELECT * FROM travelAgency WHERE email=? AND is_delete=? AND status = ?";
		try {
			connection = DatabaseConfig.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setBoolean(2, false);
			preparedStatement.setString(3, "APPROVED");
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Agency agency = new Agency();
				agency.setAgencyId(resultSet.getInt("agency_id"));
				agency.setAgencyName(resultSet.getString("agency_name"));
				agency.setOwnerName(resultSet.getString("owner_name"));
				agency.setEmail(resultSet.getString("email"));
				agency.setPhone(resultSet.getString("phone"));
				agency.setCity(resultSet.getString("city"));
				agency.setState(resultSet.getString("state"));
				agency.setCountry(resultSet.getString("country"));
				agency.setPincode(resultSet.getString("pincode"));
				agency.setRegistrationNumber(resultSet.getString("registration_number"));
				agency.setPassword(resultSet.getString("password"));
				return agency;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
