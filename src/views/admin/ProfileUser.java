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
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import utils.DBConnection;

public class ProfileUser extends JFrame {
    private String adminId;
    private JTable serviceTable;
    private JTextField tfId, tfTenND, tfSDT, tfGioiTinh, tfNgaySinh, tfTrangThai, tfEmail, tfRole, tfUsername, tfPassword;
    private DefaultTableModel model;

    public ProfileUser(String adminId) throws SQLException, ClassNotFoundException {
        this.adminId = adminId;
        
        setTitle("📄 Danh sách người dùng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
     
        initComponents();
        loadProfilePatient();
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
            JLabel title = new JLabel("DANH SÁCH NGƯỜI DÙNG", JLabel.CENTER);
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
        JPanel searchFieldsPanel = new JPanel(new GridLayout(2, 5, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));

        tfId = new JTextField();
        tfTenND = new JTextField();
        tfSDT = new JTextField();
        tfGioiTinh = new JTextField();
        tfNgaySinh = new JTextField();
        tfTrangThai = new JTextField();
        tfEmail = new JTextField();
        tfRole = new JTextField();
        tfUsername = new JTextField();
        tfPassword = new JTextField();
        
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
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        // ===== TABLE: DANH SÁCH NGƯỜI DÙNG =====
        String[] columns = {"Id người dùng", "Tên người dùng", "Số điện thoại", "Giới tính", "Ngày sinh", "Trạng thái",
                            "Email", "Vai trò", "Tên đăng nhập", "Mật khẩu"};
        model = new DefaultTableModel(columns, 0);
        serviceTable = new JTable(model);
        serviceTable.setRowHeight(28); //chiều cao mỗi hàng 
        serviceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        serviceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        serviceTable.getTableHeader().setBackground(new Color(0xCDE8E5));
        serviceTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(serviceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(222, 246, 186)));
        
        add(scrollPane, BorderLayout.CENTER);

        // ===== SỰ KIỆN TÌM KIẾM =====
        btnSearch.addActionListener(e -> {
            try {
                loadProfilePatient(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ProfileUser.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm người dùng.");
            }
        });


        // ===== CLICK XEM CHI TIẾT =====
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow != -1) {
                    showProfilePaDetails(selectedRow);
                }
            }
        });
    }

    private void loadProfilePatient() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM USERS U "
                                              + "WHERE 1=1");
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

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("GIOITINH"),
                    rs.getDate("NGAYSINH"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("EMAIL"),
                    rs.getString("ROLE"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD"),
                });
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
                new ProfileUser("U002").setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ProfileUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}




