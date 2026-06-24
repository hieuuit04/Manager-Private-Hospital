/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.menuPatient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.HashUtil;


public class PersonalInfo extends JPanel {
    private Runnable onBackCallback;
    private CardLayout contentLayout;
    private JPanel contentPanel;
    private JLabel thongTinLabel, hoSoLabel, taiKhoanLabel;
    private JTextField txtHoTen, txtGioiTinh, txtNgaySinh, txtDiaChi, txtSDT, txtEmail, 
                       txtMaBN, txtBHYT, txtLSBL, txtDiUng, txtNhomMau, 
                       txtUserName, txtState;
    private boolean isEditing = false;
    private String patientId;
    private JLabel lblHoTenValue, lblGioiTinhValue, lblNgaySinhValue, lblDiaChiValue, lblSDTValue, lblEmailValue,
                   lblMaBNValue, lblBHYTValue, lblLSBLValue, lblDiUngValue, lblNhomMauValue,
                   lblUserNameValue, lblPassWordValue, lblStateValue;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JLabel lblPassWord;
    private JLabel lblOldPass;
    private JLabel lblNewPass;

    
    public PersonalInfo(String patientId, Runnable onBackCallback) {
        this.patientId = patientId;
        this.onBackCallback = onBackCallback;
        setLayout(new BorderLayout());
        setBackground(new Color(0,0,0,0));
        
        // Menu đầu trang
        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topMenu.setBackground(new Color(0xe8faf8));

        thongTinLabel = createMenuLabel("Thông tin cá nhân");
        hoSoLabel = createMenuLabel("Hồ sơ bệnh án");
        taiKhoanLabel = createMenuLabel("Tài khoản cá nhân");

        topMenu.add(thongTinLabel);
        topMenu.add(hoSoLabel);
        topMenu.add(taiKhoanLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        contentPanel.add(createThongTinPanel(), "thongtin");
        contentPanel.add(createHoSoPanel(), "hoso");
        contentPanel.add(createTaiKhoanPanel(), "taikhoan");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("thongtin");
        addMenuListeners();
        
    }

    private JLabel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(0x2B4A59));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void switchTab(String tabName) {
        resetMenuStyle();
        contentLayout.show(contentPanel, tabName);
        if (tabName.equals("thongtin")) highlightLabel(thongTinLabel);
        else if (tabName.equals("hoso")) highlightLabel(hoSoLabel);
        else if (tabName.equals("taikhoan")) highlightLabel(taiKhoanLabel);
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {thongTinLabel, hoSoLabel, taiKhoanLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        thongTinLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("thongtin");
            }
        });
        hoSoLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("hoso");
            }
        });
        taiKhoanLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("taikhoan");
            }
        });
    }
    
    public class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            // Load hình ảnh từ đường dẫn
            backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Vẽ hình nền phủ toàn bộ panel
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
    
    
    
    
    
    //Mục Thông tin cá nhân
    private JPanel createThongTinPanel() {
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BG1.png");
        backgroundPanel.setLayout(new BorderLayout()); 

        // Panel chứa toàn bộ nội dung chính
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);

        // ==== Nút Cập nhật nằm phía trên bên phải ====
        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFocusPainted(false);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(0x2B4A59)); // xanh lá
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUpdate.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnUpdate.setBackground(new Color(0x2B4A59));
            }
        });

        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            if (onBackCallback != null) onBackCallback.run();
        });
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x588EA7));
            }
        });

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        updatePanel.setOpaque(false);
        updatePanel.add(btnUpdate);
        contentWrapper.add(updatePanel, BorderLayout.NORTH);
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        backPanel.setOpaque(false);
        backPanel.add(backButton);
        contentWrapper.add(backPanel, BorderLayout.SOUTH);

        // Nội dung chính
        JPanel mainPanelTT = new JPanel(new GridBagLayout());
        mainPanelTT.setOpaque(false);
        
        btnUpdate.addActionListener(e -> {
            if (!isEditing) {
                switchToTextFieldsTT(mainPanelTT);
                btnUpdate.setText("Lưu");
                isEditing = true;
            } else {
                saveUserInfo(); // Gọi hàm cập nhật CSDL
                switchToLabelsTT(mainPanelTT);
                btnUpdate.setText("Cập nhật");
                isEditing = false;
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 10, 30, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = Color.BLACK;

        int y = 1;

        // Họ tên
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(labelFont);
        lblHoTen.setForeground(labelColor);
        mainPanelTT.add(lblHoTen, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblHoTenValue = new JLabel();
        lblHoTenValue.setFont(textFont);
        mainPanelTT.add(lblHoTenValue, gbc);

        // Giới tính
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(labelFont);
        lblGioiTinh.setForeground(labelColor);
        mainPanelTT.add(lblGioiTinh, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblGioiTinhValue = new JLabel();
        lblGioiTinhValue.setFont(textFont);
        mainPanelTT.add(lblGioiTinhValue, gbc);

        // Ngày sinh
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(labelFont);
        lblNgaySinh.setForeground(labelColor);
        mainPanelTT.add(lblNgaySinh, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblNgaySinhValue = new JLabel();
        lblNgaySinhValue.setFont(textFont);
        mainPanelTT.add(lblNgaySinhValue, gbc);

        // Địa chỉ
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(labelFont);
        lblDiaChi.setForeground(labelColor);
        mainPanelTT.add(lblDiaChi, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblDiaChiValue = new JLabel();
        lblDiaChiValue.setFont(textFont);
        mainPanelTT.add(lblDiaChiValue, gbc);

        // Số điện thoại
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setFont(labelFont);
        lblSDT.setForeground(labelColor);
        mainPanelTT.add(lblSDT, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblSDTValue = new JLabel();
        lblSDTValue.setFont(textFont);
        mainPanelTT.add(lblSDTValue, gbc);
        
        // Email
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);
        mainPanelTT.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblEmailValue = new JLabel();
        lblEmailValue.setFont(textFont);
        mainPanelTT.add(lblEmailValue, gbc);


        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));
        wrapperPanel.add(mainPanelTT);
        contentWrapper.add(wrapperPanel, BorderLayout.CENTER);

        backgroundPanel.add(contentWrapper, BorderLayout.CENTER);
        
        loadUserInfoFromDB();

        return backgroundPanel;
    }
    
    private GridBagConstraints getConstraintsAt(JPanel panel, Component component) {
        for (Component comp : panel.getComponents()) {
            if (comp == component) {
                Object layout = panel.getLayout();
                if (layout instanceof GridBagLayout) {
                    return ((GridBagLayout) layout).getConstraints(component);
                }
            }
        }
        return null;
    }
    
    
    private void switchToTextFieldsTT(JPanel mainPanelTT) {
        lblHoTenValue.setVisible(false);
        txtHoTen = new JTextField(lblHoTenValue.getText(), 20);
        mainPanelTT.add(txtHoTen, getConstraintsAt(mainPanelTT, lblHoTenValue));

        lblGioiTinhValue.setVisible(false);
        txtGioiTinh = new JTextField(lblGioiTinhValue.getText(), 20);
        mainPanelTT.add(txtGioiTinh, getConstraintsAt(mainPanelTT, lblGioiTinhValue));

        lblNgaySinhValue.setVisible(false);
        txtNgaySinh = new JTextField(lblNgaySinhValue.getText(), 20);
        mainPanelTT.add(txtNgaySinh, getConstraintsAt(mainPanelTT, lblNgaySinhValue));

        lblDiaChiValue.setVisible(false);
        txtDiaChi = new JTextField(lblDiaChiValue.getText(), 20);
        mainPanelTT.add(txtDiaChi, getConstraintsAt(mainPanelTT, lblDiaChiValue));

        lblSDTValue.setVisible(false);
        txtSDT = new JTextField(lblSDTValue.getText(), 20);
        mainPanelTT.add(txtSDT, getConstraintsAt(mainPanelTT, lblSDTValue));

        lblEmailValue.setVisible(false);
        txtEmail = new JTextField(lblEmailValue.getText(), 20);
        mainPanelTT.add(txtEmail, getConstraintsAt(mainPanelTT, lblEmailValue));

        mainPanelTT.revalidate();
        mainPanelTT.repaint();
    }

    private void switchToLabelsTT(JPanel mainPanelTT) {
        lblHoTenValue.setText(txtHoTen.getText());
        mainPanelTT.remove(txtHoTen);
        lblHoTenValue.setVisible(true);

        lblGioiTinhValue.setText(txtGioiTinh.getText());
        mainPanelTT.remove(txtGioiTinh);
        lblGioiTinhValue.setVisible(true);

        lblNgaySinhValue.setText(txtNgaySinh.getText());
        mainPanelTT.remove(txtNgaySinh);
        lblNgaySinhValue.setVisible(true);

        lblDiaChiValue.setText(txtDiaChi.getText());
        mainPanelTT.remove(txtDiaChi);
        lblDiaChiValue.setVisible(true);

        lblSDTValue.setText(txtSDT.getText());
        mainPanelTT.remove(txtSDT);
        lblSDTValue.setVisible(true);

        lblEmailValue.setText(txtEmail.getText());
        mainPanelTT.remove(txtEmail);
        lblEmailValue.setVisible(true);

        mainPanelTT.revalidate();
        mainPanelTT.repaint();
    }

    private void loadUserInfoFromDB() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT HOTENND, SDT, EMAIL, GIOITINH, NGAYSINH, DIACHI FROM USERS WHERE ID = ?")) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblHoTenValue.setText(rs.getString("HOTENND"));
                lblSDTValue.setText(rs.getString("SDT"));
                lblEmailValue.setText(rs.getString("EMAIL"));
                lblGioiTinhValue.setText(rs.getString("GIOITINH"));
                lblNgaySinhValue.setText(rs.getDate("NGAYSINH").toString());
                lblDiaChiValue.setText(rs.getString("DIACHI"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    private void saveUserInfo() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE USERS SET HOTENND = ?, SDT = ?, EMAIL = ?, GIOITINH = ?, NGAYSINH = ?, DIACHI = ? WHERE ID = ?")) {
            ps.setString(1, txtHoTen.getText());
            ps.setString(2, txtSDT.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtGioiTinh.getText());
            ps.setDate(5, java.sql.Date.valueOf(txtNgaySinh.getText()));
            ps.setString(6, txtDiaChi.getText());
            ps.setString(7, patientId);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    
    
    
    //Mục Hồ sơ bệnh án
    private JPanel createHoSoPanel() {
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BG4.png");
        backgroundPanel.setLayout(new BorderLayout()); 

        // Panel chứa toàn bộ nội dung chính
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);

        // ==== Nút Cập nhật nằm phía trên bên phải ====
        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFocusPainted(false);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(0x2B4A59)); // xanh lá
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUpdate.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnUpdate.setBackground(new Color(0x2B4A59));
            }
        });


        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            if (onBackCallback != null) onBackCallback.run();
        });
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x588EA7));
            }
        });

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        updatePanel.setOpaque(false);
        updatePanel.add(btnUpdate);
        contentWrapper.add(updatePanel, BorderLayout.NORTH);
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        backPanel.setOpaque(false);
        backPanel.add(backButton);
        contentWrapper.add(backPanel, BorderLayout.SOUTH);

        // Nội dung chính
        JPanel mainPanelHS = new JPanel(new GridBagLayout());
        mainPanelHS.setOpaque(false);
        
        btnUpdate.addActionListener(e -> {
            if (!isEditing) {
                switchToTextFieldsHS(mainPanelHS);
                btnUpdate.setText("Lưu");
                isEditing = true;
            } else {
                savePatientRecord(); // Gọi hàm cập nhật CSDL
                switchToLabelsHS(mainPanelHS);
                btnUpdate.setText("Cập nhật");
                isEditing = false;
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 10, 25, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = Color.BLACK;

        int y = 1;

        // Mã bệnh nhân
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblMaBN = new JLabel("Mã bệnh nhân:");
        lblMaBN.setFont(labelFont);
        lblMaBN.setForeground(labelColor);
        mainPanelHS.add(lblMaBN, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblMaBNValue = new JLabel();
        lblMaBNValue.setFont(textFont);
        mainPanelHS.add(lblMaBNValue, gbc);

        // Số BHYT
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblBHYT = new JLabel("Số bảo hiểm y tế:");
        lblBHYT.setFont(labelFont);
        lblBHYT.setForeground(labelColor);
        mainPanelHS.add(lblBHYT, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblBHYTValue = new JLabel();
        lblBHYTValue.setFont(textFont);
        mainPanelHS.add(lblBHYTValue, gbc);

        // Lịch sử bệnh lý
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblLSBL = new JLabel("Lịch sử bệnh lý:");
        lblLSBL.setFont(labelFont);
        lblLSBL.setForeground(labelColor);
        mainPanelHS.add(lblLSBL, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblLSBLValue = new JLabel();
        lblLSBLValue.setFont(textFont);
        mainPanelHS.add(lblLSBLValue, gbc);


        // Dị ứng
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblDiUng = new JLabel("Dị ứng:");
        lblDiUng.setFont(labelFont);
        lblDiUng.setForeground(labelColor);
        mainPanelHS.add(lblDiUng, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblDiUngValue = new JLabel();
        lblDiUngValue.setFont(textFont);
        mainPanelHS.add(lblDiUngValue, gbc);

        // Nhóm máu
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblNhomMau = new JLabel("Nhóm máu:");
        lblNhomMau.setFont(labelFont);
        lblNhomMau.setForeground(labelColor);
        mainPanelHS.add(lblNhomMau, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblNhomMauValue = new JLabel();
        lblNhomMauValue.setFont(textFont);
        mainPanelHS.add(lblNhomMauValue, gbc);
        
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));
        wrapperPanel.add(mainPanelHS);
        contentWrapper.add(wrapperPanel, BorderLayout.CENTER);

        backgroundPanel.add(contentWrapper, BorderLayout.CENTER);
        
        loadPatientRecordFromDB();

        return backgroundPanel;
    }
    
    
    private void switchToTextFieldsHS(JPanel mainPanelHS) {
        lblMaBNValue.setVisible(false);
        txtMaBN = new JTextField(lblMaBNValue.getText(), 20);
        txtMaBN.setEditable(false);
        mainPanelHS.add(txtMaBN, getConstraintsAt(mainPanelHS, lblMaBNValue));

        lblBHYTValue.setVisible(false);
        txtBHYT = new JTextField(lblBHYTValue.getText(), 20);
        mainPanelHS.add(txtBHYT, getConstraintsAt(mainPanelHS, lblBHYTValue));

        lblLSBLValue.setVisible(false);
        txtLSBL = new JTextField(lblLSBLValue.getText(), 20);
        mainPanelHS.add(txtLSBL, getConstraintsAt(mainPanelHS, lblLSBLValue));

        lblDiUngValue.setVisible(false);
        txtDiUng = new JTextField(lblDiUngValue.getText(), 20);
        mainPanelHS.add(txtDiUng, getConstraintsAt(mainPanelHS, lblDiUngValue));

        lblNhomMauValue.setVisible(false);
        txtNhomMau = new JTextField(lblNhomMauValue.getText(), 20);
        mainPanelHS.add(txtNhomMau, getConstraintsAt(mainPanelHS, lblNhomMauValue));

        mainPanelHS.revalidate();
        mainPanelHS.repaint();
    }

    private void switchToLabelsHS(JPanel mainPanelHS) {
        lblMaBNValue.setText(txtMaBN.getText());
        mainPanelHS.remove(txtMaBN);
        lblMaBNValue.setVisible(true);

        lblBHYTValue.setText(txtBHYT.getText());
        mainPanelHS.remove(txtBHYT);
        lblBHYTValue.setVisible(true);

        lblLSBLValue.setText(txtLSBL.getText());
        mainPanelHS.remove(txtLSBL);
        lblLSBLValue.setVisible(true);

        lblDiUngValue.setText(txtDiUng.getText());
        mainPanelHS.remove(txtDiUng);
        lblDiUngValue.setVisible(true);

        lblNhomMauValue.setText(txtNhomMau.getText());
        mainPanelHS.remove(txtNhomMau);
        lblNhomMauValue.setVisible(true);

        mainPanelHS.revalidate();
        mainPanelHS.repaint();
    }

    private void loadPatientRecordFromDB() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT MABN, SOBHYT, LICHSU_BENHLY, DIUNG, NHOMMAU FROM BENHNHAN WHERE MABN = ?")) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblMaBNValue.setText(rs.getString("MABN"));
                lblBHYTValue.setText(rs.getString("SOBHYT"));
                lblLSBLValue.setText(rs.getString("LICHSU_BENHLY"));
                lblDiUngValue.setText(rs.getString("DIUNG"));
                lblNhomMauValue.setText(rs.getString("NHOMMAU"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    private void savePatientRecord() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("UPDATE BENHNHAN SET MABN = ?, SOBHYT = ?, LICHSU_BENHLY = ?, DIUNG = ?, NHOMMAU = ? WHERE MABN = ?")) {
            ps.setString(1, txtMaBN.getText());
            ps.setString(2, txtBHYT.getText());
            ps.setString(3, txtLSBL.getText());
            ps.setString(4, txtDiUng.getText());
            ps.setString(5, txtNhomMau.getText());
            ps.setString(6, patientId);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    
    
    
    // Mục Tài khoản cá nhân
    private JPanel createTaiKhoanPanel() {
        
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BG3.png");
        backgroundPanel.setLayout(new BorderLayout()); 

       // Panel chứa toàn bộ nội dung chính
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);

        // ==== Nút Cập nhật nằm phía trên bên phải ====
        JButton btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFocusPainted(false);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(0x2B4A59)); // xanh lá
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnUpdate.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnUpdate.setBackground(new Color(0x2B4A59));
            }
        });


        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            if (onBackCallback != null) onBackCallback.run();
        });
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x588EA7));
            }
        });

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        updatePanel.setOpaque(false);
        updatePanel.add(btnUpdate);
        contentWrapper.add(updatePanel, BorderLayout.NORTH);
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        backPanel.setOpaque(false);
        backPanel.add(backButton);
        contentWrapper.add(backPanel, BorderLayout.SOUTH);

        // Nội dung chính
        JPanel mainPanelTK = new JPanel(new GridBagLayout());
        mainPanelTK.setOpaque(false);
        
         btnUpdate.addActionListener(e -> {
            if (!isEditing) {
                switchToTextFieldsTK(mainPanelTK);
                btnUpdate.setText("Lưu");
                isEditing = true;
            } else {
                savePersonalAccount(); // Gọi hàm cập nhật CSDL
                switchToLabelsTK(mainPanelTK);
                btnUpdate.setText("Cập nhật");
                isEditing = false;
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(25, 10, 25, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = Color.BLACK;

        int y = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblUserName = new JLabel("Username:");
        lblUserName.setFont(labelFont);
        lblUserName.setForeground(labelColor);
        mainPanelTK.add(lblUserName, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblUserNameValue = new JLabel();
        lblUserNameValue.setFont(textFont);
        mainPanelTK.add(lblUserNameValue, gbc);

        // PassWord
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        //JLabel lblPassWord = new JLabel("PassWord:");
        lblPassWord = new JLabel("PassWord:");
        lblPassWord.setFont(labelFont);
        lblPassWord.setForeground(labelColor);
        mainPanelTK.add(lblPassWord, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblPassWordValue = new JLabel("*****");
        lblPassWordValue.setFont(textFont);
        mainPanelTK.add(lblPassWordValue, gbc);

        // Trạng thái tài khoản
        y++;
        y++;
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0;
        JLabel lblState = new JLabel("Trạng thái tài khoản:");
        lblState.setFont(labelFont);
        lblState.setForeground(labelColor);
        mainPanelTK.add(lblState, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lblStateValue = new JLabel();
        lblStateValue.setFont(textFont);
        mainPanelTK.add(lblStateValue, gbc);
        
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperPanel.setOpaque(false);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 30, 50));
        wrapperPanel.add(mainPanelTK);
        contentWrapper.add(wrapperPanel, BorderLayout.CENTER);

        backgroundPanel.add(contentWrapper, BorderLayout.CENTER);
        
        loadPersonalAccountFromDB();

        return backgroundPanel;
    }
    
    private void switchToTextFieldsTK(JPanel mainPanelTK) {
        lblUserNameValue.setVisible(false);
        txtUserName = new JTextField(lblUserNameValue.getText(), 20);
        txtUserName.setEditable(false);
        mainPanelTK.add(txtUserName, getConstraintsAt(mainPanelTK, lblUserNameValue));

        // Ẩn label mật khẩu
        lblPassWord.setVisible(false);
        lblPassWordValue.setVisible(false);

        // === THÊM label và field cho mật khẩu cũ ===
        lblOldPass = new JLabel("Mật khẩu cũ:");
        lblOldPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblOldPass.setForeground(Color.BLACK);
        GridBagConstraints gbcOldLabel = new GridBagConstraints();
        gbcOldLabel.gridx = 0;
        gbcOldLabel.gridy = 2;
        gbcOldLabel.insets = new Insets(25, 10, 5, 10);
        gbcOldLabel.anchor = GridBagConstraints.WEST;
        mainPanelTK.add(lblOldPass, gbcOldLabel);

        oldPasswordField = new JPasswordField(20);
        oldPasswordField.setToolTipText("Nhập mật khẩu cũ");
        GridBagConstraints gbcOldField = new GridBagConstraints();
        gbcOldField.gridx = 1;
        gbcOldField.gridy = 2;
        gbcOldField.insets = new Insets(25, 10, 5, 10);
        gbcOldField.fill = GridBagConstraints.HORIZONTAL;
        mainPanelTK.add(oldPasswordField, gbcOldField);

        // === THÊM label và field cho mật khẩu mới ===
        lblNewPass = new JLabel("Mật khẩu mới:");
        lblNewPass.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNewPass.setForeground(Color.BLACK);
        GridBagConstraints gbcNewLabel = new GridBagConstraints();
        gbcNewLabel.gridx = 0;
        gbcNewLabel.gridy = 3;
        gbcNewLabel.insets = new Insets(5, 10, 25, 10);
        gbcNewLabel.anchor = GridBagConstraints.WEST;
        mainPanelTK.add(lblNewPass, gbcNewLabel);

        newPasswordField = new JPasswordField(20);
        newPasswordField.setToolTipText("Nhập mật khẩu mới");
        GridBagConstraints gbcNewField = new GridBagConstraints();
        gbcNewField.gridx = 1;
        gbcNewField.gridy = 3;
        gbcNewField.insets = new Insets(5, 10, 25, 10);
        gbcNewField.fill = GridBagConstraints.HORIZONTAL;
        mainPanelTK.add(newPasswordField, gbcNewField);

        lblStateValue.setVisible(false);
        txtState = new JTextField(lblStateValue.getText(), 20);
        txtState.setEditable(false);
        GridBagConstraints gbcState = new GridBagConstraints();
        gbcState.gridx = 1;
        gbcState.gridy = 4;
        gbcState.insets = new Insets(5, 10, 10, 10);
        gbcState.fill = GridBagConstraints.HORIZONTAL;
        mainPanelTK.add(txtState, gbcState);

        mainPanelTK.revalidate();
        mainPanelTK.repaint();
    }



    private void switchToLabelsTK(JPanel mainPanelTK) {
        lblUserNameValue.setText(txtUserName.getText());
        mainPanelTK.remove(txtUserName);
        lblUserNameValue.setVisible(true);

        // Xóa field mật khẩu cũ và mới
        mainPanelTK.remove(lblOldPass);
        mainPanelTK.remove(lblNewPass);
        mainPanelTK.remove(oldPasswordField);
        mainPanelTK.remove(newPasswordField);

        // Hiện lại label mật khẩu
        lblPassWordValue.setText("*****");
        lblPassWord.setVisible(true);
        lblPassWordValue.setVisible(true);

        lblStateValue.setText(txtState.getText());
        mainPanelTK.remove(txtState);
        lblStateValue.setVisible(true);

        mainPanelTK.revalidate();
        mainPanelTK.repaint();
    }




    private void loadPersonalAccountFromDB() {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT USERNAME, TRANGTHAI FROM USERS WHERE ID = ?")) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblUserNameValue.setText(rs.getString("USERNAME"));
                //lblPassWordValue.setText(rs.getString("PASSWORD"));
                lblStateValue.setText(rs.getString("TRANGTHAI"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    private void savePersonalAccount() {
        String oldPass = String.valueOf(oldPasswordField.getPassword()).trim();
        String newPass = String.valueOf(newPasswordField.getPassword()).trim();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ cả mật khẩu cũ và mật khẩu mới!");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement("SELECT PASSWORD FROM USERS WHERE ID = ?")) {

            checkPs.setString(1, patientId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                String currentHashedPass = rs.getString("PASSWORD");

                // So sánh mật khẩu cũ đã hash
                String oldHashedInput = HashUtil.hashPassword(oldPass);

                if (!currentHashedPass.equals(oldHashedInput)) {
                    JOptionPane.showMessageDialog(null, "Mật khẩu cũ không đúng!");
                    return;
                }

                // Mã hóa mật khẩu mới
                String newHashedPass = HashUtil.hashPassword(newPass);

                // Cập nhật mật khẩu mới vào DB
                try (PreparedStatement updatePs = conn.prepareStatement("UPDATE USERS SET PASSWORD = ? WHERE ID = ?")) {
                    updatePs.setString(1, newHashedPass);
                    updatePs.setString(2, patientId);
                    updatePs.executeUpdate();

                    lblPassWordValue.setText(newHashedPass); // Cập nhật lại hiển thị
                    JOptionPane.showMessageDialog(null, "Cập nhật mật khẩu thành công.");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật tài khoản!");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thông tin cá nhân");
            frame.setTitle("Hồ sơ cá nhân");
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // Co theo kích thước màn hình (đề xuất)
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height); // full màn hình

            // Tạo và thêm PersonalInfo panel
            PersonalInfo profilePanel = new PersonalInfo("U002", () -> {
                System.out.println("Quay lại được gọi!");
            });
            frame.add(profilePanel);

            frame.setVisible(true);
        });
    }
}

