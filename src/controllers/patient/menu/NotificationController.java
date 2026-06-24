/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import models.menuPa.NotificationModel;
import models.menuPa.NotificationModel.NotificationData;
import views.patient.menu.NotificationView;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class NotificationController {
    private final NotificationModel model;
    private final NotificationView view;
    private final String patientId;

    public NotificationController(NotificationModel model, NotificationView view, String patientId) {
        this.model = model;
        this.view = view;
        this.patientId = patientId;
        initController();
        loadNotifications();  // Load all notifications initially
    }

    private void initController() {
        view.getBtnSearch().addActionListener(e -> loadNotifications());
        view.getBtnBack().addActionListener(e -> backToMain());
        view.getNotificationTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = view.getNotificationTable().getSelectedRow();
                    if (row != -1) {
                        showNotificationDetails(row);
                    }
                }
            }
        });
    }

    private void loadNotifications() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<NotificationData> notifications;

            @Override
            protected Void doInBackground() throws Exception {
                notifications = model.getNotifications(
                        patientId,
                        view.getTfMaTB(),
                        view.getTfNoiDung(),
                        view.getTfNgayTB(),
                        view.getTfTrangThai(),
                        view.getTfLoai());
                return null;
            }

            @Override
            protected void done() {
                view.clearTable();
                try {
                    get();
                    for (NotificationData n : notifications) {
                        view.addNotificationRow(new Object[]{
                                n.maTB, n.userId, n.noiDung, n.ngayThongBao, n.trangThai, n.loai
                        });
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu thông báo: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void showNotificationDetails(int row) {
        String maTB = (String) view.getNotificationTable().getValueAt(row, 0);
        String noiDung = (String) view.getNotificationTable().getValueAt(row, 2);
        String trangThai = (String) view.getNotificationTable().getValueAt(row, 4);

        if ("Chưa đọc".equalsIgnoreCase(trangThai)) {
            try {
                boolean updated = model.markAsRead(maTB);
                if (updated) {
                    view.getModel().setValueAt("Đã đọc", row, 4);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi cập nhật trạng thái thông báo: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        JOptionPane.showMessageDialog(view, noiDung, "Chi tiết thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void backToMain() {
        // Logic để quay về màn hình chính của bệnh nhân
        // Ví dụ: ẩn view hiện tại, show form khác hoặc gọi controller khác
        JOptionPane.showMessageDialog(view, "Chức năng quay lại màn hình chính chưa được cài đặt.");
    }
}

