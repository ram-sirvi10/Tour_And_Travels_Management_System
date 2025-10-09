package com.travelmanagement.util;

import java.util.Random;

import jakarta.servlet.http.HttpSession;

public class OTPUtil {

	private static final int OTP_EXPIRY_MINUTES = 5;
	private static final int OTP_MAX_ATTEMPTS = 3;
	private static final int OTP_MIN_INTERVAL = 60 * 1000;
	private static final int OTP_MAX_PER_DAY = 5;

	public static String generateRandomOTP(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}

	public static String generateOTP(HttpSession session) {
		long currentTime = System.currentTimeMillis();

		Long lastOtpTime = (Long) session.getAttribute("lastOtpTime");
		if (lastOtpTime != null && (currentTime - lastOtpTime) < OTP_MIN_INTERVAL) {
			return "WAIT";
		}

		Integer otpRequestCount = (Integer) session.getAttribute("otpRequestCount");
		if (otpRequestCount == null)
			otpRequestCount = 0;
		if (otpRequestCount >= OTP_MAX_PER_DAY) {
			return "LIMIT";
		}

		String otp = generateRandomOTP(6);

		session.setAttribute("otp", otp);
		session.setAttribute("otpGeneratedTime", currentTime);
		session.setAttribute("otpAttempts", 0);
		session.setAttribute("lastOtpTime", currentTime);
		session.setAttribute("otpRequestCount", otpRequestCount + 1);

		return otp;
	}

	public static String verifyOTP(HttpSession session, String enteredOtp) {
		String sessionOtp = (String) session.getAttribute("otp");
		Long otpTime = (Long) session.getAttribute("otpGeneratedTime");
		Integer attempts = (Integer) session.getAttribute("otpAttempts");
		if (attempts == null)
			attempts = 0;

		if (sessionOtp == null || otpTime == null) {
			return "NO_OTP";
		}

		if ((System.currentTimeMillis() - otpTime) > OTP_EXPIRY_MINUTES * 60 * 1000) {
			return "EXPIRED";
		}

		if (attempts >= OTP_MAX_ATTEMPTS) {
			return "ATTEMPTS_EXCEEDED";
		}

		if (sessionOtp.equals(enteredOtp)) {
			session.setAttribute("userOtpVerified", true);
			return "SUCCESS";
		} else {
			session.setAttribute("otpAttempts", attempts + 1);
			return "INVALID";
		}
	}
}
