/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import utils.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

public class PharmacistNotification extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String mads;

    public PharmacistNotification() {
        setTitle("Thông báo");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(43, 74, 160), getWidth(), getHeight(), new Color(30, 144, 255));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 100));
        headerPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("DANH SÁCH THÔNG BÁO", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Mã TB", "Tên bệnh nhân", "Nội dung", "Ngày", "Trạng thái", "Loại", "Mã BN"}, 0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setSelectionBackground(new Color(230, 240, 255));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(new Color(43, 74, 160));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : new Color(235, 245, 255));
                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) showNotificationDialog(row);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        loadNotifications();
    }

    private void loadNotifications() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT t.MATB, u.HOTENND, t.NOIDUNG, t.NGAYTHONGBAO, t.TRANGTHAI, t.LOAI, t.USER_ID " +
                    "FROM THONGBAO t JOIN USERS u ON t.USER_ID = u.ID " +
                    "WHERE t.LOAI = 'Mua thuốc' ORDER BY t.NGAYTHONGBAO DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            rs.getString("MATB"),
                            rs.getString("HOTENND"),
                            rs.getString("NOIDUNG"),
                            rs.getDate("NGAYTHONGBAO"),
                            rs.getString("TRANGTHAI"),
                            rs.getString("LOAI"),
                            rs.getString("USER_ID")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thông báo: " + ex.getMessage());
        }
    }

    private void showNotificationDialog(int row) {
        JDialog dialog = new JDialog(this, "Chi tiết thông báo", true);
        dialog.setSize(800, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                new EmptyBorder(20, 20, 20, 20)));

        JLabel lblTitle = new JLabel("THÔNG BÁO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(43, 74, 160));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(20));

        String[] fieldNames = {"Mã TB", "Tên bệnh nhân", "Nội dung", "Ngày", "Trạng thái", "Loại"};
        for (int i = 0; i < fieldNames.length; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.setOpaque(false);

            JLabel lbl = new JLabel(fieldNames[i] + ":");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            rowPanel.add(lbl);

            if (i == 2) {
                JTextArea txt = new JTextArea(String.valueOf(tableModel.getValueAt(row, i)));
                txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                txt.setLineWrap(true);
                txt.setWrapStyleWord(true);
                txt.setEditable(false);
                txt.setBackground(new Color(245, 250, 255));
                txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                txt.setPreferredSize(new Dimension(400, 60));
                rowPanel.add(txt);
            } else {
                JLabel val = new JLabel(String.valueOf(tableModel.getValueAt(row, i)));
                val.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                rowPanel.add(val);
            }
            panel.add(rowPanel);
            panel.add(Box.createVerticalStrut(10));
        }

        JButton btnDownload = createButton("Tải file đơn thuốc", new Color(0, 153, 102));
        btnDownload.addActionListener(e -> {
            String maBN = String.valueOf(tableModel.getValueAt(row, 6));
            String maTB = String.valueOf(tableModel.getValueAt(row, 0));
            boolean success = downloadPrescriptionFile(maBN, maTB, dialog);
            if (success) {
                dialog.dispose();
                loadNotifications();
            }
        });
        panel.add(btnDownload);
        panel.add(Box.createVerticalStrut(10));

        JButton btnClose = createButton("Đóng", new Color(200, 55, 55));
        btnClose.addActionListener(e -> dialog.dispose());
        panel.add(btnClose);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }
private boolean downloadPrescriptionFile(String maBN, String maTB, Component parent) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT MADT, FILEDONTHUOC FROM DONTHUOC_DONTHUOCYC WHERE MABN = ? ORDER BY MADT DESC FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBN);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String madt = rs.getString("MADT");
                InputStream is = rs.getBinaryStream("FILEDONTHUOC");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn nơi lưu file đơn thuốc");
                fileChooser.setSelectedFile(new File("don_thuoc.jpg"));
                int userSelection = fileChooser.showSaveDialog(parent);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File saveFile = fileChooser.getSelectedFile();
                    try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        JOptionPane.showMessageDialog(parent, "Tải file thành công!");

                        // Cập nhật trạng thái của THONGBAO
                        String updateTB = "UPDATE THONGBAO SET TRANGTHAI = 'Đã đọc' WHERE MATB = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateTB)) {
                            psUpdate.setString(1, maTB);
                            psUpdate.executeUpdate();
                        }

                        // Cập nhật MADS vào DONTHUOC_DONTHUOCYC
                        if (mads == null || mads.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(parent, "Mã dược sĩ (MADS) không hợp lệ!");
                        } else {
                            String updateDT = "UPDATE DONTHUOC_DONTHUOCYC SET MADS = ? WHERE MABN = ?";
                            try (PreparedStatement psUpdate2 = conn.prepareStatement(updateDT)) {
                                psUpdate2.setString(1, mads);
                                psUpdate2.setString(2, maBN);
                                int affected = psUpdate2.executeUpdate();
                                if (affected == 0) {
                                    JOptionPane.showMessageDialog(parent, "Không cập nhật được MADS! MABN không tồn tại.");
                                }
                            }
                        }
                        return true;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Không tìm thấy file đơn thuốc!");
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(parent, "Lỗi khi tải file: " + ex.getMessage());
    }
    return false;
}

    // Test nhanh
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PharmacistNotification().setVisible(true));
    }
}



