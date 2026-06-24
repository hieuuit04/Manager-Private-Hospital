/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package views.staff;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

/**
 *
 * @author Admin
 */
public class BienBan extends JFrame {

    public BienBan() {
            try {
                String url = "https://docs.google.com/document/d/1Ypv4akATINv5TCgx2M7y5a6w3SIoigQBUvqxYLBJVX0/edit?usp=sharing";
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Không thể mở liên kết!");
            }
    }
}
