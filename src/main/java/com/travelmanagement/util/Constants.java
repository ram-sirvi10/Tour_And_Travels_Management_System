package com.travelmanagement.util;

public class Constants {

	// ===================ADMIN SERVLET ACTIONS ===================
	public static final String ACTION_DASHBOARD = "dashboard";
	public static final String ACTION_MANAGE_USERS = "manageUsers";
	public static final String ACTION_USER_ACTION = "userAction";
	public static final String ACTION_MANAGE_AGENCIES = "manageAgencies";
	public static final String ACTION_AGENCY_ACTION = "agencyAction";
	public static final String ACTION_PENDING_AGENCIES = "pendingAgencies";
	public static final String ACTION_DELETED_AGENCIES = "deletedAgencies";
	public static final String ACTION_DELETED_USERS = "deletedUsers";
	public static final String ACTION_UPDATE_PROFILE = "updateProfile";
	public static final String ACTION_CHANGE_PASSWORD = "changePassword";

	// ===================ADMIN SERVLET USER ACTION TYPES ===================
	public static final String USER_ACTIVATE = "activate";
	public static final String USER_DEACTIVATE = "deactivate";
	public static final String USER_DELETE = "delete";

	// ===================ADMIN SERVLET AGENCY ACTION TYPES ===================
	public static final String AGENCY_ACTIVATE = "activate";
	public static final String AGENCY_DEACTIVATE = "deactivate";
	public static final String AGENCY_DELETE = "delete";
	public static final String AGENCY_APPROVE = "approve";
	public static final String AGENCY_REJECT = "reject";

	// =================== ADMIN SERVLET MESSAGES ===================
	public static final String SUCCESS_PASSWORD_CHANGE = "Password changed successfully!";
	public static final String ERROR_PASSWORD_CHANGE = "Failed to change password!";
	public static final String ERROR_OLD_PASSWORD = "Old password is incorrect";
	public static final String SUCCESS_PROFILE_UPDATE = "Profile updated successfully!";
	public static final String ERROR_PROFILE_UPDATE = "Failed to update profile!";
	public static final String ERROR_INVALID_ACTION = "Invalid Action";
	public static final String ERROR_START_AFTER_END_DATE = "Start date cannot be after end date.";

	// =================== AUTH SERVLET ACTION ===================
	public static final String ACTION_REGISTER_USER = "registerAsUser";
	public static final String ACTION_VERIFY_OTP_USER = "verifyOTPAndRegisterUser";
	public static final String ACTION_REGISTER_AGENCY = "registerAsAgency";
	public static final String ACTION_VERIFY_OTP_AGENCY = "verifyOTPAndRegisterAgency";
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_LOGOUT = "logout";

	// =================== AUTH SERVLET OTP RESULTS ===================
	public static final String OTP_SUCCESS = "SUCCESS";
	public static final String OTP_EXPIRED = "EXPIRED";
	public static final String OTP_INVALID = "INVALID";
	public static final String OTP_WAIT = "WAIT";
	public static final String OTP_LIMIT = "LIMIT";

	// =================== AUTH SERVLET MESSAGES ===================
	public static final String ERROR_EMAIL_REQUIRED = "Email is required.";
	public static final String ERROR_EMAIL_INVALID = "Enter valid email.";
	public static final String ERROR_EMAIL_EXISTS = "Email already exists.";
	public static final String ERROR_OTP_FAILED = "OTP verification failed. Please try again.";
	public static final String ERROR_OTP_EXPIRED = "OTP expired. Please try again.";
	public static final String ERROR_OTP_INVALID = "Invalid OTP. Please try again.";
	public static final String ERROR_OTP_WAIT = "Please wait 1 minute before requesting another OTP.";
	public static final String ERROR_OTP_LIMIT = "You have reached maximum OTP requests for today.";
	public static final String SUCCESS_OTP_SENT = "OTP sent to your email. Please verify to complete registration.";
	public static final String SUCCESS_USER_REGISTERED = "User registered successfully. Please login.";
	public static final String SUCCESS_AGENCY_REGISTERED = "Agency registered successfully! Waiting for admin approval.";
	public static final String ERROR_ROLE_INVALID = "Invalid role selected!";
	public static final String ERROR_SESSION_EXPIRED = "Session expired. Please start registration again.";

	// =================== BOOKING SERVLET ACTIONS ===================
	public static final String ACTION_CREATE_BOOKING = "createBooking";
	public static final String ACTION_AUTO_CANCEL_BOOKING = "autoCancelBooking";
	public static final String ACTION_PAYMENT_CONFIRM = "paymentConfirm";
	public static final String ACTION_PAYMENT_REJECT = "paymentReject";
	public static final String ACTION_VERIFY_PAYMENT = "verifyPayment";
	public static final String ACTION_CANCEL_BOOKING = "cancelBooking";
	public static final String ACTION_CANCEL_TRAVELER = "cancelTraveler";
	public static final String ACTION_VIEW_BOOKING_FORM = "viewBookingForm";
	public static final String ACTION_BOOKING_HISTORY = "viewBookings";
	public static final String ACTION_VIEW_TRAVELERS = "viewTravelers";
	public static final String ACTION_PAYMENT_HISTORY = "paymentHistory";
	public static final String ACTION_DOWNLOAD_INVOICE = "downloadInvoice";

