/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBConnection;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BuyMed extends JFrame {

    private JTextField txtHoTen, txtGioiTinh, txtNgaySinh, txtDiaChi, txtSoDienThoai;
    private JTextArea txtLichSuBenhLy, txtDiUng;
    private JLabel lblTenFile;
    private JButton btnUpload;
    private JButton btnGuiYeuCau;
    private File selectedFile;
    private String patientId;
    

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

    public BuyMed(String patientId) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        
        initUI();
        loadPatientInfo();
    }

    private void initUI() {
        setTitle("Mua thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        
        //Set background
        ImagePanel backgroundPanel = new ImagePanel("src/views/patient/image/BackgroundDL1.jpg");
        setContentPane(backgroundPanel);

        // Tiêu đề
        JLabel titleLabel = new JLabel("MUA THUỐC", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // khoảng cách giữa các dòng
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.setOpaque(false); //Nền panel trong suốt

        int y = 0;

        // Họ tên
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblName = new JLabel("Họ tên");
        lblName.setForeground(Color.WHITE);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblName, gbc);
        gbc.gridx = 1;
        txtHoTen = new JTextField();
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtHoTen.setBackground(Color.LIGHT_GRAY);
        txtHoTen.setEditable(false);
        mainPanel.add(txtHoTen, gbc);

        // Giới tính
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblGender = new JLabel("Giới tính:");
        lblGender.setForeground(Color.WHITE);
        lblGender.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblGender, gbc);
        gbc.gridx = 1;
        txtGioiTinh = new JTextField();
        txtGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtGioiTinh.setBackground(new Color(0xd9eef2));
        mainPanel.add(txtGioiTinh, gbc);

        // Ngày sinh
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblDate = new JLabel("Ngày sinh:");
        lblDate.setForeground(Color.WHITE);
        lblDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        txtNgaySinh = new JTextField();
        txtNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtNgaySinh.setBackground(new Color(0xd9eef2));
        mainPanel.add(txtNgaySinh, gbc);

        // Địa chỉ
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblLocate = new JLabel("Địa chỉ:");
        lblLocate.setForeground(Color.WHITE);
        lblLocate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblLocate, gbc);
        gbc.gridx = 1;
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDiaChi.setBackground(new Color(0xd9eef2));
        mainPanel.add(txtDiaChi, gbc);

        // SĐT
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblNumPhone = new JLabel("Số điện thoại:");
        lblNumPhone.setForeground(Color.WHITE);
        lblNumPhone.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblNumPhone, gbc);
        gbc.gridx = 1;
        txtSoDienThoai = new JTextField();
        txtSoDienThoai.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSoDienThoai.setBackground(new Color(0xd9eef2));
        mainPanel.add(txtSoDienThoai, gbc);

        // Lịch sử bệnh lý
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblHistoryMed = new JLabel("Lịch sử bệnh lý:");
        lblHistoryMed.setForeground(Color.WHITE);
        lblHistoryMed.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblHistoryMed, gbc);
        gbc.gridx = 1;
        txtLichSuBenhLy = new JTextArea(3, 20);
        txtLichSuBenhLy.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtLichSuBenhLy.setBackground(new Color(0xd9eef2));
        mainPanel.add(new JScrollPane(txtLichSuBenhLy), gbc);

        // Dị ứng
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblAllergic = new JLabel("Dị ứng:");
        lblAllergic.setForeground(Color.WHITE);
        lblAllergic.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblAllergic, gbc);
        gbc.gridx = 1;
        txtDiUng = new JTextArea(3, 20);
        txtDiUng.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtDiUng.setBackground(new Color(0xd9eef2));
        mainPanel.add(new JScrollPane(txtDiUng), gbc);

        // Upload file
        y++;
        gbc.gridx = 0; 
        gbc.gridy = y;
        JLabel lblUpload = new JLabel("Upload đơn thuốc:");
        lblUpload.setForeground(Color.WHITE);
        lblUpload.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblUpload, gbc);

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnUpload = new JButton("Tải file");
        btnUpload.setForeground(Color.WHITE);
        btnUpload.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnUpload.setBackground(new Color(0x0A464F));
        
        lblTenFile = new JLabel("Chưa chọn file");
        lblTenFile.setForeground(Color.WHITE);
        lblTenFile.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        filePanel.add(btnUpload);
        filePanel.add(Box.createRigidArea(new Dimension(10, 0)));
        filePanel.setOpaque(false);
        filePanel.add(lblTenFile);
        gbc.gridx = 1;
        mainPanel.add(filePanel, gbc);

        // Nút gửi yêu cầu
        y++;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnGuiYeuCau = new JButton("Gửi yêu cầu mua thuốc");
        btnGuiYeuCau.setForeground(Color.WHITE);
        btnGuiYeuCau.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuiYeuCau.setBackground(new Color(0x0A464F));
        mainPanel.add(btnGuiYeuCau, gbc);
        
        btnGuiYeuCau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuiYeuCau.setBackground(new Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnGuiYeuCau.setBackground(new Color(0x0A464F));
            }
        });

        add(mainPanel);

        // Gắn sự kiện
        btnUpload.addActionListener(e -> chooseFile());
        btnGuiYeuCau.addActionListener(e -> {
            try {
                sendRequest();
            } catch (SQLException ex) {
                Logger.getLogger(BuyMed.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuyMed.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void loadPatientInfo() throws SQLException, ClassNotFoundException {
         try (Connection conn = DBConnection.getConnection()){
            String sql = "SELECT u.HOTENND, u.GIOITINH, u.NGAYSINH, u.DIACHI, u.SDT, b.LICHSU_BENHLY, b.DIUNG " +
                         "FROM USERS u JOIN BENHNHAN b ON u.ID = b.MABN WHERE b.MABN = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtHoTen.setText(rs.getString("HOTENND"));
                txtGioiTinh.setText(rs.getString("GIOITINH"));
                txtNgaySinh.setText(rs.getDate("NGAYSINH").toString());
                txtDiaChi.setText(rs.getString("DIACHI"));
                txtSoDienThoai.setText(rs.getString("SDT"));
                txtLichSuBenhLy.setText(rs.getString("LICHSU_BENHLY"));
                txtDiUng.setText(rs.getString("DIUNG"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        }
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            lblTenFile.setText(selectedFile.getName());
        }
    }
    
    private String generateNewMaDT(Connection conn) throws SQLException {
        String newMaDT = "DT001"; // Mặc định nếu bảng rỗng
        String sql = "SELECT MAX(MADT) FROM DONTHUOC_DONTHUOCYC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String lastMaDT = rs.getString(1); // VD: "DT002"
                int number = Integer.parseInt(lastMaDT.substring(2)); // Lấy số 2
                number++; // Tăng lên 1
                newMaDT = String.format("DT%03d", number); // VD: "DT003"
            }
        }
        return newMaDT;
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


    private void sendRequest() throws SQLException, ClassNotFoundException {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Chưa chọn file đơn thuốc!");
            return;
        }

        String gioiTinhBN = txtGioiTinh.getText();
        String ngaySinhBN = txtNgaySinh.getText();
        String LSBL_BN = txtLichSuBenhLy.getText();
        String diUngBN = txtDiUng.getText();

        try (Connection conn = DBConnection.getConnection()) {
            String newMaDT = generateNewMaDT(conn); 

            String sql = "INSERT INTO DONTHUOC_DONTHUOCYC (MADT, MABN, GIOITINHBN, NGAYSINHBN, LICHSU_BENHLY_BN, DIUNGBN, FILEDONTHUOC) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newMaDT);
            ps.setString(2, patientId);
            ps.setString(3, gioiTinhBN);
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(ngaySinhBN);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                ps.setDate(4, sqlDate);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ. Định dạng phải là yyyy-MM-dd");
                return;
            }
            ps.setString(5, LSBL_BN);
            ps.setString(6, diUngBN);

            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                ps.setBlob(7, fis); // 🛠 Gán file vào đúng vị trí
                ps.executeUpdate();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi đọc file: " + e.getMessage());
                return;
            }

            // Gửi thông báo
            String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            String noidung = "Bạn đã gửi yêu cầu mua thuốc thành công vào ngày " + currentDate + ". Vui lòng chờ xác nhận.";
            String sqlTB = "INSERT INTO THONGBAO (MATB, USER_ID, NOIDUNG, LOAI) VALUES (?, ?, ?, ?)";
            PreparedStatement psTB = conn.prepareStatement(sqlTB);
            psTB.setString(1, generateNotificationId(conn));
            psTB.setString(2, patientId);
            psTB.setString(3, noidung);
            psTB.setString(4, "Mua thuốc");
            psTB.executeUpdate();

            JOptionPane.showMessageDialog(this, "Gửi yêu cầu thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi yêu cầu: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BuyMed("U002").setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(BuyMed.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BuyMed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }); 
    }
}
