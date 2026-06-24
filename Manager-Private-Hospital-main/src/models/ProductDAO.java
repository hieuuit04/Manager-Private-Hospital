/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ProductDAO {
    public List<ProductModel> searchProducts(Map<String, String> filters) throws SQLException, ClassNotFoundException {
        List<ProductModel> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM SANPHAM WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filters.containsKey("MASP")) {
            sql.append(" AND UPPER(MASP) LIKE ?");
            params.add("%" + filters.get("MASP").toUpperCase() + "%");
        }
        if (filters.containsKey("TENSP")) {
            sql.append(" AND UPPER(TENSP) LIKE ?");
            params.add("%" + filters.get("TENSP").toUpperCase() + "%");
        }
        if (filters.containsKey("TENNPP")) {
            sql.append(" AND UPPER(TENNPP) LIKE ?");
            params.add("%" + filters.get("TENNPP").toUpperCase() + "%");
        }
        if (filters.containsKey("THANHPHAN")) {
            sql.append(" AND UPPER(THANHPHAN) LIKE ?");
            params.add("%" + filters.get("THANHPHAN").toUpperCase() + "%");
        }
        if (filters.containsKey("MOTA")) {
            sql.append(" AND UPPER(MOTA) LIKE ?");
            params.add("%" + filters.get("MOTA").toUpperCase() + "%");
        }
        if (filters.containsKey("DONGIA")) {
            sql.append(" AND DONGIA = ?");
            params.add(Double.valueOf(filters.get("DONGIA")));
        }
        if (filters.containsKey("UUDAI")) {
            sql.append(" AND UUDAI = ?");
            params.add(Integer.valueOf(filters.get("UUDAI")));
        }
        if (filters.containsKey("NSX")) {
            sql.append(" AND TO_CHAR(NSX, 'YYYY-MM-DD') = ?");
            params.add(filters.get("NSX"));
        }
        if (filters.containsKey("HSD")) {
            sql.append(" AND TO_CHAR(HSD, 'YYYY-MM-DD') = ?");
            params.add(filters.get("HSD"));
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProductModel p = new ProductModel();
                p.setMaSP(rs.getString("MASP"));
                p.setTenSP(rs.getString("TENSP"));
                p.setNhaPP(rs.getString("TENNPP"));
                p.setDvt(rs.getString("DVT"));
                p.setThanhPhan(rs.getString("THANHPHAN"));
                p.setLuuY(rs.getString("LUUY"));
                p.setCachDung(rs.getString("CACHDUNG"));
                p.setBaoQuan(rs.getString("BAOQUAN"));
                p.setNsx(rs.getString("NSX"));
                p.setHsd(rs.getString("HSD"));
                p.setDonGia(rs.getDouble("DONGIA"));
                p.setUuDai(rs.getInt("UUDAI"));
                p.setMoTa(rs.getString("MOTA"));
                products.add(p);
            }
        }

        return products;
    }
}
