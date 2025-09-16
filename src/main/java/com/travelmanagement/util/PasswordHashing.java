package com.travelmanagement.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashing {

	public static String ecryptPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
	}

	public static boolean checkPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
