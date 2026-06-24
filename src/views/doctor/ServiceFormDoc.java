///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package views.doctor;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.*;
//import java.sql.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.border.LineBorder;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;
//import javax.swing.text.StyledDocument;
//import utils.DBConnection;
//
//public class ServiceFormDoc extends JFrame {
//    private String doctorId;
//    private JTable serviceTable;
//    private JTextField tfMaDV, tfTenDV, tfUuDai,tfDonGia;
//    private DefaultTableModel model;
//
//    public ServiceFormDoc(String doctorId) throws SQLException, ClassNotFoundException {
//        this.doctorId = doctorId;
//        
//        setTitle("📄 Danh sách dịch vụ khám bệnh");
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        // Co theo kích thước màn hình (đề xuất)
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setSize(screenSize.width, screenSize.height); // full màn hình
//     
//        initComponents();
//        loadService();
//    }
//    
//    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
//        JPanel panel = new JPanel(new BorderLayout(5, 5));
//        panel.setBackground(new Color(0xCDE8E5)); ///////////////
//
//        JLabel label = new JLabel(labelText);
//        label.setFont(font);
//        label.setForeground(color);
//
//        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//
//        panel.add(label, BorderLayout.NORTH);
//        panel.add(textField, BorderLayout.CENTER);
//        return panel;
//    }
//
//
//    private void initComponents() {
//        setLayout(new BorderLayout(10, 10));
//        
//        // ===== TẠO PANEL CHỨA TIÊU ĐỀ VÀ THANH TÌM KIẾM =====
//        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BorderLayout());
//        
//        // TITLE
//        JPanel titlePanel = new JPanel(new BorderLayout());
//        JLabel title = new JLabel("DANH SÁCH DỊCH VỤ KHÁM BỆNH", JLabel.CENTER);
//        title.setFont(new Font("Arial", Font.BOLD, 40));
//        title.setForeground(new Color(0x78a2a7));
//        title.setBackground(new Color(0xe8faf8));
//        title.setOpaque(true);
//        titlePanel.add(title, BorderLayout.CENTER);
//
//        topPanel.add(titlePanel, BorderLayout.NORTH);
//
//        // ===== SEARCH PANEL =====
//        JPanel searchPanel = new JPanel(new BorderLayout());
//        searchPanel.setBackground(new Color(0xCDE8E5));
//        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//        // ===== PANEL CHỨA NÚT Ở GIỮA =====
//        JPanel nameSearchPanel = new JPanel(new BorderLayout());
//        nameSearchPanel.setBackground(new Color(0xCDE8E5));
//
//
//        JButton btnSearch = new JButton("Tìm kiếm");
//        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
//        btnSearch.setBackground(new Color(0x78a2a7));
//        btnSearch.setForeground(Color.WHITE);
//        btnSearch.setPreferredSize(new Dimension(300, 30));
//
//        // Hover effect
//        btnSearch.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                btnSearch.setBackground(new Color(0xff9800));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                btnSearch.setBackground(new Color(0x78a2a7));
//            }
//        });
//        
//        // === NÚT THÊM DỊCH VỤ ===
//        JButton btnAddService = new JButton("Thêm dịch vụ khám mới");
//        btnAddService.setFont(new Font("Segoe UI", Font.BOLD, 15));
//        btnAddService.setBackground(new Color(0xff9800));
//        btnAddService.setForeground(Color.WHITE);
//        btnAddService.setPreferredSize(new Dimension(250, 30));
//
//        btnAddService.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                btnAddService.setBackground(new Color(0x2B4A59));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                btnAddService.setBackground(new Color(0xff9800));
//            }
//        });
//
//        // Tạo panel chứa nút tìm kiếm và căn giữa
//        JPanel btnSearchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
//        btnSearchWrapper.setBackground(new Color(0xCDE8E5));
//        btnSearchWrapper.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0)); 
//        btnSearchWrapper.add(btnSearch);
//
//        // Thêm panel chứa nút tìm kiếm vào center
//        nameSearchPanel.add(btnSearchWrapper, BorderLayout.CENTER);
//
//        nameSearchPanel.add(btnAddService, BorderLayout.EAST);
//
//
//        searchPanel.add(nameSearchPanel, BorderLayout.NORTH);
//        
//        // ========== CÁC THÀNH PHẦN KHÁC ==========
//        JPanel searchFieldsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
//        searchFieldsPanel.setBackground(new Color(0xCDE8E5));
//
//        tfMaDV = new JTextField();
//        tfTenDV = new JTextField();
//        tfUuDai = new JTextField();
//        tfDonGia = new JTextField();
//        
//        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
//        Color labelColor = Color.BLACK;
//
//        searchFieldsPanel.add(createLabeledField("Mã dịch vụ khám:", tfMaDV, labelFont, labelColor));
//        searchFieldsPanel.add(createLabeledField("Tên dịch vụ khám:", tfTenDV, labelFont, labelColor));
//        searchFieldsPanel.add(createLabeledField("Ưu đãi dịch vụ:", tfUuDai, labelFont, labelColor));
//        searchFieldsPanel.add(createLabeledField("Đơn giá dịch vụ:", tfDonGia, labelFont, labelColor));
//
//        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
//        topPanel.add(searchPanel, BorderLayout.CENTER);
//        add(topPanel, BorderLayout.NORTH); 
//
//        // ===== TABLE: DANH SÁCH DỊCH VỤ =====
//        String[] columns = {"Mã dịch vụ khám", "Tên dịch vụ khám", "Mô tả dịch vụ khám", "Ưu đãi dịch vụ", "Đơn giá dịch vụ"};
//        model = new DefaultTableModel(columns, 0);
//        serviceTable = new JTable(model);
//        serviceTable.setRowHeight(28); //chiều cao mỗi hàng 
//        serviceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//        serviceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
//        serviceTable.getTableHeader().setBackground(new Color(0xCDE8E5));
//        serviceTable.getTableHeader().setForeground(Color.BLACK);
//
//        JScrollPane scrollPane = new JScrollPane(serviceTable);
//        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setBackground(new Color(0xD9EEF2));
//        scrollPane.setBorder(new LineBorder(new Color(222, 246, 186)));
//        
//        add(scrollPane, BorderLayout.CENTER);
//
//        // ===== SỰ KIỆN TÌM KIẾM =====
//        btnSearch.addActionListener(e -> {
//            try {
//                loadService(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
//            } catch (SQLException | ClassNotFoundException ex) {
//                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
//                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm dịch vụ.");
//            }
//        });
//
//
//        // ===== CLICK XEM CHI TIẾT =====
//        serviceTable.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int selectedRow = serviceTable.getSelectedRow();
//                if (selectedRow != -1) {
//                    showServiceDetails(selectedRow);
//                }
//            }
//        });
//    }
//
//    private void loadService() throws SQLException, ClassNotFoundException {
//        model.setRowCount(0);
//        StringBuilder sql = new StringBuilder("SELECT * FROM DICHVUKHAM WHERE 1=1");
//        java.util.List<Object> params = new java.util.ArrayList<>();
//
//        
//        if (!tfMaDV.getText().trim().isEmpty()) {
//            sql.append(" AND UPPER(MADV) LIKE ?");
//            params.add("%" + tfMaDV.getText().trim().toUpperCase() + "%");
//        }
//        if (!tfTenDV.getText().trim().isEmpty()) {
//            sql.append(" AND UPPER(TENDV) LIKE ?");
//            params.add("%" + tfTenDV.getText().trim().toUpperCase() + "%");
//        }
//        if (!tfUuDai.getText().trim().isEmpty()) {
//            sql.append(" AND UPPER(UUDAI) LIKE ?");
//            params.add("%" + tfUuDai.getText().trim().toUpperCase() + "%");
//        }
//        if (!tfDonGia.getText().trim().isEmpty()) {
//            sql.append(" AND UPPER(DONGIA) LIKE ?");
//            params.add("%" + tfDonGia.getText().trim().toUpperCase() + "%");
//        }
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                ps.setObject(i + 1, params.get(i));
//            }
//
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                model.addRow(new Object[]{
//                    rs.getString("MADV"),
//                    rs.getString("TENDV"),
//                    rs.getString("MOTADV"),
//                    rs.getString("UUDAI"),
//                    rs.getString("DONGIA"),
//                });
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
//        }
//    }
//
//    
//    private void showServiceDetails(int row) {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết dịch vụ", true);
//        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//
//        JPanel contentPanel = new JPanel();
//        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
//        contentPanel.setLayout(new BorderLayout(10, 10));
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            sb.append(model.getColumnName(i)).append(": ")
//              .append(model.getValueAt(row, i)).append("\n");
//        }
//
//        JTextPane textPane = new JTextPane();
//        textPane.setEditable(false);
//        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        textPane.setBackground(Color.WHITE);
//        textPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));;
//        
//        StyledDocument doc = textPane.getStyledDocument();
//
//        // Style in đậm cho tiêu đề cột
//        SimpleAttributeSet boldAttr = new SimpleAttributeSet();
//        StyleConstants.setBold(boldAttr, true);
//
//        // Style bình thường cho giá trị
//        SimpleAttributeSet normalAttr = new SimpleAttributeSet();
//        StyleConstants.setBold(normalAttr, false);
//
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            try {
//                doc.insertString(doc.getLength(), model.getColumnName(i) + ": ", boldAttr);  // In đậm tên cột
//                doc.insertString(doc.getLength(), model.getValueAt(row, i) + "\n", normalAttr); // Giá trị bình thường
//            } catch (BadLocationException ex) {
//                ex.printStackTrace();
//            }
//        }
//        
//        textPane.setCaretPosition(0); // Đặt con trỏ về đầu => không cuộn xuống cuối
//
//        JButton updateButton = new JButton("Cập nhật");
//            updateButton.setBackground(new Color(0xff9800)); // màu cam
//            updateButton.setForeground(Color.WHITE);
//            updateButton.addActionListener(e -> {
//                dialog.dispose();
//                showUpdateForm(
//                    model.getValueAt(row, 0).toString(), // MADV
//                    model.getValueAt(row, 1).toString(), // TENDV
//                    model.getValueAt(row, 2).toString(), // MOTADV
//                    model.getValueAt(row, 3).toString(),  // UUDAI
//                    model.getValueAt(row, 4).toString()  // DONGIA
//                );
//            });
//
//        
//        JButton closeButton = new JButton("Đóng");
//        closeButton.setBackground(new Color(0x2B4A59));
//        closeButton.setForeground(Color.WHITE);
//        closeButton.setFocusPainted(false);
//        closeButton.addActionListener(e -> dialog.dispose());
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setBackground(new Color(0xd9eef2)); // Nền pannel button cùng màu nền hộp thoại
//        buttonPanel.add(updateButton);
//        buttonPanel.add(closeButton);
//
//        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
//        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        dialog.setContentPane(contentPanel);
//        dialog.setSize(400, 300);
//        dialog.setLocationRelativeTo(this);
//        dialog.setVisible(true);
//    }
//    
//    private void updateAppointmentInfo(String id, String tenDV, String moTaDV, String uuDai, String donGia) throws ParseException {
//        String sql = "UPDATE DICHVUKHAM SET TENDV = ?, MOTADV = ?, UUDAI = ?, DONGIA = ? WHERE MADV = ?";
//        try (Connection conn = DBConnection.getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql)) {
//        
//            ps.setString(1, tenDV);
//            ps.setString(2, moTaDV);
//            ps.setString(3, uuDai);
//            ps.setString(4, donGia);
//            ps.setString(5, id);
//            ps.executeUpdate();
//        } catch (SQLException | ClassNotFoundException e) {
//            JOptionPane.showMessageDialog(this, "Lỗi cập nhật thông tin dịch vụ khám: " + e.getMessage());
//        }
//    }
//    
//    private void showUpdateForm(String id, String tenDV, String moTaDV, String uuDai, String donGia) {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật lịch hẹn", true);
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
//        JTextField tenDVField = new JTextField(tenDV);
//        JTextField moTaDVField = new JTextField(moTaDV);
//        JTextField uuDaiField = new JTextField(uuDai);
//        JTextField donGiaField = new JTextField(donGia);
//
//        inputPanel.add(new JLabel("Tên mới:"));
//        inputPanel.add(tenDVField);
//        inputPanel.add(new JLabel("Mô tả mới:"));
//        inputPanel.add(moTaDVField);
//        inputPanel.add(new JLabel("Ưu đãi mới:"));
//        inputPanel.add(uuDaiField);
//        inputPanel.add(new JLabel("Đơn giá mới:"));
//        inputPanel.add(donGiaField);
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
//        buttonPanel.setOpaque(false);
//
//        JButton saveBtn = new JButton("Lưu");
//        saveBtn.setBackground(new Color(0x2B4A59));
//        saveBtn.setForeground(Color.WHITE);
//        saveBtn.addActionListener(e -> {
//            try {
//                updateAppointmentInfo(id, tenDVField.getText(), moTaDVField.getText(), uuDaiField.getText(), donGiaField.getText());
//            } catch (ParseException ex) {
//                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            dialog.dispose();
//            reloadServicePanels();
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
//    public void reloadServicePanels() {
//        try {
//            tfMaDV.setText("");
//            tfTenDV.setText("");
//            tfUuDai.setText("");
//            tfDonGia.setText("");
//            loadService(); // gọi lại hàm load để nạp toàn bộ dịch vụ (vì các TextField đã được xóa)
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(this, "Lỗi khi tải lại danh sách dịch vụ.");
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {   
//                new ServiceFormDoc("U001").setVisible(true);
//            } catch (SQLException | ClassNotFoundException ex) {
//                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });
//    }
//}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import utils.DBConnection;

public class ServiceFormDoc extends JFrame {
    private String doctorId;
    private JTable serviceTable;
    private JTextField tfMaDV, tfTenDV, tfUuDai,tfDonGia;
    private DefaultTableModel model;

    public ServiceFormDoc(String doctorId) throws SQLException, ClassNotFoundException {
        this.doctorId = doctorId;
        
        setTitle("📄 Danh sách dịch vụ khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
     
        initComponents();
        loadService();
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
        JLabel title = new JLabel("DANH SÁCH DỊCH VỤ KHÁM BỆNH", JLabel.CENTER);
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
        JPanel nameSearchPanel = new JPanel(new BorderLayout());
        nameSearchPanel.setBackground(new Color(0xCDE8E5));


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
        
        // === NÚT THÊM DỊCH VỤ ===
        JButton btnAddService = new JButton("Thêm dịch vụ khám mới");
        btnAddService.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddService.setBackground(new Color(0xff9800));
        btnAddService.setForeground(Color.WHITE);
        btnAddService.setPreferredSize(new Dimension(250, 30));

        btnAddService.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAddService.setBackground(new Color(0x2B4A59));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAddService.setBackground(new Color(0xff9800));
            }
        });
        
        btnAddService.addActionListener(e -> showAddServiceDialog());

        // Tạo panel chứa nút tìm kiếm và căn giữa
        JPanel btnSearchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnSearchWrapper.setBackground(new Color(0xCDE8E5));
        btnSearchWrapper.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0)); 
        btnSearchWrapper.add(btnSearch);

        // Thêm panel chứa nút tìm kiếm vào center
        nameSearchPanel.add(btnSearchWrapper, BorderLayout.CENTER);

        nameSearchPanel.add(btnAddService, BorderLayout.EAST);


        searchPanel.add(nameSearchPanel, BorderLayout.NORTH);
        
        // ========== CÁC THÀNH PHẦN KHÁC ==========
        JPanel searchFieldsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));

        tfMaDV = new JTextField();
        tfTenDV = new JTextField();
        tfUuDai = new JTextField();
        tfDonGia = new JTextField();
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.BLACK;

        searchFieldsPanel.add(createLabeledField("Mã dịch vụ khám:", tfMaDV, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Tên dịch vụ khám:", tfTenDV, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ưu đãi dịch vụ:", tfUuDai, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Đơn giá dịch vụ:", tfDonGia, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        // ===== TABLE: DANH SÁCH DỊCH VỤ =====
        String[] columns = {"Mã dịch vụ khám", "Tên dịch vụ khám", "Mô tả dịch vụ khám", "Ưu đãi dịch vụ", "Đơn giá dịch vụ"};
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
                loadService(); // Gọi đúng hàm đang dùng tất cả các JTextField để lọc
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm dịch vụ.");
            }
        });


        // ===== CLICK XEM CHI TIẾT =====
        serviceTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = serviceTable.getSelectedRow();
                if (selectedRow != -1) {
                    showServiceDetails(selectedRow);
                }
            }
        });
    }

    private void loadService() throws SQLException, ClassNotFoundException {
        model.setRowCount(0);
        StringBuilder sql = new StringBuilder("SELECT * FROM DICHVUKHAM WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();

        
        if (!tfMaDV.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(MADV) LIKE ?");
            params.add("%" + tfMaDV.getText().trim().toUpperCase() + "%");
        }
        if (!tfTenDV.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(TENDV) LIKE ?");
            params.add("%" + tfTenDV.getText().trim().toUpperCase() + "%");
        }
        if (!tfUuDai.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(UUDAI) LIKE ?");
            params.add("%" + tfUuDai.getText().trim().toUpperCase() + "%");
        }
        if (!tfDonGia.getText().trim().isEmpty()) {
            sql.append(" AND UPPER(DONGIA) LIKE ?");
            params.add("%" + tfDonGia.getText().trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("MADV"),
                    rs.getString("TENDV"),
                    rs.getString("MOTADV"),
                    rs.getString("UUDAI"),
                    rs.getString("DONGIA"),
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    
    private void showServiceDetails(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết dịch vụ", true);
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

        JButton updateButton = new JButton("Cập nhật");
            updateButton.setBackground(new Color(0xff9800)); // màu cam
            updateButton.setForeground(Color.WHITE);
            updateButton.addActionListener(e -> {
                dialog.dispose();
                showUpdateForm(
                    model.getValueAt(row, 0).toString(), // MADV
                    model.getValueAt(row, 1).toString(), // TENDV
                    model.getValueAt(row, 2).toString(), // MOTADV
                    model.getValueAt(row, 3).toString(),  // UUDAI
                    model.getValueAt(row, 4).toString()  // DONGIA
                );
            });

        
        JButton closeButton = new JButton("Đóng");
        closeButton.setBackground(new Color(0x2B4A59));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xd9eef2)); // Nền pannel button cùng màu nền hộp thoại
        buttonPanel.add(updateButton);
        buttonPanel.add(closeButton);

        contentPanel.add(new JScrollPane(textPane), BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void updateAppointmentInfo(String id, String tenDV, String moTaDV, String uuDai, String donGia) throws ParseException {
        String sql = "UPDATE DICHVUKHAM SET TENDV = ?, MOTADV = ?, UUDAI = ?, DONGIA = ? WHERE MADV = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
        
            ps.setString(1, tenDV);
            ps.setString(2, moTaDV);
            ps.setString(3, uuDai);
            ps.setString(4, donGia);
            ps.setString(5, id);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật thông tin dịch vụ khám: " + e.getMessage());
        }
    }
    
    private void showUpdateForm(String id, String tenDV, String moTaDV, String uuDai, String donGia) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cập nhật lịch hẹn", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(new Color(0xd9eef2)); // Nền hộp thoại
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setOpaque(false); // để thấy màu nền từ contentPanel

        JTextField tenDVField = new JTextField(tenDV);
        JTextField moTaDVField = new JTextField(moTaDV);
        JTextField uuDaiField = new JTextField(uuDai);
        JTextField donGiaField = new JTextField(donGia);

        inputPanel.add(new JLabel("Tên mới:"));
        inputPanel.add(tenDVField);
        inputPanel.add(new JLabel("Mô tả mới:"));
        inputPanel.add(moTaDVField);
        inputPanel.add(new JLabel("Ưu đãi mới:"));
        inputPanel.add(uuDaiField);
        inputPanel.add(new JLabel("Đơn giá mới:"));
        inputPanel.add(donGiaField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton saveBtn = new JButton("Lưu");
        saveBtn.setBackground(new Color(0x2B4A59));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.addActionListener(e -> {
            try {
                updateAppointmentInfo(id, tenDVField.getText(), moTaDVField.getText(), uuDaiField.getText(), donGiaField.getText());
            } catch (ParseException ex) {
                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
            }
            dialog.dispose();
            reloadServicePanels();
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

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);

        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(contentPanel);
        dialog.setVisible(true);
    }
    
    public void reloadServicePanels() {
        try {
            tfMaDV.setText("");
            tfTenDV.setText("");
            tfUuDai.setText("");
            tfDonGia.setText("");
            loadService(); // gọi lại hàm load để nạp toàn bộ dịch vụ (vì các TextField đã được xóa)
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lại danh sách dịch vụ.");
        }
    }
    
    private void showAddServiceDialog() {
        JDialog dialog = new JDialog(this, "Thêm dịch vụ khám mới", true);
        dialog.setSize(380, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(0xCDE8E5));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(0xCDE8E5));

        JTextField tfTenMoi = new JTextField();
        JTextArea taMoTa = new JTextArea(3, 20);
        JTextField tfUuDaiMoi = new JTextField();
        JTextField tfDonGiaMoi = new JTextField();

        formPanel.add(new JLabel("Tên dịch vụ:"));
        formPanel.add(tfTenMoi);

        formPanel.add(new JLabel("Mô tả dịch vụ:"));
        formPanel.add(new JScrollPane(taMoTa));

        formPanel.add(new JLabel("Ưu đãi:"));
        formPanel.add(tfUuDaiMoi);

        formPanel.add(new JLabel("Đơn giá:"));
        formPanel.add(tfDonGiaMoi);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xCDE8E5));
        
        JButton btnAdd = new JButton("Thêm");
        btnAdd.setBackground(new Color(0xff9800));
        btnAdd.setForeground(Color.WHITE);
        
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAdd.setBackground(new Color(0x2B4A59));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAdd.setBackground(new Color(0xff9800));
            }
        });
        
        JButton btnCancel = new JButton("Hủy");
        btnCancel.setBackground(new Color(0x2B4A59));
        btnCancel.setForeground(Color.WHITE);
        
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCancel.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCancel.setBackground(new Color(0x2B4A59));
            }
        });
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Nút thêm
        btnAdd.addActionListener(e -> {
            String ten = tfTenMoi.getText().trim();
            String mota = taMoTa.getText().trim();
            String uudai = tfUuDaiMoi.getText().trim();
            String dongia = tfDonGiaMoi.getText().trim();

            if (ten.isEmpty() || dongia.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ tên và đơn giá.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                // Tạo mã tự động, ví dụ: DV001, DV002,...
                String newMa = generateAutoMaDV(conn);

                String sql = "INSERT INTO DICHVUKHAM (MADV, TENDV, MOTADV, UUDAI, DONGIA) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, newMa);
                    ps.setString(2, ten);
                    ps.setString(3, mota);
                    ps.setString(4, uudai);
                    ps.setString(5, dongia);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(dialog, "Thêm dịch vụ thành công!");
                    dialog.dispose();
                    loadService(); // Cập nhật lại bảng
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm dịch vụ: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private String generateAutoMaDV(Connection conn) throws SQLException {
        String sql = "SELECT MAX(MADV) AS MAXID FROM DICHVUKHAM";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maxId = rs.getString("MAXID");
                if (maxId != null) {
                    int num = Integer.parseInt(maxId.replaceAll("\\D", ""));
                    return String.format("DV%03d", num + 1);
                }
            }
        }
        return "DV001";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {   
                new ServiceFormDoc("U001").setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ServiceFormDoc.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}