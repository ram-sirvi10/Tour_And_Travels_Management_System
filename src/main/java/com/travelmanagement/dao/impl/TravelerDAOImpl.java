package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.ITravelerDAO;
import com.travelmanagement.model.Traveler;

public class TravelerDAOImpl implements ITravelerDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public TravelerDAOImpl() {
		this.connection = DatabaseConfig.getConnection();
	}

	@Override
	public boolean createTravelers(List<Traveler> travelers) throws Exception {
		boolean success = false;
		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(
					"INSERT INTO travelers (booking_id, name, email, mobile, age) VALUES (?, ?, ?, ?, ?)");
			for (Traveler t : travelers) {
				preparedStatement.setInt(1, t.getBookingId());
				preparedStatement.setString(2, t.getName());
				preparedStatement.setString(3, t.getEmail());
				preparedStatement.setString(4, t.getMobile());
				preparedStatement.setInt(5, t.getAge());
				preparedStatement.addBatch();
			}
			int[] results = preparedStatement.executeBatch();
			for (int r : results) {
				if (r == PreparedStatement.EXECUTE_FAILED) {
					connection.rollback();
					return false;
				}
			}
			connection.commit();
			success = true;
		} catch (Exception e) {
			connection.rollback();
			e.printStackTrace();
		} finally {

			connection.setAutoCommit(true);
		}
		return success;
	}

	@Override
	public Traveler getTravelerById(Integer travelerId) throws Exception {
		try {
			preparedStatement = connection.prepareStatement("SELECT * FROM travelers WHERE traveler_id = ?");
			preparedStatement.setInt(1, travelerId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Traveler t = new Traveler();
				t.setId(resultSet.getInt("traveler_id"));
				t.setBookingId(resultSet.getInt("booking_id"));
				t.setName(resultSet.getString("name"));
				t.setEmail(resultSet.getString("email"));
				t.setMobile(resultSet.getString("mobile"));
				t.setAge(resultSet.getInt("age"));
				t.setStatus(resultSet.getString("status"));
				return t;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateTraveler(Traveler traveler) throws Exception {
		try {
			preparedStatement = connection.prepareStatement(
					"UPDATE travelers SET booking_id = ?, name = ?, email = ?, mobile = ?, age = ? WHERE traveler_id = ?");
			preparedStatement.setInt(1, traveler.getBookingId());
			preparedStatement.setString(2, traveler.getName());
			preparedStatement.setString(3, traveler.getEmail());
			preparedStatement.setString(4, traveler.getMobile());
			preparedStatement.setInt(5, traveler.getAge());
			preparedStatement.setInt(6, traveler.getId());
			int updated = preparedStatement.executeUpdate();
			return updated > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteTraveler(Integer travelerId) throws Exception {
		try {
			preparedStatement = connection.prepareStatement("DELETE FROM travelers WHERE traveler_id = ?");
			preparedStatement.setInt(1, travelerId);
			int deleted = preparedStatement.executeUpdate();
			return deleted > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void updateTravelerStatus(Integer travelerId, Integer bookingId, String status) throws Exception {
		try {
			String sql = "UPDATE travelers SET status = ? WHERE 1=1 ";
			if (travelerId != null) {
				sql += "AND  traveler_id = ? ";
			}
			if (bookingId != null) {
				sql += " AND booking_id =? ";
			}
			preparedStatement = connection.prepareStatement(sql);
			int index = 1;
			preparedStatement.setString(index++, status);
			if (travelerId != null) {
				preparedStatement.setInt(index++, travelerId);
			}
			if (bookingId != null) {
				preparedStatement.setInt(index++, bookingId);

			}
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Traveler> getAllTravelers(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, String bookingStatus, String keyword, int limit, int offset) throws Exception {
		List<Traveler> travelers = new ArrayList<>();
		try {
			String sql = "SELECT DISTINCT t.traveler_id, t.booking_id, t.name, t.email, t.mobile, t.age, t.status, b.status AS booking_status"
					+ " FROM travelers t " + "JOIN bookings b ON t.booking_id = b.booking_id "
					+ "JOIN travel_packages p ON b.package_id = p.package_id "
					+ "JOIN travelAgency a ON p.agency_id = a.agency_id " + "WHERE 1=1 ";

			if (travelerId != null)
				sql += "AND t.traveler_id = ? ";
			if (bookingId != null)
				sql += "AND t.booking_id = ? ";
			if (userId != null)
				sql += "AND b.user_id = ? ";
			if (packageId != null)
				sql += "AND p.package_id = ? ";
			if (agencyId != null)
				sql += "AND a.agency_id = ? ";

			if (bookingStatus != null && !bookingStatus.isEmpty())
				sql += "AND b.status = ? ";

			if (keyword != null && !keyword.isEmpty())
				sql += "AND (t.name LIKE ? OR t.email LIKE ? OR t.mobile LIKE ?) ";

			sql += " LIMIT ? OFFSET ?";
			preparedStatement = connection.prepareStatement(sql);

			int index = 1;
			if (travelerId != null)
				preparedStatement.setInt(index++, travelerId);
			if (bookingId != null)
				preparedStatement.setInt(index++, bookingId);
			if (userId != null)
				preparedStatement.setInt(index++, userId);
			if (packageId != null)
				preparedStatement.setInt(index++, packageId);
			if (agencyId != null)
				preparedStatement.setInt(index++, agencyId);

			if (bookingStatus != null && !bookingStatus.isEmpty())
				preparedStatement.setString(index++, bookingStatus);

			if (keyword != null && !keyword.isEmpty()) {
				String k = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
				preparedStatement.setString(index++, k);
				preparedStatement.setString(index++, k);
				preparedStatement.setString(index++, k);
			}

			preparedStatement.setInt(index++, limit);
			preparedStatement.setInt(index++, offset);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Traveler t = new Traveler();
				t.setId(resultSet.getInt("traveler_id"));
				t.setBookingId(resultSet.getInt("booking_id"));
				t.setName(resultSet.getString("name"));
				t.setEmail(resultSet.getString("email"));
				t.setMobile(resultSet.getString("mobile"));
				t.setAge(resultSet.getInt("age"));
				t.setStatus(resultSet.getString("status"));
				travelers.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return travelers;
	}

	@Override
	public long getTravelerCount(Integer travelerId, Integer bookingId, Integer userId, Integer packageId,
			Integer agencyId, String bookingStatus, String keyword) throws Exception {
		int count = 0;
		try {
			String sql = "SELECT COUNT(DISTINCT t.traveler_id) AS total " + "FROM travelers t "
					+ "JOIN bookings b ON t.booking_id = b.booking_id "
					+ "JOIN travel_packages p ON b.package_id = p.package_id "
					+ "JOIN travelAgency a ON p.agency_id = a.agency_id " + "WHERE 1=1 ";

			if (travelerId != null)
				sql += "AND t.traveler_id = ? ";
			if (bookingId != null)
				sql += "AND t.booking_id = ? ";
			if (userId != null)
				sql += "AND b.user_id = ? ";
			if (packageId != null)
				sql += "AND p.package_id = ? ";
			if (agencyId != null)
				sql += "AND a.agency_id = ? ";
			if (bookingStatus != null && !bookingStatus.isEmpty())
				sql += "AND b.status = ? ";
			if (keyword != null && !keyword.isEmpty())
				sql += "AND (t.name LIKE ? OR t.email LIKE ? OR t.mobile LIKE ?) ";

			preparedStatement = connection.prepareStatement(sql);

			int index = 1;
			if (travelerId != null)
				preparedStatement.setInt(index++, travelerId);
			if (bookingId != null)
				preparedStatement.setInt(index++, bookingId);
			if (userId != null)
				preparedStatement.setInt(index++, userId);
			if (packageId != null)
				preparedStatement.setInt(index++, packageId);
			if (agencyId != null)
				preparedStatement.setInt(index++, agencyId);

			if (bookingStatus != null && !bookingStatus.isEmpty())
				preparedStatement.setString(index++, bookingStatus);

			if (keyword != null && !keyword.isEmpty()) {
				String k = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
				preparedStatement.setString(index++, k);
				preparedStatement.setString(index++, k);
				preparedStatement.setString(index++, k);
			}

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public boolean isTravelerAlreadyBooked(String email, Integer packageId) throws Exception {
		boolean exists = false;
		try {
			String sql = "SELECT COUNT(*) AS total " + "FROM travelers t "
					+ "JOIN bookings b ON t.booking_id = b.booking_id "
					+ "WHERE t.email = ? AND b.package_id = ? and b.status != ?";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, packageId);
			preparedStatement.setString(3, "CANCELLED");
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next() && resultSet.getInt("total") > 0) {
				exists = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exists;
	}

	

}
