package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.PackageSchedule;

public interface IPackageScheduleDAO {
	public boolean addSchedules(List<PackageSchedule> schedules) throws Exception;

	boolean deleteSchedule(int scheduleId) throws Exception;

	List<PackageSchedule> getScheduleByPackage(int packageId) throws Exception;

	boolean updatePackageSchedules(Integer packageId, List<PackageSchedule> updates, List<PackageSchedule> adds,
			List<Integer> deletes) throws Exception;
}
