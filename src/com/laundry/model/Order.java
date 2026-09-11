/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 *
 * @author ASUS
 */
public class Order {
    private String orderId;
    private int customerId;
    private int serviceId;
    private BigDecimal weightOrQty;
    private BigDecimal totalPrice;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private String picStaff;
    private String notes;
    private LocalDateTime orderDate;
    private LocalDateTime estimatedCompletion;
    private LocalDateTime actualCompletion;
    
    private Customer customer;
    private ServiceType service;
    
    public enum PaymentMethod { CASH, DEBIT, EWALLET }
    public enum PaymentStatus { PENDING, PAID }
    public enum OrderStatus { RECEIVED, WASHING, DRYING, READY, DELIVERING, COMPLETED }
    
    public Order() {}
    
    public Order(String orderId, int customerId, int serviceId, BigDecimal weightOrQty,
                BigDecimal totalPrice, PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.weightOrQty = weightOrQty;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.orderStatus = OrderStatus.RECEIVED;
    }
    
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    
    public BigDecimal getWeightOrQty() { return weightOrQty; }
    public void setWeightOrQty(BigDecimal weightOrQty) { this.weightOrQty = weightOrQty; }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { 
        this.paymentMethod = paymentMethod; 
    }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { 
        this.paymentStatus = paymentStatus; 
    }
    
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    
    public String getPicStaff() { return picStaff; }
    public void setPicStaff(String picStaff) { this.picStaff = picStaff; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getEstimatedCompletion() { return estimatedCompletion; }
    public void setEstimatedCompletion(LocalDateTime estimatedCompletion) { 
        this.estimatedCompletion = estimatedCompletion; 
    }
    
    public LocalDateTime getActualCompletion() { return actualCompletion; }
    public void setActualCompletion(LocalDateTime actualCompletion) { 
        this.actualCompletion = actualCompletion; 
    }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public ServiceType getService() { return service; }
    public void setService(ServiceType service) { this.service = service; }
}
