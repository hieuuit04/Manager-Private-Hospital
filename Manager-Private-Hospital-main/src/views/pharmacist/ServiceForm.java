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

public class ServiceForm extends JFrame {
    private String patientId;
    private JTable serviceTable;
    private JTextField tfMaDV, tfTenDV, tfUuDai,tfDonGia;
    private DefaultTableModel model;

    public ServiceForm(String patientId) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        
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
                Logger.getLogger(ServiceForm.class.getName()).log(Level.SEVERE, null, ex);
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
                new ServiceForm("U002").setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(ServiceForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServiceForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}


