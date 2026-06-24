/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.admin;

import utils.ThemeManager;
import utils.ThemeManager.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class SystemConfigEditor extends JFrame {
    private Properties config;
    private File configFile = new File("config.properties");

    private JTextField txtHospitalName, txtEmail, txtVersion;
    private JComboBox<String> cbLanguage, cbTheme;
    private JCheckBox chkDebug;
    private JButton btnSave;

    private String adminId;

    public SystemConfigEditor(String adminId) {
        this.adminId = adminId;
        loadConfig();
        applyInitialTheme(); // ← Áp dụng theme khi khởi chạy
        initUI();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full màn hình
        setVisible(true);
    }

    private void loadConfig() {
        config = new Properties();

        if (!configFile.exists()) {
            config.setProperty("hospital.name", "Bệnh viện tư nhân ABC");
            config.setProperty("support.email", "support@abcclinic.com");
            config.setProperty("version", "1.0");
            config.setProperty("language", "vi");
            config.setProperty("theme", "light");
            config.setProperty("debug.mode", "false");
            saveConfig();
        } else {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Không thể tải cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            config.store(fos, "System Configuration");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Không thể lưu cấu hình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initUI() {
        setTitle("Cấu hình hệ thống - Admin: " + adminId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        formPanel.setOpaque(false);
        formPanel.setBackground(new Color(0xCDE8E5)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        txtHospitalName = new JTextField(config.getProperty("hospital.name", ""), 30);
        txtEmail = new JTextField(config.getProperty("support.email", ""), 30);
        txtVersion = new JTextField(config.getProperty("version", ""), 30);

        cbLanguage = new JComboBox<>(new String[]{"vi", "en"});
        cbLanguage.setSelectedItem(config.getProperty("language", "vi"));

        cbTheme = new JComboBox<>(new String[]{"light", "dark"});
        cbTheme.setSelectedItem(config.getProperty("theme", "light"));

        chkDebug = new JCheckBox("Bật chế độ debug");
        chkDebug.setSelected(Boolean.parseBoolean(config.getProperty("debug.mode", "false")));

        addRow(formPanel, gbc, "Tên bệnh viện:", txtHospitalName);
        addRow(formPanel, gbc, "Email hỗ trợ:", txtEmail);
        addRow(formPanel, gbc, "Phiên bản phần mềm:", txtVersion);
        addRow(formPanel, gbc, "Ngôn ngữ:", cbLanguage);
        addRow(formPanel, gbc, "Giao diện:", cbTheme);

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(chkDebug, gbc);
        gbc.gridy++;

        btnSave = new JButton("Lưu cấu hình");
        btnSave.setPreferredSize(new Dimension(150, 40));

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSave);

        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            config.setProperty("hospital.name", txtHospitalName.getText().trim());
            config.setProperty("support.email", txtEmail.getText().trim());
            config.setProperty("version", txtVersion.getText().trim());
            config.setProperty("language", cbLanguage.getSelectedItem().toString());
            config.setProperty("theme", cbTheme.getSelectedItem().toString());
            config.setProperty("debug.mode", String.valueOf(chkDebug.isSelected()));
            saveConfig();
            JOptionPane.showMessageDialog(this, "Đã lưu cấu hình thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        cbTheme.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String theme = cbTheme.getSelectedItem().toString();
                if ("dark".equals(theme)) {
                    ThemeManager.setTheme(Theme.DARK);
                } else {
                    ThemeManager.setTheme(Theme.LIGHT);
                }

                ThemeManager.applyTheme(this.getContentPane());
                this.repaint();
                JOptionPane.showMessageDialog(this, "Đã thay đổi giao diện. Các màn hình khác cần mở lại để cập nhật.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Áp dụng giao diện lúc tạo giao diện
        ThemeManager.applyTheme(this.getContentPane());
        
        getContentPane().setBackground(new Color(0xCDE8E5));
        btnPanel.setBackground(new Color(0xCDE8E5)); // Thêm dòng này nếu chưa có

    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }

    private void applyInitialTheme() {
        String theme = config.getProperty("theme", "light");
        if ("dark".equals(theme)) {
            ThemeManager.setTheme(Theme.DARK);
        } else {
            ThemeManager.setTheme(Theme.LIGHT);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SystemConfigEditor("U006"));
    }
}
