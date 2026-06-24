/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.menuPatient;

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

public class Notification extends JPanel {
    private String patientId;
    private Runnable onBackCallback;
    private JTable notificationTable;
    private JTextField tfMaTB, tfNoiDung, tfNgayTB, tfTrangThai, tfLoai;
    private DefaultTableModel model;

    public Notification(String patientId, Runnable onBackCallback) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        this.onBackCallback = onBackCallback;
        setLayout(new BorderLayout());
        setBackground(new Color(0,0,0,0));
        
        initComponents();
        loadNotification();
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
        JLabel title = new JLabel("DANH SÁCH THÔNG BÁO", JLabel.CENTER);
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

        JPanel topButtonsPanel = new JPanel(new BorderLayout());
        topButtonsPanel.setBackground(new Color(0xCDE8E5));
        topButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchButtonPanel.setOpaque(false);
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setPreferredSize(new Dimension(150, 30)); 
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
        
        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> {
            if (onBackCallback != null) onBackCallback.run();
        });
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x588EA7));
            }
        });
        topButtonsPanel.add(backButton, BorderLayout.WEST);

        Box centerBox = Box.createHorizontalBox();
        centerBox.add(Box.createHorizontalGlue());   // đẩy nút tìm kiếm ra giữa
        centerBox.add(btnSearch);
        centerBox.add(Box.createHorizontalGlue());   // đẩy nút tìm kiếm ra giữa

        searchButtonPanel.add(btnSearch);

        // Đặt panel chứa nút tìm kiếm vào CENTER để căn giữa
        topButtonsPanel.add(searchButtonPanel, BorderLayout.CENTER);
        
        searchPanel.add(topButtonsPanel, BorderLayout.NORTH);
        
        // ========== CÁC THÀNH PHẦN KHÁC ==========
        JPanel searchFieldsPanel = new JPanel(new GridLayout(1, 5, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));

        tfMaTB = new JTextField();
        tfNoiDung = new JTextField();
        tfNgayTB = new JTextField();
        tfTrangThai = new JTextField();
        tfLoai = new JTextField();
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.BLACK;

        searchFieldsPanel.add(createLabeledField("Mã thông báo:", tfMaTB, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Nội dung:", tfNoiDung, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày thông báo:", tfNgayTB, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Trạng thái:", tfTrangThai, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Loại thông báo:", tfLoai, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        // ===== TABLE: DANH SÁCH DỊCH VỤ =====
        String[] columns = {"Mã thông báo", "Mã bệnh nhân", "Nội dung", "Ngày thông báo", "Trạng thái thông báo", "Loại thông báo"};
        model = new DefaultTableModel(columns, 0);
        notificationTable = new JTable(model);
        notificationTable.setRowHeight(28); //chiều cao mỗi hàng 
        notificationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        notificationTable.getTableHeader().setBackground(new Color(0xCDE8E5));
        notificationTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(222, 246, 186)));
        
        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                loadNotification(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm dịch vụ.");
            }
        });


        // ===== CLICK XEM CHI TIẾT =====
        notificationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = notificationTable.rowAtPoint(e.getPoint());
                if (selectedRow != -1) {
                    try {
                        // Kiểm tra trạng thái đọc chưa
                        String trangThai = model.getValueAt(selectedRow, 4).toString(); 
                        // Thay <index_trangthai> bằng index cột trạng thái trong model

                        if ("Chưa đọc".equalsIgnoreCase(trangThai)) {
                            markAsRead(selectedRow); // Đánh dấu đã đọc
                            model.setValueAt("Đã đọc", selectedRow, 4); // Cập nhật model nếu cần
                        }

                        // Hiện chi tiết thông báo
                        showNotificationDetails(selectedRow);

                    } catch (SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });


    }
    
    private void markAsRead(int selectedRow) throws SQLException, ClassNotFoundException {
        DefaultTableModel model = (DefaultTableModel) notificationTable.getModel();

        // Lấy mã thông báo từ cột đầu tiên (index 0), bạn điều chỉnh nếu khác
        String maTB = model.getValueAt(selectedRow, 0).toString();

        // Kiểm tra nếu trạng thái đã là "Đã đọc" thì không cần cập nhật
        String currentStatus = model.getValueAt(selectedRow, 4).toString(); // Cột 4 là TRANGTHAI
        if ("Đã đọc".equals(currentStatus)) {
            return;
        }

        // Cập nhật trong TableModel
        model.setValueAt("Đã đọc", selectedRow, 4); // Cột 4 là TRANGTHAI

        // Cập nhật trong CSDL
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE THONGBAO SET TRANGTHAI = 'Đã đọc' WHERE MATB = ?"
             )) {

            stmt.setString(1, maTB);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy thông báo để cập nhật.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật trạng thái đã đọc trong cơ sở dữ liệu.");
        }

        // Cập nhật lại hàng trong bảng giao diện
        model.fireTableRowsUpdated(selectedRow, selectedRow);
    }


    public void loadNotification() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM THONGBAO WHERE USER_ID = ? ORDER BY NGAYTHONGBAO DESC");
        java.util.List<Object> params = new java.util.ArrayList<>();
        params.add(patientId);
        
        if (!tfMaTB.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MATB) LIKE ?");
            params.add("%" + tfMaTB.getText().trim().toUpperCase() + "%");
        }
        if (!tfNoiDung.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(NOIDUNG) LIKE ?");
            params.add("%" + tfNoiDung.getText().trim().toUpperCase() + "%");
        }
        if (!tfNgayTB.getText().trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYTHONGBAO, 'YYYY-MM-DD') = ?");
            params.add(tfNgayTB.getText().trim());
        }
        if (!tfTrangThai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + tfTrangThai.getText().trim().toUpperCase() + "%");
        }
        if (!tfLoai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(LOAI) LIKE ?");
            params.add("%" + tfLoai.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MATB"),
                    rs.getString("USER_ID"),
                    rs.getString("NOIDUNG"),
                    rs.getString("NGAYTHONGBAO"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("LOAI"),
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }
    

    
    private void showNotificationDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết thông báo", true);
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
            JFrame frame = new JFrame("Danh sách thông báo");
            frame.setTitle("Danh sách thông báo");
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // Co theo kích thước màn hình (đề xuất)
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height); // full màn hình

            // Tạo và thêm PersonalInfo panel
            Notification noti;
            try {
                noti = new Notification("U002", () -> {
                    System.out.println("Quay lại được gọi!");
                });
                frame.add(noti);
            } catch (SQLException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Notification.class.getName()).log(Level.SEVERE, null, ex);
            }
            //frame.add(noti);

            frame.setVisible(true);
        });
    }
}