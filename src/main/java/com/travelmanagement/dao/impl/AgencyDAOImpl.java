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
			String sql = "INSERT INTO travelAgency (agency_name, owner_name, email, phone, city, state, country, pincode, registration_number, password, is_active, is_delete) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, agency.getAgencyName());
			ps.setString(2, agency.getOwnerName());
			ps.setString(3, agency.getEmail());
			ps.setString(4, agency.getPhone());
			ps.setString(5, agency.getCity());
			ps.setString(6, agency.getState());
			ps.setString(7, agency.getCountry());
			ps.setString(8, agency.getPincode());
			ps.setString(9, agency.getRegistrationNumber());
			ps.setString(10, agency.getPassword());
			ps.setBoolean(11, false);
			ps.setBoolean(12, true);

			int affectedRows = ps.executeUpdate();
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

		String sql = "SELECT * FROM travelAgency " + "WHERE " + fieldName + "=? AND is_delete=false "
				+ "ORDER BY CASE WHEN status=? THEN 1 ELSE 2 END, updated_at DESC, created_at DESC LIMIT 1";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, value);
			ps.setString(2, "PENDING");

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Agency agency = new Agency();
					agency.setAgencyId(rs.getInt("agency_id"));
					agency.setAgencyName(rs.getString("agency_name"));
					agency.setOwnerName(rs.getString("owner_name"));
					agency.setEmail(rs.getString("email"));
					agency.setPhone(rs.getString("phone"));
					agency.setCity(rs.getString("city"));
					agency.setState(rs.getString("state"));
					agency.setCountry(rs.getString("country"));
					agency.setPincode(rs.getString("pincode"));
					agency.setRegistrationNumber(rs.getString("registration_number"));
					agency.setPassword(rs.getString("password"));
					agency.setActive(rs.getBoolean("is_active"));
					agency.setDelete(rs.getBoolean("is_delete"));
					agency.setStatus(rs.getString("status"));
					if (rs.getDate("created_at") != null)
						agency.setCreatedAt(rs.getDate("created_at").toLocalDate());
					if (rs.getDate("updated_at") != null)
						agency.setUpdatedAt(rs.getDate("updated_at").toLocalDate());
					return agency;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return null;
	}

	@Override
	public Agency getAgencyById(int id) throws Exception {
		Agency agency = null;
		try {
			String sql = "SELECT * FROM travelAgency WHERE agency_id=? AND is_delete=?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setBoolean(2, false);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				agency = new Agency();
				agency.setAgencyId(rs.getInt("agency_id"));
				agency.setAgencyName(rs.getString("agency_name"));
				agency.setOwnerName(rs.getString("owner_name"));
				agency.setEmail(rs.getString("email"));
				agency.setPhone(rs.getString("phone"));
				agency.setCity(rs.getString("city"));
				agency.setState(rs.getString("state"));
				agency.setCountry(rs.getString("country"));
				agency.setPincode(rs.getString("pincode"));
				agency.setRegistrationNumber(rs.getString("registration_number"));
				agency.setPassword(rs.getString("password"));
				agency.setActive(rs.getBoolean("is_active"));
				agency.setDelete(rs.getBoolean("is_delete"));
				if (rs.getDate("created_at") != null)
					agency.setCreatedAt(rs.getDate("created_at").toLocalDate());
				if (rs.getDate("updated_at") != null)
					agency.setUpdatedAt(rs.getDate("updated_at").toLocalDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agency;
	}

	@Override
	public boolean updateAgency(Agency agency) throws Exception {
		try {

			String sql = "UPDATE travelAgency SET agency_name=?, owner_name=?, email=?, phone=?, city=?, state=?, country=?, pincode=?, registration_number=?, password=? WHERE agency_id=?";
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
			preparedStatement.setInt(11, agency.getAgencyId());

			int affectedRows = preparedStatement.executeUpdate();
			return affectedRows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAgency(int id) throws Exception {
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
	public boolean updateAgencyStatus(int agencyId, String status) throws Exception {
		try {
			String sql = "UPDATE travelAgency SET status = ?, is_active = ?, is_delete = ? WHERE agency_id = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateAgencyActiveState(int agencyId, boolean active) throws Exception {
		try {
			String sql = "UPDATE travelAgency SET is_active=? WHERE agency_id=? AND is_delete=?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setBoolean(1, active);
				preparedStatement.setInt(2, agencyId);
				preparedStatement.setBoolean(3, false);

				int affectedRows = preparedStatement.executeUpdate();
				return affectedRows > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Agency> searchAgenciesByKeyword(String keyword, int limit, int offset) throws Exception {
	    List<Agency> agencies = new ArrayList<>();
	    if (keyword == null || keyword.isEmpty()) return agencies;

	    String sql = "SELECT * FROM travelAgency WHERE is_delete = false AND (" +
	                 "agency_name LIKE ? OR owner_name LIKE ? OR email LIKE ? OR phone LIKE ? " +
	                 "OR city LIKE ? OR state LIKE ? OR country LIKE ? OR pincode LIKE ? OR registration_number LIKE ?) " +
	                 "ORDER BY created_at DESC LIMIT ? OFFSET ?";

	    try (PreparedStatement ps = connection.prepareStatement(sql)) {
	        String likeKeyword = "%" + keyword + "%";
	        for (int i = 1; i <= 9; i++) ps.setString(i, likeKeyword); 
	        ps.setInt(10, limit);
	        ps.setInt(11, offset);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Agency agency = new Agency();
	                agency.setAgencyId(rs.getInt("agency_id"));
	                agency.setAgencyName(rs.getString("agency_name"));
	                agency.setOwnerName(rs.getString("owner_name"));
	                agency.setEmail(rs.getString("email"));
	                agency.setPhone(rs.getString("phone"));
	                agency.setCity(rs.getString("city"));
	                agency.setState(rs.getString("state"));
	                agency.setCountry(rs.getString("country"));
	                agency.setPincode(rs.getString("pincode"));
	                agency.setRegistrationNumber(rs.getString("registration_number"));
	                agency.setStatus(rs.getString("status"));
	                agency.setActive(rs.getBoolean("is_active"));
	                agency.setDelete(rs.getBoolean("is_delete"));
	                if (rs.getDate("created_at") != null) agency.setCreatedAt(rs.getDate("created_at").toLocalDate());
	                if (rs.getDate("updated_at") != null) agency.setUpdatedAt(rs.getDate("updated_at").toLocalDate());

	                agencies.add(agency);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return agencies;
	}


	@Override
	public List<Agency> getAgenciesByStatus(String status, int limit, int offset) throws Exception {
		List<Agency> list = new ArrayList<>();
		try {

			String sql = "SELECT * FROM travelAgency  WHERE  status = ? limit ? offset ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, limit);
			preparedStatement.setInt(3, offset);
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
				agency.setPassword(resultSet.getString("password"));
				agency.setActive(resultSet.getBoolean("is_active"));
				agency.setDelete(resultSet.getBoolean("is_delete"));
				agency.setStatus(resultSet.getString("status"));
				if (resultSet.getDate("created_at") != null)
					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());

				list.add(agency);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Agency> getAgenciesByActiveState(boolean isActive, int limit, int offset) throws Exception {
		List<Agency> agencies = new ArrayList<>();
		try {
			String sql = "SELECT * FROM travelAgency WHERE is_active = ?  LIMIT ? OFFSET ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, isActive);

			preparedStatement.setInt(2, limit);
			preparedStatement.setInt(3, offset);

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

	@Override
	public long countAgencies(String key) throws Exception {

		return 0;
	}

	@Override
	public List<Agency> getAllAgencies(int limit, int offset) {
		List<Agency> agencies = new ArrayList<>();
		try {

			String sql = "SELECT * FROM travelAgency WHERE is_delete=? and status = ? LIMIT ? OFFSET ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, false);
			preparedStatement.setString(2, "APPROVED");
			preparedStatement.setInt(3, limit);
			preparedStatement.setInt(4, offset);
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
				agency.setPassword(resultSet.getString("password"));
				agency.setActive(resultSet.getBoolean("is_active"));
				agency.setDelete(resultSet.getBoolean("is_delete"));
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

	@Override
	public List<Agency> getDeletedAgencies(int limit, int offset) throws Exception {
		List<Agency> list = new ArrayList<>();
		try {
			String sql = "SELECT * FROM travelAgency WHERE is_delete = ? LIMIT ? OFFSET ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, true);
			preparedStatement.setInt(2, limit);
			preparedStatement.setInt(3, offset);

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
				if (resultSet.getDate("created_at") != null)
					agency.setCreatedAt(resultSet.getDate("created_at").toLocalDate());
				if (resultSet.getDate("updated_at") != null)
					agency.setUpdatedAt(resultSet.getDate("updated_at").toLocalDate());
				list.add(agency);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public long countAgencies(String status, Boolean activeState, Boolean isDeleted, String keyword) throws Exception {
		long total = 0;
		String sql = "SELECT COUNT(*) AS total FROM travelAgency WHERE 1=1";

		if (status != null && !status.isEmpty()) {
			sql += " AND status = ?";
		}
		if (activeState != null) {
			sql += " AND is_active = ?";
		}
		if (isDeleted != null) {
			sql += " AND is_delete = ?";
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
			if (keyword != null && !keyword.isEmpty()) {
				String likeKeyword = "%" + keyword + "%";
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
				preparedStatement.setString(index++, likeKeyword);
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
	public List<Agency> filterAgencies(String status, String active, String startDate, String endDate, String keyword,
	        int limit, int offset) throws Exception {
	    List<Agency> agencies = new ArrayList<>();

	    String sql = "SELECT * FROM travelAgency WHERE 1=1";

	  
	    if (status != null && !status.isEmpty()) {
	        sql += " AND status = ?";
	    }

	  
	    if (active != null && !active.isEmpty()) {
	        sql += " AND is_active = ?";
	    }

	  
	    if (startDate != null && !startDate.isEmpty()) {
	        sql += " AND created_at >= ?";
	    }
	    if (endDate != null && !endDate.isEmpty()) {
	        sql += " AND created_at <= ?";
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

	        if (status != null && !status.isEmpty()) {
	            preparedStatement.setString(index++, status);
	        }
	        if (active != null && !active.isEmpty()) {
	            preparedStatement.setBoolean(index++, Boolean.parseBoolean(active));
	        }
	        if (startDate != null && !startDate.isEmpty()) {
	            preparedStatement.setDate(index++, Date.valueOf(startDate));
	        }
	        if (endDate != null && !endDate.isEmpty()) {
	            preparedStatement.setDate(index++, Date.valueOf(endDate));
	        }
	        if (keyword != null && !keyword.isEmpty()) {
	            String kw = "%" + keyword.toLowerCase() + "%";
	            for (int i = 0; i < 9; i++) {
	                preparedStatement.setString(index++, kw);
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
