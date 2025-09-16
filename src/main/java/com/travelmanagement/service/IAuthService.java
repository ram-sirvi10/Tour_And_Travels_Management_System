package com.travelmanagement.service;

import com.travelmanagement.model.User;

public interface IAuthService {
    /**
     * Authenticate user with email and password.
     * @param email User email
     * @param password Plain text password (will be hashed internally)
     * @return Authenticated User object if valid, otherwise null
     * @throws Exception if DB error or hashing issue
     */
    User login(String email, String password) throws Exception;

    /**
     * Logout the current user (invalidate session).
     * @param sessionId Session identifier (can be HttpSession ID or token)
     * @return true if logout successful
     */
    boolean logout(String sessionId);

    /**
     * Check if the session is valid for a user.
     * @param sessionId Session identifier
     * @return true if session is active
     */
    boolean isAuthenticated(String sessionId);
}
