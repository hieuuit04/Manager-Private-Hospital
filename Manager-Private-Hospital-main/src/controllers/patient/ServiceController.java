/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.ServiceModel;
import models.ServiceModel.Service;
import views.patient.ServiceForm;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ServiceController {
    private ServiceForm view;
    private ServiceModel model;

    public ServiceController(ServiceForm view, ServiceModel model) {
        this.view = view;
        this.model = model;

        initController();
        loadAllServices();
    }

    private void initController() {
        getBtnSearch().addActionListener(e -> searchServices());

        // Khi chọn 1 dòng trong bảng, hiển thị chi tiết dịch vụ
        getServiceTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = getServiceTable().getSelectedRow();
                    if (selectedRow != -1) {
                        showServiceDetails(selectedRow);
                    }
                }
            }
        });
    }
    private void showServiceDetails(int selectedRow) {
        DefaultTableModel model = getModel();

        String maDV = (String) model.getValueAt(selectedRow, 0);
        String tenDV = (String) model.getValueAt(selectedRow, 1);
        String moTaDV = (String) model.getValueAt(selectedRow, 2);
        String uuDai = (String) model.getValueAt(selectedRow, 3);
        String donGia = String.valueOf(model.getValueAt(selectedRow, 4));
    }

    private JButton getBtnSearch() {
        return getBtnSearch();
    }

    private JTable getServiceTable() {
        return getServiceTable();
    }

    private JTextField getTfMaDV() {
        return getTfMaDV();
    }

    private JTextField getTfTenDV() {
        return getTfTenDV();
    }

    private JTextField getTfUuDai() {
        return getTfUuDai();
    }

    private JTextField getTfDonGia() {
        return getTfDonGia();
    }

    private DefaultTableModel getModel() {
        return (DefaultTableModel) getServiceTable().getModel();
    }


    private void loadAllServices() {
        try {
            List<Service> services = model.searchServices(null, null, null, null);
            updateTable(services);
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchServices() {
        String maDV = getTfMaDV().getText();
        String tenDV = getTfTenDV().getText();
        String uuDai = getTfUuDai().getText();
        String donGia = getTfDonGia().getText();

        try {
            List<Service> services = model.searchServices(maDV, tenDV, uuDai, donGia);
            updateTable(services);
        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Service> services) {
        DefaultTableModel tableModel = getModel();
        tableModel.setRowCount(0);
        for (Service s : services) {
            Object[] row = {
                    s.getMaDV(),
                    s.getTenDV(),
                    s.getMoTaDV(),
                    s.getUuDai(),
                    s.getDonGia()
            };
            tableModel.addRow(row);
        }
    }
    
}

