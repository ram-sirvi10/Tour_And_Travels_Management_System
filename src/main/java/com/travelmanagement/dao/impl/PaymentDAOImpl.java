package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IPaymentDAO;
import com.travelmanagement.model.Payment;

public class PaymentDAOImpl implements IPaymentDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public PaymentDAOImpl() {
		connection = DatabaseConfig.getConnection();
	}

	@Override
	public boolean createPayment(Payment payment) throws Exception {
		String sql = "INSERT INTO payments (booking_id, status, amount) VALUES (?, ?, ?)";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, payment.getBookingId());
		preparedStatement.setString(2, payment.getStatus());
		preparedStatement.setDouble(3, payment.getAmount());
		int rowsInserted = preparedStatement.executeUpdate();
		return rowsInserted > 0;
	}

	@Override
	public Payment getPaymentById(int paymentId) throws Exception {
		String sql = "SELECT * FROM payments WHERE payment_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, paymentId);
		resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			Payment payment = new Payment();
			payment.setPaymentId(resultSet.getInt("payment_id"));
			payment.setBookingId(resultSet.getInt("booking_id"));
			payment.setStatus(resultSet.getString("status"));
			payment.setAmount(resultSet.getDouble("amount"));
			payment.setPaymentDate(resultSet.getTimestamp("payment_date").toLocalDateTime());
			return payment;
		}
		return null;
	}

	@Override
	public Payment getPaymentByBookingId(int bookingId) throws Exception {
		String sql = "SELECT * FROM payments WHERE booking_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, bookingId);
		resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			Payment payment = new Payment();
			payment.setPaymentId(resultSet.getInt("payment_id"));
			payment.setBookingId(resultSet.getInt("booking_id"));
			payment.setStatus(resultSet.getString("status"));
			payment.setAmount(resultSet.getDouble("amount"));
			payment.setPaymentDate(resultSet.getTimestamp("payment_date").toLocalDateTime());
			return payment;
		}
		return null;
	}

	@Override
	public boolean updatePaymentStatus(int paymentId, String status) throws Exception {
		String sql = "UPDATE payments SET status = ? WHERE payment_id = ?";
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, status);
		preparedStatement.setInt(2, paymentId);
		int rowsUpdated = preparedStatement.executeUpdate();
		return rowsUpdated > 0;
	}

	@Override
	public List<Payment> getPaymentHistory(Integer userId, Integer agencyId, Integer packageId, String status,
			String startDate, String endDate,int limit,int offset) throws Exception {
		List<Payment> payments = new ArrayList<>();

		String sql = "SELECT p.payment_id, p.booking_id, p.payment_date, p.status, p.amount " + "FROM payments p "
				+ "JOIN bookings b ON p.booking_id = b.booking_id "
				+ "JOIN travel_packages tp ON b.package_id = tp.package_id " + "WHERE 1=1";

		if (userId != null)
			sql += " AND b.user_id = ?";
		if (agencyId != null)
			sql += " AND tp.agency_id = ?";
		if (packageId != null)
			sql += " AND b.package_id = ?";
		if (status != null && !status.isEmpty())
			sql += " AND p.status = ?";

		if (startDate != null && endDate != null) {
			sql += " AND  DATE(p.payment_date) BETWEEN ? AND ?";
		} else if (startDate != null) {
			sql += " AND DATE(p.payment_date) >= ?";
		} else if (endDate != null) {
			sql += " AND DATE(p.payment_date) <= ?";
		}sql += " ORDER BY created_at DESC LIMIT ? OFFSET ?";

		preparedStatement = connection.prepareStatement(sql);

		int index = 1;

		if (userId != null)
			preparedStatement.setInt(index++, userId);
		if (agencyId != null)
			preparedStatement.setInt(index++, agencyId);
		if (packageId != null)
			preparedStatement.setInt(index++, packageId);
		if (status != null && !status.isEmpty())
			preparedStatement.setString(index++, status);

		if (startDate != null && endDate != null) {
			preparedStatement.setDate(index++, Date.valueOf(startDate));
			preparedStatement.setDate(index++, Date.valueOf(endDate));
		} else if (startDate != null) {
			preparedStatement.setDate(index++, Date.valueOf(startDate));
		} else if (endDate != null) {
			preparedStatement.setDate(index++, Date.valueOf(endDate));
		}
		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index++, offset);
		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Payment payment = new Payment();
			payment.setPaymentId(resultSet.getInt("payment_id"));
			payment.setBookingId(resultSet.getInt("booking_id"));
			payment.setStatus(resultSet.getString("status"));
			payment.setAmount(resultSet.getDouble("amount"));
			payment.setPaymentDate(resultSet.getTimestamp("payment_date").toLocalDateTime());
			payments.add(payment);
		}

		return payments;
	}
	
	@Override
	public int getPaymentHistoryCount(Integer userId, Integer agencyId, Integer packageId, String status,
	        String startDate, String endDate) throws Exception {
	    int count = 0;

	    String sql = "SELECT COUNT(*) AS total " +
	            "FROM payments p " +
	            "JOIN bookings b ON p.booking_id = b.booking_id " +
	            "JOIN travel_packages tp ON b.package_id = tp.package_id " +
	            "WHERE 1=1";

	    if (userId != null)
	        sql += " AND b.user_id = ?";
	    if (agencyId != null)
	        sql += " AND tp.agency_id = ?";
	    if (packageId != null)
	        sql += " AND b.package_id = ?";
	    if (status != null && !status.isEmpty())
	        sql += " AND p.status = ?";

	    if (startDate != null && endDate != null) {
	        sql += " AND DATE(p.payment_date) BETWEEN ? AND ?";
	    } else if (startDate != null) {
	        sql += " AND DATE(p.payment_date) >= ?";
	    } else if (endDate != null) {
	        sql += " AND DATE(p.payment_date) <= ?";
	    }

	    preparedStatement = connection.prepareStatement(sql);

	    int index = 1;

	    if (userId != null)
	        preparedStatement.setInt(index++, userId);
	    if (agencyId != null)
	        preparedStatement.setInt(index++, agencyId);
	    if (packageId != null)
	        preparedStatement.setInt(index++, packageId);
	    if (status != null && !status.isEmpty())
	        preparedStatement.setString(index++, status);

	    if (startDate != null && endDate != null) {
	        preparedStatement.setDate(index++, Date.valueOf(startDate));
	        preparedStatement.setDate(index++, Date.valueOf(endDate));
	    } else if (startDate != null) {
	        preparedStatement.setDate(index++, Date.valueOf(startDate));
	    } else if (endDate != null) {
	        preparedStatement.setDate(index++, Date.valueOf(endDate));
	    }

	    resultSet = preparedStatement.executeQuery();
	    if (resultSet.next()) {
	        count = resultSet.getInt("total");
	    }

	    return count;
	}


}
