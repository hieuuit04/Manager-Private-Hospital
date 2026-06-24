/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import views.patient.menu.SecurityPolicyView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SecurityPolicyController {
    private SecurityPolicyView view;
    private Runnable onBackCallback;

    public SecurityPolicyController(SecurityPolicyView view, Runnable onBackCallback) {
        this.view = view;
        this.onBackCallback = onBackCallback;
        initController();
    }

    private void initController() {
        view.backButton.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });

        view.backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                view.backButton.setBackground(new java.awt.Color(0xff9800));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                view.backButton.setBackground(new java.awt.Color(0x588EA7));
            }
        });
    }
}

