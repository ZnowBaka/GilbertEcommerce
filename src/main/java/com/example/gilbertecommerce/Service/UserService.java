package com.example.gilbertecommerce.Service;


import com.example.gilbertecommerce.Entity.LoginInfo;
import com.example.gilbertecommerce.Entity.User;
import com.example.gilbertecommerce.Framework.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getUserByEmail(LoginInfo loginInfo) {
        User loggedInUser = userRepo.getUserByEmail(loginInfo);
        if(loggedInUser == null){
            return null;
        } else {
            return loggedInUser;
        }
    }

}
