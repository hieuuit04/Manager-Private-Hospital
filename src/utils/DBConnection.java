/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        try {
            // Tải driver Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            // Thông tin kết nối
            String hostName = "localhost";  // Địa chỉ máy chủ Oracle
            String sid = "orcl";           // SID của cơ sở dữ liệu
            String connectionUrl = "jdbc:oracle:thin:@" + hostName + ":1521:" + sid;
            String username = "QLBENHVIENTU";  // Tên người dùng Oracle
            String password = "Admin123";  // Mật khẩu của người dùng Oracle
            
            // Kết nối đến cơ sở dữ liệu
            conn = DriverManager.getConnection(connectionUrl, username, password);
            
            // Kiểm tra kết nối thành công
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            e.printStackTrace(); // In ra stack trace chi tiết
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi không tìm thấy lớp Oracle JDBC Driver: " + e.getMessage());
            e.printStackTrace(); // In ra stack trace chi tiết
        }
        return conn;
    }
}

