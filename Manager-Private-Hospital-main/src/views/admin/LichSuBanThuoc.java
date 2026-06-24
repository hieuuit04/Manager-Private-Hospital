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
import java.util.Vector;

public class LichSuBanThuoc extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate;
    private String mads; // Mã dược sĩ đăng nhập

    public LichSuBanThuoc(String mads) {
        this.mads = mads;
        setTitle("Lịch sử bán thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(43, 74, 160));
        JLabel title = new JLabel("LỊCH SỬ BÁN THUỐC", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Đúng thứ tự cột như DB
        String[] columns = {
            "MADT", "MADS", "MABS", "MABN", "GIOITINHBN", "NGAYSINHBN", "LICHSU_BENHLY_BN", "DIUNGBN",
            "FILEDONTHUOC", "GHICHU", "NGAYBAN", "THANHTIEN", "TRANGTHAITT"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);

        btnUpdate = new JButton("Cập nhật");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(43, 74, 160));
        btnUpdate.setForeground(Color.WHITE);

        btnUpdate.addActionListener(e -> openUpdateDialog());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnUpdate);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM DONTHUOC_DONTHUOCYC ORDER BY MADT DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        Object val = rs.getObject(i);
                        if (val instanceof java.sql.Date) {
                            row.add(val != null ? val.toString() : "");
                        } else if (val instanceof java.sql.Blob) {
                            row.add(val != null ? "[File]" : "");
                        } else {
                            row.add(val);
                        }
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }

private void openUpdateDialog() {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn thuốc để cập nhật!");
        return;
    }

    String madt = String.valueOf(tableModel.getValueAt(row, 0));
    String madsRow = String.valueOf(tableModel.getValueAt(row, 1));
    String mabs = String.valueOf(tableModel.getValueAt(row, 2));
    String ghichu = String.valueOf(tableModel.getValueAt(row, 9));
    String ngayban = String.valueOf(tableModel.getValueAt(row, 10));
    String thanhtien = String.valueOf(tableModel.getValueAt(row, 11));
    String trangthai = String.valueOf(tableModel.getValueAt(row, 12));

    // ComboBox MADS cho quản trị viên chọn lại
    JComboBox<String> cbMADS = new JComboBox<>();
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT MADS FROM DUOC_SI";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                cbMADS.addItem(rs.getString("MADS"));
            }
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách dược sĩ: " + ex.getMessage());
    }
    cbMADS.setSelectedItem(madsRow);

    JTextField tfMABS = new JTextField(mabs.equals("null") ? "" : mabs);
    JTextField tfGhiChu = new JTextField(ghichu.equals("null") ? "" : ghichu);
    JTextField tfNgayBan = new JTextField(ngayban.equals("null") ? "" : ngayban);
    JTextField tfThanhTien = new JTextField(thanhtien.equals("null") ? "" : thanhtien);
    String[] trangThaiArr = {"Chưa thanh toán", "Đã thanh toán"};
    JComboBox<String> cbTrangThai = new JComboBox<>(trangThaiArr);
    cbTrangThai.setSelectedItem(trangthai);

    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
    panel.add(new JLabel("Mã dược sĩ:"));
    panel.add(cbMADS);
    panel.add(new JLabel("Mã bác sĩ:"));
    panel.add(tfMABS);
    panel.add(new JLabel("Ghi chú:"));
    panel.add(tfGhiChu);
    panel.add(new JLabel("Ngày bán (yyyy-MM-dd):"));
    panel.add(tfNgayBan);
    panel.add(new JLabel("Thành tiền:"));
    panel.add(tfThanhTien);
    panel.add(new JLabel("Trạng thái:"));
    panel.add(cbTrangThai);

    JButton btnCapNhat = new JButton("Cập nhật");
    btnCapNhat.setBackground(new Color(43, 74, 160));
    btnCapNhat.setForeground(Color.WHITE);
    btnCapNhat.setFont(new Font("Segoe UI", Font.BOLD, 15));

    JButton btnXemChiTiet = new JButton("Xem chi tiết");
    btnXemChiTiet.setBackground(new Color(0, 123, 255));
    btnXemChiTiet.setForeground(Color.WHITE);
    btnXemChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 15));

    JDialog dialog = new JDialog(this, "Cập nhật đơn thuốc", true);
    dialog.setLayout(new BorderLayout());
    dialog.add(panel, BorderLayout.CENTER);

    JPanel btnPanel = new JPanel();
    btnPanel.add(btnCapNhat);
    btnPanel.add(btnXemChiTiet);
    dialog.add(btnPanel, BorderLayout.SOUTH);

    btnCapNhat.addActionListener(e -> {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE DONTHUOC_DONTHUOCYC SET MADS=?, MABS=?, GHICHU=?, NGAYBAN=?, THANHTIEN=?, TRANGTHAITT=? WHERE MADT=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, (String) cbMADS.getSelectedItem()); // Lấy từ comboBox, không dùng biến mads
                ps.setString(2, tfMABS.getText().trim());
                ps.setString(3, tfGhiChu.getText().trim());
                if (tfNgayBan.getText().trim().isEmpty()) {
                    ps.setNull(4, Types.DATE);
                } else {
                    ps.setDate(4, java.sql.Date.valueOf(tfNgayBan.getText().trim()));
                }
                if (tfThanhTien.getText().trim().isEmpty()) {
                    ps.setNull(5, Types.NUMERIC);
                } else {
                    ps.setDouble(5, Double.parseDouble(tfThanhTien.getText().trim()));
                }
                ps.setString(6, cbTrangThai.getSelectedItem().toString());
                ps.setString(7, madt);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                dialog.dispose();
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Lỗi khi cập nhật: " + ex.getMessage());
        }
    });

    btnXemChiTiet.addActionListener(e -> {
        dialog.dispose();
        new CTDT(madt).setVisible(true);
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

}