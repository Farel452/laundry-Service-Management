/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.dao;
import com.laundry.config.DatabaseConfig;
import com.laundry.model.Delivery;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class DeliveryDAO {
    private Connection connection;
    
   public DeliveryDAO() {
    try {
        this.connection = DatabaseConfig.getConnection();
    } catch (SQLException e) {
        System.err.println("Error di DAO: " + e.getMessage());
    }
}
    
    public boolean scheduleDelivery(Delivery delivery) {
        String sql = "INSERT INTO deliveries (order_id, courier_name, scheduled_date, " +
                    "scheduled_time, delivery_area, recipient_name, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            LocalDateTime scheduled = delivery.getScheduledDateTime();
            
            stmt.setString(1, delivery.getOrderId());
            stmt.setString(2, delivery.getCourierName());
            stmt.setDate(3, Date.valueOf(scheduled.toLocalDate()));
            stmt.setTime(4, Time.valueOf(scheduled.toLocalTime()));
            stmt.setString(5, delivery.getDeliveryArea());
            stmt.setString(6, delivery.getRecipientName());
            stmt.setString(7, delivery.getNotes());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    delivery.setDeliveryId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Delivery getDeliveryById(int deliveryId) {
        String sql = "SELECT * FROM deliveries WHERE delivery_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractDeliveryFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Delivery> getDeliveriesByOrderId(String orderId) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries WHERE order_id = ? ORDER BY scheduled_date, scheduled_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                deliveries.add(extractDeliveryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public List<Delivery> getDeliveriesByStatus(Delivery.DeliveryStatus status) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, o.order_id, c.name as customer_name, c.address, c.phone " +
                    "FROM deliveries d " +
                    "JOIN orders o ON d.order_id = o.order_id " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "WHERE d.delivery_status = ? " +
                    "ORDER BY d.scheduled_date, d.scheduled_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                deliveries.add(extractDeliveryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public List<Delivery> getDeliveriesByDate(java.time.LocalDate date) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, o.order_id, c.name as customer_name, c.address, c.phone " +
                    "FROM deliveries d " +
                    "JOIN orders o ON d.order_id = o.order_id " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "WHERE d.scheduled_date = ? " +
                    "ORDER BY d.scheduled_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                deliveries.add(extractDeliveryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public List<Delivery> getDeliveriesByCourier(String courierName, java.time.LocalDate date) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT d.*, o.order_id, c.name as customer_name, c.address, c.phone " +
                    "FROM deliveries d " +
                    "JOIN orders o ON d.order_id = o.order_id " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "WHERE d.courier_name = ? AND d.scheduled_date = ? " +
                    "ORDER BY d.scheduled_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courierName);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                deliveries.add(extractDeliveryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public List<Delivery> getDeliveriesByArea(String area) {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries WHERE delivery_area LIKE ? " +
                    "AND delivery_status != 'DELIVERED' " +
                    "ORDER BY scheduled_date, scheduled_time";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + area + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                deliveries.add(extractDeliveryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deliveries;
    }
    
    public boolean updateDeliveryStatus(int deliveryId, Delivery.DeliveryStatus status, 
                                       String recipientName) {
        String sql = "UPDATE deliveries SET delivery_status = ?, recipient_name = ?, " +
                    "actual_delivery_time = ? WHERE delivery_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setString(2, recipientName);
            
            if (status == Delivery.DeliveryStatus.DELIVERED) {
                stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            } else {
                stmt.setTimestamp(3, null);
            }
            
            stmt.setInt(4, deliveryId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean rescheduleDelivery(int deliveryId, LocalDateTime newSchedule) {
        String sql = "UPDATE deliveries SET scheduled_date = ?, scheduled_time = ? " +
                    "WHERE delivery_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(newSchedule.toLocalDate()));
            stmt.setTime(2, Time.valueOf(newSchedule.toLocalTime()));
            stmt.setInt(3, deliveryId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean cancelDelivery(int deliveryId) {
        String sql = "DELETE FROM deliveries WHERE delivery_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Delivery extractDeliveryFromResultSet(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(rs.getInt("delivery_id"));
        delivery.setOrderId(rs.getString("order_id"));
        delivery.setCourierName(rs.getString("courier_name"));
        
        Date date = rs.getDate("scheduled_date");
        Time time = rs.getTime("scheduled_time");
        LocalDateTime scheduled = LocalDateTime.of(
            date.toLocalDate(),
            time.toLocalTime()
        );
        delivery.setScheduledDateTime(scheduled);
        
        delivery.setDeliveryStatus(
            Delivery.DeliveryStatus.valueOf(rs.getString("delivery_status"))
        );
        delivery.setDeliveryArea(rs.getString("delivery_area"));
        delivery.setRecipientName(rs.getString("recipient_name"));
        delivery.setNotes(rs.getString("notes"));
        
        if (rs.getTimestamp("actual_delivery_time") != null) {
            delivery.setActualDeliveryTime(
                rs.getTimestamp("actual_delivery_time").toLocalDateTime()
            );
        }
        
        return delivery;
    }
    
    public int getTotalDeliveries() {
        String sql = "SELECT COUNT(*) FROM deliveries";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getDeliveryCountByStatus(Delivery.DeliveryStatus status) {
        String sql = "SELECT COUNT(*) FROM deliveries WHERE delivery_status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
