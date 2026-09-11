/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.model;
import java.math.BigDecimal;
/**
 *
 * @author ASUS
 */
public class ServiceType {
    private int serviceId;
    private String serviceName;
    private ServiceCategory serviceCategory;
    private ServiceTier tier;
    private BigDecimal pricePerUnit;
    private int estimatedHours;
    private String description;
    
    public enum ServiceCategory { KILOAN, SATUAN }
    public enum ServiceTier { STANDARD, PREMIUM, EXPRESS }
    
    public ServiceType() {}
    
    public ServiceType(String serviceName, ServiceCategory category, ServiceTier tier, 
                      BigDecimal pricePerUnit, int estimatedHours) {
        this.serviceName = serviceName;
        this.serviceCategory = category;
        this.tier = tier;
        this.pricePerUnit = pricePerUnit;
        this.estimatedHours = estimatedHours;
    }
    
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    
    public ServiceCategory getServiceCategory() { return serviceCategory; }
    public void setServiceCategory(ServiceCategory serviceCategory) { 
        this.serviceCategory = serviceCategory; 
    }
    
    public ServiceTier getTier() { return tier; }
    public void setTier(ServiceTier tier) { this.tier = tier; }
    
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { 
        this.pricePerUnit = pricePerUnit; 
    }
    
    public int getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(int estimatedHours) { 
        this.estimatedHours = estimatedHours; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return serviceName + " - Rp " + pricePerUnit + " (" + tier + ")";
    }
}
