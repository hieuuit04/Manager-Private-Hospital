/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import views.patient.PatientDashboard;
import utils.DBConnection;
import utils.HashUtil;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import views.doctor.DoctorDashboard;
import views.pharmacist.PharmacistDashboard;


public class LoginForm extends JFrame {
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JButton btnLogin, btnRegister;
    
    public LoginForm() {
        setTitle("Đăng nhập");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLocationRelativeTo(null); // canh giữa
        initUI();
    }

    private void initUI() {
        // Panel chính vẽ background full màn hình
        JPanel backgroundPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("src/views/BackgroundLogin.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Panel đăng nhập bên trái với nền sáng, padding
        JPanel loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Không vẽ gì ở đây => trong suốt hoàn toàn
            }
        };
        loginPanel.setOpaque(false); // làm trong suốt panel
        loginPanel.setPreferredSize(new Dimension(500, 0)); // rộng 400 px
        loginPanel.setBackground(new Color(255, 255, 255, 200)); // nền trắng hơi trong suốt (alpha=200)
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        JLabel lbUsername = new JLabel("Tên đăng nhập:");
        lbUsername.setFont(new Font("Arial", Font.BOLD, 20));
        lbUsername.setForeground(Color.WHITE);
        
        tfUsername = new JTextField(20);
        tfUsername.setFont(new Font("Arial", Font.PLAIN, 18));
        tfUsername.setOpaque(false);
        tfUsername.setBackground(new Color(0, 0, 0, 80));
        tfUsername.setForeground(Color.WHITE);
        tfUsername.setCaretColor(Color.WHITE);
        tfUsername.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbPassword = new JLabel("Mật khẩu:");
        lbPassword.setFont(new Font("Arial", Font.BOLD, 20));
        lbPassword.setForeground(Color.WHITE);

        pfPassword = new JPasswordField(20);
        pfPassword.setFont(new Font("Arial", Font.PLAIN, 25));
        pfPassword.setOpaque(false);
        pfPassword.setBackground(new Color(0, 0, 0, 80));
        pfPassword.setForeground(Color.WHITE);
        pfPassword.setCaretColor(Color.WHITE);
        pfPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        btnLogin = createStyledButton("Đăng nhập", new Color(0x588EA7));
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btnRegister = createStyledButton("Đăng ký", new Color(0x588EA7));
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JLabel lbOr = new JLabel("_____________ Hoặc _____________");
        lbOr.setFont(new Font("Arial", Font.PLAIN, 16));
        lbOr.setForeground(Color.WHITE);
        lbOr.setHorizontalAlignment(SwingConstants.CENTER);

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y++;
        loginPanel.add(lbUsername, gbc);  // dùng biến đã set font
        gbc.gridy = y++;
        loginPanel.add(tfUsername, gbc);

        gbc.gridy = y++;
        loginPanel.add(lbPassword, gbc);  // dùng biến đã set font
        gbc.gridy = y++;
        loginPanel.add(pfPassword, gbc);

        // Tăng khoảng cách giữa password và login
        gbc.gridy = y++;
        gbc.insets = new Insets(20, 0, 10, 0);
        loginPanel.add(btnLogin, gbc);

        // Thêm dòng "Hoặc"
        gbc.gridy = y++;
        gbc.insets = new Insets(10, 0, 10, 0);
        loginPanel.add(lbOr, gbc);

        // Nút đăng ký
        gbc.gridy = y++;
        loginPanel.add(btnRegister, gbc); gbc.gridy = y++;
        

        // Thêm panel đăng nhập vào bên trái backgroundPanel
        backgroundPanel.add(loginPanel, BorderLayout.WEST);

        setContentPane(backgroundPanel);

        btnLogin.addActionListener(e -> login());
        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            Color currentColor = baseColor;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 22));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        currentColor = new Color(0xff9800);
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        currentColor = baseColor;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(currentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };
        return button;
    }

    private void login() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());
        String hashedPassword = HashUtil.hashPassword(password);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hoTen = rs.getString("HOTENND");
                String userId = rs.getString("ID");
                String role = rs.getString("ROLE");
                
                //showCustomMessageDialog(this, "Đăng nhập thành công với vai trò: " + role);
                
                switch (role) {
                    case "Bệnh nhân" -> { 
                        System.out.println("Opening interface of Patient...");
                        new PatientDashboard(String.valueOf(hoTen), userId).setVisible(true);
                    }
                    case "Bác sĩ" -> {
                        System.out.println("Opening interface of Doctor...");
                        new DoctorDashboard(String.valueOf(hoTen), userId).setVisible(true);
                    }
                    case "Dược sĩ" -> {
                        System.out.println("Opening interface of Pharmacist...");
                        new views.pharmacist.PharmacistDashboard(userId, String.valueOf(hoTen)).setVisible(true);
                    }
                    case "Nhân viên" -> {
                        // Gọi dashboard cho nhân viên
                    }
                    default -> {
                        JOptionPane.showMessageDialog(this, "Chưa hỗ trợ giao diện cho vai trò: " + role);
                        return;
                    }
                }

                dispose(); // đóng form đăng nhập
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng nhập!");
        }
    }
}
