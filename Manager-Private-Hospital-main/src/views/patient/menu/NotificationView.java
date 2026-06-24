/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient.menu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NotificationView extends JPanel {
    private JTextField tfMaTB, tfNoiDung, tfNgayTB, tfTrangThai, tfLoai;
    private JButton btnSearch, btnBack;
    private JTable notificationTable;
    private DefaultTableModel model;
    private String patientId;

    public NotificationView(String patientId, Runnable onBack) {
        this.patientId = patientId;
        initComponents();
    }

    private JPanel createLabeledField(String labelText, JTextField textField, Font font, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(0xCDE8E5));

        JLabel label = new JLabel(labelText);
        label.setFont(font);
        label.setForeground(color);

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0, 0, 0, 0));

        // Top Panel with Title and Search Fields
        JPanel topPanel = new JPanel(new BorderLayout());

        // Title
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("DANH SÁCH THÔNG BÁO", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(new Color(0x78a2a7));
        title.setBackground(new Color(0xe8faf8));
        title.setOpaque(true);
        titlePanel.add(title, BorderLayout.CENTER);
        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(0xCDE8E5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topButtonsPanel = new JPanel(new BorderLayout());
        topButtonsPanel.setBackground(new Color(0xCDE8E5));
        topButtonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnBack = new JButton("← Quay lại");
        btnBack.setPreferredSize(new Dimension(120, 30));
        btnBack.setFocusPainted(false);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(0x588EA7));
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        topButtonsPanel.add(btnBack, BorderLayout.WEST);

        btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setBackground(new Color(0x78a2a7));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(300, 30));

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchButtonPanel.setOpaque(false);
        searchButtonPanel.add(btnSearch);
        topButtonsPanel.add(searchButtonPanel, BorderLayout.CENTER);

        searchPanel.add(topButtonsPanel, BorderLayout.NORTH);

        // Search Fields
        JPanel searchFieldsPanel = new JPanel(new GridLayout(1, 5, 15, 15));
        searchFieldsPanel.setBackground(new Color(0xCDE8E5));

        tfMaTB = new JTextField();
        tfNoiDung = new JTextField();
        tfNgayTB = new JTextField();
        tfTrangThai = new JTextField();
        tfLoai = new JTextField();

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = Color.BLACK;

        searchFieldsPanel.add(createLabeledField("Mã thông báo:", tfMaTB, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Nội dung:", tfNoiDung, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Ngày thông báo:", tfNgayTB, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Trạng thái:", tfTrangThai, labelFont, labelColor));
        searchFieldsPanel.add(createLabeledField("Loại thông báo:", tfLoai, labelFont, labelColor));

        searchPanel.add(searchFieldsPanel, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Mã thông báo", "Mã bệnh nhân", "Nội dung", "Ngày thông báo", "Trạng thái thông báo", "Loại thông báo"};
        model = new DefaultTableModel(columns, 0);
        notificationTable = new JTable(model);
        notificationTable.setRowHeight(28);
        notificationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        notificationTable.getTableHeader().setBackground(new Color(0xCDE8E5));
        notificationTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(notificationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(new Color(0xD9EEF2));
        scrollPane.setBorder(new LineBorder(new Color(222, 246, 186)));

        add(scrollPane, BorderLayout.CENTER);
    }

    // Getters for controller

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnBack() {
        return btnBack;
    }

    public String getTfMaTB() {
        return tfMaTB.getText().trim();
    }

    public String getTfNoiDung() {
        return tfNoiDung.getText().trim();
    }

    public String getTfNgayTB() {
        return tfNgayTB.getText().trim();
    }

    public String getTfTrangThai() {
        return tfTrangThai.getText().trim();
    }

    public String getTfLoai() {
        return tfLoai.getText().trim();
    }

    public JTable getNotificationTable() {
        return notificationTable;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void clearTable() {
        model.setRowCount(0);
    }

    public void addNotificationRow(Object[] rowData) {
        model.addRow(rowData);
    }
}

