/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.menuPatient;

import javax.swing.*;
import java.awt.*;

public class Contact extends JPanel {
    private Runnable onBackCallback;

    public Contact(Runnable onBackCallback) {
        this.onBackCallback = onBackCallback;
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        // Tiêu đề
        JLabel titleLabel = new JLabel("AN TOÀN BẢO MẬT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0x2B4A59));
        add(titleLabel, BorderLayout.NORTH);

        // Nội dung
        JEditorPane contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setText(getTermsHtml());
        contentPane.setEditable(false);
        contentPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentPane.setMargin(new Insets(10, 200, 10, 150));

        JScrollPane scrollPane = new JScrollPane(contentPane);
        add(scrollPane, BorderLayout.CENTER);

        // Nút quay lại
        JButton backButton = new JButton("← Quay lại");
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String getTermsHtml() {
        return """
               
            <html>
            <body style='font-family: Segoe UI; font-size: 14px;'>  
            <p>Nếu bạn có bất kỳ câu hỏi, góp ý, hoặc cần hỗ trợ về Ứng dụng Quản lý Bệnh viện Healink, 
               vui lòng liên hệ với chúng tôi theo các thông tin dưới đây:</p>
            
            <p><b>1. Hỗ trợ kỹ thuật</b><br>
                - Điện thoại: 0123456789<br>
               
                - Email: BVT_Healink@gmail.com<br>
               
                - Giờ làm việc: Thứ Hai - Thứ Sáu, 8:00 AM - 5:00 PM, Thứ 7: 8:00 AM - 4:00PM<br>
            </p>
                
            <p><b>2. Phản hồi và góp ý</b><br>
            Chúng tôi luôn hoan nghênh những ý kiến đóng góp của bạn để cải thiện Ứng Dụng. Vui lòng gửi email 
            cho chúng tôi theo địa chỉ:<br>
               
                - Email: BVT_Healink@gmail.com<br>
            </p>
                
            <p><b>3. Thông tin Công ty/Đơn vị phát triển</b><br>
                - Tên Công ty: Công ty TNHH Healink<br>
                - Địa chỉ: Việt Nam<br>
                - Website: Healink.com.vn<br>
            </p>
               
            <p><b>Cảm ơn bạn đã sử dụng Ứng dụng Quản lý Bệnh viện Healink!<br></p>
                             
            </body>
            </html>
 
        """;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test liên hệ");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600); // hoặc tùy chỉnh theo nhu cầu

            // Tạo panel điều khoản và gán callback là in ra console
            Contact contactPanel = new Contact(() -> {
                System.out.println("Quay lại được gọi!");
                // Bạn có thể thêm frame.dispose(); nếu muốn đóng cửa sổ
            });

            frame.setContentPane(contactPanel);
            frame.setLocationRelativeTo(null); // hiển thị ở giữa màn hình
            frame.setVisible(true);
        });
    }
}

