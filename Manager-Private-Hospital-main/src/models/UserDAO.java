/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import models.User;
import utils.DBConnection;
import utils.HashUtil;

import java.sql.*;

public class UserDAO {
    public static User login(String username, String password) throws Exception {
        String hashedPassword = HashUtil.hashPassword(password);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("ID"),
                    rs.getString("HOTENND"),
                    rs.getString("SDT"),
                    rs.getString("EMAIL"),
                    rs.getString("GIOITINH"),
                    rs.getString("NGAYSINH"),
                    rs.getString("DIACHI"),
                    rs.getString("ROLE"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("USERNAME"),
                    rs.getString("PASSWORD")
                );
            }
        }
        return null;
    }
    
    public static boolean register(User user) throws Exception {
        String hashedPassword = HashUtil.hashPassword(user.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO USERS (HOTENND, SDT, EMAIL, GIOITINH, NGAYSINH, DIACHI, ROLE, TRANGTHAI, USERNAME, PASSWORD) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getGender());
            ps.setString(5, user.getBirthDate());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getRole());
            ps.setString(8, user.getState());
            ps.setString(9, user.getUsername());
            ps.setString(10, hashedPassword);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        }
    }
}

