/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.model;
import java.time.LocalDateTime;
/**
 *
 * @author ASUS
 */
public class Delivery {
    private int deliveryId;
    private String orderId;
    private String courierName;
    private LocalDateTime scheduledDateTime;
    private DeliveryStatus deliveryStatus;
    private String deliveryArea;
    private LocalDateTime actualDeliveryTime;
    private String recipientName;
    private String notes;
    
    private Order order;
    
    public enum DeliveryStatus { SCHEDULED, IN_TRANSIT, DELIVERED, FAILED }
    
    public Delivery() {}
    
    public Delivery(String orderId, String courierName, LocalDateTime scheduledDateTime,
                   String deliveryArea) {
        this.orderId = orderId;
        this.courierName = courierName;
        this.scheduledDateTime = scheduledDateTime;
        this.deliveryArea = deliveryArea;
        this.deliveryStatus = DeliveryStatus.SCHEDULED;
    }
    
    public int getDeliveryId() { return deliveryId; }
    public void setDeliveryId(int deliveryId) { this.deliveryId = deliveryId; }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }
    
    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) { 
        this.scheduledDateTime = scheduledDateTime; 
    }
    
    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { 
        this.deliveryStatus = deliveryStatus; 
    }
    
    public String getDeliveryArea() { return deliveryArea; }
    public void setDeliveryArea(String deliveryArea) { this.deliveryArea = deliveryArea; }
    
    public LocalDateTime getActualDeliveryTime() { return actualDeliveryTime; }
    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) { 
        this.actualDeliveryTime = actualDeliveryTime; 
    }
    
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { 
        this.recipientName = recipientName; 
    }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}
