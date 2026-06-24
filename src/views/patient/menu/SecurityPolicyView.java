/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import java.awt.*;

public class SecurityPolicyView extends JPanel {
    public JButton backButton;
    private JEditorPane contentPane;
    private JLabel titleLabel;
    private JPanel bottomPanel;

    public SecurityPolicyView(Runnable onBack) {
        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);

        titleLabel = new JLabel("AN TOÀN BẢO MẬT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0x2B4A59));
        add(titleLabel, BorderLayout.NORTH);

        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);
        contentPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        contentPane.setMargin(new Insets(10, 200, 10, 150));
        
        JScrollPane scrollPane = new JScrollPane(contentPane);
        add(new JScrollPane(contentPane), BorderLayout.CENTER);

        backButton = new JButton("← Quay lại");
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x588EA7));
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Chính sách bảo mật");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(800, 600);
//
//            SecurityPolicyView view = new SecurityPolicyView();
//            new SecurityPolicyController(view, () -> {
//                System.out.println("Quay lại được gọi!");
//                // frame.dispose(); nếu muốn đóng
//            });
//
//            frame.setContentPane(view);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}

