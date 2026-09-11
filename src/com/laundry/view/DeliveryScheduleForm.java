/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.*;
import com.laundry.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author ASUS
 */
public class DeliveryScheduleForm extends JFrame {
    private JComboBox<String> cmbOrder;
    private JTextField txtCourier, txtArea, txtRecipient;
    private JSpinner spinnerDate, spinnerTime;
    private JTextArea txtNotes;
    
    private OrderDAO orderDAO;
    private DeliveryDAO deliveryDAO;
    
    public DeliveryScheduleForm() {
        orderDAO = new OrderDAO();
        deliveryDAO = new DeliveryDAO();
        
        setTitle("Jadwalkan Pengiriman");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        loadReadyOrders();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(241, 196, 15));
        JLabel lblHeader = new JLabel("JADWALKAN PENGIRIMAN");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Order Selection
        gbc.gridy = 0;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Pilih Order:"), gbc);
        
        gbc.gridx = 1;
        cmbOrder = new JComboBox<>();
        formPanel.add(cmbOrder, gbc);
        
        // Courier
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Nama Kurir:"), gbc);
        
        gbc.gridx = 1;
        txtCourier = new JTextField(20);
        formPanel.add(txtCourier, gbc);
        
        // Delivery Area
        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Area Pengiriman:"), gbc);
        
        gbc.gridx = 1;
        txtArea = new JTextField(20);
        formPanel.add(txtArea, gbc);
        
        // Scheduled Date
        gbc.gridy = 3;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        
        gbc.gridx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        spinnerDate = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy");
        spinnerDate.setEditor(dateEditor);
        formPanel.add(spinnerDate, gbc);
        
        // Scheduled Time
        gbc.gridy = 4;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Jam:"), gbc);
        
        gbc.gridx = 1;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        spinnerTime = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerTime, "HH:mm");
        spinnerTime.setEditor(timeEditor);
        formPanel.add(spinnerTime, gbc);
        
        // Recipient
        gbc.gridy = 5;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Penerima:"), gbc);
        
        gbc.gridx = 1;
        txtRecipient = new JTextField(20);
        formPanel.add(txtRecipient, gbc);
        
        // Notes
        gbc.gridy = 6;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Catatan:"), gbc);
        
        gbc.gridx = 1;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        formPanel.add(new JScrollPane(txtNotes), gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        JButton btnSchedule = new JButton("Jadwalkan");
        btnSchedule.setBackground(new Color(46, 204, 113));
        btnSchedule.setForeground(Color.WHITE);
        btnSchedule.setFont(new Font("Arial", Font.BOLD, 12));
        btnSchedule.setPreferredSize(new Dimension(130, 40));
        btnSchedule.addActionListener(e -> scheduleDelivery());
        
        JButton btnCancel = new JButton("Batal");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setPreferredSize(new Dimension(130, 40));
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSchedule);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadReadyOrders() {
        cmbOrder.removeAllItems();
        List<Order> readyOrders = orderDAO.getOrdersByStatus(Order.OrderStatus.READY);
        
        for (Order order : readyOrders) {
            String displayText = String.format("%s - %s (%s)", 
                order.getOrderId(),
                order.getCustomer().getName(),
                order.getCustomer().getAddress());
            cmbOrder.addItem(displayText);
        }
    }
    
    private void scheduleDelivery() {
        if (cmbOrder.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih order terlebih dahulu!");
            return;
        }
        
        if (txtCourier.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama kurir!");
            return;
        }
        
        // Extract order ID
        String selected = (String) cmbOrder.getSelectedItem();
        String orderId = selected.split(" - ")[0];
        
        // Create delivery schedule
        // Note: DeliveryDAO implementation needed
        JOptionPane.showMessageDialog(this, 
            "Pengiriman berhasil dijadwalkan untuk Order: " + orderId);
        
        // Update order status to DELIVERING
        orderDAO.updateOrderStatus(orderId, Order.OrderStatus.DELIVERING, 
                                  txtCourier.getText().trim(), "Scheduled for delivery");
        
        dispose();
    }
}
