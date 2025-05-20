package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.CustomException.UserAlreadyExistException;
import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.SecurityConfig;
import com.example.gilbertecommerce.Framework.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class LoginService {
    private UserRepo userRepo;
    private SecurityConfig securityConfig;
    public LoginService(UserRepo userRepo, SecurityConfig securityConfig) {
        this.userRepo = userRepo;
        this.securityConfig = securityConfig;
    }
    public boolean checkLogin(LoginInfo loginInfo) throws SQLException {
        LoginInfo loginInfo1 = userRepo.getLoginInfo(loginInfo);
        System.out.println(loginInfo1.getLoginPass());
        System.out.println(loginInfo1.getLoginName());
        String hashed = loginInfo1.getLoginPass();
        if(securityConfig.passwordEncoder().matches(loginInfo.getLoginPass(), hashed)){
            return true;
        }
        return false;
    }
    public LoginInfo getLoginInfo(LoginInfo loginInfo) {
        return userRepo.getLoginInfo(loginInfo);
    }
    public boolean registerUser(LoginInfo loginInfo, User user) {
        //hashing password
        loginInfo.setLoginPass(securityConfig.passwordEncoder().encode(loginInfo.getLoginPass()));
        System.out.println("testing registerUser");
        userRepo.createNewUser(loginInfo, user);
        return true;
    }
    public boolean doesLoginInfoExist(String loginName) {
        try{
            return userRepo.doesLoginInfoExist(loginName);

        } catch (UserAlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }
}
