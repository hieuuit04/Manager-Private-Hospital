/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import models.menuPa.MedicalRecordModel;
import views.patient.menu.MedicalRecordView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MedicalRecordController {

    private MedicalRecordModel model;
    private MedicalRecordView view;
    private boolean isEditing = false;

    public MedicalRecordController(MedicalRecordModel model, MedicalRecordView view, String patientId) {
        this.model = model;
        this.view = view;

        // Load dữ liệu từ DB vào model rồi cập nhật lên view
        try {
            model.loadFromDB(patientId);
            updateViewFromModel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }

        // Ban đầu không cho chỉnh sửa
        setEditing(false);

        // Đăng ký sự kiện nút Update
        view.getBtnUpdate().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isEditing) {
                    // Bắt đầu chỉnh sửa
                    setEditing(true);
                    view.getBtnUpdate().setText("Lưu");
                } else {
                    // Lưu dữ liệu
                    saveData();
                }
            }
        });

        // Đăng ký sự kiện nút Back
        view.getBtnBack().addActionListener(e -> {
            if (isEditing) {
                // Nếu đang chỉnh sửa thì hỏi có hủy không
                int confirm = JOptionPane.showConfirmDialog(view,
                        "Bạn có muốn hủy thay đổi không?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    cancelEditing();
                }
            } else {
                // Đóng hoặc trở về màn hình trước (cái này bạn tự xử lý)
                // Ví dụ:
                // view.setVisible(false);
            }
        });
    }

    // Cập nhật dữ liệu từ model lên view
    private void updateViewFromModel() {
        view.setMaBN(model.getMaBN());
        view.setBHYT(model.getSoBHYT());
        view.setLSBL(model.getLichSuBenhLy());
        view.setDiUng(model.getDiUng());
        view.setNhomMau(model.getNhomMau());
        view.switchToLabels();
    }

    // Cập nhật dữ liệu từ view xuống model
    private void updateModelFromView() {
        model.setMaBN(view.getMaBN());
        model.setSoBHYT(view.getBHYT());
        model.setLichSuBenhLy(view.getLSBL());
        model.setDiUng(view.getDiUng());
        model.setNhomMau(view.getNhomMau());
    }

    // Thiết lập trạng thái chỉnh sửa: true thì cho edit, false thì khóa
    private void setEditing(boolean editing) {
        this.isEditing = editing;
        if (editing) {
            view.switchToTextFields();
        } else {
            view.switchToLabels();
        }
        // Ở đây bạn có thể thêm để bật/tắt các field nếu cần
    }

    // Lưu dữ liệu vào DB
    private void saveData() {
        try {
            updateModelFromView();

            // Kiểm tra dữ liệu hợp lệ nếu cần, ví dụ:
            if (model.getMaBN() == null || model.getMaBN().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Mã bệnh nhân không được để trống!");
                return;
            }

            model.saveToDB();
            JOptionPane.showMessageDialog(view, "Lưu thành công!");
            setEditing(false);
            view.getBtnUpdate().setText("Sửa");
            updateViewFromModel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi lưu dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Hủy chỉnh sửa
    private void cancelEditing() {
        setEditing(false);
        updateViewFromModel();
        view.getBtnUpdate().setText("Sửa");
    }
}
