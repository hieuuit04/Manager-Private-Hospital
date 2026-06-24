/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentModel {

    // --- Phương thức hiện có ---
    public static List<String> getDoctors() throws SQLException, ClassNotFoundException {
        List<String> doctors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, HOTENND FROM USERS WHERE ROLE = 'Bác sĩ'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getString("ID") + " - " + rs.getString("HOTENND"));
            }
        }
        return doctors;
    }

    public static String generateAppointmentId(Connection conn) throws SQLException {
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
        }
        return prefix + "001";
    }

    public static String generateNotificationId(Connection conn) throws SQLException {
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
        }
        return prefix + "001";
    }

    public static boolean saveAppointment(String patientId, String doctorId, Timestamp ngayHen,
                                          String diadiem, String trieuchung) throws SQLException, ClassNotFoundException {
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
                psTB.setString(1, generateNotificationId(conn));
                psTB.setString(2, patientId);
                psTB.setString(3, noidung);
                psTB.setString(4, "Lịch hẹn");
                psTB.executeUpdate();
                return true;
            }
        }
        return false;
    }

    // --- PHƯƠNG THỨC MỚI CẦN BỔ SUNG ---

    public static List<Appointment> getAppointmentsByPatient(String patientId) throws SQLException, ClassNotFoundException {
        List<Appointment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM LICHHEN WHERE MABN = ? ORDER BY NGAYHEN DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setMaLich(rs.getString("MALICH"));
                appt.setMaBN(rs.getString("MABN"));
                appt.setMaBS(rs.getString("MABS"));
                appt.setNgayHen(rs.getTimestamp("NGAYHEN"));
                appt.setDiaDiem(rs.getString("DIADIEM"));
                appt.setTrieuChung(rs.getString("TRIEUCHUNG"));
                appt.setTrangThai(rs.getString("TRANGTHAI"));
                list.add(appt);
            }
        }
        return list;
    }

    public static boolean updateStatus(String appointmentId, String status) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE LICHHEN SET TRANGTHAI = ? WHERE MALICH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, appointmentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public static boolean deleteAppointment(String appointmentId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM LICHHEN WHERE MALICH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, appointmentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public static boolean updateAppointmentDetails(String appointmentId, Timestamp ngayHen, String diaDiem, String trieuChung)
            throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE LICHHEN SET NGAYHEN = ?, DIADIEM = ?, TRIEUCHUNG = ? WHERE MALICH = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, ngayHen);
            ps.setString(2, diaDiem);
            ps.setString(3, trieuChung);
            ps.setString(4, appointmentId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
