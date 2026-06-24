/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.patient.PayBillModel;
import views.patient.PayBill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import views.patient.PayBill;

public class PayBillController {
    private PayBillModel model;
    private PayBill view;
    private JButton btnSearch;
    private JTable tableThuoc, tableHDKham, tableDieuTri;
    private JTextField txtSearch, txtMaHD, txtTongTien;
    private JComboBox cbTrangThai;

    public PayBillController(PayBillModel model, PayBill view) {
        this.model = model;
        this.view = view;

        initController();
        loadData();
    }

    private void initController() {
        btnSearch.addActionListener(e -> searchBills());
    }

    private void loadData() {
        try {
            loadThuocBills();
            loadHoaDonKhamBills();
            loadDieuTriBills();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadThuocBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = model.getThuocBills();
        String[] columns = {"Mã ĐT", "Mã DS", "Mã BS", "Mã BN", "Giới tính BN", "Ngày sinh BN",
                            "Lịch sử bệnh lý", "Dị ứng BN", "File đơn thuốc", "Ghi chú",
                            "Ngày bán", "Thành tiền", "Trạng thái"};
        DefaultTableModel dtm = new DefaultTableModel(columns, 0);
        for (Object[] row : data) dtm.addRow(row);
        setTableModel(tableThuoc, dtm);
    }

    private void loadHoaDonKhamBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = model.getHoaDonKhamBills();
        String[] columns = {"Mã HĐ KB", "Mã khám", "Ngày lập", "Tổng tiền",
                            "Phương thức TT", "Ghi chú", "Trạng thái"};
        DefaultTableModel dtm = new DefaultTableModel(columns, 0);
        for (Object[] row : data) dtm.addRow(row);
        setTableModel(tableHDKham, dtm);
    }

    private void loadDieuTriBills() throws SQLException, ClassNotFoundException {
        List<Object[]> data = model.getDieuTriBills();
        String[] columns = {"Mã ĐT", "Mã BS", "Mã BN", "Ngày ĐT", "Tổng tiền",
                            "Ghi chú", "Trạng thái"};
        DefaultTableModel dtm = new DefaultTableModel(columns, 0);
        for (Object[] row : data) dtm.addRow(row);
        setTableModel(tableDieuTri, dtm);
    }

    private void searchBills() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String maHD = txtMaHD.getText().trim().toLowerCase();
        String tongTien = txtTongTien.getText().trim().toLowerCase();
        String trangThai = cbTrangThai.getSelectedItem().toString().toLowerCase();

        try {
            loadData();

            filterTable(tableThuoc, keyword, maHD, tongTien, trangThai);
            filterTable(tableHDKham, keyword, maHD, tongTien, trangThai);
            filterTable(tableDieuTri, keyword, maHD, tongTien, trangThai);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }
    
    private void setTableModel(JTable table, DefaultTableModel model) {
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(24); 

        table.setDefaultEditor(Object.class, null);

        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }


    private void filterTable(JTable table, String keyword, String maHD, String tongTien, String trangThai) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            boolean matches = false;
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object val = model.getValueAt(i, j);
                if (val != null && val.toString().toLowerCase().contains(keyword)) {
                    matches = true;
                    break;
                }
            }
            if (!matches) {
                model.removeRow(i);
                continue;
            }
            
            if (!maHD.isEmpty() && !model.getValueAt(i, 0).toString().toLowerCase().contains(maHD)) {
                model.removeRow(i);
                continue;
            }
            if (!tongTien.isEmpty() && !model.getValueAt(i, model.getColumnCount() - 2).toString().toLowerCase().contains(tongTien)) {
                model.removeRow(i);
                continue;
            }
            if (!trangThai.equals("tất cả") &&
                !model.getValueAt(i, model.getColumnCount() - 1).toString().toLowerCase().contains(trangThai)) {
                model.removeRow(i);
            }
        }
    }
}

