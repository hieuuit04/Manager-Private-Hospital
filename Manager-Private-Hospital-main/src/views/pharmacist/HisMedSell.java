package views.pharmacist;

import utils.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.TableCellRenderer;
import utils.MailSender;

public class HisMedSell extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnUpdate;
    private JTextField txtSearch;
    private String mads; // Mã dược sĩ đăng nhập
    private JButton btnSendAllUnpaid;

    public HisMedSell(String mads) {
        this.mads = mads;
        setTitle("Lịch sử bán thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(43, 74, 160));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel title = new JLabel("LỊCH SỬ BÁN THUỐC", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.CENTER);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        searchPanel.setBackground(new Color(43, 74, 160));
        
        JLabel lblSearch = new JLabel("Tìm kiếm mã bệnh nhân:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(Color.WHITE);
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(200, 35));
        
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBackground(new Color(255, 255, 255));
        btnSearch.setForeground(new Color(43, 74, 160));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(255, 255, 255));
        btnRefresh.setForeground(new Color(43, 74, 160));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        headerPanel.add(searchPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {
            "Mã đơn thuốc", "Mã dược sĩ", "Mã bác sĩ khám", "Mã bệnh nhân", "Giới tính", "Ngày sinh",
            "Lịch sử bệnh lý", "Dị ứng", "Đơn thuốc", "Ghi chú", "Ngày bán", "Thành tiền", "Trạng thái", "Gửi mail"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 13;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setGridColor(new Color(234, 234, 234));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(43, 74, 160));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Status column renderer
        table.getColumnModel().getColumn(12).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value != null ? value.toString() : "";
                if ("Đã thanh toán".equalsIgnoreCase(status)) {
                    c.setForeground(new Color(0, 128, 0));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(Color.RED);
                }
                return c;
            }
        });
        // Button in table
        table.getColumnModel().getColumn(13).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(13).setCellEditor(new ButtonEditor(new JCheckBox()));


        // Double click listener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        showRowDetails(row);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        btnUpdate = new JButton("Thêm thông tin");
        btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(43, 74, 160));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(e -> openUpdateDialog());

        JButton btnAddPrescription = new JButton("Thêm đơn thuốc");
        btnAddPrescription.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddPrescription.setBackground(new Color(0, 153, 76));
        btnAddPrescription.setForeground(Color.WHITE);
        btnAddPrescription.setFocusPainted(false);
        btnAddPrescription.setBorderPainted(false);
        btnAddPrescription.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddPrescription.addActionListener(e -> openAddPrescriptionDialog());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnAddPrescription); // Thêm nút mới

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        


        // Add search functionality
        btnSearch.addActionListener(e -> searchPatient());
        btnRefresh.addActionListener(e -> loadData());

        loadData();

        // Panel chứa các nút điều khiển
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));


    }

    private void searchPatient() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM DONTHUOC_DONTHUOCYC WHERE LOWER(MABN) LIKE ? ORDER BY MADT DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + searchText + "%");
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData meta = rs.getMetaData();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        Object val = rs.getObject(i);
                        if (val instanceof java.sql.Date) {
                            row.add(val != null ? sdf.format(val) : "");
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
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + ex.getMessage());
        }
    }

    private void loadData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                            row.add(val != null ? sdf.format(val) : "");
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

    private void showRowDetails(int row) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);
            Object value = tableModel.getValueAt(row, i);

            JLabel lbl = new JLabel(columnName + ":");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setForeground(Color.DARK_GRAY);

            JTextField tf = new JTextField(value != null ? value.toString() : "");
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tf.setEditable(false);
            tf.setBackground(Color.WHITE);

            panel.add(lbl);
            panel.add(tf);
        }

        JDialog detailDialog = new JDialog(this, "Chi tiết đơn thuốc", true);
        detailDialog.getContentPane().setBackground(Color.WHITE);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.add(panel, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Đóng");
        closeBtn.setBackground(new Color(43, 74, 160));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.addActionListener(e -> detailDialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(closeBtn);
        detailDialog.add(btnPanel, BorderLayout.SOUTH);

        detailDialog.setSize(700, 600);
        detailDialog.setLocationRelativeTo(this);
        detailDialog.setVisible(true);
    }

    private void openUpdateDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn thuốc để cập nhật!");
            return;
        }

        String madt = String.valueOf(tableModel.getValueAt(row, 0));
        String mabs = String.valueOf(tableModel.getValueAt(row, 2));
        String ghichu = String.valueOf(tableModel.getValueAt(row, 9));
        String ngayban = String.valueOf(tableModel.getValueAt(row, 10));
        String thanhtien = String.valueOf(tableModel.getValueAt(row, 11));
        String trangthai = String.valueOf(tableModel.getValueAt(row, 12));
        boolean isPaid = "Đã thanh toán".equalsIgnoreCase(trangthai);

        JTextField tfMABS = new JTextField(mabs.equals("null") ? "" : mabs, 15);
        JTextField tfGhiChu = new JTextField(ghichu.equals("null") ? "" : ghichu, 15);
        JTextField tfNgayBan = new JTextField(ngayban.equals("null") ? "" : ngayban, 15);
        JTextField tfThanhTien = new JTextField(thanhtien.equals("null") ? "" : thanhtien, 15);
        tfThanhTien.setEditable(false);

        String[] trangThaiArr = {"Chưa thanh toán", "Đã thanh toán"};
        JComboBox<String> cbTrangThai = new JComboBox<>(trangThaiArr);
        cbTrangThai.setSelectedItem(trangthai);
        cbTrangThai.setPreferredSize(new Dimension(200, 30));

        cbTrangThai.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if ("Đã thanh toán".equals(value)) {
                    c.setBackground(new Color(144, 238, 144)); // xanh lá
                    if (isSelected) c.setBackground(new Color(100, 200, 100));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel[] labels = {
            new JLabel("Mã bác sĩ:"), new JLabel("Ghi chú:"),
            new JLabel("Ngày bán (yyyy-MM-dd):"), new JLabel("Thành tiền:"),
            new JLabel("Trạng thái:")
        };
        for (JLabel lbl : labels) lbl.setFont(labelFont);

        JTextField[] fields = {tfMABS, tfGhiChu, tfNgayBan, tfThanhTien};
        for (JTextField f : fields) {
            f.setFont(fieldFont);
            f.setPreferredSize(new Dimension(250, 30));
        }

        Component[] components = {tfMABS, tfGhiChu, tfNgayBan, tfThanhTien, cbTrangThai};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            form.add(labels[i], gbc);
            gbc.gridx = 1;
            form.add(components[i], gbc);
        }

        JButton btnCapNhat = new JButton("Thêm thông tin");
        btnCapNhat.setBackground(new Color(43, 74, 160));
        btnCapNhat.setForeground(Color.WHITE);
        btnCapNhat.setFont(labelFont);

        JButton btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setBackground(new Color(0, 123, 255));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFont(labelFont);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnXemChiTiet);

        // Khóa chỉnh sửa nếu đã thanh toán
        if (isPaid) {
            tfMABS.setEditable(false);
            tfGhiChu.setEditable(false);
            tfNgayBan.setEditable(false);
            cbTrangThai.setEnabled(false);
            btnCapNhat.setEnabled(false);
            btnCapNhat.setToolTipText("Đơn thuốc đã thanh toán không thể sửa.");
        }

        JDialog dialog = new JDialog(this, "Cập nhật đơn thuốc", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Hiển thị ảnh đơn thuốc nếu có
        ImageIcon imageIcon = getPrescriptionImage(madt);
        if (imageIcon != null) {
            JLabel lblImage = new JLabel(imageIcon);
            lblImage.setBorder(BorderFactory.createTitledBorder("Ảnh đơn thuốc"));
            dialog.add(lblImage, BorderLayout.EAST);
        }

        // Cập nhật thông tin
        btnCapNhat.addActionListener(e -> {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE DONTHUOC_DONTHUOCYC SET MADS=?, MABS=?, GHICHU=?, NGAYBAN=?, THANHTIEN=?, TRANGTHAITT=? WHERE MADT=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, mads);
                    ps.setString(2, tfMABS.getText().trim());
                    ps.setString(3, tfGhiChu.getText().trim());

                    if (tfNgayBan.getText().trim().isEmpty()) {
                        ps.setNull(4, Types.DATE);
                    } else {
                        ps.setDate(4, java.sql.Date.valueOf(tfNgayBan.getText().trim().substring(0, 10)));
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

        // Mở chi tiết đơn thuốc
        btnXemChiTiet.addActionListener(e -> {
            dialog.dispose();
            CTDT ctdt = new CTDT(madt, () -> {
                loadData(); // callback sau khi xem CTDT
            });
            ctdt.setVisible(true);
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private ImageIcon getPrescriptionImage(String madt) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT FILEDONTHUOC FROM DONTHUOC_DONTHUOCYC WHERE MADT=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, madt);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Blob blob = rs.getBlob("FILEDONTHUOC");
                    if (blob != null) {
                        byte[] bytes = blob.getBytes(1, (int) blob.length());
                        ImageIcon icon = new ImageIcon(bytes);
                        Image scaledImage = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải ảnh: " + ex.getMessage());
        }
        return null;
    }
    // Renderer cho nút gửi
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean focus, int row, int col) {
            setText(v == null ? "Gửi": v.toString());
            setBackground(new Color(30, 144, 255));
            setForeground(Color.WHITE);
            return this;
        }
    }

    // Editor cho nút gửi
    class ButtonEditor extends DefaultCellEditor {
        private JButton btn;
        private boolean clicked;
        private int selRow;

        public ButtonEditor(JCheckBox chk) {
            super(chk);
            btn = new JButton();
            btn.setOpaque(true);
            btn.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
            btn.setText(v == null ? "Gửi" : v.toString());
            btn.setBackground(new Color(30, 144, 255));
            btn.setForeground(Color.WHITE);
            clicked = true;
            selRow = row;
            return btn;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                sendEmailProcess(selRow);
            }
            clicked = false;
            return btn.getText();
        }
    }

