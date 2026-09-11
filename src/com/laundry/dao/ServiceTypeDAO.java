/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.dao;
import com.laundry.config.DatabaseConfig;
import com.laundry.model.ServiceType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class ServiceTypeDAO {
    private Connection connection;
    
    public ServiceTypeDAO() {
    try {
        this.connection = DatabaseConfig.getConnection();
    } catch (SQLException e) {
        System.err.println("Error di DAO: " + e.getMessage());
    }
}
    
    public List<ServiceType> getAllServices() {
        List<ServiceType> services = new ArrayList<>();
        String sql = "SELECT * FROM service_types ORDER BY service_category, tier";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    
    public ServiceType getServiceById(int serviceId) {
        String sql = "SELECT * FROM service_types WHERE service_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractServiceFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<ServiceType> getServicesByCategory(ServiceType.ServiceCategory category) {
        List<ServiceType> services = new ArrayList<>();
        String sql = "SELECT * FROM service_types WHERE service_category = ? ORDER BY tier";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    
    public List<ServiceType> getServicesByTier(ServiceType.ServiceTier tier) {
        List<ServiceType> services = new ArrayList<>();
        String sql = "SELECT * FROM service_types WHERE tier = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tier.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                services.add(extractServiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    
    private ServiceType extractServiceFromResultSet(ResultSet rs) throws SQLException {
        ServiceType service = new ServiceType();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setServiceCategory(
            ServiceType.ServiceCategory.valueOf(rs.getString("service_category"))
        );
        service.setTier(ServiceType.ServiceTier.valueOf(rs.getString("tier")));
        service.setPricePerUnit(rs.getBigDecimal("price_per_unit"));
        service.setEstimatedHours(rs.getInt("estimated_hours"));
        service.setDescription(rs.getString("description"));
        return service;
    }
}
