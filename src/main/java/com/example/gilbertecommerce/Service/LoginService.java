package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Framework.SecurityConfig;
import com.example.gilbertecommerce.Framework.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private SecurityConfig securityConfig;
    @Autowired
    private UserRepo userRepo;

    public boolean checkLogin(String email, String password){
        String hashed = userRepo.
        securityConfig.passwordEncoder().matches(password, hashed);

    }
}
