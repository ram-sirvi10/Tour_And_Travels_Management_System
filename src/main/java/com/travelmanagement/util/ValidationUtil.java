package com.travelmanagement.util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtil {

	// Email Validation
	public static Boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}$";
		return Pattern.matches(emailRegex, email);
	}

	// Mobile Number Validation (India specific: 10 digits, starts with 6-9)
	public static Boolean isValidMob(String mob) {
		String mobRegex = "^[6-9]\\d{9}$";
		return Pattern.matches(mobRegex, mob);
	}

	// Name Validation (alphabets + spaces + few special chars)
	public static Boolean isValidName(String name) {
		String nameRegex = "^[A-Za-z .,'’\\-()]+$";
		return Pattern.matches(nameRegex, name);
	}

	// Password Validation (min 6 chars, at least 1 upper, 1 lower, 1 digit, 1
	// special char)
	public static Boolean isValidPassword(String password) {
		String passRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^])[A-Za-z\\d@$!%*?&#^]{6,}$";
		return Pattern.matches(passRegex, password);
	}

	// Pincode Validation (India: 6 digits, not starting with 0)
	public static Boolean isValidPincode(String pincode) {
		String pinRegex = "^[1-9][0-9]{5}$";
		return Pattern.matches(pinRegex, pincode);
	}

	// Number Validation (positive integers)
	public static Boolean isValidNumber(String numberStr) {
		if (numberStr == null || numberStr.trim().isEmpty())
			return false;
		try {
			int num = Integer.parseInt(numberStr);
			return num > 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// Price Validation (Decimal upto 2 places)
	public static Boolean isValidPrice(String price) {
		String priceRegex = "^[0-9]+(\\.[0-9]{1,2})?$";
		return Pattern.matches(priceRegex, price);
	}

	// City/State/Country Validation (alphabets + spaces)
	public static Boolean isValidCityOrState(String name) {
		String cityRegex = "^[A-Za-z ]{2,50}$";
		return Pattern.matches(cityRegex, name);
	}

	// Registration Number Validation (alphanumeric + hyphen)
	public static Boolean isValidRegistrationNumber(String regNo) {
		String regRegex = "^[A-Za-z0-9\\-]+$";
		return Pattern.matches(regRegex, regNo);
	}

	public static Boolean isValidName1(String name) {
		return name != null && Pattern.matches("^[A-Za-z0-9 .,'()-]{2,100}$", name);
	}

	public static Boolean isValidPrice1(String price) {
		return price != null && Pattern.matches("^[0-9]+(\\.[0-9]{1,2})?$", price);
	}

	// Description Validation (minimum 10 words)
	public static Boolean isValidDescription(String description) {
		if (description == null || description.trim().isEmpty())
			return false;
		String[] words = description.trim().split("\\s+");
		return words.length >= 10; // minimum 10 words
	}

	// Package/Activity Name Validation (letters, numbers, spaces, few special
	// chars, max 100 chars)
	public static Boolean isValidPackageActivity(String name) {
		if (name == null)
			return false;
		return Pattern.matches("^[A-Za-z0-9 .,'’\\-()]{2,100}$", name);
	}

}
