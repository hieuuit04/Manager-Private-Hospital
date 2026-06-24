/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.patient;

import models.AppointmentModel;
import models.Appointment;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;

public class AppointmentController {
    private Appointment view;

    public AppointmentController() {
        // Nếu bạn không dùng AppointmentView trong HistoryAppointment thì có thể để trống constructor
    }

    public AppointmentController(Appointment view) {
        this.view = view;
    }

    public List<String> fetchDoctors() {
        try {
            return AppointmentModel.getDoctors();
        } catch (Exception e) {
            e.printStackTrace();
            if(view != null) showMessage("Lỗi khi tải danh sách bác sĩ.");
            return null;
        }
    }

    public void handleAppointment(String patientId, String selectedDoctor, String ngayHenStr,
                                  String diadiem, String trieuchung) {
        if (selectedDoctor == null || selectedDoctor.isEmpty()) {
            showMessage("Vui lòng chọn bác sĩ.");
            return;
        }
        if (ngayHenStr.isEmpty() || diadiem.isEmpty() || trieuchung.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ngayHen;
        try {
            ngayHen = LocalDateTime.parse(ngayHenStr, formatter);
        } catch (Exception ex) {
            showMessage("Định dạng ngày hẹn không đúng. Vui lòng nhập theo định dạng yyyy-MM-dd HH:mm:ss");
            return;
        }

        String doctorId = selectedDoctor.split(" - ")[0];

        try {
            boolean success = AppointmentModel.saveAppointment(patientId, doctorId,
                    Timestamp.valueOf(ngayHen), diadiem, trieuchung);
            if (success) {
                showMessage("Đặt lịch thành công! Vui lòng chờ xác nhận.");
            } else {
                showMessage("Đặt lịch thất bại. Vui lòng thử lại.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Lỗi khi lưu lịch hẹn.");
        }
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    // --- MỚI THÊM ĐỂ KHỚP VỚI HistoryAppointment ---

    public List<Appointment> getAppointments(String patientId) throws Exception {
        return AppointmentModel.getAppointmentsByPatient(patientId);
    }


    public boolean cancelAppointment(String appointmentId) throws Exception {
        return AppointmentModel.updateStatus(appointmentId, "Đã hủy");
    }


    public boolean deleteAppointment(String appointmentId) throws Exception {
        return AppointmentModel.deleteAppointment(appointmentId);
    }

    public boolean updateAppointment(String appointmentId, String ngayHenStr,
                                     String diaDiem, String trieuChung) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ngayHen;
        try {
            ngayHen = LocalDateTime.parse(ngayHenStr, formatter);
        } catch (Exception ex) {
            throw new Exception("Định dạng ngày hẹn không đúng.");
        }

        return AppointmentModel.updateAppointmentDetails(
                appointmentId,
                Timestamp.valueOf(ngayHen),
                diaDiem,
                trieuChung);
    }
}

