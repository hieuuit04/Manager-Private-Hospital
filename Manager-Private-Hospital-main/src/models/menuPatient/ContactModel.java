/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.menuPa;

public class ContactModel {
    // Trả về nội dung HTML liên hệ
    public String getContactHtml() {
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
}

