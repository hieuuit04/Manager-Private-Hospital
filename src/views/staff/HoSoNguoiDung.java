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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import utils.DBConnection;

public class HoSoNguoiDung extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private CardLayout contentLayout;
    private JLabel benhNhanLabel, bacSiLabel, duocSiLabel, nhanVienLabel;
    private JPanel benhNhanPanel, bacSiPanel, duocSiPanel, nhanVienPanel;
    private String currentPanel = "benhnhan";

    private DefaultTableModel modelBenhNhan;
    private DefaultTableModel modelBacSi;
    private DefaultTableModel modelDuocSi;
    private DefaultTableModel modelNhanVien;

    private JTable tableBenhNhan;
    private JTable tableBacSi;
    private JTable tableDuocSi;
    private JTable tableNhanVien;

    public HoSoNguoiDung() throws SQLException, ClassNotFoundException {

        setTitle("📄 Danh sách người dùng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình

        // ===== RIGHT PANEL (Card Layout) =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        add(contentPanel, BorderLayout.CENTER);

        // Menu đầu trang
        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topMenu.setBackground(new Color(0xe8faf8));

        benhNhanLabel = createMenuLabel("Bệnh nhân");
        bacSiLabel = createMenuLabel("Bác sĩ");
        duocSiLabel = createMenuLabel("Dược sĩ");
        nhanVienLabel = createMenuLabel("Nhân viên");

        topMenu.add(benhNhanLabel);
        topMenu.add(bacSiLabel);
        topMenu.add(duocSiLabel);
        topMenu.add(nhanVienLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        modelBenhNhan = createModel();
        modelBacSi = createModel();
        modelDuocSi = createModel();
        modelNhanVien = createModel();

        tableBenhNhan = new JTable();
        tableBacSi = new JTable();
        tableDuocSi = new JTable();
        tableNhanVien = new JTable();

        benhNhanPanel = createPanel(modelBenhNhan, tableBenhNhan);
        bacSiPanel = createPanel(modelBacSi, tableBacSi);
        duocSiPanel = createPanel(modelDuocSi, tableDuocSi);
        nhanVienPanel = createPanel(modelNhanVien, tableNhanVien);

        contentPanel.add(benhNhanPanel, "benhnhan");
        contentPanel.add(bacSiPanel, "bacsi");
        contentPanel.add(duocSiPanel, "duocsi");
        contentPanel.add(nhanVienPanel, "nhanvien");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("benhnhan");
        addMenuListeners();

        bacSiLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentPanel = "bacsi";
                switchTab("bacsi");
            }
        });

        duocSiLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentPanel = "duocsi";
                switchTab("duocsi");
            }
        });

        nhanVienLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                currentPanel = "nhanvien";
                switchTab("nhanvien");
            }
        });

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

        switch (tabName) {
            case "benhnhan":
                highlightLabel(benhNhanLabel);
                loadDataForRole("Bệnh nhân", modelBenhNhan);
                break;
            case "bacsi":
                highlightLabel(bacSiLabel);
                loadDataForRole("Bác sĩ", modelBacSi);
                break;
            case "duocsi":
                highlightLabel(duocSiLabel);
                loadDataForRole("Dược sĩ", modelDuocSi);
                break;
            case "nhanvien":
                highlightLabel(nhanVienLabel);
                loadDataForRole("Nhân viên", modelNhanVien);
                break;
        }
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {benhNhanLabel, bacSiLabel, duocSiLabel, nhanVienLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        benhNhanLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("benhnhan");
            }
        });

        bacSiLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("bacsi");
            }
        });

        duocSiLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("duocsi");
            }
        });

        nhanVienLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("nhanvien");
            }
        });
    }

    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0xCDE8E5));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private DefaultTableModel createModel() {
        String[] columns = {"ID", "Họ tên", "SĐT", "Email", "Giới tính", "Ngày sinh", "Địa chỉ", "Vai trò", "Trạng thái", "Tên đăng nhập", "Mật khẩu"};
        return new DefaultTableModel(columns, 0);
    }

    private JPanel createPanel(DefaultTableModel model, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel nameSearchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        nameSearchPanel.setBackground(new Color(0xCDE8E5));

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setBackground(new Color(0x78a2a7));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(300, 30));

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

        // ===== SEARCH FIELDS =====
        JPanel searchFieldsPanel = new JPanel(new GridLayout(2, 5, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));
        

        
        JTextField tfId = new JTextField();
        JTextField tfTenND = new JTextField();
        JTextField tfSDT = new JTextField();
        JTextField tfGioiTinh = new JTextField();
        JTextField tfNgaySinh = new JTextField();
        JTextField tfTrangThai = new JTextField();
        JTextField tfEmail = new JTextField();
        JTextField tfRole = new JTextField();
        JTextField tfUsername = new JTextField();
        JTextField tfPassword = new JTextField();

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.BLACK;

        searchFieldsPanel.add(createLabeledField("Id người dùng:", tfId, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Tên người dùng:", tfTenND, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Số điện thoại:", tfSDT, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Giới tính:", tfGioiTinh, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày sinh:", tfNgaySinh, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Trạng thái:", tfTrangThai, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Email:", tfEmail, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Vai trò:", tfRole, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Tên đăng nhập:", tfUsername, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Mật khẩu:", tfPassword, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.NORTH);

        // ===== NÚT TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                switch (currentPanel) {
                    case "benhnhan":
                        loadProfileBenhNhan(tfId, tfTenND, tfSDT, tfGioiTinh, tfNgaySinh, tfTrangThai, tfEmail, tfRole, tfUsername, tfPassword);
                        break;
                    case "bacsi":
                        loadProfileBacSi(tfId, tfTenND, tfSDT, tfGioiTinh, tfNgaySinh, tfTrangThai, tfEmail, tfRole, tfUsername, tfPassword);
                        break;
                    case "duocsi":
                        loadProfileDuocSi(tfId, tfTenND, tfSDT, tfGioiTinh, tfNgaySinh, tfTrangThai, tfEmail, tfRole, tfUsername, tfPassword);
                        break;
                    case "nhanvien":
                        loadProfileNhanVien(tfId, tfTenND, tfSDT, tfGioiTinh, tfNgaySinh, tfTrangThai, tfEmail, tfRole, tfUsername, tfPassword);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Panel không xác định!");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm.");
            }
        });

        // ===== SỰ KIỆN CLICK BẢNG =====
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    showProfilePaDetails(selectedRow);
                }
            }
        });

        table.setModel(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0xCDE8E5));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadDataForRole(String role, DefaultTableModel modelToLoad) {
        try {
            // Xóa dữ liệu cũ nếu model không null
            if (modelToLoad != null) {
                modelToLoad.setRowCount(0);
            }

            Connection conn = DBConnection.getConnection();
            String sql = "SELECT ID, HOTENND, SDT, EMAIL, GIOITINH, NGAYSINH, DIACHI, ROLE, TRANGTHAI, USERNAME, PASSWORD FROM USERS WHERE ROLE = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            // Load từng dòng
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getString("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD")
                };
                modelToLoad.addRow(row);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu: " + ex.getMessage());
        }
    }
    
    private void loadProfileBenhNhan(JTextField tfId, JTextField tfTenND, JTextField tfSDT, JTextField tfGioiTinh,
     JTextField tfNgaySinh, JTextField tfTrangThai, JTextField tfEmail, JTextField tfRole, JTextField tfUsername,
      JTextField tfPassword) throws SQLException, ClassNotFoundException {
        modelBenhNhan.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ROLE = 'Bệnh nhân'");
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfId.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ID) LIKE ?");
            params.add("%" + tfId.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenND.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(HOTENND) LIKE ?");
            params.add("%" + tfTenND.getText().trim().toUpperCase() + "%");
        }
        if (!tfSDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(SDT) LIKE ?");
            params.add("%" + tfSDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfGioiTinh.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(GIOITINH) LIKE ?");
            params.add("%" + tfGioiTinh.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgaySinh.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYSINH, 'YYYY-MM-DD') = ?");
            params.add(tfNgaySinh.getText().trim());
        }
        if (!tfTrangThai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + tfTrangThai.getText().trim().toUpperCase() + "%");
        }
        if (!tfEmail.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(EMAIL) LIKE ?");
            params.add("%" + tfEmail.getText().trim().toUpperCase() + "%");
        }
        if (!tfRole.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ROLE) LIKE ?");
            params.add("%" + tfRole.getText().trim().toUpperCase() + "%");
        }
        if (!tfUsername.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(USERNAME) LIKE ?");
            params.add("%" + tfUsername.getText().trim().toUpperCase() + "%");
        }
        if (!tfPassword.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(PASSWORD) LIKE ?");
            params.add("%" + tfPassword.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelBenhNhan.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadProfileBacSi(JTextField tfId, JTextField tfTenND, JTextField tfSDT, JTextField tfGioiTinh,
     JTextField tfNgaySinh, JTextField tfTrangThai, JTextField tfEmail, JTextField tfRole, JTextField tfUsername,
      JTextField tfPassword) throws SQLException, ClassNotFoundException {
        modelBacSi.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ROLE = 'Bác sĩ'");
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfId.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ID) LIKE ?");
            params.add("%" + tfId.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenND.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(HOTENND) LIKE ?");
            params.add("%" + tfTenND.getText().trim().toUpperCase() + "%");
        }
        if (!tfSDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(SDT) LIKE ?");
            params.add("%" + tfSDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfGioiTinh.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(GIOITINH) LIKE ?");
            params.add("%" + tfGioiTinh.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgaySinh.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYSINH, 'YYYY-MM-DD') = ?");
            params.add(tfNgaySinh.getText().trim());
        }
        if (!tfTrangThai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + tfTrangThai.getText().trim().toUpperCase() + "%");
        }
        if (!tfEmail.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(EMAIL) LIKE ?");
            params.add("%" + tfEmail.getText().trim().toUpperCase() + "%");
        }
        if (!tfRole.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ROLE) LIKE ?");
            params.add("%" + tfRole.getText().trim().toUpperCase() + "%");
        }
        if (!tfUsername.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(USERNAME) LIKE ?");
            params.add("%" + tfUsername.getText().trim().toUpperCase() + "%");
        }
        if (!tfPassword.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(PASSWORD) LIKE ?");
            params.add("%" + tfPassword.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelBacSi.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadProfileDuocSi(JTextField tfId, JTextField tfTenND, JTextField tfSDT, JTextField tfGioiTinh,
     JTextField tfNgaySinh, JTextField tfTrangThai, JTextField tfEmail, JTextField tfRole, JTextField tfUsername,
      JTextField tfPassword) throws SQLException, ClassNotFoundException {
        modelDuocSi.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ROLE = 'Dược sĩ'");
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfId.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ID) LIKE ?");
            params.add("%" + tfId.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenND.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(HOTENND) LIKE ?");
            params.add("%" + tfTenND.getText().trim().toUpperCase() + "%");
        }
        if (!tfSDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(SDT) LIKE ?");
            params.add("%" + tfSDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfGioiTinh.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(GIOITINH) LIKE ?");
            params.add("%" + tfGioiTinh.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgaySinh.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYSINH, 'YYYY-MM-DD') = ?");
            params.add(tfNgaySinh.getText().trim());
        }
        if (!tfTrangThai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + tfTrangThai.getText().trim().toUpperCase() + "%");
        }
        if (!tfEmail.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(EMAIL) LIKE ?");
            params.add("%" + tfEmail.getText().trim().toUpperCase() + "%");
        }
        if (!tfRole.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ROLE) LIKE ?");
            params.add("%" + tfRole.getText().trim().toUpperCase() + "%");
        }
        if (!tfUsername.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(USERNAME) LIKE ?");
            params.add("%" + tfUsername.getText().trim().toUpperCase() + "%");
        }
        if (!tfPassword.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(PASSWORD) LIKE ?");
            params.add("%" + tfPassword.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelDuocSi.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadProfileNhanVien(JTextField tfId, JTextField tfTenND, JTextField tfSDT, JTextField tfGioiTinh,
     JTextField tfNgaySinh, JTextField tfTrangThai, JTextField tfEmail, JTextField tfRole, JTextField tfUsername,
      JTextField tfPassword) throws SQLException, ClassNotFoundException {
        modelNhanVien.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS WHERE ROLE = 'Nhân viên'");
        java.util.List<Object> params = new java.util.ArrayList<>();

        if (!tfId.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ID) LIKE ?");
            params.add("%" + tfId.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenND.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(HOTENND) LIKE ?");
            params.add("%" + tfTenND.getText().trim().toUpperCase() + "%");
        }
        if (!tfSDT.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(SDT) LIKE ?");
            params.add("%" + tfSDT.getText().trim().toUpperCase() + "%");
        }
        if (!tfGioiTinh.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(GIOITINH) LIKE ?");
            params.add("%" + tfGioiTinh.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgaySinh.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYSINH, 'YYYY-MM-DD') = ?");
            params.add(tfNgaySinh.getText().trim());
        }
        if (!tfTrangThai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + tfTrangThai.getText().trim().toUpperCase() + "%");
        }
        if (!tfEmail.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(EMAIL) LIKE ?");
            params.add("%" + tfEmail.getText().trim().toUpperCase() + "%");
        }
        if (!tfRole.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(ROLE) LIKE ?");
            params.add("%" + tfRole.getText().trim().toUpperCase() + "%");
        }
        if (!tfUsername.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(USERNAME) LIKE ?");
            params.add("%" + tfUsername.getText().trim().toUpperCase() + "%");
        }
        if (!tfPassword.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(PASSWORD) LIKE ?");
            params.add("%" + tfPassword.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelNhanVien.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void showProfilePaDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hồ sơ người dùng", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < modelBenhNhan.getColumnCount(); i++) {
            sb.append(modelBenhNhan.getColumnName(i)).append(": ")
                    .append(modelBenhNhan.getValueAt(row, i)).append("\n");
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

        for (int i = 0; i < modelBenhNhan.getColumnCount(); i++) {
            try {
                doc.insertString(doc.getLength(), modelBenhNhan.getColumnName(i) + ": ", boldAttr);  // In đậm tên cột
                doc.insertString(doc.getLength(), modelBenhNhan.getValueAt(row, i) + "\n", normalAttr); // Giá trị bình thường
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
