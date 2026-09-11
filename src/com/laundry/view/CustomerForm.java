/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.CustomerDAO;
import com.laundry.model.Customer;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author ASUS
 */
public class CustomerForm extends JFrame {
    private JTextField txtName, txtPhone, txtAddress, txtKosName, txtEmail;
    private JButton btnSave, btnCancel;
    
    private CustomerDAO customerDAO;
    private Customer customer;
    private MainFrame parentFrame;
    
    public CustomerForm(MainFrame parent) {
        this(parent, null);
    }
    
    public CustomerForm(MainFrame parent, Customer customer) {
        this.parentFrame = parent;
        this.customer = customer;
        this.customerDAO = new CustomerDAO();
        
        setTitle(customer == null ? "Tambah Customer Baru" : "Edit Customer");
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        if (customer != null) {
            fillForm();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        JLabel lblHeader = new JLabel(customer == null ? 
            "TAMBAH CUSTOMER BARU" : "EDIT CUSTOMER");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 16));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        addFormRow(formPanel, gbc, 0, "Nama:", txtName = new JTextField(25));
        addFormRow(formPanel, gbc, 1, "No. Telepon:", txtPhone = new JTextField(25));
        addFormRow(formPanel, gbc, 2, "Alamat:", txtAddress = new JTextField(25));
        addFormRow(formPanel, gbc, 3, "Nama Kos:", txtKosName = new JTextField(25));
        addFormRow(formPanel, gbc, 4, "Email:", txtEmail = new JTextField(25));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        btnSave = new JButton("Simpan");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 12));
        btnSave.setPreferredSize(new Dimension(120, 40));
        btnSave.addActionListener(e -> saveCustomer());
        
        btnCancel = new JButton("Batal");
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setPreferredSize(new Dimension(120, 40));
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, 
                           String label, JTextField field) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }
    
    private void fillForm() {
        txtName.setText(customer.getName());
        txtPhone.setText(customer.getPhone());
        txtAddress.setText(customer.getAddress());
        txtKosName.setText(customer.getKosName());
        txtEmail.setText(customer.getEmail());
    }
    
    private void saveCustomer() {
        if (txtName.getText().trim().isEmpty() || 
            txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama dan No. Telepon wajib diisi!");
            return;
        }
        
        try {
            if (customer == null) {
                // New customer
                customer = new Customer(
                    txtName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtAddress.getText().trim(),
                    txtKosName.getText().trim(),
                    txtEmail.getText().trim()
                );
                
                if (customerDAO.addCustomer(customer)) {
                    JOptionPane.showMessageDialog(this, "Customer berhasil ditambahkan!");
                    if (parentFrame != null) {
                        parentFrame.refreshStatistics();
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan customer!");
                }
            } else {
                customer.setName(txtName.getText().trim());
                customer.setPhone(txtPhone.getText().trim());
                customer.setAddress(txtAddress.getText().trim());
                customer.setKosName(txtKosName.getText().trim());
                customer.setEmail(txtEmail.getText().trim());
                
                if (customerDAO.updateCustomer(customer)) {
                    JOptionPane.showMessageDialog(this, "Customer berhasil diupdate!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal update customer!");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
