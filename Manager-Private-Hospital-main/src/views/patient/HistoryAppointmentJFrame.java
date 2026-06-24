/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.patient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.SQLException;
import javax.swing.JFrame;
import views.patient.HistoryAppointment;

public class HistoryAppointmentJFrame extends JFrame {
    public HistoryAppointmentJFrame(String patientId) throws SQLException, ClassNotFoundException {
        setTitle("Lịch hẹn bệnh nhân");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Co theo kích thước màn hình (đề xuất)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height); // full màn hình
        
        HistoryAppointment historyPanel = new HistoryAppointment(patientId);
        setContentPane(historyPanel);
    }
}