/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.dao;
import com.laundry.config.DatabaseConfig;
import com.laundry.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 *
 * @author ASUS
 */
public class OrderDAO {
    private Connection connection;
    
   public OrderDAO() {
    try {
        this.connection = DatabaseConfig.getConnection();
    } catch (SQLException e) {
        System.err.println("Error di DAO: " + e.getMessage());
    }
}
    
    public String generateOrderId() {
        String sql = "SELECT COUNT(*) FROM orders WHERE DATE(order_date) = CURDATE()";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                String date = LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")
                );
                return String.format("MLG-%s-%03d", date, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "MLG-ERROR-001";
    }
    
    public boolean addOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, customer_id, service_id, weight_or_qty, " +
                    "total_price, payment_method, payment_status, order_status, pic_staff, notes, " +
                    "estimated_completion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderId());
            stmt.setInt(2, order.getCustomerId());
            stmt.setInt(3, order.getServiceId());
            stmt.setBigDecimal(4, order.getWeightOrQty());
            stmt.setBigDecimal(5, order.getTotalPrice());
            stmt.setString(6, order.getPaymentMethod().name());
            stmt.setString(7, order.getPaymentStatus().name());
            stmt.setString(8, order.getOrderStatus().name());
            stmt.setString(9, order.getPicStaff());
            stmt.setString(10, order.getNotes());
            stmt.setTimestamp(11, Timestamp.valueOf(order.getEstimatedCompletion()));
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                addOrderTracking(order.getOrderId(), Order.OrderStatus.RECEIVED, 
                               order.getPicStaff(), "Order received");
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Order getOrderById(String orderId) {
        String sql = "SELECT o.*, c.name as customer_name, c.phone, c.address, c.kos_name, " +
                    "s.service_name, s.service_category, s.tier, s.price_per_unit " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "JOIN service_types s ON o.service_id = s.service_id " +
                    "WHERE o.order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractOrderFromResultSet(rs, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name, c.phone, " +
                    "s.service_name, s.tier " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "JOIN service_types s ON o.service_id = s.service_id " +
                    "ORDER BY o.order_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name, c.phone, " +
                    "s.service_name, s.tier " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "JOIN service_types s ON o.service_id = s.service_id " +
                    "WHERE o.order_status = ? " +
                    "ORDER BY o.order_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public List<Order> searchOrders(String keyword) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, c.name as customer_name, c.phone, " +
                    "s.service_name, s.tier " +
                    "FROM orders o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "JOIN service_types s ON o.service_id = s.service_id " +
                    "WHERE o.order_id LIKE ? OR c.name LIKE ? OR c.phone LIKE ? " +
                    "ORDER BY o.order_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public boolean updateOrderStatus(String orderId, Order.OrderStatus newStatus, 
                                    String updatedBy, String notes) {
        String sql = "UPDATE orders SET order_status = ?, pic_staff = ? WHERE order_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus.name());
            stmt.setString(2, updatedBy);
            stmt.setString(3, orderId);
            
            boolean success = stmt.executeUpdate() > 0;
            
            if (success) {
                // Add tracking entry
                addOrderTracking(orderId, newStatus, updatedBy, notes);
                
                // Update actual completion if status is COMPLETED
                if (newStatus == Order.OrderStatus.COMPLETED) {
                    updateActualCompletion(orderId);
                }
            }
            
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updatePaymentStatus(String orderId, Order.PaymentStatus status) {
        String sql = "UPDATE orders SET payment_status = ? WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setString(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean addOrderTracking(String orderId, Order.OrderStatus status, 
                                    String updatedBy, String notes) {
        String sql = "INSERT INTO order_tracking (order_id, status, updated_by, notes) " +
                    "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            stmt.setString(2, status.name());
            stmt.setString(3, updatedBy);
            stmt.setString(4, notes);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean updateActualCompletion(String orderId) {
        String sql = "UPDATE orders SET actual_completion = NOW() WHERE order_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<String> getOrderTracking(String orderId) {
        List<String> tracking = new ArrayList<>();
        String sql = "SELECT status, updated_by, timestamp, notes FROM order_tracking " +
                    "WHERE order_id = ? ORDER BY timestamp";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String entry = String.format("[%s] %s - %s (by %s)",
                    rs.getTimestamp("timestamp"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("updated_by"));
                tracking.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tracking;
    }
    
    private Order extractOrderFromResultSet(ResultSet rs, boolean fullDetails) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getString("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setServiceId(rs.getInt("service_id"));
        order.setWeightOrQty(rs.getBigDecimal("weight_or_qty"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setPaymentMethod(Order.PaymentMethod.valueOf(rs.getString("payment_method")));
        order.setPaymentStatus(Order.PaymentStatus.valueOf(rs.getString("payment_status")));
        order.setOrderStatus(Order.OrderStatus.valueOf(rs.getString("order_status")));
        order.setPicStaff(rs.getString("pic_staff"));
        order.setNotes(rs.getString("notes"));
        
        Timestamp orderTs = rs.getTimestamp("order_date");
        if (orderTs != null) {
            order.setOrderDate(orderTs.toLocalDateTime());
        }
        
        Timestamp estimatedTs = rs.getTimestamp("estimated_completion");
        if (estimatedTs != null) {
            order.setEstimatedCompletion(estimatedTs.toLocalDateTime());
        }
        
        if (rs.getTimestamp("actual_completion") != null) {
            order.setActualCompletion(rs.getTimestamp("actual_completion").toLocalDateTime());
        }
        
        Customer customer = new Customer();
        customer.setName(rs.getString("customer_name"));
        customer.setPhone(rs.getString("phone"));
        if (fullDetails) {
            customer.setAddress(rs.getString("address"));
            customer.setKosName(rs.getString("kos_name"));
        }
        order.setCustomer(customer);
        
        ServiceType service = new ServiceType();
        service.setServiceName(rs.getString("service_name"));
        service.setTier(ServiceType.ServiceTier.valueOf(rs.getString("tier")));
        order.setService(service);
        
        return order;
    }
    
    public int getTotalOrders() {
        return getOrderCountByStatus(null);
    }
    
    public int getOrderCountByStatus(Order.OrderStatus status) {
        String sql = status == null ? 
            "SELECT COUNT(*) FROM orders" :
            "SELECT COUNT(*) FROM orders WHERE order_status = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (status != null) {
                stmt.setString(1, status.name());
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(total_price) FROM orders WHERE payment_status = 'PAID'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
