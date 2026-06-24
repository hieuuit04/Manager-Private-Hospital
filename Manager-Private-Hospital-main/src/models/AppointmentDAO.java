/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<Appointment> getAppointmentsByPatientId(String patientId) throws SQLException, ClassNotFoundException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM LICHHEN WHERE MABN = ?";  // sửa MABENHNHAN -> MABN
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Appointment appt = new Appointment(
                    rs.getString("MALICH"),
                    rs.getString("MABN"),
                    rs.getString("MABS"),
                    rs.getTimestamp("NGAYDAT"),   // giả sử Appointment có kiểu Timestamp
                    rs.getTimestamp("NGAYHEN"),
                    rs.getString("DIADIEM"),
                    rs.getString("TRIEUCHUNG"),
                    rs.getString("TRANGTHAI")
                );
                appointments.add(appt);
            }
        }
        return appointments;
    }

    public boolean updateAppointmentStatus(String id, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE LICHHEN SET TRANGTHAI = ? WHERE MALICH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteAppointment(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM LICHHEN WHERE MALICH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Thêm phương thức update chi tiết lịch hẹn nếu cần
    public boolean updateAppointmentDetails(String appointmentId, Timestamp ngayHen, String diaDiem, String trieuChung)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE LICHHEN SET NGAYHEN = ?, DIADIEM = ?, TRIEUCHUNG = ? WHERE MALICH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, ngayHen);
            ps.setString(2, diaDiem);
            ps.setString(3, trieuChung);
            ps.setString(4, appointmentId);
            return ps.executeUpdate() > 0;
        }
    }
}


