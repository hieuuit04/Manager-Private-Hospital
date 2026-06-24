/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import java.awt.*;

public class AccountView extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnEdit;
    private JButton btnSave;
    private JButton btnCancel;

    public AccountView() {
        initComponents();
        setLayout(new BorderLayout());
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void initComponents() {
        txtUsername = new JTextField(20);
        txtUsername.setEditable(false); // Username không sửa được

        txtPassword = new JPasswordField(20);
        txtPassword.setEditable(false);

        txtConfirmPassword = new JPasswordField(20);
        txtConfirmPassword.setEditable(false);

        btnEdit = new JButton("Chỉnh sửa");
        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);

        gbc.gridx = 1;
        panel.add(txtConfirmPassword, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(btnEdit);
        panel.add(btnSave);
        panel.add(btnCancel);
        return panel;
    }

    // Getter cho controller lấy dữ liệu hoặc gắn sự kiện
    public String getUsername() {
        return txtUsername.getText();
    }

    public void setUsername(String username) {
        txtUsername.setText(username);
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public void setPassword(String password) {
        txtPassword.setText(password);
    }

    public String getConfirmPassword() {
        return new String(txtConfirmPassword.getPassword());
    }

    public void setConfirmPassword(String confirmPassword) {
        txtConfirmPassword.setText(confirmPassword);
    }

    public void setEditable(boolean editable) {
        txtPassword.setEditable(editable);
        txtConfirmPassword.setEditable(editable);
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }
}
