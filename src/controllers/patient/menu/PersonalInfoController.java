/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import models.menuPa.PersonalInfoModel;
import views.patient.menu.PersonalInfoView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonalInfoController {
    private PersonalInfoModel model;
    private PersonalInfoView view;

    public PersonalInfoController(PersonalInfoModel model, PersonalInfoView view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
    }

    private void initView() {
        view.setFullName(model.getFullName());
        view.setPhone(model.getPhone());
        view.setEmail(model.getEmail());
        view.setGender(model.getGender());
        view.setBirthDate(model.getBirthDate());
        view.setAddress(model.getAddress());
        view.switchToLabels();
    }

    private void initController() {
        view.getBtnEdit().addActionListener(e -> view.switchToTextFields());

        view.getBtnCancel().addActionListener(e -> {
            // Khi hủy chỉnh sửa, đổ lại dữ liệu model ra view
            initView();
        });

        view.getBtnSave().addActionListener(e -> {
            // Lấy dữ liệu từ view
            String fullName = view.getFullName();
            String phone = view.getPhone();
            String email = view.getEmail();
            String gender = view.getGender();
            String birthDate = view.getBirthDate();
            String address = view.getAddress();

            // Có thể validate dữ liệu ở đây
            if(fullName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Họ tên không được để trống");
                return;
            }

            // Cập nhật model
            model.setFullName(fullName);
            model.setPhone(phone);
            model.setEmail(email);
            model.setGender(gender);
            model.setBirthDate(birthDate);
            model.setAddress(address);

            // TODO: Lưu model vào database ở đây nếu có DB

            view.switchToLabels();
            JOptionPane.showMessageDialog(null, "Cập nhật thông tin cá nhân thành công!");
        });
    }
}
