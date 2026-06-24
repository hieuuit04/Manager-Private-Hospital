/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class ServiceModel {

    public static class Service {
        private String maDV;
        private String tenDV;
        private String moTaDV;
        private String uuDai;
        private String donGia;

        public Service(String maDV, String tenDV, String moTaDV, String uuDai, String donGia) {
            this.maDV = maDV;
            this.tenDV = tenDV;
            this.moTaDV = moTaDV;
            this.uuDai = uuDai;
            this.donGia = donGia;
        }

        public String getMaDV() { return maDV; }
        public String getTenDV() { return tenDV; }
        public String getMoTaDV() { return moTaDV; }
        public String getUuDai() { return uuDai; }
        public String getDonGia() { return donGia; }
    }

    public List<Service> searchServices(String maDV, String tenDV, String uuDai, String donGia) throws SQLException, ClassNotFoundException {
        List<Service> services = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM DICHVUKHAM WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (maDV != null && !maDV.trim().isEmpty()) {
            sql.append(" AND UPPER(MADV) LIKE ?");
            params.add("%" + maDV.trim().toUpperCase() + "%");
        }
        if (tenDV != null && !tenDV.trim().isEmpty()) {
            sql.append(" AND UPPER(TENDV) LIKE ?");
            params.add("%" + tenDV.trim().toUpperCase() + "%");
        }
        if (uuDai != null && !uuDai.trim().isEmpty()) {
            sql.append(" AND UPPER(UUDAI) LIKE ?");
            params.add("%" + uuDai.trim().toUpperCase() + "%");
        }
        if (donGia != null && !donGia.trim().isEmpty()) {
            sql.append(" AND UPPER(DONGIA) LIKE ?");
            params.add("%" + donGia.trim().toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                services.add(new Service(
                        rs.getString("MADV"),
                        rs.getString("TENDV"),
                        rs.getString("MOTADV"),
                        rs.getString("UUDAI"),
                        rs.getString("DONGIA")
                ));
            }
        }
        return services;
    }
}

