/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views;

import utils.DBConnection;
import utils.HashUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterForm extends JFrame {
    private JTextField tfUsername, tfName, tfEmail, tfPhone, tfAddress, tfBirthDate;
    private JPasswordField pfPassword;
    private JComboBox<String> cbGender, cbRole;
    private JButton btnRegister, btnLogin;

    public RegisterForm() {
        setTitle("Đăng ký tài khoản");
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

        // Panel đăng ký bên trái, trong suốt, padding
        JPanel registerPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // không vẽ gì thêm => trong suốt
            }
        };
        registerPanel.setOpaque(false);
        registerPanel.setPreferredSize(new Dimension(500, 0));
        registerPanel.setBackground(new Color(255, 255, 255, 200)); // nền trắng hơi trong suốt (alpha=200)
        registerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 2.0;

        JLabel lbUsername = new JLabel("Tên đăng nhập:");
        lbUsername.setFont(new Font("Arial", Font.BOLD, 16));
        lbUsername.setForeground(Color.WHITE);
        
        tfUsername = new JTextField(50);
        tfUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        tfUsername.setOpaque(false);
        tfUsername.setBackground(new Color(0, 0, 0, 80));
        tfUsername.setForeground(Color.WHITE);
        tfUsername.setCaretColor(Color.WHITE);
        tfUsername.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
       
        
        JLabel lbPassword = new JLabel("Mật khẩu:");
        lbPassword.setFont(new Font("Arial", Font.BOLD, 16));
        lbPassword.setForeground(Color.WHITE);
        
        pfPassword = new JPasswordField(50);
        pfPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        pfPassword.setOpaque(false);
        pfPassword.setBackground(new Color(0, 0, 0, 80));
        pfPassword.setForeground(Color.WHITE);
        pfPassword.setCaretColor(Color.WHITE);
        pfPassword.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbName = new JLabel("Họ tên:");
        lbName.setFont(new Font("Arial", Font.BOLD, 16));
        lbName.setForeground(Color.WHITE);
        
        tfName = new JTextField(50);
        tfName.setFont(new Font("Arial", Font.PLAIN, 16));
        tfName.setOpaque(false);
        tfName.setBackground(new Color(0, 0, 0, 80));
        tfName.setForeground(Color.WHITE);
        tfName.setCaretColor(Color.WHITE);
        tfName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbEmail = new JLabel("Email:");
        lbEmail.setFont(new Font("Arial", Font.BOLD, 16));
        lbEmail.setForeground(Color.WHITE);
        
        tfEmail = new JTextField(50);
        tfEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        tfEmail.setOpaque(false);
        tfEmail.setBackground(new Color(0, 0, 0, 80));
        tfEmail.setForeground(Color.WHITE);
        tfEmail.setCaretColor(Color.WHITE);
        tfEmail.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbPhone = new JLabel("Số điện thoại:");
        lbPhone.setFont(new Font("Arial", Font.BOLD, 16));
        lbPhone.setForeground(Color.WHITE);
        
        tfPhone = new JTextField(50);
        tfPhone.setFont(new Font("Arial", Font.PLAIN, 16));
        tfPhone.setOpaque(false);
        tfPhone.setBackground(new Color(0, 0, 0, 80));
        tfPhone.setForeground(Color.WHITE);
        tfPhone.setCaretColor(Color.WHITE);
        tfPhone.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbAddress = new JLabel("Địa chỉ:");
        lbAddress.setFont(new Font("Arial", Font.BOLD, 16));
        lbAddress.setForeground(Color.WHITE);
        
        tfAddress = new JTextField(50);
        tfAddress.setFont(new Font("Arial", Font.PLAIN, 16));
        tfAddress.setOpaque(false);
        tfAddress.setBackground(new Color(0, 0, 0, 80));
        tfAddress.setForeground(Color.WHITE);
        tfAddress.setCaretColor(Color.WHITE);
        tfAddress.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbBirthDate = new JLabel("Ngày sinh (yyyy-MM-dd):");
        lbBirthDate.setFont(new Font("Arial", Font.BOLD, 16));
        lbBirthDate.setForeground(Color.WHITE);
        
        tfBirthDate = new JTextField(50);
        tfBirthDate.setFont(new Font("Arial", Font.PLAIN, 16));
        tfBirthDate.setOpaque(false);
        tfBirthDate.setBackground(new Color(0, 0, 0, 80));
        tfBirthDate.setForeground(Color.WHITE);
        tfBirthDate.setCaretColor(Color.WHITE);
        tfBirthDate.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        
        JLabel lbGender = new JLabel("Giới tính:");
        lbGender.setFont(new Font("Arial", Font.BOLD, 16));
        lbGender.setForeground(Color.WHITE);
        
        cbGender = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbGender.setFont(new Font("Arial", Font.BOLD, 16));
        cbGender.setBackground(new Color(0xd9eef2));
        cbGender.setOpaque(false);
        
        JLabel lbRole = new JLabel("Vai trò:");
        lbRole.setFont(new Font("Arial", Font.BOLD, 16));
        lbRole.setForeground(Color.WHITE);
        
        cbRole = new JComboBox<>(new String[]{"Bệnh nhân", "Bác sĩ", "Dược sĩ", "Nhân viên"});
        cbRole.setFont(new Font("Arial", Font.BOLD, 16));
        cbRole.setBackground(new Color(0xd9eef2));
        cbRole.setOpaque(false);


        Color loginColor = new Color(0x588EA7);
        Color registerColor = new Color(0x2B4A59);
        Color hoverColor = new Color(0xff9800);

        // Tạo nút với hiệu ứng màu
        btnRegister = createStyledButton("Đăng ký", registerColor, hoverColor);
        btnLogin = createStyledButton("Đã có tài khoản", loginColor, hoverColor);


        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        registerPanel.add(lbUsername, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfUsername, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbPassword, gbc);
        gbc.gridx = 1;
        registerPanel.add(pfPassword, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbName, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbEmail, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfEmail, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbPhone, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfPhone, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbAddress, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfAddress, gbc);
        
        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbBirthDate, gbc);
        gbc.gridx = 1;
        registerPanel.add(tfBirthDate, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbGender, gbc);
        gbc.gridx = 1;
        registerPanel.add(cbGender, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        registerPanel.add(lbRole, gbc);
        gbc.gridx = 1;
        registerPanel.add(cbRole, gbc);


        // Tăng khoảng cách giữa dòng cuối cùng và nút đăng ký
        gbc.gridy = ++row;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10); // tăng top = 30
        registerPanel.add(btnRegister, gbc);

        // Dòng phân cách "Hoặc"
        gbc.gridy++;
        JLabel orLabel = new JLabel("_____________ Hoặc _____________");
        orLabel.setForeground(Color.WHITE);
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orLabel.setFont(new Font("Arial", Font.BOLD, 20));
        registerPanel.add(orLabel, gbc);

        // Nút "Đã có tài khoản"
        gbc.gridy++;
        gbc.insets = new Insets(20, 10, 10, 10); // reset lại insets
        registerPanel.add(btnLogin, gbc);

        // Thêm registerPanel vào bên trái backgroundPanel
        backgroundPanel.add(registerPanel, BorderLayout.WEST);

        // Đặt backgroundPanel làm nội dung chính của JFrame
        setContentPane(backgroundPanel);

        // Action listeners
        btnRegister.addActionListener(e -> register());
        btnLogin.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

  
    private JButton createStyledButton(String text, Color normalColor, Color hoverColor) {
        return new JButton(text) {
            Color currentColor = normalColor;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 20));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        currentColor = hoverColor;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        currentColor = normalColor;
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
    }


    private void register() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        String address = tfAddress.getText().trim();
        String gender = cbGender.getSelectedItem().toString();
        String birthDateStr = tfBirthDate.getText().trim();
        String role = cbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || birthDateStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        // Parse date
        java.sql.Date birthDate;
        try {
            birthDate = java.sql.Date.valueOf(birthDateStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ. Định dạng đúng: yyyy-MM-dd");
            return;
        }

        String hashedPassword = HashUtil.hashPassword(password);

        try (Connection conn = DBConnection.getConnection()) {
            // Check username existence
            String checkSql = "SELECT COUNT(*) FROM USERS WHERE USERNAME = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, username);
            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!");
                return;
            }

            // Generate new ID
            String newID = generateNewUserID(conn);

            String sql = "INSERT INTO USERS (ID, HOTENND, SDT, EMAIL, GIOITINH, NGAYSINH, DIACHI, ROLE, TRANGTHAI, USERNAME, PASSWORD) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newID);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setDate(6, birthDate);
            ps.setString(7, address);
            ps.setString(8, role);
            ps.setString(9, "ACTIVE");
            ps.setString(10, username);
            ps.setString(11, hashedPassword);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
            new LoginForm().setVisible(true);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng ký: " + ex.getMessage());
        }
    }

    // Tạo ID mới dựa trên số lượng người dùng
    private String generateNewUserID(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM USERS";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        return String.format("U%03d", count + 1);  // VD: U001, U002
    }
}



