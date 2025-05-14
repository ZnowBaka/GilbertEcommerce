package com.example.gilbertecommerce.Entity;

public class LoginInfo {
    private int loginId;
    private String loginName;
    private String loginPass;

    public LoginInfo(int loginId, String loginName, String loginPass) {}
    public LoginInfo(){}
    public int getLoginId() {
        return loginId;
    }
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getLoginPass() {
        return loginPass;
    }
    public void setLoginPass(String loginPass) {
        this.loginPass = loginPass;
    }
}
