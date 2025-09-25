package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	public boolean addPackage(Packages pkg) throws Exception {
		String sql = "INSERT INTO travel_packages (title, agency_id, description, price, location, duration, is_active, created_at, updated_at) "
				+ "VALUES (?,?,?,?,?,?,?, NOW(), NOW())";

		 connection = DatabaseConfig.getConnection();
		 preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setString(1, pkg.getTitle());
		preparedStatement.setInt(2, pkg.getAgencyId());
		preparedStatement.setString(3, pkg.getDescription());
		preparedStatement.setDouble(4, pkg.getPrice());
		preparedStatement.setString(5, pkg.getLocation());
		preparedStatement.setInt(6, pkg.getDuration());
		preparedStatement.setBoolean(7, pkg.isActive());

		return preparedStatement.executeUpdate() > 0;
	}

	@Override
	public boolean updatePackage(Packages pkg) throws Exception {
		String sql = "UPDATE travel_packages SET title=?, agency_id=?, description=?, price=?, location=?, duration=?, is_active=?, updated_at = NOW() WHERE package_id=?";

		 connection = DatabaseConfig.getConnection();
		 preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setString(1, pkg.getTitle());
		preparedStatement.setInt(2, pkg.getAgencyId());
		preparedStatement.setString(3, pkg.getDescription());
		preparedStatement.setDouble(4, pkg.getPrice());
		preparedStatement.setString(5, pkg.getLocation());
		preparedStatement.setInt(6, pkg.getDuration());
		preparedStatement.setBoolean(7, pkg.isActive());
		preparedStatement.setInt(8, pkg.getPackageId());

		return preparedStatement.executeUpdate() > 0;
	}

	@Override
	public boolean deletePackage(int packageId) throws Exception {
		String sql = "DELETE FROM travel_packages WHERE package_id=?";

		 connection = DatabaseConfig.getConnection();
		 preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, packageId);

		return preparedStatement.executeUpdate() > 0;
	}

	@Override
	public boolean togglePackageStatus(int packageId) throws Exception {
		String sql = "UPDATE travel_packages SET is_active = CASE WHEN is_active=1 THEN 0 ELSE 1 END, updated_at = NOW() WHERE package_id=?";

		 connection = DatabaseConfig.getConnection();
		 preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, packageId);

		return preparedStatement.executeUpdate() > 0;
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
			pkg.setTotalSeats(resultSet.getInt("totalseats"));
			if (resultSet.getString("imageurl") != null) {
				pkg.setImageurl(resultSet.getString("imageurl"));
			}
			pkg.setTotalSeats(resultSet.getInt("totalseats"));
			list.add(pkg);
		}

		return list;
	}

	@Override
	public Packages getPackageById(int packageId) throws Exception {
		String sql = "SELECT * FROM travel_packages WHERE package_id = ?";

		 connection = DatabaseConfig.getConnection();
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
			pkg.setTotalSeats(resultSet.getInt("totalseats"));
			if (resultSet.getString("imageurl") != null) {
				pkg.setImageurl(resultSet.getString("imageurl"));
			}
			return pkg;
		}

		return null;
	}

	@Override
	public List<Packages> searchPackages(String title, Integer agencyId, String location, String keyword,
			String dateFrom, String dateTo, int limit, int offset) throws Exception {
		List<Packages> list = new ArrayList<>();

		StringBuilder sql = new StringBuilder("SELECT * FROM travel_packages WHERE 1=1");

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
			sql.append(" AND DATE(created_at) >= ?");
		}
		if (dateTo != null && !dateTo.isEmpty()) {
			sql.append(" AND  DATE(created_at) <= ?");
		}

		sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");

		 connection = DatabaseConfig.getConnection();
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
			preparedStatement.setString(index++, "%" + keyword + "%");
			preparedStatement.setString(index++, "%" + keyword + "%");
			preparedStatement.setString(index++, "%" + keyword + "%");
		}
		if (dateFrom != null && !dateFrom.isEmpty()) {
			preparedStatement.setString(index++, dateFrom);
		}
		if (dateTo != null && !dateTo.isEmpty()) {
			preparedStatement.setString(index++, dateTo);
		}

		preparedStatement.setInt(index++, limit);
		preparedStatement.setInt(index, offset);

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
			pkg.setTotalSeats(resultSet.getInt("totalseats"));
			if (resultSet.getString("imageurl") != null) {
				pkg.setImageurl(resultSet.getString("imageurl"));
			}
			list.add(pkg);
		}

		return list;
	}

	@Override
	public boolean adjustSeats(int packageId, int seatsChange) throws Exception {
		if (seatsChange < 0)
			return false;

		String sql = "UPDATE travel_packages SET totalseats = ?, updated_at = NOW() WHERE package_id = ?";
		try  {
			 preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, seatsChange);
			preparedStatement.setInt(2, packageId);
			int rows = preparedStatement.executeUpdate();
			System.out.println("Rows updated: " + rows);
			return rows > 0;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
