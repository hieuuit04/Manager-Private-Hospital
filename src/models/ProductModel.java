/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {
    public String maSP;
    public String tenSP;
    public String nhaPP;
    public String dvt;
    public String thanhPhan;
    public String luuY;
    public String cachDung;
    public String baoQuan;
    public Date nsx;
    public Date hsd;
    public Double donGia;
    public int uuDai;
    public String moTa;

//    public ProductModel(String maSP, String tenSP, String nhaPP, String dvt, String thanhPhan,
//                       String luuY, String cachDung, String baoQuan,
//                       Date nsx, Date hsd, double donGia, int uuDai, String moTa) {
//        this.maSP = maSP;
//        this.tenSP = tenSP;
//        this.nhaPP = nhaPP;        
//        this.dvt = dvt;
//        this.thanhPhan = thanhPhan;
//        this.luuY = luuY;
//        this.cachDung = cachDung;
//        this.baoQuan = baoQuan;
//        this.nsx = nsx;
//        this.hsd = hsd;
//        this.donGia = donGia;
//        this.uuDai = uuDai;
//        this.moTa = moTa;
//    }

    public String getMaSP() {
        return maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public String getNhaPP() {
        return nhaPP;
    }

    public String getDvt() {
        return dvt;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public String getLuuY() {
        return luuY;
    }

    public String getCachDung() {
        return cachDung;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public Date getNsx() {
        return nsx;
    }

    public Date getHsd() {
        return hsd;
    }

    public double getDonGia() {
        return donGia;
    }

    public int getUuDai() {
        return uuDai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public void setNhaPP(String nhaPP) {
        this.nhaPP = nhaPP;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }

    public void setLuuY(String luuY) {
        this.luuY = luuY;
    }

    public void setCachDung(String cachDung) {
        this.cachDung = cachDung;
    }

    public void setBaoQuan(String baoQuan) {
        this.baoQuan = baoQuan;
    }

    public void setNsx(String nsxStr) {
        try {
            this.nsx = Date.valueOf(nsxStr);
        } catch (Exception e) {
            this.nsx = null;
        }
    }


    public void setHsd(String hsdStr) {
        try {
            this.hsd = Date.valueOf(hsdStr);
        } catch (Exception e) {
            this.hsd = null;
        }
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public void setUuDai(int uuDai) {
        this.uuDai = uuDai;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    
        
}
    
    