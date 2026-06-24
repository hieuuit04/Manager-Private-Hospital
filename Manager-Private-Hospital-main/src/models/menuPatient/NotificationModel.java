/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.menuPa;

import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationModel {
    public static class NotificationData {
        public String maTB;
        public String userId;
        public String noiDung;
        public String ngayThongBao;
        public String trangThai;
        public String loai;

        public NotificationData(String maTB, String userId, String noiDung, String ngayThongBao, String trangThai, String loai) {
            this.maTB = maTB;
            this.userId = userId;
            this.noiDung = noiDung;
            this.ngayThongBao = ngayThongBao;
            this.trangThai = trangThai;
            this.loai = loai;
        }
    }

    public List<NotificationData> getNotifications(String patientId, String maTBFilter, String noiDungFilter, String ngayTBFilter,
                                                  String trangThaiFilter, String loaiFilter) throws SQLException, ClassNotFoundException {
        List<NotificationData> notifications = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM THONGBAO WHERE USER_ID = ? ORDER BY NGAYTHONGBAO DESC");
        List<Object> params = new ArrayList<>();
        params.add(patientId);

        if (maTBFilter != null && !maTBFilter.isEmpty()) {
            sql.append(" AND UPPER(MATB) LIKE ?");
            params.add("%" + maTBFilter.toUpperCase() + "%");
        }
        if (noiDungFilter != null && !noiDungFilter.isEmpty()) {
            sql.append(" AND UPPER(NOIDUNG) LIKE ?");
            params.add("%" + noiDungFilter.toUpperCase() + "%");
        }
        if (ngayTBFilter != null && !ngayTBFilter.isEmpty()) {
            sql.append(" AND TO_CHAR(NGAYTHONGBAO, 'YYYY-MM-DD') = ?");
            params.add(ngayTBFilter);
        }
        if (trangThaiFilter != null && !trangThaiFilter.isEmpty()) {
            sql.append(" AND UPPER(TRANGTHAI) LIKE ?");
            params.add("%" + trangThaiFilter.toUpperCase() + "%");
        }
        if (loaiFilter != null && !loaiFilter.isEmpty()) {
            sql.append(" AND UPPER(LOAI) LIKE ?");
            params.add("%" + loaiFilter.toUpperCase() + "%");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                notifications.add(new NotificationData(
                    rs.getString("MATB"),
                    rs.getString("USER_ID"),
                    rs.getString("NOIDUNG"),
                    rs.getString("NGAYTHONGBAO"),
                    rs.getString("TRANGTHAI"),
                    rs.getString("LOAI")
                ));
            }
        }
        return notifications;
    }

    public boolean markAsRead(String maTB) throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE THONGBAO SET TRANGTHAI = 'Đã đọc' WHERE MATB = ?")) {
            stmt.setString(1, maTB);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }
}

