/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateCTHDKB extends JFrame {

    private JComboBox<String> DVComboBox;
    private JTextField txtSoLuong;
    private JButton btnSubmit;
    private String maHDKB;

    public class ImagePanel extends JPanel {

        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public CreateCTHDKB(String maHDKB) throws SQLException, ClassNotFoundException {
        this.maHDKB = maHDKB;

        setTitle("Tạo chi tiết hóa đơn khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());

        // Set background
        ImagePanel backgroundPanel = new ImagePanel("src/views/doctor/image/bill.png");
        setContentPane(backgroundPanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel("TẠO CHI TIẾT HÓA ĐƠN KHÁM BỆNH", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Mã khám
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblDV = new JLabel("Tên dịch vụ:");
        lblDV.setForeground(Color.BLACK);
        lblDV.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblDV, gbc);
        gbc.gridx = 1;
        DVComboBox = new JComboBox<>();
        DVComboBox.setFont(inputFont);
        DVComboBox.setBackground(new Color(0xd9eef2));
        DVComboBox.setOpaque(true);
        loadDV();
        formPanel.add(DVComboBox, gbc);

        //số lượng
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblSoLuong = new JLabel("Số lượng:");
        lblSoLuong.setForeground(Color.BLACK);
        lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblSoLuong, gbc);
        gbc.gridx = 1;
        txtSoLuong = new JTextField();
        txtSoLuong.setFont(inputFont);
        txtSoLuong.setBackground(new Color(0xd9eef2));
        formPanel.add(txtSoLuong, gbc);


        // thêm chi tiết
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Thêm chi tiết");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSubmit.setBackground(new Color(0x2B4A59));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setPreferredSize(new Dimension(120, 35));
        btnSubmit.addActionListener(e -> taoCTHDKB(maHDKB));

        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0x2B4A59));
            }
        });

        formPanel.add(btnSubmit, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadDV() throws SQLException, ClassNotFoundException {
        String sql = "SELECT TENDV FROM DICHVUKHAM";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DVComboBox.addItem(rs.getString("TENDV"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dịch vụ khám: " + e.getMessage());
        }
    }

    private String generateAppointmentId(Connection conn) {
        String prefix = "CTHDKB";
        String sql = "SELECT MAX(MACTHDKB) FROM CTHD_KHAMBENH";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null) {
                    int num = Integer.parseInt(lastId.replace(prefix, ""));
                    return prefix + String.format("%03d", num + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001"; // Nếu chưa có lịch hẹn nào
    }

    private void taoCTHDKB(String maHDKB) {
        if (DVComboBox.getSelectedItem() == null
                || txtSoLuong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        String tenDV = (String) DVComboBox.getSelectedItem();
        int sl = Integer.parseInt(txtSoLuong.getText());

        try (Connection conn = DBConnection.getConnection()) {
            String maCTHDKB = generateAppointmentId(conn);

            String maDV = null;
            double triGia = 0;
            String sql = "SELECT MADV, DONGIA FROM DICHVUKHAM WHERE TENDV = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tenDV.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        maDV = rs.getString("MADV");
                        triGia = rs.getDouble("DONGIA") ;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tải dịch vụ khám: " + e.getMessage());
            }

            sql = "INSERT INTO CTHD_KHAMBENH(MACTHDKB, MAHDKB, MADV, SOLUONGDV, TRIGIA) "
                    + "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maCTHDKB);
                ps.setString(2, maHDKB);
                ps.setString(3, maDV);
                ps.setInt(4, sl);
                ps.setDouble(5, triGia*sl);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Tạo chi tiết hóa đơn khám bệnh thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Tạo thất bại.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
