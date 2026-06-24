/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.HisMedExamModel;
import views.patient.HisMedExam;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HisMedExamController {
    private final HisMedExamModel model;
    private final HisMedExam view;
    
    private JTextField tfMaKham, tfMaLich, tfMaBS, tfMaDT, tfNgayKham, tfNgayTaiKham;
    private JButton btnSearch;
    private JButton btnMed;

    public HisMedExamController(String patientId) throws ClassNotFoundException, SQLException {
        model = new HisMedExamModel(patientId);
        view = new HisMedExam(patientId);
        initController();
    }

    private void initController() throws ClassNotFoundException {
        loadTableData(null, null, null, null, null, null);

        getBtnSearch().addActionListener(e -> {
            try {
                searchAction();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HisMedExamController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        getBtnMed().addActionListener(e -> openMedicineView());

        view.setVisible(true);
    }

    private void searchAction() throws ClassNotFoundException {
        String maKham = getTfMaKham().getText();
        String maLich = getTfMaLich().getText();
        String maBS = getTfMaBS().getText();
        String maDT = getTfMaDT().getText();
        String ngayKham = getTfNgayKham().getText();
        String ngayTaiKham = getTfNgayTaiKham().getText();

        loadTableData(maKham, maLich, maBS, maDT, ngayKham, ngayTaiKham);
    }

    private void loadTableData(String maKham, String maLich, String maBS, String maDT, String ngayKham, String ngayTaiKham) throws ClassNotFoundException {
        try {
            List<Object[]> data = model.getExamHistory(maKham, maLich, maBS, maDT, ngayKham, ngayTaiKham);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMedicineView() {
        JOptionPane.showMessageDialog(view, "Chức năng Đơn thuốc sẽ được mở.");
    }
    
    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnMed() {
        return btnMed;
    }

    public JTextField getTfMaKham() {
        return tfMaKham;
    }
    public JTextField getTfMaLich() {
        return tfMaLich;
    }
    public JTextField getTfMaBS() {
        return tfMaBS;
    }
    public JTextField getTfMaDT() {
        return tfMaDT;
    }
    public JTextField getTfNgayKham() {
        return tfNgayKham;
    }
    public JTextField getTfNgayTaiKham() {
        return tfNgayTaiKham;
    }

}
