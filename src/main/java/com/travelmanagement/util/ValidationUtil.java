package com.travelmanagement.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating user and package input fields.
 * Each method validates a specific type of data and returns true/false.
 * These are designed for realistic travel website data (not overly strict).
 */
public class ValidationUtil {

	// --------------------------------------------------------------
	// ✅ EMAIL VALIDATION
	// --------------------------------------------------------------
	/**
	 * Validates email address format.
	 * Example:
	 * ✅ john.doe@gmail.com
	 * ❌ john@com / @gmail.com / john@gmail
	 */
	public static Boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,}$";
		return email != null && Pattern.matches(emailRegex, email);
	}

	// --------------------------------------------------------------
	// ✅ MOBILE NUMBER VALIDATION (India specific)
	// --------------------------------------------------------------
	/**
	 * Validates 10-digit Indian mobile number (must start with 6–9).
	 * Example:
	 * ✅ 9876543210
	 * ❌ 1234567890 / 98765
	 */
	public static Boolean isValidMob(String mob) {
		String mobRegex = "^[6-9]\\d{9}$";
		return mob != null && Pattern.matches(mobRegex, mob);
	}

	// --------------------------------------------------------------
	// ✅ NAME VALIDATION
	// --------------------------------------------------------------
	/**
	 * Allows alphabets, spaces, and common name symbols.
	 * Example:
	 * ✅ Rameshwar Gehlot / O’Brien / A. P. Singh
	 * ❌ Ramesh@123 / #John
	 */
	public static Boolean isValidName(String name) {
		String nameRegex = "^[A-Za-z .,'’\\-()]{2,100}$";
		return name != null && Pattern.matches(nameRegex, name.trim());
	}

	// --------------------------------------------------------------
	// ✅ PASSWORD VALIDATION
	// --------------------------------------------------------------
	/**
	 * Ensures minimum 6 characters with at least 1 letter and 1 number.
	 * Example:
	 * ✅ Travel123 / MyTrip1
	 * ❌ travel / 123456
	 */
	public static Boolean isValidPassword(String password) {
		String passRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&#^]{6,}$";
		return password != null && Pattern.matches(passRegex, password);
	}

	// --------------------------------------------------------------
	// ✅ CITY / STATE / LOCATION VALIDATION
	// --------------------------------------------------------------
	/**
	 * Allows alphabets, spaces, commas, hyphens, and brackets.
	 * Example:
	 * ✅ Manali / New Delhi / Udaipur (Rajasthan) / Goa-North
	 * ❌ M@N@LI / 123City
	 */
	public static Boolean isValidCityOrState(String name) {
		String cityRegex = "^[A-Za-z ,()'-]{2,100}$";
		return name != null && Pattern.matches(cityRegex, name.trim());
	}

	// --------------------------------------------------------------
	// ✅ PRICE VALIDATION
	// --------------------------------------------------------------
	/**
	 * Validates positive numeric price (integer or decimal upto 2 places).
	 * Example:
	 * ✅ 12500 / 4999.99
	 * ❌ -100 / 12.345 / abc
	 */
	public static Boolean isValidPrice(String price) {
		String priceRegex = "^[0-9]+(\\.[0-9]{1,2})?$";
		return price != null && Pattern.matches(priceRegex, price);
	}

	// --------------------------------------------------------------
	// ✅ DESCRIPTION VALIDATION
	// --------------------------------------------------------------
	/**
	 * Ensures minimum 5 words and max 500 words.
	 * Ideal for package or day activity descriptions.
	 * Example:
	 * ✅ "Enjoy a 5-day mountain trip with snow activities and sightseeing."
	 * ❌ "Nice trip" (too short)
	 */
	public static Boolean isValidDescription(String description) {
		if (description == null || description.trim().isEmpty())
			return false;
		String[] words = description.trim().split("\\s+");
		return words.length >= 5 && words.length <= 500;
	}

	// --------------------------------------------------------------
	// ✅ PACKAGE TITLE / ACTIVITY VALIDATION
	// --------------------------------------------------------------
	/**
	 * Allows letters, numbers, spaces, and symbols like ( ) - , . '
	 * Example:
	 * ✅ Manali Adventure Trip / Day-1: Rohtang Pass Visit
	 * ❌ Trip@# / <b>Trip</b>
	 */
	public static Boolean isValidPackageActivity(String name) {
		String regex = "^[A-Za-z0-9 .,'’\\-()]{2,150}$";
		return name != null && Pattern.matches(regex, name.trim());
	}

	// --------------------------------------------------------------
	// ✅ POSITIVE NUMBER VALIDATION
	// --------------------------------------------------------------
	/**
	 * Checks if given number > 0.
	 * Used for seats, duration, price, etc.
	 * Example:
	 * ✅ 5 / 100
	 * ❌ 0 / -10 / null
	 */
	public static Boolean isPositiveNumber(Integer number) {
		return number != null && number > 0;
	}

	// --------------------------------------------------------------
	// ✅ BOOLEAN VALIDATION
	// --------------------------------------------------------------
	/**
	 * Checks if a Boolean value is not null.
	 * Example:
	 * ✅ true / false
	 * ❌ null
	 */
	public static Boolean isValidBoolean(Boolean bool) {
		return bool != null;
	}

	/**
	 * Validates a company registration number for a tour and travel business.
	 *
	 * This method allows alphanumeric characters (A-Z, a-z, 0-9) and hyphens (-) only.
	 * The length of the registration number must be between 5 and 20 characters.
	 *
	 * Examples of valid registration numbers:
	 *   - TRAVEL-123
	 *   - TT-2025-001
	 *   - TravelCompany01
	 */
	public static boolean isValidRegistrationNumber(String registrationNumber) {
	    String regRegex = "^[A-Za-z0-9\\-]{5,20}$";
	    return Pattern.matches(regRegex, registrationNumber);
	}


}
