/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HisMedExamModel {
    private final String patientId;

    public HisMedExamModel(String patientId) {
        this.patientId = patientId;
    }

    public List<Object[]> getExamHistory(String maKham, String maLich, String maBS, String maDT, String ngayKham, String ngayTaiKham) throws SQLException, ClassNotFoundException {
        List<Object[]> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM KHAM WHERE MABN = ?");
        List<Object> params = new ArrayList<>();
        params.add(patientId);

        if (maKham != null && !maKham.trim().isEmpty()) {
            sql.append(" AND UPPER(MAKHAM) LIKE ?");
            params.add("%" + maKham.trim().toUpperCase() + "%");
        }
        if (maLich != null && !maLich.trim().isEmpty()) {
            sql.append(" AND UPPER(MALICH) LIKE ?");
            params.add("%" + maLich.trim().toUpperCase() + "%");
        }
        if (maBS != null && !maBS.trim().isEmpty()) {
            sql.append(" AND UPPER(MABS) LIKE ?");
            params.add("%" + maBS.trim().toUpperCase() + "%");
        }
        if (maDT != null && !maDT.trim().isEmpty()) {
            sql.append(" AND UPPER(MADT) LIKE ?");
            params.add("%" + maDT.trim().toUpperCase() + "%");
        }
        if (ngayKham != null && !ngayKham.trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYKHAM, 'YYYY-MM-DD') = ?");
            params.add(ngayKham.trim());
        }
        if (ngayTaiKham != null && !ngayTaiKham.trim().isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYTAIKHAM, 'YYYY-MM-DD') = ?");
            params.add(ngayTaiKham.trim());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MAKHAM"),
                    rs.getString("MALICH"),
                    rs.getString("MABS"),
                    rs.getString("MABN"),
                    rs.getString("MADT"),
                    rs.getDate("NGAYKHAM"),
                    rs.getDate("NGAYTAIKHAM"),
                    rs.getString("KETLUAN"),
                    rs.getString("LUUY")
                };
                list.add(row);
            }
        }
        return list;
    }
}
