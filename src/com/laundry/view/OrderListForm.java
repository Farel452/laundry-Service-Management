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
public class OrderListForm extends JFrame {
     private JTable tblOrders;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbStatusFilter;
    private OrderDAO orderDAO;
    
    public OrderListForm() {
        orderDAO = new OrderDAO();
        
        setTitle("Daftar Pesanan");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        loadOrders();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel lblHeader = new JLabel("DAFTAR SEMUA PESANAN");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        filterPanel.add(new JLabel("Status:"));
        String[] statuses = {"Semua", "RECEIVED", "WASHING", "DRYING", 
                           "READY", "DELIVERING", "COMPLETED"};
        cmbStatusFilter = new JComboBox<>(statuses);
        cmbStatusFilter.addActionListener(e -> filterOrders());
        filterPanel.add(cmbStatusFilter);
        
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(new JLabel("Cari:"));
        txtSearch = new JTextField(20);
        filterPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchOrders());
        filterPanel.add(btnSearch);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadOrders());
        filterPanel.add(btnRefresh);
        
        String[] columns = {"Order ID", "Customer", "Telepon", "Layanan", "Tier", 
                          "Berat/Qty", "Total", "Status", "Pembayaran", "Tgl Order"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblOrders = new JTable(tableModel);
        tblOrders.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollTable = new JScrollPane(tblOrders);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnViewTracking = new JButton("Lihat Tracking");
        btnViewTracking.setBackground(new Color(52, 152, 219));
        btnViewTracking.setForeground(Color.WHITE);
        btnViewTracking.addActionListener(e -> viewTracking());
        
        buttonPanel.add(btnViewTracking);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.WEST);
        mainPanel.add(scrollTable, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomer().getName(),
                o.getCustomer().getPhone(),
                o.getService().getServiceName(),
                o.getService().getTier(),
                o.getWeightOrQty(),
                String.format("Rp %,.0f", o.getTotalPrice()),
                o.getOrderStatus(),
                o.getPaymentStatus(),
                o.getOrderDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            });
        }
    }
    
    private void filterOrders() {
        String status = (String) cmbStatusFilter.getSelectedItem();
        if ("Semua".equals(status)) {
            loadOrders();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getOrdersByStatus(Order.OrderStatus.valueOf(status));
        
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomer().getName(),
                o.getCustomer().getPhone(),
                o.getService().getServiceName(),
                o.getService().getTier(),
                o.getWeightOrQty(),
                String.format("Rp %,.0f", o.getTotalPrice()),
                o.getOrderStatus(),
                o.getPaymentStatus(),
                o.getOrderDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            });
        }
    }
    
    private void searchOrders() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadOrders();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.searchOrders(keyword);
        
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getOrderId(),
                o.getCustomer().getName(),
                o.getCustomer().getPhone(),
                o.getService().getServiceName(),
                o.getService().getTier(),
                o.getWeightOrQty(),
                String.format("Rp %,.0f", o.getTotalPrice()),
                o.getOrderStatus(),
                o.getPaymentStatus(),
                o.getOrderDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            });
        }
    }
    
    private void viewTracking() {
        int selectedRow = tblOrders.getSelectedRow();
        if (selectedRow >= 0) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);
            new OrderTrackingForm(orderId).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih order terlebih dahulu!");
        }
    }
}
