/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import models.menuPa.AccountModel;
import views.patient.menu.AccountView;

public class AccountController {
    private AccountModel model;
    private AccountView view;

    public AccountController(AccountModel model, AccountView view) {
        this.model = model;
        this.view = view;
        initView();
        initController();
    }

    private void initView() {
        view.setUsername(model.getUsername());
        view.setPassword(model.getPassword());
        view.setConfirmPassword(model.getPassword());
        view.setEditable(false);

        view.getBtnSave().setEnabled(false);
        view.getBtnCancel().setEnabled(false);
    }

    private void initController() {
        view.getBtnEdit().addActionListener(e -> enableEditing());
        view.getBtnSave().addActionListener(e -> saveChanges());
        view.getBtnCancel().addActionListener(e -> cancelEditing());
    }

    private void enableEditing() {
        view.setEditable(true);
        view.getBtnSave().setEnabled(true);
        view.getBtnCancel().setEnabled(true);
        view.getBtnEdit().setEnabled(false);
    }

    private void saveChanges() {
        String newPassword = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập mật khẩu và xác nhận mật khẩu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view, "Mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật model
        model.setPassword(newPassword);

        // Tắt chế độ chỉnh sửa
        view.setEditable(false);
        view.getBtnSave().setEnabled(false);
        view.getBtnCancel().setEnabled(false);
        view.getBtnEdit().setEnabled(true);

        JOptionPane.showMessageDialog(view, "Cập nhật mật khẩu thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelEditing() {
        // Quay về dữ liệu cũ
        view.setPassword(model.getPassword());
        view.setConfirmPassword(model.getPassword());

        view.setEditable(false);
        view.getBtnSave().setEnabled(false);
        view.getBtnCancel().setEnabled(false);
        view.getBtnEdit().setEnabled(true);
    }
}
