package com.travelmanagement.dto.requestDTO;

public class PackageScheduleRequestDTO {

	private Integer packageId;
	private Integer dayNumber;
	private String activity;
	private String description;
	private Integer scheduleId;

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

	public int getDayNumber() {
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
		return "PackageScheduleRequestDTO [packageId=" + packageId + ", dayNumber=" + dayNumber + ", activity="
				+ activity + ", description=" + description + "]";
	}

}
