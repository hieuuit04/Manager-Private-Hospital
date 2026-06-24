/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import utils.DBConnection;

public class PrescriptionAdmin extends JFrame {
    private JTable prescriptionTable;
    private JTextField tfMaDT, tfMaDS, tfMaBN, tfNgayBan, tfThanhTien, tfTrangThaiTT;
    private DefaultTableModel model;

    public PrescriptionAdmin() throws SQLException, ClassNotFoundException {

        setTitle("📄 Danh sách đơn thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
     
        initComponents();
        loadPrescription();
    }
    
    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0xCDE8E5)); ///////////////

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
        JLabel title = new JLabel("DANH SÁCH ĐƠN THUỐC", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0x78a2a7));
        title.setBackground(new Color(0xe8faf8));
        title.setOpaque(true);
        titlePanel.add(title, BorderLayout.CENTER);

        topPanel.add(titlePanel, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== PANEL CHỨA NÚT Ở GIỮA =====
        JPanel nameSearchPanel = new JPanel();
        nameSearchPanel.setBackground(new Color(0xCDE8E5));
        nameSearchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setBackground(new Color(0x78a2a7));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(300, 30));

        // Hover effect
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

        nameSearchPanel.add(btnSearch);
        
        searchPanel.add(nameSearchPanel, BorderLayout.NORTH);
        
        // ========== CÁC THÀNH PHẦN KHÁC ==========
        JPanel searchFieldsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));

        tfMaDT = new JTextField();
        tfMaDS = new JTextField();
        tfMaBN = new JTextField();
        tfNgayBan = new JTextField();
        tfThanhTien = new JTextField();
        tfTrangThaiTT = new JTextField();
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.BLACK;

        searchFieldsPanel.add(createLabeledField("Mã đơn thuốc:", tfMaDT, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã dược sĩ:", tfMaDS, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mã bệnh nhân:", tfMaBN, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày bán:", tfNgayBan, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Thành tiền:", tfThanhTien, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Trạng thái đơn thuốc:", tfTrangThaiTT, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        // ===== TABLE: DANH SÁCH ĐƠN THUỐC =====
        String[] columns = {"Mã đơn thuốc", "Mã dược sĩ", "Mã bác sĩ", "Mã bệnh nhân", "Giới tính bệnh nhân", "Lịch sử bệnh lý bệnh nhân", 
                            "Dị ứng bệnh nhân", "File Đơn Thuốc", "Ghi chú", "Ngày bán", "Thành tiền", "Trạng thái thanh toán"};
        
        model = new DefaultTableModel(columns, 0);
        prescriptionTable = new JTable(model);
        prescriptionTable.setRowHeight(28); //chiều cao mỗi hàng 
        prescriptionTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        prescriptionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        prescriptionTable.getTableHeader().setBackground(new Color(0xCDE8E5));
        prescriptionTable.getTableHeader().setForeground(Color.BLACK);
        prescriptionTable.getColumnModel().getColumn(9).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(prescriptionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(222, 246, 186)));
        
        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                loadPrescription(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(PrescriptionAdmin.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm dịch vụ.");
            }
        });


        // ===== CLICK XEM CHI TIẾT =====
        prescriptionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = prescriptionTable.getSelectedRow();
                if (selectedRow != -1) {
                    showPrescriptionDetails(selectedRow);
                }
            }
        });
    }

    private void loadPrescription() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM DONTHUOC_DONTHUOCYC WHERE 1=1");
        
        java.util.List<Object> params = new java.util.ArrayList<>();
        
        if (!tfMaDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MADT) LIKE ?");
            params.add("%" + tfMaDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfMaDS.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MADS) LIKE ?");
            params.add("%" + tfMaDS.getText().trim().toUpperCase() + "%");
        }
        if (!tfMaBN.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MABN) LIKE ?");
            params.add("%" + tfMaBN.getText().trim().toUpperCase() + "%");
        }
         if (!tfNgayBan.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYBAN, 'YYYY-MM-DD') = ?");
            params.add(tfNgayBan.getText().trim());
        }
         if (!tfThanhTien.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(THANHTIEN) LIKE ?");
            params.add("%" + tfThanhTien.getText().trim().toUpperCase() + "%");
        }
         if (!tfTrangThaiTT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAITT) LIKE ?");
            params.add("%" + tfTrangThaiTT.getText().trim().toUpperCase() + "%");
        }
         

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MADT"),
                    rs.getString("MADS"),
                    rs.getString("MABS"),
                    rs.getString("MABN"),
                    rs.getString("GIOITINHBN"),
                    rs.getString("NGAYSINHBN"),
                    rs.getString("LICHSU_BENHLY_BN"),
                    rs.getString("DIUNGBN"),
                    rs.getBlob("FILEDONTHUOC"),
                    rs.getString("GHICHU"),
                    rs.getString("NGAYBAN"),
                    rs.getString("THANHTIEN"),
                    rs.getString("TRANGTHAITT"),
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    
    private void showPrescriptionDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết đơn thuốc", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10 , 10, 10));

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
        
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String maDT = model.getValueAt(row, 0).toString(); // MADT
                ResultSet rs = stmt.executeQuery("SELECT * FROM CTDT WHERE MADT = '" + maDT + "'");
                doc.insertString(doc.getLength(), "\n--- Chi tiết đơn thuốc ---\n", boldAttr);
                while (rs.next()) {
                    doc.insertString(doc.getLength(), "Sản phẩm: " + rs.getString("MASP") + ", SL: " + rs.getInt("SOLUONG") + ", Đơn giá: " + rs.getDouble("DONGIA") + ", Thành tiền: " + rs.getDouble("THANHTIEN") + "\n", normalAttr);
                }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {   
                new PrescriptionAdmin().setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(PrescriptionAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
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
            case "chưa thanh toán":
                c.setForeground(Color.RED);
                break;
            case "đã thanh toán":
                c.setForeground(new Color(0x2E7D32)); // Xanh lá đậm
                break;
            default:
                c.setForeground(Color.BLACK);
                break;
        }
        return c;
    }
}




