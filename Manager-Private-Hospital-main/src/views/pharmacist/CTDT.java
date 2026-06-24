/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.pharmacist;

import utils.DBConnection;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.net.URL;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CTDT extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private String madt;
    private JButton btnAdd, btnUpdate, btnExport, btnDelete;
    private Runnable onCloseCallback;

    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(43, 74, 160);
    private static final Color SECONDARY_COLOR = new Color(0, 123, 255);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color HEADER_BG = new Color(33, 54, 69);
    private static final Color TABLE_HEADER_BG = new Color(43, 74, 160);
    private static final Color TABLE_HEADER_FG = Color.WHITE;
    private static final Color TABLE_GRID_COLOR = new Color(200, 200, 200);
    private static final Color TABLE_SELECTION_BG = new Color(222, 246, 186);
    private static final Color TABLE_SELECTION_FG = new Color(33, 54, 69);

    public CTDT(String madt, Runnable onCloseCallback) {
        this.madt = madt;
        this.onCloseCallback = onCloseCallback;
        setTitle("Chi tiết đơn thuốc: " + madt);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadData();
        checkTrangThai();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Header Panel with gradient
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, HEADER_BG, w, h, PRIMARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("CHI TIẾT ĐƠN THUỐC: " + madt, SwingConstants.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(title, BorderLayout.CENTER);

        // Table setup
        String[] columns = {"Đơn Thuốc", "Mã Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new java.awt.Font("Segoe UI",java.awt.Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Custom table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 28));
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TABLE_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        btnAdd = createButton("Thêm Chi tiết", PRIMARY_COLOR, "plus.png");
        btnUpdate = createButton("Cập nhật", SECONDARY_COLOR, "edit.png");
        btnExport = createButton("Xuất hóa đơn thuốc", SUCCESS_COLOR, "export.png");
        btnDelete = createButton("Xóa", DANGER_COLOR, "delete.png");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnExport);
        buttonPanel.add(btnDelete);

        // Add action listeners
        btnAdd.addActionListener(e -> openAddDialog());
        btnUpdate.addActionListener(e -> openUpdateDialog());
        btnExport.addActionListener(e -> exportInvoicePDF());
        btnDelete.addActionListener(e -> deleteSelectedRow());

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color bgColor, String iconName) {
        JButton button = new JButton(text);
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD,14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add icon if available
        try {
            URL imageUrl = getClass().getResource("/views/pharmacist/image/" + iconName);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                java.awt.Image img = icon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
                button.setIconTextGap(10);
            }
        } catch (Exception e) {
            // Không tìm thấy icon hoặc lỗi khác, bỏ qua
            e.printStackTrace(); // In lỗi nếu cần gỡ lỗi
        }

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void checkTrangThai() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT TRANGTHAITT FROM DONTHUOC_DONTHUOCYC WHERE MADT = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, madt);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String trangThai = rs.getString("TRANGTHAITT");
                    boolean daThanhToan = "Đã thanh toán".equalsIgnoreCase(trangThai);
                    btnAdd.setEnabled(!daThanhToan);
                    btnUpdate.setEnabled(!daThanhToan);
                    btnDelete.setEnabled(!daThanhToan);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra trạng thái: " + ex.getMessage());
        }
    }

    private void loadData() {
        tableModel.setRowCount(0);
        double tongThanhTien = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MADT, MASP, SOLUONG, DONGIA, THANHTIEN FROM CTDT WHERE MADT = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, madt);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("MADT"),
                        rs.getString("MASP"),
                        rs.getInt("SOLUONG"),
                        rs.getDouble("DONGIA"),
                        rs.getDouble("THANHTIEN")
                    };
                    tongThanhTien += rs.getDouble("THANHTIEN");
                    tableModel.addRow(row);
                }
            }
            // Cập nhật THANHTIEN vào DONTHUOC_DONTHUOCYC
            String update = "UPDATE DONTHUOC_DONTHUOCYC SET THANHTIEN=? WHERE MADT=?";
            try (PreparedStatement psUpdate = conn.prepareStatement(update)) {
                psUpdate.setDouble(1, tongThanhTien);
                psUpdate.setString(2, madt);
                psUpdate.executeUpdate();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết đơn thuốc: " + ex.getMessage());
        }
    }

    private void openAddDialog() {
        JComboBox<String> cbMASP = new JComboBox<>();
        JTextField tfSoLuong = new JTextField();
        JTextField tfDonGia = new JTextField();
        tfDonGia.setEditable(false);

        // Lấy dữ liệu MASP + TENSP từ DB
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MASP, TENSP, DONGIA FROM SANPHAM";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String masp = rs.getString("MASP");
                    String tensp = rs.getString("TENSP");
                    double dongia = rs.getDouble("DONGIA");
                    cbMASP.addItem(masp + " - " + tensp + " - " + dongia);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải sản phẩm: " + ex.getMessage());
            return;
        }

        // Khi chọn sản phẩm, tự động cập nhật đơn giá
        cbMASP.addActionListener(e -> {
            String selected = (String) cbMASP.getSelectedItem();
            if (selected != null && selected.contains(" - ")) {
                String[] parts = selected.split(" - ");
                if (parts.length == 3) {
                    tfDonGia.setText(parts[2]);
                }
            }
        });
        if (cbMASP.getItemCount() > 0) cbMASP.setSelectedIndex(0);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Sản phẩm:"));
        panel.add(cbMASP);
        panel.add(new JLabel("Số lượng:"));
        panel.add(tfSoLuong);
        panel.add(new JLabel("Đơn giá:"));
        panel.add(tfDonGia);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm chi tiết đơn thuốc", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String selected = (String) cbMASP.getSelectedItem();
                if (selected == null || !selected.contains(" - ")) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm hợp lệ!");
                    return;
                }
                String[] parts = selected.split(" - ");
                String masp = parts[0];
                double donGia = Double.parseDouble(parts[2]);
                int soLuong = Integer.parseInt(tfSoLuong.getText().trim());

                String sql = "INSERT INTO CTDT (MADT, MASP, SOLUONG, DONGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, madt);
                    ps.setString(2, masp);
                    ps.setInt(3, soLuong);
                    ps.setDouble(4, donGia);
                    ps.setDouble(5, soLuong * donGia);
                    ps.executeUpdate();
                }

                // Cập nhật tồn kho
                String updateTonKho = "UPDATE CHITIETKHO SET SLTONKHO = SLTONKHO - ? WHERE MAVATTU = ?";
                try (PreparedStatement psTonKho = conn.prepareStatement(updateTonKho)) {
                    psTonKho.setInt(1, soLuong);
                    psTonKho.setString(2, masp);
                    psTonKho.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Thêm chi tiết thành công!");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm chi tiết: " + ex.getMessage());
            }
        }
    }

    private void openUpdateDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để cập nhật!");
            return;
        }

        String masp = String.valueOf(tableModel.getValueAt(row, 1));
        double donGia = Double.parseDouble(tableModel.getValueAt(row, 3).toString());

        JTextField tfSoLuong = new JTextField(String.valueOf(tableModel.getValueAt(row, 2)));

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.add(new JLabel("Số lượng:"));
        panel.add(tfSoLuong);

        int result = JOptionPane.showConfirmDialog(this, panel, "Cập nhật số lượng", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                int newSoLuong = Integer.parseInt(tfSoLuong.getText().trim());
                String sql = "UPDATE CTDT SET SOLUONG=?, THANHTIEN=? WHERE MADT=? AND MASP=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, newSoLuong);
                    ps.setDouble(2, newSoLuong * donGia);
                    ps.setString(3, madt);
                    ps.setString(4, masp);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage());
            }
        }
    }
