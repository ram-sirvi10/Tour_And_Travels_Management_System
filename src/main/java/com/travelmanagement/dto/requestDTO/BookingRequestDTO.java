package com.travelmanagement.dto.requestDTO;

import java.util.ArrayList;
import java.util.Date;

import com.travelmanagement.model.Traveler;

public class BookingRequestDTO {
    private int userId;              // ID of the user making the booking
    private int packageId;           // ID of the selected travel package
    private Date bookingDate;        // Date of the booking
    private int numberOfTravelers;   // Total number of travelers
    private ArrayList<Traveler> travelers; // List of traveler details
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPackageId() {
		return packageId;
	}
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public int getNumberOfTravelers() {
		return numberOfTravelers;
	}
	public void setNumberOfTravelers(int numberOfTravelers) {
		this.numberOfTravelers = numberOfTravelers;
	}
	public ArrayList<Traveler> getTravelers() {
		return travelers;
	}
	public void setTravelers(ArrayList<Traveler> travelers) {
		this.travelers = travelers;
	}
	@Override
	public String toString() {
		return "BookingRequestDTO [userId=" + userId + ", packageId=" + packageId + ", bookingDate=" + bookingDate
				+ ", numberOfTravelers=" + numberOfTravelers + ", travelers=" + travelers + "]";
	}
    
}
