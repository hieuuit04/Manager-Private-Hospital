/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import utils.DBConnection;

public class PharmacistView extends JPanel {
    private String pharmacistId;

    public PharmacistView(String pharmacistId, Runnable onBackCallback) {
        this.pharmacistId = pharmacistId;
        setLayout(new BorderLayout());
        setBackground(new Color(232, 250, 248));

        // Tiêu đề
        JLabel title = new JLabel("THÔNG TIN DƯỢC SĨ", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(43, 74, 89));
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 30, 10));
        add(title, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tabbedPane.addTab("Thông tin cá nhân", createUserInfoPanel());
        tabbedPane.addTab("Chuyên môn", createSpecialtyPanel());
        tabbedPane.addTab("Tài khoản cá nhân", createAccountPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Nút quay lại (nếu cần)
        if (onBackCallback != null) {
            JButton backButton = new JButton("← Quay lại");
            backButton.setFocusPainted(false);
            backButton.setForeground(Color.WHITE);
            backButton.setBackground(new Color(0x588EA7));
            backButton.setFont(new Font("Arial", Font.PLAIN, 18));
            backButton.setPreferredSize(new Dimension(160, 40));
            backButton.addActionListener(e -> onBackCallback.run());
            JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
            backPanel.setOpaque(false);
            backPanel.add(backButton);
            add(backPanel, BorderLayout.SOUTH);
        }
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 74, 89), 2, true),
            BorderFactory.createEmptyBorder(60, 80, 60, 80)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 24);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 24);

        String[] labels = {"Mã người dùng:", "Họ tên:", "Số điện thoại:", "Email:", "Giới tính:", "Ngày sinh:", "Địa chỉ:"};
        JTextField[] valueFields = new JTextField[labels.length];
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 30, 18, 30);
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1; 

            valueFields[i] = new JTextField(20);
            valueFields[i].setFont(valueFont);
            valueFields[i].setForeground(new Color(0x00796B));
            valueFields[i].setEditable(false);

            panel.add(valueFields[i], gbc);
        }

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(0x00796B));
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(200, 45));

        updateButton.addActionListener(new ActionListener() {
            private boolean editing = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!editing) {
                    for (int i = 1; i < valueFields.length; i++) valueFields[i].setEditable(true);
                    editing = true;
                    updateButton.setText("Lưu");
                } else {
                    handleUserInfoUpdate(valueFields);
                    for (int i = 1; i < valueFields.length; i++) valueFields[i].setEditable(false);
                    editing = false;
                    updateButton.setText("Cập nhật");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(updateButton, gbc);

        // Load dữ liệu từ USERS
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT ID, HOTENND, SDT, EMAIL, GIOITINH, NGAYSINH, DIACHI FROM USERS WHERE ID = ?")) {
            
            ps.setString(1, pharmacistId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valueFields[0].setText(rs.getString("ID"));
                valueFields[1].setText(rs.getString("HOTENND"));
                valueFields[2].setText(rs.getString("SDT"));
                valueFields[3].setText(rs.getString("EMAIL"));
                valueFields[4].setText(rs.getString("GIOITINH"));
                valueFields[5].setText(rs.getDate("NGAYSINH") != null ? rs.getDate("NGAYSINH").toString() : "");
                valueFields[6].setText(rs.getString("DIACHI"));
            } else {
                for (JTextField v : valueFields) v.setText("-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            for (JTextField v : valueFields) v.setText("Lỗi DB");
        }

        return panel;
    }

    private JPanel createSpecialtyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 74, 89), 2, true),
            BorderFactory.createEmptyBorder(80, 120, 80, 120)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 28);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 28);

        JLabel lblMaDS = new JLabel("Mã dược sĩ:");
        lblMaDS.setFont(labelFont);
        JLabel lblChuyenMon = new JLabel("Chuyên môn:");
        lblChuyenMon.setFont(labelFont);
        JLabel lblBangCap = new JLabel("Bằng cấp:");
        lblBangCap.setFont(labelFont);

        JTextField valMaDS = new JTextField(20);
        valMaDS.setFont(valueFont);
        valMaDS.setForeground(new Color(0x00796B));
        valMaDS.setEditable(false);
        JTextField valChuyenMon = new JTextField(20);
        valChuyenMon.setFont(valueFont);
        valChuyenMon.setForeground(new Color(0x00796B));
        valChuyenMon.setEditable(false);
        JTextField valBangCap = new JTextField(20);
        valBangCap.setFont(valueFont);
        valBangCap.setForeground(new Color(0x00796B));
        valBangCap.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(40, 40, 40, 40);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblMaDS, gbc);
        gbc.gridx = 1; panel.add(valMaDS, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblChuyenMon, gbc);
        gbc.gridx = 1; panel.add(valChuyenMon, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblBangCap, gbc);
        gbc.gridx = 1; panel.add(valBangCap, gbc);

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(0x00796B));
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(200, 45));

        updateButton.addActionListener(new ActionListener() {
            private boolean editing = false;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!editing) {
                    valChuyenMon.setEditable(true);
                    valBangCap.setEditable(true);
                    editing = true;
                    updateButton.setText("Lưu");
                } else {
                    handleSpecialtyUpdate(valMaDS, valChuyenMon, valBangCap);
                    valChuyenMon.setEditable(false);
                    valBangCap.setEditable(false);
                    editing = false;
                    updateButton.setText("Cập nhật");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(updateButton, gbc);

        // Load dữ liệu từ DUOCSI
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT MADS, CHUYENMON, BANGCAP FROM DUOCSI WHERE MADS = ?")) {
            ps.setString(1, pharmacistId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valMaDS.setText(rs.getString("MADS"));
                valChuyenMon.setText(rs.getString("CHUYENMON"));
                valBangCap.setText(rs.getString("BANGCAP"));
            } else {
                valMaDS.setText("-");
                valChuyenMon.setText("-");
                valBangCap.setText("-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            valMaDS.setText("Lỗi DB");
            valChuyenMon.setText("Lỗi DB");
            valBangCap.setText("Lỗi DB");
        }

        return panel;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 74, 89), 2, true),
            BorderFactory.createEmptyBorder(80, 120, 80, 120)
        ));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 28);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 28);

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(labelFont);
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(labelFont);
        JLabel lblState = new JLabel("Trạng thái:");
        lblState.setFont(labelFont);

        JTextField valUsername = new JTextField(20);
        valUsername.setFont(valueFont);
        valUsername.setForeground(new Color(0x00796B));
        valUsername.setEditable(false);
        JPasswordField valPassword = new JPasswordField(20);
        valPassword.setFont(valueFont);
        valPassword.setForeground(new Color(0x00796B));
        valPassword.setEditable(false);
        valPassword.setEchoChar('*');  // Đảm bảo dấu sao hiển thị

        // Giả lập có 6 ký tự, nhưng không phải là thật
        valPassword.setText("******");
        valPassword.setForeground(new Color(0x00796B));
        valPassword.setEditable(false);
        JTextField valState = new JTextField(20);
        valState.setFont(valueFont);
        valState.setForeground(new Color(0x00796B));
        valState.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(40, 40, 40, 40);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblUsername, gbc);
        gbc.gridx = 1; panel.add(valUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblPassword, gbc);
        gbc.gridx = 1; panel.add(valPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblState, gbc);
        gbc.gridx = 1; panel.add(valState, gbc);

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(0x00796B));
        updateButton.setFocusPainted(false);
        updateButton.setPreferredSize(new Dimension(200, 45));
        updateButton.addActionListener(e -> {
            JPanel dialogPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            JPasswordField oldPass = new JPasswordField();
            JPasswordField newPass = new JPasswordField();
            JPasswordField confirmPass = new JPasswordField();
            dialogPanel.add(new JLabel("Mật khẩu cũ:"));
            dialogPanel.add(oldPass);
            dialogPanel.add(new JLabel("Mật khẩu mới:"));
            dialogPanel.add(newPass);
            dialogPanel.add(new JLabel("Xác nhận mật khẩu mới:"));
            dialogPanel.add(confirmPass);
            int result = JOptionPane.showConfirmDialog(panel, dialogPanel, "Đổi mật khẩu", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String oldPassword = new String(oldPass.getPassword());
                String newPassword = new String(newPass.getPassword());
                String confirmPassword = new String(confirmPass.getPassword());
                if (newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(panel, "Mật khẩu mới và xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT PASSWORD FROM USERS WHERE ID = ?")) {
                    ps.setString(1, pharmacistId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String currentPassword = rs.getString("PASSWORD");
                        if (!currentPassword.equals(oldPassword)) {
                            JOptionPane.showMessageDialog(panel, "Mật khẩu cũ không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Không tìm thấy tài khoản!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Lỗi kiểm tra mật khẩu cũ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                        "UPDATE USERS SET PASSWORD = ? WHERE ID = ?")) {
                    ps.setString(1, newPassword);
                    ps.setString(2, pharmacistId);
                    int rows = ps.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(panel, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Không có thông tin nào được cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(updateButton, gbc);

        // Load dữ liệu từ USERS
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT USERNAME, TRANGTHAI FROM USERS WHERE ID = ?")) {
            ps.setString(1, pharmacistId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valUsername.setText(rs.getString("USERNAME"));
                valState.setText(rs.getString("TRANGTHAI"));
            } else {
                valUsername.setText("-");
                valState.setText("-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            valUsername.setText("Lỗi DB");
            valState.setText("Lỗi DB");
        }

        return panel;
    }

    private void handleUserInfoUpdate(JTextField[] fields) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE USERS SET HOTENND = ?, SDT = ?, EMAIL = ?, GIOITINH = ?, NGAYSINH = ?, DIACHI = ? WHERE ID = ?")) {
            
            ps.setString(1, fields[1].getText());
            ps.setString(2, fields[2].getText());
            ps.setString(3, fields[3].getText());
            ps.setString(4, fields[4].getText());
            ps.setDate(5, Date.valueOf(fields[5].getText()));
            ps.setString(6, fields[6].getText());
            ps.setString(7, pharmacistId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không có thông tin nào được cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSpecialtyUpdate(JTextField maDS, JTextField chuyenMon, JTextField bangCap) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE DUOCSI SET CHUYENMON = ?, BANGCAP = ? WHERE MADS = ?")) {
            
            ps.setString(1, chuyenMon.getText());
            ps.setString(2, bangCap.getText());
            ps.setString(3, pharmacistId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật chuyên môn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không có thông tin nào được cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Thông tin Dược sĩ");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Tràn màn hình
            frame.add(new PharmacistView("U004", null));
            frame.setVisible(true);
        });
    }
}
