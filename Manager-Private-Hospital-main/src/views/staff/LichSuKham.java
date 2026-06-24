/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import views.patient.Prescription;
import views.patient.ProductView;
import utils.DBConnection;

public class LichSuKham extends JFrame {

    private JTable historyTable;
    private JTextField tfMaKham, tfMaLich, tfMaBS, tfMaDT, tfNgayKham, tfNgayTaiKham, tfMaBN;
    private DefaultTableModel model;

    public LichSuKham() throws SQLException, ClassNotFoundException {

        setTitle("📄 Danh sách lịch sử khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình

        initComponents();
        loadHistory();
    }

    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0x80B9AD));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ===== TẠO PANEL CHỨA TIÊU ĐỀ VÀ THANH TÌM KIẾM =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // TITLE
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("LỊCH SỬ KHÁM BỆNH", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0x538392));
        title.setBackground(new Color(0xd6eaed));
        title.setOpaque(true);
        titlePanel.add(title, BorderLayout.CENTER);

        topPanel.add(titlePanel, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(0x80B9AD));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== PANEL CHỨA NÚT Ở GIỮA =====
        JPanel nameSearchPanel = new JPanel(new BorderLayout());
        nameSearchPanel.setBackground(new Color(0x80B9AD));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setBackground(new Color(0x538392));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(300, 30));

        JButton btnMed = new JButton("Đơn thuốc");
        btnMed.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnMed.setBackground(new Color(0xF6F8D5));
        btnMed.setForeground(new Color(0x538392));
        btnMed.setPreferredSize(new Dimension(150, 30));

        // Panel chứa nút tìm kiếm ở giữa
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(btnSearch);

        // Panel chứa nút đơn thuốc bên phải
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        rightPanel.add(btnMed);

        // Hover effect
        btnSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSearch.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSearch.setBackground(new Color(0x538392));
            }
        });

        // Hover effect
        btnMed.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnMed.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnMed.setBackground(new Color(0xF6F8D5));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String maBenhNhan = JOptionPane.showInputDialog(
                            null,
                            "Nhập mã bệnh nhân cần xem đơn thuốc:",
                            "Xem đơn thuốc",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (maBenhNhan != null && !maBenhNhan.trim().isEmpty()) {
                        // Xử lý mã bệnh nhân, ví dụ:
                        System.out.println("Mã bệnh nhân đã nhập: " + maBenhNhan);
                        new Prescription(maBenhNhan).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Bạn chưa nhập mã bệnh nhân!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(LichSuKham.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LichSuKham.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Thêm vào nameSearchPanel
        nameSearchPanel.add(centerPanel, BorderLayout.CENTER);
        nameSearchPanel.add(rightPanel, BorderLayout.EAST);

        searchPanel.add(nameSearchPanel, BorderLayout.NORTH);

        // ========== CÁC THÀNH PHẦN KHÁC ==========
        JPanel searchFieldsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        searchFieldsPanel.setBackground(new Color(0x80B9AD));

        tfMaKham = new JTextField();
        tfMaLich = new JTextField();
        tfMaBS = new JTextField();
        tfMaDT = new JTextField();
        tfNgayKham = new JTextField();
        tfNgayTaiKham = new JTextField();
        tfMaBN = new JTextField();

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.WHITE;

        searchFieldsPanel.add(createLabeledField("Mã khám:", tfMaKham, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã lịch hẹn:", tfMaLich, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã bác sĩ:", tfMaBS, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã đơn thuốc:", tfMaDT, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày khám:", tfNgayKham, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày tái khám:", tfNgayTaiKham, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã bệnh nhân:", tfMaBN, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE: DANH SÁCH LỊCH SỬ KHÁM BỆNH =====
        String[] columns = {
            "Mã khám", "Mã lịch hẹn", "Mã bác sĩ", "Mã bệnh nhân", "Mã đơn thuốc",
            "Ngày khám", "Ngày tái khám", "Kết luận", "Lưu ý"};
        model = new DefaultTableModel(columns, 0);
        historyTable = new JTable(model);
        historyTable.setRowHeight(28); //chiều cao mỗi hàng 
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        historyTable.getTableHeader().setBackground(new Color(0x80B9AD));
        historyTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(0x80B9AD)));

        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                loadHistory(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm lịch sử khám bệnh.");
            }
        });

        // ===== CLICK XEM CHI TIẾT =====
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = historyTable.getSelectedRow();
                if (selectedRow != -1) {
                    showExamDetails(selectedRow);
                }
            }
        });
    }

    private void loadHistory() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM KHAM WHERE 1=1 ");

        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfMaKham.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MAKHAM) LIKE ?");
            params.add("%" + tfMaKham.getText().trim().toUpperCase() + "%");
        }
        if (!tfMaLich.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MALICH) LIKE ?");
            params.add("%" + tfMaLich.getText().trim().toUpperCase() + "%");
        }
        if (!tfMaBS.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MABS) LIKE ?");
            params.add("%" + tfMaBS.getText().trim().toUpperCase() + "%");
        }
        if (!tfMaDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MADT) LIKE ?");
            params.add("%" + tfMaDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgayKham.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYKHAM, 'YYYY-MM-DD') = ?");
            params.add(tfNgayKham.getText().trim());
        }
        if (!tfNgayTaiKham.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYTAIKHAM, 'YYYY-MM-DD') = ?");
            params.add(tfNgayTaiKham.getText().trim());
        }
        if (!tfMaBN.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MABN) LIKE ?");
            params.add("%" + tfMaBN.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MAKHAM"),
                    rs.getString("MALICH"),
                    rs.getString("MABS"),
                    rs.getString("MABN"),
                    rs.getString("MADT"),
                    rs.getDate("NGAYKHAM"),
                    rs.getDate("NGAYTAIKHAM"),
                    rs.getString("KETLUAN"),
                    rs.getString("LUUY"),});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void showExamDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết lịch sử khám bệnh", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < model.getColumnCount(); i++) {
            sb.append(model.getColumnName(i)).append(": ")
                    .append(model.getValueAt(row, i)).append("\n");
        }

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(Color.WHITE);
        textPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));;

        StyledDocument doc = textPane.getStyledDocument();

        // Style in đậm cho tiêu đề cột
        SimpleAttributeSet boldAttr = new SimpleAttributeSet();
        StyleConstants.setBold(boldAttr, true);

        // Style bình thường cho giá trị
        SimpleAttributeSet normalAttr = new SimpleAttributeSet();
        StyleConstants.setBold(normalAttr, false);

        for (int i = 0; i < model.getColumnCount(); i++) {
            try {
                doc.insertString(doc.getLength(), model.getColumnName(i) + ": ", boldAttr);  // In đậm tên cột
                doc.insertString(doc.getLength(), model.getValueAt(row, i) + "\n", normalAttr); // Giá trị bình thường
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        textPane.setCaretPosition(0); // Đặt con trỏ về đầu => không cuộn xuống cuối

        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(0x2B4A59));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xd9eef2)); // Nền pannel button cùng màu nền hộp thoại
        buttonPanel.add(closeButton);

        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}
