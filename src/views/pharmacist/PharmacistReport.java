/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import org.jfree.chart.axis.NumberAxis;

public class PharmacistReport extends JFrame {
    private String pharmacistId;
    private ChartPanel revenueChartPanel;
    private ChartPanel prescriptionChartPanel;
    private JComboBox<String> cbxRevenueType;
    private JComboBox<String> cbxPrescriptionType;

    public PharmacistReport(String pharmacistId) {
        this.pharmacistId = pharmacistId;
        setTitle("Báo cáo doanh thu và đơn thuốc");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab Doanh thu
        JPanel revenuePanel = new JPanel(new BorderLayout());
        revenuePanel.setBackground(new Color(230, 245, 255));
        revenuePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel revenueTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        revenueTopPanel.setBackground(new Color(230, 245, 255));
        JLabel lblRevenueType = new JLabel("Thống kê theo:");
        lblRevenueType.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cbxRevenueType = new JComboBox<>(new String[]{"Tháng", "Năm"});
        cbxRevenueType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        revenueTopPanel.add(lblRevenueType);
        revenueTopPanel.add(cbxRevenueType);

        revenueChartPanel = createRevenueChartPanel("Tháng");
        revenuePanel.add(revenueTopPanel, BorderLayout.NORTH);
        revenuePanel.add(revenueChartPanel, BorderLayout.CENTER);

        // Tab Đơn thuốc
        JPanel prescriptionPanel = new JPanel(new BorderLayout());
        prescriptionPanel.setBackground(new Color(255, 245, 230));
        prescriptionPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel prescriptionTopPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prescriptionTopPanel.setBackground(new Color(255, 245, 230));
        JLabel lblPrescriptionType = new JLabel("Thống kê theo:");
        lblPrescriptionType.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cbxPrescriptionType = new JComboBox<>(new String[]{"Tháng", "Năm"});
        cbxPrescriptionType.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        prescriptionTopPanel.add(lblPrescriptionType);
        prescriptionTopPanel.add(cbxPrescriptionType);

        prescriptionChartPanel = createPrescriptionChartPanel("Tháng");
        prescriptionPanel.add(prescriptionTopPanel, BorderLayout.NORTH);
        prescriptionPanel.add(prescriptionChartPanel, BorderLayout.CENTER);

        // Add action listeners
        cbxRevenueType.addActionListener(e -> updateRevenueChart());
        cbxPrescriptionType.addActionListener(e -> updatePrescriptionChart());

        tabbedPane.addTab("Doanh thu", revenuePanel);
        tabbedPane.addTab("Đơn thuốc", prescriptionPanel);

        add(tabbedPane);
    }

    private ChartPanel createRevenueChartPanel(String type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = utils.DBConnection.getConnection()) {
            String sql;
            if ("Tháng".equals(type)) {
                sql = "SELECT TO_CHAR(NGAYBAN, 'YYYY-MM') as TIME, SUM(THANHTIEN) as DOANHTHU " +
                      "FROM DONTHUOC_DONTHUOCYC WHERE MADS=? GROUP BY TO_CHAR(NGAYBAN, 'YYYY-MM') ORDER BY TIME";
            } else {
                sql = "SELECT TO_CHAR(NGAYBAN, 'YYYY') as TIME, SUM(THANHTIEN) as DOANHTHU " +
                      "FROM DONTHUOC_DONTHUOCYC WHERE MADS=? GROUP BY TO_CHAR(NGAYBAN, 'YYYY') ORDER BY TIME";
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pharmacistId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String time = rs.getString("TIME");
                    double doanhThu = rs.getDouble("DOANHTHU");
                    dataset.addValue(doanhThu, "Doanh thu", time);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Doanh thu bán thuốc theo " + type.toLowerCase(), type, "Doanh thu (VNĐ)",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(220, 235, 245));
        plot.getRenderer().setSeriesPaint(0, new Color(70, 130, 180));

        // Format y-axis values as currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(currencyFormat);

        return new ChartPanel(chart);
    }

    private ChartPanel createPrescriptionChartPanel(String type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = utils.DBConnection.getConnection()) {
            String sql;
            if ("Tháng".equals(type)) {
                sql = "SELECT TO_CHAR(NGAYBAN, 'YYYY-MM') as TIME, COUNT(*) as SOLAN " +
                      "FROM DONTHUOC_DONTHUOCYC WHERE MADS=? GROUP BY TO_CHAR(NGAYBAN, 'YYYY-MM') ORDER BY TIME";
            } else {
                sql = "SELECT TO_CHAR(NGAYBAN, 'YYYY') as TIME, COUNT(*) as SOLAN " +
                      "FROM DONTHUOC_DONTHUOCYC WHERE MADS=? GROUP BY TO_CHAR(NGAYBAN, 'YYYY') ORDER BY TIME";
            }
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, pharmacistId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String time = rs.getString("TIME");
                    int soLan = rs.getInt("SOLAN");
                    dataset.addValue(soLan, "Số đơn", time);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Số đơn thuốc đã bán theo " + type.toLowerCase(), type, "Số đơn",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(255, 235, 220));
        plot.getRenderer().setSeriesPaint(0, new Color(255, 140, 0));
        return new ChartPanel(chart);
    }

    private void updateRevenueChart() {
        String type = (String) cbxRevenueType.getSelectedItem();
        ChartPanel newPanel = createRevenueChartPanel(type);
        JPanel parent = (JPanel) revenueChartPanel.getParent();
        parent.remove(revenueChartPanel);
        revenueChartPanel = newPanel;
        parent.add(revenueChartPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }

    private void updatePrescriptionChart() {
        String type = (String) cbxPrescriptionType.getSelectedItem();
        ChartPanel newPanel = createPrescriptionChartPanel(type);
        JPanel parent = (JPanel) prescriptionChartPanel.getParent();
        parent.remove(prescriptionChartPanel);
        prescriptionChartPanel = newPanel;
        parent.add(prescriptionChartPanel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new PharmacistReport("U004").setVisible(true));
//    }
}