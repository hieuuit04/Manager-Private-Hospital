/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import java.awt.*;

public class PersonalInfoView extends JPanel {
    private JLabel lblFullName, lblPhone, lblEmail, lblGender, lblBirthDate, lblAddress;
    private JLabel lblFullNameVal, lblPhoneVal, lblEmailVal, lblGenderVal, lblBirthDateVal, lblAddressVal;
    private JTextField txtFullName, txtPhone, txtEmail, txtGender, txtBirthDate, txtAddress;
    private JButton btnEdit, btnSave, btnCancel;
    private boolean isEditing = false;
    private String patientId;

    public PersonalInfoView(String patientId, Runnable onBack) {
        this.patientId = patientId;
        initComponents();
        switchToLabels();
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        lblFullName = new JLabel("Họ tên:");
        lblPhone = new JLabel("Số điện thoại:");
        lblEmail = new JLabel("Email:");
        lblGender = new JLabel("Giới tính:");
        lblBirthDate = new JLabel("Ngày sinh:");
        lblAddress = new JLabel("Địa chỉ:");

        lblFullNameVal = new JLabel();
        lblPhoneVal = new JLabel();
        lblEmailVal = new JLabel();
        lblGenderVal = new JLabel();
        lblBirthDateVal = new JLabel();
        lblAddressVal = new JLabel();

        txtFullName = new JTextField(20);
        txtPhone = new JTextField(20);
        txtEmail = new JTextField(20);
        txtGender = new JTextField(20);
        txtBirthDate = new JTextField(20);
        txtAddress = new JTextField(20);

        btnEdit = new JButton("Sửa");
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        // Dòng 0: Họ tên
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(lblFullName, gbc);
        gbc.gridx = 1;
        this.add(lblFullNameVal, gbc);
        this.add(txtFullName, gbc);

        // Dòng 1: Số điện thoại
        gbc.gridx = 0; gbc.gridy = 1;
        this.add(lblPhone, gbc);
        gbc.gridx = 1;
        this.add(lblPhoneVal, gbc);
        this.add(txtPhone, gbc);

        // Dòng 2: Email
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(lblEmail, gbc);
        gbc.gridx = 1;
        this.add(lblEmailVal, gbc);
        this.add(txtEmail, gbc);

        // Dòng 3: Giới tính
        gbc.gridx = 0; gbc.gridy = 3;
        this.add(lblGender, gbc);
        gbc.gridx = 1;
        this.add(lblGenderVal, gbc);
        this.add(txtGender, gbc);

        // Dòng 4: Ngày sinh
        gbc.gridx = 0; gbc.gridy = 4;
        this.add(lblBirthDate, gbc);
        gbc.gridx = 1;
        this.add(lblBirthDateVal, gbc);
        this.add(txtBirthDate, gbc);

        // Dòng 5: Địa chỉ
        gbc.gridx = 0; gbc.gridy = 5;
        this.add(lblAddress, gbc);
        gbc.gridx = 1;
        this.add(lblAddressVal, gbc);
        this.add(txtAddress, gbc);

        // Panel nút
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnEdit);
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(btnPanel, gbc);
    }

    public void switchToLabels() {
        isEditing = false;

        lblFullNameVal.setText(txtFullName.getText());
        lblPhoneVal.setText(txtPhone.getText());
        lblEmailVal.setText(txtEmail.getText());
        lblGenderVal.setText(txtGender.getText());
        lblBirthDateVal.setText(txtBirthDate.getText());
        lblAddressVal.setText(txtAddress.getText());

        txtFullName.setVisible(false);
        txtPhone.setVisible(false);
        txtEmail.setVisible(false);
        txtGender.setVisible(false);
        txtBirthDate.setVisible(false);
        txtAddress.setVisible(false);

        lblFullNameVal.setVisible(true);
        lblPhoneVal.setVisible(true);
        lblEmailVal.setVisible(true);
        lblGenderVal.setVisible(true);
        lblBirthDateVal.setVisible(true);
        lblAddressVal.setVisible(true);

        btnEdit.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }

    public void switchToTextFields() {
        isEditing = true;

        txtFullName.setText(lblFullNameVal.getText());
        txtPhone.setText(lblPhoneVal.getText());
        txtEmail.setText(lblEmailVal.getText());
        txtGender.setText(lblGenderVal.getText());
        txtBirthDate.setText(lblBirthDateVal.getText());
        txtAddress.setText(lblAddressVal.getText());

        txtFullName.setVisible(true);
        txtPhone.setVisible(true);
        txtEmail.setVisible(true);
        txtGender.setVisible(true);
        txtBirthDate.setVisible(true);
        txtAddress.setVisible(true);

        lblFullNameVal.setVisible(false);
        lblPhoneVal.setVisible(false);
        lblEmailVal.setVisible(false);
        lblGenderVal.setVisible(false);
        lblBirthDateVal.setVisible(false);
        lblAddressVal.setVisible(false);

        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }

    // Getter dữ liệu (từ textfields khi đang chỉnh sửa, hoặc labels khi không)
    public String getFullName() { return isEditing ? txtFullName.getText() : lblFullNameVal.getText(); }
    public String getPhone() { return isEditing ? txtPhone.getText() : lblPhoneVal.getText(); }
    public String getEmail() { return isEditing ? txtEmail.getText() : lblEmailVal.getText(); }
    public String getGender() { return isEditing ? txtGender.getText() : lblGenderVal.getText(); }
    public String getBirthDate() { return isEditing ? txtBirthDate.getText() : lblBirthDateVal.getText(); }
    public String getAddress() { return isEditing ? txtAddress.getText() : lblAddressVal.getText(); }

    // Setter để đổ dữ liệu model ra view
    public void setFullName(String fullName) { lblFullNameVal.setText(fullName); txtFullName.setText(fullName); }
    public void setPhone(String phone) { lblPhoneVal.setText(phone); txtPhone.setText(phone); }
    public void setEmail(String email) { lblEmailVal.setText(email); txtEmail.setText(email); }
    public void setGender(String gender) { lblGenderVal.setText(gender); txtGender.setText(gender); }
    public void setBirthDate(String birthDate) { lblBirthDateVal.setText(birthDate); txtBirthDate.setText(birthDate); }
    public void setAddress(String address) { lblAddressVal.setText(address); txtAddress.setText(address); }

    // Nút để Controller add ActionListener
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnCancel() { return btnCancel; }
}

