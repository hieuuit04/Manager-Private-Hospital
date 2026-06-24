/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

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
import utils.DBConnection;

public class ProductView extends JFrame {
    private JTable productTable;
    private JTextField tfMaSP, tfTenSP, tfNhaPP, tfThanhPhan, tfNSX, tfHSD, tfDonGia, tfUuDai, tfMoTa;
    private DefaultTableModel model;

    public ProductView() throws SQLException, ClassNotFoundException {
        setTitle("📄 Danh sách sản phẩm");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
     
        initComponents();
        loadProducts();
    }
    
    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0x588EA7));

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
        JLabel title = new JLabel("DANH SÁCH SẢN PHẨM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0x588EA7));
        title.setBackground(new Color(0xd6eaed));
        title.setOpaque(true);
        titlePanel.add(title, BorderLayout.CENTER);

        topPanel.add(titlePanel, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(new Color(0x588EA7));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ========== TÊN SP & NÚT ==========
        JPanel nameSearchPanel = new JPanel(new BorderLayout(10, 10));
        nameSearchPanel.setBackground(new Color(0x588EA7));

        JLabel lblTenSP = new JLabel("Nhập tên sản phẩm:");
        lblTenSP.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTenSP.setForeground(Color.WHITE);
        tfTenSP = new JTextField();
        tfTenSP.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setBackground(new Color(0x2B4A59));
        btnSearch.setForeground(Color.WHITE);

        // Hover effect
        btnSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSearch.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSearch.setBackground(new Color(0x2B4A59));
            }
        });

        nameSearchPanel.add(lblTenSP, BorderLayout.WEST);
        nameSearchPanel.add(tfTenSP, BorderLayout.CENTER);
        nameSearchPanel.add(btnSearch, BorderLayout.EAST);
        
        searchPanel.add(nameSearchPanel, BorderLayout.NORTH);

        // ========== CÁC THÀNH PHẦN KHÁC ==========
        JPanel searchFieldsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        searchFieldsPanel.setBackground(new Color(0x588EA7));

        tfMaSP = new JTextField();
        tfNhaPP = new JTextField();
        tfThanhPhan = new JTextField();
        tfNSX = new JTextField();
        tfHSD = new JTextField();
        tfDonGia = new JTextField();
        tfUuDai = new JTextField();
        tfMoTa = new JTextField();

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.WHITE;

        searchFieldsPanel.add(createLabeledField("Mã sản phẩm:", tfMaSP, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Nhà phân phối:", tfNhaPP, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Thành phần:", tfThanhPhan, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("NSX:", tfNSX, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("HSD:", tfHSD, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Đơn giá:", tfDonGia, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ưu đãi:", tfUuDai, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mô tả:", tfMoTa, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        // ===== TABLE: DANH SÁCH SẢN PHẨM =====
        String[] columns = {
            "Mã sản phẩm", "Tên sản phẩm", "Nhà phân phối", "ĐVT", "Thành phần", "Lưu ý",
            "Cách dùng", "Bảo quản", "NSX", "HSD", "Đơn giá", "Ưu đãi", "Mô tả"
        };
        model = new DefaultTableModel(columns, 0);
        productTable = new JTable(model);
        productTable.setRowHeight(28); //chiều cao mỗi hàng 
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        productTable.getTableHeader().setBackground(new Color(0x588EA7));
        productTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(0x588EA7)));
        
        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                loadProducts(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sản phẩm.");
            }
        });


        // ===== CLICK XEM CHI TIẾT =====
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    showProductDetails(selectedRow);
                }
            }
        });
    }

    private void loadProducts() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM SANPHAM WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfMaSP.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MASP) LIKE ?");
            params.add("%" + tfMaSP.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenSP.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TENSP) LIKE ?");
            params.add("%" + tfTenSP.getText().trim().toUpperCase() + "%");
        }
        if (!tfNhaPP.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TENNPP) LIKE ?");
            params.add("%" + tfNhaPP.getText().trim().toUpperCase() + "%");
        }
        if (!tfThanhPhan.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(THANHPHAN) LIKE ?");
            params.add("%" + tfThanhPhan.getText().trim().toUpperCase() + "%");
        }
        if (!tfMoTa.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MOTA) LIKE ?");
            params.add("%" + tfMoTa.getText().trim().toUpperCase() + "%");
        }
        if (!tfDonGia.getText().trim().isEmpty()) {
            sql.append(" AND DONGIA = ?");
            params.add(Double.parseDouble(tfDonGia.getText().trim()));
        }
        if (!tfUuDai.getText().trim().isEmpty()) {
            sql.append(" AND UUDAI = ?");
            params.add(Integer.parseInt(tfUuDai.getText().trim()));
        }
        if (!tfNSX.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NSX, 'YYYY-MM-DD') = ?");
            params.add(tfNSX.getText().trim());
        }
        if (!tfHSD.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(HSD, 'YYYY-MM-DD') = ?");
            params.add(tfHSD.getText().trim());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MASP"),
                    rs.getString("TENSP"),
                    rs.getString("TENNPP"),
                    rs.getString("DVT"),
                    rs.getString("THANHPHAN"),
                    rs.getString("LUUY"),
                    rs.getString("CACHDUNG"),
                    rs.getString("BAOQUAN"),
                    rs.getDate("NSX"),
                    rs.getDate("HSD"),
                    rs.getDouble("DONGIA"),
                    rs.getInt("UUDAI"),
                    rs.getString("MOTA")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    
    private void showProductDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết sản phẩm", true);
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ProductView().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}



