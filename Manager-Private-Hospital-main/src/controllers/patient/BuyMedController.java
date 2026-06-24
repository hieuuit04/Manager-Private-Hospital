/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.BuyMedModel;
import views.patient.BuyMed;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyMedController {

    private BuyMed view;
    private BuyMedModel model;
    private String patientId;

    private JTextField txtHoTen, txtGioiTinh, txtNgaySinh, txtDiaChi, txtSoDienThoai, txtFile;
    private JTextArea txtLichSuBenhLy, txtDiUng;
    private JButton btnUpload;
    private JButton btnGuiYeuCau;
    private File selectedFile;
        
    public BuyMedController(String patientId) throws SQLException, ClassNotFoundException {
        this.patientId = patientId;
        this.view = new BuyMed(patientId);
        this.model = new BuyMedModel();

        initController();
        loadPatientInfo();
        view.setVisible(true);
    }

    private void initController() {
        btnUpload.addActionListener(e -> chooseFile());
        btnGuiYeuCau.addActionListener(e -> sendRequest());
    }

    private void loadPatientInfo() {
        try {
            ResultSet rs = model.getPatientInfo(patientId);
            if (rs.next()) {
                txtHoTen.setText(rs.getString("HOTENND"));
                txtGioiTinh.setText(rs.getString("GIOITINH"));
                txtNgaySinh.setText(rs.getDate("NGAYSINH").toString());
                txtDiaChi.setText(rs.getString("DIACHI"));
                txtSoDienThoai.setText(rs.getString("SDT"));
                txtLichSuBenhLy.setText(rs.getString("LICHSU_BENHLY"));
                txtDiUng.setText(rs.getString("DIUNG"));
            } else {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin bệnh nhân.");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tải thông tin bệnh nhân.");
        }
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF files", "pdf"));
        int result = fileChooser.showOpenDialog(view);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
        }
    }
    
    private File getSelectedFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn tệp đơn thuốc");

        int userSelection = fileChooser.showOpenDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private void sendRequest() {
        
        try {
            boolean success = model.savePrescription(
                    patientId,
                    txtGioiTinh.getText(),
                    txtNgaySinh.getText(),
                    txtLichSuBenhLy.getText(),
                    txtDiUng.getText(),
                    getSelectedFile()
            );
            if (success) {
                JOptionPane.showMessageDialog(view, "Gửi đơn thuốc thành công!");
                // Có thể reset hoặc đóng view tùy ý
            } else {
                JOptionPane.showMessageDialog(view, "Gửi đơn thuốc thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi gửi đơn thuốc: " + ex.getMessage());
        }
    }
}

