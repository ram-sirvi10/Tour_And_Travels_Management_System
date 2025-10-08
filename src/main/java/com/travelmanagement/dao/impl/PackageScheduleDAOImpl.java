package com.travelmanagement.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.travelmanagement.config.DatabaseConfig;
import com.travelmanagement.dao.IPackageScheduleDAO;
import com.travelmanagement.model.PackageSchedule;

public class PackageScheduleDAOImpl implements IPackageScheduleDAO {

	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public PackageScheduleDAOImpl() {
		connection = DatabaseConfig.getConnection();
	}

	public boolean addSchedules(List<PackageSchedule> schedules) throws Exception {
		boolean success = false;
		try {
			connection.setAutoCommit(false); // transaction start
			preparedStatement = connection.prepareStatement(
					"INSERT INTO package_schedule(package_id, day_number, activity, description) VALUES (?, ?, ?, ?)");

			for (PackageSchedule s : schedules) {
				System.out.println("Packages id in scheulde impl "+ s.getPackageId());
				preparedStatement.setInt(1, s.getPackageId());
				preparedStatement.setInt(2, s.getDayNumber());
				preparedStatement.setString(3, s.getActivity());
				preparedStatement.setString(4, s.getDescription());
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
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return success;
	}

	@Override
	public boolean updatePackageSchedules(Integer packageId, List<PackageSchedule> updates, List<PackageSchedule> adds,
			List<Integer> deletes) throws Exception {

		boolean success = false;
		try {
			connection.setAutoCommit(false);

			if (!updates.isEmpty()) {
				String updateSql = "UPDATE package_schedule SET activity=?, description=? WHERE package_id=? AND day_number=?";
				preparedStatement = connection.prepareStatement(updateSql);
				for (PackageSchedule s : updates) {
					preparedStatement.setString(1, s.getActivity());
					preparedStatement.setString(2, s.getDescription());
					preparedStatement.setInt(3, s.getPackageId());
					preparedStatement.setInt(4, s.getDayNumber());
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			}

			if (!adds.isEmpty()) {
				String insertSql = "INSERT INTO package_schedule(package_id, day_number, activity, description) VALUES (?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(insertSql);
				for (PackageSchedule s : adds) {
					preparedStatement.setInt(1, s.getPackageId());
					preparedStatement.setInt(2, s.getDayNumber());
					preparedStatement.setString(3, s.getActivity());
					preparedStatement.setString(4, s.getDescription());
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			}

			if (!deletes.isEmpty()) {
				String deleteSql = "DELETE FROM package_schedule WHERE package_id=? AND day_number=?";
				preparedStatement = connection.prepareStatement(deleteSql);
				for (Integer day : deletes) {
					preparedStatement.setInt(1, packageId);
					preparedStatement.setInt(2, day);
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			}

			connection.commit();
			success = true;
		} catch (Exception e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return success;
	}

//	@Override
//	public boolean deleteSchedule(Integer scheduleId) throws Exception {
//		String sql = "DELETE FROM package_schedule WHERE schedule_id=?";
//		try {
//			Connection con = DatabaseConfig.getConnection();
//			PreparedStatement ps = con.prepareStatement(sql);
//			ps.setInt(1, scheduleId);
//			return ps.executeUpdate() > 0;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

	@Override
	public List<PackageSchedule> getScheduleByPackage(Integer packageId) throws Exception {
		List<PackageSchedule> schedules = new ArrayList<>();
		String sql = "SELECT * FROM package_schedule WHERE package_id=? ORDER BY day_number";
		try {
			Connection con = DatabaseConfig.getConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, packageId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				PackageSchedule pschedule = new PackageSchedule();
				pschedule.setScheduleId(rs.getInt("schedule_id"));
				pschedule.setPackageId(rs.getInt("package_id"));
				pschedule.setDayNumber(rs.getInt("day_number"));
				pschedule.setActivity(rs.getString("activity"));
				pschedule.setDescription(rs.getString("description"));
				schedules.add(pschedule);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schedules;
	}


}
