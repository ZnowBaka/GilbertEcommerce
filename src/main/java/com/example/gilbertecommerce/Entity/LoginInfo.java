package com.example.gilbertecommerce.Entity;

public class LoginInfo {
    private int loginId;
    private String loginEmail;
    private String loginPass;

    public LoginInfo(int loginId, String loginEmail, String loginPass) {}
    public LoginInfo(int loginId, String loginPass) {}
    public LoginInfo(){}
    public int getLoginId() {
        return loginId;
    }
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    public String getLoginEmail() {
        return loginEmail;
    }
    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }
    public String getLoginPass() {
        return loginPass;
    }
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }
}
