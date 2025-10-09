package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IPackageDAO;
import com.travelmanagement.model.Packages;

public class PackageDAOImpl implements IPackageDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public PackageDAOImpl() {
		connection = DatabaseConfig.getConnection();
	}

	@Override
	public int addPackage(Packages pkg) {
		int generatedId = 0;
		String sql = "INSERT INTO travel_packages (title, agency_id, description, price, location, duration, is_active, total_seats, imageurl, departure_date, last_booking_date, version,is_delete) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?, ?,?)";

		try {
			preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, pkg.getTitle());
			preparedStatement.setInt(2, pkg.getAgencyId());
			preparedStatement.setString(3, pkg.getDescription());
			preparedStatement.setDouble(4, pkg.getPrice());
			preparedStatement.setString(5, pkg.getLocation());
			preparedStatement.setInt(6, pkg.getDuration());
			preparedStatement.setBoolean(7, pkg.isActive());
			preparedStatement.setInt(8, pkg.getTotalSeats());
			preparedStatement.setString(9, pkg.getImageurl());
			preparedStatement.setObject(10, pkg.getDepartureDate());
			preparedStatement.setObject(11, pkg.getLastBookingDate());
			preparedStatement.setInt(12, pkg.getVersion());
			preparedStatement.setBoolean(13, false);
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				resultSet = preparedStatement.getGeneratedKeys();
				if (resultSet.next()) {
					generatedId = resultSet.getInt(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatedId;
	}

	@Override
	public boolean updatePackage(Packages pkg) {
		String sql = "UPDATE travel_packages SET title=?, agency_id=?, description=?, price=?, location=?, duration=?, is_active=?, total_seats=?, imageurl=?, departure_date=?, last_booking_date=? WHERE package_id=?";

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			System.err.println(preparedStatement);
			preparedStatement.setString(1, pkg.getTitle());
			preparedStatement.setInt(2, pkg.getAgencyId());
			preparedStatement.setString(3, pkg.getDescription());
			preparedStatement.setDouble(4, pkg.getPrice());
			preparedStatement.setString(5, pkg.getLocation());
			preparedStatement.setInt(6, pkg.getDuration());
			preparedStatement.setBoolean(7, pkg.isActive());
			preparedStatement.setInt(8, pkg.getTotalSeats());
			preparedStatement.setString(9, pkg.getImageurl());
			preparedStatement.setObject(10, pkg.getDepartureDate());
			preparedStatement.setObject(11, pkg.getLastBookingDate());
			preparedStatement.setInt(12, pkg.getPackageId());

			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deletePackage(Integer packageId) {
		String sql = "update travel_packages set is_active=? , is_delete=? WHERE package_id=?";

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setBoolean(1, false);
			preparedStatement.setBoolean(2, true);
			preparedStatement.setInt(3, packageId);

			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean togglePackageStatus(Integer packageId) {
		String sql = "UPDATE travel_packages SET is_active = CASE WHEN is_active=1 THEN 0 ELSE 1 END WHERE package_id=?";

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, packageId);

			return preparedStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Packages> getAllPackages() throws Exception {
		String sql = "SELECT * FROM travel_packages ORDER BY package_id DESC";
		List<Packages> list = new ArrayList<>();

		connection = DatabaseConfig.getConnection();
		preparedStatement = connection.prepareStatement(sql);
		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			Packages pkg = new Packages();
			pkg.setPackageId(resultSet.getInt("package_id"));
			pkg.setTitle(resultSet.getString("title"));
			pkg.setAgencyId(resultSet.getInt("agency_id"));
			pkg.setDescription(resultSet.getString("description"));
			pkg.setPrice(resultSet.getDouble("price"));
			pkg.setLocation(resultSet.getString("location"));
			pkg.setDuration(resultSet.getInt("duration"));
			pkg.setActive(resultSet.getBoolean("is_active"));
			pkg.setTotalSeats(resultSet.getInt("total_seats"));
			if (resultSet.getString("imageurl") != null) {
				pkg.setImageurl(resultSet.getString("imageurl"));
			}
			if (resultSet.getTimestamp("departure_date") != null) {
				pkg.setDepartureDate(resultSet.getTimestamp("departure_date").toLocalDateTime());
			}
			pkg.setTotalSeats(resultSet.getInt("total_seats"));
			if (resultSet.getTimestamp("last_booking_date") != null) {
				pkg.setLastBookingDate(resultSet.getTimestamp("last_booking_date").toLocalDateTime());

			}
			list.add(pkg);
		}

		return list;
	}

	@Override
	public Packages getPackageById(Integer packageId) {
		String sql = "SELECT * FROM travel_packages WHERE package_id = ?";

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, packageId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Packages pkg = new Packages();
				pkg.setPackageId(resultSet.getInt("package_id"));
				pkg.setTitle(resultSet.getString("title"));
				pkg.setAgencyId(resultSet.getInt("agency_id"));
				pkg.setDescription(resultSet.getString("description"));
				pkg.setPrice(resultSet.getDouble("price"));
				pkg.setLocation(resultSet.getString("location"));
				pkg.setDuration(resultSet.getInt("duration"));
				pkg.setActive(resultSet.getBoolean("is_active"));
				pkg.setTotalSeats(resultSet.getInt("total_seats"));
				pkg.setIsDelete(resultSet.getBoolean("is_delete"));
				pkg.setVersion(resultSet.getInt("version"));
				if (resultSet.getString("imageurl") != null) {
					pkg.setImageurl(resultSet.getString("imageurl"));
				}
				if (resultSet.getTimestamp("departure_date") != null) {
					pkg.setDepartureDate(resultSet.getTimestamp("departure_date").toLocalDateTime());
				}
				if (resultSet.getTimestamp("last_booking_date") != null) {
					pkg.setLastBookingDate(resultSet.getTimestamp("last_booking_date").toLocalDateTime());
				}
				return pkg;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, Integer totalSeats, Boolean isActive, int limit, int offset,
			Boolean isAgencyView, Boolean includePast) {

		List<Packages> list = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT * FROM travel_packages WHERE 1=1");

		// Filters
		if (title != null && !title.isEmpty()) {
			sql.append(" AND title LIKE ?");
		}
		if (agencyId != null) {
			sql.append(" AND agency_id = ?");
		}
		if (location != null && !location.isEmpty()) {
			sql.append(" AND location LIKE ?");
		}
		if (keyword != null && !keyword.isEmpty()) {
			sql.append(" AND (title LIKE ?  OR location LIKE ?)");
		}
		if (dateFrom != null && !dateFrom.isEmpty()) {
			sql.append(" AND DATE(departure_date) >= ?");
		}
		if (dateTo != null && !dateTo.isEmpty()) {
			sql.append(" AND DATE(departure_date) <= ?");
		}
		if (totalSeats != null) {
			sql.append(" AND total_seats = ?");
		}
		if (isActive != null) {
			sql.append(" AND is_active = ?");
		}

		if (isAgencyView == null || !isAgencyView) {
			sql.append(" AND DATE(last_booking_date) >= CURDATE()");
			sql.append(" AND total_seats > 0");
		}
		if (includePast != null)

		{
			if (!includePast) {
				sql.append(" AND DATE_ADD(departure_date, INTERVAL duration DAY) >= CURDATE()");
			} else {
				sql.append(" AND DATE_ADD(departure_date, INTERVAL duration DAY) < CURDATE()");
			}
		}

		sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql.toString());

			int index = 1;
			if (title != null && !title.isEmpty()) {
				preparedStatement.setString(index++, "%" + title + "%");
			}
			if (agencyId != null) {
				preparedStatement.setInt(index++, agencyId);
			}
			if (location != null && !location.isEmpty()) {
				preparedStatement.setString(index++, "%" + location + "%");
			}
			if (keyword != null && !keyword.isEmpty()) {
				String kw = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
				preparedStatement.setString(index++, kw);

				preparedStatement.setString(index++, kw);
			}
			if (dateFrom != null && !dateFrom.isEmpty()) {
				preparedStatement.setString(index++, dateFrom);
			}
			if (dateTo != null && !dateTo.isEmpty()) {
				preparedStatement.setString(index++, dateTo);
			}
			if (totalSeats != null) {
				preparedStatement.setInt(index++, totalSeats);
			}
			if (isActive != null) {
				preparedStatement.setBoolean(index++, isActive);
			}

			preparedStatement.setInt(index++, limit);
			preparedStatement.setInt(index++, offset);

			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Packages pkg = new Packages();
				pkg.setPackageId(resultSet.getInt("package_id"));
				pkg.setTitle(resultSet.getString("title"));
				pkg.setAgencyId(resultSet.getInt("agency_id"));
				pkg.setDescription(resultSet.getString("description"));
				pkg.setPrice(resultSet.getDouble("price"));
				pkg.setLocation(resultSet.getString("location"));
				pkg.setDuration(resultSet.getInt("duration"));
				pkg.setActive(resultSet.getBoolean("is_active"));
				pkg.setTotalSeats(resultSet.getInt("total_seats"));
				pkg.setIsDelete(resultSet.getBoolean("is_delete"));
				if (resultSet.getString("imageurl") != null) {
					pkg.setImageurl(resultSet.getString("imageurl"));
				}
				if (resultSet.getTimestamp("departure_date") != null) {
					pkg.setDepartureDate(resultSet.getTimestamp("departure_date").toLocalDateTime());
				}
				if (resultSet.getTimestamp("last_booking_date") != null) {
					pkg.setLastBookingDate(resultSet.getTimestamp("last_booking_date").toLocalDateTime());

				}
				list.add(pkg);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean adjustSeats(Integer packageId, Integer seatsChange) {
		String sql = "UPDATE travel_packages " + "SET total_seats = total_seats + ?, updated_at = NOW() "
				+ "WHERE package_id = ? AND total_seats + ? >= 0";

		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, seatsChange);
			ps.setInt(2, packageId);
			ps.setInt(3, seatsChange);
			System.out.println(ps);
			int rows = ps.executeUpdate();
			return rows > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public long countPackages(String title, Integer agencyId, String location, String keyword, String dateFrom,
			String dateTo, Integer totalSeats, Boolean isActive, Boolean isAgencyView, Boolean includePast) {

		int count = 0;
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM travel_packages WHERE 1=1");

		// Filters
		if (title != null && !title.isEmpty()) {
			sql.append(" AND title LIKE ?");
		}
		if (agencyId != null) {
			sql.append(" AND agency_id = ?");
		}
		if (location != null && !location.isEmpty()) {
			sql.append(" AND location LIKE ?");
		}
		if (keyword != null && !keyword.isEmpty()) {
			sql.append(" AND (title LIKE ? OR description LIKE ? OR location LIKE ?)");
		}
		if (dateFrom != null && !dateFrom.isEmpty()) {
			sql.append(" AND DATE(departure_date) >= ?");
		}
		if (dateTo != null && !dateTo.isEmpty()) {
			sql.append(" AND DATE(departure_date) <= ?");
		}
		if (totalSeats != null) {
			sql.append(" AND total_seats = ?");
		}
		if (isActive != null) {
			sql.append(" AND is_active = ?");
		}

		if (isAgencyView == null || !isAgencyView) {
			sql.append(" AND DATE(last_booking_date) >= CURDATE()");
			sql.append(" AND total_seats > 0");
		}
		if (!includePast) {
			sql.append(" AND DATE_ADD(departure_date, INTERVAL duration DAY) >= CURDATE()");
		} else {
			sql.append(" AND DATE_ADD(departure_date, INTERVAL duration DAY) < CURDATE()");
		}

		connection = DatabaseConfig.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql.toString());

			int index = 1;
			if (title != null && !title.isEmpty()) {
				preparedStatement.setString(index++, "%" + title + "%");
			}
			if (agencyId != null) {
				preparedStatement.setInt(index++, agencyId);
			}
			if (location != null && !location.isEmpty()) {
				preparedStatement.setString(index++, "%" + location + "%");
			}
			if (keyword != null && !keyword.isEmpty()) {
				String kw = "%" + keyword.replaceAll("[^A-Za-z0-9]", "") + "%";
				preparedStatement.setString(index++, kw);
				preparedStatement.setString(index++, kw);
				preparedStatement.setString(index++, kw);
			}
			if (dateFrom != null && !dateFrom.isEmpty()) {
				preparedStatement.setString(index++, dateFrom);
			}
			if (dateTo != null && !dateTo.isEmpty()) {
				preparedStatement.setString(index++, dateTo);
			}
			if (totalSeats != null) {
				preparedStatement.setInt(index++, totalSeats);
			}
			if (isActive != null) {
				preparedStatement.setBoolean(index++, isActive);
			}

			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int updateSeatsOptimistic(Integer packageId, Integer seatsToBook, Integer currentVersion) {
		String sql = "UPDATE travel_packages " + "SET total_seats = total_seats - ?, version = version + 1 "
				+ "WHERE package_id = ? AND version = ? AND total_seats >= ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, seatsToBook);
			preparedStatement.setInt(2, packageId);
			preparedStatement.setInt(3, currentVersion);
			preparedStatement.setInt(4, seatsToBook);
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
