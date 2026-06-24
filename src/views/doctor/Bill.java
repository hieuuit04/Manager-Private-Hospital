///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package views.doctor;
//
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.border.*;
//import javax.swing.table.DefaultTableModel;
//
//import utils.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;
//import javax.swing.text.StyledDocument;
//
//
//
//public class Bill extends JFrame {
//    private CardLayout cardLayout;
//    private JPanel contentPanel;
//    private String doctorId;
//    private CardLayout contentLayout;
//    private JLabel HDKBLabel;
//    private JPanel khambenhPanel;
//    
//    public Bill(String doctorId) {
//        this.doctorId = doctorId;
//        
//        setTitle("📄 Danh sách hóa đơn");
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        // Co theo kích thước màn hình (đề xuất)
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setSize(screenSize.width, screenSize.height); // full màn hình
//        setLayout(new BorderLayout());
//
//        // ===== RIGHT PANEL (Card Layout) =====
//        cardLayout = new CardLayout();
//        contentPanel = new JPanel(cardLayout);
//
//        add(contentPanel, BorderLayout.CENTER);
//        
//        // Menu đầu trang
//        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
//        topMenu.setBackground(new Color(0xe8faf8));
//
//        HDKBLabel = createMenuLabel("Hóa đơn khám bệnh");
//
//        topMenu.add(HDKBLabel);
//
//        add(topMenu, BorderLayout.NORTH);
//
//        // Panel nội dung
//        contentLayout = new CardLayout();
//        contentPanel = new JPanel(contentLayout);
//
//        khambenhPanel = createHDKhamBenhPanel();
//
//        contentPanel.add(khambenhPanel, "khambenh");
//
//        add(contentPanel, BorderLayout.CENTER);
//
//        switchTab("khambenh");
//        addMenuListeners();
//    }
//
//    private JLabel createMenuLabel(String text) {
//        JLabel label = new JLabel(text);
//        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
//        label.setForeground(new Color(0x2B4A59));
//        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
//        return label;
//    }
//
//    private void switchTab(String tabName) {
//        resetMenuStyle();
//        contentLayout.show(contentPanel, tabName);
//        if (tabName.equals("khambenh")) highlightLabel(HDKBLabel);
//    }
//
//    private void highlightLabel(JLabel label) {
//        label.setForeground(new Color(0xff9800));
//        label.setFont(label.getFont().deriveFont(Font.BOLD));
//        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
//    }
//
//    private void resetMenuStyle() {
//        JLabel[] labels = {HDKBLabel};
//        for (JLabel label : labels) {
//            label.setForeground(new Color(0x2B4A59));
//            label.setFont(label.getFont().deriveFont(Font.PLAIN));
//            label.setBorder(null);
//        }
//    }
//
//    private void addMenuListeners() {
//        HDKBLabel.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                switchTab("khambenh");
//            }
//        });
//    }
//
//
//    private JPanel pendingTableSection;
//    private JPanel historyTableSection;
//
//    private JPanel createHDKhamBenhPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//
//        JPanel content = new JPanel();
//        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
//        content.setBackground(new Color(0xf4f7fb));
//
//        JPanel searchPanel = new JPanel(new GridBagLayout());
//        searchPanel.setBackground(new Color(0xCDE8E5));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        JTextField keywordField = new JTextField(15);
//        keywordField.setFont(new Font("Arial", Font.PLAIN, 14));
//        JTextField ngayLapField = new JTextField(10);
//        ngayLapField.setFont(new Font("Arial", Font.PLAIN, 14));
//        JTextField tongTienField = new JTextField(10);
//        tongTienField.setFont(new Font("Arial", Font.PLAIN, 14));
//        JTextField phuongThucTTField = new JTextField(10);
//        phuongThucTTField.setFont(new Font("Arial", Font.PLAIN, 14));
//
//        JButton searchButton = new JButton("Tìm kiếm");
//        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
//        searchButton.setBackground(new Color(0x2B4A59));
//        searchButton.setForeground(Color.WHITE);
//
//        // Tìm kiếm trên cả hai bảng
//        searchButton.addActionListener(e -> {
//            String keyword = keywordField.getText().trim();
//            String ngayLap = ngayLapField.getText().trim();
//            String tongTien = tongTienField.getText().trim();
//            String phuongThucTT = phuongThucTTField.getText().trim(); 
//
//            try {
//                Map<String, List<String[]>> resultMap = searchAdvancedBills(doctorId, keyword, ngayLap, tongTien, phuongThucTT);
//
//                content.removeAll();
//                content.add(searchPanel);
//                content.add(Box.createVerticalStrut(20));
//
//                content.add(createTableSection("Hóa đơn chưa thanh toán", 
//                    resultMap.get("Chưa thanh toán").toArray(new String[0][])));
//                content.add(Box.createVerticalStrut(30));
//                content.add(createTableSection("Hóa đơn đã thanh toán", 
//                    resultMap.get("Đã thanh toán").toArray(new String[0][])));
//
//                content.revalidate();
//                content.repaint();
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm nâng cao: " + ex.getMessage());
//            }
//        });
//
//        searchButton.addMouseListener(new MouseAdapter() {
//            public void mouseEntered(MouseEvent evt) {
//                searchButton.setBackground(new Color(0xff9800));
//            }
//
//            public void mouseExited(MouseEvent evt) {
//                searchButton.setBackground(new Color(0x2B4A59));
//            }
//        });
//        
//        // Giao diện hàng ngang
//        Font labelFont = new Font("Arial", Font.BOLD, 14); 
//
//        JLabel tuKhoaLabel = new JLabel("Từ khóa:");
//        tuKhoaLabel.setFont(labelFont);
//        gbc.gridx = 0; gbc.gridy = 0; searchPanel.add(tuKhoaLabel, gbc);
//        gbc.gridx = 1; searchPanel.add(keywordField, gbc);
//
//        JLabel ngayLapLabel = new JLabel("Ngày lập:");
//        ngayLapLabel.setFont(labelFont);
//        gbc.gridx = 2; searchPanel.add(ngayLapLabel, gbc);
//        gbc.gridx = 3; searchPanel.add(ngayLapField, gbc);
//
//        JLabel tongTienLabel = new JLabel("Tổng tiền:");
//        tongTienLabel.setFont(labelFont);
//        gbc.gridx = 4; searchPanel.add(tongTienLabel, gbc);
//        gbc.gridx = 5; searchPanel.add(tongTienField, gbc);
//
//        JLabel phuongThucTTLabel = new JLabel("Phương thức thanh toán:");
//        phuongThucTTLabel.setFont(labelFont);
//        gbc.gridx = 6; searchPanel.add(phuongThucTTLabel, gbc);
//        gbc.gridx = 7; searchPanel.add(phuongThucTTField, gbc);
//
//        gbc.gridx = 8; searchPanel.add(searchButton, gbc);
//
//        content.add(searchPanel);
//        content.add(Box.createVerticalStrut(20));
//
//        try {
//            List<String[]> pending = getBillsByStatus("= 'Chưa thanh toán'");
//            List<String[]> history = getBillsByStatus("= 'Đã thanh toán'");
//
//            pendingTableSection = createTableSection("Hóa đơn chưa thanh toán", pending.toArray(String[][]::new));
//            historyTableSection = createTableSection("Hóa đơn đã thanh toán", history.toArray(String[][]::new));
//
//            content.add(pendingTableSection);
//            content.add(Box.createVerticalStrut(30));
//            content.add(historyTableSection);
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Lỗi khi tải hóa đơn: " + e.getMessage());
//        }
//
//        JScrollPane scrollPane = new JScrollPane(content);
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        return panel;
//    }
//
//    
//    private List<String[]> getBillsByStatus(String statusCondition) throws SQLException, ClassNotFoundException {
//        List<String[]> result = new ArrayList<>();
//
//        String sql = "SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM WHERE K.MABS = ? AND H.TRANGTHAITT" + statusCondition;
//
//        try (Connection conn = DBConnection.getConnection();
//        PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, doctorId.trim());
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    String[] row = {
//                        rs.getString("MAHDKB"),
//                        rs.getString("MAKHAM"),
//                        rs.getString("NGAYLAP"),
//                        rs.getString("TIENKHAM"),
//                        rs.getString("TONGTIEN"),
//                        rs.getString("PHUONGTHUCTT"),
//                        rs.getString("GHICHU") == null ? "" : rs.getString("GHICHU"),
//                        rs.getString("TRANGTHAITT"),
//                    };
//
//                    result.add(row);
//                }
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Lỗi truy vấn hóa đơn: " + e.getMessage());
//        }
//
//        return result;
//    }
//
//    
//    private Map<String, List<String[]>> searchAdvancedBills(String doctorId, String keyword, String ngayLap, String tongTien, String phuongThucTT)
//        throws SQLException, ClassNotFoundException {
//        Map<String, List<String[]>> resultMap = new HashMap<>();
//        resultMap.put("Chưa thanh toán", new ArrayList<>());
//        resultMap.put("Đã thanh toán", new ArrayList<>());
//
//        StringBuilder sql = new StringBuilder("SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM WHERE K.MABS = ?");
//        List<String> params = new ArrayList<>();
//        params.add(doctorId.trim());
//
//        if (!keyword.isEmpty()) {
//            sql.append(" AND (LOWER(H.MAHDKB) LIKE ? OR LOWER(H.MAKHAM) LIKE ? OR LOWER(H.NGAYLAP) LIKE ? OR LOWER(H.TIENKHAM) LIKE ? "
//                    + "OR LOWER(H.TONGTIEN) LIKE ? OR LOWER(H.PHUONGTHUCTT) LIKE ? OR LOWER(H.GHICHU) LIKE ? OR LOWER(H.TRANGTHAITT) LIKE ?)");
//            for (int i = 0; i < 8; i++) params.add("%" + keyword.toLowerCase() + "%");
//        }
//
//        if (!ngayLap.isEmpty()) {
//            sql.append(" AND NGAYLAP = ?");
//            params.add(ngayLap);
//        }
//
//        if (!tongTien.isEmpty()) {
//            sql.append(" AND LOWER(TONGTIEN) LIKE ?");
//            params.add("%" + tongTien.toLowerCase() + "%");
//        }
//
//        if (!phuongThucTT.isEmpty()) {
//            sql.append(" AND LOWER(PHUONGTHUCTT) LIKE ?");
//            params.add("%" + phuongThucTT.toLowerCase() + "%");
//        }
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                ps.setString(i + 1, params.get(i));
//            }
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    String[] row = {
//                        rs.getString("MAHDKB"),
//                        rs.getString("MAKHAM"),
//                        rs.getString("NGAYLAP"),
//                        rs.getString("TIENKHAM"),
//                        rs.getString("TONGTIEN"),
//                        rs.getString("PHUONGTHUCTT"),
//                        rs.getString("GHICHU") == null ? "" : rs.getString("GHICHU"),
//                        rs.getString("TRANGTHAITT"),
//                    };
//                    String trangThai = rs.getString("TRANGTHAITT");
//                    if (trangThai.equalsIgnoreCase("Chưa thanh toán")) {
//                        resultMap.get("Chưa thanh toán").add(row);
//                    } else if (trangThai.equalsIgnoreCase("Đã thanh toán")) {
//                        resultMap.get("Đã thanh toán").add(row);
//                    }
//                }
//            }
//        }
//
//        return resultMap;
//    }
//    
//    private void updateBillStatus(String id, String newStatus) {
//        String sql = "UPDATE HOADON_KHAMBENH SET TRANGTHAITT = ? WHERE MAHDKB = ?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, newStatus);
//            ps.setString(2, id);
//            ps.executeUpdate();
//        } catch (SQLException | ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái: " + e.getMessage());
//        }
//    }
//    
//    private void reloadBillPanels() {
//        contentPanel.remove(khambenhPanel);
//
//        khambenhPanel = createHDKhamBenhPanel();
//
//        contentPanel.add(khambenhPanel, "khambenh");
//
//        cardLayout.show(contentPanel, "khambenh");
//        revalidate();
//        repaint();
//    }
//
//    
//    
//    private void showUpdateForm(String id, String maKham, String tienKham, String tongTien, String ghiChu) {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật hóa đơn khám bệnh", true);
//        dialog.setSize(350, 200);
//        dialog.setLocationRelativeTo(this);
//
//        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
//        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
//        inputPanel.setOpaque(false); // để thấy màu nền từ contentPanel
//
//        JTextField maKhamField = new JTextField(maKham);
//        JTextField tienKhamField = new JTextField(tienKham);
//        JTextField tongTienField = new JTextField(tongTien);
//        JTextField ghiChuField = new JTextField(ghiChu);
//
//        inputPanel.add(new JLabel("Mã khám:"));
//        inputPanel.add(maKhamField);
//        inputPanel.add(new JLabel("Tiền khám:"));
//        inputPanel.add(tienKhamField);
//        inputPanel.add(new JLabel("Tổng tiền:"));
//        inputPanel.add(tongTienField);
//        inputPanel.add(new JLabel("Ghi chú:"));
//        inputPanel.add(ghiChuField);
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
//        buttonPanel.setOpaque(false);
//
//        JButton saveBtn = new JButton("Lưu");
//        saveBtn.setBackground(new Color(0x2B4A59));
//        saveBtn.setForeground(Color.WHITE);
//        saveBtn.addActionListener(e -> {
//            try {
//                updateBillInfo(id, maKhamField.getText(), tienKhamField.getText(), tongTienField.getText(), ghiChuField.getText());
//            } catch (ParseException ex) {
//                Logger.getLogger(Bill.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            dialog.dispose();
//            reloadBillPanels();
//        });
//        
//        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                saveBtn.setBackground(new Color(0xff9800));
//            }
//
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                saveBtn.setBackground(new Color(0x2B4A59));
//            }
//        });
//
//        JButton cancelBtn = new JButton("Hủy");
//        cancelBtn.setBackground(new Color(0x2B4A59));
//        cancelBtn.setForeground(Color.WHITE);
//        cancelBtn.addActionListener(e -> dialog.dispose());
//        
//        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                saveBtn.setBackground(new Color(0xff9800));
//            }
//
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                saveBtn.setBackground(new Color(0x2B4A59));
//            }
//        });
//
//        buttonPanel.add(saveBtn);
//        buttonPanel.add(cancelBtn);
//
//        contentPanel.add(inputPanel, BorderLayout.CENTER);
//        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        dialog.setContentPane(contentPanel);
//        dialog.setVisible(true);
//    }
//
//
//    private void updateBillInfo(String id, String maKham, String tienKham, String tongTien, String ghiChu) throws ParseException {
//        String sql = "UPDATE HOADON_KHAMBENH SET MAKHAM = ?, TIENKHAM = ?, TONGTIEN = ?, GHICHU = ? WHERE MAHDKB = ?";
//        try (Connection conn = DBConnection.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql)) {
//            
//            ps.setString(1, maKham);
//            ps.setString(2, tienKham);
//            ps.setString(3, tongTien);
//            ps.setString(4, ghiChu);
//            ps.setString(5, id);
//            ps.executeUpdate();
//        } catch (SQLException | ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi cập nhật thông tin lịch hẹn: " + e.getMessage());
//        }
//    }
//    
//    private void deleteBill(String id) {
//        String sql = "DELETE FROM HOADON_KHAMBENH WHERE MAHDKB = ?";
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, id);
//            int affectedRows = ps.executeUpdate();
//            if (affectedRows > 0) {
//                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
//            } else {
//                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn để xóa.");
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + e.getMessage());
//        }
//    }
//
//    private void showBillDetails(JTable table, int row) {
//        String status = table.getValueAt(row, 7).toString();
//        boolean isPending = status.equalsIgnoreCase("Chưa thanh toán");
//
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//
//        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//        contentPanel.setBackground(new Color(0xd9eef2));
//
//        JTextPane textPane = new JTextPane();
//        textPane.setEditable(false);
//        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        textPane.setBackground(Color.WHITE);
//        textPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
//        StyledDocument doc = textPane.getStyledDocument();
//
//        SimpleAttributeSet boldAttr = new SimpleAttributeSet();
//        StyleConstants.setBold(boldAttr, true);
//        SimpleAttributeSet normalAttr = new SimpleAttributeSet();
//
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            try {
//                doc.insertString(doc.getLength(), table.getColumnName(i) + ": ", boldAttr);
//                doc.insertString(doc.getLength(), table.getValueAt(row, i).toString() + "\n", normalAttr);
//            } catch (BadLocationException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        textPane.setCaretPosition(0);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setBackground(new Color(0xd9eef2));
//
//        if (isPending) {
//            JButton cancelButton = new JButton("Đã thanh toán");
//            cancelButton.setBackground(new Color(215, 86, 86));
//            cancelButton.setForeground(Color.WHITE);
//            cancelButton.addActionListener(e -> {
//                String id = table.getValueAt(row, 0).toString();
//                updateBillStatus(id, "Đã thanh toán");
//                dialog.dispose();
//                reloadBillPanels();
//            });
//
//            JButton updateButton = new JButton("Cập nhật");
//            updateButton.setBackground(new Color(0xff9800)); 
//            updateButton.setForeground(Color.WHITE);
//            updateButton.addActionListener(e -> {
//                dialog.dispose();
//                showUpdateForm(
//                    table.getValueAt(row, 0).toString(), // MALICH
//                    table.getValueAt(row, 1).toString(), // MAKHAM
//                    table.getValueAt(row, 3).toString(), // TIENKHAM
//                    table.getValueAt(row, 4).toString(), // TONGTIEN
//                    table.getValueAt(row, 6).toString()  // GHICHU
//                );
//            });
//
//            buttonPanel.add(updateButton);
//            buttonPanel.add(cancelButton);
//        }
//        
//        JButton closeButton = new JButton("Đóng");
//        closeButton.setBackground(new Color(0x2B4A59));
//        closeButton.setForeground(Color.WHITE);
//        closeButton.setFocusPainted(false);
//        closeButton.addActionListener(e -> dialog.dispose());
//
//        buttonPanel.add(closeButton);
//
//        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
//        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        dialog.setContentPane(contentPanel);
//        dialog.setSize(500, 350);
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
//    }
//
//    
//    private JPanel createTableSection(String title, String[][] data) {
//        JPanel section = new JPanel(new BorderLayout());
//        section.setBorder(new EmptyBorder(10, 20, 10, 20));
//        section.setBackground(new Color(0xf4f7fb)); 
//
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        headerPanel.setOpaque(false);
//        JLabel sectionTitle = new JLabel(title);
//        sectionTitle.setFont(new Font("Arial", Font.BOLD, 22));
//        sectionTitle.setForeground(new Color(0x588EA7));
//        //sectionTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
//        headerPanel.add(sectionTitle, BorderLayout.WEST);
//        
//        if (title.equals("Hóa đơn chưa thanh toán")) {
//            JButton btnTaoHD = new JButton("Tạo hóa đơn");
//            btnTaoHD.setBackground(new Color(0xff9800)); 
//            btnTaoHD.setForeground(Color.WHITE);
//            btnTaoHD.setFont(new Font("Arial", Font.BOLD, 20));
//            btnTaoHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            btnTaoHD.setFocusPainted(false);
//
//            btnTaoHD.addMouseListener(new MouseAdapter() {
//                public void mouseEntered(MouseEvent e) {
//                    btnTaoHD.setBackground(new Color(0x588EA7)); 
//                }
//
//                public void mouseExited(MouseEvent e) {
//                    btnTaoHD.setBackground(new Color(0xff9800));
//                }
//            });
//
//            btnTaoHD.addActionListener(e -> {
//                try {
//                    new CreateBill(doctorId).setVisible(true);
//                } catch (SQLException | ClassNotFoundException ex) {
//                    Logger.getLogger(Bill.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//
//            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            buttonPanel.setOpaque(false);
//            buttonPanel.add(btnTaoHD);
//            headerPanel.add(buttonPanel, BorderLayout.EAST);
//        }
//        
//        section.add(headerPanel, BorderLayout.NORTH);
//
//        // Cột tương ứng với bảng LICHHEN
//        String[] columnNames = {
//            "Mã hóa đơn khám bệnh", "Mã khám", "Ngày lập", "Tiền khám", "Tổng tiền",
//            "Phương thức thanh toán", "Ghi chú", "Trạng thái thanh toán"
//        };
//
//        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; 
//            }
//        };
//
//        JTable table = new JTable(model); 
//        table.setOpaque(true);
//
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
//
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
//        }
//        
//        table.getTableHeader().setBackground(new Color(0xCDE8E5));
//        table.getTableHeader().setForeground(Color.BLACK);
//        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
//        table.setFont(new Font("Arial", Font.PLAIN, 14));
//        table.setBackground(Color.WHITE);
//        table.setCellSelectionEnabled(false);
//        table.setRowSelectionAllowed(true); // Chỉ cho phép chọn hàng
//        
//        
//        // Thêm sự kiện double-click hiển thị chi tiết
//        table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 1) {
//                    int selectedRow = table.getSelectedRow();
//                    if (selectedRow != -1) {
//                        showBillDetails(table, selectedRow);
//                }
//                }
//            }
//        });
//
//        JScrollPane scroll = new JScrollPane(table);
//        
//        scroll.setPreferredSize(new Dimension(1000, 250));
//
//        section.add(scroll, BorderLayout.CENTER);
//        return section;
//       
//    }
//    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new Bill("U001").setVisible(true);
//        });
//    }
//    
//}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Bill extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private String doctorId;
    private CardLayout contentLayout;
    private JLabel HDKBLabel;
    private JPanel khambenhPanel;

    public Bill(String doctorId) {
        this.doctorId = doctorId;

        setTitle("📄 Danh sách hóa đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());

        // ===== RIGHT PANEL (Card Layout) =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        add(contentPanel, BorderLayout.CENTER);

        // Menu đầu trang
        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topMenu.setBackground(new Color(0xe8faf8));

        HDKBLabel = createMenuLabel("Hóa đơn khám bệnh");

        topMenu.add(HDKBLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        khambenhPanel = createHDKhamBenhPanel();

        contentPanel.add(khambenhPanel, "khambenh");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("khambenh");
        addMenuListeners();
    }

    private JLabel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        label.setForeground(new Color(0x2B4A59));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void switchTab(String tabName) {
        resetMenuStyle();
        contentLayout.show(contentPanel, tabName);
        if (tabName.equals("khambenh")) {
            highlightLabel(HDKBLabel);
        }
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {HDKBLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        HDKBLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("khambenh");
            }
        });
    }

    private JPanel pendingTableSection;
    private JPanel historyTableSection;

    private JPanel createHDKhamBenhPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(0xf4f7fb));

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField keywordField = new JTextField(15);
        keywordField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField ngayLapField = new JTextField(10);
        ngayLapField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField tongTienField = new JTextField(10);
        tongTienField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField phuongThucTTField = new JTextField(10);
        phuongThucTTField.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(0x2B4A59));
        searchButton.setForeground(Color.WHITE);

        // Tìm kiếm trên cả hai bảng
        searchButton.addActionListener(e -> {
            String keyword = keywordField.getText().trim();
            String ngayLap = ngayLapField.getText().trim();
            String tongTien = tongTienField.getText().trim();
            String phuongThucTT = phuongThucTTField.getText().trim();

            try {
                Map<String, List<String[]>> resultMap = searchAdvancedBills(doctorId, keyword, ngayLap, tongTien, phuongThucTT);

                content.removeAll();
                content.add(searchPanel);
                content.add(Box.createVerticalStrut(20));

                content.add(createTableSection("Hóa đơn chưa thanh toán",
                        resultMap.get("Chưa thanh toán").toArray(new String[0][])));
                content.add(Box.createVerticalStrut(30));
                content.add(createTableSection("Hóa đơn đã thanh toán",
                        resultMap.get("Đã thanh toán").toArray(new String[0][])));

                content.revalidate();
                content.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm nâng cao: " + ex.getMessage());
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                searchButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(MouseEvent evt) {
                searchButton.setBackground(new Color(0x2B4A59));
            }
        });

        // Giao diện hàng ngang
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        JLabel tuKhoaLabel = new JLabel("Từ khóa:");
        tuKhoaLabel.setFont(labelFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(tuKhoaLabel, gbc);
        gbc.gridx = 1;
        searchPanel.add(keywordField, gbc);

        JLabel ngayLapLabel = new JLabel("Ngày lập:");
        ngayLapLabel.setFont(labelFont);
        gbc.gridx = 2;
        searchPanel.add(ngayLapLabel, gbc);
        gbc.gridx = 3;
        searchPanel.add(ngayLapField, gbc);

        JLabel tongTienLabel = new JLabel("Tổng tiền:");
        tongTienLabel.setFont(labelFont);
        gbc.gridx = 4;
        searchPanel.add(tongTienLabel, gbc);
        gbc.gridx = 5;
        searchPanel.add(tongTienField, gbc);

        JLabel phuongThucTTLabel = new JLabel("Phương thức thanh toán:");
        phuongThucTTLabel.setFont(labelFont);
        gbc.gridx = 6;
        searchPanel.add(phuongThucTTLabel, gbc);
        gbc.gridx = 7;
        searchPanel.add(phuongThucTTField, gbc);

        gbc.gridx = 8;
        searchPanel.add(searchButton, gbc);

        content.add(searchPanel);
        content.add(Box.createVerticalStrut(20));

        try {
            List<String[]> pending = getBillsByStatus("= 'Chưa thanh toán'");
            List<String[]> history = getBillsByStatus("= 'Đã thanh toán'");

            pendingTableSection = createTableSection("Hóa đơn chưa thanh toán", pending.toArray(String[][]::new));
            historyTableSection = createTableSection("Hóa đơn đã thanh toán", history.toArray(String[][]::new));

            content.add(pendingTableSection);
            content.add(Box.createVerticalStrut(30));
            content.add(historyTableSection);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải hóa đơn: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(content);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private List<String[]> getBillsByStatus(String statusCondition) throws SQLException, ClassNotFoundException {
        List<String[]> result = new ArrayList<>();

        String sql = "SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM WHERE K.MABS = ? AND H.TRANGTHAITT" + statusCondition;

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorId.trim());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = {
                        rs.getString("MAHDKB"),
                        rs.getString("MAKHAM"),
                        rs.getString("NGAYLAP"),
                        rs.getString("TIENKHAM"),
                        rs.getString("TONGTIEN"),
                        rs.getString("PHUONGTHUCTT"),
                        rs.getString("GHICHU") == null ? "" : rs.getString("GHICHU"),
                        rs.getString("TRANGTHAITT"),};

                    result.add(row);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn hóa đơn: " + e.getMessage());
        }

        return result;
    }

    private Map<String, List<String[]>> searchAdvancedBills(String doctorId, String keyword, String ngayLap, String tongTien, String phuongThucTT)
            throws SQLException, ClassNotFoundException {
        Map<String, List<String[]>> resultMap = new HashMap<>();
        resultMap.put("Chưa thanh toán", new ArrayList<>());
        resultMap.put("Đã thanh toán", new ArrayList<>());

        StringBuilder sql = new StringBuilder("SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM WHERE K.MABS = ?");
        List<String> params = new ArrayList<>();
        params.add(doctorId.trim());

        if (!keyword.isEmpty()) {
            sql.append(" AND (LOWER(H.MAHDKB) LIKE ? OR LOWER(H.MAKHAM) LIKE ? OR LOWER(H.NGAYLAP) LIKE ? OR LOWER(H.TIENKHAM) LIKE ? "
                    + "OR LOWER(H.TONGTIEN) LIKE ? OR LOWER(H.PHUONGTHUCTT) LIKE ? OR LOWER(H.GHICHU) LIKE ? OR LOWER(H.TRANGTHAITT) LIKE ?)");
            for (int i = 0; i < 8; i++) {
                params.add("%" + keyword.toLowerCase() + "%");
            }
        }

        if (!ngayLap.isEmpty()) {
            sql.append(" AND NGAYLAP = ?");
            params.add(ngayLap);
        }

        if (!tongTien.isEmpty()) {
            sql.append(" AND LOWER(TONGTIEN) LIKE ?");
            params.add("%" + tongTien.toLowerCase() + "%");
        }

        if (!phuongThucTT.isEmpty()) {
            sql.append(" AND LOWER(PHUONGTHUCTT) LIKE ?");
            params.add("%" + phuongThucTT.toLowerCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = {
                        rs.getString("MAHDKB"),
                        rs.getString("MAKHAM"),
                        rs.getString("NGAYLAP"),
                        rs.getString("TIENKHAM"),
                        rs.getString("TONGTIEN"),
                        rs.getString("PHUONGTHUCTT"),
                        rs.getString("GHICHU") == null ? "" : rs.getString("GHICHU"),
                        rs.getString("TRANGTHAITT"),};
                    String trangThai = rs.getString("TRANGTHAITT");
                    if (trangThai.equalsIgnoreCase("Chưa thanh toán")) {
                        resultMap.get("Chưa thanh toán").add(row);
                    } else if (trangThai.equalsIgnoreCase("Đã thanh toán")) {
                        resultMap.get("Đã thanh toán").add(row);
                    }
                }
            }
        }

        return resultMap;
    }

    private void updateBillStatus(String id, String newStatus) {
        String sql = "UPDATE HOADON_KHAMBENH SET TRANGTHAITT = ? WHERE MAHDKB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    private void reloadBillPanels() {
        contentPanel.remove(khambenhPanel);

        khambenhPanel = createHDKhamBenhPanel();

        contentPanel.add(khambenhPanel, "khambenh");

        cardLayout.show(contentPanel, "khambenh");
        revalidate();
        repaint();
    }

    private void showUpdateForm(String id, String maKham, String tienKham, String tongTien, String ghiChu) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật hóa đơn khám bệnh", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setOpaque(false); // để thấy màu nền từ contentPanel

        JTextField maKhamField = new JTextField(maKham);
        JTextField tienKhamField = new JTextField(tienKham);
        JTextField tongTienField = new JTextField(tongTien);
        JTextField ghiChuField = new JTextField(ghiChu);

        inputPanel.add(new JLabel("Mã khám:"));
        inputPanel.add(maKhamField);
        inputPanel.add(new JLabel("Tiền khám:"));
        inputPanel.add(tienKhamField);
        inputPanel.add(new JLabel("Tổng tiền:"));
        inputPanel.add(tongTienField);
        inputPanel.add(new JLabel("Ghi chú:"));
        inputPanel.add(ghiChuField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton saveBtn = new JButton("Lưu");
        saveBtn.setBackground(new Color(0x2B4A59));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                updateBillInfo(id, maKhamField.getText(), tienKhamField.getText(), tongTienField.getText(), ghiChuField.getText());
            } catch (ParseException ex) {
                Logger.getLogger(Bill.class.getName()).log(Level.SEVERE, null, ex);
            }
            dialog.dispose();
            reloadBillPanels();
        });

        saveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0x2B4A59));
            }
        });

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setBackground(new Color(0x2B4A59));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(e -> dialog.dispose());

        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0x2B4A59));
            }
        });

        JButton editBtn = new JButton("Thêm chi tiết hóa đơn");
        editBtn.setBackground(new Color(0x2B4A59));
        editBtn.setForeground(Color.WHITE);
        editBtn.addActionListener(e -> {dialog.dispose();
                try {
                    new CreateCTHDKB(id).setVisible(true);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(Bill.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        editBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                saveBtn.setBackground(new Color(0x2B4A59));
            }
        });
        
        buttonPanel.add(editBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setVisible(true);
    }

    private void updateBillInfo(String id, String maKham, String tienKham, String tongTien, String ghiChu) throws ParseException {
        String sql = "UPDATE HOADON_KHAMBENH SET MAKHAM = ?, TIENKHAM = ?, TONGTIEN = ?, GHICHU = ? WHERE MAHDKB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKham);
            ps.setString(2, tienKham);
            ps.setString(3, tongTien);
            ps.setString(4, ghiChu);
            ps.setString(5, id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật thông tin lịch hẹn: " + e.getMessage());
        }
    }

    private void deleteBill(String id) {
        String sql = "DELETE FROM HOADON_KHAMBENH WHERE MAHDKB = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn để xóa.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }

    private void showBillDetails(JTable table, int row) {
        String status = table.getValueAt(row, 7).toString();
        boolean isPending = status.equalsIgnoreCase("Chưa thanh toán");

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(0xd9eef2));

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(Color.WHITE);
        textPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        StyledDocument doc = textPane.getStyledDocument();

        SimpleAttributeSet boldAttr = new SimpleAttributeSet();
        StyleConstants.setBold(boldAttr, true);
        SimpleAttributeSet normalAttr = new SimpleAttributeSet();

        for (int i = 0; i < table.getColumnCount(); i++) {
            try {
                doc.insertString(doc.getLength(), table.getColumnName(i) + ": ", boldAttr);
                doc.insertString(doc.getLength(), table.getValueAt(row, i).toString() + "\n", normalAttr);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String maHD = table.getValueAt(row, 0).toString(); // MAHDKB
            ResultSet rs = stmt.executeQuery("SELECT * FROM CTHD_KHAMBENH WHERE MAHDKB = '" + maHD + "'");
            doc.insertString(doc.getLength(), "\n--- Chi tiết khám bệnh ---\n", boldAttr);
            while (rs.next()) {
                doc.insertString(doc.getLength(), "Dịch vụ: " + rs.getString("MADV") + ", SL: " + rs.getInt("SOLUONGDV") + ", Trị giá: " + rs.getDouble("TRIGIA") + "\n", normalAttr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            String maKham = table.getValueAt(row, 1).toString(); // MAKHAM
            ResultSet rs = stmt.executeQuery("SELECT * FROM DIEUTRI DT WHERE DT.MAKHAM = '" + maKham + "'");
            doc.insertString(doc.getLength(), "--- Số tiền điều trị ---\n", boldAttr);
            while (rs.next()) {
                doc.insertString(doc.getLength(), "" + rs.getDouble("TONGHDDT") + "\n", normalAttr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        textPane.setCaretPosition(0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xd9eef2));

        if (isPending) {
            JButton cancelButton = new JButton("Đã thanh toán");
            cancelButton.setBackground(new Color(215, 86, 86));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.addActionListener(e -> {
                String id = table.getValueAt(row, 0).toString();
                updateBillStatus(id, "Đã thanh toán");
                dialog.dispose();
                reloadBillPanels();
            });

            JButton updateButton = new JButton("Cập nhật");
            updateButton.setBackground(new Color(0xff9800));
            updateButton.setForeground(Color.WHITE);
            updateButton.addActionListener(e -> {
                dialog.dispose();
                showUpdateForm(
                        table.getValueAt(row, 0).toString(), // MAHDKB
                        table.getValueAt(row, 1).toString(), // MAKHAM
                        table.getValueAt(row, 3).toString(), // TIENKHAM
                        table.getValueAt(row, 4).toString(), // TONGTIEN
                        table.getValueAt(row, 6).toString() // GHICHU
                );
            });

            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);
        }

        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(0x2B4A59));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(closeButton);

        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createTableSection(String title, String[][] data) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(new EmptyBorder(10, 20, 10, 20));
        section.setBackground(new Color(0xf4f7fb));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Arial", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(0x588EA7));
        //sectionTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        headerPanel.add(sectionTitle, BorderLayout.WEST);

        if (title.equals("Hóa đơn chưa thanh toán")) {
            JButton btnTaoHD = new JButton("Tạo hóa đơn");
            btnTaoHD.setBackground(new Color(0xff9800));
            btnTaoHD.setForeground(Color.WHITE);
            btnTaoHD.setFont(new Font("Arial", Font.BOLD, 20));
            btnTaoHD.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnTaoHD.setFocusPainted(false);

            btnTaoHD.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btnTaoHD.setBackground(new Color(0x588EA7));
                }

                public void mouseExited(MouseEvent e) {
                    btnTaoHD.setBackground(new Color(0xff9800));
                }
            });

            btnTaoHD.addActionListener(e -> {
                try {
                    new CreateBill(doctorId).setVisible(true);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(Bill.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setOpaque(false);
            buttonPanel.add(btnTaoHD);
            headerPanel.add(buttonPanel, BorderLayout.EAST);
        }

        section.add(headerPanel, BorderLayout.NORTH);

        // Cột tương ứng với bảng LICHHEN
        String[] columnNames = {
            "Mã hóa đơn khám bệnh", "Mã khám", "Ngày lập", "Tiền khám", "Tổng tiền",
            "Phương thức thanh toán", "Ghi chú", "Trạng thái thanh toán"
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setOpaque(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getTableHeader().setBackground(new Color(0xCDE8E5));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setBackground(Color.WHITE);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true); // Chỉ cho phép chọn hàng

        // Thêm sự kiện double-click hiển thị chi tiết
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

        JScrollPane scroll = new JScrollPane(table);

        scroll.setPreferredSize(new Dimension(1000, 250));

        section.add(scroll, BorderLayout.CENTER);
        return section;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Bill("U001").setVisible(true);
        });
    }

}
