/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.util;
import com.laundry.model.User;
import java.time.LocalDateTime;
/**
 *
 * @author ASUS
 */

public class SessionManager {
    private static User currentUser = null;
    private static LocalDateTime loginTime = null;
    
    /**
     * Set current logged in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        loginTime = LocalDateTime.now();
    }
    
    /**
     * Get current logged in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get login time
     */
    public static LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Clear session (logout)
     */
    public static void clearSession() {
        currentUser = null;
        loginTime = null;
    }
    
    /**
     * Get current username
     */
    public static String getCurrentUsername() {
        return currentUser != null ? currentUser.getUsername() : "Guest";
    }
    
    /**
     * Get current user full name
     */
    public static String getCurrentUserFullName() {
        return currentUser != null ? currentUser.getFullName() : "Guest";
    }
    
    /**
     * Get current user role
     */
    public static User.UserRole getCurrentUserRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }
    
    /**
     * Get current user role as String
     */
    public static String getCurrentUserRoleString() {
        return currentUser != null ? currentUser.getRole().toString() : "GUEST";
    }
    
    /**
     * Check if current user is admin
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == User.UserRole.ADMIN;
    }
    
    /**
     * Check if current user is manager
     */
    public static boolean isManager() {
        return currentUser != null && currentUser.getRole() == User.UserRole.MANAGER;
    }
    
    /**
     * Check if current user is staff
     */
    public static boolean isStaff() {
        return currentUser != null && currentUser.getRole() == User.UserRole.STAFF;
    }
}