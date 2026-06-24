/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class User {
    private String id, name, phone, email, gender, birthDate, address, role, state, username, password;

    // Constructor đầy đủ cho khi lấy dữ liệu từ DB (đăng nhập, tra cứu)
    public User(String id, String name, String phone, String email, String gender, String birthDate,
                String address, String role, String state, String username, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.state = state;
        this.username = username;
        this.password = password;
    }

    // Constructor không có id, dùng khi đăng ký (vì ID tự sinh trong DB)
    public User(String name, String phone, String email, String gender, String birthDate,
                String address, String role, String username, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getState() { return state; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; }
    public void setState(String state) { this.state = state; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
