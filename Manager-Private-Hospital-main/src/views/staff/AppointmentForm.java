/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.staff;


import utils.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class AppointmentForm extends JFrame {
    private JComboBox<String> doctorComboBox, chuyenKhoaComboBox;
    private JTextField txtNgayHen;
    private JTextField txtDiadiem;
    private JTextArea txtTrieuchung;
    private JButton btnSubmit;
    private String patientId;
    private boolean isAdjustingDoctorComboBox = false;
    private boolean isAdjustingChuyenKhoaComboBox = false;

    public class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public AppointmentForm(String patientId) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        
        setTitle("Đặt lịch khám bệnh");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());

        // Set background
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BackgroundDL.jpg");
        setContentPane(backgroundPanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel("ĐẶT LỊCH HẸN KHÁM BỆNH", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font inputFont = new Font("Segoe UI", Font.PLAIN, 20);

        // Bác sĩ
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblDoctor = new JLabel("Bác sĩ:");
        lblDoctor.setForeground(Color.BLACK);
        lblDoctor.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblDoctor, gbc);
        gbc.gridx = 1;
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setFont(inputFont);
        doctorComboBox.setBackground(new Color(0xd9eef2));
        doctorComboBox.setOpaque(true);
        loadDoctorsSortedByDistance(patientId);
        formPanel.add(doctorComboBox, gbc);
        
        doctorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAdjustingDoctorComboBox) return;

                String selected = (String) doctorComboBox.getSelectedItem();
                if (selected != null && selected.contains(" - ")) {
                    String[] parts = selected.split(" - ", 2);
                    String doctorId = parts[0].trim();

                    System.out.println("Selected ID: " + doctorId);

                    // Khi cập nhật chuyên khoa, tắt listener chuyên khoa
                    isAdjustingChuyenKhoaComboBox = true;
                    updateChuyenKhoaTheoBacSi(doctorId);
                    isAdjustingChuyenKhoaComboBox = false;
                }
            }
        });

        // Chuyên khoa
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblChuyenKhoa = new JLabel("Chuyên khoa:");
        lblChuyenKhoa.setForeground(Color.BLACK);
        lblChuyenKhoa.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblChuyenKhoa, gbc);

        gbc.gridx = 1;
        chuyenKhoaComboBox = new JComboBox<>();
        chuyenKhoaComboBox.setFont(inputFont);
        chuyenKhoaComboBox.setBackground(new Color(0xd9eef2));
        formPanel.add(chuyenKhoaComboBox, gbc);
        
        loadChuyenKhoa();

        chuyenKhoaComboBox.addActionListener(e -> {
            if (isAdjustingChuyenKhoaComboBox) return;

            String selectedCK = (String) chuyenKhoaComboBox.getSelectedItem();
            if (selectedCK != null && selectedCK.equals("-- Chọn chuyên khoa --")) {
                try {
                    isAdjustingDoctorComboBox = true;
                    loadDoctorsSortedByDistance(patientId);
                    isAdjustingDoctorComboBox = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (selectedCK != null) {
                try {
                    isAdjustingDoctorComboBox = true;
                    loadDoctorsByChuyenKhoa(selectedCK);
                    isAdjustingDoctorComboBox = false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Ngày hẹn
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblDate = new JLabel("Ngày hẹn (yyyy-MM-dd HH:mm:ss):");
        lblDate.setForeground(Color.BLACK);
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        txtNgayHen = new JTextField();
        txtNgayHen.setFont(inputFont);
        txtNgayHen.setBackground(new Color(0xd9eef2));
        formPanel.add(txtNgayHen, gbc);

        // Địa điểm
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblLocation = new JLabel("Địa điểm khám:");
        lblLocation.setForeground(Color.BLACK);
        lblLocation.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblLocation, gbc);
        gbc.gridx = 1;
        txtDiadiem = new JTextField();
        txtDiadiem.setFont(inputFont);
        txtDiadiem.setBackground(new Color(0xd9eef2));
        formPanel.add(txtDiadiem, gbc);

        // Triệu chứng
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel lblSymptoms = new JLabel("Triệu chứng:");
        lblSymptoms.setForeground(Color.BLACK);
        lblSymptoms.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formPanel.add(lblSymptoms, gbc);
        gbc.gridx = 1;
        txtTrieuchung = new JTextArea(4, 20);
        txtTrieuchung.setFont(inputFont);
        txtTrieuchung.setBackground(new Color(0xd9eef2));
        txtTrieuchung.setOpaque(true);
        txtTrieuchung.setLineWrap(true);
        txtTrieuchung.setWrapStyleWord(true);
        txtTrieuchung.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(txtTrieuchung,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        formPanel.add(scrollPane, gbc);

        // Nút đặt lịch
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        btnSubmit = new JButton("Đặt lịch");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSubmit.setBackground(new Color(0x588EA7));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setPreferredSize(new Dimension(120, 35));
        btnSubmit.addActionListener(e -> datLich(patientId));
        
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0xff9800)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSubmit.setBackground(new Color(0x588EA7));
            }
        });

        formPanel.add(btnSubmit, gbc);

        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
  
    private void loadChuyenKhoa() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT CHUYENKHOA FROM BACSI ORDER BY CHUYENKHOA";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            chuyenKhoaComboBox.addItem("-- Chọn chuyên khoa --");
            while (rs.next()) {
                chuyenKhoaComboBox.addItem(rs.getString("CHUYENKHOA"));
            }
        }
    }
    
    private void loadDoctorsByChuyenKhoa(String chuyenKhoa) {
        doctorComboBox.removeAllItems();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT B.MABS, U.HOTENND FROM BACSI B JOIN USERS U ON B.MABS = U.ID WHERE B.CHUYENKHOA = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, chuyenKhoa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("MABS").trim();      
                String name = rs.getString("HOTENND").trim();
                String display = id + " - " + name;
                doctorComboBox.addItem(display);              
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateChuyenKhoaTheoBacSi(String doctorId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT CHUYENKHOA FROM BACSI WHERE MABS = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, doctorId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String chuyenKhoa = rs.getString("CHUYENKHOA");
                chuyenKhoaComboBox.setSelectedItem(chuyenKhoa);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } 
    
    private String generateAppointmentId(Connection conn) {
        String prefix = "LH";
        String sql = "SELECT MAX(MALICH) FROM LICHHEN";
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
    
    private void datLich(String patientId) {
        if (doctorComboBox.getSelectedItem() == null || txtNgayHen.getText().isEmpty() || txtDiadiem.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        java.sql.Timestamp ngayHen;
        try {
            ngayHen = java.sql.Timestamp.valueOf(txtNgayHen.getText());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ! Định dạng: yyyy-MM-dd HH:mm:ss");
            return;
        }

        String doctorId = doctorComboBox.getSelectedItem().toString().split(" - ")[0];
        String diadiem = txtDiadiem.getText();
        String trieuchung = txtTrieuchung.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String malich = generateAppointmentId(conn);
            String sql = "INSERT INTO LICHHEN (MALICH, MABN, MABS, NGAYHEN, DIADIEM, TRIEUCHUNG, TRANGTHAI) " +
                         "VALUES (?, ?, ?, ?, ?, ?, 'Chờ xác nhận')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, malich);
            ps.setString(2, patientId);
            ps.setString(3, doctorId);
            ps.setTimestamp(4, ngayHen);
            ps.setString(5, diadiem);
            ps.setString(6, trieuchung);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                String noidung = "Bạn đã đặt lịch hẹn thành công vào ngày " + ngayHen.toLocalDateTime().toString().replace("T", " ") + ". Vui lòng chờ xác nhận.";
                String sqlTB = "INSERT INTO THONGBAO (MATB, USER_ID, NOIDUNG, LOAI) VALUES (?, ?, ?, ?)";
                PreparedStatement psTB = conn.prepareStatement(sqlTB);
                psTB.setString(1, generateNotificationId(conn)); // Hàm tự viết để sinh ID
                psTB.setString(2, patientId);
                psTB.setString(3, noidung);
                psTB.setString(4, "Lịch hẹn");
                psTB.executeUpdate();

                // Bước 1: Lấy email bệnh nhân
                String email = null;
                String hoTenBenhNhan = "";
                String hoTenBacSi = "";
                String sqlEmail = "SELECT EMAIL, HOTENND FROM USERS WHERE ID = ?";
                PreparedStatement psEmail = conn.prepareStatement(sqlEmail);
                psEmail.setString(1, patientId);
                ResultSet rsEmail = psEmail.executeQuery();
                if (rsEmail.next()) {
                    email = rsEmail.getString("EMAIL");
                    hoTenBenhNhan = rsEmail.getString("HOTENND");
                }
                
                String sqlBacSi = "SELECT HOTENND FROM USERS WHERE ID = ?";
                PreparedStatement psBacSi = conn.prepareStatement(sqlBacSi);
                psBacSi.setString(1, doctorId);
                ResultSet rsBacSi = psBacSi.executeQuery();
                if (rsBacSi.next()) {
                    hoTenBacSi = rsBacSi.getString("HOTENND");
                }

                // Bước 2: Gửi email nếu có địa chỉ
                if (email != null && !email.isEmpty()) {
                    String subject = "Xác nhận đặt lịch hẹn khám bệnh";
                    String messageText = "Xin chào " + hoTenBenhNhan + ",\n\n" +
                                         "Bạn đã đặt lịch hẹn thành công với bác sĩ " + hoTenBacSi +
                                         " vào lúc " + ngayHen.toLocalDateTime().toString().replace("T", " ") +
                                         " tại " + diadiem + ".\n\n" +
                                         "Vui lòng chờ xác nhận từ phía bác sĩ.\n\nTrân trọng.";
                    messageText += "\n\n------------------------\n" +
                                "Bệnh viện tư Healink\n" +
                                "Địa chỉ: Khu phố 6, phường Linh Trung, Tp.Thủ Đức, Tp.Hồ Chí Minh\n" +
                                "Điện thoại: (0123) 456 789\n" +
                                "Email: contactBVTHealink@gmail.com\n" +
                                "Website: www.benhvientuHealink.vn\n" +
                                "Facebook: fb.com/benhvientuHealink\n\n" +
                                "Trân trọng cảm ơn quý bệnh nhân đã tin tưởng và sử dụng dịch vụ của chúng tôi.";


                    try {
                        new EmailSender().sendEmail(email, subject, messageText);
                        System.out.println("Đã gửi email xác nhận cho bệnh nhân: " + email);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println("Không thể gửi email cho bệnh nhân.");
                    }
                }

                JOptionPane.showMessageDialog(this, "Đặt lịch thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đặt lịch thất bại.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đặt lịch: " + e.getMessage());
        }
    }
  
    private double[] getCoordinatesFromAddress(String address) throws IOException {
        String apiKey = "4569858e5b1d4820ade68c10878fb5c1";
        String url = "https://api.opencagedata.com/geocode/v1/json?q=" +
                     URLEncoder.encode(address, "UTF-8") + "&key=" + apiKey;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            JSONObject json = new JSONObject(response.toString());
            JSONArray results = json.getJSONArray("results");

            if (results.length() == 0)
                throw new IOException("Không tìm thấy tọa độ");

            JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
            return new double[]{ geometry.getDouble("lat"), geometry.getDouble("lng") };
        }
    }

    public void updateLatLngForDoctorsAndPatients() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, DIACHI FROM USERS " +
                         "WHERE (ROLE = 'Bác sĩ' OR ROLE = 'Bệnh nhân') " +
                         "AND (LATITUDE IS NULL OR LONGITUDE IS NULL)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userId = rs.getString("ID");
                String diaChi = rs.getString("DIACHI");

                if (diaChi == null || diaChi.trim().isEmpty()) {
                    System.err.println("⚠️ Không có địa chỉ cho: " + userId);
                    continue;
                }

                try {
                    double[] coords = getCoordinatesFromAddress(diaChi);
                    PreparedStatement update = conn.prepareStatement(
                        "UPDATE USERS SET LATITUDE = ?, LONGITUDE = ? WHERE ID = ?"
                    );
                    update.setDouble(1, coords[0]);
                    update.setDouble(2, coords[1]);
                    update.setString(3, userId);
                    update.executeUpdate();

                    System.out.println("✅ Đã cập nhật lat/lng cho: " + userId);
                } catch (IOException e) {
                    System.err.println("⚠️ Không lấy được tọa độ cho: " + userId + " - " + diaChi);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public void loadDoctorsSortedByDistance(String patientId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(
                "SELECT DIACHI, LATITUDE, LONGITUDE FROM USERS WHERE ID = ?");
            ps1.setString(1, patientId);
            ResultSet rs1 = ps1.executeQuery();
            if (!rs1.next()) return;

            Double lat1 = rs1.getObject("LATITUDE", Double.class);
            Double lon1 = rs1.getObject("LONGITUDE", Double.class);
            String diaChi = rs1.getString("DIACHI");

            if ((lat1 == null || lon1 == null) && diaChi != null) {
                try {
                    double[] coords = getCoordinatesFromAddress(diaChi);
                    lat1 = coords[0];
                    lon1 = coords[1];
                    PreparedStatement update = conn.prepareStatement(
                        "UPDATE USERS SET LATITUDE = ?, LONGITUDE = ? WHERE ID = ?");
                    update.setDouble(1, lat1);
                    update.setDouble(2, lon1);
                    update.setString(3, patientId);
                    update.executeUpdate();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Không tìm được tọa độ cho bệnh nhân.");
                    return;
                }
            }

            if (lat1 == null || lon1 == null) {
                JOptionPane.showMessageDialog(null, "Thiếu thông tin tọa độ bệnh nhân.");
                return;
            }

            PreparedStatement ps2 = conn.prepareStatement(
                "SELECT ID, HOTENND, DIACHI, LATITUDE, LONGITUDE FROM USERS WHERE ROLE = 'Bác sĩ'");
            ResultSet rs2 = ps2.executeQuery();

            List<DoctorDistance> list = new ArrayList<>();
            while (rs2.next()) {
                String id = rs2.getString("ID");
                String name = rs2.getString("HOTENND");
                String address = rs2.getString("DIACHI");
                Double lat2 = rs2.getObject("LATITUDE", Double.class);
                Double lon2 = rs2.getObject("LONGITUDE", Double.class);

                if ((lat2 == null || lon2 == null) && address != null) {
                    try {
                        double[] coords = getCoordinatesFromAddress(address);
                        lat2 = coords[0];
                        lon2 = coords[1];
                        PreparedStatement update = conn.prepareStatement(
                            "UPDATE USERS SET LATITUDE = ?, LONGITUDE = ? WHERE ID = ?");
                        update.setDouble(1, lat2);
                        update.setDouble(2, lon2);
                        update.setString(3, id);
                        update.executeUpdate();
                    } catch (IOException e) {
                        System.err.println("⚠️ Không tìm được tọa độ cho bác sĩ: " + id);
                        continue;
                    }
                }

                if (lat2 != null && lon2 != null) {
                    double distance = haversine(lat1, lon1, lat2, lon2);
                    list.add(new DoctorDistance(id, name, distance));
                }
            }

            if (doctorComboBox == null) {
                System.err.println("⚠️ doctorComboBox chưa được gán!");
                return;
            }

            list.sort(Comparator.comparingDouble(d -> d.distance));
            doctorComboBox.removeAllItems();
            for (DoctorDistance d : list) {
                doctorComboBox.addItem(d.id + " - " + d.name + " (" + String.format("%.2f km", d.distance) + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xử lý khoảng cách: " + e.getMessage());
        }
    }

    public void setDoctorComboBox(JComboBox<String> comboBox) {
        this.doctorComboBox = comboBox;
    }

    public class DoctorDistance {
        private String id;
        private String name;
        private double distance; // km

        public DoctorDistance(String id, String name, double distance) {
            this.id = id;
            this.name = name;
            this.distance = distance;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public double getDistance() { return distance; }

        @Override
        public String toString() {
            return id + " - " + name + " (" + String.format("%.2f km", distance) + ")";
        }
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
}
