package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public Boolean createBooking(Booking booking) throws Exception {
		String sql = "INSERT INTO bookings (user_id, package_id, status, no_of_travellers) VALUES (?, ?, ?, ?)";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, booking.getUserId());
		preparedStatement.setInt(2, booking.getPackageId());
		preparedStatement.setString(3, "PENDING");
		preparedStatement.setInt(4, booking.getNoOfTravellers());
		int rowsInserted = preparedStatement.executeUpdate();
		return rowsInserted > 0;
	}

	@Override
	public Booking getBookingById(int bookingId) throws Exception {
		String sql = "SELECT * FROM bookings WHERE booking_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, bookingId);
		resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			Booking booking = new Booking();
			booking.setBookingId(resultSet.getInt("booking_id"));
			booking.setUserId(resultSet.getInt("user_id"));
			booking.setPackageId(resultSet.getInt("package_id"));
			booking.setBookingDate(resultSet.getDate("booking_date").toLocalDate());
			booking.setStatus(resultSet.getString("status"));
			booking.setNoOfTravellers(resultSet.getInt("no_of_travellers"));
			return booking;
		}
		return null;
	}

	@Override
	public boolean updateBookingStatus(int bookingId, String status) throws Exception {
		String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, status);
		preparedStatement.setInt(2, bookingId);
		int rowsUpdated = preparedStatement.executeUpdate();
		return rowsUpdated > 0;
	}

	@Override
	public List<Booking> getAllBookings(Integer userId, Integer packageId, Integer noOfTravellers, String status,
			String keyword, String startDate, String endDate,int limit,int offset) throws Exception {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT * FROM bookings WHERE 1=1";

		if (userId != null)
			sql += " AND user_id = ?";
		if (packageId != null)
			sql += " AND package_id = ?";
		if (status != null && !status.isEmpty())
			sql += " AND status = ?";
		if (startDate != null && endDate != null)
			sql += " AND booking_date BETWEEN ? AND ?";
		sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";
		preparedStatement = connection.prepareStatement(sql);

		int index = 1;
		if (userId != null)
			preparedStatement.setInt(index++, userId);
		if (packageId != null)
			preparedStatement.setInt(index++, packageId);
		if (status != null && !status.isEmpty())
			preparedStatement.setString(index++, status);
		if (startDate != null && endDate != null) {
			preparedStatement.setDate(index++, java.sql.Date.valueOf(startDate));
			preparedStatement.setDate(index++, java.sql.Date.valueOf(endDate));
		}
		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index++, offset);
		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Booking booking = new Booking();
			booking.setBookingId(resultSet.getInt("booking_id"));
			booking.setUserId(resultSet.getInt("user_id"));
			booking.setPackageId(resultSet.getInt("package_id"));
			booking.setBookingDate(resultSet.getDate("booking_date").toLocalDate());
			booking.setStatus(resultSet.getString("status"));
			booking.setNoOfTravellers(resultSet.getInt("no_of_travellers"));
			bookings.add(booking);
		}

		return bookings;
	}

	@Override
	public int getAllBookingsCount(Integer userId, Integer packageId, Integer noOfTravellers, String status,
	        String keyword, String startDate, String endDate) throws Exception {
	    int count = 0;
	    String sql = "SELECT COUNT(*) AS total FROM bookings WHERE 1=1";

	    if (userId != null)
	        sql += " AND user_id = ?";
	    if (packageId != null)
	        sql += " AND package_id = ?";
	    if (noOfTravellers != null)
	        sql += " AND no_of_travellers = ?";
	    if (status != null && !status.isEmpty())
	        sql += " AND status = ?";
	    if (keyword != null && !keyword.isEmpty())
	        sql += " AND (CAST(booking_id AS CHAR) LIKE ? OR status LIKE ?)"; 
	    if (startDate != null && endDate != null)
	        sql += " AND booking_date BETWEEN ? AND ?";

	    preparedStatement = connection.prepareStatement(sql);

	    int index = 1;
	    if (userId != null)
	        preparedStatement.setInt(index++, userId);
	    if (packageId != null)
	        preparedStatement.setInt(index++, packageId);
	    if (noOfTravellers != null)
	        preparedStatement.setInt(index++, noOfTravellers);
	    if (status != null && !status.isEmpty())
	        preparedStatement.setString(index++, status);
	    if (keyword != null && !keyword.isEmpty()) {
	        String k = "%" + keyword + "%";
	        preparedStatement.setString(index++, k);
	        preparedStatement.setString(index++, k);
	    }
	    if (startDate != null && endDate != null) {
	        preparedStatement.setDate(index++, java.sql.Date.valueOf(startDate));
	        preparedStatement.setDate(index++, java.sql.Date.valueOf(endDate));
	    }

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
	        count = resultSet.getInt("total");
	    }

	    return count;
	}

	
	@Override
	public boolean cancelBooking(int bookingId) throws Exception {
		return updateBookingStatus(bookingId, "CANCELLED");
	}
}