	// =================== BOOKING MESSAGES ===================
	public static final String ERROR_INVALID_USER = "Invalid User !";
	public static final String ERROR_USER_NOT_LOGGED_IN = "User is not logged in!";
	public static final String ERROR_BOOKING_ID_MISSING = "Booking ID is missing!";
	public static final String ERROR_INVALID_BOOKING_ID = "Invalid Booking ID!";
	public static final String ERROR_BOOKING_NOT_FOUND = "Booking not found!";
	public static final String ERROR_NOT_AUTHORIZED_DOWNLOAD = "You are not authorized to download this invoice!";
	public static final String ERROR_UNABLE_GENERATE_INVOICE = "Unable to generate invoice.";
	public static final String ERROR_INVALID_TRAVELER = "Invalid traveler or booking mismatch!";
	public static final String ERROR_TRAVELER_ALREADY_CANCELLED = "Traveler already cancelled!";
	public static final String ERROR_UNAUTHORIZED_CANCEL = "Unauthorized cancellation attempt!";
	public static final String ERROR_PACKAGE_NOT_FOUND = "Package not found!";
	public static final String ERROR_CANNOT_CANCEL_PAST_BOOKING = "Cannot cancel traveler for past booking!";
	public static final String ERROR_CANNOT_CANCEL_PAST_BOOKINGS = "Cannot cancel past bookings!";
	public static final String ERROR_NO_OF_TRAVELLERS_MISSING = "No of travellers is missing!";
	public static final String ERROR_INVALID_NO_OF_TRAVELLERS = "Invalid no of travellers!";
	public static final String ERROR_NOT_ENOUGH_SEATS = "Not enough seats available!";
	public static final String ERROR_CONCURRENT_BOOKING = "Booking failed due to concurrent bookings. Please try again.";
	public static final String ERROR_PACKAGE_INACTIVE = "Package not found or inactive.";
	public static final String ERROR_SOMETHING_WENT_WRONG = "Something went wrong. Please try again.";
	public static final String ERROR_BOOKING_ID_AMOUNT_MISSING = "Booking ID or Amount is missing!";
	public static final String ERROR_INVALID_BOOKING = "Invalid Booking!";
	public static final String ERROR_BOOKING_ALREADY_CANCELLED = "Booking already cancelled.";
	public static final String ERROR_PAYMENT_VERIFICATION_FAILED = "Payment verification failed!";
	public static final String ERROR_PAYMENT_SESSION_EXPIRED = "Session expired. Cannot verify payment.";
	public static final String ERROR_PAYMENT_PROCESSING_FAILED = "Payment processing failed. Please try again.";
	public static final String ERROR_FETCH_PAYMENT_HISTORY = "Unable to fetch payment history.";
	public static final String ERROR_FETCH_BOOKING_HISTORY = "Unable to fetch booking history.";
	public static final String ERROR_ALREADY_BOOKED = "You have already booked this package.";
	public static final String ERROR_BOOKING_FAILED = "Booking failed. Please try again.";
	public static final String SUCCESS_TRAVELER_CANCELLED = "Traveler cancelled successfully! Refund: ";
	public static final String SUCCESS_BOOKING_CANCELLED = "Your booking is canceled! Refund: ";
	public static final String SUCCESS_PAYMENT_CONFIRMED = "Payment Successful ! Booking Confirmed";
	public static final String SUCCESS_PAYMENT_REJECTED = "Payment rejected. Booking cancelled.";
	public static final String SUCCESS_AUTO_CANCELLED = "Booking auto-cancelled successfully.";
	public static final String SUCCESS_BOOKING_CONFIRMED = "Payment Successful & Booking Confirmed!";

//	=============================PACKAGE SERVLET ACTION===================================
	public static final String ACTION_PACKAGES_LIST = "packageList";
	// =================== PACKAGE SERVLET MESSAGES ===================
	public static final String ERROR_FETCH_PACKAGES = "Unable to fetch packages. Please try again.";

	// =================== USER SERVLET ACTIONS ===================

	public static final String ACTION_VIEW_PROFILE = "viewProfile";

	// =================== USER SERVLET MESSAGES ===================
	public static final String ERROR_UNEXPECTED_ACTION = "Unexpected action for user!";
	public static final String ERROR_FETCH_DASHBOARD = "Unable to load dashboard. Please try again.";

	private Constants() {
		// prevent instantiation
	}
}
