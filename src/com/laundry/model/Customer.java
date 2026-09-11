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
public class Customer {
    private int customerId;
    private String name;
    private String phone;
    private String address;
    private String kosName;
    private String email;
    private LocalDateTime createdAt;
    
    public Customer() {}
    
    public Customer(String name, String phone, String address, String kosName, String email) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.kosName = kosName;
        this.email = email;
    }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getKosName() { return kosName; }
    public void setKosName(String kosName) { this.kosName = kosName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
