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

	@Override
	public boolean addSchedule(PackageSchedule schedule) throws Exception {
		String sql = "INSERT INTO package_schedule(package_id, day_number, activity, description) VALUES(?,?,?,?)";
		try (Connection con = DatabaseConfig.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, schedule.getPackageId());
			ps.setInt(2, schedule.getDayNumber());
			ps.setString(3, schedule.getActivity());
			ps.setString(4, schedule.getDescription());
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean updateSchedule(PackageSchedule schedule) throws Exception {
		String sql = "UPDATE package_schedule SET day_number=?, activity=?, description=? WHERE schedule_id=?";
		try (Connection con = DatabaseConfig.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, schedule.getDayNumber());
			ps.setString(2, schedule.getActivity());
			ps.setString(3, schedule.getDescription());
			ps.setInt(4, schedule.getScheduleId());
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean deleteSchedule(int scheduleId) throws Exception {
		String sql = "DELETE FROM package_schedule WHERE schedule_id=?";
		try (Connection con = DatabaseConfig.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, scheduleId);
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public List<PackageSchedule> getScheduleByPackage(int packageId) throws Exception {
		List<PackageSchedule> schedules = new ArrayList<>();
		String sql = "SELECT * FROM package_schedule WHERE package_id=? ORDER BY day_number";
		try (Connection con = DatabaseConfig.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
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
		}
		return schedules;
	}
}
