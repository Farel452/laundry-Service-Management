/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.*;
import com.laundry.model.Order;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import com.laundry.util.SessionManager;
import com.laundry.view.LoginForm;
import com.laundry.model.User;
import com.laundry.view.LoginForm;
/**
 *
 * @author ASUS
 */
public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel statsPanel;
    private JPanel actionsPanel;
    private JPanel sidebarPanel;
    
    private JLabel lblTitle;
    private JLabel lblTotalOrdersValue;
    private JLabel lblTotalRevenueValue;
    private JLabel lblPendingOrdersValue;
    private JLabel lblCompletedOrdersValue;
    
    private JButton btnNewOrder;
    private JButton btnViewOrders;
    private JButton btnTracking;
    private JButton btnCustomers;
    private JButton btnProduction;
    private JButton btnDelivery;
    private JButton btnReports;
    private JButton btnRefresh;
    private JButton btnExit;
    
    private OrderDAO orderDAO;
    private CustomerDAO customerDAO;
    
    public MainFrame() {
        orderDAO = new OrderDAO();
        customerDAO = new CustomerDAO();
        
        initComponents();
        loadStatistics();
        
        setTitle("Laundry Service Management System - Malang");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(1200, 80));
        headerPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        
        lblTitle = new JLabel("LAUNDRY SERVICE MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setIcon(createIcon("🧺", 32));
        
        JLabel lblSubtitle = new JLabel("Sistem Manajemen Laundry - Kota Malang");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(236, 240, 241));
        
        JPanel titlePanel = new JPanel(new BorderLayout(5, 2));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.CENTER);
        
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userInfoPanel.setOpaque(false);

        JLabel lblUserIcon = new JLabel("👤");
        lblUserIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        lblUserIcon.setForeground(Color.WHITE);

        JLabel lblUsername = new JLabel(SessionManager.getCurrentUserFullName());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsername.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel("(" + SessionManager.getCurrentUserRole() + ")");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setForeground(new Color(236, 240, 241));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setPreferredSize(new Dimension(80, 30));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> performLogout());

        userInfoPanel.add(lblUserIcon);
        userInfoPanel.add(lblUsername);
        userInfoPanel.add(lblRole);
        userInfoPanel.add(btnLogout);

        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        JPanel headerInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerInfoPanel.setOpaque(false);
        
        JLabel lblDate = new JLabel(java.time.LocalDate.now().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(Color.WHITE);
        lblDate.setIcon(createIcon("📅", 16));
        
        JLabel lblTime = new JLabel(java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTime.setForeground(Color.WHITE);
        lblTime.setIcon(createIcon("🕐", 16));
        
        headerInfoPanel.add(lblDate);
        headerInfoPanel.add(lblTime);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(headerInfoPanel, BorderLayout.EAST);
        
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(220, 670));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        JLabel lblMenu = new JLabel("MENU UTAMA");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblMenu.setForeground(new Color(189, 195, 199));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMenu.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        sidebarPanel.add(lblMenu);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnNewOrder = createSidebarButton("📝 Order Baru", new Color(52, 152, 219));
        btnViewOrders = createSidebarButton("📋 Lihat Pesanan", new Color(52, 152, 219));
        btnTracking = createSidebarButton("🔍 Tracking Order", new Color(155, 89, 182));
        btnCustomers = createSidebarButton("👥 Data Customer", new Color(46, 204, 113));
        btnProduction = createSidebarButton("⚙️ Produksi", new Color(241, 196, 15));
        btnDelivery = createSidebarButton("🚚 Pengiriman", new Color(230, 126, 34));
        btnReports = createSidebarButton("📊 Laporan", new Color(149, 165, 166));
        
        sidebarPanel.add(btnNewOrder);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnViewOrders);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnTracking);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnCustomers);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnProduction);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnDelivery);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnReports);
        sidebarPanel.add(Box.createVerticalGlue());
        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(200, 2));
        separator.setForeground(new Color(52, 73, 94));
        sidebarPanel.add(separator);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnRefresh = createSidebarButton("🔄 Refresh", new Color(52, 73, 94));
        btnExit = createSidebarButton("❌ Keluar", new Color(192, 57, 43));
        
        sidebarPanel.add(btnRefresh);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebarPanel.add(btnExit);
        
        btnNewOrder.addActionListener(e -> openNewOrderForm());
        btnViewOrders.addActionListener(e -> openOrderListForm());
        btnTracking.addActionListener(e -> openTrackingForm());
        btnCustomers.addActionListener(e -> openCustomerListForm());
        btnProduction.addActionListener(e -> openProductionTrackingForm());
        btnDelivery.addActionListener(e -> openDeliveryScheduleForm());
        btnReports.addActionListener(e -> openReportsForm());
        btnRefresh.addActionListener(e -> {
            loadStatistics();
            JOptionPane.showMessageDialog(this, "Data berhasil direfresh!", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin keluar dari aplikasi?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(new Color(236, 240, 241));
        statsPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel cardTotalOrders = createStatCard("Total Orders", "0", 
            "📦", new Color(52, 152, 219), new Color(41, 128, 185));
        JPanel cardRevenue = createStatCard("Total Revenue", "Rp 0", 
            "💰", new Color(46, 204, 113), new Color(39, 174, 96));
        JPanel cardPending = createStatCard("Pending Orders", "0", 
            "⏳", new Color(241, 196, 15), new Color(243, 156, 18));
        JPanel cardCompleted = createStatCard("Completed", "0", 
            "✅", new Color(155, 89, 182), new Color(142, 68, 173));
        
        lblTotalOrdersValue = (JLabel) ((JPanel) cardTotalOrders.getComponent(1)).getComponent(0);
        lblTotalRevenueValue = (JLabel) ((JPanel) cardRevenue.getComponent(1)).getComponent(0);
        lblPendingOrdersValue = (JLabel) ((JPanel) cardPending.getComponent(1)).getComponent(0);
        lblCompletedOrdersValue = (JLabel) ((JPanel) cardCompleted.getComponent(1)).getComponent(0);
        
        statsPanel.add(cardTotalOrders);
        statsPanel.add(cardRevenue);
        statsPanel.add(cardPending);
        statsPanel.add(cardCompleted);
        
        actionsPanel = new JPanel();
        actionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(15, 20, 15, 20),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                "Quick Actions",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(44, 62, 80)
            )
        ));
        
        JButton btnQuickNewOrder = createActionButton("Order Baru", "📝", new Color(52, 152, 219));
        JButton btnQuickTracking = createActionButton("Tracking", "🔍", new Color(155, 89, 182));
        JButton btnQuickProduction = createActionButton("Produksi", "⚙️", new Color(241, 196, 15));
        JButton btnQuickDelivery = createActionButton("Delivery", "🚚", new Color(230, 126, 34));
        
        btnQuickNewOrder.addActionListener(e -> openNewOrderForm());
        btnQuickTracking.addActionListener(e -> openTrackingForm());
        btnQuickProduction.addActionListener(e -> openProductionTrackingForm());
        btnQuickDelivery.addActionListener(e -> openDeliveryScheduleForm());
        
        actionsPanel.add(btnQuickNewOrder);
        actionsPanel.add(btnQuickTracking);
        actionsPanel.add(btnQuickProduction);
        actionsPanel.add(btnQuickDelivery);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(236, 240, 241));
        centerPanel.add(statsPanel, BorderLayout.CENTER);
        centerPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(44, 62, 80));
        footerPanel.setPreferredSize(new Dimension(1200, 30));
        
        JLabel lblFooter = new JLabel("© 2025 Laundry Service Management - Sistem Manajemen Laundry Malang | Version 1.0");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(189, 195, 199));
        footerPanel.add(lblFooter);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createSidebarButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
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
    
    private JPanel createStatCard(String title, String value, String emoji, 
                                   Color bgColor, Color borderColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(borderColor, 3, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setOpaque(false);
        
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        
        topPanel.add(lblEmoji);
        topPanel.add(lblTitle);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        valuePanel.setOpaque(false);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValue.setForeground(Color.WHITE);
        
        valuePanel.add(lblValue);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String text, String emoji, Color color) {
        JButton btn = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setPreferredSize(new Dimension(140, 80));
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
    
    private ImageIcon createIcon(String emoji, int size) {
        return null; // Placeholder for emoji rendering
    }
    
    private void loadStatistics() {
        int totalOrders = orderDAO.getTotalOrders();
        BigDecimal totalRevenue = orderDAO.getTotalRevenue();
        int pendingOrders = orderDAO.getOrderCountByStatus(Order.OrderStatus.RECEIVED) +
                           orderDAO.getOrderCountByStatus(Order.OrderStatus.WASHING) +
                           orderDAO.getOrderCountByStatus(Order.OrderStatus.DRYING);
        int completedOrders = orderDAO.getOrderCountByStatus(Order.OrderStatus.COMPLETED);
        
        
        
        lblTotalOrdersValue.setText(String.valueOf(totalOrders));
        lblTotalRevenueValue.setText(String.format("Rp %,d", totalRevenue.longValue()));
        lblPendingOrdersValue.setText(String.valueOf(pendingOrders));
        lblCompletedOrdersValue.setText(String.valueOf(completedOrders));
    }
    
    public void refreshStatistics() {
        loadStatistics();
    }
    
    private void openNewOrderForm() {
        new OrderEntryForm(this).setVisible(true);
    }
    
    private void openOrderListForm() {
        new OrderListForm().setVisible(true);
    }
    
    private void openTrackingForm() {
        String orderId = JOptionPane.showInputDialog(this, 
            "Masukkan Order ID untuk tracking:", 
            "Order Tracking", 
            JOptionPane.QUESTION_MESSAGE);
        if (orderId != null && !orderId.isEmpty()) {
            new OrderTrackingForm(orderId).setVisible(true);
        }
    }
    
    private void openCustomerListForm() {
        new CustomerListForm().setVisible(true);
    }
    
    private void openProductionTrackingForm() {
        new ProductionTrackingForm().setVisible(true);
    }
    
    private void openDeliveryScheduleForm() {
        new DeliveryScheduleForm().setVisible(true);
    }
    
    private void openReportsForm() {
        JOptionPane.showMessageDialog(this, 
            "Fitur Laporan sedang dalam pengembangan", 
            "Coming Soon", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
    private void performLogout() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Yakin ingin logout?",
        "Konfirmasi Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // Clear session
        SessionManager.clearSession();
        
        // Close main frame
        dispose();
        
        // Open login form
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}

}
