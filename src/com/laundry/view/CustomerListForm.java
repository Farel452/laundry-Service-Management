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
public class CustomerListForm extends JFrame {
    private JTable tblCustomers;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private CustomerDAO customerDAO;
    
    public CustomerListForm() {
        customerDAO = new CustomerDAO();
        
        setTitle("Daftar Customer");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        loadCustomers();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        JLabel lblHeader = new JLabel("DAFTAR CUSTOMER");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 18));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.add(new JLabel("Cari:"));
        txtSearch = new JTextField(25);
        searchPanel.add(txtSearch);
        
        JButton btnSearch = new JButton("Cari");
        btnSearch.addActionListener(e -> searchCustomers());
        searchPanel.add(btnSearch);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadCustomers());
        searchPanel.add(btnRefresh);
        
        JButton btnAdd = new JButton("Tambah Customer");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> new CustomerForm(null).setVisible(true));
        searchPanel.add(btnAdd);
        
        String[] columns = {"ID", "Nama", "Telepon", "Alamat", "Kos", "Email", "Tgl Daftar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCustomers = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tblCustomers);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(searchPanel, BorderLayout.WEST);
        mainPanel.add(scrollTable, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.getAllCustomers();
        
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getName(),
                c.getPhone(),
                c.getAddress(),
                c.getKosName(),
                c.getEmail(),
                c.getCreatedAt().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
            });
        }
    }
    
    private void searchCustomers() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadCustomers();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.searchCustomers(keyword);
        
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getName(),
                c.getPhone(),
                c.getAddress(),
                c.getKosName(),
                c.getEmail(),
                c.getCreatedAt().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
            });
        }
    }
}