private void sendEmailProcess(int row) {
    String mabn = table.getValueAt(row, 3).toString(); // Mã bệnh nhân
    String madt = table.getValueAt(row, 0).toString(); // Mã đơn thuốc
    String trangThai = table.getValueAt(row, 12).toString(); // Trạng thái

    // ✅ Không gửi nếu đã thanh toán
    if ("Đã thanh toán".equalsIgnoreCase(trangThai)) {
        JOptionPane.showMessageDialog(this,
                "Đơn thuốc đã thanh toán, không thể gửi mail.",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // ✅ Lấy email
    String email = lookupEmail(mabn);
    if (email == null || email.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this,
                "Không tìm thấy email bệnh nhân " + mabn,
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

            String content = "Kính gửi bệnh nhân " + mabn + ",\n\n"
                    + "Đơn thuốc mã số " + madt + " hiện chưa được xử lý và thanh toán.\n"
                    + "Vui lòng hoàn tất thủ tục nhận thuốc.\n\n"
                    + "Trân trọng,\n"
                    + "Bệnh viện tư Healink\n"
                    + "Địa chỉ: Khu phố 6, phường Linh Trung, Tp.Thủ Đức, Tp.Hồ Chí Minh\n"
                    + "Điện thoại: (0123) 456 789\n"
                    + "Email: contactBVTHealink@gmail.com\n"
                    + "Website: www.benhvientuHealink.vn\n"
                    + "Facebook: fb.com/benhvientuHealink";

    // ✅ Tạo khung hiển thị nội dung to hơn
    JTextArea ta = new JTextArea(content);
    ta.setWrapStyleWord(true);
    ta.setLineWrap(true);
    ta.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(ta);
    scrollPane.setPreferredSize(new Dimension(500, 300)); // 👈 chỉnh kích thước cửa sổ xác nhận

    // ✅ Hiển thị hộp thoại xác nhận
    int res = JOptionPane.showOptionDialog(this, scrollPane,
            "Xác nhận gửi email đến: " + email,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, new Object[]{"Xác nhận gửi", "Hủy"}, "Xác nhận gửi");

    if (res == JOptionPane.YES_OPTION) {
        boolean ok = MailSender.send(email, "Thông báo đơn thuốc " + madt, content);
        JOptionPane.showMessageDialog(this,
                ok ? "Đã gửi email tới " + email : "Lỗi khi gửi mail",
                ok ? "Thành công" : "Lỗi",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }
}

    private String lookupEmail(String mabn) {
        try (Connection c = DBConnection.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT EMAIL FROM USERS WHERE ID=?");
            ps.setString(1, mabn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("EMAIL");
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
private String getNextMADT() {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT MAX(MADT) FROM DONTHUOC_DONTHUOCYC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maxMADT = rs.getString(1);
                if (maxMADT == null) return "1";
                // Nếu MADT là số
                try {
                    int next = Integer.parseInt(maxMADT) + 1;
                    return String.valueOf(next);
                } catch (NumberFormatException e) {
                    // Nếu MADT có tiền tố, ví dụ: DT001, DT002...
                    String number = maxMADT.replaceAll("\\D+", "");
                    int next = Integer.parseInt(number) + 1;
                    return "DT" + String.format("%03d", next);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "1";
}


private void openAddPrescriptionDialog() {
    JTextField tfMADT = new JTextField(getNextMADT(), 15);
    tfMADT.setEditable(false);
    JComboBox<String> cbMABS = createDoctorComboBox();
    JTextField tfGioiTinh = new JTextField(5);
    JTextField tfNgaySinh = new JTextField(10);
    JTextField tfLichSuBenhLy = new JTextField(30);
    JComboBox<String> cbMABN = createPatientComboBox(tfGioiTinh, tfNgaySinh, tfLichSuBenhLy);
    JTextField tfDiUng = new JTextField(30);
    JTextField tfGhiChu = new JTextField(30);
    JTextField tfNgayBan = new JTextField(10);
    JTextField tfThanhTien = new JTextField(10);

    String[] trangThaiArr = {"Chưa thanh toán", "Đã thanh toán"};
    JComboBox<String> cbTrangThai = new JComboBox<>(trangThaiArr);

    JPanel panel = new JPanel(new GridLayout(0,2,10,10));
    panel.add(new JLabel("Mã đơn thuốc:")); panel.add(tfMADT);
    panel.add(new JLabel("Mã bác sĩ:")); panel.add(cbMABS);
    panel.add(new JLabel("Bệnh nhân:")); panel.add(cbMABN);
    panel.add(new JLabel("Giới tính:")); panel.add(tfGioiTinh);
    panel.add(new JLabel("Ngày sinh (yyyy-MM-dd):")); panel.add(tfNgaySinh);
    panel.add(new JLabel("Lịch sử bệnh lý:")); panel.add(tfLichSuBenhLy);
    panel.add(new JLabel("Dị ứng:")); panel.add(tfDiUng);
    panel.add(new JLabel("Ghi chú:")); panel.add(tfGhiChu);
    panel.add(new JLabel("Ngày bán (yyyy-MM-dd):")); panel.add(tfNgayBan);
    panel.add(new JLabel("Thành tiền:")); panel.add(tfThanhTien);
    panel.add(new JLabel("Trạng thái:")); panel.add(cbTrangThai);

    int res = JOptionPane.showConfirmDialog(this, panel, "Thêm đơn thuốc mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (res == JOptionPane.OK_OPTION) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO DONTHUOC_DONTHUOCYC (MADT, MADS, MABS, MABN, GIOITINHBN, NGAYSINHBN, LICHSU_BENHLY_BN, DIUNGBN, GHICHU, NGAYBAN, THANHTIEN, TRANGTHAITT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tfMADT.getText().trim());
                ps.setString(2, mads); // Tự động lấy mã dược sĩ đăng nhập
                String selectedDoctor = (String) cbMABS.getSelectedItem();
                String mabs = selectedDoctor.split(" - ")[0]; // Lấy MABS
                ps.setString(3, mabs);
                String selectedPatient = (String) cbMABN.getSelectedItem();
                String mabn = selectedPatient.split(" - ")[0]; // Lấy MABN
                ps.setString(4, mabn);
                ps.setString(5, tfGioiTinh.getText().trim());
                if (tfNgaySinh.getText().trim().isEmpty()) ps.setNull(6, Types.DATE);
                else ps.setDate(6, java.sql.Date.valueOf(tfNgaySinh.getText().trim()));
                ps.setString(7, tfLichSuBenhLy.getText().trim());
                ps.setString(8, tfDiUng.getText().trim());
                ps.setString(9, tfGhiChu.getText().trim());
                if (tfNgayBan.getText().trim().isEmpty()) ps.setNull(10, Types.DATE);
                else ps.setDate(10, java.sql.Date.valueOf(tfNgayBan.getText().trim()));
                if (tfThanhTien.getText().trim().isEmpty()) ps.setNull(11, Types.NUMERIC);
                else ps.setDouble(11, Double.parseDouble(tfThanhTien.getText().trim()));
                ps.setString(12, cbTrangThai.getSelectedItem().toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm đơn thuốc thành công!");
                loadData();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm đơn thuốc: " + ex.getMessage());
        }
    }
}

class PatientInfo {
    String mabn, hoten, sobhyt, gioitinh, ngaysinh, lichsu;
    PatientInfo(String mabn, String hoten, String sobhyt, String gioitinh, String ngaysinh, String lichsu) {
        this.mabn = mabn; this.hoten = hoten; this.sobhyt = sobhyt;
        this.gioitinh = gioitinh; this.ngaysinh = ngaysinh; this.lichsu = lichsu;
    }
}
private JComboBox<String> createDoctorComboBox() {
    JComboBox<String> cb = new JComboBox<>();
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT B.MABS, U.HOTENND, B.CHUYENKHOA " +
                     "FROM BACSI B JOIN USERS U ON B.MABS = U.ID";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String mabs = rs.getString("MABS");
                String hoten = rs.getString("HOTENND");
                String chuyenKhoa = rs.getString("CHUYENKHOA");
                cb.addItem(mabs + " - " + hoten + " - " + chuyenKhoa);
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return cb;
}
// 2. Sửa hàm tạo comboBox
private Map<String, PatientInfo> patientMap = new HashMap<>();
private JComboBox<String> createPatientComboBox(JTextField tfGioiTinh, JTextField tfNgaySinh, JTextField tfLichSu) {
    JComboBox<String> cb = new JComboBox<>();
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT B.MABN, U.HOTENND, B.SOBHYT, U.GIOITINH, U.NGAYSINH, B.LICHSU_BENHLY " +
                     "FROM BENHNHAN B JOIN USERS U ON B.MABN = U.ID";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String mabn = rs.getString("MABN");
                String hoten = rs.getString("HOTENND");
                String sobhyt = rs.getString("SOBHYT");
                String gioitinh = rs.getString("GIOITINH");
                Date ngaysinh = rs.getDate("NGAYSINH");
                String lichsu = rs.getString("LICHSU_BENHLY");
                String ngaysinhStr = (ngaysinh != null) ? ngaysinh.toString() : "";
                String display = mabn + " - " + hoten + " - " + sobhyt;
                cb.addItem(display);
                patientMap.put(display, new PatientInfo(mabn, hoten, sobhyt, gioitinh, ngaysinhStr, lichsu));
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
   // 3. Thêm sự kiện chọn để tự động set các trường
    cb.addActionListener(e -> {
        String selected = (String) cb.getSelectedItem();
        PatientInfo info = patientMap.get(selected);
        if (info != null) {
            tfGioiTinh.setText(info.gioitinh != null ? info.gioitinh : "");
            tfNgaySinh.setText(info.ngaysinh != null ? info.ngaysinh : "");
            tfLichSu.setText(info.lichsu != null ? info.lichsu : "");
        }
    });

    return cb;
}
}
