/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.admin;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.awt.*;
import java.awt.event.*;
import java.security.Timestamp;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HisAppointAdmin extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    
    public HisAppointAdmin() throws SQLException, ClassNotFoundException {
        
        setTitle("📄 Danh sách lịch hẹn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());

        // ===== RIGHT PANEL (Card Layout) =====
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel appointmentMainPanel = createAppointmentMainPanel();
        JPanel scheduleFormPanel = createScheduleFormPanel();

        contentPanel.add(appointmentMainPanel, "main");
        contentPanel.add(scheduleFormPanel, "schedule");

        add(contentPanel, BorderLayout.CENTER);
    }

    private List<String[]> getAppointmentsByStatus(String statusCondition) throws SQLException, ClassNotFoundException {
        List<String[]> result = new ArrayList<>();

        String sql = "SELECT * FROM LICHHEN WHERE TRANGTHAI " + statusCondition;

        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = {
                        rs.getString("MALICH"),
                        rs.getString("MABN"),
                        rs.getString("MABS"),
                        rs.getString("NGAYDAT"),
                        rs.getString("NGAYHEN"),
                        rs.getString("DIADIEM"),
                        rs.getString("TRIEUCHUNG"),
                        rs.getString("TRANGTHAI")
                    };

                    result.add(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi truy vấn lịch hẹn: " + e.getMessage());
        }

        return result; 
    }


    private JPanel createAppointmentMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tiêu đề
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("LỊCH HẸN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0x588EA7));
        title.setBackground(new Color(0xd6eaed));
        title.setOpaque(true);
        topPanel.add(title, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);


        // Nội dung chính
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
        JTextField ngayHenField = new JTextField(10);
        ngayHenField.setFont(new Font("Arial", Font.PLAIN, 14));
        JTextField mabnField = new JTextField(10);
        mabnField.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] trangThaiOptions = {"Tất cả", "Chờ xác nhận", "Thành công", "Đã hủy", "Bị từ chối"};
        JComboBox<String> trangThaiBox = new JComboBox<>(trangThaiOptions);
        trangThaiBox.setFont(new Font("Arial", Font.PLAIN, 14));
        trangThaiBox.setBackground(Color.WHITE);

        JButton searchButton = new JButton("Tìm kiếm");
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setBackground(new Color(0x2B4A59));
        searchButton.setForeground(Color.WHITE);
        
        JButton backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(0x2B4A59));
        backButton.setForeground(Color.WHITE);
        backButton.setVisible(false); // Ẩn ban đầu

        backButton.addActionListener(ev -> {
            content.removeAll();
            content.add(searchPanel);
            content.add(Box.createVerticalStrut(20));
            
            try {
                List<String[]> pending = getAppointmentsByStatus("= 'Chờ xác nhận'");
                List<String[]> history = getAppointmentsByStatus("IN ('Thành công', 'Đã hủy', 'Bị từ chối')");

                content.add(createTableSection("Lịch hẹn chờ xác nhận", pending.toArray(new String[0][])));
                content.add(Box.createVerticalStrut(30));
                content.add(createTableSection("Lịch sử lịch hẹn", history.toArray(new String[0][])));

                content.revalidate();
                content.repaint();
                backButton.setVisible(false); // Ẩn lại nút quay lại
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải lại lịch hẹn: " + ex.getMessage());
            }
        });
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x2B4A59));
            }
        });

        searchButton.addActionListener(e -> {
            String keyword = keywordField.getText().trim();
            String ngayHen = ngayHenField.getText().trim();
            String mabn = mabnField.getText().trim();
            String trangThai = trangThaiBox.getSelectedItem().toString();
            

            try {
                List<String[]> result = searchAdvancedAppointments(keyword, ngayHen, mabn, trangThai);

                content.removeAll();
                content.add(searchPanel);
                content.add(Box.createVerticalStrut(20));
                content.add(createTableSection("Kết quả tìm kiếm", result.toArray(new String[0][])));
                backButton.setVisible(true);


                content.revalidate();
                content.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi tìm kiếm nâng cao: " + ex.getMessage());
            }
        });
        
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0xff9800));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(new Color(0x588EA7));
            }
        });
        
        
        // Giao diện hàng ngang
        Font labelFont = new Font("Arial", Font.BOLD, 14); 

        JLabel tuKhoaLabel = new JLabel("Từ khóa:");
        tuKhoaLabel.setFont(labelFont);
        gbc.gridx = 0; gbc.gridy = 0; searchPanel.add(tuKhoaLabel, gbc);

        gbc.gridx = 1; searchPanel.add(keywordField, gbc);

        JLabel ngayHenLabel = new JLabel("Ngày hẹn:");
        ngayHenLabel.setFont(labelFont);
        gbc.gridx = 2; searchPanel.add(ngayHenLabel, gbc);

        gbc.gridx = 3; searchPanel.add(ngayHenField, gbc);

        JLabel maBNLabel = new JLabel("Mã bệnh nhân:");
        maBNLabel.setFont(labelFont);
        gbc.gridx = 4; searchPanel.add(maBNLabel, gbc);

        gbc.gridx = 5; searchPanel.add(mabnField, gbc);

        JLabel trangThaiLabel = new JLabel("Trạng thái:");
        trangThaiLabel.setFont(labelFont);
        gbc.gridx = 6; searchPanel.add(trangThaiLabel, gbc);

        gbc.gridx = 7; searchPanel.add(trangThaiBox, gbc);

        gbc.gridx = 8; searchPanel.add(searchButton, gbc);
        gbc.gridx = 9; searchPanel.add(backButton, gbc);
        
        content.add(searchPanel);
        content.add(Box.createVerticalStrut(20));

        try {
            // Lấy dữ liệu lịch hẹn theo trạng thái
            List<String[]> pending = getAppointmentsByStatus("= 'Chờ xác nhận'");
            List<String[]> history = getAppointmentsByStatus("IN ('Thành công', 'Đã hủy', 'Bị từ chối')");

            // Tạo bảng hiển thị
            content.add(createTableSection("Lịch hẹn chờ xác nhận", pending.toArray(new String[0][])));
            content.add(Box.createVerticalStrut(30));
            content.add(createTableSection("Lịch sử lịch hẹn", history.toArray(new String[0][])));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải lịch hẹn: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(content);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private List<String[]> searchAdvancedAppointments(String keyword, String ngayHen, String mabn, String trangThai)
        throws SQLException, ClassNotFoundException {
        List<String[]> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM LICHHEN WHERE 1=1");
        List<String> params = new ArrayList<>();
        
        if (!keyword.isEmpty()) {
            sql.append(" AND (LOWER(MALICH) LIKE ? OR LOWER(MABN) LIKE ? OR LOWER(NGAYDAT) LIKE ? OR LOWER(NGAYHEN) LIKE ? OR LOWER(DIADIEM) LIKE ? OR LOWER(TRIEUCHUNG) LIKE ?)");
            for (int i = 0; i < 6; i++) params.add("%" + keyword.toLowerCase() + "%");
        }

        if (!ngayHen.isEmpty()) {
            sql.append(" AND NGAYHEN = ?");
            params.add(ngayHen); // định dạng yyyy-MM-dd
        }

        if (!mabn.isEmpty()) {
            sql.append(" AND LOWER(MABN) LIKE ?");
            params.add("%" + mabn.toLowerCase() + "%");
        }

        if (!trangThai.equals("Tất cả")) {
            sql.append(" AND TRANGTHAI = ?");
            params.add(trangThai);
        }
        
        System.out.println("SQL : " + sql);
        System.out.println("Params : " + params);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = {
                        rs.getString("MALICH"),
                        rs.getString("MABN"),
                        rs.getString("MABS"),
                        rs.getString("NGAYDAT"),
                        rs.getString("NGAYHEN"),
                        rs.getString("DIADIEM"),
                        rs.getString("TRIEUCHUNG"),
                        rs.getString("TRANGTHAI")
                    };
                    result.add(row);
                }
            }
        }
        return result;
    }

    private JPanel createScheduleFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Giao diện đặt lịch (điền thông tin tại đây)", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.CENTER);

        JButton backBtn = new JButton("Quay lại");
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "main"));
        panel.add(backBtn, BorderLayout.SOUTH);

        return panel;
    }
    
    private void sendAppointmentStatusEmail(String patientEmail, String patientName, String appointmentId, String status) {
        String subject = "Lịch hẹn đã **" + status + "**.\n" + appointmentId;
        String messageText = "Kính chào " + patientName + ",\n\n"
                + "Lịch hẹn của bạn (Mã: " + appointmentId + ") đã **" + status + "**.\n"
                + "Vui lòng đăng nhập hệ thống để xem chi tiết.\n\n"
                + "----------------------------\n"
                + "Bệnh viện tư Healink\n"
                + "Địa chỉ: Khu phố 6, phường Linh Trung, Tp.Thủ Đức, Tp.Hồ Chí Minh\n"
                + "Điện thoại: (0123) 456 789\n"
                + "Email: contactBVTHealink@gmail.com\n"
                + "Website: www.benhvientuHealink.vn\n" 
                + "Facebook: fb.com/benhvientuHealink\n\n" 
                + "Trân trọng cảm ơn quý bệnh nhân đã tin tưởng và sử dụng dịch vụ của chúng tôi.";

        try {
            EmailSender sender = new EmailSender(); // tạo đối tượng
            sender.sendEmail(patientEmail, subject, messageText); // gọi phương thức
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể gửi email thông báo: " + ex.getMessage());
        }
    }
   
    
    private void updateAppointmentStatus(String id, String newStatus) {
        String sqlUpdate = "UPDATE LICHHEN SET TRANGTHAI = ? WHERE MALICH = ?";
        String sqlInfo = "SELECT U.EMAIL, U.HOTENND, L.NGAYHEN FROM LICHHEN L " +
                         "JOIN USERS U ON L.MABN = U.ID WHERE L.MALICH = ?";
        String sqlNotify = "INSERT INTO THONGBAO (MATB, USER_ID, NOIDUNG, LOAI) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
             PreparedStatement psInfo = conn.prepareStatement(sqlInfo)) {

            // 1. Cập nhật trạng thái
            psUpdate.setString(1, newStatus);
            psUpdate.setString(2, id);
            psUpdate.executeUpdate();

            // 2. Lấy thông tin bệnh nhân và lịch hẹn
            psInfo.setString(1, id);
            try (ResultSet rs = psInfo.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("EMAIL");
                    String name = rs.getString("HOTENND");
                    java.sql.Timestamp ngayHen = rs.getTimestamp("NGAYHEN");

                    // 3. Thêm thông báo hệ thống
                    String noidung = "Lịch hẹn vào ngày " + ngayHen.toLocalDateTime().toString().replace("T", " ") +
                                     " của bạn đã được cập nhật trạng thái thành: " + newStatus;
                    String notificationId = generateNotificationId(conn); // Hàm tự viết để tạo ID thông báo

                    try (PreparedStatement psNotify = conn.prepareStatement(sqlNotify)) {
                        psNotify.setString(1, notificationId);
                        psNotify.setString(2, getPatientIdByAppointmentId(conn, id)); // lấy ID bệnh nhân từ MALICH
                        psNotify.setString(3, noidung);
                        psNotify.setString(4, "Lịch hẹn");
                        psNotify.executeUpdate();
                    }

                    // 4. Gửi email nếu có
                    if (email != null && !email.isEmpty()) {
                        String subject = "Cập nhật trạng thái lịch hẹn khám bệnh";
                        String message = "Xin chào " + name + ",\n\n" +
                                         "Lịch hẹn của bạn vào lúc " + ngayHen.toLocalDateTime().toString().replace("T", " ") +
                                         " đã được cập nhật trạng thái thành: " + newStatus + ".\n\n" +
                                         "Nếu có thắc mắc, vui lòng liên hệ bệnh viện để được hỗ trợ.\n\n" +
                                         "Trân trọng.\n\n------------------------\n" +
                                         "Bệnh viện tư Healink\n" +
                                         "Địa chỉ: Khu phố 6, phường Linh Trung, Tp.Thủ Đức, Tp.Hồ Chí Minh\n" +
                                         "Điện thoại: (0123) 456 789\n" +
                                         "Email: contactBVTHealink@gmail.com\n" +
                                         "Website: www.benhvientuHealink.vn\n" +
                                         "Facebook: fb.com/benhvientuHealink";

                        try {
                            new EmailSender().sendEmail(email, subject, message);
                            System.out.println("Đã gửi email cập nhật trạng thái cho: " + email);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println("Không thể gửi email.");
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái thành công.");
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    private String getPatientIdByAppointmentId(Connection conn, String appointmentId) throws SQLException {
        String sql = "SELECT MABN FROM LICHHEN WHERE MALICH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("MABN");
                }
            }
        }
        return null;
    }
        
    private String generateNotificationId(Connection conn){
         String prefix = "TB";
        String sql = "SELECT MAX(MATB) FROM THONGBAO";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null) {
                    int num = Integer.parseInt(lastId.replace(prefix, ""));
                    return prefix + String.format("%03d", num + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + "001"; // Nếu chưa có lịch hẹn nào
    }
   
    private void reloadAppointmentPanels() {
        contentPanel.removeAll();
        JPanel newMain = createAppointmentMainPanel();
        contentPanel.add(newMain, "main");
        cardLayout.show(contentPanel, "main");
        revalidate();
        repaint();
    }
    
    private void deleteAppointment(String id) {
        String sql = "DELETE FROM LICHHEN WHERE MALICH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Xóa lịch hẹn thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn để xóa.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa lịch hẹn: " + e.getMessage());
        }
    }

    private void showProductDetails(JTable table, int row) {
        String status = table.getValueAt(row, 7).toString();
        boolean isPending = status.equalsIgnoreCase("Chờ xác nhận");
        boolean isDeletable = status.equalsIgnoreCase("Đã hủy")
                       || status.equalsIgnoreCase("Thành công")
                       || status.equalsIgnoreCase("Bị từ chối");

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết lịch hẹn", true);
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

        if (isPending) {
            JButton cancelButton = new JButton("Từ chối");
            cancelButton.setBackground(new Color(215, 86, 86));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.addActionListener(e -> {
                String id = table.getValueAt(row, 0).toString();
                updateAppointmentStatus(id, "Bị từ chối");
                dialog.dispose();
                reloadAppointmentPanels();
            });
            
            JButton confirmButton = new JButton("Xác nhận");
            confirmButton.setBackground(new Color(0xff9800));
            confirmButton.setForeground(Color.WHITE);
            confirmButton.addActionListener(e -> {
                String id = table.getValueAt(row, 0).toString();
                updateAppointmentStatus(id, "Thành công");
                dialog.dispose();
                reloadAppointmentPanels();
            });

            buttonPanel.add(confirmButton);
            buttonPanel.add(cancelButton);
        } else if (isDeletable) {
            JButton deleteButton = new JButton("Xóa lịch hẹn");
            deleteButton.setBackground(new Color(215, 86, 86));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc chắn muốn xóa lịch hẹn này?", 
                                                            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String id = table.getValueAt(row, 0).toString();
                    deleteAppointment(id);
                    dialog.dispose();
                    reloadAppointmentPanels();
                }
            });
            buttonPanel.add(deleteButton);
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
        
        section.add(headerPanel, BorderLayout.NORTH);

        // Cột tương ứng với bảng LICHHEN
        String[] columnNames = {
            "Mã lịch", "Mã bệnh nhân", "Mã bác sĩ", "Ngày đặt", "Ngày hẹn",
            "Địa điểm", "Triệu chứng", "Trạng thái"
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa bất kỳ ô nào
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
                        showProductDetails(table, selectedRow);
                }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        
        scroll.setPreferredSize(new Dimension(1000, 250));

        section.add(scroll, BorderLayout.CENTER);
        return section;
       
    }
    
    private class EmailSender {
        private final String fromEmail = "diep03062015@gmail.com";
        private final String password = "elaz xcyx nqdo hsyl";

        public void sendEmail(String toEmail, String subject, String messageText) {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail)
                );
                message.setSubject(subject);
                message.setText(messageText);

                Transport.send(message);
                System.out.println("Email sent successfully to " + toEmail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {   
                new HisAppointAdmin().setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(HisAppointAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
}


