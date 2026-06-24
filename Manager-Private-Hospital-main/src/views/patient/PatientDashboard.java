/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import views.menuPatient.Contact;
import views.menuPatient.Notification;
import views.menuPatient.PersonalInfo;
import views.menuPatient.SecurityPolicy;
import views.menuPatient.TermOfUse;


public class PatientDashboard extends JFrame {
    private String currentPatient, patientId;
    private JPanel contentPanel;  // panel chứa các màn hình chức năng
    private CardLayout cardLayout;
    private JPanel sideMenu; 

    public PatientDashboard(String hoTen, String maBN) throws SQLException, ClassNotFoundException {
        this.currentPatient = hoTen;
        this.patientId = maBN;

        System.out.println("Tên: " + hoTen);
        System.out.println("Mã bệnh nhân: " + patientId);

        setTitle("Dashboard Bệnh nhân");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLocationRelativeTo(null); // canh giữa
        setLayout(new BorderLayout());

        // Thanh menu trái (ví dụ đơn giản)
        sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension(300, getHeight()));
        sideMenu.setBackground(new Color(0x2B4A59));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));


        sideMenu.add(Box.createVerticalStrut(20)); // thêm trước khi add menuLabel

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setFont(new Font("Arial", Font.BOLD, 20));
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        menuLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sideMenu.add(menuLabel);

        sideMenu.add(Box.createVerticalStrut(20)); // khoảng cách sau MENU

        // Các mục menu bên trái (để demo, không dùng để chuyển giao diện)
        String[] menuItems = {
            "Hồ sơ cá nhân",
            "Thông báo",
            "Điều khoản sử dụng",
            "An toàn bảo mật",
            "Liên hệ",
            "Đăng xuất"
        };

        for (String item : menuItems) {
            JLabel lbl = new JLabel(item);
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.PLAIN, 16));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Hover effect
            lbl.addMouseListener(new MouseAdapter() {
            Color normal = lbl.getForeground();

            @Override
            public void mouseEntered(MouseEvent e) {
                lbl.setForeground(new Color(0x1ABC9C));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lbl.setForeground(normal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked menu trái: " + item);
                if (item.equals("Đăng xuất")) {
                    dispose();
                } else {
                    cardLayout.show(contentPanel, item);

                    // Ẩn side menu nếu là "Hồ sơ cá nhân", hiện nếu là cái khác
                    sideMenu.setVisible(!item.equals("Hồ sơ cá nhân") && !item.equals("Thông báo"));

                }
            }
        });

            sideMenu.add(lbl);
            sideMenu.add(Box.createVerticalStrut(10));
        }

        add(sideMenu, BorderLayout.WEST);

        // Panel chính: sử dụng CardLayout để chuyển đổi giao diện chức năng
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        

        // Tạo các panel chức năng tương ứng
        contentPanel.add(createProfilePanel(), "Hồ sơ cá nhân");
        contentPanel.add(createNotificationPanel(), "Thông báo");
        contentPanel.add(createTermsPanel(), "Điều khoản sử dụng");
        contentPanel.add(createSecurityPanel(), "An toàn bảo mật");
        contentPanel.add(createContactPanel(), "Liên hệ");

        // Tạo panel các chức năng chính dạng container giống như trước
        contentPanel.add(createMainFunctionPanel(), "MainFunctions");

        // Mặc định hiển thị màn hình chính (chức năng)
        cardLayout.show(contentPanel, "MainFunctions");

        add(contentPanel, BorderLayout.CENTER);
    }


    // Các màn hình demo chức năng khác
    private JPanel createProfilePanel() {
        PersonalInfo profilePanel = new PersonalInfo(patientId, () -> {
            cardLayout.show(contentPanel, "MainFunctions");
            sideMenu.setVisible(true); // <-- hiện lại menu khi bấm nút quay lại
        });
        return profilePanel;
    }

    private JPanel createNotificationPanel() throws SQLException, ClassNotFoundException {
        Notification noti = new Notification(patientId, () -> {
            cardLayout.show(contentPanel, "MainFunctions");
            sideMenu.setVisible(true); // <-- hiện lại menu khi bấm nút quay lại
        });
        return noti;
    }

    private JPanel createTermsPanel() {
        TermOfUse termsPanel = new TermOfUse(() -> {
            cardLayout.show(contentPanel, "MainFunctions");
        });
        return termsPanel;
    }

    private JPanel createSecurityPanel() {
        SecurityPolicy SecurityPanel = new SecurityPolicy(() -> {
            cardLayout.show(contentPanel, "MainFunctions");
        });
        return SecurityPanel;
    }

    private JPanel createContactPanel() {
        Contact ContactPanel = new Contact(() -> {
            cardLayout.show(contentPanel, "MainFunctions");
        });
        return ContactPanel;
    }

    // Panel chính chứa các container chức năng như trước
    private JPanel createMainFunctionPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        JPanel wrapper = new JPanel(new GridLayout(2, 3, 30, 30));
        wrapper.setPreferredSize(new Dimension(700, 400));
        wrapper.setBackground(Color.WHITE);

        // Các container chức năng
        wrapper.add(createFunctionContainer("Tra cứu thuốc", "src/views/patient/image/find.png"));
        wrapper.add(createFunctionContainer("Lịch hẹn", "src/views/patient/image/calendar.png"));
        wrapper.add(createFunctionContainer("Lịch sử khám", "src/views/patient/image/history.png"));
        wrapper.add(createFunctionContainer("Mua thuốc", "src/views/patient/image/drug.png"));
        wrapper.add(createFunctionContainer("Dịch vụ khám", "src/views/patient/image/service.png"));
        wrapper.add(createFunctionContainer("Thanh toán hóa đơn", "src/views/patient/image/bill.jpg"));

        centerPanel.add(wrapper);

        // Thêm label chào mừng trên đầu centerPanel
        JLabel welcomeLabel = new JLabel("Chào mừng bệnh nhân: " + currentPatient, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(Color.WHITE);
        centerWrapper.add(welcomeLabel, BorderLayout.NORTH);
        centerWrapper.add(centerPanel, BorderLayout.CENTER);

        return centerWrapper;
    }

    private JPanel createFunctionContainer(String title, String iconPath) {
        RoundedPanel panel = new RoundedPanel(20);
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Icon
        ImageIcon icon = new ImageIcon(iconPath);
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img), JLabel.CENTER);

        JLabel textLabel = new JLabel(title, JLabel.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));

        panel.add(iconLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);

        // Hover hiệu ứng đổi màu nền
        Color normalColor = Color.WHITE;
        Color hoverColor = new Color(0xd9eef2);
        panel.setBackground(normalColor);

        panel.addMouseListener(new MouseAdapter() {
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
                    handleMenuClick(title);
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(PatientDashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        return panel;
    }

    private void handleMenuClick(String title) throws SQLException, ClassNotFoundException {
        System.out.println("Bạn đã chọn: " + title);

        // Chuyển sang panel tương ứng trong contentPanel
        switch (title) {
            case "Hồ sơ cá nhân" -> cardLayout.show(contentPanel, "Hồ sơ cá nhân");
            case "Thông báo" -> cardLayout.show(contentPanel, "Thông báo");
            case "Điều khoản sử dụng" -> cardLayout.show(contentPanel, "Điều khoản sử dụng");
            case "An toàn bảo mật" -> cardLayout.show(contentPanel, "An toàn bảo mật");
            case "Liên hệ" -> cardLayout.show(contentPanel, "Liên hệ");
            case "Lịch hẹn" -> {
                System.out.println(patientId);
                new HistoryAppointment(patientId).setVisible(true);
            }
            case "Tra cứu thuốc" -> new ProductView().setVisible(true);
            case "Lịch sử khám" -> {
                System.out.println(patientId);
                new HisMedExam(patientId).setVisible(true);
            }
            case "Mua thuốc" -> {
                System.out.println(patientId);
                new BuyMed(patientId).setVisible(true);
            }
            case "Dịch vụ khám" -> {
                System.out.println(patientId);
                new ServiceForm(patientId).setVisible(true);
            }
            case "Thanh toán hóa đơn" -> {
                System.out.println(patientId);
                new PayBill(patientId).setVisible(true);
            }
            default -> cardLayout.show(contentPanel, "MainFunctions");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Dữ liệu mẫu cho tên và mã bệnh nhân
                String hoTen = "Nguyễn Ngọc Duy Bảo";
                String maBN = "U002";

                PatientDashboard dashboard = new PatientDashboard(hoTen, maBN);
                dashboard.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

// Custom JPanel bo góc
class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền trắng bo góc
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);

        // Viền xám nhẹ
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}