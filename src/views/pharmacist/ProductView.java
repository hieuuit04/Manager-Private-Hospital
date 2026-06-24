/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

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
    private JButton btnEdit, btnDelete;

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
        
    // ===== NÚT SỬA/XÓA =====
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 15));
    
    // Thêm nút "Thêm"
    JButton btnAdd = new JButton("Thêm");
    btnAdd.setBackground(new Color(0x2E7D32)); // Màu xanh lá
    btnAdd.setForeground(Color.WHITE);
    btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
    btnAdd.setFocusPainted(false);
    
    btnEdit = new JButton("Sửa");
    btnEdit.setBackground(new Color(0x2B4A59));
    btnEdit.setForeground(Color.WHITE);
    btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 15));
    btnEdit.setFocusPainted(false);

    btnDelete = new JButton("Xóa");
    btnDelete.setBackground(new Color(0xB71C1C));
    btnDelete.setForeground(Color.WHITE);
    btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 15));
    btnDelete.setFocusPainted(false);

    buttonPanel.add(btnAdd);
    buttonPanel.add(btnEdit);
    buttonPanel.add(btnDelete);
    add(buttonPanel, BorderLayout.SOUTH);

    // ===== SỰ KIỆN THÊM =====
    btnAdd.addActionListener(e -> addNewProduct());

    // ===== SỰ KIỆN SỬA =====
    btnEdit.addActionListener(e -> editSelectedProduct());

    // ===== SỰ KIỆN XÓA =====
    btnDelete.addActionListener(e -> deleteSelectedProduct());
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

