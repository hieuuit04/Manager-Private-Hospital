/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient.menu;

import models.menuPa.ContactModel;
import views.patient.menu.ContactView;

import javax.swing.*;

public class ContactController {
    private ContactView view;
    private ContactModel model;
    private Runnable onBackCallback;

    public ContactController(ContactView view, ContactModel model, Runnable onBackCallback) {
        this.view = view;
        this.model = model;
        this.onBackCallback = onBackCallback;

        initController();
        loadContent();
    }

    private void initController() {
        view.getBackButton().addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });
    }

    private void loadContent() {
        String html = model.getContactHtml();
        view.setContentHtml(html);
    }
}
