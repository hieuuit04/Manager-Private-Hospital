/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.menuPatient;

import javax.swing.*;
import java.awt.*;

public class SecurityPolicy extends JPanel {
    private Runnable onBackCallback;

    public SecurityPolicy(Runnable onBackCallback) {
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
            <p>Tại Healink, chúng tôi cam kết bảo vệ dữ liệu và thông tin cá nhân của bệnh nhân, nhân viên và các bên liên quan khác 
            được lưu trữ trong ứng dụng của bạn. Chính sách An toàn bảo mật này mô tả các biện pháp chúng tôi thực hiện 
            để đảm bảo an toàn cho dữ liệu của bạn.</p>
            
            <p><b>1. Thu thập và sử dụng dữ liệu</b><br>
                - Dữ liệu thu thập: Ứng Dụng được thiết kế để lưu trữ các loại dữ liệu y tế và hành chính cần thiết 
                  cho việc quản lý bệnh viện, bao gồm thông tin bệnh nhân (tên, tuổi, giới tính, tiền sử bệnh án), 
                  thông tin nhân viên (tên, chức vụ, thông tin liên hệ), lịch hẹn, kết quả xét nghiệm, đơn thuốc, 
                  thông tin thanh toán, v.v.<br>
               
                - Mục đích sử dụng: Dữ liệu được sử dụng duy nhất cho mục đích quản lý và vận hành bệnh viện hiệu quả, 
                  cung cấp dịch vụ y tế, lập báo cáo, và tuân thủ các quy định pháp luật.<br>
               
                - Không chia sẻ dữ liệu: Chúng tôi không chia sẻ, bán hoặc cho thuê dữ liệu của bạn cho bất kỳ bên thứ ba nào, 
                  trừ khi có yêu cầu hợp pháp từ cơ quan nhà nước có thẩm quyền hoặc có sự đồng ý rõ ràng của bạn.<br>
            </p>
                
            <p><b>2. Các biện pháp bảo mật</b><br>
            Bảo vệ dữ liệu cục bộ: Ứng Dụng là ứng dụng desktop, dữ liệu của bạn được lưu trữ cục bộ trên máy tính 
            hoặc máy chủ của bệnh viện bạn. Chúng tôi khuyến nghị bạn thực hiện các biện pháp bảo mật sau 
            để bảo vệ dữ liệu của mình:<br>
               
                - Mã hóa dữ liệu: Cân nhắc sử dụng các giải pháp mã hóa dữ liệu trên ổ đĩa nơi dữ liệu được lưu trữ.<br>
               
                - Sao lưu dữ liệu định kỳ: Thực hiện sao lưu dữ liệu thường xuyên và lưu trữ bản sao lưu ở nơi an toàn, 
                  riêng biệt để phòng tránh mất mát dữ liệu do sự cố phần cứng hoặc các nguyên nhân khác.<br>
               
                - Kiểm soát truy cập: Thiết lập quyền truy cập chặt chẽ cho từng người dùng, chỉ cấp quyền truy cập 
                  vào những thông tin cần thiết cho công việc của họ.<br>
               
                - Mật khẩu mạnh: Yêu cầu người dùng sử dụng mật khẩu mạnh và thay đổi định kỳ.<br>
               
                - Tường lửa và phần mềm diệt virus: Đảm bảo hệ thống của bạn được bảo vệ bởi tường lửa và 
                  phần mềm diệt virus/phần mềm độc hại được cập nhật thường xuyên.<br>
               
                - Cập nhật hệ điều hành và ứng dụng: Đảm bảo hệ điều hành và tất cả các ứng dụng liên quan trên máy tính/máy chủ 
                  của bạn được cập nhật phiên bản mới nhất để vá các lỗ hổng bảo mật.<br>
               
            Bảo mật ứng dụng: Ứng Dụng được phát triển với các nguyên tắc bảo mật từ thiết kế (Security by Design), bao gồm:<br>
               
                - Kiểm soát truy cập dựa trên vai trò: Cho phép quản trị viên cấp quyền truy cập khác nhau 
                  cho từng vai trò người dùng (ví dụ: bác sĩ, y tá, nhân viên lễ tân, quản lý).<br>
               
                - Ghi nhật ký hoạt động: Ứng Dụng có khả năng ghi lại các hoạt động quan trọng của người dùng để hỗ trợ kiểm tra 
                  và phát hiện các hành vi đáng ngờ.<br>
               
                - Bảo vệ chống lại các cuộc tấn công phổ biến: Các biện pháp phòng chống các lỗ hổng bảo mật phổ biến 
                  (ví dụ: SQL Injection, Cross-Site Scripting) được tích hợp trong quá trình phát triển.<br>
            </p>
                
            <p><b>3. Trách nhiệm của người sử dụng</b><br>
                - Người sử dụng có trách nhiệm tuân thủ các chính sách bảo mật nội bộ của bệnh viện và các quy định pháp luật 
                  liên quan đến bảo vệ dữ liệu cá nhân.<br>
               
                - Người sử dụng phải báo cáo ngay lập tức cho quản trị viên hoặc bộ phận kỹ thuật nếu phát hiện bất kỳ 
                  sự cố bảo mật nào.<br>
            </p>
               
            <p><b>4. Thay đổi chính sách</b><br>
            Chúng tôi có thể cập nhật Chính sách An toàn bảo mật này theo thời gian. Mọi thay đổi sẽ được thông báo 
            thông qua Ứng Dụng hoặc các kênh liên lạc khác.<br>
            </p>
                             
            </body>
            </html>
 
        """;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test An toàn bảo mật");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600); // hoặc tùy chỉnh theo nhu cầu

            // Tạo panel điều khoản và gán callback là in ra console
            SecurityPolicy termPanel = new SecurityPolicy(() -> {
                System.out.println("Quay lại được gọi!");
                // Bạn có thể thêm frame.dispose(); nếu muốn đóng cửa sổ
            });

            frame.setContentPane(termPanel);
            frame.setLocationRelativeTo(null); // hiển thị ở giữa màn hình
            frame.setVisible(true);
        });
    }
}