// Hàm sửa sản phẩm
private void editSelectedProduct() {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa.");
        return;
    }

    String maSP = (String) model.getValueAt(selectedRow, 0);

    // Lấy dữ liệu hiện tại từ DB
    try (Connection conn = DBConnection.getConnection()) {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT sp.*, ck.MAKHO, ck.SLTONKHO, ck.LOAISP, ck.TINHTRANGSP " +
            "FROM SANPHAM sp LEFT JOIN CHITIETKHO ck ON sp.MASP = ck.MAVATTU WHERE sp.MASP = ?"
        );
        ps.setString(1, maSP);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Các field sản phẩm
            JTextField tfTenSP = new JTextField(rs.getString("TENSP"));
            JTextField tfNhaPP = new JTextField(rs.getString("TENNPP"));
            JTextField tfDVT = new JTextField(rs.getString("DVT"));
            JTextField tfThanhPhan = new JTextField(rs.getString("THANHPHAN"));
            JTextField tfLuuY = new JTextField(rs.getString("LUUY"));
            JTextField tfCachDung = new JTextField(rs.getString("CACHDUNG"));
            JTextField tfBaoQuan = new JTextField(rs.getString("BAOQUAN"));
            JTextField tfNSX = new JTextField(rs.getString("NSX") != null ? rs.getDate("NSX").toString() : "");
            JTextField tfHSD = new JTextField(rs.getString("HSD") != null ? rs.getDate("HSD").toString() : "");
            JTextField tfDonGia = new JTextField(String.valueOf(rs.getDouble("DONGIA")));
            JTextField tfUuDai = new JTextField(String.valueOf(rs.getInt("UUDAI")));
            JTextField tfMoTa = new JTextField(rs.getString("MOTA"));

            // Các field chi tiết kho
            JComboBox<String> cbMaKho = new JComboBox<>();
            loadMaKhoToComboBox(cbMaKho);
            cbMaKho.setSelectedItem(rs.getString("MAKHO"));

            JTextField tfSLTonKho = new JTextField(String.valueOf(rs.getInt("SLTONKHO")));
            JTextField tfLoaiSP = new JTextField(rs.getString("LOAISP"));
            JComboBox<String> cbTinhTrangSP = new JComboBox<>(new String[] { "Còn hạn", "Sắp hết hạn" });
            cbTinhTrangSP.setSelectedItem(rs.getString("TINHTRANGSP"));

            JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
            panel.add(new JLabel("Tên sản phẩm:")); panel.add(tfTenSP);
            panel.add(new JLabel("Nhà phân phối:")); panel.add(tfNhaPP);
            panel.add(new JLabel("ĐVT:")); panel.add(tfDVT);
            panel.add(new JLabel("Thành phần:")); panel.add(tfThanhPhan);
            panel.add(new JLabel("Lưu ý:")); panel.add(tfLuuY);
            panel.add(new JLabel("Cách dùng:")); panel.add(tfCachDung);
            panel.add(new JLabel("Bảo quản:")); panel.add(tfBaoQuan);
            panel.add(new JLabel("NSX (yyyy-mm-dd):")); panel.add(tfNSX);
            panel.add(new JLabel("HSD (yyyy-mm-dd):")); panel.add(tfHSD);
            panel.add(new JLabel("Đơn giá:")); panel.add(tfDonGia);
            panel.add(new JLabel("Ưu đãi:")); panel.add(tfUuDai);
            panel.add(new JLabel("Mô tả:")); panel.add(tfMoTa);
            panel.add(new JLabel("Mã kho:")); panel.add(cbMaKho);
            panel.add(new JLabel("Số lượng tồn kho:")); panel.add(tfSLTonKho);
            panel.add(new JLabel("Loại sản phẩm:")); panel.add(tfLoaiSP);
            panel.add(new JLabel("Tình trạng sản phẩm:")); panel.add(cbTinhTrangSP);

            int result = JOptionPane.showConfirmDialog(this, panel, "Sửa thông tin sản phẩm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    conn.setAutoCommit(false);

                    // Cập nhật bảng SANPHAM
                    PreparedStatement updateSP = conn.prepareStatement(
                        "UPDATE SANPHAM SET TENSP=?, TENNPP=?, DVT=?, THANHPHAN=?, LUUY=?, CACHDUNG=?, BAOQUAN=?, NSX=?, HSD=?, DONGIA=?, UUDAI=?, MOTA=? WHERE MASP=?"
                    );
                    updateSP.setString(1, tfTenSP.getText());
                    updateSP.setString(2, tfNhaPP.getText());
                    updateSP.setString(3, tfDVT.getText());
                    updateSP.setString(4, tfThanhPhan.getText());
                    updateSP.setString(5, tfLuuY.getText());
                    updateSP.setString(6, tfCachDung.getText());
                    updateSP.setString(7, tfBaoQuan.getText());
                    updateSP.setDate(8, tfNSX.getText().isEmpty() ? null : java.sql.Date.valueOf(tfNSX.getText()));
                    updateSP.setDate(9, tfHSD.getText().isEmpty() ? null : java.sql.Date.valueOf(tfHSD.getText()));
                    updateSP.setDouble(10, Double.parseDouble(tfDonGia.getText()));
                    updateSP.setInt(11, Integer.parseInt(tfUuDai.getText()));
                    updateSP.setString(12, tfMoTa.getText());
                    updateSP.setString(13, maSP);
                    updateSP.executeUpdate();

                    // Cập nhật bảng CHITIETKHO
                    PreparedStatement updateKho = conn.prepareStatement(
                        "UPDATE CHITIETKHO SET MAKHO=?, SLTONKHO=?, LOAISP=?, TINHTRANGSP=? WHERE MAVATTU=?"
                    );
                    updateKho.setString(1, cbMaKho.getSelectedItem().toString());
                    updateKho.setInt(2, Integer.parseInt(tfSLTonKho.getText()));
                    updateKho.setString(3, tfLoaiSP.getText());
                    updateKho.setString(4, cbTinhTrangSP.getSelectedItem().toString());
                    updateKho.setString(5, maSP);
                    updateKho.executeUpdate();

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
                    loadProducts();
                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm.");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi truy xuất dữ liệu: " + ex.getMessage());
    }
}

// Hàm xóa sản phẩm
private void deleteSelectedProduct() {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.");
        return;
    }

    String maSP = (String) model.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this, 
        "Bạn có chắc chắn muốn xóa sản phẩm này?", 
        "Xác nhận xóa", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Xóa trong CHITIETKHO trước
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM CHITIETKHO WHERE MAVATTU = ?")) {
                ps1.setString(1, maSP);
                ps1.executeUpdate();
            }

            // Sau đó xóa trong SANPHAM
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM SANPHAM WHERE MASP = ?")) {
                ps2.setString(1, maSP);
                ps2.executeUpdate();
            }

            conn.commit(); // Commit nếu mọi thứ thành công
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm và chi tiết kho thành công!");
            loadProducts();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
        }
    }
}

