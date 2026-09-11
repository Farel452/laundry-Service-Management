/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.OrderDAO;
import com.laundry.model.Order;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class OrderTrackingForm extends JFrame {
    private JPanel mainPanel;
    private JPanel headerPanel;
    private JPanel infoPanel;
    private JPanel progressPanel;
    private JPanel timelinePanel;
    private JPanel buttonPanel;
    
    private JLabel lblOrderId;
    private JLabel lblCustomer;
    private JLabel lblPhone;
    private JLabel lblService;
    private JLabel lblStatus;
    private JLabel lblEstimated;
    private JLabel lblTotalPrice;
    
    private JProgressBar progressBar;
    private JPanel statusIndicatorPanel;
    private JTextArea txtTrackingDetails;
    
    private JButton btnRefresh;
    private JButton btnClose;
    private JButton btnPrint;
    
    private OrderDAO orderDAO;
    private Order order;
    
    public OrderTrackingForm(String orderId) {
        orderDAO = new OrderDAO();
        
        initComponents();
        loadOrderTracking(orderId);
        
        setTitle("Order Tracking - " + orderId);
        setSize(750, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        headerPanel.setPreferredSize(new Dimension(750, 80));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel lblHeader = new JLabel("🔍 ORDER TRACKING");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);
        
        JLabel lblSubheader = new JLabel("Lacak status pesanan laundry Anda secara real-time");
        lblSubheader.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubheader.setForeground(new Color(255, 255, 255, 200));
        
        JPanel titlePanel = new JPanel(new BorderLayout(5, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(lblHeader, BorderLayout.NORTH);
        titlePanel.add(lblSubheader, BorderLayout.CENTER);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblOrderIdLabel = new JLabel("Order ID:");
        lblOrderIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblOrderIdLabel, gbc);
        
        gbc.gridx = 1;
        lblOrderId = new JLabel("-");
        lblOrderId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblOrderId.setForeground(new Color(52, 152, 219));
        infoPanel.add(lblOrderId, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblCustomerLabel = new JLabel("Customer:");
        lblCustomerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblCustomerLabel, gbc);
        
        gbc.gridx = 1;
        lblCustomer = new JLabel("-");
        lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(lblCustomer, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblPhoneLabel = new JLabel("Telepon:");
        lblPhoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblPhoneLabel, gbc);
        
        gbc.gridx = 1;
        lblPhone = new JLabel("-");
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(lblPhone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblServiceLabel = new JLabel("Layanan:");
        lblServiceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblServiceLabel, gbc);
        
        gbc.gridx = 1;
        lblService = new JLabel("-");
        lblService.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoPanel.add(lblService, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblTotalPriceLabel = new JLabel("Total Harga:");
        lblTotalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblTotalPriceLabel, gbc);
        
        gbc.gridx = 1;
        lblTotalPrice = new JLabel("-");
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalPrice.setForeground(new Color(46, 204, 113));
        infoPanel.add(lblTotalPrice, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel lblStatusLabel = new JLabel("Status Saat Ini:");
        lblStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblStatusLabel, gbc);
        
        gbc.gridx = 1;
        lblStatus = new JLabel("-");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setOpaque(true);
        lblStatus.setBackground(new Color(236, 240, 241));
        lblStatus.setBorder(new EmptyBorder(5, 10, 5, 10));
        infoPanel.add(lblStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel lblEstimatedLabel = new JLabel("Estimasi Selesai:");
        lblEstimatedLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        infoPanel.add(lblEstimatedLabel, gbc);
        
        gbc.gridx = 1;
        lblEstimated = new JLabel("-");
        lblEstimated.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstimated.setForeground(new Color(231, 76, 60));
        infoPanel.add(lblEstimated, gbc);
        
        progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createTitledBorder(
                new EmptyBorder(15, 20, 15, 20),
                "📊 Progress Status",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)
            )
        ));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(0, 40));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        progressBar.setForeground(new Color(52, 152, 219));
        progressBar.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        statusIndicatorPanel = new JPanel(new GridLayout(1, 6, 10, 0));
        statusIndicatorPanel.setBackground(Color.WHITE);
        statusIndicatorPanel.setBorder(new EmptyBorder(15, 10, 10, 10));
        
        progressPanel.add(progressBar);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        progressPanel.add(statusIndicatorPanel);
        
        timelinePanel = new JPanel(new BorderLayout());
        timelinePanel.setBackground(Color.WHITE);
        timelinePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createTitledBorder(
                new EmptyBorder(10, 15, 15, 15),
                "📜 Timeline Tracking Detail",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(52, 73, 94)
            )
        ));
        
        txtTrackingDetails = new JTextArea();
        txtTrackingDetails.setEditable(false);
        txtTrackingDetails.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtTrackingDetails.setLineWrap(true);
        txtTrackingDetails.setWrapStyleWord(true);
        txtTrackingDetails.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollTracking = new JScrollPane(txtTrackingDetails);
        scrollTracking.setBorder(BorderFactory.createEmptyBorder());
        timelinePanel.add(scrollTracking, BorderLayout.CENTER);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        btnRefresh = createButton("🔄 Refresh", new Color(52, 152, 219), 140, 40);
        btnPrint = createButton("🖨️ Print", new Color(46, 204, 113), 140, 40);
        btnClose = createButton("❌ Tutup", new Color(149, 165, 166), 140, 40);
        
        btnRefresh.addActionListener(e -> {
            if (order != null) {
                loadOrderTracking(order.getOrderId());
            }
        });
        btnPrint.addActionListener(e -> printTrackingReport());
        btnClose.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnPrint);
        buttonPanel.add(btnClose);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(progressPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(timelinePanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JButton createButton(String text, Color color, int width, int height) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
    
    private void loadOrderTracking(String orderId) {
        order = orderDAO.getOrderById(orderId);
        
        if (order == null) {
            JOptionPane.showMessageDialog(this, 
                "Order dengan ID " + orderId + " tidak ditemukan!",
                "Order Not Found", 
                JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }
        
        lblOrderId.setText(order.getOrderId());
        lblCustomer.setText(order.getCustomer().getName());
        lblPhone.setText(order.getCustomer().getPhone());
        lblService.setText(String.format("%s (%s)", 
            order.getService().getServiceName(),
            order.getService().getTier()));
        lblTotalPrice.setText(String.format("Rp %,.0f", order.getTotalPrice()));
        
        lblStatus.setText(order.getOrderStatus().toString());
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setBackground(getStatusColor(order.getOrderStatus()));
        
        lblEstimated.setText(order.getEstimatedCompletion().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm")
        ));
        
        updateProgress(order.getOrderStatus());
        
        updateStatusIndicators(order.getOrderStatus());
        
        List<String> tracking = orderDAO.getOrderTracking(orderId);
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║          TRACKING HISTORY - ").append(orderId).append("          ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n\n");
        
        for (int i = tracking.size() - 1; i >= 0; i--) {
            sb.append("  ").append(tracking.size() - i).append(". ")
              .append(tracking.get(i)).append("\n\n");
        }
        
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");
        
        txtTrackingDetails.setText(sb.toString());
        txtTrackingDetails.setCaretPosition(0);
    }
    
    private void updateProgress(Order.OrderStatus status) {
        int progress = 0;
        String progressText = "";
        Color barColor = new Color(52, 152, 219);
        
        switch (status) {
            case RECEIVED:
                progress = 15;
                progressText = "Order Diterima";
                barColor = new Color(52, 152, 219);
                break;
            case WASHING:
                progress = 40;
                progressText = "Sedang Dicuci";
                barColor = new Color(155, 89, 182);
                break;
            case DRYING:
                progress = 65;
                progressText = "Sedang Dikeringkan";
                barColor = new Color(230, 126, 34);
                break;
            case READY:
                progress = 85;
                progressText = "Siap Diambil/Dikirim";
                barColor = new Color(46, 204, 113);
                break;
            case DELIVERING:
                progress = 95;
                progressText = "Sedang Dikirim";
                barColor = new Color(241, 196, 15);
                break;
            case COMPLETED:
                progress = 100;
                progressText = "Selesai";
                barColor = new Color(22, 160, 133);
                break;
        }
        
        progressBar.setValue(progress);
        progressBar.setString(progressText + " (" + progress + "%)");
        progressBar.setForeground(barColor);
    }
    
    private void updateStatusIndicators(Order.OrderStatus currentStatus) {
        statusIndicatorPanel.removeAll();
        
        String[] statuses = {"RECEIVED", "WASHING", "DRYING", "READY", "DELIVERING", "COMPLETED"};
        String[] icons = {"📥", "🧼", "💨", "✅", "🚚", "🎉"};
        
        int currentIndex = getStatusIndex(currentStatus);
        
        for (int i = 0; i < statuses.length; i++) {
            JPanel indicator = new JPanel(new BorderLayout());
            indicator.setOpaque(true);
            indicator.setBorder(new LineBorder(Color.WHITE, 2));
            
            JLabel lblIcon = new JLabel(icons[i], SwingConstants.CENTER);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            
            JLabel lblText = new JLabel(statuses[i], SwingConstants.CENTER);
            lblText.setFont(new Font("Segoe UI", Font.BOLD, 9));
            
            if (i <= currentIndex) {
                indicator.setBackground(getStatusColor(Order.OrderStatus.valueOf(statuses[i])));
                lblText.setForeground(Color.WHITE);
            } else {
                indicator.setBackground(new Color(236, 240, 241));
                lblText.setForeground(new Color(149, 165, 166));
            }
            
            indicator.add(lblIcon, BorderLayout.CENTER);
            indicator.add(lblText, BorderLayout.SOUTH);
            
            statusIndicatorPanel.add(indicator);
        }
        
        statusIndicatorPanel.revalidate();
        statusIndicatorPanel.repaint();
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
    
    private Color getStatusColor(Order.OrderStatus status) {
        switch (status) {
            case RECEIVED:
                return new Color(52, 152, 219);
            case WASHING:
                return new Color(155, 89, 182);
            case DRYING:
                return new Color(230, 126, 34);
            case READY:
                return new Color(46, 204, 113);
            case DELIVERING:
                return new Color(241, 196, 15);
            case COMPLETED:
                return new Color(22, 160, 133);
            default:
                return Color.GRAY;
        }
    }
    
    private void printTrackingReport() {
        if (order == null) return;
        
        String report = String.format("""
            ═══════════════════════════════════════════════════
                    LAUNDRY SERVICE - MALANG
                     TRACKING REPORT
            ═══════════════════════════════════════════════════
            
            Order ID        : %s
            Tanggal Order   : %s
            
            Customer        : %s
            Telepon         : %s
            Alamat          : %s
            
            Layanan         : %s
            Tier            : %s
            Berat/Qty       : %s
            Total Harga     : Rp %,.0f
            
            Status Saat Ini : %s
            Estimasi Selesai: %s
            
            ═══════════════════════════════════════════════════
                       TRACKING HISTORY
            ═══════════════════════════════════════════════════
            
            %s
            
            ═══════════════════════════════════════════════════
            Printed: %s
            ═══════════════════════════════════════════════════
            """,
            order.getOrderId(),
            order.getOrderDate().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ),
            order.getCustomer().getName(),
            order.getCustomer().getPhone(),
            order.getCustomer().getAddress(),
            order.getService().getServiceName(),
            order.getService().getTier(),
            order.getWeightOrQty(),
            order.getTotalPrice(),
            order.getOrderStatus(),
            lblEstimated.getText(),
            txtTrackingDetails.getText(),
            java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            )
        );
        
        JTextArea textArea = new JTextArea(report);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 500));
        
        JOptionPane.showMessageDialog(this, 
            scrollPane,
            "Tracking Report - " + order.getOrderId(), 
            JOptionPane.PLAIN_MESSAGE);
    }
}
