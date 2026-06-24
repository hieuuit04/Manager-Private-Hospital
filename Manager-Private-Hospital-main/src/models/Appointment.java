/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

public class Appointment {
    private String malich;
    private String mabn;
    private String mabs;
    private Timestamp ngayDat;
    private Timestamp ngayHen;
    private String diaDiem;
    private String trieuChung;
    private String trangThai;
    
    public Appointment() {
    }

    public Appointment(String malich, String mabn, String mabs, Timestamp ngayDat, Timestamp ngayHen,
                       String diaDiem, String trieuChung, String trangThai) {
        this.malich = malich;
        this.mabn = mabn;
        this.mabs = mabs;
        this.ngayDat = ngayDat;
        this.ngayHen = ngayHen;
        this.diaDiem = diaDiem;
        this.trieuChung = trieuChung;
        this.trangThai = trangThai;
    }

    // Getter và Setter
    public String getMaLich() {
        return malich;
    }
    
    public void setMaLich(String malich) {
        this.malich = malich;
    }

    public String getMaBN() {
        return mabn;
    }

    public void setMaBN(String mabn) {
        this.mabn = mabn;
    }

    public String getMaBS() {
        return mabs;
    }

    public void setMaBS(String mabs) {
        this.mabs = mabs;
    }

    public Timestamp getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(Timestamp ngayDat) {
        this.ngayDat = ngayDat;
    }

    public Timestamp getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(Timestamp ngayHen) {
        this.ngayHen = ngayHen;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getTrieuChung() {
        return trieuChung;
    }

    public void setTrieuChung(String trieuChung) {
        this.trieuChung = trieuChung;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}

