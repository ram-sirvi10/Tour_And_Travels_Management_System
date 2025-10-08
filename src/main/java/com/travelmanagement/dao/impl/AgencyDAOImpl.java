package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IAgencyDAO;
import com.travelmanagement.model.Agency;

public class AgencyDAOImpl implements IAgencyDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public AgencyDAOImpl() {
		this.connection = DatabaseConfig.getConnection();
	}

	@Override
	public boolean createAgency(Agency agency) throws Exception {
		try {
			String sql = "INSERT INTO travelAgency (agency_name, owner_name, email, phone, city, state, country, pincode, registration_number, password, is_active, is_delete,imageurl,area) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?,?)";
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
			preparedStatement.setBoolean(11, false);
			preparedStatement.setBoolean(12, true);
			preparedStatement.setObject(13, agency.getImageurl());
			preparedStatement.setString(14, agency.getArea());
			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Agency getAgencyByField(String fieldName, String value) throws Exception {
		if (!fieldName.equalsIgnoreCase("email") && !fieldName.equalsIgnoreCase("registration_number")) {
			throw new IllegalArgumentException("Invalid field: " + fieldName);
		}

		String sql = "SELECT * FROM travelAgency " + "WHERE " + fieldName + "=? "
				+ "ORDER BY CASE WHEN status=? THEN 1  WHEN status=? THEN 2 ELSE 3 END,  created_at DESC LIMIT 1";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, value);
			preparedStatement.setString(2, "PENDING");
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
				agency.setActive(resultSet.getBoolean("is_active"));
				agency.setDelete(resultSet.getBoolean("is_delete"));
				agency.setStatus(resultSet.getString("status"));
				agency.setArea(resultSet.getString("area"));

				if (resultSet.getString("imageurl") != null)
					agency.setImageurl(resultSet.getString("imageurl"));
				if (resultSet.getDate("created_at") != null)
					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
				return agency;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return null;
	}

	@Override
	public Agency getAgencyById(Integer id) throws Exception {
		Agency agency = null;
		try {
			String sql = "SELECT * FROM travelAgency WHERE agency_id=? ";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
//			preparedStatement.setBoolean(2, false);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				agency = new Agency();
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
				agency.setActive(resultSet.getBoolean("is_active"));
				agency.setDelete(resultSet.getBoolean("is_delete"));
				agency.setStatus(resultSet.getString("status"));
				agency.setArea(resultSet.getString("area"));
				if (resultSet.getString("imageurl") != null)
					agency.setImageurl(resultSet.getString("imageurl"));
				if (resultSet.getDate("created_at") != null)
					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agency;
	}

	@Override
	public boolean updateAgency(Agency agency) throws Exception {
		try {

			String sql = "UPDATE travelAgency SET agency_name=?, owner_name=?, phone=?, city=?, state=?, country=?, pincode=?, imageurl = ? , area=? WHERE agency_id=?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, agency.getAgencyName());
			preparedStatement.setString(2, agency.getOwnerName());
			preparedStatement.setString(3, agency.getPhone());
			preparedStatement.setString(4, agency.getCity());
			preparedStatement.setString(5, agency.getState());
			preparedStatement.setString(6, agency.getCountry());
			preparedStatement.setString(7, agency.getPincode());
			preparedStatement.setObject(8, agency.getImageurl());
			preparedStatement.setString(9, agency.getArea());
			preparedStatement.setInt(10, agency.getAgencyId());
			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changePassword(Integer agencyId, String newPassword) throws Exception {
		String sql = "UPDATE travelagency SET password=?  WHERE agency_id=?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPassword);
			preparedStatement.setInt(2, agencyId);

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
	public boolean deleteAgency(Integer id) throws Exception {
		try {

			String sql = "UPDATE travelAgency SET is_delete=? , is_active=? WHERE agency_id=?";
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
	public boolean updateAgencyStatus(Integer agencyId, String status) throws Exception {
		try {
			String sql = "UPDATE travelAgency SET status = ?, is_active = ?, is_delete = ? WHERE agency_id = ?";

			preparedStatement = connection.prepareStatement(sql);
			boolean is_active = false;
			boolean is_delete = true;
			if ("APPROVED".equalsIgnoreCase(status)) {
				is_active = true;
				is_delete = false;
			}

			preparedStatement.setString(1, status.toUpperCase());
			preparedStatement.setBoolean(2, is_active);
			preparedStatement.setBoolean(3, is_delete);
			preparedStatement.setInt(4, agencyId);

			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public boolean updateAgencyActiveState(Integer agencyId, Boolean active) throws Exception {
		try {
			String sql = "UPDATE travelAgency SET is_active=? WHERE agency_id=? AND is_delete=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, active);
			preparedStatement.setInt(2, agencyId);
			preparedStatement.setBoolean(3, false);

			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

//	@Override
//	public List<Agency> searchAgenciesByKeyword(String keyword, int limit, int offset) throws Exception {
//		List<Agency> agencies = new ArrayList<>();
//
//		String sql = "SELECT * FROM travelAgency WHERE is_delete = false AND ("
//				+ "agency_name LIKE ? OR owner_name LIKE ? OR email LIKE ? OR phone LIKE ? "
//				+ "OR city LIKE ? OR state LIKE ? OR country LIKE ? OR pincode LIKE ? OR registration_number LIKE ?) "
//				+ "ORDER BY created_at DESC LIMIT ? OFFSET ?";
//		if (keyword != null) {
//
//		}
//
//		try {
//			preparedStatement = connection.prepareStatement(sql);
//			String likeKeyword = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
//
//			for (int i = 1; i <= 9; i++)
//				preparedStatement.setString(i, likeKeyword);
//			preparedStatement.setInt(10, limit);
//			preparedStatement.setInt(11, offset);
//
//			resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				Agency agency = new Agency();
//				agency.setAgencyId(resultSet.getInt("agency_id"));
//				agency.setAgencyName(resultSet.getString("agency_name"));
//				agency.setOwnerName(resultSet.getString("owner_name"));
//				agency.setEmail(resultSet.getString("email"));
//				agency.setPhone(resultSet.getString("phone"));
//				agency.setCity(resultSet.getString("city"));
//				agency.setState(resultSet.getString("state"));
//				agency.setCountry(resultSet.getString("country"));
//				agency.setPincode(resultSet.getString("pincode"));
//				agency.setRegistrationNumber(resultSet.getString("registration_number"));
//				agency.setStatus(resultSet.getString("status"));
//				agency.setActive(resultSet.getBoolean("is_active"));
//				agency.setDelete(resultSet.getBoolean("is_delete"));
//				agency.setArea(resultSet.getString("area"));
//				if (resultSet.getString("imageurl") != null)
//					agency.setImageurl(resultSet.getString("imageurl"));
//				if (resultSet.getDate("created_at") != null)
//					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
//				if (resultSet.getDate("updated_at") != null)
//					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
//
//				agencies.add(agency);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return agencies;
//	}

//	@Override
//	public List<Agency> getAgenciesByStatus(String status, int limit, int offset) throws Exception {
//		List<Agency> list = new ArrayList<>();
//		try {
//
//			String sql = "SELECT * FROM travelAgency  WHERE  status = ? limit ? offset ?";
//			preparedStatement = connection.prepareStatement(sql);
//
//			preparedStatement.setString(1, status);
//			preparedStatement.setInt(2, limit);
//			preparedStatement.setInt(3, offset);
//			resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				Agency agency = new Agency();
//				agency.setAgencyId(resultSet.getInt("agency_id"));
//				agency.setAgencyName(resultSet.getString("agency_name"));
//				agency.setOwnerName(resultSet.getString("owner_name"));
//				agency.setEmail(resultSet.getString("email"));
//				agency.setPhone(resultSet.getString("phone"));
//				agency.setCity(resultSet.getString("city"));
//				agency.setState(resultSet.getString("state"));
//				agency.setCountry(resultSet.getString("country"));
//				agency.setPincode(resultSet.getString("pincode"));
//				agency.setRegistrationNumber(resultSet.getString("registration_number"));
//				agency.setPassword(resultSet.getString("password"));
//				agency.setActive(resultSet.getBoolean("is_active"));
//				agency.setDelete(resultSet.getBoolean("is_delete"));
//				agency.setStatus(resultSet.getString("status"));
//				agency.setArea(resultSet.getString("area"));
//				if (resultSet.getString("imageurl") != null)
//					agency.setImageurl(resultSet.getString("imageurl"));
//				if (resultSet.getDate("created_at") != null)
//					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
//				if (resultSet.getDate("updated_at") != null)
//					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
//
//				list.add(agency);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}

//	@Override
//	public List<Agency> getAgenciesByActiveState(Boolean isActive, int limit, int offset) throws Exception {
//		List<Agency> agencies = new ArrayList<>();
//		try {
//			String sql = "SELECT * FROM travelAgency WHERE is_active = ?  LIMIT ? OFFSET ?";
//			preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setBoolean(1, isActive);
//
//			preparedStatement.setInt(2, limit);
//			preparedStatement.setInt(3, offset);
//
//			resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				Agency agency = new Agency();
//				agency.setAgencyId(resultSet.getInt("agency_id"));
//				agency.setAgencyName(resultSet.getString("agency_name"));
//				agency.setOwnerName(resultSet.getString("owner_name"));
//				agency.setEmail(resultSet.getString("email"));
//				agency.setPhone(resultSet.getString("phone"));
//				agency.setCity(resultSet.getString("city"));
//				agency.setState(resultSet.getString("state"));
//				agency.setCountry(resultSet.getString("country"));
//				agency.setPincode(resultSet.getString("pincode"));
//				agency.setRegistrationNumber(resultSet.getString("registration_number"));
//				agency.setStatus(resultSet.getString("status"));
//				agency.setActive(resultSet.getBoolean("is_active"));
//				agency.setDelete(resultSet.getBoolean("is_delete"));
//				agency.setArea(resultSet.getString("area"));
//				if (resultSet.getString("imageurl") != null)
//					agency.setImageurl(resultSet.getString("imageurl"));
//				if (resultSet.getDate("created_at") != null)
//					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
//				if (resultSet.getDate("updated_at") != null)
//					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
//				agencies.add(agency);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return agencies;
//	}

//	@Override
//	public List<Agency> getAllAgencies(int limit, int offset) {
//		List<Agency> agencies = new ArrayList<>();
//		try {
//
//			String sql = "SELECT * FROM travelAgency WHERE is_delete=? and status = ? LIMIT ? OFFSET ?";
//			preparedStatement = connection.prepareStatement(sql);
//			preparedStatement.setBoolean(1, false);
//			preparedStatement.setString(2, "APPROVED");
//			preparedStatement.setInt(3, limit);
//			preparedStatement.setInt(4, offset);
//			resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				Agency agency = new Agency();
//				agency.setAgencyId(resultSet.getInt("agency_id"));
//				agency.setAgencyName(resultSet.getString("agency_name"));
//				agency.setOwnerName(resultSet.getString("owner_name"));
//				agency.setEmail(resultSet.getString("email"));
//				agency.setPhone(resultSet.getString("phone"));
//				agency.setCity(resultSet.getString("city"));
//				agency.setState(resultSet.getString("state"));
//				agency.setCountry(resultSet.getString("country"));
//				agency.setPincode(resultSet.getString("pincode"));
//				agency.setRegistrationNumber(resultSet.getString("registration_number"));
//				agency.setPassword(resultSet.getString("password"));
//				agency.setActive(resultSet.getBoolean("is_active"));
//				agency.setDelete(resultSet.getBoolean("is_delete"));
//				agency.setArea(resultSet.getString("area"));
//				if (resultSet.getString("imageurl") != null)
//					agency.setImageurl(resultSet.getString("imageurl"));
//				if (resultSet.getDate("created_at") != null)
//					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
//				if (resultSet.getDate("updated_at") != null)
//					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
//				agencies.add(agency);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return agencies;
//	}

//	@Override
//	public List<Agency> getDeletedAgencies(String keyword, int limit, int offset) throws Exception {
//		List<Agency> list = new ArrayList<>();
//		try {
//			String sql = "SELECT * FROM travelAgency WHERE is_delete = true and status = 'APPROVED'";
//
//			if (keyword != null && !keyword.isEmpty()) {
//				sql += " AND (LOWER(agency_name) LIKE ? OR LOWER(owner_name) LIKE ? OR LOWER(email) LIKE ? "
//						+ "OR LOWER(phone) LIKE ? OR LOWER(city) LIKE ? OR LOWER(state) LIKE ? OR LOWER(country) LIKE ? "
//						+ "OR LOWER(pincode) LIKE ? OR LOWER(registration_number) LIKE ?)";
//			}
//
//			sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";
//
//			preparedStatement = connection.prepareStatement(sql);
//			int index = 1;
//
//			if (keyword != null && !keyword.isEmpty()) {
//				String likeKeyword = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
//
//				for (int i = 0; i < 9; i++) {
//					preparedStatement.setString(index++, likeKeyword);
//				}
//			}
//
//			preparedStatement.setInt(index++, limit);
//			preparedStatement.setInt(index++, offset);
//
//			resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				Agency agency = new Agency();
//				agency.setAgencyId(resultSet.getInt("agency_id"));
//				agency.setAgencyName(resultSet.getString("agency_name"));
//				agency.setOwnerName(resultSet.getString("owner_name"));
//				agency.setEmail(resultSet.getString("email"));
//				agency.setPhone(resultSet.getString("phone"));
//				agency.setCity(resultSet.getString("city"));
//				agency.setState(resultSet.getString("state"));
//				agency.setCountry(resultSet.getString("country"));
//				agency.setPincode(resultSet.getString("pincode"));
//				agency.setRegistrationNumber(resultSet.getString("registration_number"));
//				agency.setStatus(resultSet.getString("status"));
//				agency.setActive(resultSet.getBoolean("is_active"));
//				agency.setDelete(resultSet.getBoolean("is_delete"));
//				agency.setArea(resultSet.getString("area"));
//				if (resultSet.getString("imageurl") != null)
//					agency.setImageurl(resultSet.getString("imageurl"));
//				if (resultSet.getDate("created_at") != null)
//					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
//				if (resultSet.getDate("updated_at") != null)
//					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
//
//				list.add(agency);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}

	@Override
	public long countAgencies(String status, Boolean activeState, Boolean isDeleted, String keyword, String startDate,
			String endDate) throws Exception {
		long total = 0;
		String sql = "SELECT COUNT(*) AS total FROM travelAgency WHERE 1=1";

		if (status != null && !status.isEmpty()) {
			sql += " AND status = ?";
		} else
			sql += " AND status = 'APPROVED'";

		if (activeState != null) {
			sql += " AND is_active = ?";
		}
		if (isDeleted != null) {
			sql += " AND is_delete = ?";
		}
		if (startDate != null && !startDate.isEmpty()) {
			sql += " AND DATE(created_at) >= ?";
		}
		if (endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(created_at) <= ?";
		}

		if (keyword != null && !keyword.isEmpty()) {
			sql += " AND (agency_name LIKE ? OR owner_name LIKE ? OR email LIKE ? "
					+ "OR phone LIKE ? OR city LIKE ? OR state LIKE ? OR country LIKE ?)";
		}

		try {
			preparedStatement = connection.prepareStatement(sql);
			int index = 1;

			if (status != null && !status.isEmpty()) {
				preparedStatement.setString(index++, status);
			}
			if (activeState != null) {
				preparedStatement.setBoolean(index++, activeState);
			}
			if (isDeleted != null) {
				preparedStatement.setBoolean(index++, isDeleted);
			}
			if (startDate != null && !startDate.isEmpty()) {
				preparedStatement.setDate(index++, Date.valueOf(startDate));
			}
			if (endDate != null && !endDate.isEmpty()) {
				preparedStatement.setDate(index++, Date.valueOf(endDate));
			}
			if (keyword != null && !keyword.isEmpty()) {
				String likeKeyword = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
				for (int i = 0; i < 7; i++) {
					preparedStatement.setString(index++, likeKeyword);
				}
			}

			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					total = rs.getLong("total");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return total;
	}

	@Override
	public List<Agency> filterAgencies(String status, Boolean active, String startDate, String endDate, String keyword,
			Boolean isDeleted, int limit, int offset) throws Exception {

		List<Agency> agencies = new ArrayList<>();

		String sql = "SELECT * FROM travelAgency WHERE 1=1";

		if (status != null && !status.isEmpty()) {
			sql += " AND status = ?";
		} else {
			sql += " AND status = 'APPROVED'";
		}

		if (active != null) {
			sql += " AND is_active = ?";
		}

		if (startDate != null && !startDate.isEmpty()) {
			sql += " AND DATE(created_at) >= ?";
		}

		if (endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(created_at) <= ?";
		}

		if (isDeleted != null) {
			sql += " AND is_delete = ?";
		} else {
			sql += " AND is_delete = false";
		}

		if (keyword != null && !keyword.isEmpty()) {
			sql += " AND (LOWER(agency_name) LIKE ? OR LOWER(owner_name) LIKE ? OR LOWER(email) LIKE ? "
					+ "OR LOWER(phone) LIKE ? OR LOWER(city) LIKE ? OR LOWER(state) LIKE ? OR LOWER(country) LIKE ? "
					+ "OR LOWER(pincode) LIKE ? OR LOWER(registration_number) LIKE ?)";
		}

		sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";

		try {
			preparedStatement = connection.prepareStatement(sql);
			int index = 1;

			// Set parameters
			if (status != null && !status.isEmpty()) {
				preparedStatement.setString(index++, status);
			}
			if (active != null) {
				preparedStatement.setBoolean(index++, active);
			}
			if (startDate != null && !startDate.isEmpty()) {
				preparedStatement.setDate(index++, Date.valueOf(startDate));
			}
			if (endDate != null && !endDate.isEmpty()) {
				preparedStatement.setDate(index++, Date.valueOf(endDate));
			}
			if (isDeleted != null) {
				preparedStatement.setBoolean(index++, isDeleted);
			}
			if (keyword != null && !keyword.isEmpty()) {
				String likeKeyword = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";

				for (int i = 0; i < 9; i++) {
					preparedStatement.setString(index++, likeKeyword);
				}
			}

			preparedStatement.setInt(index++, limit);
			preparedStatement.setInt(index++, offset);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
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
				agency.setStatus(resultSet.getString("status"));
				agency.setActive(resultSet.getBoolean("is_active"));
				agency.setDelete(resultSet.getBoolean("is_delete"));
				agency.setImageurl(resultSet.getString("imageurl"));
				agency.setArea(resultSet.getString("area"));
				if (resultSet.getDate("created_at") != null)
					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());

				agencies.add(agency);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agencies;
	}

}
