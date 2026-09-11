/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.OrderDAO;
import com.laundry.model.Order;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class ProductionTrackingForm extends JFrame{
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel filterPanel;
    private JPanel tablePanel;
    private JPanel detailPanel;
    private JPanel actionPanel;
    
    private JTable tblOrders;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbFilterStatus;
    private JTextField txtSearch;
    private JTextArea txtTrackingHistory;
    private JLabel lblSelectedOrder;
    private JLabel lblCurrentStatus;
    private JLabel lblCustomerInfo;
    
    private JButton btnReceived, btnWashing, btnDrying, btnReady, btnDelivering, btnCompleted;
    private JButton btnSearch, btnRefresh;
    
    private OrderDAO orderDAO;
    private Order selectedOrder;
    
    public ProductionTrackingForm() {
        orderDAO = new OrderDAO();
        
        initComponents();
        loadOrders();
        
        setTitle("Production Tracking - Update Status Produksi");
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(241, 196, 15));
        headerPanel.setPreferredSize(new Dimension(1300, 70));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        JLabel lblHeader = new JLabel("⚙️ PRODUCTION TRACKING - UPDATE STATUS");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Monitor dan update status produksi laundry");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel(new BorderLayout(5, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(lblHeader, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.CENTER);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JLabel lblFilterStatus = new JLabel("Filter Status:");
        lblFilterStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        String[] statuses = {"-- Semua Status --", "RECEIVED", "WASHING", 
                            "DRYING", "READY", "DELIVERING", "COMPLETED"};
        cmbFilterStatus = new JComboBox<>(statuses);
        cmbFilterStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbFilterStatus.setPreferredSize(new Dimension(150, 30));
        cmbFilterStatus.addActionListener(e -> filterOrders());
        
        JSeparator sep1 = new JSeparator(SwingConstants.VERTICAL);
        sep1.setPreferredSize(new Dimension(2, 25));
        
        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(4, 8, 4, 8)
        ));
        
        btnSearch = createButton("🔍 Cari", new Color(52, 152, 219), 100, 28);
        btnRefresh = createButton("🔄 Refresh", new Color(149, 165, 166), 100, 28);
        
        btnSearch.addActionListener(e -> searchOrders());
        btnRefresh.addActionListener(e -> loadOrders());
        
        filterPanel.add(lblFilterStatus);
        filterPanel.add(cmbFilterStatus);
        filterPanel.add(sep1);
        filterPanel.add(lblSearch);
        filterPanel.add(txtSearch);
        filterPanel.add(btnSearch);
        filterPanel.add(btnRefresh);
        
        String[] columns = {"Order ID", "Customer", "Telepon", "Layanan", 
                          "Berat/Qty", "Status", "PIC", "Tgl Order"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblOrders = new JTable(tableModel);
        tblOrders.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblOrders.setRowHeight(30);
        tblOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOrders.setSelectionBackground(new Color(52, 152, 219));
        tblOrders.setSelectionForeground(Color.WHITE);
        tblOrders.setGridColor(new Color(189, 195, 199));
        
        JTableHeader header = tblOrders.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        tblOrders.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected && value != null) {
                    String status = value.toString();
                    JLabel label = (JLabel) c;
                    label.setOpaque(true);
                    label.setHorizontalAlignment(CENTER);
                    label.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    
                    switch (status) {
                        case "RECEIVED":
                            label.setBackground(new Color(52, 152, 219));
                            label.setForeground(Color.WHITE);
                            break;
                        case "WASHING":
                            label.setBackground(new Color(155, 89, 182));
                            label.setForeground(Color.WHITE);
                            break;
                        case "DRYING":
                            label.setBackground(new Color(230, 126, 34));
                            label.setForeground(Color.WHITE);
                            break;
                        case "READY":
                            label.setBackground(new Color(46, 204, 113));
                            label.setForeground(Color.WHITE);
                            break;
                        default:
                            label.setBackground(Color.WHITE);
                            label.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        });
        
        tblOrders.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showOrderDetails();
            }
        });
        
        JScrollPane scrollTable = new JScrollPane(tblOrders);
        scrollTable.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        
        detailPanel = new JPanel(new BorderLayout(0, 10));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setPreferredSize(new Dimension(350, 0));
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel orderInfoPanel = new JPanel();
        orderInfoPanel.setLayout(new BoxLayout(orderInfoPanel, BoxLayout.Y_AXIS));
        orderInfoPanel.setBackground(Color.WHITE);
        orderInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            "Detail Order",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        lblSelectedOrder = new JLabel("Pilih order dari tabel");
        lblSelectedOrder.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSelectedOrder.setForeground(new Color(52, 152, 219));
        
        lblCurrentStatus = new JLabel("Status: -");
        lblCurrentStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblCustomerInfo = new JLabel("Customer: -");
        lblCustomerInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        orderInfoPanel.add(lblSelectedOrder);
        orderInfoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        orderInfoPanel.add(lblCurrentStatus);
        orderInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        orderInfoPanel.add(lblCustomerInfo);
        
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(Color.WHITE);
        historyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            "📜 Tracking History",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        txtTrackingHistory = new JTextArea();
        txtTrackingHistory.setEditable(false);
        txtTrackingHistory.setFont(new Font("Consolas", Font.PLAIN, 10));
        txtTrackingHistory.setLineWrap(true);
        txtTrackingHistory.setWrapStyleWord(true);
        JScrollPane scrollHistory = new JScrollPane(txtTrackingHistory);
        historyPanel.add(scrollHistory);
        
        detailPanel.add(orderInfoPanel, BorderLayout.NORTH);
        detailPanel.add(historyPanel, BorderLayout.CENTER);
        
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            "⚡ Update Status Produksi",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13),
            new Color(52, 73, 94)
        ));
        
        btnReceived = createStatusButton("📥 RECEIVED", new Color(52, 152, 219));
        btnWashing = createStatusButton("🧼 WASHING", new Color(155, 89, 182));
        btnDrying = createStatusButton("💨 DRYING", new Color(230, 126, 34));
        btnReady = createStatusButton("✅ READY", new Color(46, 204, 113));
        btnDelivering = createStatusButton("🚚 DELIVERING", new Color(241, 196, 15));
        btnCompleted = createStatusButton("🎉 COMPLETED", new Color(22, 160, 133));
        
        actionPanel.add(btnReceived);
        actionPanel.add(btnWashing);
        actionPanel.add(btnDrying);
        actionPanel.add(btnReady);
        actionPanel.add(btnDelivering);
        actionPanel.add(btnCompleted);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                              scrollTable, detailPanel);
        splitPane.setDividerLocation(900);
        splitPane.setOneTouchExpandable(true);
        
        contentPanel.add(splitPane, BorderLayout.CENTER);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton createButton(String text, Color color, int width, int height) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
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
    
    private JButton createStatusButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        String status = text.split(" ")[1]; // Extract status from button text
        btn.addActionListener(e -> updateStatus(Order.OrderStatus.valueOf(status)));
        
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
    
    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        
        for (Order order : orders) {
            if (order.getOrderStatus() == Order.OrderStatus.COMPLETED) {
                continue;
            }
            
            tableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getCustomer().getName(),
                order.getCustomer().getPhone(),
                order.getService().getServiceName(),
                order.getWeightOrQty(),
                order.getOrderStatus(),
                order.getPicStaff(),
                order.getOrderDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            });
        }
    }
    
    private void filterOrders() {
        String selectedStatus = (String) cmbFilterStatus.getSelectedItem();
        
        if ("-- Semua Status --".equals(selectedStatus)) {
            loadOrders();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getOrdersByStatus(Order.OrderStatus.valueOf(selectedStatus));
        
        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getCustomer().getName(),
                order.getCustomer().getPhone(),
                order.getService().getServiceName(),
                order.getWeightOrQty(),
                order.getOrderStatus(),
                order.getPicStaff(),
                order.getOrderDate().format(
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
        
        for (Order order : orders) {
            if (order.getOrderStatus() == Order.OrderStatus.COMPLETED) {
                continue;
            }
            
            tableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getCustomer().getName(),
                order.getCustomer().getPhone(),
                order.getService().getServiceName(),
                order.getWeightOrQty(),
                order.getOrderStatus(),
                order.getPicStaff(),
                order.getOrderDate().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                )
            });
        }
    }
    
    private void showOrderDetails() {
        int selectedRow = tblOrders.getSelectedRow();
        if (selectedRow >= 0) {
            String orderId = (String) tableModel.getValueAt(selectedRow, 0);
            selectedOrder = orderDAO.getOrderById(orderId);
            
            if (selectedOrder != null) {
                lblSelectedOrder.setText(selectedOrder.getOrderId());
                lblCurrentStatus.setText("Status: " + selectedOrder.getOrderStatus());
                lblCustomerInfo.setText(String.format("Customer: %s (%s)",
                    selectedOrder.getCustomer().getName(),
                    selectedOrder.getCustomer().getPhone()));
                
                List<String> tracking = orderDAO.getOrderTracking(orderId);
                StringBuilder sb = new StringBuilder();
                
                for (int i = 0; i < tracking.size(); i++) {
                    sb.append(String.format("[%d] %s\n\n", i + 1, tracking.get(i)));
                }
                
                txtTrackingHistory.setText(sb.toString());
                txtTrackingHistory.setCaretPosition(0);
            }
        }
    }
    
    private void updateStatus(Order.OrderStatus newStatus) {
        if (selectedOrder == null) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih order terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Order.OrderStatus currentStatus = selectedOrder.getOrderStatus();
        if (!isValidStatusProgression(currentStatus, newStatus)) {
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Status progression tidak valid!\n\n" +
                    "Status saat ini: %s\n" +
                    "Status baru: %s\n\n" +
                    "Mohon pilih status yang sesuai urutan.",
                    currentStatus, newStatus
                ),
                "Invalid Status", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String pic = JOptionPane.showInputDialog(this, 
            "Masukkan Nama PIC/Staff:", 
            "Input PIC",
            JOptionPane.QUESTION_MESSAGE);
        if (pic == null || pic.trim().isEmpty()) {
            pic = "STAFF";
        }
        
        String notes = JOptionPane.showInputDialog(this, 
            "Catatan (opsional):", 
            "Catatan Status",
            JOptionPane.QUESTION_MESSAGE);
        if (notes == null) notes = "";
        
        if (orderDAO.updateOrderStatus(selectedOrder.getOrderId(), newStatus, pic, notes)) {
            JOptionPane.showMessageDialog(this, 
                String.format("✓ Status berhasil diupdate!\n\n" +
                             "Order ID: %s\n" +
                             "Status Baru: %s\n" +
                             "PIC: %s",
                             selectedOrder.getOrderId(),
                             newStatus,
                             pic),
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            loadOrders();
            showOrderDetails();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Gagal update status!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isValidStatusProgression(Order.OrderStatus current, Order.OrderStatus next) {
        int currentIndex = getStatusIndex(current);
        int nextIndex = getStatusIndex(next);
        return nextIndex >= currentIndex;
    }
    
    private int getStatusIndex(Order.OrderStatus status) {
        switch (status) {
            case RECEIVED: return 0;
            case WASHING: return 1;
            case DRYING: return 2;
            case READY: return 3;
            case DELIVERING: return 4;
            case COMPLETED: return 5;
            default: return -1;
        }
    }
}
