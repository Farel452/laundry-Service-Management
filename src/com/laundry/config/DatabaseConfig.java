/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author ASUS
 */
public class DatabaseConfig {
    private static Connection mysqlconfig;

    public static Connection getConnection() throws SQLException {
        if (mysqlconfig == null || mysqlconfig.isClosed()) {
            try {
                String url = "jdbc:mysql://localhost/laundry_management";
                String user = "root";
                String pass = "";
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver()); // Driver versi baru
                mysqlconfig = DriverManager.getConnection(url, user, pass);
            } catch (Exception e) {
                System.err.println("Koneksi Gagal: " + e.getMessage());
            }
        }
        return mysqlconfig;
    }

    public static Object getInstance() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
