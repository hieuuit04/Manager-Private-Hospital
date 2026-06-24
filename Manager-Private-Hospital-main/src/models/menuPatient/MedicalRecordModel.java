/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.menuPa;

import utils.DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicalRecordModel {
    private String maBN;
    private String soBHYT;
    private String lichSuBenhLy;
    private String diUng;
    private String nhomMau;

    // getter/setter
    public String getMaBN() { return maBN; }
    public void setMaBN(String maBN) { this.maBN = maBN; }
    public String getSoBHYT() { return soBHYT; }
    public void setSoBHYT(String soBHYT) { this.soBHYT = soBHYT; }
    public String getLichSuBenhLy() { return lichSuBenhLy; }
    public void setLichSuBenhLy(String lichSuBenhLy) { this.lichSuBenhLy = lichSuBenhLy; }
    public String getDiUng() { return diUng; }
    public void setDiUng(String diUng) { this.diUng = diUng; }
    public String getNhomMau() { return nhomMau; }
    public void setNhomMau(String nhomMau) { this.nhomMau = nhomMau; }

    // Load data từ CSDL
    public void loadFromDB(String patientId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "SELECT MABN, SOBHYT, LICHSU_BENHLY, DIUNG, NHOMMAU FROM BENHNHAN WHERE MABN = ?")) {
            ps.setString(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    setMaBN(rs.getString("MABN"));
                    setSoBHYT(rs.getString("SOBHYT"));
                    setLichSuBenhLy(rs.getString("LICHSU_BENHLY"));
                    setDiUng(rs.getString("DIUNG"));
                    setNhomMau(rs.getString("NHOMMAU"));
                }
            }
        }
    }

    // Lưu dữ liệu vào CSDL
    public void saveToDB() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "UPDATE BENHNHAN SET SOBHYT = ?, LICHSU_BENHLY = ?, DIUNG = ?, NHOMMAU = ? WHERE MABN = ?")) {
            ps.setString(1, soBHYT);
            ps.setString(2, lichSuBenhLy);
            ps.setString(3, diUng);
            ps.setString(4, nhomMau);
            ps.setString(5, maBN);
            ps.executeUpdate();
        }
    }
}

