/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import java.awt.BorderLayout;
import models.menuPa.TermOfUseModel;
import views.patient.menu.TermOfUseView;

import javax.swing.*;
import java.awt.event.*;

public class TermOfUseController extends JPanel {
    private TermOfUseView view;
    private TermOfUseModel model;
    private Runnable onBackCallback;

    public TermOfUseController(Runnable onBackCallback) {
        this.view = new TermOfUseView(onBackCallback);
        this.model = new TermOfUseModel();
        this.onBackCallback = onBackCallback;

        setLayout(new BorderLayout());
        add(view, BorderLayout.CENTER);

        // Load nội dung từ model
        view.contentPane.setText(model.getTermsHtml());

        // Gắn sự kiện cho nút quay lại
        view.backButton.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });

        view.backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                view.backButton.setBackground(new java.awt.Color(0xff9800));
            }

            public void mouseExited(MouseEvent evt) {
                view.backButton.setBackground(new java.awt.Color(0x588EA7));
            }
        });
    }
}

