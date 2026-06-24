/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.User;
import models.UserDAO;
import views.RegisterForm;

import javax.swing.*;
import views.RegisterForm;

public class RegisterController {
    private final RegisterForm view;
    private JTextField txtUsername, txtPassword, txtFullName, txtEmail, txtPhone;

    public RegisterController(RegisterForm view) {
        this.view = view;
    }

    private void handleRegister() { 
        try {    
            if (true) {
                JOptionPane.showMessageDialog(view, "Đăng ký thành công!");
            } else {
                JOptionPane.showMessageDialog(view, "Đăng ký thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Đã xảy ra lỗi khi đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void getUserInput() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin bắt buộc.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleLogin() {
        view.dispose();
    }
}

