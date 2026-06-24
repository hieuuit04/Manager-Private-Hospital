/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import views.patient.PatientDashboard;
import views.patient.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.*;
import java.sql.SQLException;

public class PatientDashboardController {
    private final PatientDashboard view;
    private final String patientId;
    private JPanel mainPanel;

    public PatientDashboardController(PatientDashboard view, String patientId) {
        this.view = view;
        this.patientId = patientId;
    }

    public void handleSideMenuClick(String item) {
        switch (item) {
            case "Đăng xuất" -> view.dispose();
            case "Hồ sơ cá nhân", "Thông báo", "Điều khoản sử dụng", "An toàn bảo mật", "Liên hệ" -> {
                showPanel(item);
                boolean showSideMenu = !(item.equals("Hồ sơ cá nhân") || item.equals("Thông báo"));
                getSideMenu().setVisible(showSideMenu);
            }
        }
    }

    public JPanel createFunctionContainer(String title, String iconPath) {
        JPanel panel = createRoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img), JLabel.CENTER);

        JLabel textLabel = new JLabel(title, JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            final Color normalColor = Color.WHITE;
            final Color hoverColor = new Color(0xd9eef2);

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(normalColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    handleFunctionClick(title);
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        return panel;
    }
    private JPanel getSideMenu() {
        return getSideMenu(); 
    }
    
    private void showPanel(String name) {
        CardLayout cardLayout = (CardLayout) getMainPanel().getLayout();
        cardLayout.show(getMainPanel(), name);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    public void handleFunctionClick(String title) throws SQLException, ClassNotFoundException, Exception {
        switch (title) {
            case "Hồ sơ cá nhân" -> showPanel("Hồ sơ cá nhân");
            case "Thông báo" -> showPanel("Thông báo");
            case "Điều khoản sử dụng" -> showPanel("Điều khoản sử dụng");
            case "An toàn bảo mật" -> showPanel("An toàn bảo mật");
            case "Liên hệ" -> showPanel("Liên hệ");
            case "Lịch hẹn" -> new HistoryAppointment(patientId).setVisible(true);
            case "Tra cứu thuốc" -> new ProductView().setVisible(true);
            case "Lịch sử khám" -> new HisMedExam(patientId).setVisible(true);
            case "Mua thuốc" -> new BuyMed(patientId).setVisible(true);
            case "Dịch vụ khám" -> new ServiceForm(patientId).setVisible(true);
            case "Thanh toán hóa đơn" -> new PayBill(patientId).setVisible(true);
        }
    }
    
    public JPanel createRoundedPanel(int radius) {
    return new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(Color.LIGHT_GRAY); 
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            
            g2.dispose();
        }

        @Override
        public boolean isOpaque() {
            return false; 
        }
    };
}

}

