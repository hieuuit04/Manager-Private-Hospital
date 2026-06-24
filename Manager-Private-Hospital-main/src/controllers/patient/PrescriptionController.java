/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.PrescriptionModel;
import views.patient.Prescription;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class PrescriptionController {

    private PrescriptionModel model;
    private Prescription view;
    private String patientId;
    private JTextField tfMaDT, tfMaDS, tfMaBS, tfNgayBan, tfThanhTien;
    private JComboBox cbTrangThai;
    private JButton btnSearch;
    private JTable tableThuoc;

    public PrescriptionController(String patientId) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        this.model = new PrescriptionModel();
        this.view = new Prescription(patientId);

        initController();
        loadAllPrescriptions();
        view.setVisible(true);
    }

    private void initController() {
        getBtnSearch().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchPrescriptions();
                } catch (SQLException ex) {
                    Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(PrescriptionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadAllPrescriptions() {
        try {
            java.util.List<PrescriptionModel.Prescription> list = model.getPrescriptions(patientId, null, null, null, null, null, null);
            updateTable(list);
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu đơn thuốc: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public JButton getBtnSearch() {
        return btnSearch; // btnSearch là JButton khai báo trong PrescriptionView
    }

    private String getMaDT() {
        return tfMaDT.getText().trim(); // tfMaDT là JTextField nhập mã đơn thuốc
    }

    private String getMaDS() {
        return tfMaDS.getText().trim(); // tfMaDS là JTextField nhập mã danh sách thuốc
    }

    private String getMaBS() {
        return tfMaBS.getText().trim(); // tfMaBS là JTextField nhập mã bác sĩ
    }

    private String getNgayBan() {
        return tfNgayBan.getText().trim(); // tfNgayBan là JTextField nhập ngày bán
    }

    private String getThanhTien() {
        return tfThanhTien.getText().trim(); // tfThanhTien là JTextField nhập thành tiền
    }

    private String getTrangThaiTT() {
        return cbTrangThai.getSelectedItem() != null ? cbTrangThai.getSelectedItem().toString() : "";  
        // cbTrangThai là JComboBox chọn trạng thái thanh toán
    }
    
    private void updateTable(List<PrescriptionModel.Prescription> list) {
        String[] columns = {
            "Mã ĐT", "Mã DS", "Mã BS", "Mã BN", "Giới tính BN", "Ngày sinh BN",
            "Lịch sử bệnh lý", "Dị ứng BN", "File đơn thuốc", "Ghi chú",
            "Ngày bán", "Thành tiền", "Trạng thái"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        tableThuoc.setModel(model);  // tableThuoc là JTable hiển thị đơn thuốc
        tableThuoc.setRowHeight(24);
        tableThuoc.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableThuoc.setDefaultEditor(Object.class, null); // không cho sửa dữ liệu trong bảng
    }



    private void searchPrescriptions() throws SQLException, ClassNotFoundException {
        java.util.List<PrescriptionModel.Prescription> list = model.getPrescriptions(
                patientId,
                getMaDT(),
                getMaDS(),
                getMaBS(),
                getNgayBan(),
                getThanhTien(),
                getTrangThaiTT()
        );
        updateTable(list);
    }
}
