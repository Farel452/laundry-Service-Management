/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laundry.view;
import com.laundry.dao.UserDAO;
import com.laundry.model.User;
import com.laundry.util.SessionManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author ASUS
 */
public class LoginForm extends JFrame{
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JCheckBox chkRememberMe;
    
    private JButton btnLogin;
    private JButton btnExit;
    
    private JLabel lblLoginAttempts;
    
    private UserDAO userDAO;
    private int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 5;
    
    public LoginForm() {
        userDAO = new UserDAO();
        
        initComponents();
        
        setTitle("Login - Laundry Service Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);
        
        // ========== LEFT PANEL (Branding) ==========
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(52, 152, 219));
        leftPanel.setBorder(new EmptyBorder(50, 40, 50, 40));
        
        // Logo/Icon
        JLabel lblLogo = new JLabel("🧺");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // App Title
        JLabel lblAppTitle = new JLabel("LAUNDRY SERVICE");
        lblAppTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblAppTitle.setForeground(Color.WHITE);
        lblAppTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblAppSubtitle = new JLabel("MANAGEMENT SYSTEM");
        lblAppSubtitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblAppSubtitle.setForeground(new Color(255, 255, 255, 200));
        lblAppSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Description
        JLabel lblDescription = new JLabel("<html><center>Sistem Manajemen Laundry<br>Kota Malang<br><br>Solusi Modern untuk Bisnis Laundry Anda</center></html>");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescription.setForeground(new Color(255, 255, 255, 180));
        lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(lblLogo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(lblAppTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPanel.add(lblAppSubtitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(lblDescription);
        leftPanel.add(Box.createVerticalGlue());
        
        // ========== RIGHT PANEL (Login Form) ==========
        rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        
        // Login Header
        JLabel lblLoginHeader = new JLabel("Sign In");
        lblLoginHeader.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblLoginHeader.setForeground(new Color(44, 62, 80));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(lblLoginHeader, gbc);
        
        JLabel lblLoginSubheader = new JLabel("Masuk ke akun Anda");
        lblLoginSubheader.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLoginSubheader.setForeground(new Color(127, 140, 141));
        gbc.gridy = 1;
        rightPanel.add(lblLoginSubheader, gbc);
        
        gbc.gridy = 2;
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);
        
        // Username Field
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(new Color(44, 62, 80));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        rightPanel.add(lblUsername, gbc);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 4;
        rightPanel.add(txtUsername, gbc);
        
        // Password Field
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(new Color(44, 62, 80));
        gbc.gridy = 5;
        rightPanel.add(lblPassword, gbc);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy = 6;
        rightPanel.add(txtPassword, gbc);
        
        // Show Password & Remember Me
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        optionsPanel.setBackground(Color.WHITE);
        
        chkShowPassword = new JCheckBox("Show Password");
        chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPassword.setBackground(Color.WHITE);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.addActionListener(e -> togglePasswordVisibility());
        
        chkRememberMe = new JCheckBox("Remember Me");
        chkRememberMe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRememberMe.setBackground(Color.WHITE);
        chkRememberMe.setFocusPainted(false);
        
        optionsPanel.add(chkShowPassword);
        optionsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        optionsPanel.add(chkRememberMe);
        
        gbc.gridy = 7;
        rightPanel.add(optionsPanel, gbc);
        
        // Login Button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setPreferredSize(new Dimension(0, 45));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> performLogin());
        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(52, 152, 219));
            }
        });
        
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(btnLogin, gbc);
        
        // Exit Button
        btnExit = new JButton("EXIT");
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExit.setForeground(new Color(149, 165, 166));
        btnExit.setBackground(Color.WHITE);
        btnExit.setPreferredSize(new Dimension(0, 35));
        btnExit.setFocusPainted(false);
        btnExit.setBorder(new LineBorder(new Color(189, 195, 199), 2));
        btnExit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> System.exit(0));
        
        gbc.gridy = 9;
        gbc.insets = new Insets(5, 10, 10, 10);
        rightPanel.add(btnExit, gbc);
        
        // Login Attempts Label
        lblLoginAttempts = new JLabel("");
        lblLoginAttempts.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLoginAttempts.setForeground(new Color(231, 76, 60));
        lblLoginAttempts.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 10;
        rightPanel.add(lblLoginAttempts, gbc);
        
        // Default Credentials Info
        JLabel lblInfo = new JLabel("<html><center><br>Default Login:<br>admin / admin123</center></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblInfo.setForeground(new Color(149, 165, 166));
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 11;
        rightPanel.add(lblInfo, gbc);
        
        // Add panels to main
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        add(mainPanel);
        
        // Enter key listener
        txtPassword.addActionListener(e -> performLogin());
    }
    
    private void togglePasswordVisibility() {
        if (chkShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('•');
        }
    }
    
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan Password tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Check max attempts
        if (loginAttempts >= MAX_ATTEMPTS) {
            JOptionPane.showMessageDialog(this,
                "Terlalu banyak percobaan login gagal!\nAplikasi akan ditutup.",
                "Security Alert",
                JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }
        
        // Show loading
        btnLogin.setEnabled(false);
        btnLogin.setText("LOGGING IN...");
        
        // Perform login in background thread
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                Thread.sleep(500); // Simulate network delay
                return userDAO.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    User user = get();
                    
                    if (user != null) {
                        // Login success
                        SessionManager.setCurrentUser(user);
                        
                        JOptionPane.showMessageDialog(LoginForm.this,
                            "Login berhasil!\nSelamat datang, " + user.getFullName(),
                            "Login Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Open main application
                        SwingUtilities.invokeLater(() -> {
                            MainFrame mainFrame = new MainFrame();
                            mainFrame.setVisible(true);
                            dispose();
                        });
                        
                    } else {
                        // Login failed
                        loginAttempts++;
                        int remainingAttempts = MAX_ATTEMPTS - loginAttempts;
                        
                        lblLoginAttempts.setText(String.format(
                            "Login gagal! Sisa percobaan: %d", remainingAttempts));
                        
                        JOptionPane.showMessageDialog(LoginForm.this,
                            "Username atau Password salah!\nSisa percobaan: " + remainingAttempts,
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                        
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                        
                        btnLogin.setEnabled(true);
                        btnLogin.setText("LOGIN");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(LoginForm.this,
                        "Error: " + e.getMessage(),
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
                    
                    btnLogin.setEnabled(true);
                    btnLogin.setText("LOGIN");
                }
            }
        };
        
        worker.execute();
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
}
