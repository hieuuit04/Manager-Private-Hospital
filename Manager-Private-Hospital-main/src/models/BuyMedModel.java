/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

public class BuyMedModel {

    public ResultSet getPatientInfo(String patientId) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT u.HOTENND, u.GIOITINH, u.NGAYSINH, u.DIACHI, u.SDT, b.LICHSU_BENHLY, b.DIUNG " +
                     "FROM USERS u JOIN BENHNHAN b ON u.ID = b.MABN WHERE b.MABN = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, patientId);
        return ps.executeQuery();
    }

    public String generateNewMaDT(Connection conn) throws SQLException {
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

    public String generateNotificationId(Connection conn) throws SQLException {
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
        return prefix + "001"; // Nếu chưa có thông báo nào
    }

    public boolean savePrescription(String patientId, String gioiTinh, String ngaySinh,
                                    String lichSuBenhLy, String diUng,
                                    File prescriptionFile) throws Exception {
        Connection conn = DBConnection.getConnection();

        try {
            conn.setAutoCommit(false);

            String newMaDT = generateNewMaDT(conn);

            String insertSQL = "INSERT INTO DONTHUOC_DONTHUOCYC (MADT, MABN, GIOITINHBN, NGAYSINHBN, LICHSU_BENHLY, DIUNG, FILE_DONTHUOC) " +
                    "VALUES (?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSQL);
            ps.setString(1, newMaDT);
            ps.setString(2, patientId);
            ps.setString(3, gioiTinh);
            ps.setString(4, ngaySinh);
            ps.setString(5, lichSuBenhLy);
            ps.setString(6, diUng);

            // Đọc file thành InputStream
            FileInputStream fis = new FileInputStream(prescriptionFile);
            ps.setBinaryStream(7, fis, (int) prescriptionFile.length());

            ps.executeUpdate();

            // Tạo thông báo
            String newMaTB = generateNotificationId(conn);
            String insertNotifySQL = "INSERT INTO THONGBAO (MATB, MABN, NOIDUNG, NGAYTAO) VALUES (?, ?, ?, SYSDATE)";
            PreparedStatement psNotify = conn.prepareStatement(insertNotifySQL);
            psNotify.setString(1, newMaTB);
            psNotify.setString(2, patientId);
            psNotify.setString(3, "Bệnh nhân đã gửi đơn thuốc mới: " + newMaDT);

            psNotify.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }
}