private void exportInvoicePDF() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Choose where to save the invoice PDF");
    fileChooser.setSelectedFile(new File("Invoice_" + madt + ".pdf"));
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) return;

    File pdfFile = fileChooser.getSelectedFile();

    String pharmacyName = "HEALINK PRIVATE HOSPITAL";
    String address = "123 Main Street, City, Country";
    String licenseNumber = "GP-123456";
    String phone = "(+84) 123-456-789";
    String email = "pharmacy@email.com";

    try {
        ArrayList<Object[]> data = new ArrayList<>();
        double totalAmount = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            totalAmount += Double.parseDouble(row[4].toString());
            data.add(row);
        }

        InputStream qrStream = getClass().getResourceAsStream("/views/pharmacist/image/QR.jpg");
        Image qrImage = null;
        if (qrStream != null) {
            byte[] qrBytes = qrStream.readAllBytes();
            qrImage = Image.getInstance(qrBytes);
            qrImage.scaleAbsolute(120, 120);
        }

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        // Use standard Helvetica font (no Unicode)
        Font mainTitleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(0, 102, 204));
        Font codeFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        Font totalFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.RED);

        // Title
        PdfPCell cell;
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("MEDICINE INVOICE", mainTitleFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10);
        titleTable.addCell(cell);
        document.add(titleTable);

        // Prescription code
        PdfPTable codeTable = new PdfPTable(1);
        codeTable.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("Prescription Code: " + madt, codeFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10);
        codeTable.addCell(cell);
        document.add(codeTable);

        // Pharmacy info
        StringBuilder info = new StringBuilder();
        info.append("Pharmacy Name: ").append(pharmacyName).append("\n");
        info.append("Address: ").append(address).append("\n");
        info.append("License Number: ").append(licenseNumber).append("\n");
        info.append("Phone: ").append(phone).append("\n");
        info.append("Email: ").append(email);

        PdfPTable infoTable = new PdfPTable(1);
        infoTable.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase(info.toString(), infoFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(10);
        infoTable.addCell(cell);
        document.add(infoTable);

        // QR code
        if (qrImage != null) {
            qrImage.setAlignment(Element.ALIGN_CENTER);
            document.add(qrImage);
        }
        document.add(Chunk.NEWLINE);

        // Product table
        PdfPTable pdfTable = new PdfPTable(6);
        pdfTable.setWidthPercentage(100);
        pdfTable.setSpacingBefore(10f);
        pdfTable.setSpacingAfter(10f);
        float[] columnWidths = {1.5f, 3f, 1.2f, 1.5f, 1.5f, 2f};
        pdfTable.setWidths(columnWidths);

        String[] headers = {"Product Code", "Product Name", "Quantity", "Unit Price", "Amount", "Note"};
        for (String headerText : headers) {
            cell = new PdfPCell(new Phrase(headerText, tableHeaderFont));
            cell.setBackgroundColor(new BaseColor(43, 74, 160));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            pdfTable.addCell(cell);
        }

        for (Object[] row : data) {
            pdfTable.addCell(new PdfPCell(new Phrase(row[1] == null ? "" : row[1].toString(), tableCellFont)));
            pdfTable.addCell(new PdfPCell(new Phrase(getProductName(row[1] == null ? "" : row[1].toString()), tableCellFont)));
            pdfTable.addCell(new PdfPCell(new Phrase(row[2] == null ? "" : row[2].toString(), tableCellFont)));
            pdfTable.addCell(new PdfPCell(new Phrase(row[3] == null ? "" : row[3].toString(), tableCellFont)));
            pdfTable.addCell(new PdfPCell(new Phrase(row[4] == null ? "" : row[4].toString(), tableCellFont)));
            pdfTable.addCell(new PdfPCell(new Phrase("", tableCellFont)));
        }
        document.add(pdfTable);

        // Total
        PdfPTable totalTable = new PdfPTable(1);
        totalTable.setWidthPercentage(100);
        cell = new PdfPCell(new Phrase("Total Amount: " + String.format("%,.2f", totalAmount), totalFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingTop(10);
        totalTable.addCell(cell);
        document.add(totalTable);

        document.close();
        JOptionPane.showMessageDialog(this, "Export invoice successfully!");

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error exporting invoice: " + ex.getMessage());
    }
}

    private String getProductName(String masp) {
        String name = "";
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT TENSP FROM SANPHAM WHERE MASP = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, masp);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    name = rs.getString("TENSP");
                }
            }
        } catch (Exception ex) {
            // Nếu lỗi thì trả về rỗng
        }
        return name;
    }

    private void deleteSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
            return;
        }

        String masp = String.valueOf(tableModel.getValueAt(row, 1));
        int soLuong = Integer.parseInt(tableModel.getValueAt(row, 2).toString());

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa sản phẩm này khỏi đơn thuốc?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String deleteSql = "DELETE FROM CTDT WHERE MADT = ? AND MASP = ?";
                try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
                    ps.setString(1, madt);
                    ps.setString(2, masp);
                    ps.executeUpdate();
                }

                String updateTonKho = "UPDATE CHITIETKHO SET SLTONKHO = SLTONKHO + ? WHERE MAVATTU = ?";
                try (PreparedStatement psTonKho = conn.prepareStatement(updateTonKho)) {
                    psTonKho.setInt(1, soLuong);
                    psTonKho.setString(2, masp);
                    psTonKho.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Xóa sản phẩm khỏi đơn thuốc thành công!");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage());
            }
        }
    }
}
