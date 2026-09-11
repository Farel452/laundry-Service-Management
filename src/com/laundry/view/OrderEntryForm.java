/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.*;
import com.laundry.model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class OrderEntryForm extends JFrame {
     private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel customerPanel;
    private JPanel servicePanel;
    private JPanel buttonPanel;
    
    private JTextField txtCustomerName;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextField txtKosName;
    private JTextField txtEmail;
    private JTextField txtWeight;
    private JTextField txtTotalPrice;
    private JTextField txtSearchPhone;
    
    private JComboBox<ServiceType> cmbService;
    private JComboBox<String> cmbPaymentMethod;
    private JTextArea txtNotes;
    
    private JLabel lblOrderId;
    private JLabel lblEstimatedCompletion;
    private JLabel lblPricePerUnit;
    
    private JButton btnSearchCustomer;
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnClearForm;
    
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private ServiceTypeDAO serviceDAO;
    private MainFrame parentFrame;
    private Customer selectedCustomer;
    
    public OrderEntryForm(MainFrame parent) {
        this.parentFrame = parent;
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();
        serviceDAO = new ServiceTypeDAO();
        
        initComponents();
        
        setTitle("Order Baru - Laundry Service Management");
        setSize(900, 750);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        JLabel lblHeader = new JLabel("📝 FORM ORDER BARU");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        
        String generatedOrderId = orderDAO.generateOrderId();
        lblOrderId = new JLabel("Order ID: " + generatedOrderId);
        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOrderId.setForeground(new Color(255, 255, 255));
        lblOrderId.setBackground(new Color(41, 128, 185));
        lblOrderId.setOpaque(true);
        lblOrderId.setBorder(new EmptyBorder(5, 15, 5, 15));
        
        headerPanel.add(lblHeader, BorderLayout.WEST);
        headerPanel.add(lblOrderId, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(236, 240, 241));
        centerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        customerPanel = new JPanel();
        customerPanel.setLayout(new GridBagLayout());
        customerPanel.setBackground(Color.WHITE);
        customerPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15),
                "👥 DATA CUSTOMER",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)
            )
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JLabel lblSearch = new JLabel("Cari Customer:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        customerPanel.add(lblSearch, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.setOpaque(false);
        
        txtSearchPhone = new JTextField(20);
        txtSearchPhone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearchPhone.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        btnSearchCustomer = new JButton("🔍 Cari");
        btnSearchCustomer.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSearchCustomer.setBackground(new Color(52, 152, 219));
        btnSearchCustomer.setForeground(Color.WHITE);
        btnSearchCustomer.setFocusPainted(false);
        btnSearchCustomer.setBorderPainted(false);
        btnSearchCustomer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearchCustomer.addActionListener(e -> searchCustomer(txtSearchPhone.getText()));
        
        searchPanel.add(txtSearchPhone);
        searchPanel.add(btnSearchCustomer);
        customerPanel.add(searchPanel, gbc);

        gbc.gridwidth = 1;
        addFormField(customerPanel, gbc, 1, "Nama Customer:", 
            txtCustomerName = createTextField());
        addFormField(customerPanel, gbc, 2, "No. Telepon:", 
            txtPhone = createTextField());
        addFormField(customerPanel, gbc, 3, "Alamat/Kos:", 
            txtAddress = createTextField());
        addFormField(customerPanel, gbc, 4, "Nama Kos:", 
            txtKosName = createTextField());
        addFormField(customerPanel, gbc, 5, "Email:", 
            txtEmail = createTextField());
        
        servicePanel = new JPanel();
        servicePanel.setLayout(new GridBagLayout());
        servicePanel.setBackground(Color.WHITE);
        servicePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15),
                "🧺 LAYANAN & HARGA",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)
            )
        ));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        List<ServiceType> services = serviceDAO.getAllServices();
        cmbService = new JComboBox<>(services.toArray(new ServiceType[0]));
        cmbService.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbService.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ServiceType) {
                    ServiceType service = (ServiceType) value;
                    setText(String.format("%s - %s (Rp %,.0f)", 
                        service.getServiceName(), 
                        service.getTier(),
                        service.getPricePerUnit()));
                }
                return this;
            }
        });
        cmbService.addActionListener(e -> calculatePrice());
        addFormField(servicePanel, gbc, 0, "Jenis Layanan:", cmbService);
        
        lblPricePerUnit = new JLabel("Harga: -");
        lblPricePerUnit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPricePerUnit.setForeground(new Color(46, 204, 113));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        servicePanel.add(lblPricePerUnit, gbc);
        
        txtWeight = createTextField();
        txtWeight.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                calculatePrice();
            }
        });
        gbc.gridwidth = 1;
        addFormField(servicePanel, gbc, 2, "Berat/Jumlah (kg/pcs):", txtWeight);
        
        txtTotalPrice = createTextField();
        txtTotalPrice.setEditable(false);
        txtTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        txtTotalPrice.setForeground(new Color(46, 204, 113));
        txtTotalPrice.setBackground(new Color(241, 248, 233));
        addFormField(servicePanel, gbc, 3, "💰 TOTAL HARGA:", txtTotalPrice);
        
        String[] paymentMethods = {"CASH", "DEBIT", "EWALLET"};
        cmbPaymentMethod = new JComboBox<>(paymentMethods);
        cmbPaymentMethod.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addFormField(servicePanel, gbc, 4, "Metode Pembayaran:", cmbPaymentMethod);
        
        lblEstimatedCompletion = new JLabel("-");
        lblEstimatedCompletion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEstimatedCompletion.setForeground(new Color(231, 76, 60));
        addFormField(servicePanel, gbc, 5, "⏰ Estimasi Selesai:", lblEstimatedCompletion);
        
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblNotes = new JLabel("Catatan:");
        lblNotes.setFont(new Font("Segoe UI", Font.BOLD, 12));
        servicePanel.add(lblNotes, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.gridheight = 2;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollNotes = new JScrollPane(txtNotes);
        servicePanel.add(scrollNotes, gbc);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        btnSave = createButton("💾 Simpan Order", new Color(46, 204, 113), 160, 45);
        btnClearForm = createButton("🔄 Reset Form", new Color(52, 152, 219), 160, 45);
        btnCancel = createButton("❌ Batal", new Color(231, 76, 60), 160, 45);
        
        btnSave.addActionListener(e -> saveOrder());
        btnClearForm.addActionListener(e -> clearForm());
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClearForm);
        buttonPanel.add(btnCancel);
        
        centerPanel.add(customerPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(servicePanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField(25);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private JButton createButton(String text, Color color, int width, int height) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, 
                             String label, JComponent component) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(component, gbc);
    }
    
    private void searchCustomer(String phone) {
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Masukkan nomor telepon!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        selectedCustomer = customerDAO.getCustomerByPhone(phone);
        
        if (selectedCustomer != null) {
            txtCustomerName.setText(selectedCustomer.getName());
            txtPhone.setText(selectedCustomer.getPhone());
            txtAddress.setText(selectedCustomer.getAddress());
            txtKosName.setText(selectedCustomer.getKosName());
            txtEmail.setText(selectedCustomer.getEmail());
            
            txtCustomerName.setEditable(false);
            txtPhone.setEditable(false);
            txtCustomerName.setBackground(new Color(241, 248, 233));
            txtPhone.setBackground(new Color(241, 248, 233));
            
            JOptionPane.showMessageDialog(this, 
                "Customer ditemukan! ✓", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            int response = JOptionPane.showConfirmDialog(this,
                "Customer tidak ditemukan.\nBuat customer baru dengan nomor ini?",
                "Customer Baru", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (response == JOptionPane.YES_OPTION) {
                clearCustomerFields();
                txtPhone.setText(phone);
                txtCustomerName.requestFocus();
            }
        }
    }
    
    private void calculatePrice() {
        ServiceType selectedService = (ServiceType) cmbService.getSelectedItem();
        if (selectedService == null) return;
        
        lblPricePerUnit.setText(String.format("Harga: Rp %,.0f per %s", 
            selectedService.getPricePerUnit(),
            selectedService.getServiceCategory() == ServiceType.ServiceCategory.KILOAN ? "kg" : "pcs"));
        
        String weightStr = txtWeight.getText().trim();
        if (weightStr.isEmpty()) {
            txtTotalPrice.setText("Rp 0");
            return;
        }
        
        try {
            BigDecimal weight = new BigDecimal(weightStr);
            BigDecimal price = selectedService.getPricePerUnit();
            BigDecimal total = weight.multiply(price);
            
            txtTotalPrice.setText(String.format("Rp %,.0f", total));
            
            LocalDateTime estimated = LocalDateTime.now()
                .plusHours(selectedService.getEstimatedHours());
            lblEstimatedCompletion.setText(estimated.format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ));
        } catch (NumberFormatException e) {
            txtTotalPrice.setText("Rp 0");
            JOptionPane.showMessageDialog(this, 
                "Format angka tidak valid!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void saveOrder() {
        if (txtCustomerName.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty() ||
            txtAddress.getText().trim().isEmpty() ||
            txtWeight.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Mohon lengkapi semua data yang diperlukan!", 
                "Data Tidak Lengkap", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (selectedCustomer == null) {
                selectedCustomer = new Customer(
                    txtCustomerName.getText().trim(),
                    txtPhone.getText().trim(),
                    txtAddress.getText().trim(),
                    txtKosName.getText().trim(),
                    txtEmail.getText().trim()
                );
                
                if (!customerDAO.addCustomer(selectedCustomer)) {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menyimpan data customer!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            ServiceType selectedService = (ServiceType) cmbService.getSelectedItem();
            BigDecimal weight = new BigDecimal(txtWeight.getText().trim());
            String totalPriceStr = txtTotalPrice.getText()
                .replace("Rp ", "").replace(",", "").replace(".", "");
            BigDecimal totalPrice = new BigDecimal(totalPriceStr);
            
            LocalDateTime estimated = LocalDateTime.now()
                .plusHours(selectedService.getEstimatedHours());
            
            Order order = new Order(
                lblOrderId.getText().replace("Order ID: ", ""),
                selectedCustomer.getCustomerId(),
                selectedService.getServiceId(),
                weight,
                totalPrice,
                Order.PaymentMethod.valueOf(cmbPaymentMethod.getSelectedItem().toString())
            );
            
            order.setEstimatedCompletion(estimated);
            order.setPicStaff("ADMIN");
            order.setNotes(txtNotes.getText().trim());
            
            if (orderDAO.addOrder(order)) {
                JOptionPane.showMessageDialog(this,
                    String.format(
                        "✓ Order berhasil disimpan!\n\n" +
                        "Order ID: %s\n" +
                        "Customer: %s\n" +
                        "Total: %s\n" +
                        "Estimasi: %s",
                        order.getOrderId(),
                        selectedCustomer.getName(),
                        txtTotalPrice.getText(),
                        lblEstimatedCompletion.getText()
                    ),
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (parentFrame != null) {
                    parentFrame.refreshStatistics();
                }
                
                int printReceipt = JOptionPane.showConfirmDialog(this,
                    "Cetak struk order?", 
                    "Cetak Struk", 
                    JOptionPane.YES_NO_OPTION);
                
                if (printReceipt == JOptionPane.YES_OPTION) {
                    printReceipt(order);
                }
                
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan order!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        clearCustomerFields();
        txtWeight.setText("");
        txtTotalPrice.setText("Rp 0");
        txtNotes.setText("");
        lblEstimatedCompletion.setText("-");
        lblPricePerUnit.setText("Harga: -");
        cmbService.setSelectedIndex(0);
        cmbPaymentMethod.setSelectedIndex(0);
        selectedCustomer = null;
    }
    
    private void clearCustomerFields() {
        txtCustomerName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtKosName.setText("");
        txtEmail.setText("");
        txtSearchPhone.setText("");
        txtCustomerName.setEditable(true);
        txtPhone.setEditable(true);
        txtCustomerName.setBackground(Color.WHITE);
        txtPhone.setBackground(Color.WHITE);
    }
    
    private void printReceipt(Order order) {
        ServiceType service = (ServiceType) cmbService.getSelectedItem();
        
        String receipt = String.format("""
            ════════════════════════════════════════
                 LAUNDRY SERVICE - MALANG
            ════════════════════════════════════════
            Order ID    : %s
            Tanggal     : %s
            
            Customer    : %s
            Telepon     : %s
            Alamat      : %s
            
            ────────────────────────────────────────
            Layanan     : %s
            Tier        : %s
            Berat/Qty   : %s
            Harga Satuan: Rp %,.0f
            ────────────────────────────────────────
            
            TOTAL       : %s
            Pembayaran  : %s
            Status      : BELUM BAYAR
            
            Estimasi Selesai: %s
            
            ════════════════════════════════════════
            Terima kasih atas kepercayaan Anda!
            Simpan struk ini sebagai bukti.
            ════════════════════════════════════════
            """,
            order.getOrderId(),
            LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ),
            txtCustomerName.getText(),
            txtPhone.getText(),
            txtAddress.getText(),
            service.getServiceName(),
            service.getTier(),
            txtWeight.getText(),
            service.getPricePerUnit(),
            txtTotalPrice.getText(),
            cmbPaymentMethod.getSelectedItem(),
            lblEstimatedCompletion.getText()
        );
        
        JTextArea textArea = new JTextArea(receipt);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        
        JOptionPane.showMessageDialog(this, 
            new JScrollPane(textArea),
            "Struk Order - " + order.getOrderId(), 
            JOptionPane.PLAIN_MESSAGE);
    }
}
