/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.general.DefaultPieDataset;

import utils.DBConnection;

public class BaoCao extends JFrame {
    private JPanel contentPanel;
    private CardLayout contentLayout;
    private JLabel khamBenhLabel, lichHenLabel, donThuocLabel, benhNhanLabel, hoaDonLabel;
    private String doctorId;

    public BaoCao(String doctorId) throws SQLException, ClassNotFoundException {
        this.doctorId = doctorId;
        
        setTitle("📄 Danh sách hóa đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        setLayout(new BorderLayout());
        
        // Menu đầu trang
        JPanel topMenu = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        topMenu.setBackground(new Color(0xe8faf8));

        khamBenhLabel = createMenuLabel("Khám bệnh");
        lichHenLabel = createMenuLabel("Lịch hẹn");
        donThuocLabel = createMenuLabel("Đơn thuốc");
        benhNhanLabel = createMenuLabel("Bệnh nhân");
        hoaDonLabel = createMenuLabel("Hóa đơn");

        topMenu.add(khamBenhLabel);
        topMenu.add(lichHenLabel);
        topMenu.add(donThuocLabel);
        topMenu.add(benhNhanLabel);
        topMenu.add(hoaDonLabel);

        add(topMenu, BorderLayout.NORTH);

        // Panel nội dung
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);

        contentPanel.add(createKhamBenhPanel(), "khambenh");
        contentPanel.add(createLichHenPanel(), "lichhen");
        contentPanel.add(createDonThuocPanel(), "donthuoc");
        contentPanel.add(createBenhNhanPanel(), "benhnhan");
        contentPanel.add(createHoaDonPanel(), "hoadon");

        add(contentPanel, BorderLayout.CENTER);

        switchTab("khambenh");
        addMenuListeners();
    }
    
     private JLabel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setForeground(new Color(0x2B4A59));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private void switchTab(String tabName) {
        resetMenuStyle();
        contentLayout.show(contentPanel, tabName);
        if (tabName.equals("khambenh")) highlightLabel(khamBenhLabel);
        else if (tabName.equals("lichhen")) highlightLabel(lichHenLabel);
        else if (tabName.equals("donthuoc")) highlightLabel(donThuocLabel);
        else if (tabName.equals("benhnhan")) highlightLabel(benhNhanLabel);
        else if (tabName.equals("hoadon")) highlightLabel(hoaDonLabel);
    }

