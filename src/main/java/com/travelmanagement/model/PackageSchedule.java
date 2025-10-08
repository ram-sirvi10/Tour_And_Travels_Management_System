package com.travelmanagement.model;

public class PackageSchedule {
	private Integer scheduleId;
	private Integer packageId;
	private Integer dayNumber;
	private String activity;
	private String description;

	// Getters & Setters
	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getDayNumber() {
		return dayNumber;
	}

	public void setDayNumber(Integer dayNumber) {
		this.dayNumber = dayNumber;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "PackageSchedule [scheduleId=" + scheduleId + ", packageId=" + packageId + ", dayNumber=" + dayNumber
				+ ", activity=" + activity + ", description=" + description + "]";
	}
	
}