// Hàm thêm sản phẩm mới
private void addNewProduct() {
    // Tạo form thêm mới
    JTextField tfMaSP = new JTextField();
    JTextField tfTenSP = new JTextField();
    JTextField tfNhaPP = new JTextField();
    JTextField tfDVT = new JTextField();
    JTextField tfThanhPhan = new JTextField();
    JTextField tfLuuY = new JTextField();
    JTextField tfCachDung = new JTextField();
    JTextField tfBaoQuan = new JTextField();
    JTextField tfNSX = new JTextField();
    JTextField tfHSD = new JTextField();
    JTextField tfDonGia = new JTextField();
    JTextField tfUuDai = new JTextField();
    JTextField tfMoTa = new JTextField();
    // Các trường chi tiết kho
    JComboBox<String> cbMaKho = new JComboBox<>();
    loadMaKhoToComboBox(cbMaKho);  // Load mã kho từ DB
   
    JTextField tfSLTonKho = new JTextField();
    JTextField tfLoaiSP = new JTextField();
    
    JTextField tfTinhTrangSP = new JTextField();
    
    // Tình trạng sản phẩm (ComboBox cố định)
    JComboBox<String> cbTinhTrangSP = new JComboBox<>(new String[] {"Còn hạn", "Sắp hết hạn"});

    // Tạo panel giao diện
    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
    panel.add(new JLabel("Mã sản phẩm:")); panel.add(tfMaSP);
    panel.add(new JLabel("Tên sản phẩm:")); panel.add(tfTenSP);
    panel.add(new JLabel("Nhà phân phối:")); panel.add(tfNhaPP);
    panel.add(new JLabel("ĐVT:")); panel.add(tfDVT);
    panel.add(new JLabel("Thành phần:")); panel.add(tfThanhPhan);
    panel.add(new JLabel("Lưu ý:")); panel.add(tfLuuY);
    panel.add(new JLabel("Cách dùng:")); panel.add(tfCachDung);
    panel.add(new JLabel("Bảo quản:")); panel.add(tfBaoQuan);
    panel.add(new JLabel("NSX (yyyy-mm-dd):")); panel.add(tfNSX);
    panel.add(new JLabel("HSD (yyyy-mm-dd):")); panel.add(tfHSD);
    panel.add(new JLabel("Đơn giá:")); panel.add(tfDonGia);
    panel.add(new JLabel("Ưu đãi:")); panel.add(tfUuDai);
    panel.add(new JLabel("Mô tả:")); panel.add(tfMoTa);
    panel.add(new JLabel("Mã kho:")); panel.add(cbMaKho);
    panel.add(new JLabel("Số lượng tồn kho:")); panel.add(tfSLTonKho);
    panel.add(new JLabel("Loại sản phẩm:")); panel.add(tfLoaiSP);
    panel.add(new JLabel("Tình trạng sản phẩm:")); panel.add(cbTinhTrangSP);

    int result = JOptionPane.showConfirmDialog(this, panel, "Thêm sản phẩm mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Giao dịch

            // Thêm vào bảng SANPHAM
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO SANPHAM (MASP, TENSP, TENNPP, DVT, THANHPHAN, LUUY, CACHDUNG, BAOQUAN, NSX, HSD, DONGIA, UUDAI, MOTA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, tfMaSP.getText());
                ps.setString(2, tfTenSP.getText());
                ps.setString(3, tfNhaPP.getText());
                ps.setString(4, tfDVT.getText());
                ps.setString(5, tfThanhPhan.getText());
                ps.setString(6, tfLuuY.getText());
                ps.setString(7, tfCachDung.getText());
                ps.setString(8, tfBaoQuan.getText());
                ps.setDate(9, tfNSX.getText().isEmpty() ? null : java.sql.Date.valueOf(tfNSX.getText()));
                ps.setDate(10, tfHSD.getText().isEmpty() ? null : java.sql.Date.valueOf(tfHSD.getText()));
                ps.setDouble(11, Double.parseDouble(tfDonGia.getText()));
                ps.setInt(12, Integer.parseInt(tfUuDai.getText()));
                ps.setString(13, tfMoTa.getText());

                ps.executeUpdate();
            }

            // Thêm vào bảng CHITIETKHO
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "INSERT INTO CHITIETKHO (MAVATTU, MAKHO, SLTONKHO, LOAISP, TINHTRANGSP, LOAIVATTU) VALUES (?, ?, ?, ?, ?, 'SP')")) {

                ps2.setString(1, tfMaSP.getText()); // MAVATTU trùng MASP
                ps2.setString(2, cbMaKho.getSelectedItem().toString());
                ps2.setInt(3, Integer.parseInt(tfSLTonKho.getText()));
                ps2.setString(4, tfLoaiSP.getText());
                ps2.setString(5, cbTinhTrangSP.getSelectedItem().toString());

                ps2.executeUpdate();
            }

            conn.commit(); // Xác nhận giao dịch
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm và chi tiết kho thành công!");
            loadProducts();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
        }
    }
}


private void loadMaKhoToComboBox(JComboBox<String> comboBox) {
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT MAKHO FROM KHO")) {

        while (rs.next()) {
            comboBox.addItem(rs.getString("MAKHO"));
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi tải mã kho: " + ex.getMessage());
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