    private void highlightLabel(JLabel label) {
        label.setForeground(new Color(0xff9800));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0xff9800)));
    }

    private void resetMenuStyle() {
        JLabel[] labels = {khamBenhLabel, lichHenLabel, donThuocLabel, benhNhanLabel, hoaDonLabel};
        for (JLabel label : labels) {
            label.setForeground(new Color(0x2B4A59));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
            label.setBorder(null);
        }
    }

    private void addMenuListeners() {
        khamBenhLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("khambenh");
            }
        });
        lichHenLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("lichhen");
            }
        });
        donThuocLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("donthuoc");
            }
        });
        benhNhanLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("benhnhan");
            }
        });
        hoaDonLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                switchTab("hoadon");
            }
        });
    }
    
    private JPanel createKhamBenhPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            int currentYear = LocalDate.now().getYear();

            String sql = "SELECT EXTRACT(MONTH FROM NGAYKHAM) AS THANG, COUNT(*) AS SOLAN_KHAM " +
                         "FROM KHAM WHERE EXTRACT(YEAR FROM NGAYKHAM) = ? " +
                         "GROUP BY EXTRACT(MONTH FROM NGAYKHAM) ORDER BY THANG";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentYear);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("THANG");
                int soLanKham = rs.getInt("SOLAN_KHAM");
                dataset.addValue(soLanKham, "Số lần khám", "Tháng " + thang);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lần khám bệnh theo tháng trong năm nay",
                "Tháng", "Số lần khám",
                dataset
        );

        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                return new Color(228, 139, 139); // Màu hồng
            }
        };
        renderer.setSeriesPaint(0, new Color(228, 139, 139));
        plot.setRenderer(renderer);

        barChart.setBackgroundPaint(new Color(0xCDE8E5)); // màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); // màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); // màu lưới

        return new ChartPanel(barChart);
    }

    
    private JPanel createLichHenPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            int currentYear = LocalDate.now().getYear();
            
            String sql = "SELECT EXTRACT(MONTH FROM NGAYDAT) AS THANG, COUNT(*) AS SOLAN_DATLICH " +
                         "FROM LICHHEN WHERE EXTRACT(YEAR FROM NGAYDAT) = ? " +
                         "GROUP BY EXTRACT(MONTH FROM NGAYDAT) ORDER BY THANG";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentYear);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("THANG");
                int soLanDatLich = rs.getInt("SOLAN_DATLICH");
                dataset.addValue(soLanDatLich, "Số lần đặt lịch", "Tháng " + thang);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lần đặt lịch khám bệnh theo tháng trong năm nay",
                "Tháng", "Số lần đặt lịch",
                dataset
        );
        
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(80, 139, 171)
                };
                return colors[column % colors.length];
            }
        };
        renderer.setSeriesPaint(0, new Color(80, 139, 171));
        plot.setRenderer(renderer);
        
        barChart.setBackgroundPaint(new Color(0xCDE8E5));// màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); //màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); //màu đường kẻ đứt nét ngang

        return new ChartPanel(barChart);
    }
    
    private JPanel createDonThuocPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection()) {
            int currentYear = LocalDate.now().getYear();
            
            String sql = "SELECT EXTRACT(MONTH FROM NGAYBAN) AS THANG, COUNT(*) AS SOLAN_BANTHUOC " +
                         "FROM DONTHUOC_DONTHUOCYC WHERE EXTRACT(YEAR FROM NGAYBAN) = ? " +
                         "GROUP BY EXTRACT(MONTH FROM NGAYBAN) ORDER BY THANG";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentYear);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int thang = rs.getInt("THANG");
                int soLanBanThuoc = rs.getInt("SOLAN_BANTHUOC");
                dataset.addValue(soLanBanThuoc, "Số lần bán thuốc", "Tháng " + thang);
            }
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu: " + e.getMessage());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê số lần đặt lịch khám bệnh theo tháng trong năm nay",
                "Tháng", "Số lần bán thuốc",
                dataset
        );
        
        CategoryPlot plot = barChart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer() {
            @Override
            public Paint getItemPaint(int row, int column) {
                Color[] colors = {
                    new Color(74, 141, 150)
                };
                return colors[column % colors.length];
            }
        };
        renderer.setSeriesPaint(0, new Color(74, 141, 150));
        plot.setRenderer(renderer);
        
        barChart.setBackgroundPaint(new Color(0xCDE8E5));// màu nền ngoài biểu đồ
        plot.setBackgroundPaint(Color.WHITE); //màu nền trong biểu đồ
        plot.setRangeGridlinePaint(Color.GRAY); //màu đường kẻ đứt nét ngang
        
        return new ChartPanel(barChart);
    }

    private JPanel createBenhNhanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xCDE8E5));

        JLabel totalLabel = new JLabel("Tổng số bệnh nhân: " + getTongSoBenhNhan(), SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 22));
        totalLabel.setForeground(new Color(0x2B4A59));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.add(totalLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Giới tính", createPieChart("Giới tính",
                "SELECT GIOITINH, COUNT(*) AS SL FROM USERS WHERE ROLE = 'Bệnh nhân' GROUP BY GIOITINH"));

        tabbedPane.addTab("Bảo hiểm y tế", createPieChart("Bảo hiểm y tế",
                "SELECT CASE " +
                "WHEN SOBHYT = 'Không' THEN 'Không có BHYT' " +
                "ELSE 'Có BHYT' END AS BAOHIEM, COUNT(*) AS SL FROM BENHNHAN " +
                "GROUP BY CASE WHEN SOBHYT = 'Không' THEN 'Không có BHYT' ELSE 'Có BHYT' END"));

        tabbedPane.addTab("Nhóm máu", createLineChart("Nhóm máu",
                "SELECT NHOMMAU, COUNT(*) AS SL FROM BENHNHAN GROUP BY NHOMMAU"));

        tabbedPane.addTab("Độ tuổi", createLineChart("Độ tuổi",
            "SELECT DO_TUOI, COUNT(*) AS SL FROM (" +
            "  SELECT CASE " +
            "    WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) < 18 THEN 'Dưới 18' " +
            "    WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 18 AND 35 THEN '18-35' " +
            "    WHEN FLOOR(MONTHS_BETWEEN(SYSDATE, NGAYSINH)/12) BETWEEN 36 AND 60 THEN '36-60' " +
            "    ELSE 'Trên 60' " +
            "  END AS DO_TUOI " +
            "  FROM USERS WHERE ROLE = 'Bệnh nhân'" +
            ") GROUP BY DO_TUOI " +
            "ORDER BY CASE DO_TUOI " +
            "  WHEN 'Dưới 18' THEN 1 " +
            "  WHEN '18-35' THEN 2 " +
            "  WHEN '36-60' THEN 3 " +
            "  ELSE 4 END"
        ));


        tabbedPane.addTab("Dị ứng phổ biến", createBarChart("Dị ứng phổ biến",
                "SELECT DIUNG, COUNT(*) AS SL FROM BENHNHAN GROUP BY DIUNG"));

        tabbedPane.addTab("Lịch sử bệnh lý", createBarChart("Lịch sử bệnh lý",
                "SELECT LICHSU_BENHLY AS BENH, COUNT(*) AS SL FROM BENHNHAN GROUP BY LICHSU_BENHLY"));

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }


    private int getTongSoBenhNhan() {
        int total = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM BENHNHAN";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private ChartPanel createPieChart(String title, String sql) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.setValue(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(title, dataset, true, true, false);
        chart.setBackgroundPaint(new Color(0xCDE8E5));

        // Đặt nền cho phần plot (biểu đồ bên trong)
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        //plot.setOutlineVisible(false); // Ẩn viền ngoài nếu muốn
        
         // Gán màu cụ thể cho giới tính
        for (Object key : dataset.getKeys()) {
            String label = key.toString().toLowerCase();
            if (label.contains("nam")) {
                plot.setSectionPaint((Comparable) key, new Color(196, 220, 246)); 
            } else if (label.contains("nữ")) {
                plot.setSectionPaint((Comparable) key, new Color(246, 196, 235));
            } else if (label.contains("không có bhyt")) {
                plot.setSectionPaint((Comparable) key, new Color(237, 221, 250)); 
            } else if (label.contains("có bhyt")) {
                plot.setSectionPaint((Comparable) key, new Color(226, 198, 247)); 
            }
            
        }
        
        return new ChartPanel(chart);
    }

    private ChartPanel createLineChart(String title, String sql) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt(2), title, rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createLineChart(
                title, "", "Số lượng", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        chart.setBackgroundPaint(new Color(0xCDE8E5));

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        return new ChartPanel(chart);
    }

    private ChartPanel createBarChart(String title, String sql) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt(2), title, rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title, "", "Số lượng",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = new BarRenderer();
        renderer.setSeriesPaint(0, new Color(102, 204, 204));
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        chart.setBackgroundPaint(new Color(0xCDE8E5));
        return new ChartPanel(chart);
    }
    
    private JPanel createHoaDonPanel() throws SQLException, ClassNotFoundException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xCDE8E5));

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Hóa đơn khám bệnh", createGroupedBarChart("Thống kê trạng thái hóa đơn khám bệnh theo tháng trong năm nay",
            "SELECT EXTRACT(MONTH FROM NGAYLAP) AS THANG, " +
            "TRANGTHAITT, COUNT(*) AS SL " +
            "FROM HOADON_KHAMBENH " +
            "WHERE EXTRACT(YEAR FROM NGAYLAP) = EXTRACT(YEAR FROM SYSDATE) " +
            "GROUP BY EXTRACT(MONTH FROM NGAYLAP), TRANGTHAITT " +
            "ORDER BY THANG"));

        tabbedPane.addTab("Hóa đơn điều trị", createGroupedBarChart("Thống kê trạng thái hóa đơn điều trị theo tháng trong năm nay",
            "SELECT EXTRACT(MONTH FROM NGAYTIEPNHAN) AS THANG, " +
            "TRANGTHAITT, COUNT(*) AS SL " +
            "FROM DIEUTRI " +
            "WHERE EXTRACT(YEAR FROM NGAYTIEPNHAN) = EXTRACT(YEAR FROM SYSDATE) " +
            "GROUP BY EXTRACT(MONTH FROM NGAYTIEPNHAN), TRANGTHAITT " +
            "ORDER BY THANG"));

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createGroupedBarChart(String title, String query) throws SQLException, ClassNotFoundException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = "Tháng " + rs.getInt("THANG");
                String status = rs.getString("TRANGTHAITT");
                int count = rs.getInt("SL");
                dataset.addValue(count, status, month); // status = "Đã thanh toán" hoặc "Chưa thanh toán"
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Tháng",
                "Số hóa đơn",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setSeriesPaint(0, new Color(56, 122, 130)); // Đã thanh toán
        renderer.setSeriesPaint(1, new Color(194, 224, 135)); // Chưa thanh toán
        

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.GRAY);

        chart.setBackgroundPaint(new Color(0xCDE8E5));
        
        return new ChartPanel(chart);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BaoCao("U001").setVisible(true);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(BaoCao.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}

