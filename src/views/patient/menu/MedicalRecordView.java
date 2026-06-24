/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import java.awt.*;

public class MedicalRecordView extends JPanel {
    private JLabel lblMaBN, lblSoBHYT, lblLSBL, lblDiUng, lblNhomMau;
    private JLabel lblMaBNValue, lblBHYTValue, lblLSBLValue, lblDiUngValue, lblNhomMauValue;

    private JTextField txtMaBN, txtBHYT, txtLSBL, txtDiUng, txtNhomMau;

    private JButton btnUpdate, btnBack;

    private boolean isEditing = false;

    public MedicalRecordView() {
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels bên trái
        lblMaBN = new JLabel("Mã bệnh nhân:");
        lblSoBHYT = new JLabel("Số BHYT:");
        lblLSBL = new JLabel("Lịch sử bệnh lý:");
        lblDiUng = new JLabel("Dị ứng:");
        lblNhomMau = new JLabel("Nhóm máu:");

        // Labels hiện thị dữ liệu
        lblMaBNValue = new JLabel();
        lblBHYTValue = new JLabel();
        lblLSBLValue = new JLabel();
        lblDiUngValue = new JLabel();
        lblNhomMauValue = new JLabel();

        // TextFields chỉnh sửa
        txtMaBN = new JTextField(20);
        txtMaBN.setEditable(false); // Mã BN thường không cho sửa
        txtBHYT = new JTextField(20);
        txtLSBL = new JTextField(20);
        txtDiUng = new JTextField(20);
        txtNhomMau = new JTextField(20);

        // Nút
        btnUpdate = new JButton("Sửa");
        btnBack = new JButton("Quay lại");

        // Hàng 0: MaBN
        gbc.gridx = 0; gbc.gridy = 0;
        this.add(lblMaBN, gbc);
        gbc.gridx = 1;
        this.add(lblMaBNValue, gbc);
        this.add(txtMaBN, gbc);

        // Hàng 1: SoBHYT
        gbc.gridx = 0; gbc.gridy = 1;
        this.add(lblSoBHYT, gbc);
        gbc.gridx = 1;
        this.add(lblBHYTValue, gbc);
        this.add(txtBHYT, gbc);

        // Hàng 2: LSBL
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(lblLSBL, gbc);
        gbc.gridx = 1;
        this.add(lblLSBLValue, gbc);
        this.add(txtLSBL, gbc);

        // Hàng 3: DiUng
        gbc.gridx = 0; gbc.gridy = 3;
        this.add(lblDiUng, gbc);
        gbc.gridx = 1;
        this.add(lblDiUngValue, gbc);
        this.add(txtDiUng, gbc);

        // Hàng 4: NhomMau
        gbc.gridx = 0; gbc.gridy = 4;
        this.add(lblNhomMau, gbc);
        gbc.gridx = 1;
        this.add(lblNhomMauValue, gbc);
        this.add(txtNhomMau, gbc);

        // Hàng 5: nút
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnUpdate);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(btnPanel, gbc);

        // Khởi tạo trạng thái view: ẩn textfields, chỉ hiện label
        switchToLabels();
    }

    // Chuyển sang chế độ chỉnh sửa: ẩn label, hiện textfield với giá trị hiện tại
    public void switchToTextFields() {
        isEditing = true;

        lblMaBNValue.setVisible(false);
        lblBHYTValue.setVisible(false);
        lblLSBLValue.setVisible(false);
        lblDiUngValue.setVisible(false);
        lblNhomMauValue.setVisible(false);

        txtMaBN.setVisible(true);
        txtBHYT.setVisible(true);
        txtLSBL.setVisible(true);
        txtDiUng.setVisible(true);
        txtNhomMau.setVisible(true);

        // Đưa dữ liệu label sang textfield
        txtMaBN.setText(lblMaBNValue.getText());
        txtBHYT.setText(lblBHYTValue.getText());
        txtLSBL.setText(lblLSBLValue.getText());
        txtDiUng.setText(lblDiUngValue.getText());
        txtNhomMau.setText(lblNhomMauValue.getText());
    }

    // Chuyển sang chế độ xem: ẩn textfield, hiện label, lấy dữ liệu từ textfield sang label
    public void switchToLabels() {
        isEditing = false;

        // Lấy dữ liệu từ textfield gán vào label
        lblMaBNValue.setText(txtMaBN.getText());
        lblBHYTValue.setText(txtBHYT.getText());
        lblLSBLValue.setText(txtLSBL.getText());
        lblDiUngValue.setText(txtDiUng.getText());
        lblNhomMauValue.setText(txtNhomMau.getText());

        txtMaBN.setVisible(false);
        txtBHYT.setVisible(false);
        txtLSBL.setVisible(false);
        txtDiUng.setVisible(false);
        txtNhomMau.setVisible(false);

        lblMaBNValue.setVisible(true);
        lblBHYTValue.setVisible(true);
        lblLSBLValue.setVisible(true);
        lblDiUngValue.setVisible(true);
        lblNhomMauValue.setVisible(true);
    }

    // Các getter/setter cho dữ liệu
    public String getMaBN() {
        return isEditing ? txtMaBN.getText() : lblMaBNValue.getText();
    }
    public void setMaBN(String maBN) {
        lblMaBNValue.setText(maBN);
        txtMaBN.setText(maBN);
    }

    public String getBHYT() {
        return isEditing ? txtBHYT.getText() : lblBHYTValue.getText();
    }
    public void setBHYT(String bhyt) {
        lblBHYTValue.setText(bhyt);
        txtBHYT.setText(bhyt);
    }

    public String getLSBL() {
        return isEditing ? txtLSBL.getText() : lblLSBLValue.getText();
    }
    public void setLSBL(String lsbl) {
        lblLSBLValue.setText(lsbl);
        txtLSBL.setText(lsbl);
    }

    public String getDiUng() {
        return isEditing ? txtDiUng.getText() : lblDiUngValue.getText();
    }
    public void setDiUng(String diUng) {
        lblDiUngValue.setText(diUng);
        txtDiUng.setText(diUng);
    }

    public String getNhomMau() {
        return isEditing ? txtNhomMau.getText() : lblNhomMauValue.getText();
    }
    public void setNhomMau(String nhomMau) {
        lblNhomMauValue.setText(nhomMau);
        txtNhomMau.setText(nhomMau);
    }

    // Getter cho nút để Controller đăng ký sự kiện
    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnBack() {
        return btnBack;
    }
}
