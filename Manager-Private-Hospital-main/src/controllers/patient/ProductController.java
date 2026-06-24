/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;


import models.ProductDAO;
import models.ProductModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductController {
    private final ProductDAO productDAO;

    public ProductController() {
        productDAO = new ProductDAO();
    }

    public List<ProductModel> getFilteredProducts(ProductModel filter) throws SQLException, ClassNotFoundException {
        Map<String, String> filters = new HashMap<>();

        if (filter.getMaSP() != null && !filter.getMaSP().isEmpty()) {
            filters.put("MASP", filter.getMaSP());
        }
        if (filter.getTenSP() != null && !filter.getTenSP().isEmpty()) {
            filters.put("TENSP", filter.getTenSP());
        }
        if (filter.getNhaPP() != null && !filter.getNhaPP().isEmpty()) {
            filters.put("TENNPP", filter.getNhaPP());
        }
        if (filter.getThanhPhan() != null && !filter.getThanhPhan().isEmpty()) {
            filters.put("THANHPHAN", filter.getThanhPhan());
        }
        if (filter.getMoTa() != null && !filter.getMoTa().isEmpty()) {
            filters.put("MOTA", filter.getMoTa());
        }
        if (filter.getDonGia() != 0) {
            filters.put("DONGIA", String.valueOf(filter.getDonGia()));
        }
        if (filter.getUuDai() != 0) {
            filters.put("UUDAI", String.valueOf(filter.getUuDai()));
        }
        if (filter.getNsx() != null) {
            filters.put("NSX", filter.getNsx().toString());
        }
        if (filter.getHsd() != null) {
            filters.put("HSD", filter.getHsd().toString());
        }

        return productDAO.searchProducts(filters);
    }
}




