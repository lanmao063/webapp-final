package com.neu.webapp.vo;

public class LoginResponse {

    private Long userId;
    private String username;
    private String role;
    private String realName;
    private String phone;
    private String address;

    public LoginResponse(Long userId, String username, String role, String realName, String phone, String address) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.realName = realName;
        this.phone = phone;
        this.address = address;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
