/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.admin;

import utils.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CTDT extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String madt;
    private JButton btnAdd, btnUpdate;

    public CTDT(String madt) {
        this.madt = madt;
        setTitle("Chi tiết đơn thuốc: " + madt);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(43, 74, 160));
        JLabel title = new JLabel("CHI TIẾT ĐƠN THUỐC: " + madt, SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"MADT", "MASP", "SOLUONG", "DONGIA", "THANHTIEN"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnAdd = new JButton("Thêm Chi tiết");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setBackground(new Color(43, 74, 160));
        btnAdd.setForeground(Color.WHITE);

        btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnUpdate.setBackground(new Color(0, 123, 255));
        btnUpdate.setForeground(Color.WHITE);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnUpdate);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> openAddDialog());
        btnUpdate.addActionListener(e -> openUpdateDialog());

        loadData();
    }

private void loadData() {
    tableModel.setRowCount(0);
    double tongThanhTien = 0;
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT MADT, MASP, SOLUONG, DONGIA, THANHTIEN FROM CTDT WHERE MADT = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, madt);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MADT"),
                    rs.getString("MASP"),
                    rs.getInt("SOLUONG"),
                    rs.getDouble("DONGIA"),
                    rs.getDouble("THANHTIEN")
                };
                tongThanhTien += rs.getDouble("THANHTIEN");
                tableModel.addRow(row);
            }
        }
        // Cập nhật THANHTIEN vào DONTHUOC_DONTHUOCYC
        String update = "UPDATE DONTHUOC_DONTHUOCYC SET THANHTIEN=? WHERE MADT=?";
        try (PreparedStatement psUpdate = conn.prepareStatement(update)) {
            psUpdate.setDouble(1, tongThanhTien);
            psUpdate.setString(2, madt);
            psUpdate.executeUpdate();
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết đơn thuốc: " + ex.getMessage());
    }
}

    private void openAddDialog() {
        JTextField tfMASP = new JTextField();
        JTextField tfSoLuong = new JTextField();
        JTextField tfDonGia = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Mã sản phẩm:"));
        panel.add(tfMASP);
        panel.add(new JLabel("Số lượng:"));
        panel.add(tfSoLuong);
        panel.add(new JLabel("Đơn giá:"));
        panel.add(tfDonGia);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm chi tiết đơn thuốc", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO CTDT (MADT, MASP, SOLUONG, DONGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, madt);
                    ps.setString(2, tfMASP.getText().trim());
                    int soLuong = Integer.parseInt(tfSoLuong.getText().trim());
                    double donGia = Double.parseDouble(tfDonGia.getText().trim());
                    ps.setInt(3, soLuong);
                    ps.setDouble(4, donGia);
                    ps.setDouble(5, soLuong * donGia);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Thêm chi tiết thành công!");
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm chi tiết: " + ex.getMessage());
            }
        }
    }

    private void openUpdateDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để cập nhật!");
            return;
        }

        String masp = String.valueOf(tableModel.getValueAt(row, 1));
        int soLuong = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
        double donGia = Double.parseDouble(tableModel.getValueAt(row, 3).toString());

        JTextField tfSoLuong = new JTextField(String.valueOf(soLuong));
        JTextField tfDonGia = new JTextField(String.valueOf(donGia));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Số lượng:"));
        panel.add(tfSoLuong);
        panel.add(new JLabel("Đơn giá:"));
        panel.add(tfDonGia);

        int result = JOptionPane.showConfirmDialog(this, panel, "Cập nhật chi tiết đơn thuốc", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE CTDT SET SOLUONG=?, DONGIA=?, THANHTIEN=? WHERE MADT=? AND MASP=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    int newSoLuong = Integer.parseInt(tfSoLuong.getText().trim());
                    double newDonGia = Double.parseDouble(tfDonGia.getText().trim());
                    ps.setInt(1, newSoLuong);
                    ps.setDouble(2, newDonGia);
                    ps.setDouble(3, newSoLuong * newDonGia);
                    ps.setString(4, madt);
                    ps.setString(5, masp);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
            }
        }
    }
}