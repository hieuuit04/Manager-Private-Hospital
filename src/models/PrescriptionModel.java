/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionModel {

    public static class Prescription {
        public String maDT, maDS, maBS, maBN, gioiTinhBN, lichSuBenhLyBN, diUngBN, ghiChu, ngayBan, thanhTien, trangThaiTT;
        public Blob fileDonThuoc;

        public Prescription(String maDT, String maDS, String maBS, String maBN, String gioiTinhBN,
                            String lichSuBenhLyBN, String diUngBN, Blob fileDonThuoc, String ghiChu,
                            String ngayBan, String thanhTien, String trangThaiTT) {
            this.maDT = maDT;
            this.maDS = maDS;
            this.maBS = maBS;
            this.maBN = maBN;
            this.gioiTinhBN = gioiTinhBN;
            this.lichSuBenhLyBN = lichSuBenhLyBN;
            this.diUngBN = diUngBN;
            this.fileDonThuoc = fileDonThuoc;
            this.ghiChu = ghiChu;
            this.ngayBan = ngayBan;
            this.thanhTien = thanhTien;
            this.trangThaiTT = trangThaiTT;
        }
    }

    public List<Prescription> getPrescriptions(String patientId, String maDT, String maDS, String maBS,
                                               String ngayBan, String thanhTien, String trangThaiTT) throws SQLException, ClassNotFoundException {
        List<Prescription> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM DONTHUOC_DONTHUOCYC WHERE MABN = ?");
        List<Object> params = new ArrayList<>();
        params.add(patientId);

        if (maDT != null && !maDT.trim().isEmpty()) {
            sql.append(" AND UPPER(MADT) LIKE ?");
            params.add("%" + maDT.trim().toUpperCase() + "%");
        }
        if (maDS != null && !maDS.trim().isEmpty()) {
            sql.append(" AND UPPER(MADS) LIKE ?");
            params.add("%" + maDS.trim().toUpperCase() + "%");
        }
        if (maBS != null && !maBS.trim().isEmpty()) {
            sql.append(" AND UPPER(MABS) LIKE ?");
            params.add("%" + maBS.trim().toUpperCase() + "%");
        }
        if (ngayBan != null && !ngayBan.trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYBAN, 'YYYY-MM-DD') = ?");
            params.add(ngayBan.trim());
        }
        if (thanhTien != null && !thanhTien.trim().isEmpty()) {
            sql.append(" AND UPPER(THANHTIEN) LIKE ?");
            params.add("%" + thanhTien.trim().toUpperCase() + "%");
        }
        if (trangThaiTT != null && !trangThaiTT.trim().isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAITT) LIKE ?");
            params.add("%" + trangThaiTT.trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Prescription p = new Prescription(
                    rs.getString("MADT"),
                    rs.getString("MADS"),
                    rs.getString("MABS"),
                    rs.getString("MABN"),
                    rs.getString("GIOITINHBN"),
                    rs.getString("LICHSU_BENHLY_BN"),
                    rs.getString("DIUNGBN"),
                    rs.getBlob("FILEDONTHUOC"),
                    rs.getString("GHICHU"),
                    rs.getString("NGAYBAN"),
                    rs.getString("THANHTIEN"),
                    rs.getString("TRANGTHAITT")
                );
                list.add(p);
            }
        }
        return list;
    }

//    public Prescription getPrescriptionDetails(String maDT) throws SQLException, ClassNotFoundException {
//        Prescription prescription = null;
//
//        String sql = "SELECT * FROM DONTHUOC WHERE MADT = ?";
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, maDT);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    prescription = new Prescription();
//                    prescription.maDT = rs.getString("MADT");
//                    prescription.maDS = rs.getString("MADS");
//                    prescription.maBS = rs.getString("MABS");
//                    prescription.maBN = rs.getString("MABN");
//                    prescription.gioiTinhBN = rs.getString("GIOITINHBN");
//                    prescription.lichSuBenhLyBN = rs.getString("LICHSUBENHLYBN");
//                    prescription.diUngBN = rs.getString("DIUNG_BN");
//                    prescription.fileDonThuoc = rs.getBytes("FILE_DONTHUOC"); // Blob
//                    prescription.ghiChu = rs.getString("GHICHU");
//                    prescription.ngayBan = rs.getString("NGAYBAN");
//                    prescription.thanhTien = rs.getString("THANHTIEN");
//                    prescription.trangThaiTT = rs.getString("TRANGTHAI_TT");
//                }
//            }
//        }
//
//        return prescription;
//    }

}

