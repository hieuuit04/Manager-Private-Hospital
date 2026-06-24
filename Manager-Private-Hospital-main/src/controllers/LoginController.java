/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JOptionPane;
import models.User;
import models.UserDAO;
import views.LoginForm;
//import views.doctor.DoctorDashboard;
import views.patient.PatientDashboard;
import views.RegisterForm;

public class LoginController {
    private final LoginForm view;

    public LoginController(LoginForm view) {
        this.view = view;
    }
    private String username, password;
    public void handleLogin() {

        try {
            User user = UserDAO.login(username, password);
            if (user != null) {
                switch (user.getRole()) {
                    case "Bệnh nhân" -> new PatientDashboard(user.getName(), user.getId()).setVisible(true);
                    case "Bác sĩ" -> new PatientDashboard(user.getName(), user.getId()).setVisible(true);
                    default -> {
                        showMessage("Chưa hỗ trợ giao diện cho vai trò: " + user.getRole());
                        return;
                    }
                }    
            } else {
                showMessage("Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (Exception e) {
            showMessage("Lỗi khi đăng nhập: " + e.getMessage());
        }
    }

    // Hiển thị thông báo (có thể dùng JOptionPane)
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }


    public void handleRegister() {
        new RegisterForm().setVisible(true);
    }
}


