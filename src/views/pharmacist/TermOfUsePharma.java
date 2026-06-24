/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import javax.swing.*;
import java.awt.*;

public class TermOfUsePharma extends JFrame {

    public TermOfUsePharma() {
        setTitle("Điều khoản sử dụng");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // thêm để tối đa hóa (an toàn)
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String htmlContent = """
            <html>
            <body style='font-family: Segoe UI; font-size: 14px; padding: 10px;'>
            <h2 style='color: #2b4a59;'>Điều khoản sử dụng</h2>

            <p>Chào mừng bạn đến với Ứng dụng Quản lý Bệnh viện tư Healink.
            Bằng việc cài đặt và sử dụng Ứng Dụng này, bạn đồng ý tuân thủ các điều khoản và điều kiện sau đây ("Điều khoản sử dụng").
            Vui lòng đọc kỹ trước khi sử dụng.</p>

            <h3>1. Chấp nhận Điều khoản</h3>
            <p>Việc bạn truy cập và sử dụng Ứng Dụng đồng nghĩa với việc bạn đã đọc, hiểu và đồng ý bị ràng buộc bởi các Điều khoản sử dụng này,
            cùng với Chính sách An toàn bảo mật của chúng tôi. Nếu bạn không đồng ý với bất kỳ điều khoản nào, vui lòng không sử dụng Ứng Dụng.</p>

            <h3>2. Quyền sở hữu trí tuệ</h3>
            <p>Tất cả mã nguồn, thiết kế, đồ họa, văn bản, hình ảnh, video, dữ liệu và nội dung trong Ứng Dụng
            thuộc quyền sở hữu của [Tên Công ty/Cá nhân sở hữu] hoặc đối tác cấp phép, và được bảo vệ bởi luật sở hữu trí tuệ.
            Bạn không được sao chép, sửa đổi, phân phối, bán, cho thuê hoặc tạo sản phẩm phái sinh từ Ứng Dụng nếu không có sự cho phép bằng văn bản.</p>

            <h3>3. Giấy phép sử dụng</h3>
            <p>Bạn được cấp giấy phép không độc quyền, không chuyển nhượng để sử dụng Ứng Dụng cho việc quản lý nội bộ bệnh viện tư của bạn.
            Mọi hành vi sử dụng trái phép đều bị nghiêm cấm.</p>

            <h3>4. Trách nhiệm của người sử dụng</h3>
            <ul>
                <li>Chịu trách nhiệm về dữ liệu nhập vào</li>
                <li>Bảo mật thông tin đăng nhập và hoạt động tài khoản</li>
                <li>Không làm gián đoạn hoặc phá hoại Ứng Dụng</li>
                <li>Tuân thủ các quy định pháp luật liên quan</li>
            </ul>

            <h3>5. Từ chối bảo đảm</h3>
            <p>Ứng Dụng được cung cấp "nguyên trạng" và không có bất kỳ đảm bảo rõ ràng hay ngụ ý nào.
            Chúng tôi không đảm bảo rằng Ứng Dụng luôn hoạt động ổn định, không lỗi, hay không có virus.</p>

            <h3>6. Giới hạn trách nhiệm</h3>
            <p>Chúng tôi không chịu trách nhiệm đối với bất kỳ thiệt hại gián tiếp nào như mất lợi nhuận, mất dữ liệu hoặc cơ hội kinh doanh
            phát sinh từ việc sử dụng Ứng Dụng.</p>

            <h3>7. Thay đổi Điều khoản sử dụng</h3>
            <p>Chúng tôi có thể sửa đổi Điều khoản sử dụng bất kỳ lúc nào. Việc tiếp tục sử dụng sau khi có thay đổi đồng nghĩa với việc bạn đồng ý với nội dung cập nhật.</p>

            <h3>8. Chấm dứt</h3>
            <p>Chúng tôi có quyền chấm dứt quyền sử dụng nếu bạn vi phạm bất kỳ điều khoản nào mà không cần báo trước.</p>

            <h3>9. Luật điều chỉnh và giải quyết tranh chấp</h3>
            <p>Điều khoản sử dụng được điều chỉnh bởi pháp luật Việt Nam. Mọi tranh chấp sẽ được giải quyết tại tòa án có thẩm quyền tại Việt Nam.</p>

            </body>
            </html>
        """;

        JEditorPane editorPane = new JEditorPane("text/html", htmlContent);
        editorPane.setEditable(false);
        editorPane.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
    }
}

