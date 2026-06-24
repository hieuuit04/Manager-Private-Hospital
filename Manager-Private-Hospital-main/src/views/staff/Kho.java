/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.staff;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Kho extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private CardLayout contentLayout;
    private JLabel KSPLabel, KTBLabel;
    private JPanel khoSPPanel;
    private JPanel KhoTBPanel;

    public Kho() {

        setTitle("📄 Quản lý kho");
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

        KSPLabel = createMenuLabel("Kho sản phẩm");
        KTBLabel = createMenuLabel("Kho thiết bị");

        topMenu.add(KSPLabel);
        topMenu.add(KTBLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        khoSPPanel = createKhoPanel("sanpham");
        KhoTBPanel = createKhoPanel("thietbi");

        contentPanel.add(khoSPPanel, "sanpham");
        contentPanel.add(KhoTBPanel, "thietbi");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("sanpham");
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
        if (tabName.equals("sanpham")) {
            highlightLabel(KSPLabel);
        } else if (tabName.equals("thietbi")) {
            highlightLabel(KTBLabel);
        }
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {KSPLabel, KTBLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        KSPLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("sanpham");
            }
        });
        KTBLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("thietbi");
            }
        });
    }

    private JPanel pendingTableSection;
    private JPanel historyTableSection;

    private JPanel createKhoPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(0xf4f7fb));

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(0x2B4A59));
        searchButton.setForeground(Color.WHITE);

        JTextField keywordField = new JTextField(15);
        keywordField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField tinhTrangSPField = new JTextField(10);
        tinhTrangSPField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField soLuongField = new JTextField(10);
        soLuongField.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField tenField = new JTextField(10);
        tenField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField ngayField = new JTextField(10);
        ngayField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Tìm kiếm trên cả hai bảng
        searchButton.addActionListener(e -> {
            String keyword = keywordField.getText().trim();
            String soLuong = soLuongField.getText().trim();
            String tinhTrangSP = tinhTrangSPField.getText().trim();
            String ten = tenField.getText().trim();
            String ngay = ngayField.getText();

            try {
                Map<String, List<String[]>> resultMap = searchKho(keyword, soLuong, ten, ngay, tinhTrangSP, title);

                content.removeAll();
                content.add(searchPanel);
                content.add(Box.createVerticalStrut(20));
                if (title.equalsIgnoreCase("sanpham")) {
                    content.add(createTableSection("Kho sản phẩm",
                            resultMap.get("SP").toArray(new String[0][])));
                } else if (title.equalsIgnoreCase("thietbi")) {
                    content.add(createTableSection("Kho thiết bị",
                            resultMap.get("TB").toArray(new String[0][])));
                }
                content.add(Box.createVerticalStrut(30));
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

        if (title.equalsIgnoreCase("sanpham")) {
            JLabel tenLabel = new JLabel("Tên sản phẩm:");
            tenLabel.setFont(labelFont);
            gbc.gridx = 2;
            searchPanel.add(tenLabel, gbc);
            gbc.gridx = 3;
            searchPanel.add(tenField, gbc);

            JLabel ngayLabel = new JLabel("HSD:");
            ngayLabel.setFont(labelFont);
            gbc.gridx = 4;
            searchPanel.add(ngayLabel, gbc);
            gbc.gridx = 5;
            searchPanel.add(ngayField, gbc);
        } else if (title.equalsIgnoreCase("thietbi")) {
            JLabel tenLabel = new JLabel("Tên thiết bị:");
            tenLabel.setFont(labelFont);
            gbc.gridx = 2;
            searchPanel.add(tenLabel, gbc);
            gbc.gridx = 3;
            searchPanel.add(tenField, gbc);

            JLabel ngayLabel = new JLabel("Ngày mua:");
            ngayLabel.setFont(labelFont);
            gbc.gridx = 4;
            searchPanel.add(ngayLabel, gbc);
            gbc.gridx = 5;
            searchPanel.add(ngayField, gbc);
        }

        JLabel soLuongLabel = new JLabel("Số lượng tồn kho:");
        soLuongLabel.setFont(labelFont);
        gbc.gridx = 6;
        searchPanel.add(soLuongLabel, gbc);
        gbc.gridx = 7;
        searchPanel.add(soLuongField, gbc);

        JLabel tinhTrangSPLabel = new JLabel("Tình trạng sản phẩm:");
        tinhTrangSPLabel.setFont(labelFont);
        gbc.gridx = 8;
        searchPanel.add(tinhTrangSPLabel, gbc);
        gbc.gridx = 9;
        searchPanel.add(tinhTrangSPField, gbc);

        gbc.gridx = 10;
        searchPanel.add(searchButton, gbc);

        content.add(searchPanel);
        content.add(Box.createVerticalStrut(20));

        try {
            if (title.equalsIgnoreCase("sanpham")) {
                List<String[]> pending = layDuLieuKho(title);
                pendingTableSection = createTableSection("Kho sản phẩm", pending.toArray(String[][]::new));
                content.add(pendingTableSection);
            } else if (title.equalsIgnoreCase("thietbi")) {
                List<String[]> history = layDuLieuKho(title);
                historyTableSection = createTableSection("Kho thiết bị", history.toArray(String[][]::new));
                content.add(historyTableSection);
            }
            content.add(Box.createVerticalStrut(30));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải hóa đơn: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(content);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private List<String[]> layDuLieuKho(String title) throws SQLException, ClassNotFoundException {
        List<String[]> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            if (title.equalsIgnoreCase("sanpham")) {
                String sql = "SELECT CT.*, TENSP, NSX, HSD, TENKHO FROM CHITIETKHO CT JOIN SANPHAM SP ON CT.MAVATTU = SP.MASP JOIN KHO K ON CT.MAKHO = K.MAKHO";
                PreparedStatement ps = conn.prepareStatement(sql);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String[] row = {
                            rs.getString("MAVATTU"),
                            rs.getString("TENKHO"),
                            rs.getString("TENSP"),
                            rs.getString("SLTONKHO"),
                            rs.getString("LOAISP"),
                            rs.getString("NSX"),
                            rs.getString("HSD"),
                            rs.getString("TINHTRANGSP"),
                            rs.getString("LOAIVATTU"),};

                        result.add(row);
                    }
                }
            } else if (title.equalsIgnoreCase("thietbi")) {
                String sql = "SELECT CT.*, TENTB, NGAYMUA, TENKHO FROM CHITIETKHO CT JOIN THIETBI TB ON CT.MAVATTU = TB.MATB JOIN KHO K ON CT.MAKHO = K.MAKHO";
                PreparedStatement ps = conn.prepareStatement(sql);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String[] row = {
                            rs.getString("MAVATTU"),
                            rs.getString("TENKHO"),
                            rs.getString("TENTB"),
                            rs.getString("SLTONKHO"),
                            rs.getString("LOAISP"),
                            rs.getString("NGAYMUA"),
                            rs.getString("TINHTRANGSP"),
                            rs.getString("LOAIVATTU"),};

                        result.add(row);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn hóa đơn: " + e.getMessage());
        }

        return result;
    }

    private Map<String, List<String[]>> searchKho(String keyword, String soLuong, String ten, String ngay, String tinhTrangSP, String title)
            throws SQLException, ClassNotFoundException {
        Map<String, List<String[]>> resultMap = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        List<String> params = new ArrayList<>();

        if (title.equalsIgnoreCase("sanpham")) {
            resultMap.put("SP", new ArrayList<>());
            sql = new StringBuilder("SELECT CT.*, TENSP, NSX, HSD, TENKHO FROM CHITIETKHO CT JOIN SANPHAM SP ON CT.MAVATTU = SP.MASP JOIN KHO K ON CT.MAKHO = K.MAKHO WHERE 1=1");

            if (!keyword.isEmpty()) {
                sql.append(" AND (LOWER(MAVATTU) LIKE ? OR LOWER(TENKHO) LIKE ? OR LOWER(SLTONKHO) LIKE ? OR LOWER(LOAISP) LIKE ? OR LOWER(LOAIVATTU) LIKE ? "
                        + "OR LOWER(TINHTRANGSP) LIKE ? OR LOWER(TENSP) LIKE ? )");
                for (int i = 0; i < 7; i++) {
                    params.add("%" + keyword.toLowerCase() + "%");
                }
            }

            if (!ngay.isEmpty()) {
                sql.append(" AND HSD = TO_DATE(?, 'DD/MM/YYYY')");
                params.add(ngay);
            }

            if (!ten.isEmpty()) {
                sql.append(" AND LOWER(TENSP) LIKE ?");
                params.add("%" + ten.toLowerCase() + "%");
            }

        } else if (title.equalsIgnoreCase("thietbi")) {
            resultMap.put("TB", new ArrayList<>());
            sql = new StringBuilder("SELECT CT.*, TENTB, NGAYMUA, TENKHO FROM CHITIETKHO CT JOIN THIETBI TB ON CT.MAVATTU = TB.MATB JOIN KHO K ON CT.MAKHO = K.MAKHO WHERE 1=1");
            if (!keyword.isEmpty()) {
                sql.append(" AND (LOWER(MAVATTU) LIKE ? OR LOWER(TENKHO) LIKE ? OR LOWER(SLTONKHO) LIKE ? OR LOWER(LOAISP) LIKE ? OR LOWER(LOAIVATTU) LIKE ? "
                        + "OR LOWER(TINHTRANGSP) LIKE ? OR LOWER(TENTB) LIKE ? )");
                for (int i = 0; i < 7; i++) {
                    params.add("%" + keyword.toLowerCase() + "%");
                }
            }

            if (!ngay.isEmpty()) {
                sql.append(" AND NGAYMUA = TO_DATE(?, 'DD/MM/YYYY')");
                params.add(ngay);
            }

            if (!ten.isEmpty()) {
                sql.append(" AND LOWER(TENTB) LIKE ?");
                params.add("%" + ten.toLowerCase() + "%");
            }
        }
        
        if (!soLuong.isEmpty()) {
            sql.append(" AND LOWER(SLTONKHO) LIKE ?");
            params.add("%" + soLuong.toLowerCase() + "%");
        }

        if (!tinhTrangSP.isEmpty()) {
            sql.append(" AND LOWER(TINHTRANGSP) LIKE ?");
            params.add("%" + tinhTrangSP.toLowerCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (title.equalsIgnoreCase("sanpham")) {
                        String[] row = {
                            rs.getString("MAVATTU"),
                            rs.getString("TENKHO"),
                            rs.getString("TENSP"),
                            rs.getString("SLTONKHO"),
                            rs.getString("LOAISP"),
                            rs.getString("NSX"),
                            rs.getString("HSD"),
                            rs.getString("TINHTRANGSP"),
                            rs.getString("LOAIVATTU"),};
                        resultMap.get("SP").add(row);
                    } else if (title.equalsIgnoreCase("thietbi")) {
                        String[] row = {
                            rs.getString("MAVATTU"),
                            rs.getString("TENKHO"),
                            rs.getString("TENTB"),
                            rs.getString("SLTONKHO"),
                            rs.getString("LOAISP"),
                            rs.getString("TINHTRANGSP"),
                            rs.getString("LOAIVATTU"),};
                        resultMap.get("TB").add(row);
                    }
                }
            }
        }

        return resultMap;
    }


    private void xemCTK(JTable table, int row) {

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

        textPane.setCaretPosition(0);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xd9eef2));

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
        DefaultTableModel model = new DefaultTableModel();

        if (title.equals("Kho sản phẩm")) {
//            JButton btnThemSP = btXuLyKho("Thêm sản phẩm");
//            JButton btnSuaSP = btXuLyKho("Sửa sản phẩm");
//            JButton btnXoaSP = btXuLyKho("Xóa sản phẩm");
//
//            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            buttonPanel.setOpaque(false);
//            buttonPanel.add(btnThemSP);
//            buttonPanel.add(btnXoaSP);
//            buttonPanel.add(btnSuaSP);
//            headerPanel.add(buttonPanel, BorderLayout.EAST);

            String[] columnNames = {
                "Mã vật tư", "Tên kho",
                "Tên sản phẩm", "Số lượng tồn kho", "Loại sản phẩm", "NSX", "HSD", "Tình trạng sản phẩm",
                "Loại vật tư"
            };

            section.add(headerPanel, BorderLayout.NORTH);

            model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Không cho chỉnh sửa bất kỳ ô nào
                }
            };

        } else if (title.equals("Kho thiết bị")) {
//            JButton btnThemTB = btXuLyKho("Thêm thiết bị");
//            JButton btnSuaTB = btXuLyKho("Sửa thiết bị");
//            JButton btnXoaTB = btXuLyKho("Xóa thiết bị");
//
//            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//            buttonPanel.setOpaque(false);
//            buttonPanel.add(btnThemTB);
//            buttonPanel.add(btnXoaTB);
//            buttonPanel.add(btnSuaTB);
//            headerPanel.add(buttonPanel, BorderLayout.EAST);

            String[] columnNames = {
                "Mã vật tư", "Tên kho",
                "Tên thiết bị", "Số lượng tồn kho", "Loại sản phẩm", "Ngày mua", "Tình trạng sản phẩm",
                "Loại vật tư"
            };
            section.add(headerPanel, BorderLayout.NORTH);

            model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Không cho chỉnh sửa bất kỳ ô nào
                }
            };
        }

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

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        // Thêm sự kiện double-click hiển thị chi tiết
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        xemCTK(table, selectedRow);
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        scroll.setPreferredSize(new Dimension(1000, 250));

        section.add(scroll, BorderLayout.CENTER);
        return section;

    }
    
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            int soLuongTonKhoCol = 3;
            Object slValue = table.getValueAt(row, soLuongTonKhoCol);

            try {
                int sl = Integer.parseInt(slValue.toString().trim());
                if (sl < 20) {
                    c.setForeground(Color.RED); // chữ đỏ
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.BLACK); // bình thường
                }
            } catch (NumberFormatException e) {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    };

//    private JButton btXuLyKho(String title) {
//        JButton btn = new JButton(title);
//        btn.setBackground(new Color(0xff9800));
//        btn.setForeground(Color.WHITE);
//        btn.setFont(new Font("Arial", Font.BOLD, 15));
//        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        btn.setFocusPainted(false);
//
//        btn.addMouseListener(new MouseAdapter() {
//            public void mouseEntered(MouseEvent e) {
//                btn.setBackground(new Color(0x588EA7));
//            }
//
//            public void mouseExited(MouseEvent e) {
//                btn.setBackground(new Color(0xff9800));
//            }
//        });

//        btn.addActionListener(e -> {
//            try {
//                new CreateBill('').setVisible(true);
//            } catch (SQLException | ClassNotFoundException ex) {
//                Logger.getLogger(Kho.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//
//        return btn;
//    }


}
