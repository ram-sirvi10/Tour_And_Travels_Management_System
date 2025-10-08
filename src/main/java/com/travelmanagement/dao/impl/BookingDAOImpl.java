package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IBookingDAO;
import com.travelmanagement.model.Booking;

public class BookingDAOImpl implements IBookingDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public BookingDAOImpl() {
		connection = DatabaseConfig.getConnection();
	}

	@Override
	public int createBooking(Booking booking) {
		int bookingId = -1;
		String sql = "INSERT INTO bookings (user_id, package_id,no_of_travellers) VALUES (?,  ?,  ?)";

		try {
			preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, booking.getUserId());
			preparedStatement.setInt(2, booking.getPackageId());
			preparedStatement.setInt(3, booking.getNoOfTravellers());

			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					bookingId = resultSet.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bookingId;
	}

	@Override
	public Booking getBookingById(Integer bookingId) throws Exception {
		String sql = "SELECT * FROM bookings WHERE booking_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, bookingId);
		resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			Booking booking = new Booking();
			booking.setBookingId(resultSet.getInt("booking_id"));
			booking.setUserId(resultSet.getInt("user_id"));
			booking.setPackageId(resultSet.getInt("package_id"));
			booking.setBookingDate(resultSet.getTimestamp("booking_date").toLocalDateTime());
			booking.setStatus(resultSet.getString("status"));
			booking.setNoOfTravellers(resultSet.getInt("no_of_travellers"));

			if (resultSet.getTimestamp("created_at") != null)
				booking.setCreated_at((resultSet.getTimestamp("created_at").toLocalDateTime()));
			return booking;
		}
		return null;
	}

	@Override
	public boolean updateBookingStatus(Integer bookingId, String status) throws Exception {
		String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, status);
		preparedStatement.setInt(2, bookingId);
		int rowsUpdated = preparedStatement.executeUpdate();
		return rowsUpdated > 0;
	}

	@Override
	public List<Booking> getAllBookings(Integer agencyId, Integer userId, Integer packageId, Integer noOfTravellers,
			String status, String startDate, String endDate, int limit, int offset) throws Exception {

		List<Booking> bookings = new ArrayList<>();

		String sql = "SELECT b.* FROM bookings b " + "INNER JOIN travel_packages p ON b.package_id = p.package_id "
				+ "WHERE 1=1";

		if (agencyId != null)
			sql += " AND p.agency_id = ?";
		if (userId != null)
			sql += " AND b.user_id = ?";
		if (packageId != null)
			sql += " AND b.package_id = ?";
		if (status != null && !status.isEmpty())
			sql += " AND b.status = ?";

		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) BETWEEN ? AND ?";
		} else if (startDate != null && !startDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) >= ?";
		} else if (endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) <= ?";
		}

		sql += " ORDER BY b.created_at DESC LIMIT ? OFFSET ?";

		preparedStatement = connection.prepareStatement(sql);

		int index = 1;
		if (agencyId != null)
			preparedStatement.setInt(index++, agencyId);
		if (userId != null)
			preparedStatement.setInt(index++, userId);
		if (packageId != null)
			preparedStatement.setInt(index++, packageId);
		if (status != null && !status.isEmpty())
			preparedStatement.setString(index++, status);

		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		} else if (startDate != null && !startDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		} else if (endDate != null && !endDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		}

		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index++, offset);

		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Booking booking = new Booking();
			booking.setBookingId(resultSet.getInt("booking_id"));
			booking.setUserId(resultSet.getInt("user_id"));
			booking.setPackageId(resultSet.getInt("package_id"));
			booking.setBookingDate(resultSet.getTimestamp("booking_date").toLocalDateTime());
			booking.setStatus(resultSet.getString("status"));
			booking.setNoOfTravellers(resultSet.getInt("no_of_travellers"));
			if (resultSet.getTimestamp("created_at") != null)
				booking.setCreated_at(resultSet.getTimestamp("created_at").toLocalDateTime());

			bookings.add(booking);
		}

		return bookings;
	}

	@Override
	public long getAllBookingsCount(Integer agencyId, Integer userId, Integer packageId, Integer noOfTravellers,
			String status, String startDate, String endDate) throws Exception {

		int count = 0;

		String sql = "SELECT COUNT(*) AS total FROM bookings b "
				+ "INNER JOIN travel_packages p ON b.package_id = p.package_id " + "WHERE 1=1";

		if (agencyId != null)
			sql += " AND p.agency_id = ?";
		if (userId != null)
			sql += " AND b.user_id = ?";
		if (packageId != null)
			sql += " AND b.package_id = ?";
		if (noOfTravellers != null)
			sql += " AND b.no_of_travellers = ?";
		if (status != null && !status.isEmpty())
			sql += " AND b.status = ?";

		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) BETWEEN ? AND ?";
		} else if (startDate != null && !startDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) >= ?";
		} else if (endDate != null && !endDate.isEmpty()) {
			sql += " AND DATE(b.booking_date) <= ?";
		}

		preparedStatement = connection.prepareStatement(sql);

		int index = 1;
		if (agencyId != null)
			preparedStatement.setInt(index++, agencyId);
		if (userId != null)
			preparedStatement.setInt(index++, userId);
		if (packageId != null)
			preparedStatement.setInt(index++, packageId);
		if (noOfTravellers != null)
			preparedStatement.setInt(index++, noOfTravellers);
		if (status != null && !status.isEmpty())
			preparedStatement.setString(index++, status);

		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		} else if (startDate != null && !startDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		} else if (endDate != null && !endDate.isEmpty()) {
			preparedStatement.setDate(index++,
					Date.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
		}

		resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			count = resultSet.getInt("total");
		}

		return count;
	}

	@Override
	public boolean cancelBooking(Integer bookingId) throws Exception {
		return updateBookingStatus(bookingId, "CANCELLED");
	}

	@Override
	public void decrementTravelerCount(Integer bookingId) throws Exception {
		try {
			preparedStatement = connection.prepareStatement(
					"UPDATE bookings SET no_of_travellers = no_of_travellers - 1 WHERE booking_id = ?");
			preparedStatement.setInt(1, bookingId);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
