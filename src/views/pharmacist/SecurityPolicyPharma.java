/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import javax.swing.*;
import java.awt.*;

public class SecurityPolicyPharma extends JFrame {

    public SecurityPolicyPharma() {
        setTitle("Chính sách An toàn & Bảo mật");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === Header đẹp ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(43, 74, 160));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel headerTitle = new JLabel("Chính sách An toàn & Bảo mật", SwingConstants.CENTER);
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(headerTitle, BorderLayout.CENTER);

        // === Nội dung HTML ===
        String htmlContent = """
            <html>
            <body style='font-family: Segoe UI; font-size: 15px; padding: 20px; background-color: #f8f9fa; color: #333333;'>
            <div style='max-width: 1000px; margin: auto;'>
            <h2 style='color: #2b4a59;'>Chính sách An toàn & Bảo mật</h2>

            <p>Healink cam kết bảo vệ thông tin cá nhân và dữ liệu của bệnh nhân, nhân viên và các bên liên quan.</p>

            <h3>1. Thu thập và sử dụng dữ liệu</h3>
            <ul>
              <li><b>Dữ liệu thu thập:</b> Thông tin bệnh nhân, nhân viên, đơn thuốc, lịch sử khám, kết quả xét nghiệm, thanh toán...</li>
              <li><b>Mục đích sử dụng:</b> Quản lý y tế, báo cáo, tuân thủ quy định pháp luật.</li>
              <li><b>Không chia sẻ dữ liệu:</b> Trừ khi có yêu cầu pháp lý hoặc sự đồng ý của bạn.</li>
            </ul>

            <h3>2. Biện pháp bảo mật</h3>
            <ul>
              <li><b>Dữ liệu cục bộ:</b></li>
              <ul>
                <li>Mã hóa dữ liệu, sao lưu định kỳ</li>
                <li>Kiểm soát truy cập, mật khẩu mạnh</li>
                <li>Diệt virus, tường lửa, cập nhật phần mềm</li>
              </ul>
              <li><b>Bảo mật ứng dụng:</b></li>
              <ul>
                <li>Phân quyền, ghi log hoạt động</li>
                <li>Chống SQL Injection, XSS...</li>
              </ul>
            </ul>

            <h3>3. Trách nhiệm người sử dụng</h3>
            <ul>
              <li>Tuân thủ quy định nội bộ và pháp luật</li>
              <li>Báo cáo sự cố bảo mật kịp thời</li>
            </ul>

            <h3>4. Thay đổi chính sách</h3>
            <p>Chính sách có thể cập nhật theo thời gian. Thông báo sẽ được gửi qua hệ thống.</p>
            </div>
            </body>
            </html>
        """;

        JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);
        editorPane.setCaretPosition(0);
        editorPane.setBorder(null);
        editorPane.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
}
