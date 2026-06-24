/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import java.awt.*;

public class ContactView extends JPanel {
    private JLabel titleLabel;
    private JEditorPane contentPane;
    private JButton backButton;

    public ContactView(Runnable onBack) {
        initComponents();
        layoutComponents();
        styleComponents();
    }

    private void initComponents() {
        titleLabel = new JLabel("AN TOÀN BẢO MẬT", JLabel.CENTER);

        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);

        backButton = new JButton("← Quay lại");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(contentPane);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleComponents() {
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0x2B4A59));

        contentPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentPane.setMargin(new Insets(10, 200, 10, 150));

        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0xff9800));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(0x588EA7));
            }
        });
    }

    public void setContentHtml(String html) {
        contentPane.setText(html);
        contentPane.setCaretPosition(0);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
