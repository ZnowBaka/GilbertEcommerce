package com.example.gilbertecommerce.Entity;

public class RegistrationForm {
    private User user = new User();
    private LoginInfo loginInfo = new LoginInfo();
    public LoginInfo getLoginInfo() {
        return loginInfo;
    }
    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
