/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.sql.Statement;

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;


public class HoaDon extends JFrame {
    private JTextField txtSearch, txtMaHD, txtTongTien;
    private JComboBox<String> cbTrangThai;
    private JTable tableThuoc, tableHDKham;
    private String staffId;
    private JPanel hoaDonPanel; 


    public HoaDon(String staffId) throws SQLException, ClassNotFoundException {
        this.staffId = staffId;
        
        setTitle("DANH SÁCH HÓA ĐƠN");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        //Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        
        hoaDonPanel = initComponents();
        add(hoaDonPanel); 
    }
    
    private void filterTable(JTable table, String keyword, String maHD, double tongTien, String trangThai) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!keyword.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword)));
        }
        if (!maHD.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + Pattern.quote(maHD)));
        }
        if (tongTien >= 0) {
            filters.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, tongTien));
        }
        if (!trangThai.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter("(?i)" + trangThai));
        }

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    
    private void filterData() throws SQLException, ClassNotFoundException {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String maHD = txtMaHD.getText().trim().toLowerCase();
        String tongTienStr = txtTongTien.getText().trim();
        String trangThai = cbTrangThai.getSelectedItem().toString();

        double tongTien = -1;
        try {
            if (!tongTienStr.isEmpty()) {
                tongTien = Double.parseDouble(tongTienStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tổng tiền phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        filterTable(tableThuoc, keyword, maHD, tongTien, trangThai);
        filterTable(tableHDKham, keyword, maHD, tongTien, trangThai);
    }

    
    public JPanel initComponents() throws SQLException, ClassNotFoundException {
        JPanel panel = new JPanel(new BorderLayout());
        
        // ===== TOP PANEL (Tiêu đề + tìm kiếm) =====
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Tiêu đề
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("DANH SÁCH HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitle.setForeground(new Color(0x588EA7));
        lblTitle.setBackground(new Color(0xd6eaed));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        lblTitle.setOpaque(true);
        titlePanel.add(lblTitle, BorderLayout.CENTER);
        
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtSearch = new JTextField(12);
        txtMaHD = new JTextField(10);
        txtTongTien = new JTextField(10);
        cbTrangThai = new JComboBox<>(new String[]{"Tất cả", "Đã thanh toán", "Chưa thanh toán"});
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(0x78a2a7));
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        //Hover
        btnSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSearch.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSearch.setBackground(new Color(0x78a2a7));
            }
        });
        
        btnSearch.addActionListener(e -> {
            try {
                filterData();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; 
        JLabel lbWord = new JLabel("Từ khóa:");
        lbWord.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lbWord, gbc);
        gbc.gridx = 1; 
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        searchPanel.add(txtSearch, gbc);

        gbc.gridx = 2; 
        JLabel lbMaHD = new JLabel("Mã hóa đơn:");
        lbMaHD.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lbMaHD, gbc);
        txtMaHD.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        gbc.gridx = 3; searchPanel.add(txtMaHD, gbc);

        gbc.gridx = 4; 
        JLabel lbMoney = new JLabel("Tổng tiền:");
        lbMoney.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lbMoney, gbc);
        txtTongTien.setFont(new Font("Segoe UI", Font.PLAIN, 14)); 
        gbc.gridx = 5; searchPanel.add(txtTongTien, gbc);

        gbc.gridx = 6; 
        JLabel lbState = new JLabel("Trạng thái:");
        lbState.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lbState, gbc);
        gbc.gridx = 7; 
        cbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTrangThai.setBackground(Color.WHITE);
        searchPanel.add(cbTrangThai, gbc);

        gbc.gridx = 8;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(btnSearch, gbc);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH); 


        // ===== BOTTOM PANEL (2 bảng) =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(0xd6eaed));
        panel.add(mainPanel, BorderLayout.CENTER);

        // Các bảng
        mainPanel.add(createTableSection("Hóa đơn thuốc", tableThuoc = new JTable()));
        mainPanel.add(createTableSection("Hóa đơn khám bệnh", tableHDKham = new JTable()));
        
        loadThuocTableData();    
        loadHoaDonKBTableData();       
        
        return panel;
    }

    private JPanel createTableSection(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0xd6eaed)); // Giữ đồng nhất với mainPanel nếu cần

        // Tiêu đề (JLabel thay cho TitledBorder)
        JLabel lblSectionTitle = new JLabel(title);
        lblSectionTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblSectionTitle.setForeground(Color.BLACK);
        lblSectionTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // khoảng cách
        panel.add(lblSectionTitle, BorderLayout.NORTH);
        
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(new Color(0xCDE8E5));
        
        //Xem chi tiết bảng
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        showBillDetails(table, selectedRow);
                }
                }
            }
        });

        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        return panel;
    }

    public void loadThuocTableData() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) tableThuoc.getModel();
        model.setRowCount(0); // Clear table

        model.setColumnIdentifiers(new Object[] {
            "Mã đơn thuốc", "Tên dược sĩ", "Tên bác sĩ", "Tên bệnh nhân", "Giới tính bệnh nhân", "Ngày sinh bệnh nhân", "Lịch sử bệnh lý bệnh nhân",
            "Dị ứng bệnh nhân", "File Đơn Thuốc", "Ghi chú", "Ngày bán", "Thành tiền", "Trạng thái thanh toán"
        });

        tableThuoc.getColumnModel().getColumn(12).setCellRenderer(new StatusCellRenderer());

        String sql = "SELECT MADT, U1.HOTENND, U2.HOTENND AS HOTENND1, U3.HOTENND AS HOTENND2, GIOITINHBN, NGAYSINHBN, LICHSU_BENHLY_BN, DIUNGBN, "
                   + "FILEDONTHUOC, GHICHU, NGAYBAN, THANHTIEN, TRANGTHAITT "
                   + "FROM DONTHUOC_DONTHUOCYC DT JOIN USERS U1 ON DT.MADS = U1.ID "
                   + "JOIN USERS U2 ON DT.MABS = U2.ID "
                   + "JOIN USERS U3 ON DT.MABN = U3.ID"
                ;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADT"),
                        rs.getString("HOTENND"),
                        rs.getString("HOTENND1"),
                        rs.getString("HOTENND2"),
                        rs.getString("GIOITINHBN"),
                        rs.getString("NGAYSINHBN"),
                        rs.getString("LICHSU_BENHLY_BN"),
                        rs.getString("DIUNGBN"),
                        rs.getBlob("FILEDONTHUOC"),
                        rs.getString("GHICHU"),
                        rs.getString("NGAYBAN"),
                        rs.getString("THANHTIEN"),
                        rs.getString("TRANGTHAITT")
                    };
                    model.addRow(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu đơn thuốc: " + e.getMessage());
        }
    }

    
    public void loadHoaDonKBTableData() throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) tableHDKham.getModel();
        model.setRowCount(0); // Clear table

        // Set column headers
        model.setColumnIdentifiers(new Object[] {
            "Mã hóa đơn khám bệnh", "Mã khám", "Ngày lập", "Tổng tiền",
            "Phương thức thanh toán", "Ghi chú", "Trạng thái thanh toán"
        });
        
        tableHDKham.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        String sql = "SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM ";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MAHDKB"),
                        rs.getString("MAKHAM"),
                        rs.getDate("NGAYLAP"),
                        rs.getDouble("TONGTIEN"),
                        rs.getString("PHUONGTHUCTT"),
                        rs.getString("GHICHU"),
                        rs.getString("TRANGTHAITT")
                    };
                    model.addRow(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void showBillDetails(JTable table, int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(0xd9eef2));

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(Color.WHITE);
        textPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        StyledDocument doc = textPane.getStyledDocument();

        SimpleAttributeSet boldAttr = new SimpleAttributeSet();
        StyleConstants.setBold(boldAttr, true);
        SimpleAttributeSet normalAttr = new SimpleAttributeSet();

        // Hiển thị thông tin cơ bản
        for (int i = 0; i < table.getColumnCount(); i++) {
            String colName = table.getColumnName(i);
            Object valueObj = table.getValueAt(row, i);
            String value = (valueObj != null) ? valueObj.toString() : "Không có dữ liệu";
            try {
                doc.insertString(doc.getLength(), colName + ": ", boldAttr);
                doc.insertString(doc.getLength(), value + "\n", normalAttr);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }

        // Thêm dữ liệu chi tiết theo bảng
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            if (table == tableThuoc) {
                String maDT = table.getValueAt(row, 0).toString(); // MADT
                ResultSet rs = stmt.executeQuery("SELECT * FROM CTDT WHERE MADT = '" + maDT + "'");
                doc.insertString(doc.getLength(), "\n--- Chi tiết đơn thuốc ---\n", boldAttr);
                while (rs.next()) {
                    doc.insertString(doc.getLength(), "Sản phẩm: " + rs.getString("MASP") + ", SL: " + rs.getInt("SOLUONG") + ", Đơn giá: " + rs.getDouble("DONGIA") + ", Thành tiền: " + rs.getDouble("THANHTIEN") + "\n", normalAttr);
                }
            } else if (table == tableHDKham) {
                String maHD = table.getValueAt(row, 0).toString(); // MAHDKB
                ResultSet rs = stmt.executeQuery("SELECT * FROM CTHD_KHAMBENH WHERE MAHDKB = '" + maHD + "'");
                doc.insertString(doc.getLength(), "\n--- Chi tiết khám bệnh ---\n", boldAttr);
                while (rs.next()) {
                    doc.insertString(doc.getLength(), "Dịch vụ: " + rs.getString("MADV") + ", SL: " + rs.getInt("SOLUONGDV") + ", Trị giá: " + rs.getDouble("TRIGIA") + "\n", normalAttr);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        textPane.setCaretPosition(0); // Đặt con trỏ về đầu => không cuộn xuống cuối
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0xd9eef2));
        
        String status = table.getValueAt(row, table.getColumnCount() - 1).toString().trim().toLowerCase();

        
        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(0x2B4A59));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}


// Renderer tùy chỉnh để đổi màu văn bản trong cột "Trạng thái thanh toán"
class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String status = value != null ? value.toString().trim().toLowerCase() : "";
        switch (status) {
            case "chưa thanh toán" -> {
                c.setFont(new Font("Arial", Font.BOLD, 13));
                c.setForeground(Color.RED);
            }
            case "đã thanh toán" -> c.setForeground(new Color(0x2E7D32));
            default -> c.setForeground(Color.BLACK);
        }
        return c;
    }
}