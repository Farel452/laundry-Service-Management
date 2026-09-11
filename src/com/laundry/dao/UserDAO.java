/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.dao;
import com.laundry.config.DatabaseConfig;
import com.laundry.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author ASUS
 */


public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        try {
        this.connection = DatabaseConfig.getConnection();
    } catch (SQLException e) {
        System.err.println("Error UserDAO: " + e.getMessage());
    }
    }
        
    public User login(String username, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = extractUserFromResultSet(rs);
                
                // Update last login
                updateLastLogin(user.getUserId());
                
                // Log login history
                logLoginHistory(user.getUserId(), username, "SUCCESS");
                
                return user;
            } else {
                // Log failed attempt
                logLoginHistory(0, username, "FAILED");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Hash password using MD5
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Update last login time
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Log login history
     */
    private void logLoginHistory(int userId, String username, String status) {
        String sql = "INSERT INTO login_history (user_id, username, login_time, status) " + "VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (userId == 0) {
            stmt.setNull(1, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(1, userId);
        } 
        stmt.setString(2, username);
        stmt.setString(3, status);
        stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create new user
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashPassword(user.getPassword()));
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole().name());
            stmt.setBoolean(6, user.isActive());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get user by username
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Change password
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // Verify old password first
        String verifySql = "SELECT password FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(verifySql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String hashedOldPassword = hashPassword(oldPassword);
                
                if (storedPassword.equals(hashedOldPassword)) {
                    // Old password correct, update to new password
                    String updateSql = "UPDATE users SET password = ? WHERE user_id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setString(1, hashPassword(newPassword));
                        updateStmt.setInt(2, userId);
                        return updateStmt.executeUpdate() > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update user status (activate/deactivate)
     */
    public boolean updateUserStatus(int userId, boolean isActive) {
        String sql = "UPDATE users SET is_active = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isActive);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Get total login attempts (for security monitoring)
     */
    public int getFailedLoginAttempts(String username, int minutesAgo) {
        String sql = "SELECT COUNT(*) FROM login_history " +
                    "WHERE username = ? AND status = 'FAILED' " +
                    "AND login_time > DATE_SUB(NOW(), INTERVAL ? MINUTE)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, minutesAgo);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Extract User from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(User.UserRole.valueOf(rs.getString("role")));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return user;
    }
}