/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.patient;

import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayBillModel {
    private String patientId;

    public PayBillModel(String patientId) {
        this.patientId = patientId;
    }

    public List<Object[]> getThuocBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT MADT, MADS, MABS, MABN, GIOITINHBN, NGAYSINHBN, LICHSU_BENHLY_BN, DIUNGBN, "
                   + "FILEDONTHUOC, GHICHU, NGAYBAN, THANHTIEN, TRANGTHAITT "
                   + "FROM DONTHUOC_DONTHUOCYC WHERE MABN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADT"),
                        rs.getString("MADS"),
                        rs.getString("MABS"),
                        rs.getString("MABN"),
                        rs.getString("GIOITINHBN"),
                        rs.getString("NGAYSINHBN"),
                        rs.getString("LICHSU_BENHLY_BN"),
                        rs.getString("DIUNGBN"),
                        rs.getBlob("FILEDONTHUOC"),
                        rs.getString("GHICHU"),
                        rs.getString("NGAYBAN"),
                        rs.getString("THANHTIEN"),
                        rs.getString("TRANGTHAITT")
                    };
                    data.add(row);
                }
            }
        }
        return data;
    }

    public List<Object[]> getHoaDonKhamBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM HOADON_KHAMBENH H JOIN KHAM K ON H.MAKHAM = K.MAKHAM WHERE K.MABN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MAHDKB"),
                        rs.getString("MAKHAM"),
                        rs.getDate("NGAYLAP"),
                        rs.getDouble("TONGTIEN"),
                        rs.getString("PHUONGTHUCTT"),
                        rs.getString("GHICHU"),
                        rs.getString("TRANGTHAITT")
                    };
                    data.add(row);
                }
            }
        }
        return data;
    }

    public List<Object[]> getDieuTriBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM DIEUTRI WHERE MABN = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADT"),
                        rs.getString("MABS"),
                        rs.getString("MABN"),
                        rs.getDate("NGAYDT"),
                        rs.getDouble("TONGTIEN"),
                        rs.getString("GHICHU"),
                        rs.getString("TRANGTHAITT")
                    };
                    data.add(row);
                }
            }
        }
        return data;
    }
}

