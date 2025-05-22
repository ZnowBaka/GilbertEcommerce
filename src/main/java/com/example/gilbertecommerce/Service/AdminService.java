package com.example.gilbertecommerce.Service;

import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final UserService userService;
    private final UserRepo userRepo;
    public AdminService(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }
    public List<User> getAllUsers(){
        return userRepo.getAllUsers();
    }
}
