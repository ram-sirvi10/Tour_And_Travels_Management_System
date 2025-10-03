package com.travelmanagement.dao;

import java.util.List;

import com.travelmanagement.model.PackageSchedule;

public interface IPackageScheduleDAO {
	boolean addSchedule(PackageSchedule schedule) throws Exception;

	boolean updateSchedule(PackageSchedule schedule) throws Exception;

	boolean deleteSchedule(int scheduleId) throws Exception;

	List<PackageSchedule> getScheduleByPackage(int packageId) throws Exception;
}
